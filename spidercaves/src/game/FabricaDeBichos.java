package game;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.Enum.Facing;
import game.Enum.TipoBicho;


public class FabricaDeBichos {
	
	public Bicho makeBicho(int x, int y, Scenario _scenario, TipoBicho tipo){
		Bicho newBicho = new Bicho(x, y, _scenario);
        
		if (tipo == TipoBicho.AÒBebe){
			newBicho.b.accelX = 0.4f;
			newBicho.b.decelX = 0.8f;
			newBicho.b.maxSpeedX = 10;
			newBicho.b.maxSpeedY = 25;
			newBicho.b.jumpStartSpeedY = 10;
			
			newBicho.v = new VisualBody(16, 16, "data/bicho/");
	        newBicho.v.tipo = tipo;
			newBicho.v.collisionRectangle = getCollisionRectangle(TipoBicho.AÒBebe);
			
		}else if (tipo == TipoBicho.AÒAdulta){
			newBicho.b.accelX = 0.4f;
			newBicho.b.decelX = 0.8f;
			newBicho.b.maxSpeedX = 10;
			newBicho.b.maxSpeedY = 25;
			newBicho.b.jumpStartSpeedY = 10;
			
			newBicho.v = new VisualBody(32, 32, "data/bicho/adulta/");
	        newBicho.v.tipo = tipo;
			newBicho.v.collisionRectangle = getCollisionRectangle(TipoBicho.AÒAdulta);
		}
		
		try {
			setSprites(newBicho);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		return newBicho;
	}
	
	
	
	private void setSprites(Bicho bicho) throws SlickException{
	    bicho.v.sprites = new HashMap<Facing,Image>();
	    bicho.v.sprites.put(Facing.RIGHT, takeBG(new Image(bicho.v.SPRITES_FOLDER + "1.png")));
	    bicho.v.sprites.put(Facing.LEFT , takeBG(new Image(bicho.v.SPRITES_FOLDER + "1.png")).getFlippedCopy(true, false));
        
    }
	
	/**setea transparente el background. setea transparentes los puntos iguales al (0,0) de la imagen*/
	private Image takeBG(Image img) throws SlickException{
		//NO SE PUEDE CAMBIAR UN COLOR QUE YA EST¡ A TRANSPARENTE
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
	
	private int [][] getCollisionRectangle(TipoBicho tipo){
		int result[][] = null;
		if (tipo == TipoBicho.AÒBebe){
	    	int points[][] = {
	    	        { 5,  10  }, { 11, 10  }, // Top of head
	    	        { 5,  15 }, { 11, 15 }, // Feet
	    	        { 5,  12 }, { 5,  13 }, // Left arm
	    	        { 11, 12 }, { 11, 13 }  // Right arm
	    	};
	    	result = points;
		}
		if (tipo == TipoBicho.AÒAdulta){
			//cambiar a otros
			int points[][] = {
	    	        { 5,  10  }, { 11, 10  }, // Top of head
	    	        { 5,  15 }, { 11, 15 }, // Feet
	    	        { 5,  12 }, { 5,  13 }, // Left arm
	    	        { 11, 12 }, { 11, 13 }  // Right arm
	    	};
			result = points;
		}
    	return result;
    }
	
	
	
	
	
	
	
	
	
	
	
	
///////////////////////
}
