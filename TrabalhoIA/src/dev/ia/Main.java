package dev.ia;

import java.io.IOException;
import java.util.List;

import astar.ExampleNode;
import astar.Map;
import astar.Maploader;

public class Main {

	public static void main(String[] args) throws IOException {

		char[][] mapChar = (new Maploader()).getmap();
		int tempo;

		Map<ExampleNode> myMap = new Map<ExampleNode>(41, 41, new ExampleNode(), mapChar);
		List<ExampleNode> path = myMap.findPath(36, 4, 36, 36);

		Game game = new Game("IA Chapeuzinho Vermelho", 900, 800);

		tempo = (int)System.currentTimeMillis();
		for(int i = 0; i < path.size(); i++) {
			mapChar[path.get(i).getxPosition()][path.get(i).getyPosition()] = 'I';
			mapChar[path.get(i).getPrevious().getxPosition()][path.get(i).getPrevious().getyPosition()] = 'P';
			game.printmap(mapChar,path.get(i).getValueMovement());
			while((int)System.currentTimeMillis() - tempo > 300);
			tempo = (int)System.currentTimeMillis();
		}
	}

}
