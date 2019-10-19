package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.conditions.Conditionals;

public class GhostEdible implements Transition {

	int limit = 30;
	
	@Override
	public boolean evaluate(Input in) {
		Conditionals.Conditions condition = new Conditionals.Conditions();
		if(condition.isEdible(in))
			return true;
		return false;
	}

}
