package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class FleeFromPacman extends Action {

	GHOST ghostType;
	
	public FleeFromPacman(GHOST gType) {
		this.ghostType = gType;
	}
	
	@Override
	public MOVE executeAction() {
		MOVE res = g.getNextMoveAwayFromTarget(g.getGhostCurrentNodeIndex(ghostType), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(ghostType),  DM.EUCLID);		 		
		return res;
	}
}
