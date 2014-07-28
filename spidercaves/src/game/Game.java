package game;

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
    private boolean scaleChanged = false;
    private Image screen;
    private int frames;
    private long miliseconds;
    public static final String GAME_NAME = "Spcv";
    private Scenario scenario;
    private Player player;
    private Controller controller;
    
    
    public Game() {
		super(GAME_NAME);
	}
    
    @Override
	public void init(GameContainer container) throws SlickException {
		screen = new Image(WINDOW_WIDTH, WINDOW_HEIGTH);
		controller = new Controller();
		scenario = new Scenario("map1");
		player = new Player(87, 5, scenario);
	}
    
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		Graphics screenG = screen.getGraphics();
		if (scaleChanged == true){
			scaleChanged = false;
	        ((AppGameContainer) container).setDisplayMode(WINDOW_WIDTH*SCALE, WINDOW_HEIGTH*SCALE, false);
		}
		scenario.render(container, g, screenG);
		player.render(screenG);
		screenG.flush();
		screen.draw(0,0, SCALE);
		g.drawString("state: " + player.state,50,50);
		g.drawString("speedX: " + player.body.speedX,50,62);
		g.drawString("speedY: " + player.body.speedY,50,74);
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		keyBoardSettings(container);
		controller.update(container, delta, SCALE, scenario, player);
	}
    
	public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGTH, false);
        app.setTargetFrameRate(30);
        app.start();
	}
	
	private void keyBoardSettings(GameContainer container){
		if ( container.getInput().isKeyDown(Input.KEY_1) ) {
			SCALE = 1;
			scaleChanged = true;
        }
		if ( container.getInput().isKeyDown(Input.KEY_2) ) {
			SCALE = 2;
			scaleChanged = true;
        }
		if ( container.getInput().isKeyDown(Input.KEY_SPACE) && miliseconds +40 < System.currentTimeMillis() ) {
			if (frames == 30)
				frames = 5;
			else
				frames = 30;
			miliseconds = System.currentTimeMillis();
			((AppGameContainer) container).setTargetFrameRate(frames);
        }
	}
	

}