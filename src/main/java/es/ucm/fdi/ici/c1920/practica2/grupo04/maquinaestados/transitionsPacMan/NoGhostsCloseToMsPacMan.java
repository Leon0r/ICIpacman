package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.conditions.Condition;

public class NoGhostsCloseToMsPacMan implements Transition {

	//GoToPillSafe()
	@Override
	public boolean evaluate(Input in) {				
		return Condition.noGhostsNear(in);
	}

}
