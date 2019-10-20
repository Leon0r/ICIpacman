package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;

public class StateEngineImpl implements StateEngine{

	private State initialState;
	private State currentState;
	private HashMap<State, ArrayList<StateTransition>> machine;	

	public StateEngineImpl() {		
		machine = new HashMap<State, ArrayList<StateTransition>>();
	}


	@Override
	public StateEngine add(State oldState, Transition transition, State newState) throws IllegalStateException {

		boolean found = false;

		for (State key : machine.keySet()) {			
			if(key == oldState) {
				found = true;
				machine.get(key).add(new StateTransition(transition, newState));
				break;
			}		
		}

		if(!found) {

			ArrayList<StateTransition> a = new ArrayList<StateTransition>();

			a.add(new StateTransition(transition, newState));

			machine.put(oldState, a);
		}



		return this;
	}

	@Override
	public void checkChange(Input in, boolean debug) {

		for(StateTransition t: machine.get(this.currentState)) {
			if(t.getTransition().evaluate(in))
			{
				System.out.println("Cambio de estado: " + this.currentState.id +", nuevo: " +t.getNewState().id);
				
				this.currentState = t.getNewState();
				return;
			}
		}
	}

	@Override
	public State currentState() {
		return this.currentState;
	}

	@Override
	public void setInitialState(State initialState) {
		this.currentState = initialState;
	}

	
	private class StateTransition{

		private Transition transition;
		private State newState;

		public StateTransition(Transition transition, State newState) {
			super();
			this.transition = transition;
			this.newState = newState;
		}
		public Transition getTransition() {
			return transition;
		}
		public void setTransition(Transition transition) {
			this.transition = transition;
		}
		public State getNewState() {
			return newState;
		}
		public void setNewState(State newState) {
			this.newState = newState;
		}
	}
}
