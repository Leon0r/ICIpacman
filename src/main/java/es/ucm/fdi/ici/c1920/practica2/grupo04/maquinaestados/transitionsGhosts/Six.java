package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Transition;

//6

public class Six implements Transition {

	@Override
	public boolean evaluate(Input in) {
		
		//System.out.println(in.getNonEdibleGhosts());
		
		if(in.isEdible() && in.getNonEdibleGhosts() > 0)
			return true;
		
		
		return false;
	}

}
