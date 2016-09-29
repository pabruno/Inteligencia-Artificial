package dev.ia;

import dev.ia.display.Display;
import dev.ia.display.Drawmap;

public class Game {
	private Display display;
	
	public Game(String title, int width, int height){
		display = new Display(title, width, height);
	}
	
	public void printmap(char map[][]){
		Drawmap d = new Drawmap();
		d.draw(map);
		display.add(d);
	}
	
}
