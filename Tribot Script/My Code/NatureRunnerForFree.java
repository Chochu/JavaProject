
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
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.EnumScript;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.NatureRunnerForFree.NewJFrame;
import scripts.NatureRunnerForFree.STATE;


@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG Free Loaders", description = "Buy and Sell Ess and make Nat Rune \n 2.0 Rework the whole scrip /nt" +
		"2.1 Fixed Getting Stuck at Atlar\n" +
		"2.2 fixed Walking path\n" +
		"2.3 Fix ID and Walkpath\n 2.4 Fix Exiting Portal",version = 2.4)


public class NatureRunnerForFree extends Script implements Painting {

	//------------Object----------------
	NewJFrame g1 = new NewJFrame();

	private static int MOUSESPEED = 0;
	private static boolean USESHORTCUT = false;
	private final int ID_ROCK = 19199;
	private final int ID_ALTAR = 10086;
	private final int ID_PORTAL = 10073;
	private final int ID_NESS = 7937;
	private final int ID_ESS = 7936;
	private final int NPC = 3852;
	private int [] keep = {995,7937,561,7936};
	char ctrl = KeyEvent.VK_CONTROL;
	private STATE SS = getState();
	private int amount = Inventory.getCount(561);
	String sa;
	Random randomGenerator = new Random();
	private final long startTime = System.currentTimeMillis();
	private int START_XP = Skills.getXP("Runecrafting");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Runecrafting");
	private int lvl = Skills.getCurrentLevel("Runecrafting");
	//------------location--------------
	final RSTile ATOS[] ={
			new RSTile(2867,3020),
			new RSTile(2862,3023),
			new RSTile(2857,3027),
			new RSTile(2853,3033),
			new RSTile(2853,3037),
			new RSTile(2851,3044),
			new RSTile(2841,3046),
			new RSTile(2837,3052),
			new RSTile(2833,3058),
			new RSTile(2828,3061),
			new RSTile(2826,3071),
			new RSTile(2830,3072),
			new RSTile(2830,3083),
			new RSTile(2830,3091),
			new RSTile(2827,3100),
			new RSTile(2818,3108),
			new RSTile(2813,3117),
			new RSTile(2806,3127),
			new RSTile(2794,3126),
			new RSTile(2785,3131),
			new RSTile(2774,3129),
			new RSTile(2769,3124)};
	final RSTile STOA[] = {
			new RSTile(2771,3125),
			new RSTile(2780,3127),
			new RSTile(2787,3127),
			new RSTile(2792,3128),
			new RSTile(2798,3126),
			new RSTile(2806,3126),
			new RSTile(2811,3121),
			new RSTile(2817,3110),
			new RSTile(2827,3101),
			new RSTile(2828,3092),
			new RSTile(2831,3086),
			new RSTile(2830,3079),
			new RSTile(2827,3073),
			new RSTile(2828,3068),
			new RSTile(2829,3061),
			new RSTile(2833,3054),
			new RSTile(2842,3047),
			new RSTile(2850,3040),
			new RSTile(2854,3032),
			new RSTile(2862,3024),
			new RSTile(2866,3019)
	};
	final RSTile STOAs[] = {
			new RSTile(2766, 3120), new RSTile(2772, 3113), new RSTile(2777, 3106), new RSTile(2783, 3101), new RSTile(2788, 3096), new RSTile(2789, 3090), new RSTile(2797, 3087), new RSTile(2804, 3082), new RSTile(2812, 3077), new RSTile(2819, 3070), 
			new RSTile(2825, 3064), new RSTile(2832, 3058), new RSTile(2836, 3050), new RSTile(2842, 3047), new RSTile(2849, 3037), new RSTile(2856, 3033), new RSTile(2863, 3027), new RSTile(2866, 3021)
	};
	final RSTile ATOSs[] = {
			new RSTile(2865, 3022), new RSTile(2858, 3028), new RSTile(2852, 3034), new RSTile(2845, 3034), new RSTile(2835, 3043), new RSTile(2832, 3052), new RSTile(2829, 3059), new RSTile(2825, 3064), new RSTile(2820, 3069), new RSTile(2816, 3072), 
			new RSTile(2810, 3075), new RSTile(2805, 3077), new RSTile(2800, 3082), new RSTile(2796, 3085), new RSTile(2791, 3089), new RSTile(2786, 3097), new RSTile(2778, 3106), new RSTile(2772, 3113), new RSTile(2768, 3120)
	};
	//---------------------------------------------------------------
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

		g.drawString("DG Nature Runner", 280, 218);
		g.setFont(font1);
		g.setColor(color2);
		g.drawString("Status", 254, 245);
		g.drawString("Exp Gain", 254, 265);
		g.drawString("Rune Craft", 254, 285);
		g.drawString("Time", 254, 305);
		g.drawString( sa, 310, 245);
		g.drawString("" + ((START_XP) - (CURRENT_XP)), 310, 265);
		g.drawString(""+ (Inventory.getCount(561)-amount), 310, 285);
		g.drawString(Timing.msToString(Timing.timeFromMark(this.startTime)), 310, 305);
	}
	//END: Code generated using Enfilade's Easel
	//--------------------------------------------------------------------------------------------------------------------------

	@Override
	public void run() {
		g1.setVisible(true);

		while(g1.isVisible())
			sleep(500);
		while(true){
			Inventory.dropAllExcept(keep);
			START_XP = Skills.getXP("Runecrafting");
			XP_TILL = Skills.getPercentToNextLevel("Runecrafting");
			lvl = Skills.getCurrentLevel("Runecrafting");
			Mouse.setSpeed(MOUSESPEED);	
			Walking.control_click = true;
			int randomInt = randomGenerator.nextInt(360);

			SS = getState();	
			switch(SS){
			case CHECK:
				break;
			case enterrock:
				sa = "Entering Rock";
				enterrock();
				sleeps();
				break;
			case runes:
				sa = "Crafting Rune";
				runes();
				sleeps();
				break;
			case enterportal:
				sleeps();
				sa = "Entering Portal";
				enterportal();
				sleeps();
				break;
			case trade:
				sa = "Trading NPC";
				store();
				sleep(20,150);
				break;
			case buyandsell:
				while(!haveess() && shopInterfaceOpen()){
					if(storeess()){
						sa = "Buying";
						buy(10);
					}
					else{
						if((!in() && !storeess()) && Inventory.find(ID_NESS).length != 0) {
							sa = "Selling";
							if(Inventory.find(ID_ESS).length < 25 && Inventory.find(ID_ESS).length >= 20){
								println("Selling 5");
								sell(5);
							}
							else{
								println("Selling 10");
								sell(10);
							}
						}

						if(in()){
							SS = getState();
							break;
						}
						buy(10);
					}
				}
				sleep(100);

			case walktorock:
				sa = "walking to rock";
				walktorock();
				break;
			case walktostore:
				sa = "walking to store";
				walktostore();
				break;
			case danger:
				sa = "Low on HP, Teleporting";
				danger();
			default:
				break;
			}
		}

	}

	public enum STATE {
		enterrock, CHECK, walktorock, makerune, walktostore, trade, runes, enterportal, buyandsell, danger

	}
	//--------------------GET STATE---------------------------------
	private STATE getState(){
		if(haveess()){
			if(!ataltar() && !atrock()){
				return STATE.walktorock;

			}
			if(atrock()){
				return STATE.enterrock;
			}
			if(ataltar()){
				return STATE.runes;
			}	
		}
		if(!haveess()){
			if(ataltar()){
				return STATE.enterportal;
			}
			if(!atstore()){
				return STATE.walktostore;
			}
			if(atstore()){
				if(shopInterfaceOpen()){
					buy(10);
					return STATE.buyandsell;
				}
				else{
					return STATE.trade;
				}
			}
		}
		if(Skills.getCurrentLevel("CONSTITUTION") < 6){
			return STATE.danger;
		}
		println("CHECK");
		return STATE.CHECK;

	}



	//---------------------AT LOCATION--------------------------------
	private boolean in(){
		if(Inventory.find(ID_ESS).length > 25){
			return true;
		}
		return false;
	}
	private boolean storeess(){
		RSItem[] items = Interfaces.get(300 , 75).getItems();
		for(int i = 0; i < items.length; i++)
			if(items[i].getID() == ID_ESS){
				return true;
			}
		return false;
	}
	private boolean haveess(){
		if(Inventory.find(ID_ESS).length < 25){
			return false;
		}
		return true;
	}
	private boolean atrock(){
		return Objects.find(15, ID_ROCK).length > 0;
	}
	private boolean atstore()
	{
		return NPCs.find(15, NPC).length > 0;
	}
	private boolean ataltar() {
		return Objects.find(40, ID_ALTAR).length > 0;
	}
	//-----------------------To Do Method-------------------------
	private boolean danger(){
		RSItem[] dan = Inventory.find(8009);
		if(Skills.getCurrentLevel("CONSTITUTION") < 10){
			dan[0].click("Break");
			return true;
		}
		return false;

	}	
	private boolean enterportal(){
		try{
		RSObject[] portal = Objects.find(15, ID_PORTAL);
		if(portal.length > 0)
			if(portal[0] != null)
				if(!portal[0].isOnScreen()){
					camera(portal[0]);
					if(!portal[0].isOnScreen())
						Walking.walkTo(portal[0].getPosition());
				}
		if(DynamicClicking.clickRSObject(portal[0], "Use")){
			sleep(500);
			while(Player.isMoving())
				sleep(50);
		}
		sleeps();
		return true;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
	}
	private boolean runes(){
		RSObject[] altar = Objects.find(15, ID_ALTAR);
		camera(altar[0]);
		if(!altar[0].isOnScreen())
			Walking.walkTo(altar[0].getPosition());
		if(DynamicClicking.clickRSObject(altar[0], "Craft-rune")){
			sleep(500);
			return true;
		}
		return false;
	}
	private void store(){
		GameTab.open(org.tribot.api2007.GameTab.TABS.INVENTORY);
		RSNPC[] jim = NPCs.findNearest(NPC);
		if(!jim[0].isOnScreen()){
			RSNPC target = jim[0];
			Camera.turnToTile(target.getPosition());
			Walking.walkTo(jim[0].getPosition());
			sleeps();
		}
		DynamicClicking.clickRSNPC(jim[0], "Trade");
	}


	public boolean shopInterfaceOpen()
	{
		if(Interfaces.get(300, 91) != null){
			return true;
		}
		return false;
	}


	private boolean sell(int x){
		Mouse.setSpeed(180);	
		RSItem[] ess = Inventory.find(ID_NESS);

		if(storeess())
			buy(10);
		else if(!storeess()){
			if(x == 10){
				
				ess[0].click("Sell 10");
				sleep(750);
				return true;
			}
			else if(x == 5){
				ess[0].click("Sell 5");
				sleep(1000);
				return true;
			}
		}
		return false;
	}
	private boolean buy(int x){
		RSItem[] items = Interfaces.get(300, 75).getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getID() == ID_ESS) {
				items[i].changeType(org.tribot.api2007.types.RSItem.TYPE.BANK);
				if(x == 10){
					items[i].click("Buy 10", null , new Point(0,30) );
					sleep(500);
					return true;
				}
			}
		}
		return false;
	}
	private boolean walktostore(){
		Cameraset();
		if(USESHORTCUT == true)
			if(Walking.walkPath(ATOSs)){
				return true;
			}
		if(USESHORTCUT == false)
			if(Walking.walkPath(ATOS))
				return true;
		return false;
	}

	private boolean walktorock(){
		if(USESHORTCUT == true)
			if(Walking.walkPath(STOAs))
				return true;
		if(USESHORTCUT == false)
			if(Walking.walkPath(STOA))
				return true;
		return false;
	}



	private boolean enterrock(){
		RSObject[] rock = Objects.findNearest(25, ID_ROCK);
		if(rock.length > 0)
			if(rock[0] != null){
				camera(rock[0]);
			//	if(DynamicClicking.clickRSObject(rock[0], "Enter")){
				if(rock[0].click("Enter")){
					sleeps();
					return true;
				}
			}
		sleeps();
		return false;
	}
	//-----------------------------------------------------------
	private void Cameraset(){
		Camera.setCameraAngle(100);
		Camera.setCameraRotation(0);
	}
	private void sleeps(){
		sleep(500);
		while(Player.getAnimation() != -1){
			sleep(50);
		}
		sleep(200,30);
	}
	private void camera(RSObject cam){
		RSObject target = cam;	
		Camera.turnToTile(target.getPosition());
	}
	//-------------------------------------
	/*
	 * To change this template, choose Tools | Templates
	 * and open the template in the editor.
	 */

	/**
	 *
	 * @author John
	 */
	class NewJFrame extends javax.swing.JFrame {

		/**
		 * Creates new form NewJFrame
		 */
		public NewJFrame() {
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

			jLabel1 = new javax.swing.JLabel();
			jPanel1 = new javax.swing.JPanel();
			jLabel2 = new javax.swing.JLabel();
			SPEED = new javax.swing.JTextField();
			shortcut = new javax.swing.JCheckBox();
			jButton1 = new javax.swing.JButton();

			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

			jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel1.setText("DG Nature Crafter");

			jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel2.setText("Mouse Speed");

			shortcut.setText("Use ShortCut");

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
							.addGap(26, 26, 26)
							.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(46, 46, 46)
							.addComponent(SPEED, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGroup(jPanel1Layout.createSequentialGroup()
									.addGap(46, 46, 46)
									.addComponent(shortcut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addContainerGap())
									.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
											.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(jButton1)
											.addGap(137, 137, 137))
					);
			jPanel1Layout.setVerticalGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(jPanel1Layout.createSequentialGroup()
							.addGap(22, 22, 22)
							.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(SPEED, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
									.addGap(18, 18, 18)
									.addComponent(shortcut)
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
									.addComponent(jButton1)
									.addGap(25, 25, 25))
					);

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(layout.createSequentialGroup()
											.addGap(82, 82, 82)
											.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
											.addGroup(layout.createSequentialGroup()
													.addContainerGap()
													.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
													.addContainerGap(48, Short.MAX_VALUE))
					);
			layout.setVerticalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(0, 25, Short.MAX_VALUE))
					);

			pack();
		}// </editor-fold>                        

		private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
			MOUSESPEED = Integer.parseInt(SPEED.getText());
			println("Mouse Speed Set to: " + MOUSESPEED);
			if(shortcut.isSelected()){
				USESHORTCUT = true;
				println("Using ShortCut");
			}
			g1.setVisible(false);
		}                                        

		// Variables declaration - do not modify                     
		private javax.swing.JTextField SPEED;
		private javax.swing.JButton jButton1;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JPanel jPanel1;
		private javax.swing.JCheckBox shortcut;
		// End of variables declaration                   
	}


}