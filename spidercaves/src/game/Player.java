package game;
import game.Enum.Facing;
import game.Enum.State;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player{
	public static final int PLAYERSIZE = 32;
	float x, y;
	float speedX, speedY;
    float accelX, decelX;
    float maxSpeedX, maxSpeedY;
    
    float jumpStartSpeedY;
    boolean jumping, jumpKeyDown;
    State state;//hay veces que no podes con 1 sola, ver
    protected Facing facing;
    int collisionPoint[][];

    
    protected Image sprite;
    protected HashMap<Facing, Image> sprites;
    protected HashMap<Facing, Animation> aRunning, aStanding, aJumping, aFalling;
    protected long lastTimeMoved;
    private Scenario scenario;
    
 
    public Player(float x, float y, Scenario _scenario) throws SlickException{
    	this.x = x;
        this.y = y;
        accelX = 0.4f;
        decelX = accelX*4;
        maxSpeedX = 3;
        maxSpeedY = 10;
        jumpStartSpeedY = 4;
        jumping = false;
        jumpKeyDown = false;
        
        scenario = _scenario;
        initCollisionPoints();
        facing = Facing.RIGHT;
        state = State.FallingR;
        setSprite(takeBG(new Image("data/1.png")));
        
        
        Image [] runRight = {takeBG(new Image("data/1.png")), takeBG(new Image("data/2.png")), takeBG(new Image("data/3.png")), takeBG(new Image("data/4.png")),
        		takeBG(new Image("data/5.png")), takeBG(new Image("data/6.png")), takeBG(new Image("data/7.png")), takeBG(new Image("data/8.png"))};
        setRunningAnim(runRight, 100);
        
        Image [] jumpRight = {takeBG(new Image("data/j1.png")), takeBG(new Image("data/j2.png")), takeBG(new Image("data/j3.png")), takeBG(new Image("data/j4.png")),
        		takeBG(new Image("data/j5.png")), takeBG(new Image("data/j6.png")), takeBG(new Image("data/j7.png"))};
        setJumpingAnim(jumpRight, 100);
    }
    
	protected void setSprite(Image i){
	    sprites = new HashMap<Facing,Image>();
	    sprites.put(Facing.RIGHT, i);
	    sprites.put(Facing.LEFT , i.getFlippedCopy(true, false));
	}
	
    public void render(){
    	if (jumping){
    		aJumping.get(facing).draw(x, y);
    	}
    	else if(aRunning != null && lastTimeMoved+150 > System.currentTimeMillis()){
            aRunning.get(facing).draw(x,y);
        }else{            
            sprites.get(facing).draw(x, y);
        }
    }
    
    protected void setRunningAnim(Image[] images, int frameDuration){
        aRunning = new HashMap<Facing,Animation>();
 
        //we can just put the right facing in with the default images
        aRunning.put(Facing.RIGHT, new Animation(images,frameDuration));
 
        Animation facingLeftAnimation = new Animation();
        for(Image i : images){
            facingLeftAnimation.addFrame(i.getFlippedCopy(true, false), frameDuration);
        }
        aRunning.put(Facing.LEFT, facingLeftAnimation);
 
    }
    protected void setStandingAnim(Image[] images, int frameDuration){
        aStanding = new HashMap<Facing,Animation>();
 
        //we can just put the right facing in with the default images
        aStanding.put(Facing.RIGHT, new Animation(images,frameDuration));
 
        Animation facingLeftAnimation = new Animation();
        for(Image i : images){
            facingLeftAnimation.addFrame(i.getFlippedCopy(true, false), frameDuration);
        }
        aStanding.put(Facing.LEFT, facingLeftAnimation);
 
    }
    protected void setJumpingAnim(Image[] images, int frameDuration){
        aJumping = new HashMap<Facing,Animation>();
 
        //we can just put the right facing in with the default images
        aJumping.put(Facing.RIGHT, new Animation(images,frameDuration));
 
        Animation facingLeftAnimation = new Animation();
        for(Image i : images){
            facingLeftAnimation.addFrame(i.getFlippedCopy(true, false), frameDuration);
        }
        aJumping.put(Facing.LEFT, facingLeftAnimation);
 
    }
    
    public void handleKeyboardInput(Input i, int delta){
        boolean moveRequest = false;
        if (speedY == 0) jumping = false;
        
        if (i.isKeyDown(Input.KEY_A) || i.isKeyDown(Input.KEY_LEFT))
        {
            speedX -= accelX;
            if (speedY == 0) state = State.RunningL;
            moveRequest = true;
            facing = Facing.LEFT;
            lastTimeMoved = System.currentTimeMillis();   
        }
        if (i.isKeyDown(Input.KEY_D) || i.isKeyDown(Input.KEY_RIGHT))
        {
            speedX += accelX;
            if (speedY == 0) state = State.RunningR;
            moveRequest = true;
            facing = Facing.RIGHT;
            lastTimeMoved = System.currentTimeMillis();   
        }
        if ( (i.isKeyDown(Input.KEY_W) || i.isKeyDown(Input.KEY_UP)) && !jumping && !jumpKeyDown )
        {
            speedY = -jumpStartSpeedY;
            if (facing == Facing.RIGHT)
            	state = State.JumpingR;
            else
            	state = State.JumpingL;
            jumping = true;
            jumpKeyDown = true;
        }
        if ( !i.isKeyDown(Input.KEY_W) && !i.isKeyDown(Input.KEY_UP))
            jumpKeyDown = false;

        if (speedX > maxSpeedX) speedX = maxSpeedX;
        if (speedX < -maxSpeedX) speedX = -maxSpeedX;
        if (speedY < -maxSpeedY) speedY = -maxSpeedY;

        speedY += scenario.gravity;

        if (!moveRequest)
        {
            if (speedX < 0) speedX += decelX;
            if (speedX > 0) speedX -= decelX;
            if (speedX > 0 && speedX < decelX) speedX = 0;
            if (speedX < 0 && speedX > -decelX) speedX = 0;
            if (!jumping){
            	if (facing == Facing.RIGHT)
            		state = State.StandingR;
            	else
            		state = State.StandingL;
            }
        }
    }
    
    private void initCollisionPoints(){
    	int points[][] = {
    	        { 13,  5  }, { 17, 5  }, // Top of head
    	        { 13,  31 }, { 17, 31 }, // Feet
    	        { 6,  10 }, { 6,  20 }, // Left arm
    	        { 25, 10 }, { 25, 20 }  // Right arm
    	};
    	collisionPoint = points;
    }
    
    private Image takeBG(Image player) throws SlickException{
    	Image result = new Image(PLAYERSIZE,PLAYERSIZE);
		Graphics g = result.getGraphics();
		g.setColor(Color.transparent);
		g.fillRect(0,0,PLAYERSIZE,PLAYERSIZE);
		Color backGround = player.getColor(0, 0);
		for (int y=0 ; y<PLAYERSIZE ; y++){
         	for (int x=0 ; x<PLAYERSIZE ; x++){
         		if ( player.getColor(x, y).r != backGround.r || player.getColor(x, y).g != backGround.g || player.getColor(x, y).b != backGround.b ){
         			g.setColor( player.getColor(x, y) );
         			g.fillRect(x, y, 1, 1);
         		}
            }
        }
		g.flush();//IMPORTANT!!!
    	return result;
    }
    
    public void update(GameContainer container, int arg1){
    	Input input = container.getInput();
		handleKeyboardInput(input, 2);
		controlCollision2();
		y += speedY;
	    x += speedX;
	    if (x >= 320-PLAYERSIZE || x <= 0 || y >= 480-PLAYERSIZE || y <= 0) {x = 20;y = 5;}
    }
    
    private void controlCollision(){
    	boolean contactX = false, contactYbottom = false, contactYtop = false;//tipo de colision
        float nextMoveX = speedX;
        float nextMoveY = speedY;

        float projectedMoveX, projectedMoveY, originalMoveX, originalMoveY;
        originalMoveX = nextMoveX;
        originalMoveY = nextMoveY;
        float nextCP1, nextCP2;
        int projectedPosX1UP, projectedPosX2UP, projectedPosY1UP, projectedPosY2UP;
        int projectedPosX1DOWN, projectedPosX2DOWN, projectedPosY1DOWN, projectedPosY2DOWN;
        int projectedPosX1LEFT, projectedPosX2LEFT, projectedPosY1LEFT, projectedPosY2LEFT;
        int projectedPosX1RIGHT, projectedPosX2RIGHT, projectedPosY1RIGHT, projectedPosY2RIGHT;
        String stringX1, stringX2, stringY1, stringY2;
        
        for (int dir = 0; dir < 4; dir++) {
            if (dir == 0 && nextMoveY > 0) continue;//0 arr, 1 ab, 2 izq, 3 der
            if (dir == 1 && nextMoveY < 0) continue;//estos son para que si esta saltando a la derecha
            if (dir == 2 && nextMoveX > 0) continue;// que checkee colisiones para ese lado solamente
            if (dir == 3 && nextMoveX < 0) continue;
            projectedMoveX = (dir >= 2? nextMoveX : 0);
            projectedMoveY = (dir <  2? nextMoveY : 0);
            
            //CORRECCION DE MOVIMIENTO
            //transformar el float en integer para la colision por pixels.. cambia el floor, ceil y round
            //UP
    		projectedPosX1UP = Math.round(collisionPoint[dir*2][0] + x + projectedMoveX);
    		projectedPosY1UP = ceil(collisionPoint[dir*2][1] + y + projectedMoveY);
    		projectedPosX2UP = Math.round(collisionPoint[dir*2+1][0] + x + projectedMoveX);
    		projectedPosY2UP = ceil(collisionPoint[dir*2+1][1] + y + projectedMoveY);
            //DOWN
    		projectedPosX1DOWN = Math.round(collisionPoint[dir*2][0] + x + projectedMoveX);
    		projectedPosY1DOWN = floor(collisionPoint[dir*2][1] + y + projectedMoveY);
    		projectedPosX2DOWN = Math.round(collisionPoint[dir*2+1][0] + x + projectedMoveX);
    		projectedPosY2DOWN = floor(collisionPoint[dir*2+1][1] + y + projectedMoveY);
            //LEFT
    		projectedPosX1LEFT = ceil(collisionPoint[dir*2][0] + x + projectedMoveX);
    		projectedPosY1LEFT = Math.round(collisionPoint[dir*2][1] + y + projectedMoveY);
    		projectedPosX2LEFT = ceil(collisionPoint[dir*2+1][0] + x + projectedMoveX);
    		projectedPosY2LEFT = Math.round(collisionPoint[dir*2+1][1] + y + projectedMoveY);
            //RIGHT
    		projectedPosX1RIGHT = floor(collisionPoint[dir*2][0] + x + projectedMoveX);
    		projectedPosY1RIGHT = Math.round(collisionPoint[dir*2][1] + y + projectedMoveY);
    		projectedPosX2RIGHT = floor(collisionPoint[dir*2+1][0] + x + projectedMoveX);
    		projectedPosY2RIGHT = Math.round(collisionPoint[dir*2+1][1] + y + projectedMoveY);
    		int pointx1 = 0, pointy1 = 0, pointx2 = 0, pointy2 = 0;
            if (dir == 0){
            	pointx1 = projectedPosX1UP;
            	pointy1 = projectedPosY1UP;
            	pointx2 = projectedPosX2UP;
            	pointy2 = projectedPosY2UP;
            }
            if (dir == 1){
            	pointx1 = projectedPosX1DOWN;
            	pointy1 = projectedPosY1DOWN;
            	pointx2 = projectedPosX2DOWN;
            	pointy2 = projectedPosY2DOWN;
            }
            if (dir == 2){
            	pointx1 = projectedPosX1LEFT;
            	pointy1 = projectedPosY1LEFT;
            	pointx2 = projectedPosX2LEFT;
            	pointy2 = projectedPosY2LEFT;
            }
            if (dir == 3){
            	pointx1 = projectedPosX1RIGHT;
            	pointy1 = projectedPosY1RIGHT;
            	pointx2 = projectedPosX2RIGHT;
            	pointy2 = projectedPosY2RIGHT;
            }
            
            //puntos: DIR 0: cabeza 1 y cabeza 2, DIR 1: pie 1 y pie 2  , DIR 2 brazo I 1 y brazo I 2, DIR 3 brazo D 1 y brazo D 2
            while ( scenario.collides(pointx1, pointy1) || scenario.collides(pointx2, pointy2) )//o punto 2
            {
            	//UP
        		projectedPosX1UP = Math.round(collisionPoint[dir*2][0] + x + projectedMoveX);
        		projectedPosY1UP = ceil(collisionPoint[dir*2][1] + y + projectedMoveY);
        		projectedPosX2UP = Math.round(collisionPoint[dir*2+1][0] + x + projectedMoveX);
        		projectedPosY2UP = ceil(collisionPoint[dir*2+1][1] + y + projectedMoveY);
                //DOWN
        		projectedPosX1DOWN = Math.round(collisionPoint[dir*2][0] + x + projectedMoveX);
        		projectedPosY1DOWN = floor(collisionPoint[dir*2][1] + y + projectedMoveY);
        		projectedPosX2DOWN = Math.round(collisionPoint[dir*2+1][0] + x + projectedMoveX);
        		projectedPosY2DOWN = floor(collisionPoint[dir*2+1][1] + y + projectedMoveY);
                //LEFT
        		projectedPosX1LEFT = ceil(collisionPoint[dir*2][0] + x + projectedMoveX);
        		projectedPosY1LEFT = Math.round(collisionPoint[dir*2][1] + y + projectedMoveY);
        		projectedPosX2LEFT = ceil(collisionPoint[dir*2+1][0] + x + projectedMoveX);
        		projectedPosY2LEFT = Math.round(collisionPoint[dir*2+1][1] + y + projectedMoveY);
                //RIGHT
        		projectedPosX1RIGHT = floor(collisionPoint[dir*2][0] + x + projectedMoveX);
        		projectedPosY1RIGHT = Math.round(collisionPoint[dir*2][1] + y + projectedMoveY);
        		projectedPosX2RIGHT = floor(collisionPoint[dir*2+1][0] + x + projectedMoveX);
        		projectedPosY2RIGHT = Math.round(collisionPoint[dir*2+1][1] + y + projectedMoveY);
                if (dir == 0){
                	pointx1 = projectedPosX1UP;
                	pointy1 = projectedPosY1UP;
                	pointx2 = projectedPosX2UP;
                	pointy2 = projectedPosY2UP;
                }
                if (dir == 1){
                	pointx1 = projectedPosX1DOWN;
                	pointy1 = projectedPosY1DOWN;
                	pointx2 = projectedPosX2DOWN;
                	pointy2 = projectedPosY2DOWN;
                }
                if (dir == 2){
                	pointx1 = projectedPosX1LEFT;
                	pointy1 = projectedPosY1LEFT;
                	pointx2 = projectedPosX2LEFT;
                	pointy2 = projectedPosY2LEFT;
                }
                if (dir == 3){
                	pointx1 = projectedPosX1RIGHT;
                	pointy1 = projectedPosY1RIGHT;
                	pointx2 = projectedPosX2RIGHT;
                	pointy2 = projectedPosY2RIGHT;
                }
            	
            	
                if (dir == 0)//arriba
                {
                    nextCP1 = scenario.nextCollisionPointUpwards(projectedPosX1UP, projectedPosY1UP);
                    nextCP2 = scenario.nextCollisionPointUpwards(projectedPosX2UP, projectedPosY2UP);
                    
                    if (nextCP1 >= nextCP2)
                        projectedMoveY = nextCP1 - (collisionPoint[dir*2][1] + y) + 1;//proyectectado el movimiento necesario para quedar sobre el punto de contacto
                    else
                        projectedMoveY = nextCP2 - (collisionPoint[dir*2+1][1] + y) + 1;
                }
                if (dir == 1)//abajo
                {
                	nextCP1 = scenario.nextCollisionPointDownwards(projectedPosX1DOWN, projectedPosY1DOWN);
                    nextCP2 = scenario.nextCollisionPointDownwards(projectedPosX2DOWN, projectedPosY2DOWN);
                    
                    if (nextCP1 <= nextCP2){
                        projectedMoveY = nextCP1 - (collisionPoint[dir*2][1] + y) - 1;//proyectectado el movimiento necesario para quedar sobre el punto de contacto
                    }else{
                        projectedMoveY = nextCP2 - (collisionPoint[dir*2+1][1] + y) - 1;
                    }
                }
                if (dir == 2)//izquierda
                {
                	nextCP1 = scenario.nextCollisionPointLeftwards(projectedPosX1LEFT, projectedPosY1LEFT);
					nextCP2 = scenario.nextCollisionPointLeftwards(projectedPosX2LEFT, projectedPosY2LEFT);
					
					if (nextCP1 >= nextCP2)
						projectedMoveX = nextCP1 - (collisionPoint[dir*2][0] + x) + 1;//proyectectado el movimiento necesario para quedar sobre el punto de contacto
					else
						projectedMoveX = nextCP2 - (collisionPoint[dir*2+1][0] + x) + 1;
                }
                if (dir == 3)//derecha
                {
                	nextCP1 = scenario.nextCollisionPointRightwards(projectedPosX1RIGHT, projectedPosY1RIGHT);
					nextCP2 = scenario.nextCollisionPointRightwards(projectedPosX2RIGHT, projectedPosY2RIGHT);
					
					if (nextCP1 <= nextCP2)
						projectedMoveX = nextCP1 - (collisionPoint[dir*2][0] + x) - 1;//proyectectado el movimiento necesario para quedar sobre el punto de contacto
					else
						projectedMoveX = nextCP2 - (collisionPoint[dir*2+1][0] + x) - 1;
                }
            }


            if (dir >= 2 && dir <= 3) nextMoveX = projectedMoveX;
            if (dir >= 0 && dir <= 1) nextMoveY = projectedMoveY;
        }
        
        //MOVIMIENTO CORREGIDO
        //tipo de colision
        if (nextMoveY > originalMoveY && originalMoveY < 0)
        {
            contactYtop = true;
        }
        if (nextMoveY < originalMoveY && originalMoveY > 0)
        {
            contactYbottom = true;
        }
        if (Math.abs(nextMoveX - originalMoveX) > 0.01f)
        {
            contactX = true;
        }
        //que caiga si esta saltando y choca con techo
        if (contactX && contactYtop && speedY < 0)
            speedY = nextMoveY = 0;
        
        if (contactYbottom || contactYtop)
        {
            y += nextMoveY;
            speedY = 0;

            if (contactYbottom)
                jumping = false;
        }

        if (contactX)
        {
            x += nextMoveX;
            speedX = 0;
        }
    }
    
    private void controlCollision2(){
    	boolean contactX = false, contactYbottom = false, contactYtop = false;//tipo de colision
        float nextMoveX = speedX;
        float nextMoveY = speedY;

        float projectedMoveX, projectedMoveY, originalMoveX, originalMoveY;
        originalMoveX = nextMoveX;
        originalMoveY = nextMoveY;
        
        for (int dir = 0; dir < 4; dir++) {
            if (dir == 0 && nextMoveY > 0) continue;//0 arr, 1 ab, 2 izq, 3 der
            if (dir == 1 && nextMoveY < 0) continue;//estos son para que si esta saltando a la derecha
            if (dir == 2 && nextMoveX > 0) continue;// que checkee colisiones para ese lado solamente
            if (dir == 3 && nextMoveX < 0) continue;
            projectedMoveX = 0;
            projectedMoveY = 0;
            
            //CORRECCION DE MOVIMIENTO
            //transformar el float en integer para la colision por pixels.. cambia el floor, ceil y round
    		int correnctedPx1 = 0, correctedPy1 = 0, correctedPx2 = 0, correctedPy2 = 0;
            if (dir == 0){
            	correnctedPx1 = Math.round(collisionPoint[dir*2][0] + x + projectedMoveX);
            	correctedPy1 = ceil(collisionPoint[dir*2][1] + y + projectedMoveY);
            	correctedPx2 = Math.round(collisionPoint[dir*2+1][0] + x + projectedMoveX);
            	correctedPy2 = ceil(collisionPoint[dir*2+1][1] + y + projectedMoveY);
            }
            if (dir == 1){
            	correnctedPx1 = Math.round(collisionPoint[dir*2][0] + x + projectedMoveX);
            	correctedPy1 = floor(collisionPoint[dir*2][1] + y + projectedMoveY);
            	correctedPx2 = Math.round(collisionPoint[dir*2+1][0] + x + projectedMoveX);
            	correctedPy2 = floor(collisionPoint[dir*2+1][1] + y + projectedMoveY);
            }
            if (dir == 2){
            	correnctedPx1 = ceil(collisionPoint[dir*2][0] + x + projectedMoveX);
            	correctedPy1 = Math.round(collisionPoint[dir*2][1] + y + projectedMoveY);
            	correctedPx2 = ceil(collisionPoint[dir*2+1][0] + x + projectedMoveX);
            	correctedPy2 = Math.round(collisionPoint[dir*2+1][1] + y + projectedMoveY);
            }
            if (dir == 3){
            	correnctedPx1 = floor(collisionPoint[dir*2][0] + x + projectedMoveX);
            	correctedPy1 = Math.round(collisionPoint[dir*2][1] + y + projectedMoveY);
            	correctedPx2 = floor(collisionPoint[dir*2+1][0] + x + projectedMoveX);
            	correctedPy2 = Math.round(collisionPoint[dir*2+1][1] + y + projectedMoveY);
            }
            
            /*
             // Iterate over each object whose bounding box intersects with the player's bounding box
			// until a collision is found
			for (auto it = boundObjects.begin(); it != boundObjects.end() && !contactX && !contactYbottom && !contactYtop; it++)
			{
             */
            float vectorLength = (float) Math.sqrt(nextMoveX * nextMoveX + nextMoveY * nextMoveY);
            int segments = 0;
            while ( !scenario.collides(correnctedPx1, correctedPy1) && !scenario.collides(correctedPx2, correctedPy2 ) && segments < vectorLength )
            {
            	projectedMoveX += nextMoveX / vectorLength;
                projectedMoveY += nextMoveY / vectorLength;
                segments++;
                
                if (dir == 0){
                	correnctedPx1 = Math.round(collisionPoint[dir*2][0] + x + projectedMoveX);
                	correctedPy1 = ceil(collisionPoint[dir*2][1] + y + projectedMoveY);
                	correctedPx2 = Math.round(collisionPoint[dir*2+1][0] + x + projectedMoveX);
                	correctedPy2 = ceil(collisionPoint[dir*2+1][1] + y + projectedMoveY);
                }
                if (dir == 1){
                	correnctedPx1 = Math.round(collisionPoint[dir*2][0] + x + projectedMoveX);
                	correctedPy1 = floor(collisionPoint[dir*2][1] + y + projectedMoveY);
                	correctedPx2 = Math.round(collisionPoint[dir*2+1][0] + x + projectedMoveX);
                	correctedPy2 = floor(collisionPoint[dir*2+1][1] + y + projectedMoveY);
                }
                if (dir == 2){
                	correnctedPx1 = ceil(collisionPoint[dir*2][0] + x + projectedMoveX);
                	correctedPy1 = Math.round(collisionPoint[dir*2][1] + y + projectedMoveY);
                	correctedPx2 = ceil(collisionPoint[dir*2+1][0] + x + projectedMoveX);
                	correctedPy2 = Math.round(collisionPoint[dir*2+1][1] + y + projectedMoveY);
                }
                if (dir == 3){
                	correnctedPx1 = floor(collisionPoint[dir*2][0] + x + projectedMoveX);
                	correctedPy1 = Math.round(collisionPoint[dir*2][1] + y + projectedMoveY);
                	correctedPx2 = floor(collisionPoint[dir*2+1][0] + x + projectedMoveX);
                	correctedPy2 = Math.round(collisionPoint[dir*2+1][1] + y + projectedMoveY);
                }
            }
            
            // If an intersection occurred...
            if (segments < vectorLength) {
                // Apply correction for over-movement
                if (segments > 0)
                {
                    projectedMoveX -= nextMoveX / vectorLength;
                    projectedMoveY -= nextMoveY / vectorLength;
                }
             
                // Adjust the X or Y component of the vector depending on
                // which direction we are currently testing
                if (dir >= 2 && dir <= 3) nextMoveX = projectedMoveX;
                if (dir >= 0 && dir <= 1) nextMoveY = projectedMoveY;
            }
        }
        
        //MOVIMIENTO CORREGIDO
        //tipo de colision
        if (nextMoveY > originalMoveY && originalMoveY < 0)
        {
            contactYtop = true;
        }
        if (nextMoveY < originalMoveY && originalMoveY > 0)
        {
            contactYbottom = true;
        }
        if (Math.abs(nextMoveX - originalMoveX) > 0.01f)
        {
            contactX = true;
        }
        //que caiga si esta saltando y choca con techo
        if (contactX && contactYtop && speedY < 0)
            speedY = nextMoveY = 0;
        
        if (contactYbottom || contactYtop)
        {
            y += nextMoveY;
            speedY = 0;

            if (contactYbottom)
                jumping = false;
        }

        if (contactX)
        {
            x += nextMoveX;
            speedX = 0;
        }
    }
    
    private int ceil(float v){
		return Integer.parseInt(String.valueOf( Math.ceil(v) ).substring(0, String.valueOf( Math.ceil(v) ).indexOf(".")));
	}
	private int floor(float v){
		return Integer.parseInt(String.valueOf( Math.floor(v) ).substring(0, String.valueOf( Math.floor(v) ).indexOf(".")));
	}
//EOF
}