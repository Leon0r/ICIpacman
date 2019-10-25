package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;
import pacman.game.Constants.GHOST;

//7

public class GoToPacmanStrategic_T implements Transition {

	double distanceLimit;
	
	public GoToPacmanStrategic_T(double dLimit)
	{
		distanceLimit = dLimit;
	}
	
	@Override
	public boolean evaluate(Input in) {
		
		if(!in.isEdible() && in.getDistancePC() >= distanceLimit) {
			
			if(in.getGhostType()== GHOST.SUE && in.getActivePowerPills() > 0)
				return true;
			else if(in.getGhostType()== GHOST.SUE)
				return false;
			
			return true;		
		}
		
		return false;
	}

}
