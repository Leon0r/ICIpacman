package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitions;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;
import pacman.game.Constants.DM;

public class GhostsCloseSafePathPowPill implements Transition {
	//GoToPowerPillSafe()
	@Override
	public boolean evaluate(Input in) {
		if(nearGhosts.size() > 1) {					
			// if no safe path, its the only possibility
			if(numSafePath != 0) {
				nearestPowP = findNearestPowerPill(game);
				if(nearestPowP != -1) {
					move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPowP, game.getPacmanLastMoveMade(), DM.PATH);
					if(!moveIsSuicide(game, move)) return true;
				}

			}
		}
		return false;
	}
}
