package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;
import pacman.game.Constants.GHOST;

//5

public class Five implements Transition {

	double distanceLimit;
	
	public Five(double dLimit)
	{
		distanceLimit = dLimit;
	}
	
	@Override
	public boolean evaluate(Input in) {
		
		if(!in.isEdible() && in.getDistancePC() >= distanceLimit && in.getGhostType() == GHOST.SUE && in.getActivePowerPills() == 0)
			return true;
		
		return false;
	}

}
