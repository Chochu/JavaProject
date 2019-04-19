package scripts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import scripts.DGNatureCrafter.NewJFrame;


@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG AIO Fletching", version = 1.0)


public class DGAIOFletching extends Script implements Painting {

	//Variable---------------------------------------------------

	private STATE SS = getState();
	private final long startTime = System.currentTimeMillis(); //timer
	private int START_XP = Skills.getXP("Fletching");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Fletching");
	private int lvl = Skills.getCurrentLevel("Fletching");
	String sa; //Some String, I use it to store my player's action
	//any other variable that you want to use

	//-------------------------------------------------------------
	private boolean USETAB = false;
	private int MAPLELOG = 1517; // a array of ID
	private int KNIFE = 946;
	private int CHEST = 4483;

	private int amount  = 0;

	private final RSTile ToTree[] = {
			new RSTile(2953,3225),
			new RSTile(2949,3228),
			new RSTile(2942,3229),
			new RSTile(2938,3232)
	};
	int DUELRING[] = {
			2552,2554,2556,2558,2560,2562,2564,2566
	};
	int AXESnRING[] = {
			2552,2554,2556,2558,2560,2562,2564,2566,6739,1359
	};

	//Paint-----------------------------------------
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

		g.drawString("DG AIO Fletching", 280, 218);
		g.setFont(font1);
		g.setColor(color2);
		g.drawString("Status", 254, 245);
		g.drawString("Exp Gain", 254, 265);
		g.drawString("Wood Cut", 254, 285);
		g.drawString("Time", 254, 305);
		g.drawString( sa, 310, 245);
		g.drawString("" + ((START_XP) - (CURRENT_XP)), 310, 265);
		g.drawString("" + ((START_XP) - (CURRENT_XP))/58, 310, 285);
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 305);
	}
	//END: Code generated using Enfilade's Easel
	//Main Loop----------------------------------------------------

	@Override
	public void run() {


		println("The Bot has run");


		while(true){
			START_XP = Skills.getXP("Fletching");
			XP_TILL = Skills.getPercentToNextLevel("Fletching");
			lvl = Skills.getCurrentLevel("Fletching");
			Mouse.setSpeed(180); //this is mouse speed, not necessay 
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable

			switch(SS){ //loop through the enum array and check what state does it return
			case CHECK: //this is default return of getState method
				SS = getState();
				break;
			case Banking:
				sa = "Banking";
				Banking();
				break;
			case FLETCHING:
				sa = "Fletching";
				Fletching();
				break;
			case WalkingToChest:
				sa = "Walking to Chest";
				WalkingToChest();
				break;
			case opening:
				sa = "Opening Chest";
				opening();
				break;
			case AtLum:
				sa = "At Lumb, teleporting to Castle";
				AtLum();
				break;
			default:
				break;

			}
		}

	}

	//-------------------------------------------------------------


	//STATE--------------------------------------------------------
	private STATE getState(){
		if(Inventory.find(MAPLELOG).length >0 && Inventory.find(KNIFE).length > 0)
			return STATE.FLETCHING;
		if(Objects.find(10, 1124).length > 0){
			return STATE.AtLum;
		}
		else{
			if(AtCastle()){
				if(!NearChest())
					return STATE.WalkingToChest;
				if(NearChest() && !BankScreenIsOpen())
					return STATE.opening;
			}
			if(BankScreenIsOpen()){
				return STATE.Banking;
			}

		}
		return STATE.CHECK; //default return
	}

	public enum STATE { 
		CHECK, FLETCHING, AtLum, WalkingToChest, opening, Banking, 

	}	
	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	public boolean Fletching(){
		while(Inventory.find(MAPLELOG).length > 0){
			RSItem [] knife = Inventory.find(KNIFE);
			RSItem [] LOG = Inventory.find(MAPLELOG);
			if(!(knife.length > 0)){
				return false;
			}
		}
		return false;
	}
	public static void enterPin(int pin){
		char[] pinNums = Integer.toString(pin).toCharArray();
		if(Interfaces.get(13) != null){
			try{
				if(Interfaces.get(13, 151).getText().equals("First click the FIRST digit.")){
					//Find number to click
					for(int i = 110; i < 119; i++){
						if(Interfaces.get(13, i).getText().equals("" + pinNums[0]) &&
								Interfaces.get(13, 151).getText().equals("First click the FIRST digit.")){
							Interfaces.get(13, i - 10).click("Enter digit");
							long time = System.currentTimeMillis();
							while(Interfaces.get(13) != null &&
									System.currentTimeMillis() - time < General.random(1250,1750)){
								General.sleep(1);
							}
						}
					}
				}else if(Interfaces.get(13, 151).getText().equals("Now click the SECOND digit.")){
					//Find number to click
					for(int i = 110; i < 119; i++){
						if(Interfaces.get(13, i).getText().equals("" + pinNums[1]) &&
								Interfaces.get(13, 151).getText().equals("Now click the SECOND digit.")){
							Interfaces.get(13, i - 10).click("Enter digit");
							long time = System.currentTimeMillis();
							while(Interfaces.get(13) != null &&
									System.currentTimeMillis() - time < General.random(1250,1750)){
								General.sleep(1);
							}
						}
					}
				}else if(Interfaces.get(13, 151).getText().equals("Time for the THIRD digit.")){
					for(int i = 110; i < 119; i++){
						if(Interfaces.get(13, i).getText().equals("" + pinNums[2]) && 
								Interfaces.get(13, 151).getText().equals("Time for the THIRD digit.")){
							Interfaces.get(13, i - 10).click("Enter digit");
							long time = System.currentTimeMillis();
							while(Interfaces.get(13) != null &&
									System.currentTimeMillis() - time < General.random(1250,1750)){
								General.sleep(1);
							}
						}
					}
				}else if(Interfaces.get(13, 151).getText().equals("Finally, the FOURTH digit.")){
					//Find number to click
					for(int i = 110; i < 119; i++){
						if(Interfaces.get(13, i).getText().equals("" + pinNums[3]) &&
								Interfaces.get(13, 151).getText().equals("Finally, the FOURTH digit.")){
							Interfaces.get(13, i - 10).click("Enter digit");
							long time = System.currentTimeMillis();
							while(Interfaces.get(13) != null &&
									System.currentTimeMillis() - time < General.random(1250,1750)){
								General.sleep(1);
							}
						}
					}
				}
			}catch(Exception e){}
		}
	}
	boolean AtLum(){
		if(Objects.find(10, 1124).length > 0){
			RSItem[] ring = Inventory.find(DUELRING);
			if(ring.length > 0)
				if(ring[0] != null){
					ring[0].click("Wear");
					sleep(1000);
				}
			TeleportingCW();
		}
		return false;
	}

	boolean Banking(){
		for(int x = 0; x < 4 ; x++){
			enterPin(5294);
		}
		Banking.depositAllExcept(KNIFE);
		sleep(2000);
		if(!getEquipment()){
			withdrawItem(2552, "1");
		}
		sleep(1000);
		withdrawItem(MAPLELOG,"All");
		Banking.close();
		sleep(2000);
		if(Inventory.find(DUELRING).length > 0){
			RSItem [] ring = Inventory.find(DUELRING);
			ring[0].click("Wear");
		}
		return true;
	}
	boolean WalkingToChest(){
		GameTab.open(GameTab.TABS.INVENTORY);
		if(Walking.walkTo(new RSTile(2442,3084))){
			return true;
		}
		return false;

	}
	boolean opening(){
		RSObject [] chest = Objects.findNearest(5, CHEST);
		while(!Banking.isBankScreenOpen())
			if(chest.length > 0)
				if(chest[0] != null){
					if(!chest[0].isOnScreen())
						camera(chest[0]);
					chest[0].click("Use");
					sleep(750);
					return true;
				}
		return false;
	}
	public boolean TeleportingCW(){
		GameTab.open(GameTab.TABS.EQUIPMENT);
		clickRing();
		RSInterfaceChild castelwars = Interfaces.get(137, 9);
		sleep(1000);
		castelwars.click("Continue");
		sleep(2500,3000);
		return true;
	}
	public boolean clickRing() {
		RSInterfaceChild ringSlot = Interfaces.get(387, 22);
		ringSlot.click("Operate");
		RSItem [] ring = Inventory.find(DUELRING);
		if(ring.length > 0)
			if(ring[0] != null){
				ring[0].click("Rub");
				return true;
			}
		return false;
	}
	void WearRing(){
		RSItem [] ring = Inventory.find(DUELRING);
		if(ring.length > 0)
			if(ring[0] != null)
				ring[0].click("Wear");
	}
	void duelRingClick(int option) {
		RSInterfaceChild click = Interfaces.get(230).getChild(1 + option);
		click.click("Continue");      
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
	//Check Method---------------------------------------------------
	private boolean cuttingent(){
		RSObject Ent[] = Objects.find(5, 778);
		RSObject flower[] = Objects.find(35,1189);
		if(Ent.length > 0){
			sa = "Walking away from Ent Tree";
			Walking.walkTo(flower[0].getPosition());
			return true;
		}
		return false;
	}
	/*
	private boolean havering(){
		if(Inventory.find(DUELRING).length > 0)
			return true;
		return false;


	}
	 */
	private boolean IsInCombat(){
		if(Player.getAnimation() == 424 || Player.getAnimation() == 422)
			return true;
		return false;
	}
	private boolean AtTreeLocation(){
		if(Objects.find(25, 1189).length > 0)
			return true;
		return false;
	}

	private boolean BankScreenIsOpen(){
		if(Banking.isBankScreenOpen())
			return true;
		return false;
	}
	private boolean HaveLog(){
		if(Inventory.find(MAPLELOG).length > 0)
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
		cuttingent();
		while(Player.getAnimation() != -1 && !IsInCombat()){
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