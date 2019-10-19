package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.conditions;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;

public class Conditionals {
	public static class Conditions{
		public boolean isEdible(Input in) {
			if(in.isEdible())
				return true;
			return false;
		}
	}
}
