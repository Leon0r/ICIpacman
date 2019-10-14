package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

public class FSMImpl implements FSM {
	
	private boolean debug;
	private StateEngine stateEngine;
	
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
	public void doIt(Input in) {
		// TODO Auto-generated method stub
		stateEngine.doIt(in, debug);
	}
}
