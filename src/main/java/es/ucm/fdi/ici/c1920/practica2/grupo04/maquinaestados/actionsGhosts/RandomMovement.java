package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts;

import java.util.Random;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RandomMovement extends Action { // Random movement but only for SUE

	GHOST ghostType;
	Random rnd;
	
	public RandomMovement(GHOST gType) {
		this.ghostType = gType;
		rnd = new Random();
	}
	
	@Override
	public MOVE executeAction() {
		
		return MOVE.values()[rnd.nextInt(4)];
	}

}
