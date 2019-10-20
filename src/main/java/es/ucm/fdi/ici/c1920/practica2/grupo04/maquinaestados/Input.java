package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class Input {
	
	double distancePC;
	boolean edible;
	int nearGhosts;
	
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
	
}
