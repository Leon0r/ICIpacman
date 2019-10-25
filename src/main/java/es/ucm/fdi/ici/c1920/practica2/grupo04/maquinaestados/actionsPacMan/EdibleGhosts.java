package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.auxiliaryCode.DataGetter;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class EdibleGhosts extends Action {

	public MOVE executeAction() {
		return g.getNextMoveTowardsTarget(g.getPacmanCurrentNodeIndex(), g.getGhostCurrentNodeIndex(DataGetter.findEdibleGhost(g)), DM.PATH);
	}
}