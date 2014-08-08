package game;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.Enum.Facing;
import game.Enum.TipoBicho;

public class FabricaDePlayers {
	
	private static final String SPRITES_FOLDER = "data/player/";
	private int SPRITE_HEIGHT = 32;
	private int SPRITE_WIDTH = 32;
	
	public Player makePlayer(int x, int y, Scenario _scenario, TipoBicho tipo){
		Player newPlayer = null;
		try {
			newPlayer = new Player(x, y, _scenario);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		newPlayer.b = new Body();
		newPlayer.b.x = x;
		newPlayer.b.y = y;
        
        
        newPlayer.b.accelX = 0.4f;
        newPlayer.b.decelX = 0.8f;
        newPlayer.b.maxSpeedX = 6;
        newPlayer.b.maxSpeedY = 25;
        newPlayer.b.jumpStartSpeedY = 15;
		
		newPlayer.v = new VisualBody(32, 32, "data/player/");
        newPlayer.v.tipo = tipo;
		newPlayer.v.collisionRectangle = getCollisionRectangle();
		
		_scenario.player = newPlayer;
        
        try {
        	setPlayerSprites(newPlayer);
			newPlayer.bulletImage = getBulletSprite();
		} catch (SlickException e) {
			e.printStackTrace();
		}
        
		return newPlayer;
	}
	
	
	
	
	
	
	private void setPlayerSprites(Player player) throws SlickException{
    	setPlayerStandingSprite(player, takeBG(new Image(SPRITES_FOLDER + "1.png")));
        
        Image [] runRight = {takeBG(new Image(SPRITES_FOLDER + "1.png")), takeBG(new Image(SPRITES_FOLDER + "2.png")), takeBG(new Image(SPRITES_FOLDER + "3.png")), takeBG(new Image(SPRITES_FOLDER + "4.png")),
        		takeBG(new Image(SPRITES_FOLDER + "5.png")), takeBG(new Image(SPRITES_FOLDER + "6.png")), takeBG(new Image(SPRITES_FOLDER + "7.png")), takeBG(new Image(SPRITES_FOLDER + "8.png"))};
        player.aRunning = makeAnimation(runRight, 100);
        
        Image [] jumpRight = {takeBG(new Image(SPRITES_FOLDER + "j1.png")), takeBG(new Image(SPRITES_FOLDER + "j2.png")), takeBG(new Image(SPRITES_FOLDER + "j3.png")), takeBG(new Image(SPRITES_FOLDER + "j4.png"))};
        player.aJumping = makeAnimation(jumpRight, 100);
        
        Image [] fallRight = {takeBG(new Image(SPRITES_FOLDER + "f1.png")), takeBG(new Image(SPRITES_FOLDER + "f2.png")), takeBG(new Image(SPRITES_FOLDER + "f3.png")), takeBG(new Image(SPRITES_FOLDER + "f4.png"))};
        player.aFalling = makeAnimation(fallRight,	100);
        
    }
    
    private Image getBulletSprite() throws SlickException{
    	Image bulletImage = new Image(1,1);
		Graphics g = bulletImage.getGraphics();
		g.setColor(Color.yellow);
		g.fillRect(0,0,1,1);
		g.flush();//IMPORTANT!!!
		
		return bulletImage;
    }
    
    /**basura a revisar cuando arregle lo de las animaciones mas adelante*/
	private void setPlayerStandingSprite(Player player, Image i){
	    player.sprites = new HashMap<Facing,Image>();
	    player.sprites.put(Facing.RIGHT, i);
	    player.sprites.put(Facing.LEFT , i.getFlippedCopy(true, false));
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
		//NO SE PUEDE CAMBIAR UN COLOR QUE YA ESTÁ A TRANSPARENTE
		//SOLO ME QUEDA HACER UNA NUEVA Y DIBUJAR SIN EL FONDO
    	Image result = new Image(img.getWidth(),img.getHeight());
		Graphics g = result.getGraphics();
		g.setColor(Color.transparent);
		g.fillRect(0,0,img.getWidth(),img.getHeight());
		Color backGround = img.getColor(0, 0);
		for (int y=0 ; y<img.getHeight() ; y++){
         	for (int x=0 ; x<img.getWidth() ; x++){
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
	
}
