package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import org.tribot.api.DynamicClicking;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Camera;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "Lumbi Man Stealer", description = "Learning to Be a thief")


public class MenTheif extends Script implements Painting {
	
	private STATE SS = getState();
	char ctrl = KeyEvent.VK_CONTROL;
	private final int [] men = {1,2,3,6};
	final RSTile[] walkhere = {
			new RSTile(3230,3219),
			new RSTile(3232,3231),
			new RSTile(3223,3238)
	};
	private int ID_TRAMP = 2794;
	private int ID_DOOR = 1530;
	private final long startTime = System.currentTimeMillis();
	private int START_XP = Skills.getXP("Thieving");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Thieving");
	private int lvl = Skills.getCurrentLevel("Thieving");
	String sa;
	
	
//-------------------------------------------------------------
	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("DoomGuard Thieving", 10, 70);
		g.drawString("State : " + sa, 10, 100);
		g.drawString("EXP Gain: " + ((START_XP) - (CURRENT_XP) ) , 10, 110);
		g.drawString("Percent Till Next Level: " + XP_TILL, 10 , 120);
		g.drawString("Current Lvl: " + lvl, 10 , 130 );
		g.drawString("Time Ran : " + Timing.msToString(Timing.timeFromMark(this.startTime)) , 10, 140);
	}
	
	@Override
	public void run() {
		println("The Bot has run");
		while(true){
			START_XP = Skills.getXP("Thieving");
			XP_TILL = Skills.getPercentToNextLevel("Thieving");
			lvl = Skills.getCurrentLevel("Thieving");
			Mouse.setSpeed(190);
			Walking.control_click = true;
			SS = getState();
			switch(SS){
			case CHECK:
				sa = "PickPocketing";
				Camera.setCameraAngle(100);
				pick();
				break;
			case DOOR:
				sa = "Opening Door";
				opendoor();
				break;
			case FIGHT:
				sa = "Fighting";
				running();
				break;
			default:
				break;
			
			}
		}
	}


//---------------------------------STATE-----------------------------
		private STATE getState(){
			if(fighting()){
				return STATE.FIGHT;
			}
			return STATE.CHECK;
		}
		public enum STATE {
			CHECK, DOOR, FIGHT
		}	

//----------------------Check Method-----------------------------------------
	public boolean fighting(){
		return Player.getAnimation() == 424;
	}
			

//-----------DO METHOD------------------------------

	private void opendoor() {
		RSObject[] door = Objects.findNearest(40, ID_DOOR);
		Camera.setCameraAngle(0);
		RSObject target = door[0];
		Camera.turnToTile(target.getPosition());
		door[0].click("Open");
		sleep(50);
		
	}

	private void running() {
		Walking.walkTo(new RSTile(3168,3184));
		
	}
	
	private void pick() {
		RSNPC[] jim = NPCs.findNearest(men);
		if(!jim[0].isOnScreen()){
			RSNPC target = jim[0];
			Camera.turnToTile(target.getPosition());
			Walking.walkTo(jim[0].getPosition());
			sleeps();
		}
		DynamicClicking.clickRSNPC(jim[0], "Pickpocket");
		}
	
	
	
	private void sleeps() {
			sleep(500);
		while(Player.getAnimation() != -1){
			sleep(50);
		}
	}
	
	
	private void WalkingToloc() {
			Walking.walkPath(walkhere);
		}


}