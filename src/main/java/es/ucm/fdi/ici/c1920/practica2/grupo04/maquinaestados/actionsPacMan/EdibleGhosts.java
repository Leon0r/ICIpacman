package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EdibleGhosts extends Action {

	public MOVE executeAction() {
		return g.getNextMoveTowardsTarget(g.getPacmanCurrentNodeIndex(), g.getGhostCurrentNodeIndex(findEdibleGhost(g)), DM.PATH);
	}
	
	public GHOST findEdibleGhost(Game game) {
		for(GHOST ghType : GHOST.values()) {
			if(game.isGhostEdible(ghType))
				return ghType;
		}
		return null;
	}

}