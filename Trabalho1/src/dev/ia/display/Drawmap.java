package dev.ia.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Drawmap extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private char[][] map;
	private double tempo = 0;
	
	public void draw(char map[][],double tempo){
		this.map = new char[map.length][map[0].length];
		for(int linhas=0; linhas < map.length; linhas++){
            for(int coluna=0; coluna < map[0].length; coluna++){
            	this.map[coluna][linhas] = map[linhas][coluna];
            }
	    }
		this.tempo = this.tempo + tempo;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(int linhas=0; linhas < map.length; linhas++){
            for(int coluna=0; coluna < map[0].length; coluna++){
            	if(map[linhas][coluna] == 'D'){
            		g.setColor(Color.BLUE);
            		try {
            			Image img = ImageIO.read(new File("images\\forest.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}else if(map[linhas][coluna] == '.'){
            		g.setColor(Color.GREEN);
            		try {
            			Image img = ImageIO.read(new File("images\\livre.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}else if(map[linhas][coluna] == 'G'){
            		g.setColor(Color.BLACK);
            		try {
            			Image img = ImageIO.read(new File("images\\galho.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}else if(map[linhas][coluna] == 'I'){
            		g.setColor(Color.YELLOW);
            		try {
            			Image img = ImageIO.read(new File("images\\chapeu.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}else if(map[linhas][coluna] == 'F'){
            		g.setColor(Color.RED);
            		g.fillRect(20*linhas, 20*coluna, 20, 20);
            	}else if(map[linhas][coluna] == 'C'){
            		g.setColor(Color.ORANGE);
            		try {
            			Image img = ImageIO.read(new File("images\\clareira.jpg"));
            			g.drawImage(img, 20*linhas, 20*coluna, null);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	else if(map[linhas][coluna] == 'P') {
            		g.setColor(Color.GREEN);
            		g.fillRect(20*linhas, 20*coluna, 20, 20);
            	}
            }
	    }
		
		g.setColor(Color.BLACK);
		g.drawString("Tempo: " + Double.toString(this.tempo), 841, 10);
	}
}
