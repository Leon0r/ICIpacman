package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

import pacman.game.Constants.MOVE;

public class MsPacmanCase {
	public boolean[] edibleGhosts = new boolean[4];
	public int [] characterIndex = new int[5];
	public int [] characterLastMove = new int[5];
	public int[] activePPills = new int[4];
	public int nearestPPillToPacman;
	public int nonEdibleGhosts;
	
	public MOVE movement; // the solution movement for the case
	
	public MsPacmanCase(boolean[] edibleGhosts, int [] characterIndex, int [] characterLastMove,
						int[] activePPills, int nearestPPillToPacman, int nonEdibleGhosts, int movement)
	{
		this.edibleGhosts = edibleGhosts;
		this.characterIndex = characterIndex;
		this.characterLastMove = characterLastMove;
		this.activePPills = activePPills;
		this.nearestPPillToPacman = nearestPPillToPacman;
		this.nonEdibleGhosts = nonEdibleGhosts;	
		
		this.movement = MOVE.values()[movement];
	}
}
