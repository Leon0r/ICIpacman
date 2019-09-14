package es.ucm.fdi.ici.c1920.practica0.grupoYY;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.controllers.PacmanController;
import pacman.game.Game;

public class MsPacMan extends PacmanController {
//Interesting functions: line 1556 in game class
	private DM[] allDM = DM.values();
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		int ghost, nearestGhost = (Integer) null;
		for(GHOST ghostType : GHOST.values()) {
			ghost = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), allDM[0]);
			//DM: PATH, EUCLID, MANHATTAN
			if(nearestGhost == (Integer) null || ghost < nearestGhost) 
				nearestGhost = ghost;
		}
		if(nearestGhost != (Integer) null)
			getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), allDM[0]);
		
		getNextMoveTowardsTarget
		
		return null;
	}

}
