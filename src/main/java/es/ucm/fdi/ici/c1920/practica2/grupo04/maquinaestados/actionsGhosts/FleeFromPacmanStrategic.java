package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FleeFromPacmanStrategic extends Action {

	GHOST ghostType;
	
	public FleeFromPacmanStrategic(GHOST gType) {
		this.ghostType = gType;
	}
	
	
	@Override
	public MOVE executeAction() {
		
		double distances[] = new double[4];
		
		for(int i = 0; i < distances.length; i++) {			
			distances[i] = g.getDistance(g.getPacmanCurrentNodeIndex(), g.getGhostCurrentNodeIndex(GHOST.values()[i]), DM.EUCLID);
		}
		
		int lessDistanceGindex = -1;
		double lessDistance = Double.MAX_VALUE;
		
		for(GHOST gType: GHOST.values()) {
			if(!g.isGhostEdible(gType) && gType != ghostType) { // Si edible y no yo
				if(distances[gType.ordinal()] < lessDistance) { // Si esta mas cerca que el mas cercano
					lessDistance = distances[gType.ordinal()];
					lessDistanceGindex = gType.ordinal();
				}
			}
		}
		
		// Se supone que en este estado siempre hay por lo menos un fantasma vivo (distinto a ti) no comestible pues es la condición para pasar
		
		//if(lessDistanceGindex != -1) {
			
			
			MOVE moveToGhost = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), g.getGhostCurrentNodeIndex(GHOST.values()[lessDistanceGindex]), g.getGhostLastMoveMade(ghostType), DM.EUCLID);
			MOVE moveToPacman = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(ghostType), DM.EUCLID);
			
			return compareMoves(moveToGhost, moveToPacman,lessDistanceGindex, g);
		//}
		//else {
		//	return g.getNextMoveAwayFromTarget(g.getGhostCurrentNodeIndex(ghostType), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(ghostType), DM.EUCLID);
		//}
	}
	
	private MOVE compareMoves(MOVE g, MOVE p, int otherGhostIndex ,Game game) {
		
		// Si el movimiento que tengo que hacer me lleva hacia Pacman, buscamos una alternativa
		if(g == p) {
			
			MOVE move = MOVE.NEUTRAL;
			
			int Xmine = game.getNodeXCood(game.getGhostCurrentNodeIndex(ghostType));
			int Xghost = game.getNodeXCood(game.getGhostCurrentNodeIndex(GHOST.values()[otherGhostIndex]));
			int Ymine = game.getNodeYCood(game.getGhostCurrentNodeIndex(ghostType));
			int Yghost = game.getNodeYCood(game.getGhostCurrentNodeIndex(GHOST.values()[otherGhostIndex]));
			
			switch (g) {
			case UP:
				
				if(Xmine < Xghost) 
					move = MOVE.RIGHT;
				else
					move = MOVE.LEFT;
				
				break;
			case DOWN:
				
				if(Xmine < Xghost) 
					move = MOVE.RIGHT;
				else
					move = MOVE.LEFT;
				
				break;
			case LEFT:
				if(Xmine < Xghost) 
					move = MOVE.RIGHT;
				else
					move = MOVE.LEFT;
				break;
			case RIGHT:
				if(Ymine < Yghost) 
					move = MOVE.DOWN;
				else
					move = MOVE.UP;
				break;
			default:
				if(Ymine < Yghost) 
					move = MOVE.DOWN;
				else
					move = MOVE.UP;
				break;
			}
			
			return move;
		}
		else {
			return g;
		}
		
	}
}