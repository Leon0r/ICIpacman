package pacman.controllers;

import java.util.Random;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;


public final class MsPacmanController extends PacmanController {

	private Random rnd  = new Random();
	private MOVE[] allMoves = MOVE.values();
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		if(game.getPacmanLastMoveMade() == null) {
			allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		}
		else {
			allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		}
		lastMove = allMoves[rnd.nextInt(allMoves.length)];
		
		return lastMove;
	}
}
