package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

public class GhostCase {
	public double[] distanceToPacman = new double[4];
	public boolean[] edibleGhosts = new boolean[4];
	public double [][] distanceToPPills = new double[4][4];
	public double [] distancePCToPPills = new double[4];
	public int nearestPPillToPacman;
	public int nonEdibleGhosts;
	
	public GhostCase(double[] distanceToPacman, boolean[] edibleGhosts, double [][] distanceToPPills, 
			double [] distancePCToPPills, int nearestPPillToPacman, int nonEdibleGhosts)
	{
		this.distanceToPacman = distanceToPacman;
		this.edibleGhosts = edibleGhosts;
		this.distanceToPPills = distanceToPPills;
		this.distancePCToPPills = distancePCToPPills;
		this.nearestPPillToPacman = nearestPPillToPacman;
		this.nonEdibleGhosts = nonEdibleGhosts;	
	}
}
