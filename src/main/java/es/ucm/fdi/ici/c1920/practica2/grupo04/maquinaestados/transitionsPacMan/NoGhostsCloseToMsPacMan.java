package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

public class NoGhostsCloseToMsPacMan implements Transition {

	//GoToPillSafe()
	@Override
	public boolean evaluate(Input in) {
		
		for(double d : in.getDistancetoGhosts())
			if(d < in.getDistanceLimit())
				return false;
		return true;
	}

}
