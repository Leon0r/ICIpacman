package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitions;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

public class GhostCloseSafePath implements Transition {
	//GoToPillSafe()
	@Override
	public boolean evaluate(Input in) {
		if(nearGhosts.size() == 1) {					
			if(numSafePath != 0) {
				return true;					
			}
		}
		return false;
	}
}
