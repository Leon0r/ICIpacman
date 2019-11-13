package es.ucm.fdi.ici.c1920.practica3.grupo04;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public final class Ghosts extends GhostController {

	private static final Double PACMAN_IS_NEAR = 15.0;
	private static final Double PACMAN_IS_FAR = 10.0;
	
	private static final Integer TIME_LIMIT  = 200;
	
	private int[] timersAggressive = new int[4];
	private int[] timersStrategic = new int[4];
	int level = 0;
	
	FuzzyEngine fe;
	HashMap<String, Double> input;
	HashMap<String, Double> output;
	
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	public Ghosts() {
		fe = new FuzzyEngine(FuzzyEngine.FUZZY_CONTROLLER.GHOSTS);
		input = new HashMap<String,Double>();
		output = new HashMap<String,Double>();
		
		for (int i = 0; i<4; i++) {
			timersAggressive[i] = 0;
			timersStrategic[i] = 0;
		}
	}
	
	private void fillInput(Game game, GHOST ghostType, HashMap<String, Double> input) {
		
		// PACMAN DISTANCE
		input.put("PACMANdistance", game.getDistance(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), DM.PATH));
		
		// NEAREST POWER PILL DISTANCE
		double minDist = Double.MAX_VALUE;
		double auxDist = 0;		
		for (int i = 0; i<game.getActivePowerPillsIndices().length; i++) {
			auxDist = game.getDistance(game.getGhostCurrentNodeIndex(ghostType), game.getActivePowerPillsIndices()[i], DM.PATH);
			if (minDist > auxDist) 
				minDist = auxDist;
			
		}		
		input.put("NearestPPdistance", minDist);		
		
		
		// NEAREST POWER PILL DISTANCE TO PACMAN
		minDist = Double.MAX_VALUE;
		auxDist = 0;		
		for (int i = 0; i<game.getActivePowerPillsIndices().length; i++) {
			auxDist = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices()[i], DM.PATH);
			if (minDist > auxDist) 
				minDist = auxDist;
		}		
		input.put("NearestPPdistanceToPACMAN", minDist);
		
		
		// NEAREST EDIBLEGHOST DISTANCE		
		minDist = Double.MAX_VALUE;
		auxDist = 0;		
		for (GHOST gType : GHOST.values()) {
			if(gType != ghostType && game.isGhostEdible(gType) && game.getGhostLairTime(gType) <= 0) {
				auxDist = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(gType), DM.PATH);
				if (minDist > auxDist) {
					minDist = auxDist;
				}
			}
		}		
		input.put("NearestEDIBLEGHOSTdistance", minDist);
		

		// EDIBLE TIME
		input.put("EDIBLEtime", (double) game.getGhostEdibleTime(ghostType));		
	}
	
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
    	
    	if(level != game.getCurrentLevel()) {
			level = game.getCurrentLevel();
			for(int i = 0; i < 4; i++) {
				timersAggressive[i] = 0;
				timersStrategic[i] = 0;
			}
		}
    	
    	if(game.wasPacManEaten()) {
    		for(int i = 0; i < 4; i++) {
				timersAggressive[i] = 0;
				timersStrategic[i] = 0;
			}
    	}
    	
    	for(GHOST ghostType: GHOST.values()) {
	        input.clear(); 
	        output.clear();
	        
	        // Input
	        fillInput(game, ghostType, input);	        
	        
	        // Evaluate
	        fe.evaluate("FuzzyGhosts", input, output);
	        
			// Logic
			moves.put(ghostType, logic(game, ghostType, output, timeDue));				
    	}
    	
    	return moves;
    }
    
    private MOVE logic(Game game, GHOST ghostType, HashMap<String,Double> output, long timeDue) {
    	
    	if(!game.isGhostEdible(ghostType)) {
    		
    		if((output.get("goToPacman") > PACMAN_IS_NEAR || timersStrategic[ghostType.ordinal()] >= TIME_LIMIT) && timersAggressive[ghostType.ordinal()] < TIME_LIMIT) {
    			timersAggressive[ghostType.ordinal()]++;
    			timersStrategic[ghostType.ordinal()] = 0;
    			return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType), DM.EUCLID);
    		}
    		else if ((output.get("goToPacman") < PACMAN_IS_NEAR || timersAggressive[ghostType.ordinal()] >= TIME_LIMIT) && timersStrategic[ghostType.ordinal()] < TIME_LIMIT) {
    			timersStrategic[ghostType.ordinal()]++;
    			timersAggressive[ghostType.ordinal()] = 0;
    			return ghostStrategicMovement(ghostType, game);
    		}
    	}
    	else {
    		if(nonEdibleGhosts(game, ghostType)) {
    			// Huyo de pacman hacia mi colega fuertote fantasma
    			return fleeStrategic(game, ghostType);
    		}
    		else if(output.get("fleeFromPacman") > PACMAN_IS_FAR)
    		{
    			// Huimos de pacman hacia mi zona
    			return fleeToZone(game, ghostType);    			    		
    		}
    		else
    		{
    			// Huyo de pacman directamente
    			return game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType),  DM.EUCLID);
    		}
    	}
    	
    	// Por seguridad
		return randomMovement(game);
    }
    
    private boolean nonEdibleGhosts(Game game, GHOST ghostType) {
    	
    	int nonEdibleGhosts = 0;
		
		// Añadimos el total de fantasmas no comestibles
		for (GHOST gType : GHOST.values())
			if(gType != ghostType && !game.isGhostEdible(gType) && game.getGhostLairTime(gType) <= 0)
				nonEdibleGhosts++;		
		
		if(nonEdibleGhosts > 0)
			return true;
		else
			return false;
    }
    
	private MOVE randomMovement(Game game) {
		Random rnd = new Random();
		return MOVE.values()[rnd.nextInt(4)];
	}
	
	private MOVE ghostStrategicMovement(GHOST ghostType, Game g) {
		MOVE res = null;
		
		switch(ghostType) {
		
		case BLINKY:
			res = blinkyMovement(g,ghostType);
			break;
		case INKY:			
			res = inkyMovement(g,ghostType);
			break;
		case PINKY:
			res = pinkyMovement(g,ghostType);
			break;
		case SUE:
			res = sueMovement(g,ghostType);
			break;
		default:
			break;		
		}
		
		return res;
	}
	
	MOVE blinkyMovement(Game g, GHOST ghostType) {		
		MOVE res = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(ghostType),  DM.EUCLID);		
		return res;
	}
	
	MOVE inkyMovement(Game g, GHOST ghostType) {
		
		int nodePacman = g.getPacmanCurrentNodeIndex();
		
		boolean limit = false;
		
		while (!limit) {
			int nodes[] = g.getNeighbouringNodes(nodePacman, g.getPacmanLastMoveMade());
			
			MOVE move = g.getPacmanLastMoveMade();
			boolean found = false;
			int i = 0;
			
			switch (move) {
			
				case UP:
					while(!found && i<nodes.length) {
						
						if(g.getNodeYCood(nodePacman) > g.getNodeYCood(nodes[i])) {	// Si el nodo tiene coordenada Y menor, y por tanto esta mas arriba							
							nodePacman = nodes[i];
							found = true;							
						}
						else
							i++;
					}
					
					if(!found)
						limit = true;
					
					break;
					
				case DOWN:
					while(!found && i<nodes.length) {
						
						if(g.getNodeYCood(nodePacman) < g.getNodeYCood(nodes[i])) {							
							nodePacman = nodes[i];
							found = true;							
						}
						else
							i++;
					}
					
					if(!found)
						limit = true;
					
					break;
					
				case LEFT:
					while(!found && i<nodes.length) {
						
						if(g.getNodeXCood(nodePacman) < g.getNodeXCood(nodes[i])) {							
							nodePacman = nodes[i];
							found = true;							
						}
						else
							i++;
					}
					
					if(!found)
						limit = true;
					
					break;
				case RIGHT:
					while(!found && i<nodes.length) {
						
						if(g.getNodeXCood(nodePacman) > g.getNodeXCood(nodes[i])) {							
							nodePacman = nodes[i];
							found = true;							
						}
						else
							i++;
					}
					
					if(!found)
						limit = true;
					
					break;
			default:
				break;
			}
		}
		
		
		MOVE res = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), nodePacman, g.getGhostLastMoveMade(ghostType),  DM.EUCLID);		
		return res;
	}
	
	MOVE pinkyMovement(Game g, GHOST ghostType) {
		
		MOVE lastMove = g.getPacmanLastMoveMade();
		
		int [] neighbours = g.getNeighbouringNodes(g.getPacmanCurrentNodeIndex(), lastMove);
		
		int left = -1;
		int right = -1;
		int down = -1;
		int up = -1;
		
		int pmNode = g.getPacmanCurrentNodeIndex();
		
		for(int i = 0; i< neighbours.length; i++) {
			if (g.getNodeXCood(neighbours[i]) < g.getNodeXCood(pmNode))
				left = neighbours[i];
			else if (g.getNodeXCood(neighbours[i]) > g.getNodeXCood(pmNode))
				right = neighbours[i];
			else if (g.getNodeYCood(neighbours[i]) < g.getNodeYCood(pmNode))
				up = neighbours[i];
			else if (g.getNodeYCood(neighbours[i]) > g.getNodeYCood(pmNode))
				down = neighbours[i];
		}
		
		int result = -1;
		
		switch (lastMove) {
		case LEFT:
			result = left;
			break;
			
		case RIGHT:
			result = right;
			break;
			
		case DOWN:
			result = down;
			break;
			
		case UP:
			result = up;
			break;
		default:
			break;
		
		}
		
		if(result!=-1) {
			MOVE move = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), result, g.getGhostLastMoveMade(ghostType),  DM.EUCLID);
			return move;
		}
		
		// Si no es posible devolver el delantero...
		// A un lado y al otro
		int [] pills = g.getActivePillsIndices();
		
		int Y = g.getNodeYCood(g.getGhostCurrentNodeIndex(ghostType));
		int X = g.getNodeXCood(g.getGhostCurrentNodeIndex(ghostType));
		
		
		int pillsUp = 0;
		int pillsDown = 0;
		int pillsLeft = 0;
		int pillsRight = 0;
		
		switch (lastMove) {
		case LEFT:
			
			for(int i = 0; i<pills.length; i++) {
				
				if (g.getNodeYCood(pills[i]) < Y)
					pillsUp++;
				else
					pillsDown++;
				
			}
			
			if(pillsUp > pillsDown)
				result = down;
			else
				result = up;
			
			break;
			
		case RIGHT:
			
			for(int i = 0; i<pills.length; i++) {
				
				if (g.getNodeYCood(pills[i]) < Y)
					pillsUp++;
				else
					pillsDown++;
				
			}
			
			if(pillsUp > pillsDown)
				result = down;
			else
				result = up;
			break;
			
		case DOWN:
			for(int i = 0; i<pills.length; i++) {
				
				if (g.getNodeXCood(pills[i]) < X)
					pillsLeft++;
				else
					pillsRight++;
				
			}
			
			if(pillsLeft > pillsRight)
				result = left;
			else
				result = right;
			break;
			
		case UP:
			for(int i = 0; i<pills.length; i++) {
				
				if (g.getNodeXCood(pills[i]) < X)
					pillsLeft++;
				else
					pillsRight++;				
			}
			
			if(pillsLeft > pillsRight)
				result = left;
			else
				result = right;
			break;
		default:
			break;
		}
		
		Random rnd = new Random();
		if(result == -1)
			result = neighbours[rnd.nextInt(neighbours.length)];
		
		MOVE move = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), result, g.getGhostLastMoveMade(ghostType),  DM.EUCLID);
		
		return move;		
	}
	
	MOVE sueMovement(Game g, GHOST ghostType) {
		
		int PPnodes[] = g.getActivePowerPillsIndices();
		
		double distances[] = new double[PPnodes.length];
		
		for(int i = 0; i<distances.length; i++) {			
			distances[i] = g.getDistance(g.getPacmanCurrentNodeIndex(), PPnodes[i], DM.EUCLID);
		}
		
		MOVE move = MOVE.NEUTRAL;
		
		// Este if y el else ya no sería aqui porque cambiaria de estado
		if(PPnodes.length > 0) {
			int lessDistancePPindex = 0;
			double lessDistance = distances[0];
			
			for(int i = 1; i<distances.length; i++) {
				if(distances[i] < lessDistance) {
					lessDistance = distances[i];
					lessDistancePPindex = i;
				}
			}
			move = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), PPnodes[lessDistancePPindex], g.getGhostLastMoveMade(ghostType),  DM.EUCLID);
		}
		
		else {
			Random rnd = new Random();			
			move = MOVE.values()[rnd.nextInt(4)];			
		}
		
		return move;
	}

	public MOVE fleeStrategic(Game g, GHOST ghostType) {
		
		double distances[] = new double[4];
		
		for(int i = 0; i < distances.length; i++) {			
			distances[i] = g.getDistance(g.getPacmanCurrentNodeIndex(), g.getGhostCurrentNodeIndex(GHOST.values()[i]), DM.EUCLID);
		}
		
		int lessDistanceGindex = -1;
		double lessDistance = Double.MAX_VALUE;
		
		for(GHOST gType: GHOST.values()) {
			if(!g.isGhostEdible(gType) && gType != ghostType) { // Si edible y no yo
				if(distances[gType.ordinal()] < lessDistance) { // Si esta mas cerca que el mas cercano
					lessDistance = distances[gType.ordinal()];
					lessDistanceGindex = gType.ordinal();
				}
			}
		}
	
		MOVE moveToGhost = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), g.getGhostCurrentNodeIndex(GHOST.values()[lessDistanceGindex]), g.getGhostLastMoveMade(ghostType), DM.EUCLID);
		MOVE moveToPacman = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(ghostType), DM.EUCLID);
		
		return compareMoves(moveToGhost, moveToPacman,g.getGhostCurrentNodeIndex(GHOST.values()[lessDistanceGindex]), g, ghostType);
	}
	
	private MOVE compareMoves(MOVE g, MOVE p, int otherNode ,Game game, GHOST ghostType) {
		
		// Si el movimiento que tengo que hacer me lleva hacia Pacman, buscamos una alternativa
		if(g == p) {
			
			MOVE move = MOVE.NEUTRAL;
			
			int Xmine = game.getNodeXCood(game.getGhostCurrentNodeIndex(ghostType));
			int Xghost = game.getNodeXCood(otherNode);
			int Ymine = game.getNodeYCood(game.getGhostCurrentNodeIndex(ghostType));
			int Yghost = game.getNodeYCood(otherNode);
			
			switch (g) {
			case UP:
				
				if(Xmine < Xghost) 
					move = MOVE.RIGHT;
				else
					move = MOVE.LEFT;
				
				break;
			case DOWN:
				
				if(Xmine < Xghost) 
					move = MOVE.RIGHT;
				else
					move = MOVE.LEFT;
				
				break;
			case LEFT:
				if(Xmine < Xghost) 
					move = MOVE.RIGHT;
				else
					move = MOVE.LEFT;
				break;
			case RIGHT:
				if(Ymine < Yghost) 
					move = MOVE.DOWN;
				else
					move = MOVE.UP;
				break;
			default:
				if(Ymine < Yghost) 
					move = MOVE.DOWN;
				else
					move = MOVE.UP;
				break;
			}
			
			return move;
		}
		else {
			return g;
		}
		
	}
	
	public int getAreaNode(Game game, GHOST ghostType) {
		
		int[] powerPillIndices = game.getActivePowerPillsIndices();
		
		Vector<Integer> ghosts = new Vector<Integer>(0,1); 
		
		for(int k = 0; k < powerPillIndices.length && k<4; k++) {
			ghosts.add(powerPillIndices[k]);
		}
		
		if(ghosts.size() < 4) {
			int[] powerPillDownIndices = game.getPowerPillIndices();
			
			boolean skip = false;
			
			for(int h = 0; h<powerPillDownIndices.length && ghosts.size() < 4; h++) {
				skip = false;
				for(int j = 0; j<ghosts.size(); j++) {
					
					if(!skip) {
						if(powerPillDownIndices[h] == ghosts.elementAt(j))
							skip= true;						
					}				
				}
				
				if(!skip)
					ghosts.add(powerPillDownIndices[h]);
			}
		}
		
		switch (ghostType) {
		
			case BLINKY:
				return ghosts.elementAt(0);
			case INKY:
				return ghosts.elementAt(1);
			case PINKY:
				return ghosts.elementAt(2);
			case SUE:
				return ghosts.elementAt(3);
		}
		
		return 0;
	}
	
	public MOVE fleeToZone(Game g, GHOST ghostType) {
		
		int zoneNode = getAreaNode(g, ghostType);
		MOVE ghostMove = g.getNextMoveAwayFromTarget(g.getGhostCurrentNodeIndex(ghostType), zoneNode, g.getGhostLastMoveMade(ghostType),  DM.EUCLID);
		MOVE moveToPacman = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(ghostType), DM.EUCLID);
		
		
		return compareMoves(ghostMove, moveToPacman, zoneNode, g, ghostType);
	}
	
}