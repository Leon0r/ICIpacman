package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts.*;
import java.util.HashMap;
import java.util.Map;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts.*;
import pacman.game.Constants.GHOST;

public class MyFSMGhosts extends FSMImpl{
	
	Map<String, State> states = new HashMap<String, State>();
	Map<String, Transition> transitions = new HashMap<String, Transition>();
	
	GHOST ghostType;
	
	double distanceLimit = 25; // para comprobar si pacman esta cerca
	int timeToChange = 200; // para comprobar si debe cambiar de estado
	
	public MyFSMGhosts(StateEngine se, GHOST gType) {
		super(se);
		ghostType = gType;		
	}
	
	private void initStates() {
		states.put("PS", new State("GoToPacmanStrategic", new GoToPacmanStrategic(ghostType)));
		states.put("PA", new State("GoToPacmanAggressive", new GoToPacmanAggressive(ghostType)));
		states.put("R", new State("RandomMovement", new RandomMovement(ghostType)));
		states.put("FP", new State("FleeFromPacman", new FleeFromPacman(ghostType)));		
		states.put("FZ", new State("FleeToZone", new FleeToZone(ghostType)));
		states.put("FS", new State("FleeFromPacmanStrategic", new FleeFromPacmanStrategic(ghostType)));
	}
	
	private void initTransitions() {
		transitions.put("1", new One(distanceLimit));
		transitions.put("1T", new OneT(distanceLimit, timeToChange));
		transitions.put("2", new Two(distanceLimit));
		transitions.put("3", new Three(distanceLimit));		
		transitions.put("4", new Four(distanceLimit));
		transitions.put("5", new Five(distanceLimit));
		transitions.put("6", new Six());
		transitions.put("7", new Seven(distanceLimit));
		transitions.put("7T", new SevenT(distanceLimit, timeToChange));
		transitions.put("8", new Eight(distanceLimit));
		transitions.put("9", new Nine(distanceLimit));
	}
	
	public void Init() {
		
		initStates(); // Para poder usar los mismos estados todo el rato
		initTransitions(); // igual pero para transiciones		
		
		State inicial = states.get("PS");		
		stateEngine.setInitialState(inicial);

		stateEngine.add(states.get("PS"), transitions.get("1T"), states.get("PA"));
		stateEngine.add(states.get("PS"), transitions.get("2"), states.get("FP"));
		stateEngine.add(states.get("PS"), transitions.get("3"), states.get("FZ"));	
		stateEngine.add(states.get("PS"), transitions.get("4"), states.get("R"));
		
		
		stateEngine.add(states.get("PA"), transitions.get("2"), states.get("FP"));
		stateEngine.add(states.get("PA"), transitions.get("3"), states.get("FZ"));		
		stateEngine.add(states.get("PA"), transitions.get("4"), states.get("R"));
		stateEngine.add(states.get("PA"), transitions.get("7T"), states.get("PS"));
		
		
		stateEngine.add(states.get("R"), transitions.get("2"), states.get("FP"));
		stateEngine.add(states.get("R"), transitions.get("3"), states.get("FZ"));
		stateEngine.add(states.get("R"), transitions.get("5"), states.get("PA"));		

		
		stateEngine.add(states.get("FP"), transitions.get("1"), states.get("PA"));
		stateEngine.add(states.get("FP"), transitions.get("3"), states.get("FZ"));
		stateEngine.add(states.get("FP"), transitions.get("4"), states.get("R"));
		stateEngine.add(states.get("FP"), transitions.get("6"), states.get("FS"));
		stateEngine.add(states.get("FP"), transitions.get("7"), states.get("PS"));
		
		
		stateEngine.add(states.get("FZ"), transitions.get("1"), states.get("PA"));
		stateEngine.add(states.get("FZ"), transitions.get("2"), states.get("FP"));
		stateEngine.add(states.get("FZ"), transitions.get("4"), states.get("R"));
		stateEngine.add(states.get("FZ"), transitions.get("6"), states.get("FS"));
		stateEngine.add(states.get("FZ"), transitions.get("7"), states.get("PS"));
		
		
		stateEngine.add(states.get("FS"), transitions.get("1"), states.get("PA"));
		stateEngine.add(states.get("FS"), transitions.get("4"), states.get("R"));
		stateEngine.add(states.get("FS"), transitions.get("7"), states.get("PS"));
		stateEngine.add(states.get("FS"), transitions.get("8"), states.get("FZ"));
		stateEngine.add(states.get("FS"), transitions.get("9"), states.get("FP"));
	}
}
