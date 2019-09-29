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
	private boolean carryOn = false;

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int limit = 23;
		double nearestD = limit;
		double distance;
		GHOST ghostT = null;
		List<GHOST> nearGhosts = new ArrayList<GHOST>();
		int nearestP = -1;
		int nearestPowP = -1;
		MOVE move;
		boolean powerPill = false;
		MOVE[] allMoves;


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

		//If a ghost is found and it is edible, goes towards it. If it is not edible, runs away from it. If there is no ghost, eats pills.
		if(ghostT != null) {
			if(game.isGhostEdible(ghostT)) {
				move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), allDM[0]);
			}
			else {
				nearestD = -1;
				for(int pPill : game.getActivePowerPillsIndices()) {
					distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pPill, allDM[0]);
					if(nearestPowP == -1 || distance <= nearestD) {
						nearestD = distance;
						nearestPowP = pPill;
					}
				}
				// movement towards nearest powerpill
				if(nearestPowP != -1)
					move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPowP, allDM[0]);
				else // esto es porque java es tonto y no nos deja hacerlo bien
					move = MOVE.NEUTRAL;

				// si hay mas de un ghost following us
				if(nearGhosts.size()>=2) {
					///////////////////////////////
					for(GHOST g : nearGhosts)
						if(game.isGhostEdible(g))
							return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), allDM[0]);
					///////////////////////////////

					// pillar todos los mov posibles
					allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
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
					// no safe path
					if(fin == -1) {
						if(carryOn) {
							move = game.getPacmanLastMoveMade();
						}else {

							for(MOVE m : allMoves) {
								int index = game.getPacmanCurrentNodeIndex();
								for(int i = 0; i < 4; i++) {
									index = game.getNeighbour(index, m) != -1? game.getNeighbour(index, m) : index;
									MOVE[] movesP = game.getPossibleMoves(index, m);
									if(movesP.length>1) {
										move = m;	
										return move;
									}
								}
							}
							move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghD), allDM[0]);
							carryOn = true;
						}
					}
					else {
						carryOn = false;
						// va a por power pill

						for(int i =0; i<fin+1;i++)
							if(move == allMoves[i])
								return move;
						// busca la pill mas cercana
						nearestD = -1;
						for(int pill : game.getActivePillsIndices()) {
							distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, allDM[0]);
							if(nearestP == -1 || distance <= nearestD) {
								nearestD = distance;
								nearestP = pill;
							}
						}
						// se mueve hacia ella si no es un suicidio
						move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, allDM[0]);
						for(int i =0; i<fin+1;i++)
							if(move == allMoves[i])
								return move;
						// si no, random de lo que pueda moverse
						move = allMoves[rnd.nextInt(fin+1)];
					}

				}else
					move = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), allDM[0]);
			}
		}
		else {
			nearestD = -1;
			for(int pill : game.getActivePillsIndices()) {
				distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, allDM[0]);
				if(nearestP == -1 || distance <= nearestD) {
					nearestD = distance;
					nearestP = pill;
				}
			}
			move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, allDM[0]);
		}

		return move;
	}
}

