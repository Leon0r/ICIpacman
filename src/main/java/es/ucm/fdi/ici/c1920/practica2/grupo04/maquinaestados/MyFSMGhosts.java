package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsGhosts.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts.*;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class MyFSMGhosts extends FSMImpl{
	
	Map<String, State> states = new HashMap<String, State>();
	List <Action> actions;
	Map<String, Transition> transitions = new HashMap<String, Transition>();
	
	GHOST ghostType;
	
	double distanceLimit = 25; // para comprobar si pacman esta cerca
	int timeToChange = 200; // para comprobar si debe cambiar de estado
	
	public MyFSMGhosts(StateEngine se, GHOST gType) {
		super(se);
		ghostType = gType;		
	}
	
	private void initStates() {
		actions = new ArrayList<Action>();
		Action act;
		
		act = new GoToPacmanStrategic(ghostType);
		actions.add(act);
		states.put("PS", new State("GoToPacmanStrategic", act));
		
		act = new GoToPacmanAggressive(ghostType);
		actions.add(act);
		states.put("PA", new State("GoToPacmanAggressive", act));
		
		act = new RandomMovement(ghostType);
		actions.add(act);
		states.put("R", new State("RandomMovement", act));
		
		act = new FleeFromPacman(ghostType);
		actions.add(act);
		states.put("FP", new State("FleeFromPacman", act));
		
		act = new FleeToZone(ghostType);
		actions.add(act);
		states.put("FZ", new State("FleeToZone", act));
		
		act = new FleeFromPacmanStrategic(ghostType);
		actions.add(act);
		states.put("FS", new State("FleeFromPacmanStrategic", act));
	}
	
	private void initTransitions() {
		transitions.put("1", new GoToPacmanAggressive_T(distanceLimit));
		transitions.put("1T", new GoToPacmanAggressiveTimer_T(distanceLimit, timeToChange));
		transitions.put("1S", new GoToPacmanAggressiveSue_T(distanceLimit));
		transitions.put("2", new FleeFromPacman_T(distanceLimit));
		transitions.put("2H", new FleeFromPacmanHelpless_T(distanceLimit));
		transitions.put("3", new FleeToZone_T(distanceLimit));
		transitions.put("3H", new FleeToZoneHelpless_T(distanceLimit));
		transitions.put("4", new RandomMovement_T(distanceLimit));
		transitions.put("5", new FleeFromPacmanStrategic_T());
		transitions.put("6", new GoToPacmanStrategic_T(distanceLimit));
		transitions.put("6T", new GoToPacmanStrategicTimer_T(distanceLimit, timeToChange));
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
		stateEngine.add(states.get("PA"), transitions.get("6T"), states.get("PS"));
		
		
		stateEngine.add(states.get("R"), transitions.get("2"), states.get("FP"));
		stateEngine.add(states.get("R"), transitions.get("3"), states.get("FZ"));
		stateEngine.add(states.get("R"), transitions.get("1S"), states.get("PA"));		

		
		stateEngine.add(states.get("FP"), transitions.get("1"), states.get("PA"));
		stateEngine.add(states.get("FP"), transitions.get("3"), states.get("FZ"));
		stateEngine.add(states.get("FP"), transitions.get("4"), states.get("R"));
		stateEngine.add(states.get("FP"), transitions.get("5"), states.get("FS"));
		stateEngine.add(states.get("FP"), transitions.get("6"), states.get("PS"));
		
		
		stateEngine.add(states.get("FZ"), transitions.get("1"), states.get("PA"));
		stateEngine.add(states.get("FZ"), transitions.get("2"), states.get("FP"));
		stateEngine.add(states.get("FZ"), transitions.get("4"), states.get("R"));
		stateEngine.add(states.get("FZ"), transitions.get("5"), states.get("FS"));
		stateEngine.add(states.get("FZ"), transitions.get("6"), states.get("PS"));
		
		
		stateEngine.add(states.get("FS"), transitions.get("1"), states.get("PA"));
		stateEngine.add(states.get("FS"), transitions.get("4"), states.get("R"));
		stateEngine.add(states.get("FS"), transitions.get("6"), states.get("PS"));
		stateEngine.add(states.get("FS"), transitions.get("3H"), states.get("FZ"));
		stateEngine.add(states.get("FS"), transitions.get("2H"), states.get("FP"));
	}
	
	@Override
	public void setGame(Game game) {
		for(Action a : actions)
			a.setGame(game);
	}
	
}
