package bots;

import java.util.List;

import INF1771_GameAI.GameAI;


public class SimpleAI extends GameAI {
	
	private static Estado estado=Estado.PERC_NOTHING;
	private int voltas = 0;
	static boolean  Perigo=false;
	static boolean  blocked=false;	
	private static int shotfired=0;
	
	@Override
	public void GetObservations(List<String> o) {
		
		Estado anterior = estado; 	
    	String s="observação desconhecida";
    
    	
             if(o.contains("blueLight") )
            {
            	s="blueLight";
            
            	estado =Estado.PERC_PREMIO;
            } 
            else if(o.contains("redLight") && energy<=80) // powerup
            {
            	if( energy<=30){
            		s="redLight";
            		estado = Estado.PERC_POWERUP;
            	}
            } 
            else if(o.contains("damage"))
            {
            	s="damage";
            	estado = Estado.PERC_PERIGO;
            }
            else if(o.contains("breeze"))
            {
            	s="breeze";
            	estado = Estado.PERC_PERIGO;
            }
            else if(o.contains("flash"))
            {
            	s="flash";
            	estado = Estado.PERC_PERIGO;
            }
            else if(ListContainString(o,"enemy"))
            {
            	s="enemy";
            	estado = Estado.PERC_INIMIGO;
            }
            else if(o.contains("hit"))
            { 
            	
            	s="hit";
            	estado =Estado.PERC_INIMIGO; 
            }
            else if(o.contains("steps"))
            {
            	s="steps";
            	estado = Estado.PERC_STEPS;
            }
    
            else if(o.contains("greenLight"))
            {
            	s="greenLight";
            	estado = Estado.PERC_NOTHING;
            } 
            else if(o.contains("weakLight"))
            {
            	s="weakLight";
            	estado = Estado.PERC_NOTHING;
            }
            else if(o.contains("blocked"))
            {
           
                s="blocked";
                estado = Estado.PERC_BLOCKED;
            
            }
            else
            {
            	estado= Estado.PERC_NOTHING;
            }
           
            
        	System.out.println("Observations: " + s);
       
	}

	@Override
	public void GetObservationsClean() {
		// TODO Auto-generated method stub
	
		
		estado= Estado.PERC_NOTHING;
	
		System.out.println(" Sem observações");

	}

	@Override
	public String GetDecision() {
	
		if(Perigo)
		{
			Perigo=false;
			java.util.Random rand = new java.util.Random();
			int n=rand.nextInt(2);
			if(n==0)
				return "virar_esquerda";
			else
				return "virar_direita";
			
		}
		
		if(!estado.equals(Estado.PERC_STEPS))
			voltas=0;
		
		
		switch(estado){
			case PERC_BLOCKED:
			{      
				java.util.Random rand = new java.util.Random();
				int n=rand.nextInt(2);
				if(n==0)
					return "virar_esquerda";
				else
					return "virar_direita";
			 
			}
			case PERC_PERIGO:
			{	
				Perigo=true;
				return "andar_re";
			
			}	
			case PERC_STEPS:
			{
				if(voltas>=4)
				{
					voltas=0;
					return "andar";
				}
				voltas++;
				return "virar_direita";
			}	
		    case PERC_INIMIGO:
		    	shotfired++;
		    	if(shotfired<=30)
		    		return "atacar";
		    	shotfired=0;
		    	return "bad";
			case PERC_POWERUP:
				return "pegar_powerup";
			case PERC_PREMIO:
				return "pegar_ouro";
			
			default:
			{
				java.util.Random rand = new java.util.Random();
				int n=rand.nextInt(10);
				if(n==1)
					return "virar_esquerda";
				else if(n==2)
					return "virar_direita";
				else
				   return "andar";	
			}
				
			
		}
		
	}

	private boolean ListContainString(List<String> o,String search){
		
		for(String str: o) {
		    if(str.trim().contains(search))
		       return true;
		}
		 return false;
	}


}