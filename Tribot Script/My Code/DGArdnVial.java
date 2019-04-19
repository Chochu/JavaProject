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
import org.tribot.api2007.Login;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Screen;
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
import scripts.DGNatureCrafter.NewJFrame;
@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG Arg", description = "BUY VIAL", version = 1.0)


public class DGArdnVial extends Script implements Painting {

	//Variable---------------------------------------------------

	private STATE SS = getState();
	private int VIAL = 227;
	private	int TAB = 8009;
	private final long startTime = System.currentTimeMillis(); //timer
	String sa; 
	private int Vial = 0;
	char ctrl = KeyEvent.VK_CONTROL;
	RSTile[] toDoor = {
			new RSTile(2653,3283),
			new RSTile(2645,3283),
			new RSTile(2640,3288),
			new RSTile(2633,3289),
			new RSTile(2629,3296),
			new RSTile(2621,3295),
			new RSTile(2614,3296)};
	RSTile[] toBank = {
			new RSTile(2615,3297),
			new RSTile(2621,3297),
			new RSTile(2626,3296),
			new RSTile(2632,3291),
			new RSTile(2637,3287),
			new RSTile(2642,3285),
			new RSTile(2647,3284),
			new RSTile(2653,3283)};
	private Random generator = new Random(System.currentTimeMillis());
	//-------------------------------------------------------------

	//Main Loop----------------------------------------------------
	//START: Code generated using Enfilade's Easel
	private final Color color1 = new Color(255, 255, 255, 90);
	private final Color color2 = new Color(0, 0, 0);
	private final Color color3 = new Color(0, 200, 0);

	private final BasicStroke stroke1 = new BasicStroke(1);

	private final Font font1 = new Font("Arial", 0, 9);
	private final Font font2 = new Font("Arial", 2, 15);

	@Override
	public void onPaint(Graphics g) {
		g.setColor(color1);
		g.fillRoundRect(245, 205, 188, 113, 16, 16);
		g.setColor(color2);
		((Graphics2D) g).setStroke(stroke1);
		g.drawRoundRect(245, 205, 188, 113, 16, 16);
		g.setFont(font2);

		g.drawString("DG Vial Buyer", 280, 218);
		g.setFont(font1);
		g.setColor(color2);
		g.drawString("Status", 254, 245);
		g.drawString("Vial Per Hour", 254, 265);
		g.drawString("Vial Brought", 254, 285);
		g.drawString("Time", 254, 305);
		g.drawString("" + perHour(Vial), 310, 265);
		g.drawString("" + Vial, 310, 285);
		g.drawString( sa, 310, 245);
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 305);
	}

	@Override
	public void run() {
		println("The Bot has run");

		while(true){
			Walking.control_click = true;
			Mouse.setSpeed(180); //this is mouse speed, not necessay 
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable

			switch(SS){ //loop through the enum array and check what state does it return
			case CHECK: //this is default return of getState method
				break;
			case Banking:
				sa = "Storing Dildo";
				Banking();
				break;
			case atDoor:
				sa = "Walking to striper Club";
				atDoor();
				break;
			case store:
				sa = "Paying Hooker";
				store();
				break;
			case buying:
				sa = "Buying Condom";
				buying();
				break;
			case WALKTOBANK:
				sa = "Bring the Lady to ur crib";
				WalkingtoB();
				break;
			default:
				break;

			}
		}
	}

	//-------------------------------------------------------------


	//STATE--------------------------------------------------------
	private STATE getState(){
		if(!Inventory.isFull() && ATHOUSE() && !shopInterfaceOpen())
			return STATE.store;
		if((InventoryVial() && ATBANK()) || NOMONEY())
			return STATE.Banking;
		if(!InventoryVial() && !ATHOUSE())
			return STATE.atDoor;
		if(shopInterfaceOpen() && !Inventory.isFull())
			return STATE.buying;
		if((InventoryVial() && Inventory.isFull()) && !ATBANK())
			return STATE.WALKTOBANK;
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK, Banking, WalkingtoGate, atGatec, atDoor, store,buying, TELE, LOGINSCREEN, WELCOMESCREEN,atGateo, WALKTOBANK;
	}	
	//-------------------------------------------------------------

	//Check Method---------------------------------------------------
	public boolean NOMONEY(){
		if(Inventory.find(995).length == 0){
			return true;
		}
		return false;
	}
	public int perHour(int gained) {
		return ((int) ((gained) * 3600000D / (System.currentTimeMillis() - startTime)));
	}
	private boolean InventoryVial(){
		if(Inventory.find(VIAL).length > 0)
			return true;
		return false;
	}
	public boolean ATBANK(){
		if(Objects.find(8, 23964).length > 0)
			return true;
		return false;
	}
	public boolean ATHOUSE(){
		if(NPCs.find(15, 3882).length > 0)
			return true;
		return false;
	}
	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	private boolean WalkingtoB(){
		opendoor1();
		Walking.walkPath(toBank);
		sleepa();

		return true;
	}

	private boolean buying(){
		opendoor();
		RSItem[] items = Interfaces.get(300, 75).getItems();
		if(items != null)
			for (int i = 0; i < items.length; i++) { 	
				if (items[i].getID() == VIAL) {
					items[i].changeType(org.tribot.api2007.types.RSItem.TYPE.BANK);
					if(items[i].getStack() > 0){
						items[i].click("Buy 10");
						sleep(500);
						return true;
					}
					if(items[i].getStack() == 0){
						Walking.walkTo(new RSTile(2616,3297));
						sleep(1000l);
						worldHop();
						return true;
					}

				}
			}
		return false;
	}


	private void worldHop() {

		final int[] WORLD_COLUMN_1 = {301,302,303,304,305,306,308,309,310,311,312,313,314,316,317,318},
				WORLD_COLUMN_2 = {319,320,321,322,325,326,327,328,329,330,333,334,335,336,337,338},
				WORLD_COLUMN_3 = {341,342,343,344,345,346,349,350,351,352,353,354,357,358,359,360},
				WORLD_COLUMN_4 = {361,362,365,366,367,368,369,370,373,374,375,376,377,378};

		final int[][] WORLD_LIST = {WORLD_COLUMN_1, WORLD_COLUMN_2, WORLD_COLUMN_3, WORLD_COLUMN_4};

		int world = General.random(305, 378);
		final int topX = 240,
				topY = 80,
				X_MULTIPLIER = 95,
				Y_MULTIPLIER = 24;
		int wNum = 0,
				xPos = 0,
				yPos = 0;

		println("Worldhoping");
		super.setLoginBotState(false);
		if (Login.getLoginState() == Login.STATE.INGAME) {
			Login.logout();
			sleep(2000, 3000);
		}
		if (Login.getLoginState() == Login.STATE.LOGINSCREEN) {
			Mouse.clickBox(15, 465, 95, 490, 1); // Clicks Box to Change world
			sleep(3000);
			Mouse.clickBox(394, 9, 400, 12, 1); //clicks players ^
			Mouse.clickBox(628, 9, 635, 12, 1); //clicks type v

			for(int x = 0; x < WORLD_LIST.length; x++){
				for(int y = 0; y < WORLD_LIST[x].length; y++){
					wNum = WORLD_LIST[x][y];

					if (wNum == world) {
						println(world);
						xPos = x * X_MULTIPLIER + topX;
						yPos = y * Y_MULTIPLIER + topY;

						Mouse.click(xPos, yPos, 1);
						sleep(3000);
					}
				}
			}
		}


		super.setLoginBotState(true);
		sleep(1500, 2500);
	}
	public boolean shopInterfaceOpen()
	{
		if(Interfaces.get(300, 91) != null){
			return true;
		}
		return false;
	}
	private void store(){
		GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
		RSNPC[] jim = NPCs.find(10,3882);
		if(jim != null)
			if(!jim[0].isOnScreen()){
				RSNPC target = jim[0];
				Camera.turnToTile(target.getPosition());
				Walking.walkTo(jim[0].getPosition());
				sleeps();
			}
		DynamicClicking.clickRSNPC(jim[0], "Trade");
		sleepa();
	}
	boolean Banking(){
		GameTab.open(GameTab.TABS.INVENTORY);
		while(!Banking.isBankScreenOpen() && !Banking.isPinScreenOpen()){
			Banking.openBankBooth();
	
		}
		while(Banking.isPinScreenOpen())
			Banking.inPin();
		Vial += Inventory.find(VIAL).length;
		Banking.depositAll();
		withdrawItem(995,"1k");
		while(Banking.isBankScreenOpen())
			Banking.close();

		return true;
	}



	boolean atDoor(){
		Walking.walkPath(toDoor);
		opendoor();
		sleepa();
		return true;
	}
	boolean opendoor(){
		if(Objects.getAt(new RSTile(2614,3295)).length > 0){
			Walking.walkTo(new RSTile(2614,3296));
			RSObject [] r = Objects.getAt(new RSTile(2614,3295));
			if(r != null)
				r[0].click("Open");
			sleepa();
			return true;
		}
		return false;
	}
	boolean opendoor1(){
		if(Objects.getAt(new RSTile(2614,3295)).length > 0){
			Walking.walkTo(new RSTile(2614,3296));
			RSObject [] r = Objects.getAt(new RSTile(2614,3293));
			if(r != null)
				r[0].click("Open");
			sleepa();
			return true;
		}
		return false;
	}
	//ETC...
	//-------------------------------------------------------------

	//Other Method-------------------------------------------------
	//Other method such as...
	private void SleepTill(){
		sleep(750);

		while(Player.getAnimation() != -1){
			sleep(500);
		}
		sleep(500);
	}
	private void sleeps() {
		sleep(500);
		while(Player.getAnimation() != -1){
			sleep(50);
		}
		sleep(500);
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
	boolean sleepa(){
		while(Player.isMoving())
			sleep(50l);
		return true;
	}
} // END OF SCRIPT