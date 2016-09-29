package dev.ia;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		char map[][];
		Game game = new Game("IA Chapeuzinho Vermelho", 900, 800);
		
		map = new Maploader().getmap();
		
		game.printmap(map);
	}

}
