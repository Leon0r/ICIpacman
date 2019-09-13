package pacman;

import pacman.controllers.Controller;
import pacman.controllers.GhostController;
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
				.setScaleFactor(3.0)
				.build();
		PacmanController pacman = new MsPacmanController();
		GhostController ghosts = new GhostsRandom();
		
		System.out.println(
				executor.runGame(pacman, ghosts, 50)
				);
	}

}
