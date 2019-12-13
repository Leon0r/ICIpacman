package es.ucm.fdi.ici.c1920.practica4.grupo04;

import java.util.EnumMap;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

import java.util.ArrayList;


import es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager.*;

public final class Ghosts extends GhostController{

	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	boolean lastEdibleState[] = new boolean[4];
	
	ArrayList<GhostCase>[] lastCase = new ArrayList[4]; 	  
	GhostCase c = null;
	
	// Control de puntuacion
	int lastTickScore = 0;
	int actualTicks = 0;
	int ticksToCheck = 150;
	int maxPointsAllowed = 400;
	
	// Numero minimo de cruces que se han superado sin morir
	// Esto importa cuando estás huyendo
	// Eran 10 pero normalmente no pasas 10 cruces en el tiempo que estás edible
	int crossesSurpassed = 5;
	
	// Si estas atacando lo que hacemos es guardar los ultimos 5 casos de todos los fantasmas al morir pacman,
	// porque serán los que han dirigido a los fantasmas a comerselo, y por tando solo los fantasmas no edibles,
	// hay que dar más importancia a los casos del fantasma que ha matado
	int killedCases = 5;
	
	public Ghosts() {
		for(int i = 0; i < 4; i++) {
			lastCase[i] = new ArrayList<GhostCase>();
			lastEdibleState[i] = false;
		}
	}
	 
	
	@Override
	public void preCompute(String opponent) {		
		GhostDataBase.setOpponentGhostName(opponent);
		GhostDataBase.setLimit(5000);
		GhostDataBase.readGhostCases();
    }
    
	@Override
    public  void postCompute() {		
		GhostDataBase.printGhostCases();
    }
	
	private void generateCase(Game game) {
		
		// VER EL ESTADO DEL JUEGO
		
		double[] distanceToPacman = new double[4];
		boolean[] edibleGhosts = new boolean[4];
		boolean[] activePPills = new boolean[4];
		double [][] distanceToPPills = new double[4][4];
		double [] distancePCToPPills = new double[4];
		int [] characterIndex = new int[5];
		int nearestPPillToPacman;
		
		for(int i = 0; i < 4; i++) {
			distanceToPacman[i] = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.values()[i]), game.getPacmanCurrentNodeIndex(),
					game.getGhostLastMoveMade(GHOST.values()[i]), DM.PATH);
			
			edibleGhosts[i] = game.isGhostEdible(GHOST.values()[i]);
			activePPills[i] = game.isPowerPillStillAvailable(i);
			
			distancePCToPPills[i] = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getPowerPillIndices()[i],
					game.getPacmanLastMoveMade(), DM.PATH);
			
			for(int k = 0; k < 4; k++) {
				distanceToPPills[i][k] = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.values()[i]), game.getPowerPillIndices()[k],
						game.getGhostLastMoveMade(GHOST.values()[i]), DM.PATH);
			}
			
		}
		
		nearestPPillToPacman = game.getPowerPillIndices()[0];
		double dist = distancePCToPPills[0];
		for(int i = 1; i < 4; i++) {
			if(distancePCToPPills[i] > dist) {
				dist = distancePCToPPills[i];
				nearestPPillToPacman = game.getPowerPillIndices()[i];
			}
		}
		
		for(int i = 1; i < 4; i++) {
			characterIndex[i] = game.getGhostCurrentNodeIndex(GHOST.values()[i]);
		}
		
		characterIndex[4] = game.getPacmanCurrentNodeIndex();
		
		
		c = new GhostCase(distanceToPacman, edibleGhosts, distanceToPPills, distancePCToPPills, activePPills, characterIndex , nearestPPillToPacman, 0, 0);
		
	}
	
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		actualTicks++;
		
		// Hay que actualizar el minimo de puntos ya que cuantas menos pastillas queden, menos puntos hay que tomar en consideracion para ver si has 
		// o no obstaculizado
		
		// Una decima parte de los puntos totales que pueden conseguirse en ese momento
		maxPointsAllowed = (game.getNumberOfActivePills() * 10 + game.getActivePowerPillsIndices().length * 50)/10;
		
		// Comprobacion de si eres bueno haciendo que pacman tenga pocos puntos
		if(actualTicks >= ticksToCheck) {			
			// HAS OBSTACULIZADO BIEN A PACMAN PARA QUE NO CONSIGA PUNTOS
			if(game.getScore() - lastTickScore < maxPointsAllowed) {
				for(int i = 0; i<4; i++) {
					// Si no es edible, nos importan sus ultimos 'killedCases' movimientos
					if(!game.isGhostEdible(GHOST.values()[i])) {
						int cont = 0;
						
						while(!lastCase[i].isEmpty() && cont < lastCase[i].size() && cont < killedCases) {
							GhostDataBase.addGeneratedCase(lastCase[i].get(cont));
							cont++;
						}
					}
					
					//Y luego borramos los movimientos de los fantasmas porque empezamos desde el centro
					lastCase[i].clear();
				}
			}
			
			lastTickScore = game.getScore();			
			actualTicks = 0;
		}
		
		for(GHOST ghostType : GHOST.values()) {
			
			if(game.wasGhostEaten(ghostType)) {
				// Si se han comido a este fantasma, quitamos los casos superados durante esa "vida"
				lastCase[ghostType.ordinal()].clear();
			}
			
			// AQUI SE INTRODUCEN LOS CASOS DE LOS FANTASMAS NO EDIBLES CUANDO HA IDO BIEN LA COSA, LO QUE IMPLICA QUE SE HAN COMIDO A PACMAN
			// TAMBIEN SE PODRÍA COMPROBAR SI EN X TICKS DE RELOJ PACMAN A CONSEGUIDO POCOS PUNTOS, LO QUE PUEDE SIGNIFICAR QUE LE HAN FASTIDIADO
			// LO SUFICIENTE PARA QUE REDUZCA SU RENDIMIENTO			
			if(game.wasPacManEaten() ) {
				// Si se han comido a pacman guardamos los ultimos movimientos de los fantasmas atacando (no edibles)
				for(int i = 0; i<4; i++) {
					// Si no es edible, nos importan sus ultimos 'killedCases' movimientos
					if(!game.isGhostEdible(ghostType)) {
						int cont = 0;
						
						while(!lastCase[ghostType.ordinal()].isEmpty() && cont < lastCase[i].size() && cont < killedCases) {
							GhostDataBase.addGeneratedCase(lastCase[ghostType.ordinal()].get(cont));
							cont++;
						}
					}
					
					//Y luego borramos los movimientos de los fantasmas porque empezamos desde el centro
					lastCase[i].clear();
				}
			}
			
			if(game.doesGhostRequireAction(ghostType)) {
				
				
				// CUANDO ESTAMOS EN UN ESTADO (EDIBLE/NO EDIBLE) NO QUEREMOS LOS MOVIMIENTOS DEL OTRO ESTADO
				if(game.isGhostEdible(ghostType) && lastEdibleState[ghostType.ordinal()] == false) {
					lastEdibleState[ghostType.ordinal()] = true;
					lastCase[ghostType.ordinal()].clear();
				}
				if(!game.isGhostEdible(ghostType) && lastEdibleState[ghostType.ordinal()] == true) {
					lastEdibleState[ghostType.ordinal()] = false;
					lastCase[ghostType.ordinal()].clear();
				}
				
				// AQUI SE AÑADEN LOS CASOS CUANDO LOS GHOST SON EDIBLES Y SE HAN MANTENIDO CON VIDA
				// AHORA EMPIEZA A GUARDAR TODOS LOS CASOS GENERADOS A PARTIR DE CUANDO LLEVA '' SIN MORIR
				// PERO SERÍA PREFERIBLE QUE CADA VEZ QUE SE AÑADE UNO SE LIMPIASE LA LISTA PARA VOLVER A
				// COMPROBAR SI HACE BUENOS MOVIMIENTOS, ESTO DESATURARÍA LA BASE DE CASOS
				if(game.isGhostEdible(ghostType) && lastCase[ghostType.ordinal()].size() > crossesSurpassed) {
					GhostDataBase.addGeneratedCase(lastCase[ghostType.ordinal()].get(crossesSurpassed - 1));					
				}
				
				generateCase(game);
				MOVE move = GhostDataBase.compareGhostCase(c, game, ghostType.ordinal());
				
				moves.put(ghostType, move);
				
				c.movement = move;
				lastCase[ghostType.ordinal()].add(0, c);		
			}
		}		
	return moves;
	}	
}
