package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GoToPacman extends Action {

	int idx;
	
	public GoToPacman(int ghostIndex) {
		this.idx = ghostIndex;
	}
	
	public MOVE executeAction() {
		
		//System.out.println("Ejecuto el gotopacman");
		
		return g.getApproximateNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(GHOST.values()[idx]), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(GHOST.values()[idx]),  DM.EUCLID);		

	}
}
