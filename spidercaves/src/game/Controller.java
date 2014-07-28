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
		
		manageCollisions(container, delta, scenario, player);
		
		//aca estan los proyectiles pero es algo que va a quedar incluido en manageCollisions() junto a todos los objetos movibles supongo o vivos y no vivos por otro lado
		scenario.update(container, delta);
			
		
	}
	
	/**ahora esta funcionando con player solo pero hay que hacer que funcione para todos los objetos vivos (y no vivos?)**/
	private void manageCollisions(GameContainer container, int delta, Scenario scenario, Player player){
		//lo hago mas grande para que nunca sea mayor a 1
		int segmentos = (int) Math.ceil( modulo( player.body.vectorLenght() ) );
		for (int s=0; s<segmentos; s++){
			
			//consigue la colision de cada los objeputs en este segmento del movimiento entero
			Data data = detector.detectContact(scenario, player.body, delta);
			
			//resuelve si tienen colision y encamina
			solver.solve(player.body, data);
			
			//va moviendo los objetos cada segmento del movimiento entero del frame
			player.body.y += player.body.speedY*1/segmentos;
			player.body.x += player.body.speedX*1/segmentos;
		}
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
