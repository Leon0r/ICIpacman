package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.*;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.auxiliaryCode.DataGetter;

public class OneGhost extends Action {

	public MOVE executeAction() {
		MOVE[] safeMoves =  DataGetter.findSafePaths(g);
		MOVE move = DataGetter.findPathWithMostPills(g, safeMoves);
		
		if (move == null) {
			move = g.getNextMoveTowardsTarget(g.getPacmanCurrentNodeIndex(), DataGetter.findNearestPill(g), DM.PATH);	
			for(MOVE m : safeMoves)
				if(m == move){
					return move;
				}
		}
		return g.getNextMoveAwayFromTarget(g.getPacmanCurrentNodeIndex(), g.getGhostCurrentNodeIndex(DataGetter.getClosestGhost(g)), DM.PATH);
	}
}