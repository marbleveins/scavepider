package game;

public class CollisionSolver {
	
	public void solve(Body realBody, CollisionInfo info){
	    if ( info.atraviesaLimitesPantalla == true ){
	    	//ESTO ES PARA DEBUG
	    	resetPos(realBody);
	    	return;
	    }
		//que caiga si esta saltando y choca con techo y pared a la vez. esquina
		if (info.contactX && info.contactYtop && realBody.speedY < 0)
			realBody.speedY = info.moveY = 0;
        
        if (info.contactYbottom || info.contactYtop)
        {
        	realBody.y += info.moveY;
        	realBody.speedY = 0;
        	//se mueve hasta que colisiona y se pone en 0 para que no siga yendo para ese lado
        	if (info.contactYbottom)
        		realBody.standing = true;
        }
        if (realBody.speedY == 0)
        	realBody.jumping = false;
        
        if (info.contactX)
        {
        	realBody.x += info.moveX;
        	realBody.speedX = 0;
        }
	}
	
	public void solve(Player player, CollisionInfo info){
	    if ( info.atraviesaLimitesPantalla == true ){
	    	//ESTO ES PARA DEBUG
	    	resetPos(player.b);
	    	return;
	    }
		//que caiga si esta saltando y choca con techo y pared a la vez. esquina
		if (info.contactX && info.contactYtop && player.b.speedY < 0)
			player.b.speedY = info.moveY = 0;
        
        if (info.contactYbottom || info.contactYtop)
        {
        	player.b.y += info.moveY;
        	player.b.speedY = 0;
        	//se mueve hasta que colisiona y se pone en 0 para que no siga yendo para ese lado
        	if (info.contactYbottom)
        		player.b.standing = true;
        }
        if (player.b.speedY == 0)
        	player.b.jumping = false;
        
        if (info.contactX)
        {
        	player.b.x += info.moveX;
        	player.b.speedX = 0;
        }
	}
	
	public void solve(Bicho bicho, CollisionInfo info){
	    if ( info.atraviesaLimitesPantalla == true ){
	    	//ESTO ES PARA DEBUG
	    	resetPos(bicho.b);
	    	return;
	    }
		//que caiga si esta saltando y choca con techo y pared a la vez. esquina
		if (info.contactX && info.contactYtop && bicho.b.speedY < 0)
			bicho.b.speedY = info.moveY = 0;
        
        if (info.contactYbottom || info.contactYtop)
        {
        	bicho.b.y += info.moveY;
        	bicho.b.speedY = 0;
        	//se mueve hasta que colisiona y se pone en 0 para que no siga yendo para ese lado
        	if (info.contactYbottom)
        		bicho.b.standing = true;
        }
        if (bicho.b.speedY == 0)
        	bicho.b.jumping = false;
        
        if (info.contactX)
        {
        	bicho.b.x += info.moveX;
        	bicho.b.speedX = 0;
        }
	}
	
	/**para debug**/
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
