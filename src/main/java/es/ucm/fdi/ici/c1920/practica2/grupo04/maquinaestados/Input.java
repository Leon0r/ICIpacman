package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

public class Input {

	private double[] distancetoGhosts;
	private double closestPowerPill;
	private double distanceLimit; // Distance which is considered near
	
	public double[] getDistancetoGhosts() {
		return distancetoGhosts;
	}
	public void setDistancetoGhosts(double[] distancetoGhosts) {
		this.distancetoGhosts = distancetoGhosts;
	}
	public double getClosestPowerPill() {
		return closestPowerPill;
	}
	public void setClosestPowerPill(double closestPowerPill) {
		this.closestPowerPill = closestPowerPill;
	}
	
	public double getDistanceLimit() {
		return distanceLimit;
	}
	public void setDistanceLimit(double distanceLimit) {
		this.distanceLimit = distanceLimit;
	}
	
	
	
}
