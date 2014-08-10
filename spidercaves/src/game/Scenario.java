package game;

import game.Enum.TipoPixel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Scenario {
	public static final int SCREENWIDTH = 320;
	public static final int SCREENHEIGHT = 480;
	public static final int TILESIZE = 16;
	private String levelName;
	public float gravity;
	private Vector<String[]> tilesMap;
	private Vector<Image> tiles;
	public HashMap<Integer, HashMap<Integer, TipoPixel>> collisionPoints;
	
	public Vector<Proyectil> proyectiles;
	public Vector<Bicho> bichos;
	public Player player;
	
	//////////
	private BichosController bichosController = new BichosController();
	
	
	//////////
	
	
	public Scenario(String _levelName){
		levelName = _levelName;
		gravity = .8f;
		
		try {
			tilesMap = getTilesMap();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			tiles = getTiles();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		collisionPoints = getCollisionPoints();
		setTipoPixel();
		proyectiles = new Vector<Proyectil>();
		bichos = new Vector<Bicho>();
		
	}
	
	public void render(GameContainer container, Graphics g, Graphics screenG, boolean debugMode){
		//mapa
	    for (int y=1 ; y<=tilesMap.size() ; y++)
	    {
	        for (int x=1 ; x<=tilesMap.elementAt(y-1).length ; x++)
	        {
	        	Image tile = tiles.elementAt(Integer.parseInt( (tilesMap.elementAt(y-1)[x-1]) ));
	            //tile.draw((x-1)*TILESIZE, (y-1)*TILESIZE);
	            screenG.drawImage(tile, (x-1)*TILESIZE, (y-1)*TILESIZE);
	        }
	    }
	    
	    //collision Map
	    if (debugMode){
		    for (Map.Entry<Integer, HashMap<Integer, TipoPixel>> outside : collisionPoints.entrySet()){
		    	for (Map.Entry<Integer, TipoPixel> inside : outside.getValue().entrySet()){
		    		if (inside.getValue() == TipoPixel.CENTRO)
		    			screenG.setColor(Color.pink);
		    		else if ( inside.getValue() == TipoPixel.PISO )
		    			screenG.setColor(Color.blue);
		    		else
		    			screenG.setColor(Color.cyan);
		            screenG.fillRect(outside.getKey(), inside.getKey(), 1, 1);
		    	}
		    }
	    }
	    
	    //proyectiles
	    if (proyectiles != null && proyectiles.size() > 0){
		    for (int i=0; i < proyectiles.size(); i++){
		    	if (proyectiles.get(i) == null) continue;
		    	proyectiles.get(i).render(screenG);
		    }
	    }
	    
	    //bichos
	    if (bichos != null && bichos.size() > 0){
	    	for (int i=0; i < bichos.size(); i++){
		    	if (bichos.get(i) == null) continue;
		    	bichos.get(i).render(screenG);
		    }
	    }
	}
	
	public void update(GameContainer container, int delta){
		//proyectiles
	    if (proyectiles != null && proyectiles.size() > 0){
	    	
	    	controlCollisionProyectiles();
	    	
		    for (int i=0; i < proyectiles.size(); i++){
		    	if (proyectiles.get(i) == null) continue;
		    	proyectiles.get(i).update();
		    }
	    }
	    
	    bichosController.update(this, delta);
	    
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
		HashMap<Integer, HashMap<Integer, TipoPixel>> result = new HashMap<Integer, HashMap<Integer, TipoPixel>>();
	    for (int l=0; l < tilesMap.size(); l++)
	    {
	        for (int c=0; c < tilesMap.elementAt(l).length; c++)
	        {
	            //tiles completamente Solidos
	            //Y PAREDES DERECHA XQ EL PERSONAJE TENDRIA QUE QUEDAR DETRAS DE LA PARED Y SE DIBUJA DSPS...
	            if ( tilesMap.elementAt(l)[c].matches("01"))
	            {
	                for (int x=-6; x < TILESIZE-6; x++)
	                {
	                    for (int y=-3; y < TILESIZE-3; y++)
	                    {
	                        if (result.get(x+(c*TILESIZE)) == null)
	                        	result.put(x+(c*TILESIZE), new HashMap<Integer, TipoPixel>());
	                        result.get( x+(c*TILESIZE) ).put( y+(l*TILESIZE), TipoPixel.CENTRO );
	                    }
	                }

	            }/*
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
	            }*/
	        }
	    }
	    return result;
	}
	
	private void setTipoPixel(){
		boolean arr;
		boolean aba;
		boolean izq;
		boolean der;
		for ( Map.Entry<Integer, HashMap<Integer, TipoPixel>> x : collisionPoints.entrySet() ){
			for ( Map.Entry<Integer, TipoPixel> y : x.getValue().entrySet() ){
				arr = false;
				aba = false;
				izq = false;
				der = false;
				
				if ( collisionPoints.get(x.getKey()).containsKey(y.getKey()-1) )
					arr = true;
				if ( collisionPoints.get(x.getKey()).containsKey(y.getKey()+1) )
					aba = true;
				if ( collisionPoints.containsKey(x.getKey()-1) && collisionPoints.get(x.getKey()-1).containsKey(y.getKey()) )
					izq = true;
				if ( collisionPoints.containsKey(x.getKey()+1) && collisionPoints.get(x.getKey()+1).containsKey(y.getKey()) )
					der = true;
				
				if (!arr && !izq && aba && der)
					x.getValue().put(y.getKey(),TipoPixel.EsqArrIzq);
				if (!arr && !der && aba && izq)
					x.getValue().put(y.getKey(),TipoPixel.EsqArrDer);
				if (!aba && !izq && arr && der)
					x.getValue().put(y.getKey(),TipoPixel.EsqAbIzq);
				if (!aba && !der && arr && izq)
					x.getValue().put(y.getKey(),TipoPixel.EsqAbDer);
				if (!arr && aba && izq && der)
					x.getValue().put(y.getKey(),TipoPixel.PISO);
				if (!aba && arr && izq && der)
					x.getValue().put(y.getKey(),TipoPixel.TECHO);
				if (!izq && arr && aba && der)
					x.getValue().put(y.getKey(),TipoPixel.DER);
				if (!der && arr && aba && izq)
					x.getValue().put(y.getKey(),TipoPixel.IZQ);
				
			}
		}
	}

	public boolean collides(float x, float y)
	{
		//int porque es por pixels y el pixel 5 es del 5.0 hasta el 5.9
	    if (collisionPoints.containsKey((int)x) == true && collisionPoints.get((int)x).get((int)y) != null)
	        return true;
	    return false;
	}
	
	private float modulo(float v){
		if (v >= 0)
			return v;
		else
			return -1*v;
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

	private void controlCollisionProyectiles(){
		
		for (int i=0; i < proyectiles.size(); i++){
			if ( proyectiles.isEmpty() ) break;
    		if (proyectiles.get(i) == null) continue;//si se van de la pantalla los borra
    		if (proyectiles.get(i).x > 320 || proyectiles.get(i).x < 0 || proyectiles.get(i).y > 480 || proyectiles.get(i).y < 0){
    			proyectiles.removeElementAt(i);
    			break;
			}
    		
    		float nextMoveX = proyectiles.get(i).speedX;
	        float nextMoveY = proyectiles.get(i).speedY;
    		float projectedMoveX = 0, projectedMoveY = 0;
	        //CORRECCION DE MOVIMIENTO
	        float vectorLength = (float) Math.sqrt(nextMoveX * nextMoveX + nextMoveY * nextMoveY);
	        if (vectorLength == 0) vectorLength = 1;
	        for (float segment=0; segment <= vectorLength; segment++){
	        	projectedMoveX += nextMoveX / vectorLength;
	        	projectedMoveY += nextMoveY / vectorLength;
	        	
	        	if (modulo(projectedMoveX) > modulo(nextMoveX))
	        		projectedMoveX = nextMoveX;
	        	if (modulo(projectedMoveY) > modulo(nextMoveY))
	        		projectedMoveY = nextMoveY;
		        if ( collides(proyectiles.get(i).x + projectedMoveX, proyectiles.get(i).y + projectedMoveY) )
		        {
		        	proyectiles.removeElementAt(i);
		        	break;
		        }
	        }
	        if ( proyectiles.isEmpty() ) break;
		}
	}
	
	//EOF
}
