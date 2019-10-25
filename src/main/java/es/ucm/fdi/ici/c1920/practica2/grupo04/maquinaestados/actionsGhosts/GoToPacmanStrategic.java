package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts;

import java.util.Random;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToPacmanStrategic extends Action {

	GHOST ghostType;
	
	public GoToPacmanStrategic(GHOST gType) {
		this.ghostType = gType;
	}
	
	public MOVE executeAction() {
		
		MOVE res = null;
		
		switch(ghostType) {
		
		case BLINKY:
			res = blinkyMovement(g);
			break;
		case INKY:			
			res = inkyMovement(g);
			break;
		case PINKY:
			res = pinkyMovement(g);
			break;
		case SUE:
			res = sueMovement(g);
			break;
		default:
			break;		
		}
		
		return res;
	}
	
	MOVE blinkyMovement(Game g) {		
		MOVE res = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(ghostType),  DM.EUCLID);		
		return res;
	}
	
	MOVE inkyMovement(Game g) {
		
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
	
	MOVE pinkyMovement(Game g) {
		
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
	
	MOVE sueMovement(Game g) {
		
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
}
