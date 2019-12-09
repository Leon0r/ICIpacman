package es.ucm.fdi.ici.c1920.practica4.grupo04;

import pacman.controllers.PacmanController;

import java.util.Random;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;


public final class MsPacMan extends PacmanController {

	private Random rnd  = new Random();
	private MOVE[] allMoves = MOVE.values();
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		if(getMove() == null)
			allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		else
			allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), getMove());
		
		lastMove = allMoves[rnd.nextInt(allMoves.length)];
		return lastMove;
	}
}
