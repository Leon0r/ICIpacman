package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Input {

	// GHOSTS
	double distancePC;
	boolean edible;
	int nonEdibleGhosts;
	GHOST ghostType;
	int activePowerPills;
	int ghostTimer;
	
	// PACMAN
	int nearGhosts;
	boolean edibleGhost;
	MOVE [] safeMoves;
	MOVE [] allMoves;
	
	
	//// GHOSTS //////////////////////////////////////////////
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

	public int getNonEdibleGhosts() {
		return nonEdibleGhosts;
	}
	public void setNonEdibleGhosts(int nonEdibleGhosts) {
		this.nonEdibleGhosts = nonEdibleGhosts;
	}
	
	public GHOST getGhostType() {
		return ghostType;
	}
	public void setGhostType(GHOST ghostType) {
		this.ghostType = ghostType;
	}
	
	public int getActivePowerPills() {
		return activePowerPills;
	}
	public void setActivePowerPills(int activePowerPills) {
		this.activePowerPills = activePowerPills;
	}
	
	public int getGhostTimer() {
		return ghostTimer;
	}
	public void setGhostTimer(int ghostTimer) {
		this.ghostTimer = ghostTimer;
	}
	
	//// PACMAN //////////////////////////////////////////////
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


	public MOVE [] getSafeMoves() {
		return safeMoves;
	}
	public void setSafeMoves(MOVE[] allMoves, int fin) {

		MOVE[] aux= new MOVE[fin+1];
		for(int i = 0; i<=fin; i++)
			aux[i] = allMoves[i];

		this.safeMoves = aux;
	}
	public int getAmountOfSafeMoves() {
		return safeMoves.length;
	}

	public MOVE [] getAllMoves() {
		return allMoves;
	}
	public void setAllMoves(MOVE[] allMoves) {
		this.allMoves = allMoves;
	}
}
