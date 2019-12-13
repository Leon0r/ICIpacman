package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException; 
import java.io.PrintWriter;
import org.json.simple.JSONArray; 
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.parser.*; 

import java.util.List;

public class jsonParser{

	public static void readGhost(String name) {		
		//String[] nameParts = name.split(".");

		ArrayList<JSONObject> json=new ArrayList<JSONObject>();
		JSONObject obj;
		// The name of the file to open.
		String fileName = "bin/es/ucm/fdi/ici/c1920/practica4/grupo04/data/" + name + ".json";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) new JSONParser().parse(line);
				json.add(obj);
			}
			// Always close files.
			bufferedReader.close();         
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");                  
			// Or we could just do this: 
			// ex.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int j = 0; j< json.size(); j++) {

			JSONObject jo = json.get(j);

			long nearestPPillToPacman = (long) jo.get("nearestPPillToPacman");
			long move = (long) jo.get("move");


			double[] distanceToPacman = new double[4];			
			JSONArray lm = (JSONArray) jo.get("distanceToPacman");			
			for(int p = 0; p < 4; p++) {				 
				distanceToPacman[p] = (double) lm.get(p);
			}


			boolean[] edibleGhosts = new boolean[4];			
			lm = (JSONArray) jo.get("edibleGhosts");			
			for(int p = 0; p < 4; p++) {				 
				edibleGhosts[p] =  (boolean) lm.get(p);
			}

			int [] characterIndex = new int[5];
			lm = (JSONArray) jo.get("characterIndex");			
			for(int p = 0; p < 5; p++) {
				long l = (long) lm.get(p);
				characterIndex[p] =  (int)l;
			}

			int [] characterLastMove = new int[5];
			lm = (JSONArray) jo.get("characterLastMove");			
			for(int p = 0; p < 5; p++) {
				long l = (long) lm.get(p);
				characterLastMove[p] =  (int)l;
			}

			double [][] distanceToPPills = new double[4][4];
			lm = (JSONArray) jo.get("distanceToPPills");
			for(int p = 0; p < 4; p++) {

				JSONArray lm1 = (JSONArray) lm.get(p);				
				for(int q = 0; q < 4; q++) {
					distanceToPPills[p][q] = (double)lm1.get(q);
				}				
			}

			double [] distancePCToPPills = new double[4];
			lm = (JSONArray) jo.get("distancePCToPPills");			
			for(int p = 0; p < 4; p++) {
				distancePCToPPills[p] = (double)lm.get(p);				
			}

			boolean[] activePPills = new boolean[4];
			lm = (JSONArray) jo.get("activePPills");			
			for(int p = 0; p < 4; p++) {				 
				activePPills[p] =  (boolean) lm.get(p);
			}

			GhostCase c = new GhostCase(distanceToPacman, edibleGhosts, 
					distanceToPPills, distancePCToPPills, activePPills, (int)nearestPPillToPacman, (int)move );

			GhostDataBase.addCase(c);
		}
	}

	public static void writeGhost(String name, List<GhostCase> ghostCases) {

		// writing JSON to file:"JSONExample.json" in cwd 
		PrintWriter pw = null;

		try {
			pw = new PrintWriter("bin/es/ucm/fdi/ici/c1920/practica4/grupo04/data/" + name + /*nameParts[nameParts.length-1] + */".json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		for(int i = 0; i<ghostCases.size(); i++) {

			GhostCase c = ghostCases.get(i);

			JSONObject jo = new JSONObject();			

			// putting data to JSONObject 
			jo.put("nearestPPillToPacman", c.nearestPPillToPacman); 

			int move = 0;

			switch (c.movement) {
			case LEFT:
				move = 0;
				break;
			case UP:
				move = 1;
				break;
			case RIGHT:
				move = 2;
				break;
			case DOWN:
				move = 3;
				break;
			}

			jo.put("movement", move); 


			JSONArray distanceToPacman = new JSONArray();
			for(int j = 0; j<4; j++) 
				distanceToPacman.add(c.distanceToPacman[j]);
			jo.put("distanceToPacman", distanceToPacman);

			JSONArray edibleGhosts = new JSONArray();
			for(int j = 0; j<4; j++) 
				edibleGhosts.add(c.edibleGhosts[j]);
			jo.put("edibleGhosts", edibleGhosts);

			JSONArray distancePCToPPills = new JSONArray();
			for(int j = 0; j<4; j++) 
				distancePCToPPills.add(c.distancePCToPPills[j]);	        	       
			jo.put("distancePCToPPills", distancePCToPPills);

			JSONArray activePPills = new JSONArray();
			for(int j = 0; j<4; j++) 
				activePPills.add(c.activePPills[j]);
			jo.put("activePPills", activePPills);


			JSONArray distanceToPPills = new JSONArray();
			for(int h = 0; h < 4; h++) {
				JSONArray dPills = new JSONArray();
				for(int j = 0; j<4; j++) 
					dPills.add(c.distanceToPPills[h][j]);
				distanceToPPills.add(dPills);
			}

			jo.put("distanceToPPills", distanceToPPills);

			pw.write(jo.toJSONString());

			pw.write('\n');
		}        

		pw.flush(); 
		pw.close(); 

	}

	public static void readPacman(String name) {		
		//String[] nameParts = name.split(".");

		ArrayList<JSONObject> json=new ArrayList<JSONObject>();
		JSONObject obj;
		// The name of the file to open.
		String fileName = "bin/es/ucm/fdi/ici/c1920/practica4/grupo04/data/" + name + ".json";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {
				obj = (JSONObject) new JSONParser().parse(line);
				json.add(obj);
			}
			// Always close files.
			bufferedReader.close();         
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");                  
			// Or we could just do this: 
			// ex.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int j = 0; j< json.size(); j++) {

			JSONObject jo = json.get(j);

			long nearestPPillToPacman = (long) jo.get("nearestPPillToPacman");
			long movement = (long) jo.get("move");
			JSONArray lm;

			boolean[] edibleGhosts = new boolean[4];			
			lm = (JSONArray) jo.get("edibleGhosts");			
			for(int p = 0; p < 4; p++) {				 
				edibleGhosts[p] =  (boolean) lm.get(p);
			}

			int [] characterIndex = new int[5];
			lm = (JSONArray) jo.get("characterIndex");			
			for(int p = 0; p < 5; p++) {
				long l = (long) lm.get(p);
				characterIndex[p] =  (int)l;
			}

			int [] characterLastMove = new int[5];
			lm = (JSONArray) jo.get("characterLastMove");			
			for(int p = 0; p < 5; p++) {
				long l = (long) lm.get(p);
				characterLastMove[p] =  (int)l;
			}

			int[] activePPills = new int[4];
			lm = (JSONArray) jo.get("activePPills");			
			for(int p = 0; p < 4; p++) {
				long l = (long) lm.get(p);
				activePPills[p] =  (int) l;
			}

			MsPacmanCase c = new MsPacmanCase(edibleGhosts, characterIndex, characterLastMove,
					activePPills, (int) nearestPPillToPacman, (int) movement);

			PacmanDataBase.addCase(c);
		}
	}

	public static void writePacman(String name, List<MsPacmanCase> pacmanCases) {

		// writing JSON to file:"JSONExample.json" in cwd 
		PrintWriter pw = null;

		try {
			pw = new PrintWriter("bin/es/ucm/fdi/ici/c1920/practica4/grupo04/data/" + name + /*nameParts[nameParts.length-1] + */".json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		for(int i = 0; i<pacmanCases.size(); i++) {

			MsPacmanCase c = pacmanCases.get(i);

			JSONObject jo = new JSONObject();			

			// putting data to JSONObject 
			jo.put("nearestPPillToPacman", c.nearestPPillToPacman);	  
			
			int move = c.movement.ordinal();
			jo.put("movement", move);

			JSONArray edibleGhosts = new JSONArray();
			for(int j = 0; j<4; j++) 
				edibleGhosts.add(c.edibleGhosts[j]);
			jo.put("edibleGhosts", edibleGhosts);

			JSONArray characterIndex = new JSONArray();
			for(int j = 0; j<5; j++) 
				characterIndex.add(c.activePPills[j]);
			jo.put("characterIndex", characterIndex);
			
			JSONArray characterLastMove = new JSONArray();
			for(int j = 0; j<5; j++) 
				characterLastMove.add(c.characterLastMove[j]);
			jo.put("characterLastMove", characterLastMove);
			
			JSONArray activePPills = new JSONArray();
			for(int j = 0; j<4; j++) 
				activePPills.add(c.activePPills[j]);
			jo.put("activePPills", activePPills);


			pw.write(jo.toJSONString());

			pw.write('\n');
		}        

		pw.flush(); 
		pw.close(); 

	}
}