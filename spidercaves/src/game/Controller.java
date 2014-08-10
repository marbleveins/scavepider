package game;

import org.newdawn.slick.GameContainer;

public class Controller {

    private CollisionDetector detector;
    private CollisionSolver solver;
    
    public Controller(){
    	solver = new CollisionSolver();
    	detector = new CollisionDetector();
    }
    
    /**
	 * En vez del parametro player hay que pasarle todos los objetos que se mueven y hacer lo mismo para todos.
	 * Por cada segmento del movimiento del objeto que tenga el movimiento mas grande, iterar en todos los objetos:  
	 * si hay colision resolver
	 * y volver a detectar
	 */
	public void update(GameContainer container, int delta, int scale, Scenario scenario, Player player)
	{
		handleKeyBoardInput(container, delta, scale, player);
		//handleBichosIntelligence?
		/*
		player.b.moveXrequest = player.b.speedX;
		player.b.moveYrequest = player.b.speedY;
		player.b.speedX*/
		
		manageCollisions(container, delta, scenario, player);
		
		//aca estan los proyectiles pero es algo que va a quedar incluido en manageCollisions() junto a todos los objetos movibles supongo o vivos y no vivos por otro lado
		scenario.update(container, delta);
			
		
	}
	
	/**ahora esta funcionando con player solo pero hay que hacer que funcione para todos los objetos vivos (y no vivos?)**/
	private void manageCollisions(GameContainer container, int delta, Scenario scenario, Player player){
		//lo hago mas grande para que nunca sea mayor a 1
		int segmentos = getMaxSegmentos(scenario, player);
		for (int s=0; s<segmentos; s++){
			
			playerCollision(scenario, player, delta, segmentos);
			
			if (scenario.bichos != null && scenario.bichos.size() > 0){
				for (int i=0; i<scenario.bichos.size(); i++){
					bichoCollision(scenario, scenario.bichos.get(i), delta, segmentos);
				}
			}
		}
	}
	
	private void playerCollision(Scenario scenario, Player player, int delta, int segmentos){
		//consigue la colision de cada objeputs en este segmento del movimiento entero
		CollisionInfo data = detector.detectContact(scenario, player.b, player.v, delta, segmentos);
		
		//resuelve si tienen colision y encamina
		solver.solve(player.b, data);
		
		//va moviendo los objetos cada segmento del movimiento entero del frame
		player.b.y += player.b.speedY/segmentos;
		player.b.x += player.b.speedX/segmentos;
	}
	private void bichoCollision(Scenario scenario, Bicho bicho, int delta, int segmentos){
		//consigue la colision de cada objeputs en este segmento del movimiento entero
		CollisionInfo data = detector.detectContact(scenario, bicho.b, bicho.v, delta, segmentos);
		
		//resuelve si tienen colision y encamina
		solver.solve(bicho.b, data);
		
		//va moviendo los objetos cada segmento del movimiento entero del frame
		bicho.b.y += bicho.b.speedY/segmentos;
		bicho.b.x += bicho.b.speedX/segmentos;
	}
	private int getMaxSegmentos(Scenario scenario, Player player){
		int maxSeg = (int) Math.ceil( modulo( player.b.vectorLenght() ) );
		if ( scenario.bichos != null && scenario.bichos.size() > 0 ){
			for ( int i=0; i < scenario.bichos.size(); i++ ){
				if ( (int) Math.ceil( modulo( scenario.bichos.get(i).b.vectorLenght() ) ) > maxSeg )
					maxSeg = (int) Math.ceil( modulo( scenario.bichos.get(i).b.vectorLenght() ) );
			}
		}
		
		return maxSeg;
	}
	
	/** get move requests and such **/
	private void handleKeyBoardInput(GameContainer container, int delta, int scale, Player player){
		//ahora esta funcionando solo para el player
		player.handleKeyboardInput(container, delta, scale);
	}
	

	private float modulo(float v){
		if (v >= 0)
			return v;
		else
			return -1*v;
	}
	
	
}
