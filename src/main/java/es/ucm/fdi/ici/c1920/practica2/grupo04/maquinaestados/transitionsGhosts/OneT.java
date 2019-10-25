package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;
import pacman.game.Constants.GHOST;

//1

public class OneT implements Transition {

	double distanceLimit;
	GHOST ghostType;
	int timeToChange;
	
	public OneT(double dLimit, int tToChange)
	{
		distanceLimit = dLimit;
		timeToChange = tToChange;
	}
	
	@Override
	public boolean evaluate(Input in) {
		
		
		if(!in.isEdible() && in.getDistancePC() < distanceLimit && in.getGhostTimer() > timeToChange) {
			
			if(in.getGhostType() == GHOST.SUE && in.getActivePowerPills() > 0) {
				System.out.println("From OneT siendo sue y pills");
				return true;
			}
			else if(in.getGhostType() == GHOST.SUE) {
				return false;
			}
			
			System.out.println("From OneT sin ser Sue");
			return true;
		}
		
		return false;
	}

}