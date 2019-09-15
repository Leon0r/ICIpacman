package es.ucm.fdi.ici.c1920.practica0.grupoYY;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Game;

public class MsPacMan extends PacmanController {
	//Interesting functions: line 1556 in game class
	private DM[] allDM = DM.values(); //DM: PATH, EUCLID, MANHATTAN
	private Random rnd  = new Random();

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int limit = 20;
		double nearestD = limit;
		double distance;
		GHOST ghostT = null;
		int nearestP = -1;
		MOVE move;

		for(GHOST ghostType : GHOST.values()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), allDM[0]);
			if(distance != -1 && distance <= nearestD) {
				nearestD = distance;
				ghostT = ghostType;
			}
		}

		if(ghostT != null) {
			if(game.isGhostEdible(ghostT))
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), allDM[0]);
			else
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), allDM[0]);
		}
		else {
			for(int pill : game.getActivePillsIndices()) {
				distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, allDM[0]);
				if(nearestP == -1 || distance <= nearestD) {
					nearestD = distance;
					nearestP = pill;
				}
			}

			move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, allDM[0]);//NOT POSISBLE TO DON'T HAVE AT LEAST ONE ACTIVE PILL
		}

		return move;
	}
}

