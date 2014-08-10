package game;
public class Enum {
	public enum Facing {
	    LEFT, RIGHT
	}
	
	public enum State {
        StandingR, StandingL,
        RunningR, RunningL,
        JumpingR, JumpingL,
        FallingR, FallingL
    };
    
    public enum TipoPixel {
        CENTRO, TECHO, PISO, IZQ, DER, EsqArrIzq, EsqArrDer, EsqAbIzq, EsqAbDer, NULL
    };
    
    public enum TipoBicho {
    	Player, AÒBebe, AÒAdulta
    };
	
}
