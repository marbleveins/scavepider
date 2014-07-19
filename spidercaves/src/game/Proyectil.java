package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Proyectil {
	float x, y,speedX, speedY;
	float speed = 4;
	float coef;
	Image image;
	
	public Proyectil(float _x, float _y, float _xT, float _yT, Image _image) throws SlickException {
		x = _x; y = _y;// (25 del click) -->     ( 25 * ( speedV / 25 ) )
		coef = (float) ( speed / Math.sqrt( (_xT-x) * (_xT-x) + (_yT-y) * (_yT-y) ) );
		speedX = ( _xT - x) * coef;
		speedY = ( _yT - y) * coef;
		
		image = _image;
	}
	
	public void update(){
		x += speedX;
		y += speedY;
	}
	public void render(Graphics screenG){
		 screenG.drawImage(image, x, y);
	}
}
