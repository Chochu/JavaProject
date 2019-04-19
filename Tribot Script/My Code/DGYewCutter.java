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
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSGroundItem;
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


@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG Rim Yew Cutter", description = 
"Cut Yew At Rimmingtion Deposit With DR" +
		"\n Update Log out error, Fix Pick up nest" +
		"", version = 1.2)


public class DGYewCutter extends Script implements Painting {

	//Variable---------------------------------------------------
	YewCut g1 = new YewCut();
	private STATE SS = getState();
	private final long startTime = System.currentTimeMillis(); //timer
	private int START_XP = Skills.getXP("Woodcutting");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Woodcutting");
	private int lvl = Skills.getCurrentLevel("Woodcutting");
	String sa; //Some String, I use it to store my player's action
	//any other variable that you want to use

	//-------------------------------------------------------------
	private boolean USETAB = false;
	private int AIRR = 556;
	private int LAWR = 557;
	private int EARTHR = 563;
	private int TAB = 8013;
	private int YEWLOGS = 1515; // a array of ID
	private int YEW = Integer.parseInt(JOptionPane.showInputDialog("Yew Log ID ", "18971"));
	char ctrl = KeyEvent.VK_CONTROL;
	private int [] AXES = {6739,1359};
	private int CHEST = 4483;
	private int PORTALIN = 4525;
	private int PORTALOUT = 15478;
	private int amount  = 0;
	private int count = Inventory.getCount(YEWLOGS);
	private final RSTile ToTree[] = {
			new RSTile(2955, 3224, 0), new RSTile(2953, 3227, 0), new RSTile(2939, 3231, 0) };
	int DUELRING[] = {
			2552,2554,2556,2558,2560,2562,2564,2566
	};
	int AXESnRING[] = {
			2552,2554,2556,2558,2560,2562,2564,2566,6739,1359
	};

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

		g.drawString("DG Yew Cutter", 280, 218);
		g.setFont(font1);
		g.setColor(color2);
		g.drawString("Status", 254, 245);
		g.drawString("Exp Gain", 254, 265);
		g.drawString("Wood Cut", 254, 285);
		g.drawString("Time", 254, 305);
		g.drawString( sa, 310, 245);
		g.drawString("" + ((START_XP) - (CURRENT_XP)), 310, 265);
		g.drawString("" + ((START_XP) - (CURRENT_XP))/175, 310, 285);
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 305);
		g.drawString("Log Per Hour  " + perHour(((START_XP) - (CURRENT_XP))/175), 254, 315);
	}
	//END: Code generated using Enfilade's Easel
	//Main Loop----------------------------------------------------

	@Override
	public void run() {


		println("The Bot has run");
		g1.setVisible(true);

		while(g1.isVisible())
			sleep(500);

		while(true){
			AmountCut();
			Walking.control_click = true;
			START_XP = Skills.getXP("Woodcutting");
			XP_TILL = Skills.getPercentToNextLevel("Woodcutting");
			lvl = Skills.getCurrentLevel("Woodcutting");
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
			case TeleportingHome:
				sa = "Teleporting home";
				TeleportingHome();
				break;
			case ExitHouse:
				sa = "Exiting Player House";
				ExitHouse();
				break;
			case WalkingToTree:
				sa = "Walking to Yew Location";
				WalkingToTree();
				break;
			case ChopTree:
				sa = "Looking for tree";
				ChopTree();
				break;
			case TeleportingCW:
				sa = "Teleporting to Castle War";
				TeleportingCW();
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
		if(IsInCombat())
			return STATE.TeleportingCW;
		if(Objects.find(10, 1124).length > 0){
			return STATE.AtLum;
		}
		else{
			if(AtCastle() && (HaveLog() || !HaveTab())){
				if(!NearChest())
					return STATE.WalkingToChest;
				if(NearChest() && !BankScreenIsOpen())
					return STATE.opening;
			}
			if(BankScreenIsOpen()){
				return STATE.Banking;
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
			if(AtTreeLocation() && !AtPortalOut()){
				if(!Inventory.isFull())
					return STATE.ChopTree;
				if(Inventory.isFull())
					return STATE.TeleportingCW;
			}
		}
		return STATE.CHECK; //default return
	}

	public enum STATE { 
		CHECK, WalkingToChest,AtLum,
		TeleportingHome, ExitHouse, WalkingToTree, TeleportingCW, ChopTree, Banking, 
		opening;


	}	
	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	public int perHour(int gained) {
		return ((int) ((gained) * 3600000D / (System.currentTimeMillis() - startTime)));
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
	int AmountCut(){
		if(Inventory.find(YEWLOGS).length != count){
			amount = Inventory.find(YEWLOGS).length - count;
			count = Inventory.find(YEWLOGS).length;
		}
		return amount;
	}
	boolean Banking(){
		while(org.tribot.api2007.Banking.isPinScreenOpen())
			org.tribot.api.Banking.inPin();
		while(Banking.isPinScreenOpen())
			Banking.inPin();
		Banking.depositAllExcept(AXESnRING);
		count = 0;
		sleep(2000);
		if(USETAB == true){
			withdrawItem(TAB, "1");
		}
		if(USETAB == false){
			withdrawItem(AIRR, "1");
			withdrawItem(EARTHR, "1");
			withdrawItem(LAWR, "1");
		}
		if(!getEquipment()){
			withdrawItem(2552, "1");
		}
		while(Banking.isBankScreenOpen())
			Banking.close();
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
		sleep(500);
		RSItem [] ring = Inventory.find(DUELRING);
		if(ring.length > 0)
			if(ring[0] != null){
				ring[0].click("Rub");
				sleep(750);
				return true;
			}
		return false;
	}
	boolean ChopTree(){
		RSObject [] flower = Objects.findNearest(35, 1189);
		GameTab.open(GameTab.TABS.INVENTORY);
		while(!Inventory.isFull() && AtTreeLocation() && !IsInCombat()){
			RSObject [] Tree = Objects.find(20, YEW);
			if(Tree.length > 0)
				if(Tree[0] != null){
					if(!Tree[0].isOnScreen()){
						Walking.walkTo(Tree[0].getPosition());
						if(!Tree[0].isOnScreen()){
							camera(Tree[0]);
							sleep(1000);
						}
					}
					sa = "Choping";
					Tree[0].click("Chop down");
					SleepTill();
					break;
				}
			if(Tree.length == 0){
				try{
					Walking.walkTo(flower[0].getPosition());
				}
				catch(ArrayIndexOutOfBoundsException e){
					return false;
				}
			}
		}
		return false;
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
			println("Runing");
			RSObject[] Portal = Objects.find(10, PORTALIN);
			Portal[0].click("Enter");
			sleep(1000,2000);
		}
	}
	boolean WalkingToTree(){
		cameras();
		if(Objects.find(5, PORTALOUT).length > 0){

			Walking.walkPath(ToTree);
			Walking.walking_timeout = 1;

			return true;
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

	private boolean birdNest()
	{
		RSGroundItem Nests[] = GroundItems.find(new int[] {5073,5074});
		if(Nests.length > 0)
		{
			if(!Inventory.isFull())
				Nests[0].click(new String[] {"Take"});
			sleep(450, 700);
			return true;
		} else
		{
			return false;
		}
	}
	//-------------------------------------------------------------
	//Check Method---------------------------------------------------
	private boolean cuttingent(){
		RSObject Ent[] = Objects.findNearest(10, "Ent");
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
		if(Player.getPosition().distanceTo(new RSTile(2938,3230)) < 8)
			return true;
		return false;
	}
	private boolean AtPortalOut(){
		if(Objects.find(8, PORTALOUT).length > 0)
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
		cuttingent();	
		birdNest();
		while(Player.getAnimation() != -1 && !IsInCombat()){
			START_XP = Skills.getXP("Woodcutting");
			XP_TILL = Skills.getPercentToNextLevel("Woodcutting");
			lvl = Skills.getCurrentLevel("Woodcutting");
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

	/*
	 * To change this template, choose Tools | Templates
	 * and open the template in the editor.
	 */

	/**
	 *
	 * @author John
	 */
	class YewCut extends javax.swing.JFrame {

		/**
		 * Creates new form YewCut
		 */
		public YewCut() {
			initComponents();
		}

		/**
		 * This method is called from within the constructor to initialize the form.
		 * WARNING: Do NOT modify this code. The content of this method is always
		 * regenerated by the Form Editor.
		 */
		@SuppressWarnings("unchecked")
		// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
		private void initComponents() {

			jPanel1 = new javax.swing.JPanel();
			tab = new javax.swing.JCheckBox();
			jButton1 = new javax.swing.JButton();
			jLabel1 = new javax.swing.JLabel();

			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

			tab.setText("Use Tab");

			jButton1.setText("Start");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});

			javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout.setHorizontalGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel1Layout.createSequentialGroup()
							.addGap(24, 24, 24)
							.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
									.addComponent(jButton1)
									.addComponent(tab))
									.addContainerGap(23, Short.MAX_VALUE))
					);
			jPanel1Layout.setVerticalGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel1Layout.createSequentialGroup()
							.addGap(20, 20, 20)
							.addComponent(tab)
							.addGap(18, 18, 18)
							.addComponent(jButton1)
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					);

			jLabel1.setText("DG Yew Cutter");

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addContainerGap())
							.addGroup(layout.createSequentialGroup()
									.addGap(29, 29, 29)
									.addComponent(jLabel1)
									.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					);
			layout.setVerticalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(jLabel1)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addContainerGap())
					);

			pack();
		}// </editor-fold>                        

		private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
			if(tab.isSelected()){
				USETAB = true;
			}
			g1.setVisible(false);
		}                                        

		/**
		 * @param args the command line arguments
		 */
		// Variables declaration - do not modify                     
		private javax.swing.JButton jButton1;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JPanel jPanel1;
		private javax.swing.JCheckBox tab;
		// End of variables declaration                   
	}

} // END OF SCRIPT