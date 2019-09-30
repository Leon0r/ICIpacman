//ICI Grupo13 Marcos Garcia Garcia y Rodrigo Manuel Perez Ruiz

package es.ucm.fdi.ici.c1920.practica0.grupoYY;

import java.util.EnumMap;
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
	
	
	public int getAheadPacmanNode(Game game) {
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
		
		// TODO: Si no puede ir al punto mas delante de pacman, decide otro en funcion de X (DE MOMENTO RANDOM, REVISAR)
		/*
		switch (lastMove) {
		case LEFT:
			break;
			
		case RIGHT:
			break;
			
		case DOWN:
			break;
			
		case UP:
			break;
		}
		*/
		
		result = neighbours[rnd.nextInt(neighbours.length)];
		return result;
	}
	
	public int getAreaNode(GHOST ghostType) {
		
		switch (ghostType) {
		
		case BLINKY:
			return 1089;
		case INKY:
			return 1040;
		case PINKY:
			return 78;
		case SUE:
			return 120;
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
				if(dangerPacMan) { // Si hay peligro por que PC está muy cerca de una power pill
					
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
					int node = getAreaNode(ghostType);
					
					double dToPac = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.EUCLID);
					double dToAreaNode = game.getDistance(game.getPacmanCurrentNodeIndex(), node, DM.EUCLID);
					
					if(dToAreaNode > limitNearZone) { // Vamos a nuestra zona
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
					else {	// Si estamos en la zona y cerca de pacman nos alejamos de él
						moves.put(
								ghostType, 
								game.getApproximateNextMoveTowardsTarget(
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
							
							int pmNodeIndex = getAheadPacmanNode(game);
							
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
