package game;

public class CollisionDetector {

	
	public boolean colisiona(Scenario scenario, float x, float y)
	{
		//(int) porque es por pixels y el pixel 5 es del 5.0 hasta el 5.9
	    if (scenario.collisionPoints.containsKey((int)x) == true && scenario.collisionPoints.get((int)x).get((int)y) != null)
	        return true;
	    return false;
	}
	
	/*
	public CollisionInfo scenarioAndBody(Scenario scenario, Body b, VisualBody v, int delta){
		CollisionInfo data = new CollisionInfo();
		if (atraviesaLimitesPantalla(b, v) == true){
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
        	
        	if ( colisionaAlgoConTerreno(scenario, b, v, projectedMoveX, projectedMoveY) )
	        {
	        	while ( colisionaIzquierda(scenario, b, v, projectedMoveX, projectedMoveY) ){
	        		if (projectedMoveX != (int) projectedMoveX)
	        			projectedMoveX = (int) projectedMoveX;
	        		else
	        			projectedMoveX += 1;
	        		data.contactX = true;
	        	}
	        	while ( colisionaDerecha(scenario, b, v, projectedMoveX, projectedMoveY) ){
	        		if (projectedMoveX != (int) projectedMoveX)
	        			projectedMoveX = (int) projectedMoveX;
	        		else
	        			projectedMoveX -= 1;
	        		data.contactX = true;
	        	}
	        	while ( colisionaBottom(scenario, b, v, projectedMoveX, projectedMoveY) ){
	        		if (projectedMoveY != (int) projectedMoveY)
	        			projectedMoveY = (int) projectedMoveY;
	        		else
	        			projectedMoveY -= 1;
	        		data.contactYbottom = true;
	        	}
	        	while ( colisionaTop(scenario, b, v, projectedMoveX, projectedMoveY) ){
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
	*/
	
	public CollisionInfo detectContact(Scenario scenario, Body b, VisualBody v, int delta){
		CollisionInfo info = new CollisionInfo();
		if (atraviesaLimitesPantalla(b, v) == true){
			//ESTO ES PARA DEBUG
			info.atraviesaLimitesPantalla = true;
			return info;
		}
		
        if ( colisionaAlgoConTerreno(scenario, b, v, b.speedX, b.speedY) )
        {
        	if ( colisionaIzquierda(scenario, b, v, b.speedX, b.speedY) ){
        		if (b.speedX != (int) b.speedX)
        			b.speedX = (int) b.speedX;
        		else
        			b.speedX += 1;
        		info.contactX = true;
        	}
        	if ( colisionaDerecha(scenario, b, v, b.speedX, b.speedY) ){
        		if (b.speedX != (int) b.speedX)
        			b.speedX = (int) b.speedX;
        		else
        			b.speedX -= 1;
        		info.contactX = true;
        	}
        	if ( colisionaBottom(scenario, b, v, b.speedX, b.speedY) ){
        		if (b.speedY != (int) b.speedY)
        			b.speedY = (int) b.speedY;
        		else
        			b.speedY -= 1;
        		info.contactYbottom = true;
        	}
        	if ( colisionaTop(scenario, b, v, b.speedX, b.speedY) ){
        		if (b.speedY != (int) b.speedY)
        			b.speedY = (int) b.speedY;
        		else
        			b.speedY += 1;
        		info.contactYtop = true;
        	}
        	
        }
        
        info.moveX = b.speedX;
    	info.moveY = b.speedY;
    	
        return info;
    }
	
	
	
	
	/***************
	 *     AUX
	 ***************/
	
	private boolean colisionaAlgoConTerreno(Scenario scenario, Body b, VisualBody v, float xAdd, float yAdd){
		for (int dir=0;dir<=3;dir++){
			if (scenario.collides(v.collisionRectangle[dir*2][0] + b.x + xAdd, v.collisionRectangle[dir*2][1] + b.y + yAdd)
				|| scenario.collides(v.collisionRectangle[dir*2+1][0] + b.x + xAdd, v.collisionRectangle[dir*2+1][1] + b.y + yAdd ) )
				return true;
		}
		return false;
	}
	private boolean colisionaIzquierda(Scenario scenario, Body b, VisualBody v, float xAdd, float yAdd){
		if (scenario.collides(v.collisionRectangle[4][0] + b.x + xAdd, v.collisionRectangle[4][1] + b.y + yAdd)
			|| scenario.collides(v.collisionRectangle[5][0] + b.x + xAdd, v.collisionRectangle[5][1] + b.y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaDerecha(Scenario scenario, Body b, VisualBody v, float xAdd, float yAdd){
		if (scenario.collides(v.collisionRectangle[6][0] + b.x + xAdd, v.collisionRectangle[6][1] + b.y + yAdd)
			|| scenario.collides(v.collisionRectangle[7][0] + b.x + xAdd, v.collisionRectangle[7][1] + b.y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaBottom(Scenario scenario, Body b, VisualBody v, float xAdd, float yAdd){
		if (scenario.collides(v.collisionRectangle[2][0] + b.x + xAdd, v.collisionRectangle[2][1] + b.y + yAdd)
			|| scenario.collides(v.collisionRectangle[3][0] + b.x + xAdd, v.collisionRectangle[3][1] + b.y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaTop(Scenario scenario, Body b, VisualBody v, float xAdd, float yAdd){
		if (scenario.collides(v.collisionRectangle[0][0] + b.x + xAdd, v.collisionRectangle[0][1] + b.y + yAdd)
			|| scenario.collides(v.collisionRectangle[1][0] + b.x + xAdd, v.collisionRectangle[1][1] + b.y + yAdd ) )
			return true;
		return false;
	}
	
	private boolean atraviesaLimitesPantalla(Body b, VisualBody v){
		for (int dir=0;dir<=3;dir++){
			if ( (v.collisionRectangle[dir*2][0] + b.x) < 0 || (v.collisionRectangle[dir*2][0] + b.x) > 320
			  || (v.collisionRectangle[dir*2+1][0] + b.x) < 0 || (v.collisionRectangle[dir*2+1][0] + b.x) > 320
			  || (v.collisionRectangle[dir*2][1] + b.y) < 0 || (v.collisionRectangle[dir*2][1] + b.y) > 480 
			  || (v.collisionRectangle[dir*2+1][1] + b.y) < 0 ||(v.collisionRectangle[dir*2+1][1] + b.y ) > 480)
				return true;
		}
		return false;
	}
	
	
	
	private float modulo(float v){
		if (v >= 0)
			return v;
		else
			return -1*v;
	}
	
	//EOF
}
