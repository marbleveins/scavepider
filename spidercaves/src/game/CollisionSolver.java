package game;

public class CollisionSolver {
	
	public void solve(Body realBody, Data data){
	    if ( data.atraviesaLimitesPantalla == true ){
	    	//ESTO ES PARA DEBUG
	    	resetPos(realBody);
	    	return;
	    }
		//que caiga si esta saltando y choca con techo
		if (data.contactX && data.contactYtop && realBody.speedY < 0)
			realBody.speedY = data.moveY = 0;
        
        if (data.contactYbottom || data.contactYtop)
        {
        	realBody.y += data.moveY;
        	realBody.speedY = 0;
        	//se mueve hasta que colisiona y se pone en 0 para que no siga yendo para ese lado
        	if (data.contactYbottom)
        		realBody.standing = true;
        }
        if (realBody.speedY == 0)
        	realBody.jumping = false;
        
        if (data.contactX)
        {
        	realBody.x += data.moveX;
        	realBody.speedX = 0;
        }
	}
	
	private void resetPos(Body b){
		//PARA DEBUG
		b.x = 25;
		b.y = 25;
	}
	
	private float modulo(float v){
		if (v >= 0)
			return v;
		else
			return -1*v;
	}
	
	
	
	//EOF
}
