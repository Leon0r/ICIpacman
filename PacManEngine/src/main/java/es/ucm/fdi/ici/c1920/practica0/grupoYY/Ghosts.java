package es.ucm.fdi.ici.c1920.practica0.grupoYY;

import java.util.EnumMap;

import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Ghosts extends GhostController{
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	//private MOVE[] allMoves = MOVE.values();
	private DM[] allDM = DM.values();

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		moves.clear();
		for(GHOST ghostType : GHOST.values()) {
			if(game.doesGhostRequireAction(ghostType)) {
				MOVE mov = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), allDM[0]);
				//PATH, EUCLID, MANHATTAN
				moves.put(ghostType,  mov);
			}
		}
		return moves;
	}
}
