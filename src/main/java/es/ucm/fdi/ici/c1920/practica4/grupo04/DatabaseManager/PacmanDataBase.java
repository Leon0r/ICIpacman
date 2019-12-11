package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager.jsonParser;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class PacmanDataBase {
		
	static List<MsPacmanCase> MsPacmanCases = new ArrayList<MsPacmanCase>();
	static List<MsPacmanCase> generatedMsPacmanCases = new ArrayList<MsPacmanCase>();
	
	static String opponentGhostName;
	
	static int limitSize;
	
	public static void setSize(int listLimit) {
		limitSize = listLimit;		
	}
	
	public static void setOpponentGhostName(String name) {
		opponentGhostName = name;
	}
	
	public static void addGeneratedCase(MsPacmanCase pacmanCase) {
		generatedMsPacmanCases.add(pacmanCase);
	}
	
	public static void addCase(MsPacmanCase pacmanCase) {
		MsPacmanCases.add(pacmanCase);
	}
	
	// Necesitas el caso y el estado del game (aunque seguramete no te haga falta pasarlo todo el rato porque miras distancias entre nodos)
	public static void comparePacmanCase(MsPacmanCase pacmanCase, Game game) {
		// Tener el peso total añadido para cada direccion según los 20 casos más importantes (Esto puede suponer demasiado tiempo)
		double valueUp = 0.0;
		double valueDown = 0.0;
		double valueLeft = 0.0;
		double valueRight = 0.0;		
		
		// Nos guardamos el valor de parecido y su direccion
		for(int h = 0; h < MsPacmanCases.size(); h++ ) {
			// Para cada caso comparamos cuanto se parece al caso a comparar
			MsPacmanCase c = MsPacmanCases.get(h);
			
			
			///-------------------------------------------------------------
			
			double totalValue = 0.0; 	// Suma total acumulada de cada atributo			
			int totalCases = 3;	// Atributos totales			
			double aux = 0.0;		// Variable auxiliar para la suma
			
			// ESTADO DE LOS FANTASMAS
			aux = 0;

			for(int i = 0; i<4; i++) {
				if(c.edibleGhosts[i] == pacmanCase.edibleGhosts[i]) 
					aux += 0.25;					
			}

			totalValue += aux;
			//-------------------------------------------------------------
			
						
			// ACTIVE POWER PILLS
			aux = 0;
			for(int i = 0; i<4; i++) {
				if(c.activePPills[i] == pacmanCase.activePPills[i]) 
					aux += 0.25;				
			}
			
			totalValue += aux;
			// -------------------------------------------------------------
			
			// CHARACTERS POSITION AND ORIENTATION WITHIN A RANGE
			aux = 0.0;
			double distLimit = 5.0;
			double chDist = 0;
			for(int i = 0; i<5; i++) {
				chDist = game.getDistance(c.characterIndex[i], pacmanCase.characterIndex[i], DM.PATH);
				if(chDist < distLimit && c.characterLastMove[i] == pacmanCase.characterLastMove[i])
					aux += (0.25 * chDist / distLimit);
			}
			
			totalValue += aux;
			// -------------------------------------------------------------
			
			// Parecido al caso a comparar
			double finalValue = totalValue/totalCases;
			
			// Lo metemos en una lista de valores segun su movimiento
			
		}
		
		
	}
	
	public static void readGhostCases() {		
		//jsonParser.readGhost(opponentGhostName);
	}
	
	public static void printGhostCases() {
		//jsonParser.writeGhost(opponentGhostName, generatedMsPacmanCases);		
	}
}
