package scripts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Random;

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

import scripts.DGFireCraft.STATE;
@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DoomGuard Canon", description = "Smith Cannon", version = 1.2)


public class DGCannon extends Script implements Painting {

	//Variable---------------------------------------------------

	private STATE SS = getState();
	private final int STEELID = 2353;
	private final int mold = 4; // a single ID
	private final int ball = 2;
	char ctrl = KeyEvent.VK_CONTROL;
	private int START_XP = Skills.getXP("Smithing");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Smithing");
	private int lvl = Skills.getCurrentLevel("Smithing");
	final RSTile[] TOBANK = {
			new RSTile(2974,3370),
			new RSTile(2971,3375),
			new RSTile(2966,3377),
			new RSTile(2960,3378),
			new RSTile(2955,3379),
			new RSTile(2951,3376),
			new RSTile(2946,3370)};
	final RSTile[] TOFUR = {
			new RSTile(2946,3374),
			new RSTile(2951,3378),
			new RSTile(2960,3379),
			new RSTile(2966,3379),
			new RSTile(2974,3371)};
	private final long startTime = System.currentTimeMillis(); //timer
	String sa; //Some String, I use it to store my player's action
	//any other variable that you want to use

	//-------------------------------------------------------------
	private final Color color1 = new Color(255, 255, 255, 90);
	private final Color color2 = new Color(0, 0, 0);
	private final Color color3 = new Color(0, 200, 0);

	private final BasicStroke stroke1 = new BasicStroke(1);

	private final Font font1 = new Font("Arial", 0, 9);
	private final Font font2 = new Font("Arial", 2, 15);
	//Main Loop----------------------------------------------------
	@Override
	public void onPaint(Graphics g) {
		g.setColor(color1);
		g.fillRoundRect(245, 205, 188, 113, 16, 16);
		g.setColor(color2);
		((Graphics2D) g).setStroke(stroke1);
		g.drawRoundRect(245, 205, 188, 113, 16, 16);
		g.setFont(font2);

		g.drawString("DG Cannon Smelter", 280, 218);
		g.setFont(font1);
		g.setColor(color2);
		g.drawString("Status", 254, 245);
		g.drawString("Exp Gain", 254, 265);
		g.drawString("Lvl", 254, 275);
		g.drawString("Ball Smelth", 254, 285);
		g.drawString("Time", 254, 305);
		g.drawString( sa, 310, 245);
		g.drawString(""+ lvl, 310, 275);
		g.drawString("" + ((START_XP) - (CURRENT_XP)), 310, 265);
		g.drawString("" + (((START_XP) - (CURRENT_XP))/25)*4, 310, 285);
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 305);
	}

	@Override
	public void run() {

		println("The Bot has run");
		while(true){
			Walking.control_click = true;
			check();
			Mouse.setSpeed(180); //this is mouse speed, not necessay 
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable

			switch(SS){ //loop through the enum array and check what state does it return
			case CHECK: //this is default return of getState method
				break;
			case SMITHING:
				sa = "Smithing";
				SMITHING();
				break;
			case BANKING:
				sa = "Banking";
				Banking();
				break;
			case WalkingtoBank:
				sa = "Walking to Bank";
				walkingtobank();
				break;
			case WalkingtoFur:
				sa = "Walking to Fur";
				walkingtofur();
				break;
			case walkdown:
				sa = "Walking down stair";
				walkdown();
				break;
			default:
				break;

			}
		}
	}

	//-------------------------------------------------------------


	//STATE--------------------------------------------------------
	private STATE getState(){
		if(ATFUR() && Inventory.find(STEELID).length > 0)
			return STATE.SMITHING;
		if(ATFUR() && Inventory.find(ball).length > 0)
			return STATE.WalkingtoBank;
		if(ATBANK() && (Inventory.find(ball).length > 0 || Inventory.find(STEELID).length == 0))
			return STATE.BANKING;
		if(ATBANK() && Inventory.find(STEELID).length > 0 && !ATFUR())
			return STATE.WalkingtoFur;
		if(Objects.find(20, 11737).length > 0){
			return STATE.walkdown;
		}
		//So on and so forth, you know the general rule
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK, SMITHING, WalkingtoFur, WalkingtoBank, BANKING, walkdown,
	}	
	//-------------------------------------------------------------

	//Check Method---------------------------------------------------
	public boolean ATFUR(){
		if(Objects.find(8, 24012).length > 0){
			return true;
		}
		return false;
	}
	public boolean ATBANK(){
		if(Objects.find(15, 23964).length > 0)
			return true;
		return false;
	}
	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	private void check(){
		START_XP = Skills.getXP("Smithing");
		XP_TILL = Skills.getPercentToNextLevel("Smithing");
		lvl = Skills.getCurrentLevel("Smithing");
	}
	private boolean walkdown(){
		Walking.walkTo(new RSTile(2972,3373));
		DynamicClicking.clickRSTile(new RSTile(2971,3372), "Climb-down");
		return true;
	}
	private void walkingtobank() { //Walk path [this is a example
		Walking.walkPath(TOBANK);
	}
	private void walkingtofur() { //Walk path [this is a example
		Walking.walkPath(TOFUR);
	}
	private boolean Banking(){
		Walking.walkTo(new RSTile (2946,3369));
		GameTab.open(GameTab.TABS.INVENTORY);
		Banking.openBankBooth();
		while(Banking.isPinScreenOpen())
			Banking.inPin();
		Banking.depositAllExcept(4);
		sleep(1000);
		withdrawItem(STEELID,"All");
		Banking.close();
		return true;
	}
	public boolean SMITHING(){
		try{
			Camera.setCameraRotation(0);
			Camera.setCameraAngle(100);
			RSItem [] steel = Inventory.find(STEELID);
			RSObject [] fur = Objects.find(8, 24009);
			while(Player.isMoving())
				sleep(500,1000);
			if(steel.length > 0)
				if(steel[0] != null){
					if(fur.length > 0)
						if(!fur[0].isOnScreen()){
							Walking.walkTo(fur[0].getPosition());
							TurnCamera(fur[0]);
						}
					sleep(1000);
					steel[0].click("Use");
					if(fur[0].click("Use"))
						makiing();
					else
						fur[0].click("Cancel");
				}
		}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
		return false;
	}
	public boolean makiing(){
		sleep(1000);
		RSInterfaceChild sel = Interfaces.get(309, 3);
		try{
			sel.click("Make All");
		}
		catch(NullPointerException e){
			return false;
		}
		sleep(2000);
		sleeps();
		return false;
	}

	//ETC...
	//-------------------------------------------------------------
	/*
	 * 		while(balls != Inventory.getCount(2)){
			balls = Inventory.getCount(2);
			check();
			sleep(5000);
		}
	 */
	//Other Method-------------------------------------------------

	private void sleeps() {
		sleep(500);
		int balls = Inventory.find(STEELID).length;
		sleep(3000);
		while(balls != Inventory.find(STEELID).length){
			balls = Inventory.find(STEELID).length;
			check();
			sleep(5000,7000);
		}
	}
	private void TurnCamera(RSObject EX){
		Camera.setCameraAngle(100);
		RSObject target = EX;
		Camera.turnToTile(target.getPosition());
		EX.click("Open");
		sleep(50);
	}
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
	//-------------------------------------------------------------


} // END OF SCRIPT