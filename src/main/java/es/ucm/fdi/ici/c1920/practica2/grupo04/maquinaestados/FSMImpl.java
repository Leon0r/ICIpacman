package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import pacman.game.Game;

public class FSMImpl implements FSM {
	
	private boolean debug;
	protected StateEngine stateEngine;
	
	public FSMImpl (StateEngine se) {
		this(se,  false);
	}
	
	public FSMImpl (StateEngine se, boolean debug) {
		stateEngine = se;
		this.debug = debug;
	}
	
	@Override
	public State getState() {
		return stateEngine.currentState();
	}

	@Override
	public boolean checkChange(Input in) {
		return stateEngine.checkChange(in, debug);
	}

	@Override
	public void setGame(Game game) {		
	}
}
