package es.ucm.fdi.ici.c1920.practica4.grupo04;

import java.util.EnumMap;
import java.util.Random;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.MyFSMGhosts;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.StateEngineImpl;
import es.ucm.fdi.ici.c1920.practica4.grupo04.auxiliaryCode.DataGetter;
import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

import java.util.ArrayList;
import java.util.List;



import es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager.*;


public final class MsPacMan extends PacmanController {

	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);

	ArrayList<GhostCase>[] lastCase = new ArrayList[4]; 	  
	GhostCase c = null;

	// Numero minimo de cruces que se han superado sin morir
	int crossesSurpassed = 10;

	// Si estas atacando lo que hacemos es guardar los ultimos 10 casos de todos los fantasmas, al morir pacman, 
	// hay que dar más importancia a los casos del fantasma que ha matado
	public MsPacMan() {
		for(int i = 0; i < 4; i++) {
			lastCase[i] = new ArrayList<GhostCase>();			
		}
	}

	@Override
	public void preCompute(String opponent) {		
		PacmanDataBase.setOpponentGhostName(opponent);		
		PacmanDataBase.readPacmanCases();
	}

	@Override
	public  void postCompute() {		
		PacmanDataBase.printPacmanCases();
	}

	
	private void generateCase(Game game) {

		// VER EL ESTADO DEL JUEGO
		boolean[] edibleGhosts = new boolean[4];
		int [] characterIndex = new int[5];
		int [] characterLastMove = new int[5];
		int[] activePPills = new int[4];
		int nearestPPillToPacman = -1;
		int nonEdibleGhosts = 0;
		
		MOVE movement; // solucion
		
		
		double[] distanceToPacman = new double[4];
		boolean[] edibleGhosts = new boolean[4];
		double [][] distanceToPPills = new double[4][4];
		double [] distancePCToPPills = new double[4];
		int nearestPPillToPacman;

		for(int i = 0; i < 4; i++) {
			edibleGhosts[i] = game.isGhostEdible(GHOST.values()[i]);
			if(!edibleGhosts[i])
				nonEdibleGhosts ++;
			characterIndex[i] = game.getGhostCurrentNodeIndex(GHOST.values()[i]);
			characterLastMove[i] = game.getGhostLastMoveMade(GHOST.values()[i]).ordinal();
			activePPills[i] = game.getPowerPillIndices()[i];
			
			if(game.getPowerPillIndex(activePPills[i]) == -1)
				activePPills[i] = -1;
		}

		nearestPPillToPacman = DataGetter.findNearestPowerPill(game,activePPills);

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