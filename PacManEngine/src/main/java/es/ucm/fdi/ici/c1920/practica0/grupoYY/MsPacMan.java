package es.ucm.fdi.ici.c1920.practica0.grupoYY;

import pacman.game.Constants.DM; //DM: PATH, EUCLID, MANHATTAN
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
	private Random rnd  = new Random();
	private boolean carryOn = false;
	List<GHOST> nearGhosts = new ArrayList<GHOST>();
	MOVE[] allMoves;

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int nearestPowP, nearestP;
		double nearestD = limit;
		double distance;
		GHOST ghostT = null;
		MOVE move;
		boolean powerPill = false;


		//Finds nearest ghost inside the limit
		ghostT = findNearGhosts(game, limit);

		//If a ghost is found and it is edible, goes towards it. If it is not edible, runs away from it. If there is no ghost, eats pills.
		if(ghostT != null) {
			if(game.isGhostEdible(ghostT)) {
				move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), DM.PATH);
			}
			else {

				// if only one ghost following, evade
				if(nearGhosts.size() < 2) {
					///// TO DO: Mejorar a huir por el camino con pills
					move = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), DM.PATH);
				}
				else {// if there is more than one ghost following us

					// pillar todos los mov posibles
					allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
					int fin = allMoves.length - 1;
					GHOST ghD = ghostT;
					List<GHOST> edibleG = new ArrayList<GHOST>();

					for(GHOST ghType : nearGhosts) {
						//Si me puedo comer al fantasma, lo guardo como posible futuro objetivo
						if(game.isGhostEdible(ghType))
							edibleG.add(ghType);
						fin = findAllSafePaths(game, ghType, fin);
					}

					// no safe path
					if(fin == -1) {							
						if(carryOn) {// SUISIDIO
							move = game.getPacmanLastMoveMade();
						}else {
							if(!edibleG.isEmpty()) {
								Iterator<GHOST> it = edibleG.iterator();
								do{
									move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(it.next()), DM.PATH);
									for(MOVE m : allMoves) {
										if(move==m)
											return move;
									}
								}while(it.hasNext()); 
							}

							for(MOVE m : allMoves) {
								int index = game.getPacmanCurrentNodeIndex();
								// busca salida en los pasillos posibles una casilla por delante en esa dirección
								for(int i = 0; i < 5; i++) {
									index = game.getNeighbour(index, m) != -1? game.getNeighbour(index, m) : index;
									MOVE[] movesP = game.getPossibleMoves(index, m);
									if(movesP.length>1) {
										move = m;	
										return move;
									}
								}
							}
							move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearGhosts.get(nearGhosts.size()-1)), DM.PATH);
							carryOn = true;
						}
					}
					// if there is a "safe" path
					else {
						carryOn = false;
						// va a por power pill
						nearestPowP = findNearestPowerPill(game);

						move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPowP, DM.PATH);
						for(int i =0; i<fin+1;i++)
							if(move == allMoves[i])
								return move;
						// busca la pill mas cercana
						nearestP = findNearestPill(game);
						// se mueve hacia ella si no es un suicidio
						move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, DM.PATH);
						for(int i =0; i<fin+1;i++)
							if(move == allMoves[i])
								return move;
						// si no, random de lo que pueda moverse
						move = allMoves[rnd.nextInt(fin+1)];
					}
				}

			}

		}
		else {
			nearestP = findNearestPill(game);
			move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, DM.PATH);

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

	// returns nearest power pill node index
	private int findNearestPowerPill(Game game) {
		int nearestPowP = -1;
		double distance, nearestD = -1;

		for(int pPill : game.getActivePowerPillsIndices()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pPill, DM.PATH);
			if(nearestPowP == -1 || distance <= nearestD) {
				nearestD = distance;
				nearestPowP = pPill;
			}
		}
		return nearestPowP;

	}

	// returns nearest pill node index
	private int findNearestPill(Game game) {
		int nearestP = -1;
		double distance, nearestD = -1;

		for(int pill : game.getActivePillsIndices()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
			if(nearestP == -1 || distance <= nearestD) {
				nearestD = distance;
				nearestP = pill;
			}
		}
		return nearestP;
	}

	// finds ghosts within the distance limit and adds them to the nearGhosts List
	// returns the nearest ghost
	private GHOST findNearGhosts(Game game, int limit) {
		double distance, nearestD = limit;
		GHOST ghostT = null;

		for(GHOST ghostType : GHOST.values()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
			if(distance != -1 && distance <= limit) { 
				nearGhosts.add(ghostType);

				if(distance <= nearestD) {
					nearestD = distance;
					ghostT = ghostType;
				}
			}
		}

		// should sort ghosts from closest to farthest
		nearGhosts.sort((g1, g2)->{
			if(getGhostDistFromPacman(game, g1) == getGhostDistFromPacman(game, g2))
				return 0;
			return (getGhostDistFromPacman(game, g1) < getGhostDistFromPacman(game, g2))? -1 : 1;
		});

		return ghostT;
	}

	private double getGhostDistFromPacman(Game game, GHOST ghost) {
		return game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH);
	}

	private int findAllSafePaths(Game game, GHOST ghType, int fin) {
		int ini = 0;
		while(ini <= fin) {
			//Si me acerco a un fantasma, descarto el movimiento como posible
			if(getGhostDistFromPacman(game, ghType) > 
			game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), allMoves[ini]), game.getGhostCurrentNodeIndex(ghType), DM.PATH)) {

				MOVE aux = allMoves[fin];
				allMoves[fin] = allMoves[ini];
				allMoves[ini] = aux;
				fin--;
			}else
				ini++;
		}
		return fin;
	}

	private MOVE getMoveTowardsNearestPowerPill(Game game) {
		// movement towards nearest powerpill
		int nearestPowP = findNearestPowerPill(game);
		MOVE move, mov;

		if(nearestPowP != -1) {
			move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPowP, DM.PATH);

			// TO DO: evitar todos los fantasmas si se puede
			if(move == mov) {
				move = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), DM.PATH);
			}
		}
	}


