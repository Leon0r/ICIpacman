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

	MsPacmanCase pacmanCase = null;

	// Numero minimo de cruces que se han superado sin morir
	int crossesSurpassed = 10;


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

		pacmanCase = new MsPacmanCase(edibleGhosts, characterIndex, characterLastMove, activePPills, nearestPPillToPacman, nonEdibleGhosts, 0);

	}


	@Override
	public MOVE getMove(Game game, long timeDue) {

		MOVE[] m = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());

		if(m.length > 1) {
			if(game.wasPacManEaten()) {
				PacmanDataBase.removeLastCase();
			}

			generateCase(game);
			MOVE move = PacmanDataBase.comparePacmanCase(pacmanCase, game);

			pacmanCase.movement = move;
			PacmanDataBase.addGeneratedCase(pacmanCase);
			return move;
		}	
		else {
			return m[0];
		}
	}
}