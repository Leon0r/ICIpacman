package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;
import pacman.game.Constants.GHOST;

//7

public class GoToPacmanStrategicTimer_T implements Transition {

	double distanceLimit;
	int timeToChange;
	
	public GoToPacmanStrategicTimer_T(double dLimit, int tToChange)
	{
		distanceLimit = dLimit;
		timeToChange = tToChange;
	}
	
	@Override
	public boolean evaluate(Input in) {		
		
		if(!in.isEdible() && (in.getDistancePC() >= distanceLimit || in.getGhostTimer() > timeToChange)) {
			
			if(in.getGhostType()== GHOST.SUE && in.getActivePowerPills() > 0) {
				//System.out.println("From SevenT siendo Sue");
				return true;
			}
			else if(in.getGhostType()== GHOST.SUE)
				return false;
			
			//System.out.println("From SevenT sin ser Sue");
			return true;		
		}
		
		return false;
	}

}
