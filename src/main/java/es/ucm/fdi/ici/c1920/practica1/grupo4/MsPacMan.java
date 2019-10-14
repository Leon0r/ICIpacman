// ICI Grupo4 Marcos Garcia Garcia, Rodrigo Manuel Perez Ruiz, Leonor Cuesta Molinero y Esther Ruiz-Capillas

package es.ucm.fdi.ici.c1920.practica1.grupo4;

import pacman.game.Constants.DM; //DM: PATH, EUCLID, MANHATTAN
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Game;

/*
 *COMPORTAMIENTO DE PACMAN
 *
 *Estrategias:
 *
 *	- Evasi�n:
 *		- Un fantasma: si un solo fantasma se encuentra cerca de MsPacman busca caminos que le alejen de �l,
 *					   priorizando los que le lleven a las pills m�s cercanas
 *		
 *		- 2 o m�s fantasmas: si m�s de un fantasma se encuentra cerca, trata de huir de todos 
 *							 buscando los caminos que le alejen de ellos, priorizando fantasmas comestibles, luego power pills,
 *							 pills y por �ltimo, random entre los posibles
 *
 *		- Acorralado: si todos los movimientos posibles le acercan a un fantasma, tratar� de ir a por el primer 
 *							 fantasma comestible. Si no encuentra, tratar� de ver si est� cerca de alg�n cruce en cualquier direcci�n 
 *							 y se dirigir� a �l. En el peor de los casos y hallarse acorralado, ir� hacia el fantasma m�s lejano 
 *							 esperando hallar alg�n camino alternativo entremedias, bloqueando el movimiento hacia �l hasta hallar una
 *							 nueva ruta.
 *
 *	- Ataque: si hay fantasmas comestibles cerca, se dirigir� hacia ellos tratando de comerselos, priorizandoles sobre las pills
 *
 *	- Recolecci�n: si no encuentra fantasmas cerca, ir� a por las pills m�s cercanas 
 * 
 */

public class MsPacMan extends PacmanController {

	// class variables
	private int limit = 23; // distance limit from pacman (if distance to obj < limit, is considered to be near)
	private Random rnd  = new Random(); 
	private boolean carryOn = false; // to go forward until finding other path
	List<GHOST> nearGhosts = new ArrayList<GHOST>(); // list of ghosts near pacman
	MOVE[] allMoves; // vector with all the possible moves that can be made each time we check
	//

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
				//Take possible moves
				allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
				fin = allMoves.length - 1;

				//If only one ghost following, evade
				if(nearGhosts.size() == 1) {					
					//Find safe paths
					fin = findAllSafePaths(game);

					// if no safe path, turn back because its the only possibility
					if(fin == -1) {
						move = game.getPacmanLastMoveMade().opposite();
					}
					else {
						//Find nearest pill and do the move if it is safe
						nearestP = findNearestPill(game);
						move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, DM.PATH);
						if(!moveIsSuicide(game, move, fin)) return move;

						//If the move is not possible, get a possible one randomly
						move = allMoves[rnd.nextInt(fin+1)];
					}
				}
				//If there is more than one ghost following
				else {					
					//Find safe paths and save edible ghosts
					fin = findAllSafePaths(game);
					for(GHOST ghType : nearGhosts) {
						if(game.isGhostEdible(ghType))
							edibleG.add(ghType);
					}


					//If there are no safe paths
					if(fin == -1) {
						//When surrounded by ghosts, continues doing the last move made hoping to find a new path before reaching the ghost
						if(carryOn) {							
							//If possible, do the last move made
							move = game.getPacmanLastMoveMade();
							if(!moveIsSuicide(game, move, fin)) return move;

							//If the move is not possible, get a possible one randomly
							carryOn = false;
							allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
							fin = allMoves.length - 1;
							move = allMoves[rnd.nextInt(fin+1)];
						}
						else {
							//If any of the ghosts surrounding me is edible, go towards it
							if(!edibleG.isEmpty()) {
								return moveTowardsEdibleGhost(game, edibleG);
							}

							//If any of the ghosts are edible, look if there will be new options two cells ahead in all the possible moves
							for(MOVE m : allMoves) {//TO DO: Se come a los fantasmas si la predicci�n les sobrepasa
								index = game.getPacmanCurrentNodeIndex();
								for(int i = 0; i < 4; i++) {
									index = game.getNeighbour(index, m) != -1? game.getNeighbour(index, m) : index;
									MOVE[] movesP = game.getPossibleMoves(index, m);
									if(movesP.length>1) {
										move = m;	
										return move;
									}
								}
							}

							//If there will not be a new option, go towards the farthest ghost and continue for next moves
							move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearGhosts.get(nearGhosts.size()-1)), DM.PATH);
							carryOn = true;
						}
					}
					//If there is a "safe" path
					else {
						carryOn = false;

						//If any of the ghosts surrounding me is edible, go towards it (if it doesn't go though a non edible one)
						if(!edibleG.isEmpty()) {
							move = moveTowardsEdibleGhost(game, edibleG);
							if(!moveIsSuicide(game, move, fin)) return move;

						}

						//Moves towards the power pill if no ghosts in direction
						nearestPowP = findNearestPowerPill(game);
						if(nearestPowP != -1) {
							move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPowP, DM.PATH);
							if(!moveIsSuicide(game, move, fin)) return move;
						}
						//Moves towards the power pill if no ghosts in direction
						nearestP = findNearestPill(game);
						if(nearestP != -1) {
							move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, DM.PATH);
							if(!moveIsSuicide(game, move, fin)) return move;
						}
						//If non of the above are possible, decide a move randomly
						move = allMoves[rnd.nextInt(fin+1)];
						if(!moveIsSuicide(game, move, fin)) return move;
					}
				}

			}

		}
		else {
			nearestP = findNearestPill(game);
			move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestP, DM.PATH);

			// TO DO: esquivar las powerpills 
			nearestPowP = findNearestPowerPill(game);

			if(nearestP-4 == nearestPowP)
				move = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), nearestP, DM.PATH);
			else
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

	// returns nearest power pill node index or -1 if none
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

	// returns nearest pill node index or -1 if none
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
	// returns the nearest ghost below limit
	private GHOST findNearGhosts(Game game, int limit) {
		double distance, nearestD = limit;
		GHOST ghostT = null;
		nearGhosts.clear();

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

		//Sorts ghosts from closest to farthest
		nearGhosts.sort((g1, g2)->{
			if(getGhostDistFromPacman(game, g1) == getGhostDistFromPacman(game, g2))
				return 0;
			return (getGhostDistFromPacman(game, g1) < getGhostDistFromPacman(game, g2))? -1 : 1;
		});
		return ghostT;
	}

	//Gets distance from Pacman to a ghost using path distance
	private double getGhostDistFromPacman(Game game, GHOST ghost) {
		return game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH);
	}

	//Finds safe paths in between the possible moves and returns amount of them (-1 if none)
	//Orders allMoves[] to have the possible ones between 0 and end
	private int findAllSafePaths(Game game) {
		int ini;
		int end = allMoves.length - 1;
		MOVE aux; 

		for(GHOST ghType : nearGhosts) {
			ini = 0;
			while(ini <= end) {

				//If I get closer to a non edible ghost, is not a safe path
				if(!game.isGhostEdible(ghType) && getGhostDistFromPacman(game, ghType) > 
				game.getDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), allMoves[ini]), game.getGhostCurrentNodeIndex(ghType), DM.PATH)) {

					aux = allMoves[end];
					allMoves[end] = allMoves[ini];
					allMoves[ini] = aux;
					end--;
				}else
					ini++;
			}
		}
		return end;

	}

	// Returns move towards edible ghost when completely surrounded
	// NEUTRAL if none
	private MOVE moveTowardsEdibleGhost(Game game, List<GHOST> edibleG) {
		Iterator<GHOST> it = edibleG.iterator();
		MOVE move;
		do{
			move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(it.next()), DM.PATH);
			for(MOVE m : allMoves) {
				if(move==m)
					return move;
			}
		}while(it.hasNext());
		return MOVE.NEUTRAL;
	}

	// true if the move to be made is going closer to a ghost
	private boolean moveIsSuicide(Game game, MOVE move, int fin){
		for (int i=0; i<fin+1;i++) {
			if(move == allMoves[i])
				return false;
		}
		return true;
	}
}