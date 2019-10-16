package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StateEngineImpl implements StateEngine{

	private State initialState;
	private State currentState;
	private Map<State, ArrayList<StateTransition>> machine;



	public StateEngineImpl() {
		super();
		machine = new HashMap<State, ArrayList<StateTransition>>();
	}


	@Override
	public StateEngine add(State oldState, Transition transition, State newState) throws IllegalStateException {
		if(!machine.containsKey(oldState))
			machine.put(oldState, new ArrayList<StateTransition>());
		machine.get(oldState).add(new StateTransition(transition, newState));
		return this;
	}

	@Override
	public void done() throws IllegalStateException {
		// TODO Auto-generated method stub

	}


	@Override
	public void doIt(Input in, boolean debug) {
		// Get StateTransition....
		ArrayList<StateTransition> statTrans = machine.get(currentState);
		for(StateTransition st: statTrans)
			if(st.getTransition().evaluate(in))
			{
				this.currentState = st.getNewState();
				this.currentState.execute();
				return;
			}

	}


	private class StateTransition{
		private Transition transition;
		private State newState;
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
		public StateTransition(Transition transition, State newState) {
			super();
			this.transition = transition;
			this.newState = newState;
		}


	}


	@Override
	public State currentState() {
		// TODO Auto-generated method stub
		return this.currentState;
	}


	@Override
	public void setInitialState(State initialState) {
		this.initialState = this.currentState = initialState;

	}
}
