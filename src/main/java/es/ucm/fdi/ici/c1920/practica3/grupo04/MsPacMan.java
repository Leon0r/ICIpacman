package es.ucm.fdi.ici.c1920.practica3.grupo04;

import pacman.controllers.PacmanController;
import es.ucm.fdi.ici.c1920.practica3.grupo04.auxiliaryCode.DataGetter;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * The Class RandomPacMan.
 */
public final class MsPacMan extends PacmanController {


	private static final Double RUNAWAY_LIMIT = 15.0;
	private static final Double MAX_DISTANCE = 200.0;
	private static final Double MAX_CONFIDENCE = 100.0;

	FuzzyEngine fe;
	HashMap<String, Double> input;   HashMap<String, Double> output;

	public MsPacMan() {
		fe = new FuzzyEngine(FuzzyEngine.FUZZY_CONTROLLER.MSPACMAN);
		input = new HashMap<String,Double>();
		output = new HashMap<String,Double>();
	}

	private double[] distances = {MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE};
	private double[] confidences = {MAX_CONFIDENCE,MAX_CONFIDENCE,MAX_CONFIDENCE,MAX_CONFIDENCE};
	private double[] fleeFromGhosts = {0,0,0,0};
	private double[] eatGhosts = {0,0,0,0};
	private int[] powerPillIndexes; // has the powerpills indexes, -1 when pill eaten
	private int current;

	@Override
	public MOVE getMove(Game game, long timeDue) {
		MOVE move;
		int edibleGhosts = 0;
		int nearGhosts = 0;
		MOVE[] safeMoves;
		
		current = game.getPacmanCurrentNodeIndex();
		input.clear(); output.clear();

		if(powerPillIndexes == null)
			powerPillIndexes = game.getPowerPillIndices();

		for (GHOST ghost : GHOST.values()) {
			int index = game.getGhostCurrentNodeIndex(ghost);
			double distance = MAX_DISTANCE;
			if(index != -1) {
				distance = game.getShortestPathDistance(current, index);
			}
			if(distance != 0) { // With PO visibility non observable ghosts distance is 0.
				distances[ghost.ordinal()] = distance;
				confidences[ghost.ordinal()] = MAX_CONFIDENCE;
			}
			else if (confidences[ghost.ordinal()] > 0)
				confidences[ghost.ordinal()]--;

			input.put(ghost.name()+"distance", distances[ghost.ordinal()]);
			input.put(ghost.name()+"confidence", confidences[ghost.ordinal()]);
			input.put(ghost.name()+"EdibleTime", (double) game.getGhostEdibleTime(ghost));
		}


		System.out.println(Arrays.toString(distances));
		fe.evaluate("FuzzyMsPacMan", input, output);
		for (GHOST ghost : GHOST.values()) {
			fleeFromGhosts[ghost.ordinal()] = output.get("fleeFrom"+ghost.name());
			eatGhosts[ghost.ordinal()] = output.get("eat"+ghost.name());
		}
		nearGhosts = 0;
		edibleGhosts = 0;
		for(double ghost : fleeFromGhosts) {
			System.out.println(ghost);
			if(ghost > 23) {
				nearGhosts++;
			}
		}
		for(double ghost : eatGhosts) {
			System.out.println(ghost);
			if(ghost > 23) {
				edibleGhosts++;
			}
		}

		if(nearGhosts == 4) { ///// ESTO PUEDE DEVOLVER -1!!!
			int index = DataGetter.findNearestPowerPill(game, powerPillIndexes);
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), index, DM.PATH);
		}
		else {
			return goToPills(game);
			//return DataGetter.findPathWithMostPills(game, game.getPossibleMoves(game.getPacmanCurrentNodeIndex()));
		}
	}


	private MOVE runAwayFromClosestGhost(Game game) {
		System.out.println("RunAway");
		double min_distance = Double.MAX_VALUE;
		GHOST closest_ghost = null;
		for (GHOST ghost : GHOST.values()) {
			double distance = distances[ghost.ordinal()];    		
			if(distance < min_distance)
			{
				min_distance = distance;
				closest_ghost = ghost;
			}
		}
		return game.getNextMoveAwayFromTarget(current, game.getGhostCurrentNodeIndex(closest_ghost), DM.PATH);
	}

	private MOVE goToPills(Game game) {
		System.out.println("goToPills");
		int[] pills = game.getActivePillsIndices();
		//int[] powerPills = game.getActivePowerPillsIndices();

		ArrayList<Integer> targets = new ArrayList<Integer>();

		for (int i = 0; i < pills.length; i++)                    //check which pills are available
		{
			if ((game.isPillStillAvailable(i)!=null) &&game.isPillStillAvailable(i)!=null) {
				targets.add(pills[i]);
			}
		}

		for (int i = 0; i < powerPillIndexes.length; i++)            //check with power pills are available
		{
			if(powerPillIndexes[i] != -1)
				if ((game.isPowerPillStillAvailable(i)!=null) && game.isPowerPillStillAvailable(i)) {
					targets.add(powerPillIndexes[i]);
				}
		}

		int[] targetsArray = new int[targets.size()];        //convert from ArrayList to array
		for (int i = 0; i < targetsArray.length; i++) {
			targetsArray[i] = targets.get(i);
		}
		//return the next direction once the closest target has been identified
		int index = game.getClosestNodeIndexFromNodeIndex(current, targetsArray, DM.PATH);
		for (int i = 0; i < powerPillIndexes.length; i++) {
			if(powerPillIndexes[i] == index)
				powerPillIndexes[i] = -1; // power pill is going to be eaten
		}
		return game.getNextMoveTowardsTarget(current, index, DM.PATH);

	}
}