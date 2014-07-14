package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

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
	public HashMap<Integer, HashMap<Integer, Integer>> collisionPoints;
	
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
		Image tilesFile = new Image("data/maps/" + levelName + ".png");
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
	
	private HashMap<Integer, HashMap<Integer, Integer>> getCollisionPoints(){
		/**
	    *   uno para cada tipo de tile? no jodas, RE-IMPLEMENTAR
	    *   tiles por tipo, clases
	    **/
		HashMap<Integer, HashMap<Integer, Integer>> result = new HashMap<Integer, HashMap<Integer, Integer>>();;
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
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, Integer>());
	                        result.get(x+(c*TILESIZE)).put(y+(l*TILESIZE), 1);
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
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, Integer>());
	                        result.get(x+(c*TILESIZE)).put(y+(l*TILESIZE), 1);
	                    }
	                }
	            }
	            //tiles de pared delante
	            if ( tilesMap.elementAt(l)[c].matches("08"))
	            {
	                for (int x=8; x < TILESIZE; x++)
	                {
	                    for (int y=0; y < 8; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, Integer>());
	                        result.get(x+(c*TILESIZE)).put(y+(l*TILESIZE), 1);
	                    }
	                }
	            }
	            //tiles esquinero derecha abajo
	            if ( tilesMap.elementAt(l)[c].matches("03"))
	            {
	                for (int x=8; x < TILESIZE; x++)
	                {
	                    for (int y=11; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, Integer>());
	                        result.get(x+(c*TILESIZE)).put(y+(l*TILESIZE), 1);
	                    }
	                }

	            }
	            //tiles esquina caida derecha abajo
	            if ( tilesMap.elementAt(l)[c].matches("07"))
	            {
	                for (int x=0; x < 8; x++)
	                {
	                    for (int y=11; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, Integer>());
	                        result.get(x+(c*TILESIZE)).put(y+(l*TILESIZE), 1);
	                    }
	                }

	            }
	            //tiles de esquina caida izquierda abajo
	            if ( tilesMap.elementAt(l)[c].matches("05") || tilesMap.elementAt(l)[c].matches("06"))
	            {
	                for (int x=8; x < TILESIZE; x++)
	                {
	                    for (int y=11; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, Integer>());
	                        result.get(x+(c*TILESIZE)).put(y+(l*TILESIZE), 1);
	                    }
	                }

	            }
	            //tiles de pared derecha - cuack, leer Solidos
	            //tiles de pared izquierda
	            if ( tilesMap.elementAt(l)[c].matches("04"))
	            {
	                for (int x=8; x < TILESIZE; x++)
	                {
	                    for (int y=0; y < TILESIZE; y++)
	                    {
	                    	if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, Integer>());
	                        result.get(x+(c*TILESIZE)).put(y+(l*TILESIZE), 1);
	                    }
	                }

	            }
	        }
	    }
	    return result;
	}

	public void render(){
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
		//tiene que llegar integer
		String realX = String.valueOf(Math.ceil(x));
		String realY = String.valueOf(Math.ceil(y));
		realX = realX.substring(0, realX.indexOf("."));
		realY = realY.substring(0, realY.indexOf("."));
	    if (collisionPoints.containsKey(Integer.parseInt(realX)) == true && collisionPoints.get(Integer.parseInt(realX)).get(Integer.parseInt(realY)) != null)
	        return true;//esto deberia ser re poco costoso por ser un hashmap, no?
	    return false;
	}

	public float nextCollisionPointDownwards(float x, float y)
	{
		String stringX = String.valueOf(Math.round(x));
		String stringYfloored = String.valueOf(Math.floor(y));
		stringYfloored = stringYfloored.substring(0, stringYfloored.indexOf("."));
		int intX = Integer.parseInt(stringX);//rounded
		int intYfloored = Integer.parseInt(stringYfloored);
		//ESTO ES PORQUE LOS PUNTOS DE COLISION EN EL HASHMAP SON INTEGER (PIXELS)
		//SI HAY COLISION EN EL PUNTO 7, EN EL 7.5 TAMBIEN.....no?
	    for (int i = intYfloored; i < SCREENHEIGHT; i++) {
	        if ( collides(intX, i) )
	            return i;
	    }
	    return SCREENHEIGHT;
	}
	public float nextCollisionPointUpwards(float x, float y)
	{
		String stringX = String.valueOf(Math.round(x));
		String stringYceiled = String.valueOf(Math.ceil(y));
		stringYceiled = stringYceiled.substring(0, stringYceiled.indexOf("."));
		int intX = Integer.parseInt(stringX);//rounded
		int intYceiled = Integer.parseInt(stringYceiled);
		//ESTO ES PORQUE LOS PUNTOS DE COLISION EN EL HASHMAP SON INTEGER (PIXELS)
		//SI HAY COLISION EN EL PUNTO 7, EN EL 7.5 TAMBIEN.....no?
	    for (int i = intYceiled; i > 0; i--) {
	        if ( collides(intX, i) )
	            return i;
	    }
	    return 0;
	}
	public float nextCollisionPointLeftwards(float x, float y)
	{
		String stringXceiled = String.valueOf(Math.ceil(x));
		String stringY = String.valueOf(Math.round(y));
		stringXceiled = stringXceiled.substring(0, stringXceiled.indexOf("."));
		int intXceiled = Integer.parseInt(stringXceiled);//rounded
		int intY = Integer.parseInt(stringY);
		//ESTO ES PORQUE LOS PUNTOS DE COLISION EN EL HASHMAP SON INTEGER (PIXELS)
		//SI HAY COLISION EN EL PUNTO 7, EN EL 7.5 TAMBIEN.....no?
	    for (int i = intXceiled; i > 0; i--) {
	        if ( collides(i, intY) )
	            return i;
	    }
	    return 0;
	}
	public float nextCollisionPointRightwards(float x, float y)
	{
		String stringXfloored = String.valueOf(Math.floor(x));
		String stringY = String.valueOf(Math.round(y));
		stringXfloored = stringXfloored.substring(0, stringXfloored.indexOf("."));
		int intXfloored = Integer.parseInt(stringXfloored);//rounded
		int intY = Integer.parseInt(stringY);
		//ESTO ES PORQUE LOS PUNTOS DE COLISION EN EL HASHMAP SON INTEGER (PIXELS)
		//SI HAY COLISION EN EL PUNTO 7, EN EL 7.5 TAMBIEN.....no?
	    for (int i = intXfloored; i < SCREENWIDTH; i++) {
	        if ( collides(i, intY) )
	            return i;
	    }
	    return SCREENWIDTH;
	}
	
	private int ceil(float v){
		return Integer.parseInt(String.valueOf( Math.ceil(v) ).substring(0, String.valueOf( Math.ceil(v) ).indexOf(".")));
	}
	private int floor(float v){
		return Integer.parseInt(String.valueOf( Math.floor(v) ).substring(0, String.valueOf( Math.floor(v) ).indexOf(".")));
	}
//EOF
}
