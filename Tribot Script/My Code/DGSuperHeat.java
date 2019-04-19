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
import scripts.DGNatureCrafter.NewJFrame;
@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG Super Heat", description = "Super Heat", version = 1.0)


public class DGSuperHeat extends Script implements Painting {

	//Variable---------------------------------------------------
	DGSuperHeatGUI g1 = new DGSuperHeatGUI();
	private STATE SS = getState();
	private  int COAL = 453, COALNUM = 0; // a array of ID
	private  int ORE = 0, ORENUM = 0;
	private  int NAT = 561; // a single ID
	private  int BANKs = 0, MOUSE = 0;
	private String BANKT = "";
	private int [] ID = {NAT, COAL};
	private int START_XP = Skills.getXP("Magic");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Magic");
	private int lvl = Skills.getCurrentLevel("Magic");
	private int START_XPs = Skills.getXP("Smithing");
	private int CURRENT_XPs = this.START_XPs;
	private int XP_TILLs = Skills.getPercentToNextLevel("Smithing");
	private int lvls = Skills.getCurrentLevel("Smithing");
	private int amount = 53;

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

		g.drawString("DG Iron Super heat", 280, 218);
		g.setFont(font1);
		g.setColor(color2);
		g.drawString("Status", 254, 245);
		g.drawString("Exp Gain", 254, 265);
		g.drawString("Cast", 254, 285);
		g.drawString("Time", 254, 305);
		g.drawString( sa, 310, 245);
		g.drawString("" + ((START_XP) - (CURRENT_XP)) + " Magic Exp", 310, 260);
		g.drawString("" + ((START_XPs) - (CURRENT_XPs)) + " Smelting Exp", 310, 270);
		g.drawString(""+ ((START_XP) - (CURRENT_XP))/amount, 310, 285);
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 305);
	}

	@Override
	public void run() {
		println("The Bot has run");

		g1.setVisible(true);

		while(g1.isVisible())
			sleep(500);

		while(true){
			START_XP = Skills.getXP("Magic");
			XP_TILL = Skills.getPercentToNextLevel("Magic");
			lvl = Skills.getCurrentLevel("Magic");
			START_XPs = Skills.getXP("Smithing");
			XP_TILLs = Skills.getPercentToNextLevel("Smithing");
			lvls = Skills.getCurrentLevel("Smithing");
			Mouse.setSpeed(MOUSE); //this is mouse speed, not necessay 
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable

			switch(SS){ //loop through the enum array and check what state does it return
			case CHECK: //this is default return of getState method
				break;
			case SuperHeating:
				sa = "Super Heating";
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
		if(Inventory.find(ORE).length > 0)
			return true;
		return false;
	}
	//Any other thing you check, such as near this or have this thing on inventory		

	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	boolean Banking(){
		try{
		RSObject [] bank = Objects.findNearest(5, BANKs);
		GameTab.open(GameTab.TABS.INVENTORY);
		if(bank != null){
			while(!Banking.isBankScreenOpen()){
				if(bank[0].click(BANKT))
					break;
				else if(bank[0].click("Cancel"))
					return false;
			}
		}
		sleep(2000);
		Banking.depositAllExcept(ID);
		sleep(1000l);
		if(ORENUM != 0)
			Banking.withdraw(ORENUM, ORE);
		//withdrawItem(ORE,"ORENUM");
		sleep(1000l);
		if(COALNUM != 0)
			Banking.withdraw(COALNUM, COAL);
		//withdrawItem(COAL,"COALNUM");
		while(Banking.isBankScreenOpen())
			Banking.close();
		return true;
		}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	public boolean Superheating(){
		RSItem [] ore = Inventory.find(ORE);
		RSItem [] Nature = Inventory.find(NAT);
		GameTab.open(GameTab.TABS.MAGIC);
		RSInterfaceChild SuperH = Interfaces.get(192, 25);
		if(Nature.length > 0 && ore.length > 0)
			if(SuperH.click("Cast")){
				if(Inventory.open()){
					ore[0].click("Cast");
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

	/*
	 * To change this template, choose Tools | Templates
	 * and open the template in the editor.
	 */

	/**
	 *
	 * @author John
	 */
	class DGSuperHeatGUI extends javax.swing.JFrame {

		/**
		 * Creates new form DGSuperHeat
		 */
		public DGSuperHeatGUI() {
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

			jLabel2 = new javax.swing.JLabel();
			jPanel1 = new javax.swing.JPanel();
			jButton1 = new javax.swing.JButton();
			jLabel1 = new javax.swing.JLabel();
			jLabel3 = new javax.swing.JLabel();
			CNUM = new javax.swing.JTextField();
			jLabel4 = new javax.swing.JLabel();
			OID = new javax.swing.JTextField();
			jLabel5 = new javax.swing.JLabel();
			ONUM = new javax.swing.JTextField();
			jLabel6 = new javax.swing.JLabel();
			BANKID = new javax.swing.JTextField();
			jLabel7 = new javax.swing.JLabel();
			jTextField1 = new javax.swing.JTextField();
			jLabel8 = new javax.swing.JLabel();
			jLabel9 = new javax.swing.JLabel();
			jLabel10 = new javax.swing.JLabel();
			jTextField2 = new javax.swing.JTextField();
			jButton2 = new javax.swing.JButton();
			jLabel11 = new javax.swing.JLabel();
			O1ID = new javax.swing.JTextField();

			jLabel2.setText("jLabel2");

			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

			jButton1.setText("Start");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});

			jLabel3.setText("Number of Ore One[Pull this 0, if Doing Gold/Iron");

			jLabel4.setText("ID of Ore 2 [When this Ore Run \n out it will Bank]");

			jLabel5.setText("Number of Ore Withdraw");

			jLabel6.setText("Bank ID");

			jLabel7.setText("Click Option");

			jLabel8.setText("DG AIO SuperHeat");

			jLabel9.setText("Mouse Speed");

			jButton2.setText("Closed Bot");
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jButton2ActionPerformed(evt);
				}
			});

			jLabel11.setText("ID of Ore 1[Pull 1 if you are gold/iron]");

			javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout.setHorizontalGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel1Layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
									.addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
									.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
											.addGroup(jPanel1Layout.createSequentialGroup()
													.addGap(129, 129, 129)
													.addComponent(jLabel8))
													.addGroup(jPanel1Layout.createSequentialGroup()
															.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																	.addGroup(jPanel1Layout.createSequentialGroup()
																			.addGap(129, 129, 129)
																			.addComponent(jLabel1))
																			.addComponent(jLabel4)
																			.addComponent(jLabel6)
																			.addComponent(jLabel7)
																			.addComponent(jLabel9)
																			.addComponent(jLabel11))
																			.addGap(34, 34, 34)
																			.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
																					.addComponent(OID)
																					.addComponent(BANKID)
																					.addComponent(jTextField1)
																					.addGroup(jPanel1Layout.createSequentialGroup()
																							.addGap(10, 10, 10)
																							.addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
																							.addComponent(jTextField2)
																							.addComponent(O1ID)))))
																							.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																									.addGroup(jPanel1Layout.createSequentialGroup()
																											.addGap(31, 31, 31)
																											.addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																											.addContainerGap())
																											.addGroup(jPanel1Layout.createSequentialGroup()
																													.addGap(16, 16, 16)
																													.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
																															.addGroup(jPanel1Layout.createSequentialGroup()
																																	.addComponent(jLabel5)
																																	.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																																	.addComponent(ONUM, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
																																	.addGroup(jPanel1Layout.createSequentialGroup()
																																			.addComponent(jLabel3)
																																			.addGap(52, 52, 52)
																																			.addComponent(CNUM, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
																																			.addContainerGap(61, Short.MAX_VALUE))))
					);
			jPanel1Layout.setVerticalGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
							.addComponent(jLabel8)
							.addGap(9, 9, 9)
							.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(jLabel1)
									.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
											.addComponent(jLabel3)
											.addComponent(CNUM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
											.addComponent(jLabel11)
											.addComponent(O1ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
											.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
											.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
													.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(jLabel4)
															.addComponent(OID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
															.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																	.addComponent(ONUM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																	.addComponent(jLabel5)))
																	.addGap(42, 42, 42)
																	.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																			.addComponent(jLabel6)
																			.addComponent(BANKID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																			.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																			.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																					.addComponent(jLabel7)
																					.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																					.addGap(18, 18, 18)
																					.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																							.addComponent(jLabel9)
																							.addGroup(jPanel1Layout.createSequentialGroup()
																									.addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																									.addGap(18, 18, 18)
																									.addComponent(jLabel10)))
																									.addGap(18, 18, 18)
																									.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																											.addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
																											.addComponent(jButton2)))
					);

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addContainerGap())
					);
			layout.setVerticalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addGap(20, 20, 20)
							.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addContainerGap())
					);

			pack();
		}// </editor-fold>                        

		private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {   
			COAL = Integer.parseInt(O1ID.getText());
			COALNUM =Integer.parseInt(CNUM.getText());  // a array of ID
			ORE =Integer.parseInt(OID.getText()); 
			ORENUM =Integer.parseInt(ONUM.getText()); 
			BANKs =Integer.parseInt(BANKID.getText()); 
			BANKT = jTextField1.getText();
			MOUSE = Integer.parseInt(jTextField2.getText()); 
			g1.setVisible(false);
		}                                                       

		private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
			g1.setVisible(false);
			stopScript();
		}                                        

		/**
		 * @param args the command line arguments
		 */

		// Variables declaration - do not modify                     
		private javax.swing.JTextField BANKID;
		private javax.swing.JTextField CNUM;
		private javax.swing.JTextField O1ID;
		private javax.swing.JTextField OID;
		private javax.swing.JTextField ONUM;
		private javax.swing.JButton jButton1;
		private javax.swing.JButton jButton2;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel10;
		private javax.swing.JLabel jLabel11;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JLabel jLabel3;
		private javax.swing.JLabel jLabel4;
		private javax.swing.JLabel jLabel5;
		private javax.swing.JLabel jLabel6;
		private javax.swing.JLabel jLabel7;
		private javax.swing.JLabel jLabel8;
		private javax.swing.JLabel jLabel9;
		private javax.swing.JPanel jPanel1;
		private javax.swing.JTextField jTextField1;
		private javax.swing.JTextField jTextField2;
		// End of variables declaration                   
	}


} // END OF SCRIPT