package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager.jsonParser;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PacmanDataBase {

	// casos de lectura
	static List<MsPacmanCase> MsPacmanCases = new ArrayList<MsPacmanCase>();
	// casos generados durante la partida
	static List<MsPacmanCase> generatedMsPacmanCases = new ArrayList<MsPacmanCase>();

	static String opponentGhostName;
	static int casesChecked = 5;

	public static void setOpponentGhostName(String name) {
		opponentGhostName = name;
	}

	public static void addGeneratedCase(MsPacmanCase pacmanCase) {
		generatedMsPacmanCases.add(pacmanCase);
	}

	public static void addCase(MsPacmanCase pacmanCase) {
		MsPacmanCases.add(pacmanCase);
	}

	// Necesitas el caso y el estado del game
	public static MOVE comparePacmanCase(MsPacmanCase pacmanCase, Game game) {
		// Tener el peso total añadido para cada direccion según los casos más importantes
		double valueUp = 0.001;
		double valueDown = 0.001;
		double valueLeft = 0.001;
		double valueRight = 0.001;		

		PriorityQueue<Double> leftQueue = new PriorityQueue<Double>(1, Collections.reverseOrder());
		PriorityQueue<Double> rightQueue = new PriorityQueue<Double>(1, Collections.reverseOrder());
		PriorityQueue<Double> upQueue = new PriorityQueue<Double>(1, Collections.reverseOrder());
		PriorityQueue<Double> downQueue = new PriorityQueue<Double>(1, Collections.reverseOrder());


		// Nos guardamos el valor de parecido y su direccion
		for(int h = 0; h < MsPacmanCases.size(); h++ ) {
			// Para cada caso comparamos cuanto se parece al caso a comparar
			MsPacmanCase c = MsPacmanCases.get(h);


			///-------------------------------------------------------------

			double totalValue = 0.0; 	// Suma total acumulada de cada atributo			
			double totalCases = 3.0;	// Atributos totales			
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
			default:
				break;
			}
		}

		for(int h = 0; h<casesChecked; h++) {
			if(upQueue.isEmpty())
				break;
			else
				valueUp += upQueue.poll();
		}
		for(int h = 0; h<casesChecked; h++) {
			if(rightQueue.isEmpty())
				break;
			else
				valueRight += rightQueue.poll();
		}
		for(int h = 0; h<casesChecked; h++) {
			if(leftQueue.isEmpty())
				break;
			else
				valueLeft += leftQueue.poll();
		}
		for(int h = 0; h<casesChecked; h++) {
			if(downQueue.isEmpty())
				break;
			else
				valueDown += downQueue.poll();
		}

		MOVE[] movimientos = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		int l = 0;
		if(movimientos.length > 1 && MsPacmanCases.size() > 0) {			
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
		//jsonParser.readGhost(opponentGhostName);
	}

	public static void printGhostCases() {
		// jsonParser.writeGhost(opponentGhostName, generatedMsPacmanCases);		
	}
}
