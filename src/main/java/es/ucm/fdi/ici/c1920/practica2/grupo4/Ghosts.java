package es.ucm.fdi.ici.c1920.practica2.grupo4;

import java.util.EnumMap;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Input;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.MyFSMGhosts;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.StateEngineImpl;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public final class Ghosts extends GhostController{

	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	private StateEngineImpl[] se = new StateEngineImpl[4];	
	private MyFSMGhosts[] fsm = new MyFSMGhosts[4];
	private int[] timers = new int[4];
	private int[] lastTimes = new int[4];
	
	Input in = new Input();
	
	int level = 0;
	
	public Ghosts() {
		for (int i = 0; i<4; i++) {
			se[i] = new StateEngineImpl();
			fsm[i] = new MyFSMGhosts(se[i], GHOST.values()[i]);
			fsm[i].Init();
			
			timers[i] = 0;
		}
		
	}
	
	private void fillInput(Game game, GHOST ghostType) {
		in.setDistancePC(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH));
		in.setEdible(game.isGhostEdible(ghostType));
		in.setGhostType(ghostType);	
		in.setActivePowerPills(game.getActivePowerPillsIndices().length);
		in.setGhostTimer(timers[ghostType.ordinal()]);
		
		// Meter el numero de fantasmas no edibles que no son tu
		
		int nonEdibleGhosts = 0;
		
		// Añadimos el total de fantasmas no comestibles
		for (GHOST gType : GHOST.values())
			if(!game.isGhostEdible(gType) && game.getGhostLairTime(gType) <= 0)
				nonEdibleGhosts++;		
		
		// Si el propio fantasma es no comestible, lo restamos
		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0)
			nonEdibleGhosts--;
		
		in.setNonEdibleGhosts(nonEdibleGhosts);
	}
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {

		// Hace el set del Game, con eso deberia ir
		for(MyFSMGhosts sm : fsm)
			sm.setGame(game);
		
		if(level != game.getCurrentLevel()) {
			level = game.getCurrentLevel();
			for(int i = 0; i < 4; i++) {
				timers[i] = 0;
				lastTimes[i] = 0;
			}
		}
		
		for(GHOST ghostType : GHOST.values()) {
			if(game.doesGhostRequireAction(ghostType)) {
				
				fillInput(game, ghostType);
				
				if(fsm[ghostType.ordinal()].checkChange(in)) {
					timers[ghostType.ordinal()] = 0;
					lastTimes[ghostType.ordinal()] = game.getCurrentLevelTime();
				}
				else
					timers[ghostType.ordinal()] = game.getCurrentLevelTime() - lastTimes[ghostType.ordinal()];
				
				moves.put(ghostType, 
						fsm[ghostType.ordinal()].getState().execute());					
			}
		}		
	return moves;
	}	
}
