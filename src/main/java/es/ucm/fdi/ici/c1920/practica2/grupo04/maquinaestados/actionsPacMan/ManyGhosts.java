package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsPacMan;

import java.util.Random;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.auxiliaryCode.DataGetter;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class ManyGhosts extends Action {

	public MOVE executeAction() {
		MOVE move = findPath();

		return move;
	}

	public MOVE findPath() {

		MOVE[] safeMoves = DataGetter.findSafePaths(g);
		MOVE move = null;
		MOVE[] allMoves = g.getPossibleMoves(g.getPacmanCurrentNodeIndex());

		move = DataGetter.findPathWithPowerPill(g, safeMoves.length > 0? safeMoves:allMoves);
		if(move == null)
			move = DataGetter.findPathWithMostPills(g,  safeMoves.length > 0? safeMoves:allMoves);
		if(move == null)
			move = g.getNextMoveTowardsTarget(g.getPacmanCurrentNodeIndex(), DataGetter.findNearestPill(g), DM.PATH);

		return move;
	}
}


