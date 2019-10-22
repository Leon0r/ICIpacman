package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import pacman.game.Constants.MOVE;

public class Input {
	
	double distancePC;
	boolean edible;
	int nearGhosts;
	boolean edibleGhost;
	MOVE [] safeMovesPC;
	
	public double getDistancePC() {
		return distancePC;
	}
	public void setDistancePC(double distancePC) {
		this.distancePC = distancePC;
	}
	
	public boolean isEdible() {
		return edible;
	}
	public void setEdible(boolean edible) {
		this.edible = edible;
	}
	
	public double getNearGhosts() {
		return nearGhosts;
	}
	public void setNearGhosts(int nearGhosts) {
		this.nearGhosts = nearGhosts;
	}
	
	public boolean getEdibleGhost() {
		return this.edibleGhost;
	}
	public void setEdibleGhost(boolean edibleGhost) {
		this.edibleGhost = edibleGhost;
	}
	
	
	public MOVE [] getSafeMovesPC() {
		return safeMovesPC;
	}
	public void setSafeMovesPC(MOVE[] safeMovesPC) {
		this.safeMovesPC = safeMovesPC;
	}


}
