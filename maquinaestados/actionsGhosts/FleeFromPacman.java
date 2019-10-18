package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class FleeFromPacman extends Action {

	int idx;
	
	public FleeFromPacman(int ghostIndex) {
		this.idx = ghostIndex;
	}
	
	public MOVE executeAction() {
		
		return g.getApproximateNextMoveAwayFromTarget(g.getGhostCurrentNodeIndex(GHOST.values()[idx]), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(GHOST.values()[idx]),  DM.EUCLID);		

	}

}
