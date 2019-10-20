package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

public class GhostsCloseNoSafePathNotPowPill implements Transition {

	//GoToPillNotSafe();
	@Override
	public boolean evaluate(Input in) {
		if(nearGhosts.size() > 1) {
			if(numSafePath == 0) {					
				if(pathWithPowerPills(false) == null)
					return true;
			}
		}
		return false;
	}

}