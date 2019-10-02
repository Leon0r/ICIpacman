package es.ucm.fdi.ici.c1920.practica0.grupo4;

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

		int nearestPowP;//Nearest power pill index
		int nearestP;//Nearest pill index
		int fin;//Index of the last possible move in the allMoves array
		int index;//Index of the current observed node
		GHOST ghostT;//Nearest ghost
		MOVE move;//Future move
		List<GHOST> edibleG = new ArrayList<GHOST>();//Near edible ghosts


		//Finds nearest ghost inside the limit
		ghostT = findNearGhosts(game, limit);

		//If there is at least one ghost following
		if(ghostT != null) {
			if(game.isGhostEdible(ghostT)) {
				move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), DM.PATH);
			}
			else {
				//If only one ghost following, evade
				if(nearGhosts.size() < 2) {
					// TO DO: Mejorar a huir por el camino con pills
					move = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostT), DM.PATH);
				}
				//If there is more than one ghost following
				else {
					//Take possible moves
					allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
					fin = allMoves.length - 1;
					
					//Find safe paths and save edible ghosts
					for(GHOST ghType : nearGhosts) {
						if(game.isGhostEdible(ghType))
							edibleG.add(ghType);
						fin = findAllSafePaths(game, ghType, fin);
					}

					//If there are no safe paths
					if(fin == -1) {
						//When surrounded by ghosts, continues doing the last move made hoping to find a new path before reaching the ghost
						if(carryOn) {// SUISIDIO
							move = game.getPacmanLastMoveMade();
						}
						else {
							//If any of the ghosts surrounding me is edible, go towards it
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
							
							//If any of the ghosts are edible, look if there will be new options one cell ahead in all the possible moves
							for(MOVE m : allMoves) {
								index = game.getPacmanCurrentNodeIndex();
								for(int i = 0; i < 5; i++) {
									index = game.getNeighbour(index, m) != -1? game.getNeighbour(index, m) : index;
									MOVE[] movesP = game.getPossibleMoves(index, m);
									if(movesP.length>1) {
										move = m;	
										return move;
									}
								}
							}
							
							//If there won't be any new option, go towards the farthest ghost and continue for next moves
							move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearGhosts.get(nearGhosts.size()-1)), DM.PATH);
							carryOn = true;
						}
					}
					//If there is a "safe" path
					else {
						carryOn = false;
						
						//Moves towards the power pill if no ghosts in direction
						nearestPowP = findNearestPowerPill(game);
						move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPowP, DM.PATH);
						for(int i = 0; i < fin+1; i++)
							if(move == allMoves[i])
								return move;
						
						//Moves towards the power pill if no ghosts in direction
						nearestP = findNearestPill(game);
						move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, DM.PATH);
						for(int i = 0; i < fin+1; i++)
							if(move == allMoves[i])
								return move;
						
						//If non of the above are possible, decide a move randomly
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

}


