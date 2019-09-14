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

		int limit = 20;

		int ghost = -1;
		/*int[] ghosts = new int[4];
		int i = 0;

		for(GHOST ghostType : GHOST.values()) {
			ghosts[i++]= game.getGhostCurrentNodeIndex(ghostType);
		}
		ghost  = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), ghosts, allDM[0]);*/


		double d = limit;
		double aux;
		for(GHOST ghostType : GHOST.values()) {
			aux = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), allDM[1]);
			if(aux != -1 && aux <= d) {
				d = aux;
				ghost = game.getGhostCurrentNodeIndex(ghostType);
				System.out.println(d);
			}
		}


		//DM: PATH, EUCLID, MANHATTAN
		if(ghost != -1) 
		{
			GHOST ghostType = null;
			for(GHOST g : GHOST.values()) {
				if(game.getGhostCurrentNodeIndex(g) == ghost)
					ghostType = g;
			}

			if(game.isGhostEdible(ghostType))
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), ghost, allDM[1]);
			else
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), ghost, allDM[1]);
		}else {
			if(game.wasPillEaten()) {
				double auxP = -1;
				double nearestPill = -1;
				int nearestIndex = -1;
				for(int p : game.getActivePillsIndices()) {
					auxP = game.getDistance(game.getPacmanCurrentNodeIndex(), p, allDM[1]);
					if(nearestPill == -1 || nearestPill>auxP) {
						nearestPill = auxP;
						nearestIndex= p;
					}
				}

				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestIndex, allDM[1]);
			}
			else 
				return game.getPacmanLastMoveMade();
		}
	}

}
