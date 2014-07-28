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
	public Body body;
	
    boolean jumping, jumpKeyDown, falling;
    State state;//hay veces que no podes con 1 sola, ver
    private Facing facing;
    
    private Image sprite;
    private HashMap<Facing, Image> sprites;
    private HashMap<Facing, Animation> aRunning, aStanding, aJumping, aFalling;
    private Image [] iRunningR, iStandingR, iJumpingR, iFallingR;
    private Scenario scenario;
    private long lastShoot;
    Image bulletImage;
 
    public Player(float x, float y, Scenario _scenario) throws SlickException{
    	body = new Body();
    	body.collisionRectangle = getCollisionRectangle();
    	body.x = x;
        body.y = y;
        body.accelX = 0.4f;
        body.decelX = 0.8f;
        body.maxSpeedX = 6;
        body.maxSpeedY = 25;
        body.jumpStartSpeedY = 10;
        
        jumping = false;
        jumpKeyDown = false;
        falling = true;
        scenario = _scenario;
        facing = Facing.RIGHT;
        state = State.FallingR;
        
        
        getPlayerSprites();
        getBulletSprites();
        
        //FIN CONSTRUCTOR
    }
    
    public void render(Graphics screenG){
    	switch (state){
    	case StandingL: case StandingR:
    		sprites.get(facing).draw(body.x, body.y);
            screenG.drawImage(sprites.get(facing), body.x, body.y);
    		break;
    	case RunningL: case RunningR:
    		/*if(aRunning != null && lastTimeMoved+150 > System.currentTimeMillis()){
                screenG.drawAnimation(aRunning.get(facing), x, y);
            }*/
    		screenG.drawAnimation(aRunning.get(facing), body.x, body.y);
    		break;
    	case JumpingL: case JumpingR:
    		screenG.drawAnimation(aJumping.get(facing), body.x, body.y);
    		break;
    	case FallingL: case FallingR:
    		screenG.drawAnimation(aFalling.get(facing), body.x, body.y);
    		break;
    	default:
    		break;
    	}
    }
    
	public void handleKeyboardInput(GameContainer container, int delta, int scale){
    	Input input = container.getInput();
    	handleMovement(input, delta);
    	handleShoot(input, delta, scale);
    }
    
	private void handleMovement(Input i, int delta){
		boolean moveRequest = false;
		/*boolean feetsOnFloor = false;
		if ( colisionaUnPie(0, 1) == true)
			feetsOnFloor = true;
		//REEMPLAZADO POR p.standing
		 * */
		if (body.speedY == 0){
			aFalling.get(Facing.LEFT).restart();
			aFalling.get(Facing.RIGHT).restart();
		}
        if (body.speedY < 0){
        	jumping = false;
        	falling = true;
        }
        // 1 == 1 || 
        if ( i.isKeyDown(Input.KEY_A) || i.isKeyDown(Input.KEY_LEFT) && (!i.isKeyDown(Input.KEY_D) && !i.isKeyDown(Input.KEY_RIGHT)))
        {
        	body.speedX -= body.accelX;
        	if (body.speedX > 0) body.speedX -= body.decelX;
            if (body.standing && body.speedX < -1) state = State.RunningL;
            moveRequest = true;
            facing = Facing.LEFT;
        }
        if ( i.isKeyDown(Input.KEY_D) || i.isKeyDown(Input.KEY_RIGHT) && (!i.isKeyDown(Input.KEY_A) && !i.isKeyDown(Input.KEY_LEFT)))
        {
        	body.speedX += body.accelX;
        	if (body.speedX < 0) body.speedX += body.decelX;
            if (body.standing && body.speedX > 1) state = State.RunningR;
            moveRequest = true;
            facing = Facing.RIGHT;
        }
        if ( (i.isKeyDown(Input.KEY_W) || i.isKeyDown(Input.KEY_UP)) && body.standing && !jumpKeyDown )
        {
        	body.speedY = -body.jumpStartSpeedY;
            state = (facing == Facing.RIGHT)?State.JumpingR:State.JumpingL;
            jumping = true;
            jumpKeyDown = true;
            aJumping.get(Facing.RIGHT).restart();
            aJumping.get(Facing.LEFT).restart();
        }
        if ( !i.isKeyDown(Input.KEY_W) && !i.isKeyDown(Input.KEY_UP))
            jumpKeyDown = false;

        if (body.speedX > body.maxSpeedX) body.speedX = body.maxSpeedX;
        if (body.speedX < -body.maxSpeedX) body.speedX = -body.maxSpeedX;
        if (body.speedY < -body.maxSpeedY) body.speedY = -body.maxSpeedY;

        body.speedY += scenario.gravity;
        if (body.speedY > scenario.gravity){
        	falling = true;
        	state = (facing == Facing.RIGHT)?State.FallingR:State.FallingL;
        }
        if (!moveRequest)
        {
            if (body.speedX < 0) body.speedX += body.decelX;
            if (body.speedX > 0) body.speedX -= body.decelX;
            if (body.speedX > 0 && body.speedX < body.decelX) body.speedX = 0;
            if (body.speedX < 0 && body.speedX > -body.decelX) body.speedX = 0;
            if (body.standing && ! jumping){
            	if (facing == Facing.RIGHT)
            		state = State.StandingR;
            	else
            		state = State.StandingL;
            }
        }
        
	}

	private void handleShoot(Input i, int delta, int scale){
		if (i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && System.currentTimeMillis() > lastShoot +5 )
        {
			try {
				Proyectil p = new Proyectil((body.x + 16), (body.y + 12), i.getMouseX()/scale, i.getMouseY()/scale, 3, bulletImage);
				scenario.proyectiles.add(p);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lastShoot = System.currentTimeMillis();
        }
	}
	

	
    /************************
     *     AUX
     ************************/
	
    private void getPlayerSprites() throws SlickException{
    	setPlayerStandingSprite(takeBG(new Image("data/1.png")));
        
        Image [] runRight = {takeBG(new Image("data/1.png")), takeBG(new Image("data/2.png")), takeBG(new Image("data/3.png")), takeBG(new Image("data/4.png")),
        		takeBG(new Image("data/5.png")), takeBG(new Image("data/6.png")), takeBG(new Image("data/7.png")), takeBG(new Image("data/8.png"))};
        aRunning = makeAnimation(runRight, 100);
        
        Image [] jumpRight = {takeBG(new Image("data/j1.png")), takeBG(new Image("data/j2.png")), takeBG(new Image("data/j3.png")), takeBG(new Image("data/j4.png"))};
        aJumping = makeAnimation(jumpRight, 100);
        
        Image [] fallRight = {takeBG(new Image("data/f1.png")), takeBG(new Image("data/f2.png")), takeBG(new Image("data/f3.png")), takeBG(new Image("data/f4.png"))};
        aFalling = makeAnimation(fallRight,	100);
        
    }
    
    private void getBulletSprites() throws SlickException{
    	bulletImage = new Image(1,1);
		Graphics g = bulletImage.getGraphics();
		g.setColor(Color.yellow);
		g.fillRect(0,0,1,1);
		g.flush();//IMPORTANT!!!
    }
    
    /**basura a revisar cuando arregle lo de las animaciones mas adelante*/
	private void setPlayerStandingSprite(Image i){
	    sprites = new HashMap<Facing,Image>();
	    sprites.put(Facing.RIGHT, i);
	    sprites.put(Facing.LEFT , i.getFlippedCopy(true, false));
	}
    
	private HashMap<Facing,Animation> makeAnimation(Image[] images, int frameDuration){
    	HashMap<Facing,Animation> result = new HashMap<Facing,Animation>();
 
        //we can just put the right facing in with the default images
        result.put(Facing.RIGHT, new Animation(images,frameDuration));
        
        Animation facingLeftAnimation = new Animation();
        for(Image i : images){
            facingLeftAnimation.addFrame(i.getFlippedCopy(true, false), frameDuration);
        }
        result.put(Facing.LEFT, facingLeftAnimation);
        return result;
    }
    
	/**setea transparente el background. setea transparentes los puntos iguales al (0,0) de la imagen*/
	private Image takeBG(Image img) throws SlickException{
		//NO SE PUEDE CAMBIAR UN COLOR QUE YA ESTA A TRANSPARENTE
		//SOLO ME QUEDA HACER UNA NUEVA Y DIBUJAR SIN EL FONDO
    	Image result = new Image(PLAYERSIZE,PLAYERSIZE);
		Graphics g = result.getGraphics();
		g.setColor(Color.transparent);
		g.fillRect(0,0,PLAYERSIZE,PLAYERSIZE);
		Color backGround = img.getColor(0, 0);
		for (int y=0 ; y<PLAYERSIZE ; y++){
         	for (int x=0 ; x<PLAYERSIZE ; x++){
         		if ( img.getColor(x, y).r != backGround.r || img.getColor(x, y).g != backGround.g || img.getColor(x, y).b != backGround.b ){
         			g.setColor( img.getColor(x, y) );
         			g.fillRect(x, y, 1, 1);
         		}
            }
        }
		g.flush();//IMPORTANT!!!
    	return result;
    }
    
	private int [][] getCollisionRectangle(){
    	int points[][] = {
    	        { 12,  3  }, { 18, 3  }, // Top of head
    	        { 12,  31 }, { 18, 31 }, // Feet
    	        { 12,  10 }, { 12,  25 }, // Left arm
    	        { 18, 10 }, { 18, 25 }  // Right arm
    	};
    	return points;
    }
	
	
//EOF
}