package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts.*;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts.*;
import pacman.game.*;

public class MyFSMGhosts extends FSMImpl{
	
	int idx = 0;
	
	public MyFSMGhosts(StateEngine se, int ghostIndex) {
		super(se);
		idx = ghostIndex;		
	}
	
	public void Init(Game game) {
		
		GoToPacman gtp = new GoToPacman(idx);
		gtp.setGame(game);
		
		State inicial = new State("GoToPacman", gtp);
		
		stateEngine.setInitialState(inicial);

		stateEngine.add(inicial, new GhostEdible(), new State("FleeFromPacman", new FleeFromPacman(idx)));
		
	}
}
