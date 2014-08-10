package game;

import game.Enum.TipoBicho;

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
    private boolean debugMode = false;
    private Image screen;
    private int frames;
    private long miliseconds;
    public static final String GAME_NAME = "Spcv";
    private Scenario scenario;
    private Player player;
    private Controller controller;
    private FabricaDePlayers fabricaPlayers = new FabricaDePlayers();
    
    public Game() {
		super(GAME_NAME);
	}
    
    @Override
	public void init(GameContainer container) throws SlickException {
		screen = new Image(WINDOW_WIDTH, WINDOW_HEIGTH);
		controller = new Controller();
		scenario = new Scenario("map1");
		player = fabricaPlayers.makePlayer(87, 5, scenario, TipoBicho.Player);
	}
    
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		Graphics screenG = screen.getGraphics();
		if (scaleChanged == true){
			scaleChanged = false;
	        ((AppGameContainer) container).setDisplayMode(WINDOW_WIDTH*SCALE, WINDOW_HEIGTH*SCALE, false);
		}
		scenario.render(container, g, screenG, debugMode);
		player.render(screenG);
		screenG.flush();
		screen.draw(0,0, SCALE);
		
		//debug inScreenInfo
		g.drawString("state: " + player.state,50,50);
		g.drawString("speedX: " + player.b.speedX,50,62);
		g.drawString("speedY: " + player.b.speedY,50,74);
		
		g.drawString("(" + (container.getInput().getMouseX()/SCALE) + ", " + (container.getInput().getMouseY()/SCALE) + ")",1*SCALE,466*SCALE);
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		//no dar bola
		keyBoardSettings(container);
		//no dar bola
		
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
		
		if ( container.getInput().isKeyDown(Input.KEY_3) && miliseconds +200 < System.currentTimeMillis() ) {
			debugMode = !debugMode;
			miliseconds = System.currentTimeMillis();
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