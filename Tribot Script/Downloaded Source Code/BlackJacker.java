package scripts;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;

public class BlackJacker extends Script {

	private RSTile[] thieve_area = { new RSTile(3363, 3000, 0),
			new RSTile(3364, 3000, 0), new RSTile(3363, 3002, 0),
			new RSTile(3364, 3001, 0), new RSTile(3364, 3002, 0), };
	private RSTile before_ladder_up = new RSTile(3364, 3002, 0);
	private RSTile after_ladder_up = new RSTile(3364, 3002, 1);
	private int bandit41 = 1882;
	private int bandit57 = 1879;
	private int knock_anim = 401;
	private int stun_anim = 403;
	private int pp_anim = 827;
	

	@Override
	public void run() {
		Mouse.setSpeed(General.random(200, 220));
		Inventory.dropAllExcept(keep);
		
	}
	
	private boolean knockOut() {
		
		
		
		return false;
		
		
	}

}
