package dev.ia;

public class Printmap {
	
	public Printmap(char map[][]){
		for(int linhas=0; linhas < map.length; linhas++){
            for(int coluna=0; coluna < map[0].length; coluna++){
            	System.out.print(map [linhas][coluna]);
            }
            System.out.println();
	    }
	}
}
