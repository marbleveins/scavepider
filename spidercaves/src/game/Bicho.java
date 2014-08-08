package game;

import game.Enum.Facing;
import game.Enum.State;
import org.newdawn.slick.Graphics;

public class Bicho {
	public Body b;
	public VisualBody v;
	private Scenario scenario;
	private Player target;//cualquier cosa viva en realidad
	/**0=floor, 1=ceil, 2=Lwall, 3 = Rwall**/
	private int walkingIn;
	private boolean alive = true;
	
	
	public Bicho(float x, float y, Scenario _scenario){
		b = new Body();
    	b.x = x;
        b.y = y;
        scenario = _scenario;
	}
	
	public void render(Graphics screenG){
		v.sprites.get(v.facing).draw(b.x, b.y);
        screenG.drawImage(v.sprites.get(v.facing), b.x, b.y);
    }
	
	public void nextMove(Player player, int delta){
		if (distanceTo(player) < 30)
			target = player;
		else
			target = null;
		
        if (b.speedY > 0){
        	b.jumping = false;
        	b.standing = false;
        	b.falling = true;
        }
        
		if (target != null){
			if ( b.standing && !b.jumping)
	        {
	            v.state = (v.facing == Facing.RIGHT)?State.JumpingR:State.JumpingL;
	            b.standing = false;
	            b.jumping = true;
	            this.b.speedY = -this.b.jumpStartSpeedY;
	        }
		}
		
		if (b.speedX > b.maxSpeedX) b.speedX = b.maxSpeedX;
        if (b.speedX < -b.maxSpeedX) b.speedX = -b.maxSpeedX;
        if (b.speedY < -b.maxSpeedY) b.speedY = -b.maxSpeedY;
        if (b.speedY > b.maxSpeedY) b.speedY = b.maxSpeedY;

        b.speedY += scenario.gravity;
        if (b.speedY > scenario.gravity){
        	v.state = (v.facing == Facing.RIGHT)?State.FallingR:State.FallingL;
        }
        
	}
	
	
	
	
	/*********************
	 *       AUX
	 *********************/
	
	
	private float distanceTo(Player player){
		float px_closest = 0;
		float py_closest = 0;
		float bx_closest = 0;
		float by_closest = 0;
		
		if ( (player.b.x + player.SPRITE_SIZE) < this.b.x || (player.b.x + player.SPRITE_SIZE) == this.b.x ){
			px_closest = player.b.x + player.SPRITE_SIZE;
			bx_closest = this.b.x;
		}
		if ( player.b.x > (this.b.x + v.SPRITE_WIDTH) || player.b.x == (this.b.x + v.SPRITE_WIDTH) ){
			px_closest = player.b.x;
			bx_closest = this.b.x + v.SPRITE_WIDTH;
		}
		
		if ( (player.b.y + player.SPRITE_SIZE) < this.b.y || (player.b.y + player.SPRITE_SIZE) == this.b.y ){
			py_closest = player.b.y + player.SPRITE_SIZE;
			by_closest = this.b.y;
		}
		if ( player.b.y > (this.b.y + v.SPRITE_HEIGHT) || player.b.y == (this.b.y + v.SPRITE_HEIGHT) ){
			py_closest = player.b.y;
			by_closest = this.b.y + v.SPRITE_HEIGHT;
		}
		
		return (float) Math.sqrt((px_closest-bx_closest) * (px_closest-bx_closest) + (py_closest-by_closest) * (py_closest-by_closest));
	}
	
	//////////////////////////
}

