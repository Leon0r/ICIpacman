package es.ucm.fdi.ici.c1920.practica0.grupoYY;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Game;

public class MsPacMan extends PacmanController {
	private int limit = 23;
	private DM[] allDM = DM.values(); //DM: PATH, EUCLID, MANHATTAN
	private Random rnd  = new Random();
	private boolean carryOn = false;
	List<GHOST> nearGhosts = new ArrayList<GHOST>();

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int limit = 23, nearestPowP;
		double nearestD = limit;
		double distance;
		GHOST ghostT = null;
		MOVE move;
		boolean powerPill = false;
		MOVE[] allMoves;


		//Finds nearest ghost inside the limit
		ghostT = findNearGhosts(game, limit);

		//If a ghost is found and it is edible, goes towards it. If it is not edible, runs away from it. If there is no ghost, eats pills.
		if(ghostT != null) {
			if(game.isGhostEdible(ghostT)) {
				move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), allDM[0]);
			}
			else {
				nearestPowP = findNearestPowerPill(game);
				// movement towards nearest powerpill
				if(nearestPowP != -1)
					move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPowP, allDM[0]);
				else { 
					if(nearGhosts.size() < 2) {//Mejorar a huir por el camino con pills
						move = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), allDM[0]);
					}
					else {// si hay mas de un ghost following us
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
						List<GHOST> edibleG;

						for(GHOST ghType : nearGhosts) {
							//Si me puedo comer al fantasma, lo guardo como posible futuro objetivo
							if(game.isGhostEdible(ghType))
								edibleG.add(ghType);
							ini = 0;
							while(ini <= fin) {
								double auxD;
								//Si me acerco a un fantasma, descarto el movimiento como posible
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
								if(!edibleG.isEmpty()) {
									Iterator<GHOST> it = edibleG.iterator();
									do{
										move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(it.next()), allDM[0]);
										for(MOVE m : allMoves) {
											if(move==m)
												return move;
										}
									}while(it.hasNext()); 
								}
								
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
					}
				}
			}
		}
		else {

			move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), findNearestPill(game), allDM[0]);

			/*int index = game.getNeighbour(game.getPacmanCurrentNodeIndex(), move);
			if(index != -1) {
				if(game.getPowerPillIndex(index) != -1) {//If it is a powerPill
					if(!powerPill) {

						allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), move.opposite());
						move = allMoves[rnd.nextInt(allMoves.length)];
					}
				}
			}*/
		}

		return move;
	}

	private int findNearestPowerPill(Game game) {
		int nearestPowP = -1;
		double distance, nearestD = -1;

		for(int pPill : game.getActivePowerPillsIndices()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pPill, allDM[0]);
			if(nearestPowP == -1 || distance <= nearestD) {
				nearestD = distance;
				nearestPowP = pPill;
			}
		}
		return nearestPowP;

	}

	private int findNearestPill(Game game) {
		int nearestP = -1;
		double distance, nearestD = -1;

		for(int pill : game.getActivePillsIndices()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, allDM[0]);
			if(nearestP == -1 || distance <= nearestD) {
				nearestD = distance;
				nearestP = pill;
			}
		}
		return nearestP;
	}

	private GHOST findNearGhosts(Game game, int limit) {
		double distance, nearestD = limit;
		GHOST ghostT = null;

		for(GHOST ghostType : GHOST.values()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), allDM[0]);
			if(distance != -1 && distance <= limit) { 
				nearGhosts.add(ghostType);

				if(distance <= nearestD) {
					nearestD = distance;
					ghostT = ghostType;
				}
			}
			/*else if(distance != -1 && distance <= limit*3 && !powerPill) { 
				if(!game.isGhostEdible(ghostType)){
					powerPill = true;
				}
			}*/
		}
		return ghostT;
	}
}

