package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitions;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

public class GhostsCloseSafePath implements Transition {
	//GoToPillSafe()
	@Override
	public boolean evaluate(Input in) {
		if(nearGhosts.size() > 1) {					
			// if no safe path, its the only possibility
			if(numSafePath != 0) {
				return true;
			}
		return false;
	}
}
