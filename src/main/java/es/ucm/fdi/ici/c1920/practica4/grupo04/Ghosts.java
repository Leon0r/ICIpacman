package es.ucm.fdi.ici.c1920.practica4.grupo04;

import pacman.controllers.GhostController;

import java.util.EnumMap;
import java.util.Random;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class Ghosts extends GhostController {
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private MOVE[] allMoves = MOVE.values();
	private Random rnd = new Random();
	
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		moves.clear();
		for(GHOST ghostType : GHOST.values()) {
			if(game.doesGhostRequireAction(ghostType)) {
				moves.put(ghostType,  allMoves[rnd.nextInt(allMoves.length)]);
			}
		}
		return moves;
	}
}
