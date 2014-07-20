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
	float speedX, speedY, prevSpeedX, prevSpeedY;
    float accelX = 0.2f, decelX = 0.5f;
    float maxSpeedX = 3, maxSpeedY = 8;
    
    float jumpStartSpeedY = 5;
    boolean jumping, jumpKeyDown, falling;
    State state;//hay veces que no podes con 1 sola, ver
    protected Facing facing;
    int cpScenario[][];

    
    protected Image sprite;
    protected HashMap<Facing, Image> sprites;
    protected HashMap<Facing, Animation> aRunning, aStanding, aJumping, aFalling;
    protected long lastTimeMoved;
    private Scenario scenario;
    private boolean standing = false;
    private long lastShoot;
    Image bulletImage;
 
    public Player(float x, float y, Scenario _scenario) throws SlickException{
    	this.x = x;
        this.y = y;
        jumping = false;
        jumpKeyDown = false;
        standing = false;
        falling = true;
        scenario = _scenario;
        initCollisionPoints();
        facing = Facing.RIGHT;
        state = State.FallingR;
        
        
        //ANIMATION
        setSprite(takeBG(new Image("data/1.png")));
        Image [] runRight = {takeBG(new Image("data/1.png")), takeBG(new Image("data/2.png")), takeBG(new Image("data/3.png")), takeBG(new Image("data/4.png")),
        		takeBG(new Image("data/5.png")), takeBG(new Image("data/6.png")), takeBG(new Image("data/7.png")), takeBG(new Image("data/8.png"))};
        setRunningAnim(runRight, 100);
        
        Image [] jumpRight = {takeBG(new Image("data/j1.png")), takeBG(new Image("data/j2.png")), takeBG(new Image("data/j3.png")), takeBG(new Image("data/j4.png"))};
        setJumpingAnim(jumpRight, 100);
        
        Image [] fallRight = {takeBG(new Image("data/f1.png")), takeBG(new Image("data/f2.png")), takeBG(new Image("data/f3.png")), takeBG(new Image("data/f4.png"))};
        setFallingAnim(fallRight, 100);
        
        //BULLET
        bulletImage = new Image(1,1);
		Graphics g = bulletImage.getGraphics();
		g.setColor(Color.yellow);
		g.fillRect(0,0,1,1);
		g.flush();//IMPORTANT!!!
    }
    
	protected void setSprite(Image i){
	    sprites = new HashMap<Facing,Image>();
	    sprites.put(Facing.RIGHT, i);
	    sprites.put(Facing.LEFT , i.getFlippedCopy(true, false));
	}
	
    public void render(Graphics screenG){
    	switch (state){
    	case StandingL: case StandingR:
    		sprites.get(facing).draw(x, y);
            screenG.drawImage(sprites.get(facing), x, y);
    		break;
    	case RunningL: case RunningR:
    		/*if(aRunning != null && lastTimeMoved+150 > System.currentTimeMillis()){
                screenG.drawAnimation(aRunning.get(facing), x, y);
            }*/
    		screenG.drawAnimation(aRunning.get(facing), x, y);
    		break;
    	case JumpingL: case JumpingR:
    		screenG.drawAnimation(aJumping.get(facing), x, y);
    		break;
    	case FallingL: case FallingR:
    		screenG.drawAnimation(aFalling.get(facing), x, y);
    		break;
    	default:
    		break;
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
        aJumping.get(Facing.RIGHT).stopAt(aJumping.get(Facing.RIGHT).getFrameCount()-1);
        Animation facingLeftAnimation = new Animation();
        for(Image i : images){
            facingLeftAnimation.addFrame(i.getFlippedCopy(true, false), frameDuration);
        }
        aJumping.put(Facing.LEFT, facingLeftAnimation);
        aJumping.get(Facing.LEFT).stopAt(aJumping.get(Facing.LEFT).getFrameCount()-1);
    }
    protected void setFallingAnim(Image[] images, int frameDuration){
        aFalling = new HashMap<Facing,Animation>();
        //we can just put the right facing in with the default images
        aFalling.put(Facing.RIGHT, new Animation(images,frameDuration));
        aFalling.get(Facing.RIGHT).stopAt(aFalling.get(Facing.RIGHT).getFrameCount()-1);
        Animation facingLeftAnimation = new Animation();
        for(Image i : images){
            facingLeftAnimation.addFrame(i.getFlippedCopy(true, false), frameDuration);
        }
        aFalling.put(Facing.LEFT, facingLeftAnimation);
        aFalling.get(Facing.LEFT).stopAt(aFalling.get(Facing.LEFT).getFrameCount()-1);
    }
    
    public void handleKeyboardInput(Input i, int delta, int scale){
    	handleMovement(i);
    	handleShoot(i, scale);
    }
    
    private void initCollisionPoints(){
    	int points[][] = {
    	        { 12,  3  }, { 18, 3  }, // Top of head
    	        { 12,  31 }, { 18, 31 }, // Feet
    	        { 12,  10 }, { 12,  25 }, // Left arm
    	        { 18, 10 }, { 18, 25 }  // Right arm
    	};
    	cpScenario = points;
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
    
    public void update(GameContainer container, int arg1, int scale){
    	Input input = container.getInput();
    	prevSpeedX = speedX; prevSpeedY = speedY;
		handleKeyboardInput(input, arg1, scale);
		controlCollision2();
		y += speedY;
	    x += speedX;
	    if ( atraviesaLimites(0,0) == true ) resetPos();
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
	        		contactX = true;
	        	}
	        	if ( colisionaUnBrazoDerecho(projectedMoveX, projectedMoveY) ) {
	        		contactX = true;
	        	}
	        	if ( colisionaUnPie(projectedMoveX, projectedMoveY) ){
        			contactYbottom = true;
                    standing = true;
	        	}
	        	if ( colisionaUnaCabeza(projectedMoveX, projectedMoveY) ){
	        		contactYtop = true;
	        	}
	        	
	        	/*if ( modulo(nextMoveY / vectorLength) > nextMoveY )
        			projectedMoveY -= nextMoveY;
        		else
        			projectedMoveY -= (nextMoveY / vectorLength != 0)?modulo(nextMoveY / vectorLength):1;
        			
	        	if ( modulo(nextMoveX / vectorLength) > nextMoveX )
        			projectedMoveX -= nextMoveX;
        		else
        			projectedMoveX -= (nextMoveX / vectorLength != 0)?nextMoveX / vectorLength:1;
        		*/
	        	projectedMoveY -= nextMoveY / vectorLength;
	        	projectedMoveX -= nextMoveX / vectorLength;
	        	
        		break;
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
        standing = false;
        
        float projectedMoveX, projectedMoveY;
        projectedMoveX = 0;
        projectedMoveY = 0;
        
      //CORRECCION DE MOVIMIENTO
        //puntos: DIR 0: cabeza 1 y cabeza 2, DIR 1: pie 1 y pie 2  , DIR 2 brazo I 1 y brazo I 2, DIR 3 brazo D 1 y brazo D 2
        float vectorLength = (float) Math.sqrt(nextMoveX * nextMoveX + nextMoveY * nextMoveY);
        if (vectorLength == 0) vectorLength = 1;
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
	        	while ( colisionaUnBrazoIzquierdo(projectedMoveX, projectedMoveY) ){
	        		/*if ( modulo(nextMoveX / vectorLength) > nextMoveX )
	        			projectedMoveX -= nextMoveX;
	        		else
	        			projectedMoveX += (nextMoveX / vectorLength != 0)?modulo(nextMoveX / vectorLength):1;
	        			*/
	        		if (projectedMoveX != (int) projectedMoveX)
	        			projectedMoveX = (int) projectedMoveX;
	        		else
	        			projectedMoveX += 1;
	        		
	        		contactX = true;
	        	}
	        	while ( colisionaUnBrazoDerecho(projectedMoveX, projectedMoveY) ){
	        		/*if ( modulo(nextMoveX / vectorLength) > nextMoveX )
	        			projectedMoveX -= nextMoveX;
	        		else
	        			projectedMoveX -= (nextMoveX / vectorLength != 0)?modulo(nextMoveX / vectorLength):1;
	        			*/
	        		if (projectedMoveX != (int) projectedMoveX)
	        			projectedMoveX = (int) projectedMoveX;
	        		else
	        			projectedMoveX -= 1;
	        		
	        		contactX = true;
	        	}
	        	while ( colisionaUnPie(projectedMoveX, projectedMoveY) ){
	        		/*if ( modulo(nextMoveY / vectorLength) > nextMoveY )
	        			projectedMoveY -= nextMoveY;
	        		else
	        			projectedMoveY -= (nextMoveY / vectorLength != 0)?modulo(nextMoveY / vectorLength):1;
	        		*/
	        		if (projectedMoveY != (int) projectedMoveY)
	        			projectedMoveY = (int) projectedMoveY;
	        		else
	        			projectedMoveY -= 1;
	        		
        			contactYbottom = true;
                    standing = true;
	        	}
	        	while ( colisionaUnaCabeza(projectedMoveX, projectedMoveY) ){
	        		/*if ( modulo(nextMoveY / vectorLength) > nextMoveY )
	        			projectedMoveY -= nextMoveY;
	        		else
	        			projectedMoveY += (nextMoveY / vectorLength != 0)?modulo(nextMoveY / vectorLength):1;
	        		*/
	        		if (projectedMoveY != (int) projectedMoveY)
	        			projectedMoveY = (int) projectedMoveY;
	        		else
	        			projectedMoveY += 1;
	        		
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
    private void controlCollision3(){
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
            while ( (!scenario.collides(cpScenario[dir*2][0] + x + projectedMoveX, cpScenario[dir*2][1] + y + projectedMoveY) 
            		|| !scenario.collides(cpScenario[dir*2+1][0] + x + projectedMoveX, cpScenario[dir*2+1][1] + y + projectedMoveY) ) 
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
	private boolean colisionaAlgoConTerreno(float xAdd, float yAdd){
		for (int dir=0;dir<=3;dir++){
			if (scenario.collides(cpScenario[dir*2][0] + x + xAdd, cpScenario[dir*2][1] + y + yAdd)
				|| scenario.collides(cpScenario[dir*2+1][0] + x + xAdd, cpScenario[dir*2+1][1] + y + yAdd ) )
				return true;
		}
		return false;
	}
	private boolean colisionaUnBrazoIzquierdo(float xAdd, float yAdd){
		if (scenario.collides(cpScenario[4][0] + x + xAdd, cpScenario[4][1] + y + yAdd)
			|| scenario.collides(cpScenario[5][0] + x + xAdd, cpScenario[5][1] + y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaUnBrazoDerecho(float xAdd, float yAdd){
		if (scenario.collides(cpScenario[6][0] + x + xAdd, cpScenario[6][1] + y + yAdd)
			|| scenario.collides(cpScenario[7][0] + x + xAdd, cpScenario[7][1] + y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaUnPie(float xAdd, float yAdd){
		if (scenario.collides(cpScenario[2][0] + x + xAdd, cpScenario[2][1] + y + yAdd)
			|| scenario.collides(cpScenario[3][0] + x + xAdd, cpScenario[3][1] + y + yAdd ) )
			return true;
		return false;
	}
	private boolean colisionaUnaCabeza(float xAdd, float yAdd){
		if (scenario.collides(cpScenario[0][0] + x + xAdd, cpScenario[0][1] + y + yAdd)
			|| scenario.collides(cpScenario[1][0] + x + xAdd, cpScenario[1][1] + y + yAdd ) )
			return true;
		return false;
	}
	
	private boolean atraviesaLimites(float xAdd, float yAdd){
		for (int dir=0;dir<=3;dir++){
			if ( (cpScenario[dir*2][0] + x + xAdd) < 0 || (cpScenario[dir*2][0] + x + xAdd) > 320
			  || (cpScenario[dir*2+1][0] + x + xAdd) < 0 || (cpScenario[dir*2+1][0] + x + xAdd) > 320
			  || (cpScenario[dir*2][1] + y + yAdd) < 0 || (cpScenario[dir*2][1] + y + yAdd) > 480 
			  || (cpScenario[dir*2+1][1] + y + yAdd) < 0 ||(cpScenario[dir*2+1][1] + y + yAdd ) > 480)
				return true;
		}
		return false;
	}
	private void resetPos(){
		x = 25;
		y = 25;
	}
	
	private void handleMovement(Input i){
		boolean moveRequest = false;
		boolean feetsOnFloor = false;
		if ( colisionaUnPie(0, 1) == true)
			feetsOnFloor = true;
		if (speedY == 0){
			aFalling.get(Facing.LEFT).restart();
			aFalling.get(Facing.RIGHT).restart();
		}
        if (speedY < 0){
        	jumping = false;
        	falling = true;
        }
        // 1 == 1 || 
        if (i.isKeyDown(Input.KEY_A) || i.isKeyDown(Input.KEY_LEFT))
        {
            speedX -= accelX;
            if (feetsOnFloor && speedX < -1) state = State.RunningL;
            moveRequest = true;
            facing = Facing.LEFT;
            lastTimeMoved = System.currentTimeMillis();   
        }
        if (i.isKeyDown(Input.KEY_D) || i.isKeyDown(Input.KEY_RIGHT))
        {
            speedX += accelX;
            if (feetsOnFloor && speedX > 1) state = State.RunningR;
            moveRequest = true;
            facing = Facing.RIGHT;
            lastTimeMoved = System.currentTimeMillis();   
        }
        if ( (i.isKeyDown(Input.KEY_W) || i.isKeyDown(Input.KEY_UP)) && feetsOnFloor && !jumpKeyDown )
        {
            speedY = -jumpStartSpeedY;
            state = (facing == Facing.RIGHT)?State.JumpingR:state.JumpingL;
            jumping = true;
            jumpKeyDown = true;
            aJumping.get(Facing.RIGHT).restart();
            aJumping.get(Facing.LEFT).restart();
        }
        if ( !i.isKeyDown(Input.KEY_W) && !i.isKeyDown(Input.KEY_UP))
            jumpKeyDown = false;

        if (speedX > maxSpeedX) speedX = maxSpeedX;
        if (speedX < -maxSpeedX) speedX = -maxSpeedX;
        if (speedY < -maxSpeedY) speedY = -maxSpeedY;

        speedY += scenario.gravity;
        if (speedY > scenario.gravity){
        	falling = true;
        	state = (facing == Facing.RIGHT)?state.FallingR:state.FallingL;
        }
        if (!moveRequest)
        {
            if (speedX < 0) speedX += decelX;
            if (speedX > 0) speedX -= decelX;
            if (speedX > 0 && speedX < decelX) speedX = 0;
            if (speedX < 0 && speedX > -decelX) speedX = 0;
            if (feetsOnFloor && ! jumping){
            	if (facing == Facing.RIGHT)
            		state = State.StandingR;
            	else
            		state = State.StandingL;
            }
        }
	}

	
	private void handleShoot(Input i, int scale){
		if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && System.currentTimeMillis() > lastShoot +250 )
        {
			try {
				Proyectil p = new Proyectil((this.x + 16), (this.y + 12), i.getMouseX()/scale, i.getMouseY()/scale, 3, bulletImage);
				scenario.proyectiles.add(p);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lastShoot = System.currentTimeMillis();
        }
	}
	
	
//EOF
}