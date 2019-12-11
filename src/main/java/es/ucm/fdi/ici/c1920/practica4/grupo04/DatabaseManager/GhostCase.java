package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

import pacman.game.Constants.MOVE;

public class GhostCase {
	
	public double[] distanceToPacman = new double[4];
	public boolean[] edibleGhosts = new boolean[4];
	public boolean[] activePPills = new boolean[4];
	public double [][] distanceToPPills = new double[4][4];
	public double [] distancePCToPPills = new double[4];
	public int nearestPPillToPacman;
	public MOVE movement;
	
	public GhostCase(double[] distanceToPacman, boolean[] edibleGhosts, double [][] distanceToPPills, 
			double [] distancePCToPPills, boolean[] activePPills, int nearestPPillToPacman, int movement)
	{
		this.distanceToPacman = distanceToPacman;
		this.edibleGhosts = edibleGhosts;
		this.activePPills = activePPills;
		this.distanceToPPills = distanceToPPills;
		this.distancePCToPPills = distancePCToPPills;
		this.nearestPPillToPacman = nearestPPillToPacman;
		
		switch (movement) {
			case 0:
				this.movement = MOVE.LEFT;
				break;
			case 1:
				this.movement = MOVE.UP;
				break;
			case 2:
				this.movement = MOVE.RIGHT;
				break;
			case 3:
				this.movement = MOVE.DOWN;
				break;
		}
	}
	
}
