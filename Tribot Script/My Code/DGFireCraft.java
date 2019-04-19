package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import org.omg.PortableServer.POAManagerPackage.State;
import org.tribot.api.DynamicClicking;
import org.tribot.api.GameTab.TABS;
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
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.EnumScript;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DGFireCrafter", 
description = "Chest to Rock and Rock and Chest")

public class DGFireCraft extends Script implements Painting{
//----------------------------------------------------------------
	static final int[] ID_GATEC = {3197, 3198};
	static final int[] ID_GATEO = {4139, 4140};
	static final int ID_PORTAL = 19202;
	static final int ID_ALTAR = 23274;
	static final int[] ID_CHEST = {3194,3193};
	static final int[] ID_ESS = {1436, 7936};
	static final int ID_ROCK = 26183; //outside
	static final int fire = 554;
//-------------------------------------------------------------------
	private final long startTime = System.currentTimeMillis();
	private int START_XP = Skills.getXP("Runecrafting");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Runecrafting");
	private int lvl = Skills.getCurrentLevel("Runecrafting");
	private STATE SS = getState();
	char ctrl = KeyEvent.VK_CONTROL;
	//Locations
	final RSTile TILE_ROCK = new RSTile(3312, 3253);
	final RSTile TILE_ALTAR = new RSTile(2583, 4840);
	final RSTile TILE_CHEST = new RSTile(3381, 3268);
	//Paths
	final RSTile[] ROCK_TO_GATE = {
			new RSTile(3308,3246),
			new RSTile(3308,3239),
			new RSTile(3311,3235)
	};
	final RSTile[] GATE_TO_CHEST = {
			new RSTile(3316,3234),
			new RSTile(3322,3242),
			new RSTile(3325,3252),
			new RSTile(3325,3261),
			new RSTile(3334,3265),
			new RSTile(3343,3266),
			new RSTile(3352,3265),
			new RSTile(3364,3266),
			new RSTile(3373,3266),
			new RSTile(3381,3268),
	};
	final RSTile[] CHEST_TO_GATE = {
			new RSTile(3377,3265),
			new RSTile(3367,3265),
			new RSTile(3358,3265),
			new RSTile(3346,3266),
			new RSTile(3334,3266),
			new RSTile(3326,3263),
			new RSTile(3325,3254),
			new RSTile(3319,3242),
			new RSTile(3314,3235),
	};
	final RSTile[] GATE_TO_ROCK = {
			new RSTile(3312,3253),
			new RSTile(3308,3248),
			new RSTile(3311,3253)
	};
	final RSTile[] TO_ALTAR = {
			new RSTile(2580,4844),
			new RSTile(2584,4841)
	};
//-------------------------------------------------------------------
	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("DoomGuard Fire Crafter", 10, 70);
		g.drawString("State : " + SS, 10, 100);
		g.drawString("EXP Current: " + (CURRENT_XP) , 10, 110);
		g.drawString("EXP Start: " + (START_XP) , 10, 140);
		g.drawString("Percent Till Next Level: " + XP_TILL, 10 , 120);
		g.drawString("Current Lvl: " + lvl, 10 , 130 );
		
		
	}
	
	private boolean Chest(){
		RSObject[] Cchests = Objects.getAt(new RSTile(3381,3269));
		if(Cchests[0].click("Open")){
			if(!Banking.isBankScreenOpen())
			Cchests[0].click("Bank");
			sleep(500);
			banking();
			return true;
		}
		else{
			Cchests[0].click("Bank");
			sleep(500);
			banking();
			return true;
		}
	}
	
	private void banking(){
		sleep(500);
		Banking.depositAll();
		sleep(500);
		Banking.withdraw(28, ID_ESS);
		Banking.close();
	}
	private boolean rocktogate(){
		if(Walking.walkPath(ROCK_TO_GATE)){
			sleepM();
			return true;
			}
		return false;
	}
	private boolean gatetochest(){
		if(Walking.walkPath(GATE_TO_CHEST)){
			//sleepM();
			return true;
			}
		return false;
	}
	private boolean chesttogate(){
		if(Walking.walkPath(CHEST_TO_GATE)){
				return true;
		}
		return false;
		
	}
	private boolean gatetorock(){
		if(Walking.walkPath(GATE_TO_ROCK)){
			return true;
			}
		return false;
	}
	private void opengate(){
		RSObject[] gate = Objects.findNearest(40, ID_GATEC);
		Camera.setCameraAngle(0);
		RSObject target = gate[0];
		Camera.turnToTile(target.getPosition());
		gate[0].click("Open");
		sleep(50);
	}
	private boolean Walking_to_Rune() {
		if(Walking.walkPath(TO_ALTAR)){
			sleepM();
			return true;
		}
		return false;
	}			  		
	 
	private boolean runes(){
		RSObject[] altar = Objects.find(15, ID_ALTAR);
		RSObject target = altar[0];
		Camera.turnToTile(target.getPosition());
		if(altar[0].click("Craft-rune")){
			sleep(50);
			return true;
		}
		return false;
		
	}
	private boolean walktoportal(){
		if(Walking.walkTo(new RSTile (2574, 4848))){
			
			return true;
		}
		return false;
	}
	private boolean useportal(){
		RSObject[] portal = Objects.find(15, ID_PORTAL);
		Camera.turnToTile(portal[0].getPosition());
		if(portal[0].click("Use"))
		{
			sleepM();
			return true;
		}
		return false;
	}
	private boolean enterrock(){
		RSObject[] rock = Objects.find(15, ID_ROCK);
		if(rock[0].click("Enter"))
			return true;
		return false;
	}
	private void sleepM(){
		sleep(500);
		while(Player.isMoving()){
			sleep(500);
		}
		sleep(500);
	}
	private void sleeps(){
		sleep(50);
		while(Player.isMoving()){
			sleep(500);
		}
		sleep(50);
	}

	
//-------------------------------------------------------------------
	private boolean haveess(){
	if(Inventory.find(ID_ESS).length ==0){
			return false;
		}
		return true;
	}
	private boolean atBank()
	  {
	    return Objects.find(8, ID_CHEST).length > 0;
	  }
	private boolean insideAltar() {
	    return Objects.find(40, ID_PORTAL).length > 0;
	  }
	private boolean atRock() {
	    return Objects.find(12, ID_ROCK).length > 0;
	  }
	private boolean atGateC() { //gate is closed, return true if the gate is closed
		return Objects.findNearest(30, ID_GATEC).length > 0;
	  }
	private boolean atGateO() { //gate is closed, return true if the gate is closed
		return Objects.find(10, ID_GATEO).length > 0;
	  }
	private boolean atinsiderock() { //gate is closed, return true if the gate is closed
		return Objects.find(8, ID_ALTAR).length > 0;
	  }
	private boolean atportal() { //gate is closed, return true if the gate is closed
		return Objects.find(8, ID_PORTAL).length > 0;
	  }
	public enum STATE {
		gatetochest , Chests , chesttogate , gatetorock, enterrock, Walking_to_Rune, runes, walktoportal, usepartol, rocktogate, CHECK, openinggate
	}
//------------------------------------------------------------------
	private STATE getState(){
		if(atBank()){
			if(!haveess())
				return STATE.Chests;
			return STATE.chesttogate;
		}
		if(atGateO()){
			if(haveess()){
				return STATE.gatetorock;
			}
			else{
				return STATE.gatetochest;
			}
		}
		if(atRock()){
			if(haveess()){
				return STATE.enterrock;
			}
			else{
				return STATE.rocktogate;
			}
		}
		if(insideAltar()){
			if(haveess()){
				if(atinsiderock()){
					return STATE.runes;
				}
				return STATE.Walking_to_Rune;
			}
			else{
				if(atportal()){
					return STATE.usepartol;
				}
				return STATE.walktoportal;
			}
		}
		if(atGateC()){
			return STATE.openinggate;
		}
		
		return STATE.CHECK;
	}
	@Override
	public void run() {
		println("The Bot has run");
		while(true){
			START_XP = Skills.getXP("Runecrafting");
			XP_TILL = Skills.getPercentToNextLevel("Runecrafting");
			lvl = Skills.getCurrentLevel("Runecrafting");
			Mouse.setSpeed(160);
			Walking.control_click = true;
			SS = getState();
			switch(SS){
			case CHECK:
				if(Inventory.find(ID_ESS).length == 0){
					if(atRock()){
						rocktogate();
						sleeps();
						break;
					}
				gatetochest();
				sleeps();
				break;
				}
				if(Inventory.find(ID_ESS).length > 0){
					if(atGateO() || atGateC()){
						gatetorock();
						sleeps();
						break;
					}
					chesttogate();
					sleeps();
					break;
					
				}
			case Chests:
				Chest();
				sleeps();
				break;
			case chesttogate:
				chesttogate();
				sleeps();
				break;
			case Walking_to_Rune:
				Walking_to_Rune();
				sleeps();
				break;
			case enterrock:
				enterrock();
				sleeps();
				break;
			case gatetochest:
				gatetochest();
				sleeps();
				break;
			case gatetorock:
				gatetorock();
				sleeps();
				break;
			case rocktogate:
				rocktogate();
				sleeps();
				break;
			case walktoportal:
				walktoportal();
				sleeps();
				break;
			case usepartol:
				useportal();
				sleeps();
				break;
			case runes:
				runes();
				sleeps();
				break;
			case openinggate:
				opengate();
				sleeps();
			default:
				break;
			
			}
		}
	}
		


}