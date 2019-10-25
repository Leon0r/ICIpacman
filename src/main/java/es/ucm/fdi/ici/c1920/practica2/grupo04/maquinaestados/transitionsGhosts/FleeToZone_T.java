package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

//3

public class FleeToZone_T implements Transition {

	double distanceLimit;
	
	public FleeToZone_T(double dLimit)
	{
		distanceLimit = dLimit;
	}
	
	@Override
	public boolean evaluate(Input in) {
		
		if(in.isEdible() && in.getDistancePC() >= distanceLimit)
			return true;
		
		return false;
	}

}