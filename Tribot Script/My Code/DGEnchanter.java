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

import scripts.DGFireCraft.STATE;
import scripts.DGNatureCrafter.NewJFrame;
@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG AIO Enchanting", description = "Enchanting", version = 1.0)


public class DGEnchanter extends Script implements Painting {

	//Variable---------------------------------------------------
	private STATE SS = getState();
	int bank = Integer.parseInt(JOptionPane.showInputDialog("Bank: ","23961")); // a single ID
	int RUNE = 564;
	String BankT = JOptionPane.showInputDialog("Bank Type: ","Bank"); // a single ID
	int CastWhat = Integer.parseInt(JOptionPane.showInputDialog("Sapphire[5] Emerald[16] Ruby[28] Diamond[36] Dragon[51] Onyx[61]: ")); // a single ID;
	int Items = Integer.parseInt(JOptionPane.showInputDialog("What Item to Enchant ", "11702")); // a single ID
	private int START_XP = Skills.getXP("Magic");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Magic");
	private int lvl = Skills.getCurrentLevel("Magic");
	private int amount = 17;
	private final long startTime = System.currentTimeMillis(); //timer
	String sa; //Some String, I use it to store my player's action
	//any other variable that you want to use

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

		g.drawString("DG AIO Enchanting", 280, 218);
		g.setFont(font1);
		g.setColor(color2);
		g.drawString("Status", 254, 245);
		g.drawString("Exp Gain", 254, 265);
		g.drawString("Cast", 254, 285);
		g.drawString("Time", 254, 305);
		g.drawString( sa, 310, 245);
		g.drawString("" + ((START_XP) - (CURRENT_XP)) + " Magic Exp", 310, 260);
		g.drawString(""+ ((START_XP) - (CURRENT_XP))/amount, 310, 285);
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 305);
	}

	@Override
	public void run() {
		println("The Bot has run");


		while(true){
			START_XP = Skills.getXP("Magic");
			XP_TILL = Skills.getPercentToNextLevel("Magic");
			lvl = Skills.getCurrentLevel("Magic");
			Mouse.setSpeed(180); //this is mouse speed, not necessay 
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable

			switch(SS){ //loop through the enum array and check what state does it return
			case CHECK: //this is default return of getState method
				break;
			case SuperHeating:
				sa = "Enchanting";
				Superheating();
				break;
			case Banking:
				sa = "Banking";
				Banking();
			default:
				break;

			}
		}
	}

	//-------------------------------------------------------------


	//STATE--------------------------------------------------------
	private STATE getState(){
		if(InventoryOre()){
			return STATE.SuperHeating;
		}
		if(!InventoryOre()){
			return STATE.Banking;
		}
		//So on and so forth, you know the general rule
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK, SuperHeating, Banking;
	}	
	//-------------------------------------------------------------

	//Check Method---------------------------------------------------
	private boolean InventoryOre(){
		if(Inventory.find(Items).length > 0)
			return true;
		return false;
	}
	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	boolean Banking(){
		RSObject [] banks = Objects.findNearest(5, bank);
		GameTab.open(GameTab.TABS.INVENTORY);
		if(banks != null){
			while(!Banking.isBankScreenOpen()){
				if(banks[0].click(BankT))
					break;
				else if(banks[0].click("Cancel"))
					return false;
				
			}
		}
		sleep(2000);
		Banking.depositAllExcept(RUNE);
		sleep(500);
	
		withdrawItem(RUNE, "30");
		withdrawItem(Items, "All");
		while(Banking.isBankScreenOpen())
			Banking.close();
		return true;
	}
	public boolean Superheating(){
		RSItem [] Jewels = Inventory.find(Items);
		RSItem [] Nature = Inventory.find(RUNE);
		GameTab.open(GameTab.TABS.MAGIC);
		RSInterfaceChild SuperH = Interfaces.get(192, CastWhat);
		if(Nature.length > 0 && Jewels.length > 0)
			if(SuperH.click("Cast")){
				if(Inventory.open()){
					Jewels[0].click("Cast");
					sleep(1000);
					return true;
				}
			}
			else if(SuperH.click("Cancel"))
				return true;

		return false;
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
		sleep(500);
	}
	private void TurnCamera(RSObject EX){
		Camera.setCameraAngle(0);
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


} // END OF SCRIPT