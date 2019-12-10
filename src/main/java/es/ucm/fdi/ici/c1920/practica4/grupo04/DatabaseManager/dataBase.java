package es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.ici.c1920.practica4.grupo04.DatabaseManager.jsonParser;

public class DataBase {
		
	static List<GhostCase> GhostCases = new ArrayList<GhostCase>();
	static List<GhostCase> generatedGhostCases = new ArrayList<GhostCase>();
	
	static String opponentGhostName;
	
	static int limitSize;
	
	public static void setSize(int listLimit) {
		limitSize = listLimit;		
	}
	
	public static void addGeneratedCase(GhostCase ghostCase) {
		generatedGhostCases.add(ghostCase);
	}
	
	public static void addCase(GhostCase ghostCase) {
		GhostCases.add(ghostCase);
	}
	
	public static void setOpponentGhostName(String name) {
		opponentGhostName = name;
	}
	
	public static void readGhostCases() {		
		jsonParser.read(opponentGhostName);
	}
	
	public static void printGhostCases() {
		jsonParser.write(opponentGhostName, generatedGhostCases);		
	}
}
