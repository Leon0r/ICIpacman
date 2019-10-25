package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

public class Nine implements Transition {

	double distanceLimit;
	
	public Nine(double dLimit)
	{
		distanceLimit = dLimit;
	}
	
	@Override
	public boolean evaluate(Input in) {
		if(in.isEdible() && in.getDistancePC() < distanceLimit && in.getNonEdibleGhosts()==0) 			
			return true;		
		
		return false;
	}

}
