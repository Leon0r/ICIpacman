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
		MOVE[] safeMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		int fin = safeMoves.length - 1;

		for(GHOST gh : GHOST.values()) {
			if(limit > game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(gh), DM.PATH)) {
				nearGhosts++;
				if(game.isGhostEdible(gh))
					edibleGhost = true;
				else {
					// esto es una mierda, pero reordena los movimientos posibles poniendo los safe delante
					MOVE mToGh = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(gh), DM.PATH);
					int i = 0;
					while(i<= fin && safeMoves[i] != mToGh) {i++;}
					if(i<=fin) {
						mToGh = safeMoves[fin];
						safeMoves[fin] = safeMoves[i];
						safeMoves[i] = mToGh;
						fin--;
					}
				}
			}
		}

		in.setNearGhosts(nearGhosts);
		in.setEdibleGhost(edibleGhost);
		in.setSafeMoves(safeMoves, fin); // se le pasa el fin tambien para asignarlo cutremente desde dentro

	}
}
