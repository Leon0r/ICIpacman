package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

public class MsPacmanCase {
	public boolean[] edibleGhosts = new boolean[4];
	public int [] characterIndex = new int[5];
	public int [] characterLastMove = new int[5];
	public boolean[] activePPills = new boolean[4];
	public int nearestPPillToPacman;
	public int nonEdibleGhosts;
	
	public MsPacmanCase(boolean[] edibleGhosts, int [] characterIndex, int [] characterLastMove,
						boolean[] activePPills, int nearestPPillToPacman, int nonEdibleGhosts)
	{
		this.edibleGhosts = edibleGhosts;
		this.characterIndex = characterIndex;
		this.characterLastMove = characterLastMove;
		this.activePPills = activePPills;
		this.nearestPPillToPacman = nearestPPillToPacman;
		this.nonEdibleGhosts = nonEdibleGhosts;	
	}
}
