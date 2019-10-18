package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import pacman.game.Constants.MOVE;

public class State {
	
	private Action action;
	public String id;

	public State(String id, Action action) {
		super();
		this.action = action;
		this.id = id;
	}
	
	
	public int hashCode(){
		return this.id.hashCode();
	}
	
	
	public MOVE execute(){
		System.out.println("Ejecuto la accion desde " + id.toString());
		
		return this.action.executeAction();
	}
	

}