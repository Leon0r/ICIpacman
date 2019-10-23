package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsPacMan;

import java.util.Random;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

// TO DO
public class ManyGhostsNear extends Action {

	public MOVE executeAction() {
		MOVE move = findSafePath();
		
		return move != null ? move : g.getNextMoveTowardsTarget(g.getPacmanCurrentNodeIndex(), findNearestPowerPill(g), DM.PATH);
	}

	public MOVE findSafePath() {
		MOVE move = null;
		Random rnd  = new Random(); 
		MOVE[] safeMoves = g.getPossibleMoves(g.getPacmanCurrentNodeIndex());
		int fin = safeMoves.length - 1;
		
		for(GHOST gh : GHOST.values()) {
			int i = 0;
			MOVE mToGh = g.getNextMoveTowardsTarget(g.getPacmanCurrentNodeIndex(), g.getGhostCurrentNodeIndex(gh), DM.PATH);
			while(i<= fin && safeMoves[i] != mToGh) {i++;}
			if(i<=fin) {
				mToGh = safeMoves[fin];
				safeMoves[fin] = safeMoves[i];
				safeMoves[i] = mToGh;
				fin--;
			}
		}
		
		if(fin != -1) {
			move = safeMoves[rnd.nextInt(fin+1)];
		}
		return move;
	}
	
	private int findNearestPowerPill(Game game) {
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
}
