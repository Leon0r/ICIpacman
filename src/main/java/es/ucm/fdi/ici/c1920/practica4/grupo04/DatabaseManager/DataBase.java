package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;


import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager.jsonParser;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class DataBase {
		
	static List<GhostCase> GhostCases = new ArrayList<GhostCase>();
	static List<GhostCase> generatedGhostCases = new ArrayList<GhostCase>();
	
	static String opponentGhostName;
	
	static int limitSize;
	
	public static void setSize(int listLimit) {
		limitSize = listLimit;		
	}
	
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
	public static void compareGhostCase(GhostCase ghostCase, Game game, int ghostIndex) {
		// Tener el peso total añadido para cada direccion según los 20 casos más importantes (Esto puede suponer demasiado tiempo)
		double valueUp = 0.0;
		double valueDown = 0.0;
		double valueLeft = 0.0;
		double valueRight = 0.0;		
		
		// Nos guardamos el valor de parecido y su direccion
		for(int h = 0; h < GhostCases.size(); h++ ) {
			// Para cada caso comparamos cuanto se parece al caso a comparar
			GhostCase c = GhostCases.get(h);
			
			// EDIBLE STATE
			// ESTO ALTERA TODO, SI ERES EDIBLE Y EL CASO NO, A TOMAR POR CLETA
			if(c.edibleGhosts[ghostIndex] != ghostCase.edibleGhosts[ghostIndex]) {
				continue;
			}
						
			double totalValue = 0; 	// Suma total acumulada de cada atributo			
			int totalCases = 6;	// Atributos totales			
			double aux = 0.0;		// Variable auxiliar para la suma
			
			
			// ACTIVE POWER PILLS
			for(int i = 0; i<4; i++)
				if(c.activePPills[i] == ghostCase.activePPills[i]) 
					aux += 0.25;				
						
			totalValue += aux;
			// -------------------------------------------------------------
			
			
			// DISTANCIAS A LAS POWER PILLS
			aux = 0;
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
					
					aux += 0.25 * (1 - v);
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
				if(ghostCase.activePPills[i]) {
					double distA = c.distancePCToPPills[i];
					double distB = ghostCase.distancePCToPPills[i];
					
					double v = (Math.abs(distA-distB) / maxDist);
					if(v>1)
						v = 1;
					else if (v<0)
						v= 0;
					
					aux += 0.25 * (1 - v);
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
			
			// DISTANCIAS A PACMAN
			aux = 0;
			
			int a = c.nearestPPillToPacman; 
			int b = ghostCase.nearestPPillToPacman;
			
			double dist = game.getDistance(a, b, DM.MANHATTAN);
			
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
			
		}
		
		
	}
	
	public static void readGhostCases() {		
		jsonParser.readGhost(opponentGhostName);
	}
	
	public static void printGhostCases() {
		jsonParser.writeGhost(opponentGhostName, generatedGhostCases);		
	}
}
