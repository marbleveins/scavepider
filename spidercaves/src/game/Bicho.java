package game;

import game.Enum.Facing;
import game.Enum.State;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Bicho {
	public Body body;
	private int SPRITE_SIZE = 32;
	private static final String SPRITES_FOLDER = "data/bicho/";
	private HashMap<Facing, Image> sprites;
	private Facing facing;
	State state;
	private Scenario scenario;
	private Player target;//cualquier cosa viva en realidad
	/**0=floor, 1=ceil, 2=Lwall, 3 = Rwall**/
	private int walkingIn;
	
	
	
	public Bicho(float x, float y, Scenario _scenario){
		body = new Body();
    	body.collisionRectangle = getCollisionRectangle();
    	body.x = x;
        body.y = y;
        body.accelX = 0.4f;
        body.decelX = 0.8f;
        body.maxSpeedX = 10;
        body.maxSpeedY = 25;
        body.jumpStartSpeedY = 15;
        
        scenario = _scenario;
        
        facing = Facing.RIGHT;
        state = State.FallingR;
        
		try {
			getSprites();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
	public void render(Graphics screenG){
		sprites.get(facing).draw(body.x, body.y);
        screenG.drawImage(sprites.get(facing), body.x, body.y);
    }
	
	
	public void nextMove(Player player, int delta){
		if (distanceTo(player) < 30)
			target = player;
		else
			target = null;
		
        if (body.speedY < 0){
        	body.jumping = false;
        	body.standing = false;
        	body.falling = true;
        }
        
		if (target != null){
			if ( body.standing && !body.jumping)
	        {
	            state = (facing == Facing.RIGHT)?State.JumpingR:State.JumpingL;
	            body.standing = false;
	            body.jumping = true;
	            this.body.speedY = -this.body.jumpStartSpeedY;
	        }
		}
        
	}
	
	
	
	
	/*********************
	 *       AUX
	 *********************/
	
	private void getSprites() throws SlickException{
	    sprites = new HashMap<Facing,Image>();
	    sprites.put(Facing.RIGHT, takeBG(new Image(SPRITES_FOLDER + "1.png")));
	    sprites.put(Facing.LEFT , takeBG(new Image(SPRITES_FOLDER + "1.png")).getFlippedCopy(true, false));
        
    }
	
	/**setea transparente el background. setea transparentes los puntos iguales al (0,0) de la imagen*/
	private Image takeBG(Image img) throws SlickException{
		//NO SE PUEDE CAMBIAR UN COLOR QUE YA ESTA A TRANSPARENTE
		//SOLO ME QUEDA HACER UNA NUEVA Y DIBUJAR SIN EL FONDO
    	Image result = new Image(SPRITE_SIZE,SPRITE_SIZE);
		Graphics g = result.getGraphics();
		g.setColor(Color.transparent);
		g.fillRect(0,0,SPRITE_SIZE,SPRITE_SIZE);
		Color backGround = img.getColor(0, 0);
		for (int y=0 ; y<SPRITE_SIZE ; y++){
         	for (int x=0 ; x<SPRITE_SIZE ; x++){
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

	private float distanceTo(Player player){
		float px_closest = 0;
		float py_closest = 0;
		float bx_closest = 0;
		float by_closest = 0;
		
		if ( (player.body.x + player.SPRITE_SIZE) < this.body.x || (player.body.x + player.SPRITE_SIZE) == this.body.x ){
			px_closest = player.body.x + player.SPRITE_SIZE;
			bx_closest = this.body.x;
		}
		if ( player.body.x > (this.body.x + this.SPRITE_SIZE) || player.body.x == (this.body.x + this.SPRITE_SIZE) ){
			px_closest = player.body.x;
			bx_closest = this.body.x + this.SPRITE_SIZE;
		}
		
		if ( (player.body.y + player.SPRITE_SIZE) < this.body.y || (player.body.y + player.SPRITE_SIZE) == this.body.y ){
			py_closest = player.body.y + player.SPRITE_SIZE;
			by_closest = this.body.y;
		}
		if ( player.body.y > (this.body.y + this.SPRITE_SIZE) || player.body.y == (this.body.y + this.SPRITE_SIZE) ){
			py_closest = player.body.y;
			by_closest = this.body.y + this.SPRITE_SIZE;
		}
		
		return (float) Math.sqrt((px_closest-bx_closest) * (px_closest-bx_closest) + (py_closest-by_closest) * (py_closest-by_closest));
	}
	
	//////////////////////////
}

