package game;

import game.Enum.TipoPixel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Scenario {
	public static final int SCREENWIDTH = 320;
	public static final int SCREENHEIGHT = 480;
	public static final int TILESIZE = 16;
	public String levelName;
	public float gravity;
	public Vector<String[]> tilesMap;
	public Vector<Image> tiles;
	public HashMap<Integer, HashMap<Integer, TipoPixel>> collisionPoints;
	
	public Scenario(String _levelName) throws FileNotFoundException, SlickException{
		levelName = _levelName;
		gravity = .3f;
		tilesMap = getTilesMap();
		tiles = getTiles();
		collisionPoints = getCollisionPoints();
	}
	
	private Vector<String[]> getTilesMap() throws FileNotFoundException{
		Vector<String[]> result = new Vector<String[]>();
		Scanner sc = new Scanner(new File("data/maps/" + levelName + ".scm"));
		String line;
		while (sc.hasNext()){
			line = sc.nextLine();
			if (line.contains("[MAP]")){
				line = sc.nextLine();
				while (!line.contains("[/MAP]")){
					result.add( line.split(" ") );
					line = sc.nextLine();
				}
			}
		}
		sc.close();
		return result;
	}
	
	private Vector<Image> getTiles() throws SlickException{
		Vector<Image> result = new Vector<Image>();
		Image tilesFile = new Image("data/maps/" + levelName + ".png", false, Image.FILTER_NEAREST);
		//Graphics fileG = tilesFile.getGraphics();
		int fileLines = tilesFile.getHeight()/TILESIZE;
        int fileColumns = tilesFile.getWidth()/TILESIZE;
        for (int line = 1 ; line<=fileLines ; line++)
        {
            for (int column = 1 ; column<=fileColumns ; column++)
            {
            	result.add( tilesFile.getSubImage(TILESIZE*(column-1), TILESIZE*(line-1), TILESIZE, TILESIZE) );
            	//Image tile =  new Image(TILESIZE,TILESIZE);
            	//Graphics tileG = tile.getGraphics();
                /*for (int y=1 ; y<=TILESIZE ; y++)
                {											esta parte es por pixel pero slick tiene para sacarle un cacho a la imagen
                	
                 	for (int x=1 ; x<=TILESIZE ; x++)
                    {
                    	//tileG.setColor( fileG.getPixel(x-1+(TILESIZE*(column-1)), y-1+(TILESIZE*(line-1))) );
                    	int realX = x-1+(TILESIZE*(column-1));
                    	int realY = y-1+(TILESIZE*(line-1));
                    	Color c = fileG.getPixel(realX, realY);
                    	tileG.setColor( c );
                    	tileG.fillRect(x-1, y-1, 1, 1);
                    	
                        if (x>=TILESIZE && y>=TILESIZE){
                        	tileG.flush();//IMPORTANT!!!
                            result.add(tile); //faltaba un flush de "tilesFile", si haces getGraphics se caga y el flush lo arregla
                        }
                    }
                }*/
            }
        }
        
		return result;
	}
	
	private HashMap<Integer, HashMap<Integer, TipoPixel>> getCollisionPoints(){
		/**
	    *   uno para cada tipo de tile? no jodas, RE-IMPLEMENTAR
	    *   tiles por tipo, clases
	    **/
		HashMap<Integer, HashMap<Integer, TipoPixel>> result = new HashMap<Integer, HashMap<Integer, TipoPixel>>();;
	    for (int l=0; l < tilesMap.size(); l++)
	    {
	        for (int c=0; c < tilesMap.elementAt(l).length; c++)
	        {
	            //tiles completamente Solidos
	            //Y PAREDES DERECHA XQ EL PERSONAJE TENDRIA QUE QUEDAR DETRAS DE LA PARED Y SE DIBUJA DSPS...
	            if ( tilesMap.elementAt(l)[c].matches("01"))
	            {
	                for (int x=0; x < 8; x++)
	                {
	                    for (int y=0; y < TILESIZE; y++)
	                    {
	                        if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, TipoPixel>());
	                        result.get( x+(c*TILESIZE) ).put( y+(l*TILESIZE), getTipoPx(x, y, tilesMap.elementAt(l)[c]) );
	                    }
	                }

	            }
	            //tiles de piso 12 pixels debajo del top
	            if ( tilesMap.elementAt(l)[c].matches("02"))
	            {
	                for (int x=0; x < TILESIZE; x++)
	                {
	                    for (int y=11; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, TipoPixel>());
	                        result.get( x+(c*TILESIZE) ).put( y+(l*TILESIZE), getTipoPx(x, y, tilesMap.elementAt(l)[c]) );
	                    }
	                }
	            }
	            //tiles esquinero derecha abajo
	            if ( tilesMap.elementAt(l)[c].matches("03"))
	            {
	                for (int x=0; x < TILESIZE; x++)
	                {
	                    for (int y=11; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, TipoPixel>());
	                    	result.get( x+(c*TILESIZE) ).put( y+(l*TILESIZE), getTipoPx(x, y, tilesMap.elementAt(l)[c]) );
	                    }
	                }
	                //son 2 porque la forma es una 'L' espejada
	                for (int x=8; x < TILESIZE; x++)
	                {
	                    for (int y=0; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, TipoPixel>());
	                    	result.get( x+(c*TILESIZE) ).put( y+(l*TILESIZE), getTipoPx(x, y, tilesMap.elementAt(l)[c]) );
	                    }
	                }

	            }
	            //tiles de pared izquierda
	            if ( tilesMap.elementAt(l)[c].matches("04"))
	            {
	                for (int x=8; x < TILESIZE; x++)
	                {
	                    for (int y=0; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, TipoPixel>());
	                    	result.get( x+(c*TILESIZE) ).put( y+(l*TILESIZE), getTipoPx(x, y, tilesMap.elementAt(l)[c]) );
	                    }
	                }

	            }
	            //tiles de esquina caida izquierda abajo
	            if ( tilesMap.elementAt(l)[c].matches("05"))
	            {
	                for (int x=8; x < TILESIZE; x++)
	                {
	                    for (int y=11; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, TipoPixel>());
	                    	result.get( x+(c*TILESIZE) ).put( y+(l*TILESIZE), getTipoPx(x, y, tilesMap.elementAt(l)[c]) );
	                    }
	                }

	            }
	            //tiles de pared derecha - cuack, leer Solidos
	            
	            //tiles esquina caida derecha abajo
	            if ( tilesMap.elementAt(l)[c].matches("07"))
	            {
	                for (int x=0; x < 8; x++)
	                {
	                    for (int y=11; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, TipoPixel>());
	                    	result.get( x+(c*TILESIZE) ).put( y+(l*TILESIZE), getTipoPx(x, y, tilesMap.elementAt(l)[c]) );
	                    }
	                }

	            }
	            //tiles de pared delante
	            if ( tilesMap.elementAt(l)[c].matches("08"))
	            {
	                for (int x=8; x < TILESIZE; x++)
	                {
	                    for (int y=0; y < 12; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, TipoPixel>());
	                    	result.get( x+(c*TILESIZE) ).put( y+(l*TILESIZE), getTipoPx(x, y, tilesMap.elementAt(l)[c]) );
	                    }
	                }
	            }
	        }
	    }
	    return result;
	}

	public void render(GameContainer container, Graphics g){
	    for (int y=1 ; y<=tilesMap.size() ; y++)
	    {
	        for (int x=1 ; x<=tilesMap.elementAt(y-1).length ; x++)
	        {
	        	Image tile = tiles.elementAt(Integer.parseInt( (tilesMap.elementAt(y-1)[x-1]) ));
	            tile.draw((x-1)*TILESIZE, (y-1)*TILESIZE);
	        }
	    }
	}
	
	public boolean collides(float x, float y)
	{
		//floor porque es por pixels y el pixel 5 es del 5.0 hasta el 5.9
	    if (collisionPoints.containsKey(floor(x)) == true && collisionPoints.get(floor(x)).get(floor(y)) != null)
	        return true;//esto deberia ser re poco costoso por ser un hashmap, no?
	    return false;
	}
	
	private int ceil(float v){
		return Integer.parseInt(String.valueOf( Math.ceil(v) ).substring(0, String.valueOf( Math.ceil(v) ).indexOf(".")));
	}
	private int floor(float v){
		return Integer.parseInt(String.valueOf( Math.floor(v) ).substring(0, String.valueOf( Math.floor(v) ).indexOf(".")));
	}
	
	private TipoPixel getTipoPx(int x, int y, String tile ){
		switch (tile){
		case "01":
			if (x == TILESIZE/2)
				return TipoPixel.DER;
			break;
		case "02":
			if (y == 11)
				return TipoPixel.PISO;
			break;
		case "03":
			if (x == TILESIZE/2 && y == 11)
				return TipoPixel.EsqAbDer;
			else if (x == TILESIZE/2 && y < 11)
				return TipoPixel.IZQ;
			else if (x < TILESIZE/2 && y == 11)
				return TipoPixel.PISO;
			break;
		case "04":
			if (x == TILESIZE/2)
				return TipoPixel.IZQ;
			break;
		case "05":
			if (x == TILESIZE/2 && y == 11)
				return TipoPixel.EsqArrIzq;
			else if (x > TILESIZE/2 && y == 11)
				return TipoPixel.PISO;
			else if (x == TILESIZE/2 && y > 11)
				return TipoPixel.IZQ;
			break;
		case "06":
			//ni idea este no se usa
			break;
		case "07":
			if (x == TILESIZE/2 && y == 11)
				return TipoPixel.EsqArrDer;
			else if (x < TILESIZE/2 && y == 11)
				return TipoPixel.PISO;
			else if (x == TILESIZE/2 && y > 11)
				return TipoPixel.DER;
			break;
		case "08":
			if (x == TILESIZE/2 && y == 11)
				return TipoPixel.EsqAbIzq;
			else if (x > TILESIZE/2 && y == 11)
				return TipoPixel.TECHO;
			else if (x == TILESIZE/2 && y < 11)
				return TipoPixel.IZQ;
			break;
		}
		
		//si no es de los bordes
		return TipoPixel.CENTRO;
	}
//EOF
}
