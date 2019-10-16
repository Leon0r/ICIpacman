// ICI Grupo4 Marcos Garcia Garcia, Rodrigo Manuel Perez Ruiz, Leonor Cuesta Molinero y Esther Ruiz-Capillas

package es.ucm.fdi.ici.c1920.practica1.grupo4;

import pacman.game.Constants.DM; //DM: PATH, EUCLID, MANHATTAN
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import pacman.controllers.PacmanController;
import pacman.game.Game;

/*
 *COMPORTAMIENTO DE PACMAN
 *
 *Estrategias:
 *
 *	- Evasi�n:
 *		- Un fantasma: si un solo fantasma se encuentra cerca de MsPacman busca caminos que le alejen de �l,
 *					   priorizando los que le lleven a las pills mas cercanas
 *		
 *		- 2 o m�s fantasmas: si mas de un fantasma se encuentra cerca, trata de huir de todos 
 *							 buscando los caminos que le alejen de ellos, priorizando fantasmas comestibles, luego power pills,
 *							 pills y por ultimo, random entre los posibles
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
	List<GHOST> nearGhosts = new ArrayList<GHOST>(); // list of ghosts near pacman

	List<MOVE> availableMoves = new ArrayList<MOVE>();
	List<Integer> ghostIndx = new ArrayList<Integer>();
	// info about the path until next crossroad or ghost, 
	// MOVE : [0] isASavePath (1/0), [1] hasPowerPill (1/0), [2] amount of pills
	HashMap <MOVE, int[]> pathData = new HashMap<MOVE, int[]>(); 
	//

	@Override
	public MOVE getMove(Game game, long timeDue) {

		List<GHOST> edibleG = new ArrayList<GHOST>();//Near edible ghosts
		int numSafePath = 0;
		int nearestPowP;//Nearest power pill index
		MOVE move;//Future move
		///////////////////////////////


		//Finds nearest ghost inside the limit
		findNearGhosts(game);
		//Find safe paths
		numSafePath = findAllPathData(game);

		// if there are no ghosts following
		if(nearGhosts.isEmpty()) {
			move = pathWithMorePills(game, true);
		}	
		//If there is at least one ghost following
		else {
			//If only one ghost following, evade
			if(nearGhosts.size() == 1) {					
				// if no safe path, its the only possibility
				if(numSafePath == 0) {
					if(pathWithPowerPills(false) != null)
						move = pathWithPowerPills(false);
					else 
						move = pathWithMorePills(game, false);						
				}
				else {
					move = pathWithMorePills(game, true);
				}
			}
			//If there is more than one ghost following
			else {
				for(GHOST ghType : nearGhosts) {
					if(game.isGhostEdible(ghType))
						edibleG.add(ghType);
				}

				//If there are no safe paths
				if(numSafePath == 0) {					
					if(pathWithPowerPills(false) != null)
						move = pathWithPowerPills(false);
					else 
						move = pathWithMorePills(game, false);
				}
				//If there is a "safe" path
				else {

					//If any of the ghosts surrounding me is edible, go towards it (if it doesn't go though a non edible one)
					if(!edibleG.isEmpty()) {
						move = moveTowardsEdibleGhost(game, edibleG);
						if (!moveIsSuicide(game, move)) return move;
					}

					//Moves towards the power pill if no ghosts in direction
					nearestPowP = findNearestPowerPill(game);
					if(nearestPowP != -1) {
						move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPowP, game.getPacmanLastMoveMade(), DM.PATH);
						if(!moveIsSuicide(game, move)) return move;
					}

					//Moves towards the power pill if no ghosts in direction
					move = pathWithMorePills(game, true);
				}
			}
		}
		return move;
	}

	// finds ghosts within the distance limit and adds them to the nearGhosts List
	// returns the nearest ghost below limit
	private void findNearGhosts(Game game) {
		double distance;
		nearGhosts.clear();

		for(GHOST ghostType : GHOST.values()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
			if(distance != -1 && distance <= limit) { 
				nearGhosts.add(ghostType);
			}
		}

		//Sorts ghosts from closest to farthest
		nearGhosts.sort((g1, g2)->{
			if(getGhostDistFromPacman(game, g1) == getGhostDistFromPacman(game, g2))
				return 0;
			return (getGhostDistFromPacman(game, g1) < getGhostDistFromPacman(game, g2))? -1 : 1;
		});
	}

	//Gets distance from Pacman to a ghost using path distance
	private double getGhostDistFromPacman(Game game, GHOST ghost) {
		return game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH);
	}

	//Finds safe paths in between the possible moves and returns amount of them (-1 if none)
	//Orders allMoves[] to have the possible ones between 0 and end
	private int findAllPathData(Game game) {
		//Take possible moves
		// vector with all the possible moves that can be made each time we check
		MOVE[] allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade()); 
		int index = game.getPacmanCurrentNodeIndex();
		int movCount = 0; // amount of movements until next crossroad
		MOVE nextM;
		int numPills = 0;
		int numSafePath = 0;
		boolean isTherePowPill;

		boolean carryOn = true;

		pathData.clear();
		ghostIndx.clear();

		// finds ghosts current position (non edible ones only)
		for(GHOST g: GHOST.values())
			if(!game.isGhostEdible(g))
				ghostIndx.add(game.getGhostCurrentNodeIndex(g));


		// Amount of movements until next crossroad
		for(MOVE m : allMoves) {
			nextM = m;
			movCount = 0;
			numPills = 0;
			isTherePowPill = false;
			carryOn = true;
			index = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);

			while(carryOn) {
				if(index != -1) {
					MOVE[] aux = game.getPossibleMoves(index, nextM);
					if(aux.length == 1) {
						if(game.getPillIndex(index)!= -1)
							numPills = numPills + 1;
						if(game.getPowerPillIndex(index) != -1)
							isTherePowPill = true;

						nextM = game.getPossibleMoves(index, nextM)[0];
						index = game.getNeighbour(index, nextM);
						movCount++;
					}else
						carryOn = false;
				}
				else 
					carryOn = false;
			}




			pathData.put(m,new int[3]);
			pathData.get(m)[0] = isPathSafe(game, nextM, movCount + 5, index) ? 1 : 0;
			pathData.get(m)[1] = isTherePowPill ? 1 : 0;
			pathData.get(m)[2] = numPills;

		}

		// counts number of safe paths
		for(MOVE m : pathData.keySet()) {
			if(pathData.get(m)[0] == 1) {
				numSafePath++;
			}	
		} 
		return numSafePath;
	}

	private boolean isPathSafe(Game game, MOVE dir, int numTick, int index) {
		boolean isSafe = true; 
		MOVE[] moves = game.getPossibleMoves(index, dir); 

		if(numTick > 0) { 
			for(MOVE m : moves) { 
				if(ghostIndx.contains(game.getNeighbour(index, m))) 

					return false; 
				else { 
					isSafe = (isSafe && isPathSafe(game, m, numTick - 1, game.getNeighbour(index, m))); 
					if(!isSafe) 
						return false; 
				} 
			} 
		} 
		return isSafe; 
	}

	private MOVE pathWithMorePills(Game game, boolean alsoIsSafe) {
		MOVE move = null;
		int numP = -1;

		for(MOVE m : pathData.keySet()) {
			if((!alsoIsSafe || pathData.get(m)[0] == 1) && (numP == -1 || numP < pathData.get(m)[2])) {
				numP = pathData.get(m)[2];
				move = m;
			}
		}

		if(numP == 0)
			move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), findNearestPill(game), game.getPacmanLastMoveMade(), DM.PATH);

		return move;
	}

	private MOVE pathWithPowerPills(boolean alsoIsSafe) {

		for(MOVE m : pathData.keySet()) {
			if((!alsoIsSafe || pathData.get(m)[0] == 1) && pathData.get(m)[1] == 1)
				return m;
		}
		return null;
	}

	// Returns move towards edible ghost when completely surrounded
	// NEUTRAL if none
	private MOVE moveTowardsEdibleGhost(Game game, List<GHOST> edibleG) {
		Iterator<GHOST> it = edibleG.iterator();
		MOVE move;
		do{
			move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(it.next()), game.getPacmanLastMoveMade(), DM.PATH);
			if(pathData.containsKey(move))
				return move;
		}while(it.hasNext());
		return null;
	}

	// true if the move to be made is going closer to a ghost
	private boolean moveIsSuicide(Game game, MOVE move){
		if(move != null)
			if(pathData.get(move)[0] == 1)
				return false;

		return true;
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

}