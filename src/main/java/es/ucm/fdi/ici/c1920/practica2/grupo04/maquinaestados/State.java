package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

public class State {
	
	private Action action;
	private String id;

	public State(String id, Action action) {
		super();
		this.action = action;
		this.id = id;
	}
	
	public int hashCode(){
		return this.id.hashCode();
	}
	
	public void execute(){
		this.action.executeAction();
	}
	

}