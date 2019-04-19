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


@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG Air Crafter", description = "Craft Air Run", version = 1.0)


public class DGAirCrafter extends Script implements Painting {

	//Variable---------------------------------------------------

	private STATE SS = getState();
	private final int ESS_ID = Integer.parseInt(JOptionPane.showInputDialog("Enter Rune Ess ID " ,"7936 = Pure, 1436 = Normal"));; // a array of ID
	private final int BANK_ID = 24101 ; // a single ID
	private final int ALTAR_ID = 23101;
	private final int ROCK_ID = 26160 ;
	private final int PORTAL_ID = 25034;
	final int AIR_ID = 556;
	final RSTile[] WALKTOBANK = {  //this is to declare a path way
			new RSTile(2987,3292),
			new RSTile(2991,3297),
			new RSTile(2997,3303),
			new RSTile(3004,3306),
			new RSTile(3005,3314),
			new RSTile(3007,3321),
			new RSTile(3008,3329),
			new RSTile(3008,3337),
			new RSTile(3008,3345),
			new RSTile(3008,3352),
			new RSTile(3008,3359),
			new RSTile(3012,3357)};
	final RSTile[] WALKTOALTAR = {
			new RSTile(3012,3355),
			new RSTile(3008,3345),
			new RSTile(3008,3338),
			new RSTile(3007,3331),
			new RSTile(3005,3322),
			new RSTile(3001,3314),
			new RSTile(2998,3306),
			new RSTile(2997,3303),
			new RSTile(2993,3300),
			new RSTile(2986,3296)};
	private final long startTime = System.currentTimeMillis(); //timer
	String sa; //Some String, I use it to store my player's action
	private int START_XP = Skills.getXP("Runecrafting");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Runecrafting");
	private int lvl = Skills.getCurrentLevel("Runecrafting");
	char ctrl = KeyEvent.VK_CONTROL;

	//-------------------------------------------------------------

	//Main Loop----------------------------------------------------
	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE); //set color
		g.drawString(sa , 10, 70); //Explain of using the sa variable from before
		//Any other paint stuff goes here, such as Code generated using Enfilade's Easel
	}

	@Override
	public void run() {
		println("The Bot has run");
		while(true){
			START_XP = Skills.getXP("Runecrafting");
			XP_TILL = Skills.getPercentToNextLevel("Runecrafting");
			lvl = Skills.getCurrentLevel("Runecrafting");
			Mouse.setSpeed(180);	
			Walking.control_click = true;
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable

			switch(SS){
			case CHECK:
				sa = "Checking What to do";
				break;
			case Bank:
				sa = "Banking";
				Banking();
				break;
			case Craft:
				sa = "Crafting Rune";
				Crafting();
				break;
			case EnterRock:
				sa = "Entering Rock";
				EnterRock();
				break;
			case ExitAltar:
				sa = "Exiting the Altar";
				ExitAltar();
				break;
			case WalktoAltar:
				sa = "Walking to Altar";
				WalktoA();
				break;
			case WalktoBank:
				sa = "Walking to Bank";
				WalktoB();
				break;
			default:
				break;

			}

		}
	}

	//-------------------------------------------------------------


	//STATE--------------------------------------------------------
	private STATE getState(){
		if(InsideAltar()){
			if(HaveEss())
				return STATE.Craft;
			if(HaveAir())
				return STATE.ExitAltar;
		}
		if(HaveEss() && !AtAltar())
			return STATE.WalktoAltar;
		if(HaveEss() && AtAltar())
			return STATE.EnterRock;
		if(HaveAir() && !InsideAltar() && !AtBank())
			return STATE.WalktoBank;
		if(AtBank() && HaveAir() || !HaveEss())
			return STATE.Bank;
		//So on and so forth, you know the general rule
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK, WalktoAltar, EnterRock, Craft, ExitAltar, WalktoBank, Bank;
	}	
	//-------------------------------------------------------------

	//Check Method---------------------------------------------------
	boolean AtAltar(){
		if(Objects.find(10, ALTAR_ID).length > 0 ){
			return true;
		}
		return false;
	}
	boolean InsideAltar(){
		if(Objects.find(25, ROCK_ID).length > 0)
			return true;
		return false;
	}
	boolean HaveEss(){
		if(Inventory.find(ESS_ID).length > 0)
			return true;
		return false;
	}
	boolean HaveAir(){
		if(Inventory.find(AIR_ID).length > 0){
			return true;
		}
		return false;
	}
	boolean AtBank(){
		if(Objects.find(4, BANK_ID).length > 0){
			return true;
		}
		return false;
	}
	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------

	private boolean WalktoB() {
		Walking.walkPath(WALKTOBANK);
		Walking.walking_timeout = 1;
		return true;
	}

	private boolean WalktoA() {

		Walking.walkPath(WALKTOALTAR);
		Walking.walking_timeout = 1;
		return true;
	}

	private boolean ExitAltar() {

		try{
			RSObject[] portal = Objects.find(15, PORTAL_ID);
			if(portal.length > 0)
				if(portal[0] != null)
					if(!portal[0].isOnScreen()){
						camera(portal[0]);
						if(!portal[0].isOnScreen())
							Walking.walkTo(portal[0].getPosition());
					}
			if(DynamicClicking.clickRSObject(portal[0], "Use")){
				sleep(500);
				while(Player.isMoving())
					sleep(50);
			}
			sleeps();
			return true;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
	}

	private boolean EnterRock() {

		RSObject[] rock = Objects.findNearest(25, ALTAR_ID);
		if(rock.length > 0)
			if(rock[0] != null){
				Walking.walkTo(rock[0].getPosition());
				camera(rock[0]);
				if(rock[0].click("Enter")){
					sleepWalk();
					return true;
				}
			}
		sleeps();
		return false;
	}

	private boolean Crafting() {

		RSObject[] altar = Objects.find(15, ROCK_ID);
		camera(altar[0]);
		if(!altar[0].isOnScreen())
			Walking.walkTo(altar[0].getPosition());
		if(DynamicClicking.clickRSObject(altar[0], "Craft-rune")){
			sleep(500);
			sleepWalk();
			return true;
		}
		return false;
	}

	private boolean Banking() {
		RSObject [] bank = Objects.findNearest(10, BANK_ID);
		while(!Banking.isBankScreenOpen()){
			try{bank[0].click("Bank");
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				return true;
			}
			//Banking.openBankBooth();
		}
		Banking.depositAll();
		withdrawItem(ESS_ID, "All");
		return true;
	}


	//ETC...
	//-------------------------------------------------------------

	//Other Method-------------------------------------------------
	//Other method such as...

	private boolean withdrawItem(int itemID, String amount){
		if(Banking.isBankScreenOpen()){
			RSItem[] item = Banking.find(itemID);
			if(item != null){
				if(amount == "10" || amount == "5" || amount == "1" || amount == "All"){
					item[0].click("Withdraw " + amount);
					sleep(800,1000);                    
				} else {
					item[0].click("Withdraw X");
					sleep(800,1000);
					RSInterfaceChild WithdrawInterface = Interfaces.get(548, 93);
					if(WithdrawInterface != null) {
						Keyboard.typeSend(amount);
					}
				}
				return true;
			}
		}
		return false;
	}

	private void sleepWalk() {
		sleep(500);
		while(Player.isMoving())
			sleep(50);
		sleep(500);
	}
	private void sleeps() {
		sleep(500);
		while(Player.getAnimation() != -1){
			sleep(50);
		}
	}
	private void TurnCamera(RSObject EX){
		Camera.setCameraAngle(0);
		RSObject target = EX;
		Camera.turnToTile(target.getPosition());
		sleep(50);
	}
	private void camera(RSObject cam){
		RSObject target = cam;	
		Camera.turnToTile(target.getPosition());
	}
	//-------------------------------------------------------------


} // END OF SCRIPT