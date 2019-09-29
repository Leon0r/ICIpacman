package es.ucm.fdi.ici.c1920.practica0.grupoYY;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Game;

public class MsPacMan extends PacmanController {
	private DM[] allDM = DM.values(); //DM: PATH, EUCLID, MANHATTAN
	private Random rnd  = new Random();

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int limit = 20;
		double nearestD = limit;
		double distance;
		GHOST ghostT = null;
		List<GHOST> nearGhosts = new ArrayList<GHOST>();
		int nearestP = -1;
		MOVE move;

		//Finds nearest ghost inside the limit
		for(GHOST ghostType : GHOST.values()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), allDM[0]);
			if(distance != -1 && distance <= limit) { 
				nearGhosts.add(ghostType);

				if(distance <= nearestD) {
					nearestD = distance;
					ghostT = ghostType;
				}
			}
		}

		nearestD = -1;

		//If a ghost is found and it is edible, goes towards it. If it is not edible, runs away from it. If there is no ghost, eats pills.
		if(ghostT != null) {
			if(game.isGhostEdible(ghostT)) {
				move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), allDM[0]);
			}
			else {				
				// si hay mas de un ghost following us
				if(nearGhosts.size()>=2) {
					// pillar todos los mov posibles
					MOVE[] allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
					int ini, fin = allMoves.length - 1;
					double greatestD = 0;
					GHOST ghD = ghostT;

					for(GHOST ghType : nearGhosts) {
						ini = 0;
						while(ini <= fin) {
							double auxD;
							if(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghType), allDM[0]) > 
							(auxD = game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), allMoves[ini]), game.getGhostCurrentNodeIndex(ghType), allDM[0]))) {

								MOVE aux = allMoves[fin];
								allMoves[fin] = allMoves[ini];
								allMoves[ini] = aux;
								fin--;

								if(auxD > greatestD) {
									greatestD = auxD;
									ghD = ghType;
								}
							}else
								ini++;
						}
					}
					if(fin == -1) {
						move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghD), allDM[0]);

					}else
						move = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), allDM[0]);
				}
			}
			else {
				for(int pill : game.getActivePillsIndices()) {
					distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, allDM[0]);
					if(nearestP == -1 || distance <= nearestD) {
						nearestD = distance;
						nearestP = pill;
					}
				}
				move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, allDM[0]);

				int index = game.getNeighbour(game.getPacmanCurrentNodeIndex(), move);
				if(index != -1) {
					if(game.getPowerPillIndex(index) == -1) {
						return move;
					}
					else {
						//Find how many edible ghosts are there and if... half? then eat power pill. If not, try other direction. 
						int edGh = 0;
						for(GHOST ghostType : GHOST.values()) {
							if(game.isGhostEdible(ghostType))
								edGh++;
						}

						//if (edGh<=2)
						return move;
						//else

					}
				}
			}
		}

		return move;
	}
}

