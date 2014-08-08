package game;

import game.Enum.Facing;
import game.Enum.State;
import game.Enum.TipoBicho;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class VisualBody {
	
	public int SPRITE_HEIGHT;
	public int SPRITE_WIDTH;
	public String SPRITES_FOLDER;
	
    public HashMap<Facing, Image> sprites;
    
    public TipoBicho tipo;
    public Facing facing = Facing.RIGHT;
    State state = State.FallingR;
    int collisionRectangle[][];
    
	
	// = "data/bicho/"
	public VisualBody(int height, int width, String folder){
		SPRITE_HEIGHT = height;
		SPRITE_WIDTH = width;
		SPRITES_FOLDER = folder;
		
		
		try {
			getSprites();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private void getSprites() throws SlickException{
	    sprites = new HashMap<Facing,Image>();
	    sprites.put(Facing.RIGHT, takeBG(new Image(SPRITES_FOLDER + "1.png")));
	    sprites.put(Facing.LEFT , takeBG(new Image(SPRITES_FOLDER + "1.png")).getFlippedCopy(true, false));
        
    }
	
	/**setea transparente el background. setea transparentes los puntos iguales al (0,0) de la imagen*/
	private Image takeBG(Image img) throws SlickException{
		//NO SE PUEDE CAMBIAR UN COLOR QUE YA ESTA A TRANSPARENTE
		//SOLO ME QUEDA HACER UNA NUEVA Y DIBUJAR SIN EL FONDO
    	Image result = new Image(SPRITE_WIDTH,SPRITE_HEIGHT);
		Graphics g = result.getGraphics();
		g.setColor(Color.transparent);
		g.fillRect(0,0,SPRITE_WIDTH,SPRITE_HEIGHT);
		Color backGround = img.getColor(0, 0);
		for (int y=0 ; y<SPRITE_HEIGHT ; y++){
         	for (int x=0 ; x<SPRITE_WIDTH ; x++){
         		if ( img.getColor(x, y).r != backGround.r || img.getColor(x, y).g != backGround.g || img.getColor(x, y).b != backGround.b ){
         			g.setColor( img.getColor(x, y) );
         			g.fillRect(x, y, 1, 1);
         		}
            }
        }
		g.flush();//IMPORTANT!!!
    	return result;
    }
	
	
	//////////////////
}
