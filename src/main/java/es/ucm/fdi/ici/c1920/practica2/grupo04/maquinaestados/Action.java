package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import pacman.game.Game;

public abstract class Action {
	
	protected Game game;
	public void setGame(Game game)
	{
		this.game = game;
	}
	public abstract void executeAction();
}
