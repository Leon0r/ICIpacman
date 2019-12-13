package es.ucm.fdi.ici.c1920.practica4.grupo04;

import java.util.EnumMap;

import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

import java.util.ArrayList;
import es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager.*;


public final class Ghosts extends GhostController{

	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	//GhostCase lastCase[] = new GhostCase[4];
	
	ArrayList<GhostCase>[] lastCase = new ArrayList[4]; 	  
	GhostCase c = null;
	
	// Numero minimo de cruces que se han superado sin morir
	// Esto importa cuando estás huyendo
	int crossesSurpassed = 10;
	
	// Si estas atacando lo que hacemos es guardar los ultimos 10 casos de todos los fantasmas, al morir pacman, 
	// hay que dar más importancia a los casos del fantasma que ha matado
	
	public Ghosts() {
		for(int i = 0; i < 4; i++) {
			lastCase[i] = new ArrayList<GhostCase>();			
		}
	}
	 
	
	@Override
	public void preCompute(String opponent) {
		GhostDataBase.setOpponentGhostName(opponent);		
		GhostDataBase.readGhostCases();
    }
    
	@Override
    public  void postCompute() {		
		GhostDataBase.printGhostCases();
    }
	
	private void generateCase(Game game) {
		
		// VER EL ESTADO DEL JUEGO
		
		double[] distanceToPacman = new double[4];
		boolean[] edibleGhosts = new boolean[4];
		boolean[] activePPills = new boolean[4];
		double [][] distanceToPPills = new double[4][4];
		double [] distancePCToPPills = new double[4];
		int nearestPPillToPacman;
		
		for(int i = 0; i < 4; i++) {
			distanceToPacman[i] = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.values()[i]), game.getPacmanCurrentNodeIndex(),
					game.getGhostLastMoveMade(GHOST.values()[i]), DM.PATH);
			
			edibleGhosts[i] = game.isGhostEdible(GHOST.values()[i]);
			activePPills[i] = game.isPowerPillStillAvailable(i);
			
			distancePCToPPills[i] = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getPowerPillIndices()[i],
					game.getPacmanLastMoveMade(), DM.PATH);
			
			for(int k = 0; k < 4; k++) {
				distanceToPPills[i][k] = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.values()[i]), game.getPowerPillIndices()[k],
						game.getGhostLastMoveMade(GHOST.values()[i]), DM.PATH);
			}
			
		}
		
		nearestPPillToPacman = game.getPowerPillIndices()[0];
		double dist = distancePCToPPills[0];
		for(int i = 1; i < 4; i++) {
			if(distancePCToPPills[i] > dist) {
				dist = distancePCToPPills[i];
				nearestPPillToPacman = game.getPowerPillIndices()[i];
			}
		}
		
		c = new GhostCase(distanceToPacman, edibleGhosts, distanceToPPills, distancePCToPPills, activePPills, nearestPPillToPacman, 0);
		
	}
	
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		for(GHOST ghostType : GHOST.values()) {
			
			if(game.wasGhostEaten(ghostType)) {
				// Si se han comido a este fantasma, quitamos los casos superados durante esa "vida"
				lastCase[ghostType.ordinal()].clear();
			}
			
			if(game.doesGhostRequireAction(ghostType)) {
				
				if(lastCase[ghostType.ordinal()].size() > crossesSurpassed) {
					GhostDataBase.addGeneratedCase(lastCase[ghostType.ordinal()].get(crossesSurpassed - 1));					
				}
				
				generateCase(game);
				MOVE move = GhostDataBase.compareGhostCase(c, game, ghostType.ordinal());
				
				moves.put(ghostType, move);
				
				c.movement = move;
				lastCase[ghostType.ordinal()].add(0, c);		
			}
		}		
	return moves;
	}	
}
