package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToPacmanAggressive extends Action {

	GHOST ghostType;
	
	public GoToPacmanAggressive(GHOST gType) {
		this.ghostType = gType;
	}
	
	@Override
	public MOVE executeAction() {
				
		return g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(ghostType), DM.EUCLID);
		
	}
}
