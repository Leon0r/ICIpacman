package es.ucm.fdi.ici.c1920.practica3.grupo04.auxiliaryCode;

import java.util.ArrayList;
import java.util.List;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class DataGetter {
	public static int findNearestPowerPill(Game game) {
		int nearestP = -1;
		double distance, nearestD = -1;

		for(int pill : game.getActivePowerPillsIndices()) {
			distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
			if(nearestP == -1 || distance <= nearestD) {
				nearestD = distance;
				nearestP = pill;
			}
		}
		return nearestP;
	}

	public static int findNearestPowerPill(Game game, int[] powerPillIndexes) {
		int nearestP = -1;
		double distance, nearestD = -1;

		for(int pill : powerPillIndexes) {
			if(pill != -1) {
				distance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, DM.PATH);
				if(nearestP == -1 || distance <= nearestD) {
					nearestD = distance;
					nearestP = pill;
				}
			}
		}
		return nearestP;
	}

	public static int findNearestPill(Game game) {
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

	public static GHOST findEdibleGhost(Game game) {
		for(GHOST ghType : GHOST.values()) {
			if(game.isGhostEdible(ghType))
				return ghType;
		}
		return null;
	}

	public static GHOST getClosestGhost(Game game) {
		double d = -1;
		GHOST ngh = GHOST.BLINKY;
		for(GHOST gh : GHOST.values()) {
			if(d == -1 || d > game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(gh), DM.PATH)) {
				d = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(gh), DM.PATH);
				ngh = gh;
			}
		}
		return ngh;
	}

	public static MOVE[] findSafePaths(Game game) {

		MOVE[] allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		MOVE[] safeMoves;

		int fin = allMoves.length - 1;
		int i;
		for(GHOST gh : GHOST.values()) {
			i = 0;
			if(game.getGhostCurrentNodeIndex(gh) != -1) {
				MOVE mToGh = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(gh), DM.PATH);

				while(i<= fin && allMoves[i] != mToGh) {i++;}
				if(i<=fin) {
					mToGh = allMoves[fin];
					allMoves[fin] = allMoves[i];
					allMoves[i] = mToGh;
					fin--;
				}
			}
		}

		safeMoves = new MOVE[fin+1];
		if(fin>0) {
			for(int j = 0; j <= fin; j++)
				safeMoves[j] = allMoves[j];
		}
		return safeMoves;
	}

	public static MOVE findPathWithMostPills(Game game, MOVE[] moves) {

		int index, numPills, maxPills = -1;
		MOVE nextM, move = null;
		boolean carryOn;

		for(MOVE m : moves) {
			nextM = m;
			numPills = 0;
			carryOn = true;
			index = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);

			while(carryOn) {
				if(index != -1) {
					if(game.getPossibleMoves(index, nextM).length == 1) {
						if(game.getPillIndex(index)!= -1)
							numPills = numPills + 1;
						nextM = game.getPossibleMoves(index, nextM)[0];
						index = game.getNeighbour(index, nextM);
					}else
						carryOn = false;
				}else 
					carryOn = false;
			}

			if(numPills>maxPills) {
				maxPills= numPills;
				move = m;
			}
		}

		return move;
	}

	public static MOVE findPathWithPowerPill(Game game, MOVE[] moves) {
		int index;
		MOVE nextM, move = null;
		boolean isTherePowPill, carryOn;
		List<Integer> ghostIndx = new ArrayList<Integer>();

		for(GHOST gh :GHOST.values())
			ghostIndx.add(game.getGhostCurrentNodeIndex(gh));

		for(MOVE m : moves) {
			nextM = m;
			isTherePowPill = false;
			carryOn = true;
			index = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);

			while(carryOn) {
				if(index != -1) {
					if(!ghostIndx.contains(index) && game.getPossibleMoves(index, nextM).length == 1) {
						if(game.getPowerPillIndex(index) != -1) {
							if(game.isPowerPillStillAvailable(game.getPowerPillIndex(index))) {
								isTherePowPill = true;
								carryOn = false;
							}
						}
						nextM = game.getPossibleMoves(index, nextM)[0];
						index = game.getNeighbour(index, nextM);
					}else
						carryOn = false;
				}else 
					carryOn = false;
			}
		}
		return move;
	}
	public static boolean isPathSafe(Game game, MOVE[] moves, MOVE move) {
		for(MOVE m : moves) {
			if(m == move)
				return true;
		}
		return false;
	}
}
