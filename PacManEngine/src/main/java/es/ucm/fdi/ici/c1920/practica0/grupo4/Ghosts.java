//ICI Grupo13 Marcos Garcia Garcia y Rodrigo Manuel Perez Ruiz

package es.ucm.fdi.ici.c1920.practica0.grupo4;

import java.util.EnumMap;
import java.util.Vector;
import java.util.Random;

import pacman.game.Game;
import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public final class Ghosts extends GhostController{

	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	private Random rnd = new Random();
	private MOVE[] allMoves = MOVE.values();
	
	double limitPPills = 10;
	double limitNearPacMan = 10;
	double limitNearZone = 40;
	double limitNearPacmanEdible = 20;
	
	
	public int getAheadPacmanNode(Game game, GHOST ghostType) {
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		int [] neighbours = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex(), lastMove);
		
		int left = -1;
		int right = -1;
		int down = -1;
		int up = -1;
		
		int pmNode = game.getPacmanCurrentNodeIndex();
		
		for(int i = 0; i< neighbours.length; i++) {
			if (game.getNodeXCood(neighbours[i]) < game.getNodeXCood(pmNode))
				left = neighbours[i];
			else if (game.getNodeXCood(neighbours[i]) > game.getNodeXCood(pmNode))
				right = neighbours[i];
			else if (game.getNodeYCood(neighbours[i]) < game.getNodeYCood(pmNode))
				up = neighbours[i];
			else if (game.getNodeYCood(neighbours[i]) > game.getNodeYCood(pmNode))
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
		
		}
		
		if(result!=-1)
			return result;
		
		// TODO: Si no puede ir al punto mas delante de pacman, decide otro en funcion de la cantidad de pills que haya 
		// A un lado y al otro
		int [] pills = game.getActivePillsIndices();
		
		int Y = game.getNodeYCood(game.getGhostCurrentNodeIndex(ghostType));
		int X = game.getNodeXCood(game.getGhostCurrentNodeIndex(ghostType));
		
		
		int pillsUp = 0;
		int pillsDown = 0;
		int pillsLeft = 0;
		int pillsRight = 0;
		
		switch (lastMove) {
		case LEFT:
			
			for(int i = 0; i<pills.length; i++) {
				
				if (game.getNodeYCood(pills[i]) < Y)
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
				
				if (game.getNodeYCood(pills[i]) < Y)
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
				
				if (game.getNodeXCood(pills[i]) < X)
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
			break;
		}
		
		if(result == -1)
			result = neighbours[rnd.nextInt(neighbours.length)];
		
		return result;
	}
	
	public int getAreaNode(GHOST ghostType, Game game) {
		
		int[] powerPillIndices = game.getActivePowerPillsIndices();
		
		Vector<Integer> ghosts = new Vector(0,1); 
		
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
	 
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		//System.out.println("X: " + game.getNodeXCood(game.getPacmanCurrentNodeIndex()) + 
			//			   "Y: " + game.getNodeYCood(game.getPacmanCurrentNodeIndex()));
		
		System.out.println(game.getPacmanCurrentNodeIndex());
		
		moves.clear();
		
		int[] powerPillIndices = game.getActivePowerPillsIndices();
		
		boolean dangerPacMan = false;
		
		// Si pacman esta muy cerca de las pills entonces es peligroso y huyen
		for(int i = 0; i < powerPillIndices.length; i++) {
			double d = game.getDistance(game.getPacmanCurrentNodeIndex(), powerPillIndices[i], DM.EUCLID);
						
			if(d < limitPPills) {
				dangerPacMan = true;
			}
		}
		
		// Recorre todos los fantasmas
		for(GHOST ghostType : GHOST.values()) {
			if(game.doesGhostRequireAction(ghostType)) {
				if(dangerPacMan) { // Si hay peligro por que PC est� muy cerca de una power pill
					
					moves.put(
					ghostType, 
					game.getApproximateNextMoveAwayFromTarget(
							game.getGhostCurrentNodeIndex(ghostType),
							game.getPacmanCurrentNodeIndex(), 
							game.getGhostLastMoveMade(ghostType), 
							DM.EUCLID ));
					
				}
				else if(game.isGhostEdible(ghostType)) // Si me pueden comer, miro si estoy cerca de mi zona y de pacman
				{
					int node = getAreaNode(ghostType, game);
					
					double dToPac = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.EUCLID);
					double dToAreaNode = game.getDistance(game.getPacmanCurrentNodeIndex(), node, DM.EUCLID);
					
					if(dToPac < limitNearPacMan) {
						moves.put(
								ghostType, 
								game.getApproximateNextMoveAwayFromTarget(
										game.getGhostCurrentNodeIndex(ghostType),
										game.getPacmanCurrentNodeIndex(), 
										game.getGhostLastMoveMade(ghostType), 
										DM.EUCLID ));
					}
					else if(dToAreaNode > limitNearZone) { // Vamos a nuestra zona
						moves.put(
								ghostType, 
								game.getApproximateNextMoveTowardsTarget(
										game.getGhostCurrentNodeIndex(ghostType),
										node, 
										game.getGhostLastMoveMade(ghostType), 
										DM.EUCLID ));
					}
					else if(dToPac > limitNearPacmanEdible){	// Si estamos en la zona pero lejos de pacman, nos movemos random					
						moves.put(
								ghostType, 
								allMoves[rnd.nextInt(allMoves.length)]);
					}
					else {	// Si estamos en la zona y cerca de pacman nos alejamos de �l
						moves.put(
								ghostType, 
								game.getApproximateNextMoveAwayFromTarget(
										game.getGhostCurrentNodeIndex(ghostType),
										game.getPacmanCurrentNodeIndex(), 
										game.getGhostLastMoveMade(ghostType), 
										DM.EUCLID ));
					}
				}
				else { // Si no, van a por su objetivo
					
					if(ghostType == GHOST.BLINKY || ghostType == GHOST.INKY) {	// Si son los agresivos se dirigen hacia PM
						
						moves.put(
								ghostType, 
								game.getApproximateNextMoveTowardsTarget(
										game.getGhostCurrentNodeIndex(ghostType),
										game.getPacmanCurrentNodeIndex(), 
										game.getGhostLastMoveMade(ghostType), 
										DM.EUCLID ));
						
					}
					else {	// Si son los otros dos no persiguen a pacman sino a alguna posicion delantera a el
						
						// Si esta muy cerca de PacMan, va hacia el
						double d = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.EUCLID);
						if(d < limitNearPacMan) {
							moves.put(
									ghostType, 
									game.getApproximateNextMoveTowardsTarget(
											game.getGhostCurrentNodeIndex(ghostType),
											game.getPacmanCurrentNodeIndex(), 
											game.getGhostLastMoveMade(ghostType), 
											DM.EUCLID ));
						} // Si no
						else 
						{
							
							int pmNodeIndex = getAheadPacmanNode(game,ghostType);
							
							moves.put(
									ghostType, 
									game.getApproximateNextMoveTowardsTarget(
											game.getGhostCurrentNodeIndex(ghostType),
											pmNodeIndex, 
											game.getGhostLastMoveMade(ghostType), 
											DM.EUCLID ));
						}						
					}
				}
			}
		}
		
	return moves;
	}	
}