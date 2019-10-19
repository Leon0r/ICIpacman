package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import java.util.EnumMap;

import java.util.Vector;
import java.util.Random;

import pacman.game.Game;
import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public final class Ghosts extends GhostController{

	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	private StateEngineImpl[] se = new StateEngineImpl[4];	
	private MyFSMGhosts[] fsm = new MyFSMGhosts[4];
	
	Input in = new Input();
	
	boolean done = false;
	
	public Ghosts() {
		for (int i = 0; i<4; i++) {
			se[i] = new StateEngineImpl();
			fsm[i] = new MyFSMGhosts(se[i], i);
		}
	}
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		if(!done) {
			done = true;
			for (int i = 0; i<4; i++) {
				fsm[i].Init(game);
			}
		}
		
		
		for(GHOST ghostType : GHOST.values()) {
			if(game.doesGhostRequireAction(ghostType)) {
				
				//in.setDistancesToPC(game.getDistance(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), DM.EUCLID));
				
				in.distancePC = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.EUCLID);
				
				fsm[ghostType.ordinal()].checkChange(in);				
				
				moves.put(ghostType, 
						fsm[ghostType.ordinal()].getState().execute());					
			}
		}		
	return moves;
	}	
}
