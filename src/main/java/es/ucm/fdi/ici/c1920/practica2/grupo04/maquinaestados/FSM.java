package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import java.util.Map;

public interface FSM {
	public State getState();
	
	public void checkChange(Input in);
}
