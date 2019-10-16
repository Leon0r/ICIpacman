package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitions;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

public class NoGhostsCloseToMsPacMan implements Transition {

	//GoToPillSafe()
	@Override
	public boolean evaluate(Input in) {
		if(nearGhosts.isEmpty()) {
			return true;
		}	
		return false;
	}

}
