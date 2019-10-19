package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import pacman.game.Game;
import pacman.game.Constants.*;

public abstract class Action {
	
	protected Game g;
	public void setGame(Game game)
	{
		this.g = game;
	}
	public abstract MOVE executeAction();
}
