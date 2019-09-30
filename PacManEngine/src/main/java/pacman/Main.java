package pacman;

import es.ucm.fdi.ici.c1920.practica0.grupoYY.Ghosts;
import es.ucm.fdi.ici.c1920.practica0.grupoYY.MsPacMan;
import pacman.controllers.Controller;
import pacman.controllers.GhostController;
import pacman.controllers.GhostsAgressive;
import pacman.controllers.GhostsRandom;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.MsPacmanController;
import pacman.controllers.PacmanController;

public class Main {

	public static void main(String[] args) {
		Executor executor = new Executor.Builder()
				.setTickLimit(4000)
				.setGhostPO(false)
				.setPacmanPO(false)
				.setGhostsMessage(false)
				.setVisual(true)
				.setScaleFactor(2.5)
				.build();
		PacmanController pacman = new MsPacMan();
		GhostController ghosts = new Ghosts();
		
		System.out.println(
				executor.runGame(pacman, ghosts, 50)
				);
	}

}
