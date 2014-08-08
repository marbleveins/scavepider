package game;

import game.Enum.TipoBicho;

public class BichosController {
	private FabricaDeBichos fabricaBichos = new FabricaDeBichos();
	
	public void update(Scenario scenario, int delta){
		
		if (scenario.bichos != null && scenario.bichos.size() == 0){
			scenario.bichos.add(
					fabricaBichos.makeBicho(250, 50, scenario, TipoBicho.AñBebe)
				);
		}
		
		 if (scenario.bichos != null && scenario.bichos.size() > 0){
	    	for (int i=0; i < scenario.bichos.size(); i++){
		    	if (scenario.bichos.get(i) == null) continue;
		    	scenario.bichos.get(i).nextMove(scenario.player, delta);
		    }
	    }
	}
	
	public void render(){
		
	}
}
