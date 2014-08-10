package game;


public class Body {
	public float x, y, speedX, speedY, moveXrequest, moveYrequest;
    float accelX, decelX, maxSpeedX, maxSpeedY, jumpStartSpeedY;
    public boolean jumping, standing, falling;
    
    
    public Body(){
    	x = 0; y = 0; speedX = 0; speedY = 0;
    	accelX = 0; decelX = 0; maxSpeedX = 0; maxSpeedY = 0; jumpStartSpeedY = 0;
    }
    
    public float vectorLenght(){
    	return (float) Math.sqrt(speedX * speedX + speedY * speedY);
    }
}
