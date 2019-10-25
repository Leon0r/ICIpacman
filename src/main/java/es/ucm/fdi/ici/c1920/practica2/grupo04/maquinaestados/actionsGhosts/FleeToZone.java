package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts;

import java.util.Vector;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FleeToZone extends Action {

	GHOST ghostType;
	
	public FleeToZone(GHOST gType) {
		this.ghostType = gType;
	}
	
	// Metodo que devuelve el nodo al que los fantasmas deben dirigirse cuando sean comestibles
	public int getAreaNode(Game game) {
		
		int[] powerPillIndices = game.getActivePowerPillsIndices();
		
		Vector<Integer> ghosts = new Vector<Integer>(0,1); 
		
		for(int k = 0; k < powerPillIndices.length && k<4; k++) {
			ghosts.add(powerPillIndices[k]);
		}
		
		if(ghosts.size() < 4) {
			int[] powerPillDownIndices = game.getPowerPillIndices();
			
			boolean skip = false;
			
			for(int h = 0; h<powerPillDownIndices.length && ghosts.size() < 4; h++) {
				skip = false;
				for(int j = 0; j<ghosts.size(); j++) {
					
					if(!skip) {
						if(powerPillDownIndices[h] == ghosts.elementAt(j))
							skip= true;						
					}				
				}
				
				if(!skip)
					ghosts.add(powerPillDownIndices[h]);
			}
		}
		
		switch (ghostType) {
		
			case BLINKY:
				return ghosts.elementAt(0);
			case INKY:
				return ghosts.elementAt(1);
			case PINKY:
				return ghosts.elementAt(2);
			case SUE:
				return ghosts.elementAt(3);
		}
		
		return 0;
	}
	
	public MOVE executeAction() {
		
		int zoneNode = getAreaNode(g);
		MOVE ghostMove = g.getNextMoveAwayFromTarget(g.getGhostCurrentNodeIndex(ghostType), zoneNode, g.getGhostLastMoveMade(ghostType),  DM.EUCLID);
		MOVE moveToPacman = g.getNextMoveTowardsTarget(g.getGhostCurrentNodeIndex(ghostType), g.getPacmanCurrentNodeIndex(), g.getGhostLastMoveMade(ghostType), DM.EUCLID);
		
		
		return compareMoves(ghostMove, moveToPacman, zoneNode, g);
	}
	
	private MOVE compareMoves(MOVE g, MOVE p, int zoneNode ,Game game) {
			
		// Si el movimiento que tengo que hacer me lleva hacia Pacman, buscamos una alternativa
		if(g == p) {
			
			MOVE move = MOVE.NEUTRAL;
			
			int Xmine = game.getNodeXCood(game.getGhostCurrentNodeIndex(ghostType));
			int Xzone = game.getNodeXCood(zoneNode);
			int Ymine = game.getNodeYCood(game.getGhostCurrentNodeIndex(ghostType));
			int Yzone = game.getNodeYCood(zoneNode);
			
			switch (g) {
			case UP:
				
				if(Xmine < Xzone) 
					move = MOVE.RIGHT;
				else
					move = MOVE.LEFT;
				
				break;
			case DOWN:
				
				if(Xmine < Xzone) 
					move = MOVE.RIGHT;
				else
					move = MOVE.LEFT;
				
				break;
			case LEFT:
				if(Xmine < Xzone) 
					move = MOVE.RIGHT;
				else
					move = MOVE.LEFT;
				break;
			case RIGHT:
				if(Ymine < Yzone) 
					move = MOVE.DOWN;
				else
					move = MOVE.UP;
				break;
			default:
				if(Ymine < Yzone) 
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