package es.ucm.fdi.ici.c1920.practica0.grupoYY;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Ghosts extends GhostController{
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private MOVE[] allMoves = MOVE.values();
	private DM[] allDM = DM.values();
	private Random rnd = new Random();

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		int p = 0, limit = 20;
		moves.clear();
		while(p < game.getActivePowerPillsIndices().length && game.getDistance(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices()[p], allDM[1]) > limit) {
			p++;
		}
		for(GHOST g : GHOST.values()) {
			if(game.doesGhostRequireAction(g)) {
				if(game.isGhostEdible(g) || p < game.getActivePowerPillsIndices().length) {
					moves.put(g, game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), allDM[0]));
				}
				else {
					if(rnd.nextInt(10) == 0) {
						moves.put(g,  allMoves[rnd.nextInt(allMoves.length)]);
					}
					else {
						MOVE mov = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), allDM[0]);
						//DM: PATH, EUCLID, MANHATTAN
						moves.put(g,  mov);
					}
				}
			}
		}
		return moves;
	}
}