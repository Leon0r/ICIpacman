package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsPacMan;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.auxiliaryCode.Condition;

public class OneGhostNear implements Transition {
	//OneGhost()
	@Override
	public boolean evaluate(Input in) {
		return Condition.isOneGhostNear(in) && !Condition.isGhostEdible(in);
	}
}
