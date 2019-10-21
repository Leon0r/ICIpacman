package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	Input in= new Input();
	StateEngine stateEngine = new StateEngineImpl();
	FSM fsm = new MyFSMPacman(stateEngine);

	double limit = 23;
	
	@Override
	public MOVE getMove(Game game, long timeDue) {

		/*
		 * Actualizar input
		 * buscar si hay que actualizar estado
		 * devolver el movimiento correspondiente
		 */
		fsm.setGame(game);
		updateInput(game);
		fsm.checkChange(in);
		return fsm.getState().execute();
	}

	private void updateInput(Game game) {

		//Find number of near ghosts and save the edible one, if any
		int nearGhosts = 0;
		boolean edibleGhost = false;
		for(GHOST gh : GHOST.values()) {
			if(limit > game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(gh), DM.PATH)) {
				nearGhosts++;
				if(game.isGhostEdible(gh))
					edibleGhost = true;
			}
		}
		in.setNearGhosts(nearGhosts);
		in.setEdibleGhost(edibleGhost);
		
	}
}
