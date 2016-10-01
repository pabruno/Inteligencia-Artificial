package dev.ia;

import java.io.IOException;
import java.util.List;

import AG.Algoritimo;
import AG.Populacao;
import astar.ExampleNode;
import astar.Map;
import astar.Maploader;

public class Main {

	public static void main(String[] args) throws IOException {
		
		//taxa de crossover
        Algoritimo.setTaxaDeCrossover(0.01);
        //taxa de mutação
        Algoritimo.setTaxaDeMutacao(9.0);
        //elitismo
        boolean eltismo = true;
        //tamanho da população
        int tamPop = 1000;
        //numero máximo de gerações
        int numMaxGeracoes = 1000;

        //define o número de genes do indivíduo baseado na solução
        int qtdclareira = 10;
        int qtddoces = 5;

        //cria a primeira população aleatória
        Populacao populacao = new Populacao(qtdclareira, qtddoces, tamPop);

        int geracao = 0;
        
        //loop até o critério de parada
        while ( geracao < numMaxGeracoes) {
            geracao++;

            //cria nova populacao
            populacao = Algoritimo.novaGeracao(populacao, eltismo);

            System.out.print("Geração " + geracao + " | Aptidão: " + populacao.getIndivduo(0).getAptidao() + " | Melhor: ");
            for(int i=0; i < populacao.getIndivduo(0).getGns().length; i++){
            	for(int j=0; j < populacao.getIndivduo(0).getGns()[0].length; j++){
            		System.out.print(populacao.getIndivduo(0).getGns()[i][j]);
            	}
            	System.out.print(" ");
            }
            System.out.println("");
            
        }

        if (geracao == numMaxGeracoes) {
            System.out.println("Número Maximo de Gerações | " + populacao.getIndivduo(0).getAptidao());
        }

		char[][] mapChar = (new Maploader()).getmap();
		int tempo;

		Map<ExampleNode> myMap = new Map<ExampleNode>(41, 41, new ExampleNode(), mapChar);
		List<ExampleNode> path = myMap.findPath(36, 4, 36, 36);

		Game game = new Game("IA Chapeuzinho Vermelho", 950, 900);
		int sumapreciation;
		int numClareira = 0;
		tempo = (int)System.currentTimeMillis();
		for(int i = 0; i < path.size(); i++) {
			
			if(mapChar[path.get(i).getxPosition()][path.get(i).getyPosition()] == 'C') {
				
				mapChar[path.get(i).getxPosition()][path.get(i).getyPosition()] = 'I';
				mapChar[path.get(i).getPrevious().getxPosition()][path.get(i).getPrevious().getyPosition()] = 'P';
				
				sumapreciation = 0;
				
	            for(int doce=0; doce < populacao.getIndivduo(0).getGns()[0].length; doce++){
	            	if (doce == 0){
	            		sumapreciation += populacao.getIndivduo(0).getGns()[numClareira][doce]*1.5;
            		}else if(doce == 1){
            			sumapreciation += populacao.getIndivduo(0).getGns()[numClareira][doce]*1.4;
            		}else if(doce == 2){
            			sumapreciation += populacao.getIndivduo(0).getGns()[numClareira][doce]*1.3;
            		}else if(doce == 3){
            			sumapreciation += populacao.getIndivduo(0).getGns()[numClareira][doce]*1.2;
            		}else if(doce == 4){
            			sumapreciation += populacao.getIndivduo(0).getGns()[numClareira][doce]*1.1;
            		}
	            }
	            numClareira++;
	            System.out.println(path.get(i).getValueMovement()/sumapreciation);
	            game.printmap(mapChar,path.get(i).getValueMovement()/sumapreciation);
			}
			else {
				mapChar[path.get(i).getxPosition()][path.get(i).getyPosition()] = 'I';
				mapChar[path.get(i).getPrevious().getxPosition()][path.get(i).getPrevious().getyPosition()] = 'P';
				System.out.println(path.get(i).getValueMovement());
				game.printmap(mapChar,path.get(i).getValueMovement());
			}
			while((int)System.currentTimeMillis() - tempo < 200);
			tempo = (int)System.currentTimeMillis();
		}
	}

}
