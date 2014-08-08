package game;
import game.Enum.Facing;
import game.Enum.State;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Player{
	public static final int SPRITE_SIZE = 32;
	public Body b;
	public VisualBody v;
	
    boolean jumpKeyDown = false;
    State state = State.FallingR;;//hay veces que no podes con 1 sola, ver
    private Facing facing = Facing.RIGHT;
    
    private Image sprite;
    public HashMap<Facing, Image> sprites;
    public HashMap<Facing, Animation> aRunning, aStanding, aJumping, aFalling;
    private Image [] iRunningR, iStandingR, iJumpingR, iFallingR;
    private Scenario scenario;
    private long lastShoot;
    Image bulletImage;
 
    public Player(float x, float y, Scenario _scenario) throws SlickException{
    	b = new Body();
    	b.x = x;
        b.y = y;
        scenario = _scenario;
        scenario.player = this;
    }
    
    public void render(Graphics screenG){
    	switch (state){
    	case StandingL: case StandingR:
    		sprites.get(facing).draw(b.x, b.y);
            screenG.drawImage(sprites.get(facing), b.x, b.y);
    		break;
    	case RunningL: case RunningR:
    		/*if(aRunning != null && lastTimeMoved+150 > System.currentTimeMillis()){
                screenG.drawAnimation(aRunning.get(facing), x, y);
            }*/
    		screenG.drawAnimation(aRunning.get(facing), b.x, b.y);
    		break;
    	case JumpingL: case JumpingR:
    		screenG.drawAnimation(aJumping.get(facing), b.x, b.y);
    		break;
    	case FallingL: case FallingR:
    		screenG.drawAnimation(aFalling.get(facing), b.x, b.y);
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
		boolean moveXrequest = false;
		
		if (b.speedY == 0){
			aFalling.get(Facing.LEFT).restart();
			aFalling.get(Facing.RIGHT).restart();
		}
        if (b.speedY > 0){
        	b.jumping = false;
        	b.standing = false;
        	b.falling = true;
        }
        //   1 == 1 || 
        if ( (i.isKeyDown(Input.KEY_A) || i.isKeyDown(Input.KEY_LEFT)) && (!i.isKeyDown(Input.KEY_D) && !i.isKeyDown(Input.KEY_RIGHT)))
        {
        	b.speedX -= b.accelX;
        	if (b.speedX > 0) b.speedX -= b.decelX;
			if (b.standing && b.speedX < -1) state = State.RunningL;
            moveXrequest = true;
            facing = Facing.LEFT;
        }
        if ( (i.isKeyDown(Input.KEY_D) || i.isKeyDown(Input.KEY_RIGHT)) && (!i.isKeyDown(Input.KEY_A) && !i.isKeyDown(Input.KEY_LEFT)))
        {
        	b.speedX += b.accelX;
        	if (b.speedX < 0) b.speedX += b.decelX;
            if (b.standing && b.speedX > 1) state = State.RunningR;
            moveXrequest = true;
            facing = Facing.RIGHT;
        }
        if ( (i.isKeyDown(Input.KEY_W) || i.isKeyDown(Input.KEY_UP)) && b.standing && !jumpKeyDown )
        {
        	b.speedY = -b.jumpStartSpeedY;
            state = (facing == Facing.RIGHT)?State.JumpingR:State.JumpingL;
            b.standing = false;
            b.jumping = true;
            jumpKeyDown = true;
            aJumping.get(Facing.RIGHT).restart();
            aJumping.get(Facing.LEFT).restart();
        }
        if ( b.standing && !i.isKeyDown(Input.KEY_W) && !i.isKeyDown(Input.KEY_UP))
            jumpKeyDown = false;

        if (b.speedX > b.maxSpeedX) b.speedX = b.maxSpeedX;
        if (b.speedX < -b.maxSpeedX) b.speedX = -b.maxSpeedX;
        if (b.speedY < -b.maxSpeedY) b.speedY = -b.maxSpeedY;
        if (b.speedY > b.maxSpeedY) b.speedY = b.maxSpeedY;

        b.speedY += scenario.gravity;
        if (b.speedY > scenario.gravity){
        	state = (facing == Facing.RIGHT)?State.FallingR:State.FallingL;
        }
        if (!moveXrequest)
        {
            if (b.speedX < 0) b.speedX += b.decelX;
            if (b.speedX > 0) b.speedX -= b.decelX;
            if (b.speedX > 0 && b.speedX < b.decelX) b.speedX = 0;
            if (b.speedX < 0 && b.speedX > -b.decelX) b.speedX = 0;
            if (b.standing && ! b.jumping){
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
				Proyectil p = new Proyectil((b.x + 16), (b.y + 12), i.getMouseX()/scale, i.getMouseY()/scale, 3, bulletImage);
				scenario.proyectiles.add(p);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			lastShoot = System.currentTimeMillis();
        }
	}
	

	
    /************************
     *     AUX
     ************************/
	
	
	
//EOF
}