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


@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG Nature Chest", description = "Steal Chest", version = 1.0)


public class DGChestStealer extends Script implements Painting {

	//Variable---------------------------------------------------

	private STATE SS = getState();
	private final int FOODID = Integer.parseInt(JOptionPane.showInputDialog("Enter Food ID: ")); // a single ID
	private final int EATAT = Integer.parseInt(JOptionPane.showInputDialog("Eat at: ")); // a single ID
	private final int STAIRU = 1740, STAIRD = 1738;
	private final int CHESTO = 2567, CHESTC = 2571;
	private int lvl = Skills.getCurrentLevel("Hitpoints");
	int Chicken = 2463;
	final RSTile[] WALKTOBANK = {  //this is to declare a path way
			new RSTile(2671, 3303, 0), new RSTile(2664, 3295, 0), new RSTile(2653, 3285, 0) };
	final RSTile [] WALKTOCHEST = {
			new RSTile(2662, 3287, 0), new RSTile(2670, 3297, 0), new RSTile(2671, 3305, 0) };
	private final long startTime = System.currentTimeMillis(); //timer
	String sa; //Some String, I use it to store my player's action
	//any other variable that you want to use
	private int START_XP = Skills.getXP("Thieving");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Thieving");
	//-------------------------------------------------------------

	//Main Loop----------------------------------------------------
	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE); //set color
		g.drawString(sa , 10, 70); //Explain of using the sa variable from before
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)) , 10, 90);
		g.drawString("Amount Loot: " +  ((START_XP) - (CURRENT_XP) )/25 , 10, 110);
		g.drawString("Loot Per Hour: " +  perHour(((START_XP) - (CURRENT_XP) )/25) , 10, 130);
		//Any other paint stuff goes here, such as Code generated using Enfilade's Easel
	}

	@Override
	public void run() {
		println("The Bot has run");
		while(true){
			START_XP = Skills.getXP("Thieving");
			XP_TILL = Skills.getPercentToNextLevel("Thieving");
			GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
			lvl = Skills.getCurrentLevel("Hitpoints");
			Mouse.setSpeed(160); //this is mouse speed, not necessay 
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable
			switch(SS){
			case Banking:
				sa = "Banking";
				Banking();
				break;
			case CHECK:
				SS = getState();
				break;
			case GoDownStair:
				sa = "Going Down Stair";
				GoDown(0);
				break;
			case GoDownStairB:
				sa = "In Combat going down Stair";
				GoDown(1);
				break;
			case LOOTCHEST:
				sa = "looting Chest";
				Loot();
				break;
			case WalkingToBank:
				sa = "Walking to Bank";
				WalkingTo(0);
				break;
			case GoUP:
				sa ="Walking to Up-Stair";
				GoUpStair();
				break;
			case EAT:
				sa = "Eating";
				Eat();
				break;
			default:
				break;

			}

		}
	}

	//-------------------------------------------------------------


	//STATE--------------------------------------------------------
	private STATE getState(){
		if(Eatfood()){
			return STATE.EAT;
		}
		else if(!HaveFood() && !ATBANK())
			return STATE.GoDownStair;
		else if(AtUpStair()){
			if(BADCHEST())
				return STATE.GoDownStairB;
			else if(HaveFood())
				return STATE.LOOTCHEST;
		}
		else if(ATBANK())
			return STATE.Banking;
		else if(AtDownStair() && !ATBANK()){
			if(!HaveFood())
				return STATE.WalkingToBank;
			else
				return STATE.GoUP;
		}

		//So on and so forth, you know the general rule
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK, LOOTCHEST, GoDownStair, GoDownStairB, Banking, WalkingToBank, GoUP, EAT,
	}	
	//-------------------------------------------------------------

	//Check Method---------------------------------------------------
	public int perHour(int gained) {
		return ((int) ((gained) * 3600000D / (System.currentTimeMillis() - startTime)));
	}
	boolean Eatfood(){
		if(lvl <= EATAT){
			return true;
		}
		return false;
	}
	private boolean AtUpStair(){
		if(Objects.find(10, 1740).length > 0)
			return true;
		return false;
	}
	private boolean AtDownStair(){
		if(Objects.find(10, 1738).length > 0)
			return true;
		return false;
	}
	private boolean HaveFood(){
		if(Inventory.find(FOODID).length > 0)
			return true;
		return false;
	}
	public boolean ATBANK(){
		if(Objects.find(8, 23964).length > 0)
			return true;
		return false;
	}
	public boolean BADCHEST(){
		if(Player.getRSPlayer().isInCombat() || Chi())
			return true;
		return false;
	}
	public boolean Chi(){
		RSNPC [] ch = NPCs.getAll();
		for(int x = 0; x < ch.length ; x ++)
			if(ch[x].getID() == Chicken)
				return true;
		return false;
	}

	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	boolean Eat(){
		RSItem[] food = Inventory.find(FOODID);
		if(food != null){
			food[0].click("Eat");
		}
		return true;

	}
	boolean Banking(){
		do{
			while(!Banking.isBankScreenOpen())
				Banking.openBankBooth();
			while(Banking.isPinScreenOpen())
				Banking.inPin();
			Banking.depositAll();
			sleep(500);
			Banking.withdraw(20, FOODID);
			while(Banking.isBankScreenOpen())
				Banking.close();
		}while(!HaveFood());
		WalkingTo(1);
		while(Player.getPosition().distanceTo(new RSTile(2674,3303)) != 0){
			OpenDoor(2);
		}
		while(!AtUpStair())
			GoUpStair();
		return true;
	}

	boolean WalkingTo(int x){
		if(x == 1){
			while(Player.getPosition().distanceTo(new RSTile(2674,3304)) > 2){
				sa = "Walking to House";
				Walking.walkPath(WALKTOCHEST);
				Walking.walking_timeout = 1L;
			}
			return true;
		}
		if(x == 2){
			while(!ATBANK()){
				sa = "Going to Bank";
				Walking.walkPath(WALKTOBANK);
				Walking.walking_timeout = 1L;
			}
			return true;
		}
		return true;
	}
	boolean GoDown(int x){
		if(x == 0){
			sa = "Going DownStair to Bank";
			GoDownStair();
			while(Player.getPosition().distanceTo(new RSTile(2674,3304)) > 0){
				sa = "Opening the door";
				OpenDoor(1);
			}
			while(!ATBANK()){
				sa = "Walking to Bank";
				WalkingTo(2);
				sa = "Done";
			}
			return true;
		}
		if(x == 1){
			sa = "Going DownStair Cause of Combat";
			GoDownStair();
			sleep(7000);
			GoUpStair();
			return true;
		}
		return true;
	}
	boolean OpenDoor(int x){
		RSObject [] door = Objects.getAt(new RSTile(2674,3304));
		try{
			if(door != null){
				if(!door[0].isOnScreen())
					TurnCamera(door[0]);
				if(x == 1){
					door[0].click("Open");
					return true;
				}
				else if(x == 2){
					door[0].click("Pick-lock");
					return true;
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return true;
		}
		return true;
	}
	boolean GoDownStair(){
		while(!AtDownStair()){
			RSObject[] StairU = Objects.find(10, STAIRU);
			try{
				if(StairU != null){
					if(!StairU[0].isOnScreen())
						TurnCamera(StairU[0]);
					StairU[0].click("Climb-down");
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		return true;
	}
	boolean GoUpStair(){
		while(AtDownStair()){
			RSObject[] StairU = Objects.find(10, STAIRD);
			try{
				if(StairU != null){
					if(!StairU[0].isOnScreen())
						TurnCamera(StairU[0]);
					StairU[0].click("Climb-Up");
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				break;
			}
		}
		return true;
	}
	boolean Loot(){
		RSObject[] Chest = Objects.find(10, CHESTO);
		try{
			if(Chest != null){
				if(!Chest[0].isOnScreen())
					TurnCamera(Chest[0]);
				Chest[0].click("Search for traps");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return true;
		}
		return true;
	}
	//ETC...
	//-------------------------------------------------------------

	//Other Method-------------------------------------------------
	//Other method such as...
	private void sleeps() {
		sleep(500);
		while(Player.getAnimation() != -1){
			sleep(50);
		}
	}
	private void TurnCamera(RSObject EX){
		Camera.setCameraAngle(100);
		RSObject target = EX;
		Camera.turnToTile(target.getPosition());
		EX.click("Open");
		sleep(50);
	}
	//-------------------------------------------------------------


} // END OF SCRIPT