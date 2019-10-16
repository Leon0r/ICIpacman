package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actions;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;

public class GoToPillNotSafe extends Action {

	public void executeAction() {
		move = pathWithMorePills(game, false);
	}

}
