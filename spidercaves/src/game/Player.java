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
    private boolean standing = false;
 
    public Player(float x, float y, Scenario _scenario) throws SlickException{
    	this.x = x;
        this.y = y;
        accelX = 0.4f;
        decelX = accelX*4;
        maxSpeedX = 2.5f;
        maxSpeedY = 10;
        jumpStartSpeedY = 4.5f;
        jumping = false;
        jumpKeyDown = false;
        standing = false;
        
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
        // 1 == 1 || 
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
    	        { 13,  3  }, { 17, 3  }, // Top of head
    	        { 13,  31 }, { 17, 31 }, // Feet
    	        { 6,  8 }, { 6,  25 }, // Left arm
    	        { 25, 8 }, { 25, 25 }  // Right arm
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
		controlCollision1();
		y += speedY;
	    x += speedX;
	    if (x >= 320-PLAYERSIZE || x <= 0 || y >= 480-PLAYERSIZE || y <= 0) {x = 20;y = 5;}
    }
    
    private void controlCollision1(){
    	boolean contactX = false, contactYbottom = false, contactYtop = false;//tipo de colision
        float nextMoveX = speedX;
        float nextMoveY = speedY;
        standing = false;
        
        float projectedMoveX, projectedMoveY;
        projectedMoveX = 0;
        projectedMoveY = 0;
        
      //CORRECCION DE MOVIMIENTO
        //puntos: DIR 0: cabeza 1 y cabeza 2, DIR 1: pie 1 y pie 2  , DIR 2 brazo I 1 y brazo I 2, DIR 3 brazo D 1 y brazo D 2
        float vectorLength = (float) Math.sqrt(nextMoveX * nextMoveX + nextMoveY * nextMoveY);
        for (float segment=0; segment <= vectorLength; segment++){
        	projectedMoveX += nextMoveX / vectorLength;
        	projectedMoveY += nextMoveY / vectorLength;
        	if (modulo(projectedMoveX) > modulo(nextMoveX))
        		projectedMoveX = nextMoveX;
        	if (modulo(projectedMoveY) > modulo(nextMoveY))
        		projectedMoveY = nextMoveY;
	        if ( colisionaAlgoConTerreno(projectedMoveX, projectedMoveY) )
	        {
	        	//probar con whiles en vez de ifs
	        	if ( colisionaUnBrazoIzquierdo(projectedMoveX, projectedMoveY) ){
	        		if ( modulo(nextMoveX / vectorLength) > nextMoveX )
	        			projectedMoveX -= nextMoveX;
	        		else
	        			projectedMoveX += (nextMoveX / vectorLength != 0)?modulo(nextMoveX / vectorLength):1;
	        			
	        		contactX = true;
	        	}
	        	if ( colisionaUnBrazoDerecho(projectedMoveX, projectedMoveY) ){
	        		if ( modulo(nextMoveX / vectorLength) > nextMoveX )
	        			projectedMoveX -= nextMoveX;
	        		else
	        			projectedMoveX -= (nextMoveX / vectorLength != 0)?modulo(nextMoveX / vectorLength):1;
	        			
	        		contactX = true;
	        	}
	        	if ( colisionaUnPie(projectedMoveX, projectedMoveY) ){
	        		if ( modulo(nextMoveY / vectorLength) > nextMoveY )
	        			projectedMoveY -= nextMoveY;
	        		else
	        			projectedMoveY -= (nextMoveY / vectorLength != 0)?modulo(nextMoveY / vectorLength):1;
	        		
        			contactYbottom = true;
                    standing = true;
	        	}
	        	if ( colisionaUnaCabeza(projectedMoveX, projectedMoveY) ){
	        		if ( modulo(nextMoveY / vectorLength) > nextMoveY )
	        			projectedMoveY -= nextMoveY;
	        		else
	        			projectedMoveY += (nextMoveY / vectorLength != 0)?modulo(nextMoveY / vectorLength):1;
	        		
	        			contactYtop = true;
	        	}
	        }
	        	
        }
        nextMoveX = projectedMoveX;
        nextMoveY = projectedMoveY;
        //MOVIMIENTO CORREGIDO
        
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
        if (speedY == 0)
        	jumping = false;
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
        projectedMoveX = 0;
        projectedMoveY = 0;
        for (int dir = 0; dir < 4; dir++) {
            if (dir == 0 && nextMoveY >= 0) continue;//0 arr, 1 ab, 2 izq, 3 der
            if (dir == 1 && nextMoveY <= 0) continue;//estos son para que si esta saltando a la derecha
            if (dir == 2 && nextMoveX >= 0) continue;// que checkee colisiones para ese lado solamente
            if (dir == 3 && nextMoveX <= 0) continue;
            
            float vectorLength = (float) Math.sqrt(nextMoveX * nextMoveX + nextMoveY * nextMoveY);
            int segments = 0;
            while ( (!scenario.collides(collisionPoint[dir*2][0] + x + projectedMoveX, collisionPoint[dir*2][1] + y + projectedMoveY) 
            		|| !scenario.collides(collisionPoint[dir*2+1][0] + x + projectedMoveX, collisionPoint[dir*2+1][1] + y + projectedMoveY) ) 
            		&& segments < vectorLength )
            {
            	projectedMoveX += nextMoveX / vectorLength;
                projectedMoveY += nextMoveY / vectorLength;
                segments++;
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
            standing = true;
        }
        else
        	standing = false;
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
	private float modulo(float v){
		if (v >= 0)
			return v;
		else
			return -1*v;
	}
	private boolean colisionaAlgoConTerreno(float xAdd, float yAdd){
		for (int dir=0;dir<=3;dir++){
			if (scenario.collides(collisionPoint[dir*2][0] + x + xAdd, collisionPoint[dir*2][1] + y + yAdd)
				|| scenario.collides(collisionPoint[dir*2+1][0] + x + xAdd, collisionPoint[dir*2+1][1] + y + yAdd ) )
				return true;
		}
		return false;
	}
	private boolean colisionaUnBrazoIzquierdo(float xAdd, float yAdd){
		if (scenario.collides(collisionPoint[4][0] + x + xAdd, collisionPoint[4][1] + y + yAdd)
			|| scenario.collides(collisionPoint[5][0] + x + xAdd, collisionPoint[5][1] + y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaUnBrazoDerecho(float xAdd, float yAdd){
		if (scenario.collides(collisionPoint[6][0] + x + xAdd, collisionPoint[6][1] + y + yAdd)
			|| scenario.collides(collisionPoint[7][0] + x + xAdd, collisionPoint[7][1] + y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaUnPie(float xAdd, float yAdd){
		if (scenario.collides(collisionPoint[2][0] + x + xAdd, collisionPoint[2][1] + y + yAdd)
			|| scenario.collides(collisionPoint[3][0] + x + xAdd, collisionPoint[3][1] + y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaUnaCabeza(float xAdd, float yAdd){
		if (scenario.collides(collisionPoint[0][0] + x + xAdd, collisionPoint[0][1] + y + yAdd)
			|| scenario.collides(collisionPoint[1][0] + x + xAdd, collisionPoint[1][1] + y + yAdd ) )
			return true;
		return false;
	}
//EOF
}