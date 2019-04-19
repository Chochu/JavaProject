package scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.locks.Condition;

import javax.imageio.ImageIO;

import org.omg.PortableServer.POAManagerPackage.State;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.InventoryItem;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
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
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.EnumScript;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;


@ScriptManifest(authors = { "DoomGuard" }, category = "WoodCutting", name = "Test", description = "Cut Yew At Rimmingtion Deposit With DR", version = 1.0)


public class test extends Script implements Painting {

	//Variable---------------------------------------------------

	private STATE SS = getState();
	private final long startTime = System.currentTimeMillis(); //timer
	private int START_XP = Skills.getXP("Woodcutting");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Woodcutting");
	private int lvl = Skills.getCurrentLevel("Woodcutting");
	String sa; //Some String, I use it to store my player's action
	//any other variable that you want to use

	//-------------------------------------------------------------
	private boolean USETAB = true;
	private int AIRR = 556;
	private int LAWR = 557;
	private int EARTHR = 563;
	private int TAB = 8013;
	private int YEWLOGS = 1515; // a array of ID
	private int YEW = 1309;
	private int [] AXES = {6739,1359};
	private int CHEST = 4483;
	private int PORTALIN = 13405;
	private int PORTALOUT = 15478;
	private final RSTile ToTree[] = {
			new RSTile(2953,3225),
			new RSTile(2949,3228),
			new RSTile(2942,3229),
			new RSTile(2938,3232)
	};
	int DUELRING[] = {
			2552,2554,2556,2558,2560,2562,2564,2566
	};
	int x1,y1,x2,y2;
	//Main Loop----------------------------------------------------


	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString("DoomGuard Fire Crafter", 10, 70);
		g.drawString("State : " + sa, 10, 100);
		g.drawString("EXP Current: " + (CURRENT_XP) , 10, 110);
		g.drawString("EXP Start: " + (START_XP) , 10, 140);
		g.drawString("Percent Till Next Level: " + XP_TILL, 10 , 120);
		g.drawString("Current Lvl: " + lvl, 10 , 130 );
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 305);
		g.drawRect(x1, y1, x2, y2);
	}

	@Override
	public void run() {

		println("The Bot has run");
		while(true)
		buy(1);
		

	}

	private boolean buy(int x){
	
			RSItem[] items = Interfaces.get(11, 61).getItems();// GET ALL THE ITEM IN THE SHOP
			for (int i = 0; i < items.length; i++) { // LOOP THROUGHT THE ITEM
				if (items[i].getID() == 2357) { // IF THE ITEM IS SAME AS THE ITEM WANT
					println(items[i].getID()+ " "+ items[i].getStack());

					items[i].changeType(org.tribot.api2007.types.RSItem.TYPE.BANK); // TURN TO A BANK ITEM
					Mouse.click((int)items[i].getArea().getCenterX() + 30, (int)items[i].getArea().getCenterY()+10,3); //right click the item;
					if(x == 10){
						if(ChooseOption.isOptionValid("Deposit 10")) { // IF BUY 10 IS AVI
							//ChooseOption.select("Deposit 10"); // CLICK BUY TEN
							return false;
						} 
						sleep(500);
						
					}
					if(x == 5){
						if(ChooseOption.isOptionValid("Deposit 5")) { // IF BUY 5 IS AVI
							//ChooseOption.select("Deposit 5"); // CLICK buy 5 
							return false;
						} 
						sleep(500);
					}
					if(x == 1){
						if(ChooseOption.isOptionValid("Deposit 1")) { // IF BUY 1 IS AVI
							ChooseOption.select("Deposit 1"); // CLICK BUY one
						} 
						sleep(500);
					}
					if(ChooseOption.isOptionValid("Cancel")){
						ChooseOption.select("Cancel"); // CLICK BUY one
						sleep(500);
						return false;
					}
				} 
			}
		
		return false;
	}
	private boolean getEquipment() {

		RSInterfaceChild equip = Interfaces.get(387, 28);
		if (equip != null) {
			for (int i = 0; i < equip.getItems().length; i++) {
				for (int j = 0; j < DUELRING.length; j++) {
					if (equip.getItems()[i].getID() == DUELRING[j]) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//-------------------------------------------------------------


	//STATE--------------------------------------------------------
	private STATE getState(){
		if(AtCastle() && HaveLog()){
			if(!NearChest() || !Banking.isBankScreenOpen())
				return STATE.WalkingToChest;
			if(BankScreenIsOpen()){
				if(HaveLog())
					return STATE.Banking;
			}
		}
		if(HaveTab() && !HaveLog()){
			return STATE.TeleportingHome;
		}
		if(AtPortalIn()){
			return STATE.ExitHouse;
		}
		if(AtPortalOut()){
			return STATE.WalkingToTree;
		}
		if(AtTreeLocation() && ! AtPortalOut()){
			if(!Inventory.isFull())
				return STATE.ChopTree;
			if(Inventory.isFull())
				return STATE.TeleportingCW;
		}
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK, WalkingToChest,
		TeleportingHome, ExitHouse, WalkingToTree, TeleportingCW, ChopTree, Banking;


	}	
	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------

	void Banking(){
		Banking.depositAllExcept(AXES);
		sleep(2000);
		if(USETAB == true){
			withdrawItem(TAB, "1");
		}
		if(USETAB == false){
			withdrawItem(AIRR, "1");
			withdrawItem(EARTHR, "1");
			withdrawItem(LAWR, "1");
		}
		if(getEquipment()){
			withdrawItem(2552, "1");
		}
		Banking.close();
	}
	void WalkingToChest(){
		GameTab.open(GameTab.TABS.INVENTORY);
		if(Walking.walkTo(new RSTile(2442,3084)))
			SleepTill();
		RSObject [] chest = Objects.findNearest(5, CHEST);
		while(!Banking.isBankScreenOpen())
			if(chest.length > 0)
				if(chest[0] != null){
					if(!chest[0].isOnScreen())
						camera(chest[0]);
					chest[0].click("Use");
					sleep(750);
				}
	}
	public void TeleportingCW(){
		GameTab.open(GameTab.TABS.EQUIPMENT);
		clickRing();
		RSInterfaceChild castelwars = Interfaces.get(137, 9);
		sleep(1000);
		castelwars.click("Continue");
		sleep(2500,3000);
	}
	public void clickRing() {
		RSInterfaceChild ringSlot = Interfaces.get(387, 22);
		ringSlot.click("Operate");
	}
	void ChopTree(){
		while(!Inventory.isFull()){
			RSObject [] Tree = Objects.find(20, YEW);
			if(Tree.length > 0)
				if(Tree[0] != null){
					if(!Tree[0].isOnScreen()){
						camera(Tree[0]);
						sleep(1000);
						if(!Tree[0].isOnScreen()){
							Walking.walkTo(Tree[0].getPosition());
						}
					}
					sa = "Choping";
					Tree[0].click("Chop down");
					SleepTill();

				}
		}
	}
	void WearRing(){
		RSItem [] ring = Inventory.find(DUELRING);
		if(ring.length > 0)
			if(ring[0] != null)
				ring[0].click("Wear");
	}
	void TeleportingHome(){
		if(Inventory.find(DUELRING).length > 0){
			WearRing();
		}
		if(Inventory.find(TAB).length > 0){
			RSItem [] TABS = Inventory.find(TAB);
			TABS[0].click("Break");
			SleepTill();
			sleep(500);
		}
		else{
			GameTab.open(GameTab.TABS.MAGIC);
			RSInterfaceChild house = Interfaces.get(192, 23);
			house.click("Cast");
			SleepTill();
		}

	}
	void duelRingClick(int option) {
		RSInterfaceChild click = Interfaces.get(230).getChild(1 + option);
		click.click("Continue");      
	}
	void ExitHouse(){
		if(Objects.find(5, PORTALIN).length > 0){
			RSObject[] Portal = Objects.find(5, PORTALIN);
			Portal[0].click("Enter");
			sleep(500);
		}
	}
	void WalkingToTree(){
		if(Objects.find(5, PORTALOUT).length > 0){
			Walking.walkPath(ToTree);
		}
	}


	//-------------------------------------------------------------
	//Check Method---------------------------------------------------
	private boolean AtTreeLocation(){
		if(Objects.find(35, 1189).length > 0)
			return true;
		return false;
	}
	private boolean AtPortalOut(){
		if(Objects.find(10, PORTALOUT).length > 0)
			return true;
		return false;
	}
	private boolean AtPortalIn(){
		if(Objects.find(10, PORTALIN).length > 0)
			return true;
		return false;
	}
	private boolean HaveTab(){
		if(Inventory.find(AIRR).length > 0){
			if(Inventory.find(EARTHR).length > 0)
				if(Inventory.find(LAWR).length > 0)
					return true;
		}
		else if(Inventory.find(TAB).length > 0)
			return true;
		return false;
	}
	private boolean BankScreenIsOpen(){
		if(Banking.isBankScreenOpen())
			return true;
		return false;
	}
	private boolean HaveLog(){
		if(Inventory.find(YEWLOGS).length > 0)
			return true;
		return false;
	}
	private boolean AtCastle(){
		if(NPCs.find(1526).length > 0)
			return true;
		return false;
	}
	private boolean NearChest(){
		if(Objects.find(3, CHEST).length > 0)
			return true;
		return false;
	}
	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------



	//Other Method-------------------------------------------------
	private void SleepTill(){
		sleep(750);
		while(Player.getAnimation() != -1){
			sleep(500);
		}
		sleep(500);
	}
	private void camera(RSObject cam){
		RSObject target = cam;
		Camera.turnToTile(target.getPosition());
	}
	private void cameras(){
		Camera.setCameraAngle(100);
		Camera.setCameraRotation(0);
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


} // END OF SCRIPT