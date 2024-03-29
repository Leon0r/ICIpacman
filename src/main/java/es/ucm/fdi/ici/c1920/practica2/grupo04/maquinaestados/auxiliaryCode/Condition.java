package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.auxiliaryCode;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;

public final class Condition {
	private Condition() {}
	
	public static boolean isEdible(Input in) {
		return in.isEdible();
	}
	
	// Check nearGhosts
	public static boolean noGhostsNear(Input in) {
		return in.getNearGhosts() == 0;
	}
	public static boolean isOneGhostNear(Input in) {
		return in.getNearGhosts() == 1;
	}
	public static boolean areManyGhostsNear(Input in) {
		return in.getNearGhosts() > 1;
	}
	public static boolean isGhostEdible(Input in) {
		return in.getEdibleGhost();
	}
	
	// check Safe paths
	public static boolean areThereSafePaths(Input in) {
		return in.getAmountOfSafeMoves() > 0;
	}
}
