package Main;


import es.ucm.fdi.ici.c1920.practica2.grupo04.maquinaestados.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.Ghosts;
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
