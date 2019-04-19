package scripts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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


@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG AIO Crafter", description = "DG", version = 1.2)


public class DGAIOCrafter extends Script implements Painting {

	//Variable---------------------------------------------------

	private STATE SS = getState();
	private final int GOLDBAR = 2357;
	int GEM = Integer.parseInt(JOptionPane.showInputDialog("Gold[0] Sapphire[1607] Emerald[1605] Ruby[1603] Diamond[1601] Dragon[1599] Onyx[1597]: ")); // a single ID
	private final int MOLD = Integer.parseInt(JOptionPane.showInputDialog("Enter the Mold: ","11065"));
	private final int ball =  Integer.parseInt(JOptionPane.showInputDialog("Enter the Final Product: ",""));;
	int type = Integer.parseInt(JOptionPane.showInputDialog("Ring: Gold[70] Sapphire[72] Emerald[78] Ruby[84] Diamond[90] Dragon[98] Onyx[102]: \n Neckless: Gold[113] Sapphire[119] Emerald[125] Ruby[133] Diamond[140] Dragon[145] Onyx[151]:  \n Amulet: Gold[164] Sapphire[169] Emerald[175] Ruby[182] Diamond[187] Dragon[192] Onyx[200]:\n Bracelets: Gold[210] Sapphire[220] Emerald[223] Ruby[230] Diamond[236] Dragon[241] Onyx[247]: ")); // a single ID
	char ctrl = KeyEvent.VK_CONTROL;
	private int START_XP = Skills.getXP("Crafting");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Crafting");
	private int lvl = Skills.getCurrentLevel("Crafting");
	final RSTile[] TOBANK = {
			new RSTile(3276, 3186, 0), new RSTile(3276, 3176, 0), new RSTile(3271, 3168, 0) };
	final RSTile[] TOFUR = {
			new RSTile(3270, 3167, 0), new RSTile(3277, 3175, 0), new RSTile(3276, 3185, 0) };
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

		g.drawString("DG Bracelets Maker", 280, 218);
		g.setFont(font1);
		g.setColor(color2);
		g.drawString("Status", 254, 245);
		g.drawString("Exp Gain", 254, 265);
		g.drawString("Lvl", 254, 275);
		g.drawString("Bra Made", 254, 285);
		g.drawString("Time", 254, 305);
		g.drawString( sa, 310, 245);
		g.drawString(""+ lvl, 310, 275);
		g.drawString("" + ((START_XP) - (CURRENT_XP)), 310, 265);
		g.drawString("" + (((START_XP) - (CURRENT_XP))/60), 310, 285);
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 305);
		g.drawString("Loot Per Hour: " +  perHour(((START_XP) - (CURRENT_XP) )/60) , 10, 130);
	}

	@Override
	public void run() {

		println("The Bot has run");
		while(true){
			Walking.control_click = true;
			check();
			Mouse.setSpeed(200); //this is mouse speed, not necessay 
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
			case RUN:
				sa = "Running Away";
				walkingtobank();
				sleep(7000);
				Banking();
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
			return STATE.RUN;
		if(ATFUR() && Inventory.find(GOLDBAR).length > 0)
			return STATE.SMITHING;
		if(ATFUR() && Inventory.find(ball).length > 0)
			return STATE.WalkingtoBank;
		if(ATBANK() && (Inventory.find(ball).length > 0 || Inventory.find(GOLDBAR).length == 0))
			return STATE.BANKING;
		if(ATBANK() && Inventory.find(GOLDBAR).length > 0 && !ATFUR())
			return STATE.WalkingtoFur;
		if(Objects.find(20, 11737).length > 0){
			return STATE.walkdown;
		}
		//So on and so forth, you know the general rule
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK, SMITHING, WalkingtoFur, WalkingtoBank, BANKING, walkdown, RUN,
	}	
	//-------------------------------------------------------------

	//Check Method---------------------------------------------------
	public int perHour(int gained) {
		return ((int) ((gained) * 3600000D / (System.currentTimeMillis() - startTime)));
	}
	public boolean ATFUR(){
		if(Objects.find(5, 24012).length > 0){
			return true;
		}
		return false;
	}
	public boolean ATBANK(){
		if(Objects.find(5, 2198).length > 0)
			return true;
		return false;
	}
	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	boolean CheckI(){
		RSInterfaceChild sel = Interfaces.get(446, type);
		if(sel != null)
			return true;
		return false;
	}
	private void check(){
		START_XP = Skills.getXP("Crafting");
		XP_TILL = Skills.getPercentToNextLevel("Crafting");
		lvl = Skills.getCurrentLevel("Crafting");
	}
	private boolean walkdown(){
		Walking.walkTo(new RSTile(2972,3373));
		DynamicClicking.clickRSTile(new RSTile(2971,3372), "Climb-down");
		return true;
	}
	private void walkingtobank() { //Walk path [this is a example
		while(!ATBANK()){
			Walking.walkPath(TOBANK);
			Walking.walking_timeout = 1;
		}
	}
	private void walkingtofur() { //Walk path [this is a example
		while(!ATFUR()){
			Walking.walkPath(TOFUR);
			Walking.walking_timeout = 1;
		}

	}
	private boolean Banking(){
		Walking.walkTo(new RSTile (2946,3369));
		GameTab.open(GameTab.TABS.INVENTORY);
		while(!Banking.isBankScreenOpen())
			Banking.openBankBooth();
		while(Banking.isPinScreenOpen())
			Banking.inPin();
		Banking.depositAllExcept(MOLD);
		sleep(500);
		if(GEM != 0)
			withdrawItem(GOLDBAR,"13");
		else 
			withdrawItem(GOLDBAR,"All");
		if(GEM != 0)
			withdrawItem(GEM,"All");
		Banking.close();
		return true;
	}
	public boolean SMITHING(){
		try{
			Camera.setCameraRotation(0);
			Camera.setCameraAngle(100);
			RSItem [] steel = Inventory.find(GOLDBAR);
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
					sleeps();
					while(!CheckI()){
						sleeps();
						steel[0].click("Use");
						if(fur[0].click("Use")){
							makiing();
							return false;
						}
						else
							fur[0].click("Cancel");
					}

					if(CheckI())
						makiing();
					return false;
				}

		}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
		return false;
	}
	public boolean makiing(){
		
		sleep(1000);
		RSInterfaceChild sel = Interfaces.get(446, type);
		sa = "Making Now";
		try{
			if(sel.click("Make X")){
				sleep(1000);
				Keyboard.typeSend("27");
			}
		}
		catch(NullPointerException e){
			return false;
		}
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
	boolean sleepC(){
		int x = Inventory.find("Gold bar").length;
		sleep(1000);
		if(Inventory.find("Gold bar").length < x){
			println("Sleeps");
			sleep(1000);
			check();
			sleepC();
		}
		return false;
	}
	private void sleeps() {
		sleep(900);
		while(!Player.getRSPlayer().isInCombat() && Player.getAnimation() == 899){
			check();
			sleepC();
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