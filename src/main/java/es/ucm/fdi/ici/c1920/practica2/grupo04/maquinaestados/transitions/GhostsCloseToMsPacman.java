package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitions;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

public class GhostsCloseToMsPacman implements Transition {

	@Override
	public boolean evaluate(Input in) {
		for(double d : in.getDistancetoGhosts())
			if(d < in.getDistanceLimit())
				return true;
		return false;
	}

}
