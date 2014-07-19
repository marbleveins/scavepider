package game;

import java.io.FileNotFoundException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame{

	
	public static final int WINDOW_WIDTH  = 320;
    public static final int WINDOW_HEIGTH = 480;
    private int SCALE = 1;
    private boolean scaleChanged;
    private Image screen;
    private int frames;
    private long miliseconds;
    public static final String GAME_NAME = "Spcv";
    private Scenario scenario;
    private Player player;
   
    
    public Game() {
		super(GAME_NAME);
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		Graphics screenG = screen.getGraphics();
		if (scaleChanged == true){
			scaleChanged = false;
	        ((AppGameContainer) container).setDisplayMode(WINDOW_WIDTH*SCALE, WINDOW_HEIGTH*SCALE, false);
		}
		scenario.render(container, g, screenG);
		player.render(screenG);
		screenG.flush();
		screen.draw(0,0, SCALE);
	}
	@Override
	public void init(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		screen = new Image(WINDOW_WIDTH, WINDOW_HEIGTH);
		scaleChanged = false;
		
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
		if ( container.getInput().isKeyDown(Input.KEY_1) ) {
			SCALE = 1;
			scaleChanged = true;
        }
		if ( container.getInput().isKeyDown(Input.KEY_2) ) {
			SCALE = 2;
			scaleChanged = true;
        }
		if ( container.getInput().isKeyDown(Input.KEY_SPACE) && miliseconds +40 < System.currentTimeMillis() ) {
			if (frames == 60)
				frames = 5;
			else
				frames = 60;
			miliseconds = System.currentTimeMillis();
			((AppGameContainer) container).setTargetFrameRate(frames);
        }
		player.update(container, arg1);
		scenario.update(container, arg1);
	}
    
	public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGTH, false);
        app.setTargetFrameRate(60);
        app.start();
   }
	

}