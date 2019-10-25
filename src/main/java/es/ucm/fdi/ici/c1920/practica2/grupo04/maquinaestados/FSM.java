package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import java.util.Map;

import pacman.game.Game;

public interface FSM {
	public State getState();
	
	public boolean checkChange(Input in);
	
	public void setGame(Game game);
}
