package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsPacMan.*;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsPacMan.*;
import pacman.game.Game;

public class MyFSMPacman extends FSMImpl{
	
	List<State> states;
	List <Action> actions;
	
	public MyFSMPacman(StateEngine se) {
		super(se);
		init();
	}
	
	public void init() {
		
		states = new ArrayList<State>();
		actions = new ArrayList<Action>();
		
		State getPoints, Flee;
		Action act;
		
		act =  new GoToPillSafe();
		actions.add(act);
		getPoints = new State("getPoints", act);
		states.add(getPoints);
		
		
		act = new RunAwayFromGhost();
		actions.add(act);		
		Flee = new State("flee", act);
		states.add(Flee);
		
		stateEngine.setInitialState(Flee);
		stateEngine.add(getPoints, new GhostsCloseToMsPacman(), Flee);
		stateEngine.add(Flee, new NoGhostsCloseToMsPacMan(), getPoints);
	}
	
	@Override
	public void setGame(Game game) {
		for(Action a : actions)
			a.setGame(game);
	}
}
