package es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados;

import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.actions.GoToPowerPillSafe;

public class MyFSM {

	public static void main(String[] args) {
		
		
		StateEngine stateEngine = new StateEngineImpl();
		
		State inicial = new State("GoToPowerPill", new GoToPowerPillSafe());

		//stateEngine.add(oldState, transition, newState)
		
		stateEngine.done();
		
		
		FSM myfsm = new FSMImpl(stateEngine);
		
		
		
		

	}

}
