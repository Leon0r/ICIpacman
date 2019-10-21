package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.conditions.Condition;

public class GhostsCloseSafePathGhostEdible implements Transition {
	//GoToGhost()
	@Override
	public boolean evaluate(Input in) {
		return Condition.isGhostEdible(in);
	}
}
