package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

import java.util.*;

import es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager.jsonParser;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostDataBase {
	
	// Casos de lectura
	static List<GhostCase> GhostCases = new ArrayList<GhostCase>();
	// Casos generados durante la partida
	static List<GhostCase> generatedGhostCases = new ArrayList<GhostCase>();
	
	static String opponentGhostName;
	
	public static void setOpponentGhostName(String name) {
		opponentGhostName = name;
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
						
			double totalValue = 0.0; 	// Suma total acumulada de cada atributo			
			double totalCases = 6.0;	// Atributos totales			
			double aux = 0.0;			// Variable auxiliar para la suma
			
			
			// ACTIVE POWER PILLS
			for(int i = 0; i<4; i++)
				if(c.activePPills[i] == ghostCase.activePPills[i]) 
					aux += 0.25;				
						
			totalValue += aux;
			// -------------------------------------------------------------
			
			
			// DISTANCIAS A LAS POWER PILLS
			aux = 0.0;
			double maxDist = 50.0;
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
			//-------------------------------------------------------------
			
			
			// DISTANCIAS A PACMAN
			aux = 0;
			
			for(int i = 0; i<4; i++) {
				double distA = c.distanceToPacman[i]; 
				double distB = ghostCase.distanceToPacman[i];
				
				double v = (Math.abs(distA-distB) / maxDist);
				if(v>1)
					v = 1;
				else if (v<0)
					v= 0;
				
				aux += 0.25 * (1 - v);
			}
			
			totalValue += aux;
			//-------------------------------------------------------------
			
			
			// DISTANCIAS DE PACMAN A LAS POWER PILLS
			aux = 0;
			
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
			//-------------------------------------------------------------
			
			
			// ESTADO DE LOS FANTASMAS
			aux = 0;
			
			for(int i = 0; i<4; i++) {
				if(c.edibleGhosts[i] == ghostCase.edibleGhosts[i]) 
					aux += 0.25;					
			}
			
			totalValue += aux;
			//-------------------------------------------------------------
			
			// DISTANCIA DE PACMAN A LA POWER PILL MAS CERCANA
			aux = 0;
			
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
			//-------------------------------------------------------------
			
			
			// Parecido al caso a comparar
			double finalValue = totalValue/totalCases;
			
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
		
		
		if(ghostIndex == 0) {
			System.out.println("Value right: " + valueRight + " Value left: " + valueLeft + " Value up: " + valueUp + " Value down: " + valueDown);
		}
		
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
		jsonParser.readGhostCases(opponentGhostName);
	}
	
	public static void printGhostCases() {
		jsonParser.writeGhostCases(opponentGhostName, generatedGhostCases);		
	}
}
