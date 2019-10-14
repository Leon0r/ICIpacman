package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;


public interface StateEngine {
	public StateEngine add(State oldState, Transition transition, State newState) 
		throws IllegalStateException;
	
	public void setInitialState(State initialState);
	
	public void done() throws IllegalStateException;
	
	public State currentState();
	
	public void doIt(Input in, boolean debug);
}
