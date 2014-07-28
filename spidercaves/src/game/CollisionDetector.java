package game;

public class CollisionDetector {

	
	public boolean colisiona(Scenario scenario, float x, float y)
	{
		//(int) porque es por pixels y el pixel 5 es del 5.0 hasta el 5.9
	    if (scenario.collisionPoints.containsKey((int)x) == true && scenario.collisionPoints.get((int)x).get((int)y) != null)
	        return true;
	    return false;
	}
	
	public Data scenarioAndBody(Scenario scenario, Body b, int delta){
		Data data = new Data();
		if (atraviesaLimitesPantalla(b) == true){
			//ESTO ES PARA DEBUG
			data.atraviesaLimitesPantalla = true;
			return data;
		}
		
        float projectedMoveX = 0, projectedMoveY = 0;
        float vectorLength = (float) Math.sqrt(b.speedX * b.speedX + b.speedY * b.speedY);
        if (vectorLength == 0) vectorLength = 1;
        
        for (float segment=0; segment <= vectorLength; segment++){
        	
        	projectedMoveX += b.speedX / vectorLength;
        	projectedMoveY += b.speedY / vectorLength;
        	
        	if (modulo(projectedMoveX) > modulo(b.speedX))
        		projectedMoveX = b.speedX;
        	if (modulo(projectedMoveY) > modulo(b.speedY))
        		projectedMoveY = b.speedY;
        	
        	if ( colisionaAlgoConTerreno(scenario, b, projectedMoveX, projectedMoveY) )
	        {
	        	while ( colisionaIzquierda(scenario, b, projectedMoveX, projectedMoveY) ){
	        		if (projectedMoveX != (int) projectedMoveX)
	        			projectedMoveX = (int) projectedMoveX;
	        		else
	        			projectedMoveX += 1;
	        		data.contactX = true;
	        	}
	        	while ( colisionaDerecha(scenario, b, projectedMoveX, projectedMoveY) ){
	        		if (projectedMoveX != (int) projectedMoveX)
	        			projectedMoveX = (int) projectedMoveX;
	        		else
	        			projectedMoveX -= 1;
	        		data.contactX = true;
	        	}
	        	while ( colisionaBottom(scenario, b, projectedMoveX, projectedMoveY) ){
	        		if (projectedMoveY != (int) projectedMoveY)
	        			projectedMoveY = (int) projectedMoveY;
	        		else
	        			projectedMoveY -= 1;
	        		data.contactYbottom = true;
	        	}
	        	while ( colisionaTop(scenario, b, projectedMoveX, projectedMoveY) ){
	        		if (projectedMoveY != (int) projectedMoveY)
	        			projectedMoveY = (int) projectedMoveY;
	        		else
	        			projectedMoveY += 1;
	        		data.contactYtop = true;
	        	}
	        }
        }
        data.moveX = projectedMoveX;
    	data.moveY = projectedMoveY;
    	
        return data;
    }
	
	public Data detectContact(Scenario scenario, Body b, int delta){
		Data data = new Data();
		if (atraviesaLimitesPantalla(b) == true){
			//ESTO ES PARA DEBUG
			data.atraviesaLimitesPantalla = true;
			return data;
		}
		
        if ( colisionaAlgoConTerreno(scenario, b, b.speedX, b.speedY) )
        {
        	while ( colisionaIzquierda(scenario, b, b.speedX, b.speedY) ){
        		if (b.speedX != (int) b.speedX)
        			b.speedX = (int) b.speedX;
        		else
        			b.speedX += 1;
        		data.contactX = true;
        	}
        	while ( colisionaDerecha(scenario, b, b.speedX, b.speedY) ){
        		if (b.speedX != (int) b.speedX)
        			b.speedX = (int) b.speedX;
        		else
        			b.speedX -= 1;
        		data.contactX = true;
        	}
        	while ( colisionaBottom(scenario, b, b.speedX, b.speedY) ){
        		if (b.speedY != (int) b.speedY)
        			b.speedY = (int) b.speedY;
        		else
        			b.speedY -= 1;
        		data.contactYbottom = true;
        	}
        	while ( colisionaTop(scenario, b, b.speedX, b.speedY) ){
        		if (b.speedY != (int) b.speedY)
        			b.speedY = (int) b.speedY;
        		else
        			b.speedY += 1;
        		data.contactYtop = true;
        	}
        }
        
        data.moveX = b.speedX;
    	data.moveY = b.speedY;
    	
        return data;
    }
	
	
	
	
	/***************
	 *     AUX
	 ***************/
	
	private boolean colisionaAlgoConTerreno(Scenario scenario, Body pb, float xAdd, float yAdd){
		for (int dir=0;dir<=3;dir++){
			if (scenario.collides(pb.collisionRectangle[dir*2][0] + pb.x + xAdd, pb.collisionRectangle[dir*2][1] + pb.y + yAdd)
				|| scenario.collides(pb.collisionRectangle[dir*2+1][0] + pb.x + xAdd, pb.collisionRectangle[dir*2+1][1] + pb.y + yAdd ) )
				return true;
		}
		return false;
	}
	private boolean colisionaIzquierda(Scenario scenario, Body pb, float xAdd, float yAdd){
		if (scenario.collides(pb.collisionRectangle[4][0] + pb.x + xAdd, pb.collisionRectangle[4][1] + pb.y + yAdd)
			|| scenario.collides(pb.collisionRectangle[5][0] + pb.x + xAdd, pb.collisionRectangle[5][1] + pb.y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaDerecha(Scenario scenario, Body pb, float xAdd, float yAdd){
		if (scenario.collides(pb.collisionRectangle[6][0] + pb.x + xAdd, pb.collisionRectangle[6][1] + pb.y + yAdd)
			|| scenario.collides(pb.collisionRectangle[7][0] + pb.x + xAdd, pb.collisionRectangle[7][1] + pb.y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaBottom(Scenario scenario, Body pb, float xAdd, float yAdd){
		if (scenario.collides(pb.collisionRectangle[2][0] + pb.x + xAdd, pb.collisionRectangle[2][1] + pb.y + yAdd)
			|| scenario.collides(pb.collisionRectangle[3][0] + pb.x + xAdd, pb.collisionRectangle[3][1] + pb.y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaTop(Scenario scenario, Body pb, float xAdd, float yAdd){
		if (scenario.collides(pb.collisionRectangle[0][0] + pb.x + xAdd, pb.collisionRectangle[0][1] + pb.y + yAdd)
			|| scenario.collides(pb.collisionRectangle[1][0] + pb.x + xAdd, pb.collisionRectangle[1][1] + pb.y + yAdd ) )
			return true;
		return false;
	}
	
	private boolean atraviesaLimitesPantalla(Body b){
		for (int dir=0;dir<=3;dir++){
			if ( (b.collisionRectangle[dir*2][0] + b.x) < 0 || (b.collisionRectangle[dir*2][0] + b.x) > 320
			  || (b.collisionRectangle[dir*2+1][0] + b.x) < 0 || (b.collisionRectangle[dir*2+1][0] + b.x) > 320
			  || (b.collisionRectangle[dir*2][1] + b.y) < 0 || (b.collisionRectangle[dir*2][1] + b.y) > 480 
			  || (b.collisionRectangle[dir*2+1][1] + b.y) < 0 ||(b.collisionRectangle[dir*2+1][1] + b.y ) > 480)
				return true;
		}
		return false;
	}
	
	private int ceil(float v){
		return (int)  Math.ceil(v);
	}
	private int floor(float v){
		return (int)  Math.floor(v);
	}
	private float modulo(float v){
		if (v >= 0)
			return v;
		else
			return -1*v;
	}
	
	//EOF
}
