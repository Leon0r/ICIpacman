package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.*;

public class OneGhost extends Action {

	public MOVE executeAction() {
		return g.getNextMoveAwayFromTarget(g.getPacmanCurrentNodeIndex(), g.getGhostCurrentNodeIndex(getClosestGhost()), DM.PATH);
	}

	// TO DO: huir por donde hay mas pills
	private GHOST getClosestGhost() {
		double d = -1;
		GHOST ngh = GHOST.BLINKY;
		for(GHOST gh : GHOST.values()) {
			if(d == -1 || d > g.getDistance(g.getPacmanCurrentNodeIndex(), g.getGhostCurrentNodeIndex(gh), DM.PATH)) {
				d = g.getDistance(g.getPacmanCurrentNodeIndex(), g.getGhostCurrentNodeIndex(gh), DM.PATH);
				ngh = gh;
			}
		}
		return ngh;
	}

}