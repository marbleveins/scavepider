package game;

import java.io.FileNotFoundException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame{

	
	public static final int WINDOW_WIDTH  = 320;
    public static final int WINDOW_HEIGTH = 480;
    public static final String GAME_NAME = "Spcv";
    private Scenario scenario;
    private Player player;
   
    
    public Game() {
		super(GAME_NAME);
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public void render(GameContainer container, Graphics arg1) throws SlickException {
		// TODO Auto-generated method stub
		scenario.render();
		player.render();
	}
	@Override
	public void init(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		try {
			scenario = new Scenario("map1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player = new Player(87, 5, scenario);
	}
	@Override
	public void update(GameContainer container, int arg1) throws SlickException {
		// TODO Auto-generated method stub
		player.update(container, arg1);
	}
    
	public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGTH, false);
        app.setTargetFrameRate(60);
        app.start();
   }
	

}