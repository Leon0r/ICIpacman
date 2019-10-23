package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actionsPacMan.*;
import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.transitionsGhosts.GhostEdible;
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
		
		State getPoints, flee, eatGhost, getPowerPill;
		Action act;
		
		// STATES AND ITS ACTIONS
		act =  new NoGhosts();
		actions.add(act);
		getPoints = new State("getPoints", act);
		states.add(getPoints);
		
		
		act = new OneGhost();
		actions.add(act);		
		flee = new State("flee", act);
		states.add(flee);
		
		act = new EdibleGhosts();
		actions.add(act);
		eatGhost = new State("eatGhost", act);
		states.add(eatGhost);
		
		act = new ManyGhosts();
		actions.add(act);
		getPowerPill = new State("getPowerPill", act);
		states.add(getPowerPill);
		
		//TO DO: Poner más complejos primero para que los compare antes
		// TRANSITIONS
		stateEngine.setInitialState(getPoints);
		
		// desde getPoints
		stateEngine.add(getPoints, new GhostIsEdible(), eatGhost);
		stateEngine.add(getPoints, new GhostCloseSafePath(), flee);
		stateEngine.add(getPoints, new ManyGhostsNear(), getPowerPill);		
		
		// desde flee
		stateEngine.add(flee, new GhostIsEdible(), eatGhost);
		stateEngine.add(flee, new NoGhostsNear(), getPoints);
		stateEngine.add(flee, new ManyGhostsNear(), getPowerPill);

		
		// desde eatGhost
		stateEngine.add(eatGhost, new NoGhostsNear(), getPoints);
		stateEngine.add(eatGhost, new GhostCloseSafePath(), flee);
		stateEngine.add(eatGhost, new ManyGhostsNear(), getPowerPill);
		
		// desde getPowerPill
		stateEngine.add(getPowerPill, new GhostIsEdible(), eatGhost);
		stateEngine.add(getPowerPill, new GhostCloseSafePath(), flee);
		stateEngine.add(getPowerPill, new NoGhostsNear(), getPoints);
		
		
		
	}
	
	@Override
	public void setGame(Game game) {
		for(Action a : actions)
			a.setGame(game);
	}
}
