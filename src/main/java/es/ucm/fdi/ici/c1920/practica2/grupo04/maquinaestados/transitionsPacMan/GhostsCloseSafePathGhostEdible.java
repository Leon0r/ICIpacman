package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

public class GhostsCloseSafePathGhostEdible implements Transition {
	//GoToGhost()
	@Override
	public boolean evaluate(Input in) {
		if(nearGhosts.size() > 1) {					
			// if no safe path, its the only possibility
			if(numSafePath != 0) {
				if(!edibleG.isEmpty()) {
					move = moveTowardsEdibleGhost(game, edibleG);
					if (!moveIsSuicide(game, move)) return true;
				}
			}
		}
		return false;
	}
}
