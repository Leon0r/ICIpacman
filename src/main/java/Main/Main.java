package Main;


import es.ucm.fdi.ici.c1920.practica4.grupo04.Ghosts;
import es.ucm.fdi.ici.c1920.practica4.grupo04.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class Main {

	public static void main(String[] args) {
		
		int j = 10;
		int i = 0;
		int totalScore = 0;
		
		while(i<j) {
			Executor executor = new Executor.Builder()
					.setTickLimit(4000)
					.setPacmanPO(false)
					.setGhostsMessage(false)
					.setVisual(true)
					.setScaleFactor(2)
					.build();
			
			PacmanController pacman = new MsPacMan();
			GhostController ghosts = new Ghosts();
			
			int score = executor.runGame(pacman, ghosts, 40);
			System.out.println(score);
			totalScore += score;
			
			i++;
		}
		
		System.out.println("Media de puntos en " + j + " partidas: " + totalScore/j);
		
	}

}
