package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

//2

public class FleeFromPacman_T implements Transition {
	
	double distanceLimit;
	
	public FleeFromPacman_T(double dLimit)
	{
		distanceLimit = dLimit;
	}
	
	@Override
	public boolean evaluate(Input in) {
				
		 if(in.isEdible() && in.getDistancePC()<distanceLimit) {			 
			 return true;			 
		 }
		
		return false;
	}

}
