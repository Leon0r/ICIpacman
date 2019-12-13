package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

import java.util.ArrayList;

import java.util.*;
import java.util.List;
import es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager.jsonParser;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostDataBase {
	
	// Contador de partidas
	static int matchCont = -1; 
	
	// Maximo numero de partidas guardadas que seran relevantes
	static int maxSavedMatches = 25;
	
	// Casos de lectura
	static List<GhostCase> GhostCases = new ArrayList<GhostCase>();
	// Casos generados durante la partida
	static List<GhostCase> generatedGhostCases = new ArrayList<GhostCase>();
	
	static String opponentGhostName;
	static int limitSize;
	
	public static int getMatchCont() {
		return matchCont;
	}
	
	public static void setMatchCont(int v) {
		matchCont = v;
	}
	
	public static int getMatchMaxSaved() {
		return maxSavedMatches;
	}
	
	public static void increaseMatchCont() {
		matchCont++;
	}	
	
	public static void setOpponentGhostName(String name) {
		opponentGhostName = name;
	}
	
	public static void setLimit(int size) {
		limitSize = size;
	}
	
	
	public static void addGeneratedCase(GhostCase ghostCase) {
		generatedGhostCases.add(ghostCase);
	}
	
	public static void addCase(GhostCase ghostCase) {
		GhostCases.add(ghostCase);
	}
	
	// Necesitas el caso y el estado del game (aunque seguramete no te haga falta pasarlo todo el rato porque miras distancias entre nodos)
	public static MOVE compareGhostCase(GhostCase ghostCase, Game game, int ghostIndex) {
		// Tener el peso total añadido para cada direccion según los 20 casos más importantes (Esto puede suponer demasiado tiempo)
		double valueUp = 0.001;
		double valueDown = 0.001;
		double valueLeft = 0.001;
		double valueRight = 0.001;
		
		PriorityQueue<Double> leftQueue = new PriorityQueue<Double>(1, Collections.reverseOrder());
		PriorityQueue<Double> rightQueue = new PriorityQueue<Double>(1, Collections.reverseOrder());
		PriorityQueue<Double> upQueue = new PriorityQueue<Double>(1, Collections.reverseOrder());
		PriorityQueue<Double> downQueue = new PriorityQueue<Double>(1, Collections.reverseOrder());
		
		// Nos guardamos el valor de parecido segun la direccion
		for(int h = 0; h < GhostCases.size(); h++ ) {
			// Para cada caso comparamos cuanto se parece al caso a comparar
			GhostCase c = GhostCases.get(h);
			
			// EDIBLE STATE
			// ESTO ALTERA TODO, SI ERES EDIBLE Y EL CASO NO, A TOMAR POR CLETA
			if(c.edibleGhosts[ghostIndex] != ghostCase.edibleGhosts[ghostIndex]) {
				continue;
			}
			
			boolean isEdible = false;
			
			if(c.edibleGhosts[ghostIndex]) {
				isEdible = true;
			}
						
			double totalValue = 0.0; 				// Suma total acumulada de cada atributo			
			double totalAcummulatedValue = 0.0;		// Atributos totales			
			double aux = 0.0;						// Variable auxiliar para la suma
						
			
			if(isEdible) {
				// ACTIVE POWER PILLS
				for(int i = 0; i<4; i++)
					if(c.activePPills[i] == ghostCase.activePPills[i]) 
						aux += 0.25;				
							
				totalValue += aux;
				totalAcummulatedValue += 1.0;
			}
			else {
				// ACTIVE POWER PILLS
				for(int i = 0; i<4; i++)
					if(c.activePPills[i] == ghostCase.activePPills[i]) 
						aux += 0.5;				
							
				totalValue += aux;
				totalAcummulatedValue += 2.0;
			}
			// -------------------------------------------------------------
			
			
			// DISTANCIAS DE PACMAN A LAS POWER PILLS
			aux = 0.0;
			double maxDist = 50.0;
			
			if(isEdible) {
				for(int i = 0; i<4; i++) {
					if(ghostCase.activePPills[i]) {
						double distA = c.distancePCToPPills[i]; 
						double distB = ghostCase.distancePCToPPills[i];
						
						double v = (Math.abs(distA-distB) / maxDist);
						if(v>1)
							v = 1;
						else if (v<0)
							v= 0;
						
						aux += 0.25 * (1.0 - v);
					}
				}
				
				totalValue += aux;
				totalAcummulatedValue += 1.0;
			}
			else {				
				for(int i = 0; i<4; i++) {
					if(ghostCase.activePPills[i]) {
						double distA = c.distancePCToPPills[i]; 
						double distB = ghostCase.distancePCToPPills[i];
						
						double v = (Math.abs(distA-distB) / maxDist);
						if(v>1)
							v = 1;
						else if (v<0)
							v= 0;
						
						aux += (1.0 - v);
					}
				}
				
				totalValue += aux;
				totalAcummulatedValue += 4.0;				
			}
			//-------------------------------------------------------------
			
			
			// DISTANCIAS A PACMAN
			aux = 0;
			
			if(isEdible) {
				for(int i = 0; i<4; i++) {
					double distA = c.distanceToPacman[i]; 
					double distB = ghostCase.distanceToPacman[i];
					
					double v = (Math.abs(distA-distB) / maxDist);
					if(v>1)
						v = 1;
					else if (v<0)
						v= 0;
					
					aux += (1 - v);
				}
				
				totalValue += aux;
				totalAcummulatedValue += 4.0;
			}
			else {
				// TODO
				for(int i = 0; i<4; i++) {
					double distA = c.distanceToPacman[i]; 
					double distB = ghostCase.distanceToPacman[i];
					
					double v = (Math.abs(distA-distB) / maxDist);
					if(v>1)
						v = 1;
					else if (v<0)
						v= 0;
					
					aux += 0.5 * (1 - v);
				}
				
				totalValue += aux;
				totalAcummulatedValue += 2.0;
			}
			//-------------------------------------------------------------
			
			// POSICIONES DE PACMAN Y LOS FANTASMAS
			int ghostX = game.getNodeXCood(ghostCase.characterIndex[ghostIndex]);
			int ghostY = game.getNodeYCood(ghostCase.characterIndex[ghostIndex]);			
			int pacmanX = game.getNodeXCood(ghostCase.characterIndex[4]);
			int pacmanY = game.getNodeYCood(ghostCase.characterIndex[4]);
			
			aux = 0;
			
			int distX = Math.abs(ghostX - pacmanX);
			int distY = Math.abs(ghostY - pacmanY);
			boolean vertical = false;
			
			MOVE m;
			
			if(distY > distX)
				vertical = true;			
			
			boolean pacmanUp = false;
			boolean pacmanLeft = false;
			if(vertical) {
				if(pacmanY < ghostY)
					pacmanUp = true;
			}
			else {
				if(pacmanX < ghostX)
					pacmanLeft = true;
			}
			
			if(isEdible) {
				if(vertical) {
					if(pacmanUp)
						m = MOVE.DOWN;					
					else
						m = MOVE.UP;
				}
				else {
					if(pacmanLeft)
						m = MOVE.RIGHT;					
					else
						m = MOVE.LEFT;
				}
			}
			else {
				if(vertical) {
					if(pacmanUp)
						m = MOVE.UP;					
					else
						m = MOVE.DOWN;
				}
				else {
					if(pacmanLeft)
						m = MOVE.LEFT;					
					else
						m = MOVE.RIGHT;
				}
			}
			
			// Comparamos el movimiendo que deberiamos hacer con el movimiento que tiene asignado el caso con el que comparamos
			// Supone un elemento muy importante en el PACMAN
			switch (c.movement) {
			case LEFT:
				if(m == MOVE.LEFT) {
					aux += 6.0;
				}else if(m == MOVE.UP || m == MOVE.DOWN) {
					aux += 1.5;
				}				
				break;
			case RIGHT:
				if(m == MOVE.RIGHT) {
					aux += 6.0;
				}else if(m == MOVE.UP || m == MOVE.DOWN) {
					aux += 1.5;
				}
				break;
			case UP:
				if(m == MOVE.UP) {
					aux += 6.0;
				}else if(m == MOVE.LEFT || m == MOVE.RIGHT) {
					aux += 1.5;
				}
				break;
			case DOWN:
				if(m == MOVE.DOWN) {
					aux += 6.0;
				}else if(m == MOVE.LEFT || m == MOVE.RIGHT) {
					aux += 1.5;
				}
				break;
			}
			
			totalValue += aux;
			totalAcummulatedValue += 6.0;
			
			// ------------------------------------------------------------
			
			
			// DISTANCIAS DE PACMAN A LAS POWER PILLS
			aux = 0;
			if(isEdible) {
				for(int i = 0; i<4; i++) {
					for(int k = 0; k<4; k++) {
						if(ghostCase.activePPills[i]) {
							double distA = c.distanceToPPills[i][k];
							double distB = ghostCase.distanceToPPills[i][k];
							
							double v = (Math.abs(distA-distB) / maxDist);
							if(v>1)
								v = 1;
							else if (v<0)
								v= 0;
							
							aux += 0.25 * (1 - v);
						}
					}
				}
				
				totalValue += aux;
				totalAcummulatedValue += 1.0;
			}
			else {
				for(int i = 0; i<4; i++) {
					for(int k = 0; k<4; k++) {
						if(ghostCase.activePPills[i]) {
							double distA = c.distanceToPPills[i][k];
							double distB = ghostCase.distanceToPPills[i][k];
							
							double v = (Math.abs(distA-distB) / maxDist);
							if(v>1)
								v = 1;
							else if (v<0)
								v= 0;
							
							aux += 0.125 * (1 - v);
						}
					}
				}
				
				totalValue += aux;
				totalAcummulatedValue += 0.5;
			}
			//-------------------------------------------------------------
			
			
			// ESTADO DE LOS FANTASMAS
			aux = 0;
			if(isEdible) {
				for(int i = 0; i<4; i++) {
					if(c.edibleGhosts[i] == ghostCase.edibleGhosts[i]) 
						aux += 0.5;					
				}
			
				totalValue += aux;
				totalAcummulatedValue += 2.0;
			}
			else {
				for(int i = 0; i<4; i++) {
					if(c.edibleGhosts[i] == ghostCase.edibleGhosts[i]) 
						aux += 0.25;					
				}
			
				totalValue += aux;
				totalAcummulatedValue += 1.0;
			}
			
			//-------------------------------------------------------------
			
			// DISTANCIA DE PACMAN A LA POWER PILL MAS CERCANA
			aux = 0;
			if(isEdible) {
				int a = c.nearestPPillToPacman; 
				int b = ghostCase.nearestPPillToPacman;
				
				double dist = game.getDistance(a, b, DM.PATH);
				
				double v = (dist / maxDist);
				if(v>1)
					v = 1;
				else if (v<0)
					v= 0;
				
				aux = 0.125 * (1 - v);			
				
				totalValue += aux;
				totalAcummulatedValue += 0.5;
			}else {
				int a = c.nearestPPillToPacman; 
				int b = ghostCase.nearestPPillToPacman;
				
				double dist = game.getDistance(a, b, DM.PATH);
				
				double v = (dist / maxDist);
				if(v>1)
					v = 1;
				else if (v<0)
					v= 0;
				
				aux = (1 - v);			
				
				totalValue += aux;
				totalAcummulatedValue += 4.0;
			}
			
			
			//-------------------------------------------------------------
			
			
			// Parecido al caso a comparar
			double finalValue = totalValue/totalAcummulatedValue;
			
			// Lo metemos en una lista de valores segun su movimiento
			switch(c.movement) {
				case LEFT:
					leftQueue.add(finalValue);
					break;
				case RIGHT:
					rightQueue.add(finalValue);
					break;
				case UP:
					upQueue.add(finalValue);
					break;
				case DOWN:
					downQueue.add(finalValue);
					break;
			}
		}
		
		for(int h = 0; h<5; h++) {
			if(upQueue.isEmpty())
				break;
			else
				valueUp += upQueue.poll();
		}
		for(int h = 0; h<5; h++) {
			if(rightQueue.isEmpty())
				break;
			else
				valueRight += rightQueue.poll();
		}
		for(int h = 0; h<5; h++) {
			if(leftQueue.isEmpty())
				break;
			else
				valueLeft += leftQueue.poll();
		}
		for(int h = 0; h<5; h++) {
			if(downQueue.isEmpty())
				break;
			else
				valueDown += downQueue.poll();
		}
		
		MOVE[] movimientos = game.getPossibleMoves(game.getGhostCurrentNodeIndex(GHOST.values()[ghostIndex]));		
		
		int l = 0;
		if(GhostCases.size() > 0) {			
			MOVE m = MOVE.UP;			
			boolean found = false;
			
			do {
				double value = valueUp;		
				
				if(valueRight > value) {
					value = valueRight;
					m = MOVE.RIGHT;
				}
				
				if(valueDown > value) {
					value = valueDown;
					m = MOVE.DOWN;
				}
				
				if(valueLeft > value) {
					value = valueLeft;
					m = MOVE.LEFT;
				}
				
				for(int i = 0 ; i < movimientos.length; i++) {
					if(movimientos[i] == m) {
						found = true;
						break;
					}					
				}
				
				if(!found) {
					switch (m) {
						case LEFT:
							valueLeft = 0.0;
							break;
						case RIGHT:
							valueRight = 0.0;
							break;
						case UP:
							valueUp = 0.0;
							break;
						case DOWN:
							valueDown = 0.0;
							break;
					}
				}
				
				l++;
			}
			while(!found && l<2);
			
			if(!found) {
				Random r = new Random();
				return movimientos[r.nextInt(movimientos.length)];
			}
			else
				return m;		
		}
		else {
			Random r = new Random();
			return movimientos[r.nextInt(movimientos.length)];
		}
	}
	
	public static void readGhostCases() {
		GhostCases.clear();
		generatedGhostCases.clear();
		jsonParser.readGhostCases(opponentGhostName, limitSize);
	}
	
	public static void printGhostCases() {
		jsonParser.writeGhostCases(opponentGhostName, GhostCases, generatedGhostCases);		
	}
}
