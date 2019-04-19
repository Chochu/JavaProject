package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JOptionPane;

import org.omg.PortableServer.POAManagerPackage.State;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.EnumScript;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG Clay Miner", description = "Mine Clay at Varrock", version = 1.0)


public class DGClayMiner extends Script implements Painting {

	//Variable---------------------------------------------------
	char ctrl = KeyEvent.VK_CONTROL;
	private STATE SS = getState();
	private final int sCLAY = 1761;
	final RSTile[] WALKTOBANK = {  //this is to declare a path way
			new RSTile(2631, 3139, 0), new RSTile(2631, 3134, 0), new RSTile(2631, 3126, 0), new RSTile(2629, 3121, 0), new RSTile(2629, 3117, 0), new RSTile(2625, 3110, 0), new RSTile(2620, 3108, 0), new RSTile(2612,3100 ), new RSTile(2611, 3094, 0) };
	final RSTile[] WALKTOMINE ={
			new RSTile(2612, 3093, 0), new RSTile(2607, 3095, 0), new RSTile(2615, 3102, 0), new RSTile(2616, 3109, 0), new RSTile(2619, 3115, 0), new RSTile(2624, 3129, 0), new RSTile(2631, 3137) };
	private int [] ORE = {2109};
	private int [] BRACLET = {11074};
	private int PICK = Integer.parseInt(JOptionPane.showInputDialog("Enter PickAxe ID: "));
	private RSTile TilesOne = new RSTile(3180,3371);
	private final long startTime = System.currentTimeMillis(); //timer
	String sa; //Some String, I use it to store my player's action
	//any other variable that you want to use
	private int START_XP = Skills.getXP("Mining");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Mining");
	private int lvl = Skills.getCurrentLevel("Mining");
	//-------------------------------------------------------------

	//Main Loop----------------------------------------------------
	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE); //set color
		g.drawString(sa , 10, 70); //Explain of using the sa variable from before
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 10, 90);
		g.drawString("Exp Earn: " + ((START_XP) - (CURRENT_XP)), 10, 110);
		g.drawString("Clay Mine: " + (((START_XP) - (CURRENT_XP))/5), 10, 130);
		g.drawString("Clay Mine Per Hour: " + perHour((((START_XP) - (CURRENT_XP))/5)), 10, 150);

		//Any other paint stuff goes here, such as Code generated using Enfilade's Easel
	}

	@Override
	public void run() {
		println("The Bot has run");
		while(true){
			START_XP = Skills.getXP("Mining");
			XP_TILL = Skills.getPercentToNextLevel("Mining");
			lvl = Skills.getCurrentLevel("Mining");
			Mouse.setSpeed(160); //this is mouse speed, not necessay 
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable
			Walking.control_click = true;
			switch(SS){
			case CHECK:
				sa = "Checking";
				break;
			case MINING:
				sa = "Mining Clay";
				MINING();
				break;
			case WALKTOBANK:
				sa = "Walking to Bank";
				WALKTOBANK();
				break;
			case BANKING:
				sa = "Banking";
				Banking();
				break;
			case WALKTOMINE:	
				sa = "Walking to Varrock Mine";
				WalkingtoMine();
				break;
			default:
				break;

			}
		}
	}

	//-------------------------------------------------------------


	//STATE--------------------------------------------------------
	private STATE getState(){
		if(Player.getRSPlayer().isInCombat())
			return STATE.WALKTOBANK;
		if(ATMINELOCATION()){
			if(Inventory.isFull())
				return STATE.WALKTOBANK;
			else
				return STATE.MINING;
		}
		if(ATBANK() || !Inventory.isFull()){
			if(Inventory.isFull()){
				return STATE.BANKING;
			}
			else
				return STATE.WALKTOMINE;
		}

		//So on and so forth, you know the general rule
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK, WALKTOBANK, MINING, WALKTOMINE, BANKING,
	}	
	//-------------------------------------------------------------

	//Check Method---------------------------------------------------
	public boolean ATMINELOCATION(){
		if(Objects.find(5, 2091).length > 0){
			return true;
		}
		return false;
	}
	public boolean ATBANK(){
		if(Objects.find(10, 23964).length > 0){
			return true;
		}
		return false;
	}
	public int perHour(int gained) {
		return ((int) ((gained) * 3600000D / (System.currentTimeMillis() - startTime)));
	}
	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	private boolean WalkingtoMine(){
		while(!ATMINELOCATION()){
			Walking.walkPath(WALKTOMINE);
			Walking.walking_timeout = 1;
		}
		return false;
	}
	private boolean Banking(){
		SleepingTill();
		while(!Banking.isBankScreenOpen())
			Banking.openBankBooth();
		while(Banking.isPinScreenOpen())
			Banking.inPin();
		Banking.depositAllExcept(PICK);
		ShouldB();
		while(Banking.isBankScreenOpen())
			Banking.close();
		sleep(1000);
		WearRing();
		return true;
	}
	private boolean ShouldB(){
		if(!getEquipment()){
			Banking.withdraw(2, 11074);
			return true;
		}
		else {
			Banking.withdraw(1, 11074);
			return true;
		}

	}
	void WearRing(){
		RSItem [] ring = Inventory.find(11074);
		sa = "Wearing Bra";
		if(ring.length > 0)
			if(ring[0] != null)
				ring[0].click("Wear");
	}
	private boolean getEquipment() {
		RSInterfaceChild ringSlot = Interfaces.get(387, 28);
		if (ringSlot != null){
			RSItem[] equipped = ringSlot.getItems();
			for(int i = 0; i < equipped.length; i++){
				if (equipped[i].getID() == 11074){
					return true;
				}

			}
		}
		return false;
	}

	private void WALKTOBANK() { //Walk path [this is a example
		while(!ATBANK()){
			Walking.walkPath(WALKTOBANK);
			Walking.walking_timeout = 1;
		}
	}

	private boolean MINING(){
		if(!getEquipment())
			if(Inventory.find(11074).length > 0)
				WearRing();
		RSObject [] rock = Objects.find(5, ORE);
		if(rock != null){
			try{
				rock[0].click("Mine");
				SleepingMine(rock[0].getPosition());
				if(Inventory.isFull())
					return true;
				return true;
			}
			catch(ArrayIndexOutOfBoundsException e){
				return false;
			}
		}
		return true;

	}

	//ETC...
	//-------------------------------------------------------------

	//Other Method-------------------------------------------------
	//Other method such as...
	boolean SleepingMine(RSTile x){
		SleepingTill();
		sleep(500);
		while ((Player.getAnimation() != -1)){
			RSObject [] rock = Objects.getAt(x);
			sa = "Waiting for Ore";
			if(rock[0].getID() != ORE[0]){
				Walking.walkTo(new RSTile(2630,3139));
			}
			sleep(50);
		}
		return false;
	}
	private void sleeps() {
		sleep(500);
		while(Player.getAnimation() != -1){
			sleep(50);
		}
	}
	void SleepingTill(){
		sleep(500);
		while(Player.isMoving())
			sleep(50);
	}
	private void TurnCamera(RSObject EX){
		Camera.setCameraAngle(0);
		RSObject target = EX;
		Camera.turnToTile(target.getPosition());
		EX.click("Open");
		sleep(50);
	}
	//-------------------------------------------------------------


} // END OF SCRIPT