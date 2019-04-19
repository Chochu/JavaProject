package scripts;

import java.awt.Point;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;

import java.awt.Color; //to get different colors
import java.awt.Dimension;
import java.awt.Font; //to change font
import java.awt.Graphics; //paint
import java.awt.Graphics2D; //needed for the image
import java.awt.Image; //same as above
import java.awt.Toolkit;
import java.io.IOException; //this is needed for the loading of the image
import java.net.URL; //same as above

import javax.imageio.ImageIO; //same as above

import org.tribot.api.Timing; //to calculate time things
import org.tribot.api2007.Skills; //to get XP/levels
import org.tribot.script.interfaces.Painting; //for onPaint()

import java.awt.*;

import javax.imageio.ImageIO;

import java.io.IOException;
import java.net.URL;  

@ScriptManifest(authors = { "Cyph" }, category = "Thieving", name = "Cyph's CakeStealer", description="<h2>Cyph's CakeStealer</h2><br><h3>Steals Cakes in Ardougne and banks them</h3><br><font size ='20'color='red'>Start at the <b>east</b> cake stall in ardougne</font>")
public class CyphCakeStealer extends Script implements Painting {
	public enum State {
		STEAL_CAKE, BANK, WALKING;
	}
	String STRIN;
	private State SCRIPT_STATE; 
	private final static int STALLID = 550;
	private final static int BANKID = 2196;
	private final static int CAKEID = 1891;
	private final static int BREADID = 2309;
	private final static int CHOCID = 1901;
	public RSObject[] Stall = Objects.findNearest(200, STALLID);
	public final RSTile StallLoc = new RSTile(2668, 3312, 0);
	public final RSTile BankLoc = new RSTile(2653, 3284, 0);
	public final RSTile RUNSPOT = new RSTile(2672, 3311, 0) ;
	public RSObject[] Bank = Objects.findNearest(200, BANKID);
	public  RSItem[] f = Inventory.find(BREADID,CHOCID);
	private final Color color1 = new Color(255, 102, 0);
	private final Color color2 = new Color(0, 0, 0);
	private final BasicStroke stroke1 = new BasicStroke(1);
	private final Font font1 = new Font("Arial", 1, 14);
	private final Font font2 = new Font("Arial", 0, 10);
	private final Image img1 = getImage("http://images4.wikia.nocookie.net/__cb20090119154626/runescape/nl/images/f/f0/Cake.png");
	final int startexp = Skills.getXP("Thieving");
	final int startlevel = Skills.getActualLevel("Thieving");
	long starttime = System.currentTimeMillis();
	public int HPEat;
	public boolean GUI_COMPLETE = false;

	@Override
	public void run() {
		try{

			myForm GUI = new myForm(); 

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int screenW = screenSize.width;
			int screenH = screenSize.height;

			Dimension dim = GUI.getSize();


			GUI.setVisible(true);
			println("GUI Opened");
			GUI.setLocation((screenW / 2) - (dim.width / 2), (screenH / 2) - (dim.height /2)) ;

			while (!GUI_COMPLETE){
				sleep (200);
			}

			GUI.setVisible(false);
			println("GUI Complete! Have fun");
			println("HP TO EAT:"+HPEat);


			this.starttime = 0;
			Mouse.setSpeed(General.random(150, 175));
			while (true) {

				SCRIPT_STATE = getState();

				switch (SCRIPT_STATE) {
				case STEAL_CAKE:
					stealCake();
					STRIN = "Stealing Cake";
					break;

				case BANK:
					bank();
					STRIN = "Banking";
					break;

				case WALKING:
					walk();
					STRIN = "Walking";
					break;

				default: 
					return ;
				}
				sleep(20, 40);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}


	@Override
	public void onPaint(Graphics g1) {
		long runtime = System.currentTimeMillis() - this.starttime;
		int xpGained = Skills.getXP("Thieving") - this.startexp;
		int xpPerHour = (int)(xpGained / (runtime / 3600000.0D));
		int currentlevel = Skills.getCurrentLevel("Thieving");
		int levelsgained = currentlevel - this.startlevel;
		Graphics2D g = (Graphics2D)g1;
		g.setColor(color1);
		g.fillRoundRect(233, 346, 260, 79, 16, 16);
		g.setColor(color2);
		g.setStroke(stroke1);
		g.drawRoundRect(233, 346, 260, 79, 16, 16);
		g.setFont(font1);
		g.drawString("Cyph's Cake Stealer", 300, 361);
		g.setFont(font2);
		g.drawString("Time Running:"+Timing.msToString(runtime), 244, 376);
		g.drawString("Xp/Hour:"+xpPerHour, 244, 389);
		g.drawString("Exp Gained:"+xpGained, 244, 402);
		g.drawString("Levels Gained:"+levelsgained + " ("+currentlevel + ")", 244, 415);
		g.drawString(STRIN, 244, 430);
		g.drawImage(img1, 226, 341, null);
		g.drawImage(img1, 472, 341, null);
		
		

	}
	public State getState() {
		if (fullinven()) {
			return State.BANK;
		}
		if (!fullinven()) {
			if (inSpot()) {
				if (stallsThere()) {
					if(incombat()){
						return State.STEAL_CAKE;
					}
				}
			}			
			if(!inSpot()){
				return State.WALKING;
			}
		}
		return null;
	}


	private void eatFood() {
		RSItem[] f = Inventory.find(BREADID,CHOCID);
		if (f.length > 0) {
			GameTab.open(GameTab.TABS.INVENTORY);
			f[0].click("Eat", new Point(1, -1), new Point(1, -1));
			sleep(600, 800);
		}

	}

	private void walk() {
		if(Stall[0].isOnScreen()){
			Camera.turnToTile(StallLoc);
			DynamicClicking.clickRSTile(StallLoc, "Walk here");
			sleep(800);
		}else{
			Mouse.setSpeed(General.random(250, 300));
			PathFinding.aStarWalk(StallLoc);
			sleep(800);
		}
	}


	public boolean stallsThere() {
		RSObject[] Stall = Objects.findNearest(20, STALLID);
		if(Stall.length > 0){
			return true;
		}		
		return false;
	}


	private boolean fullinven(){
		if (Inventory.isFull()){
			RSItem[] f = Inventory.find(BREADID,CHOCID);
			if (f.length > 0) {
				GameTab.open(GameTab.TABS.INVENTORY);
				f[0].click("Eat", new Point(1, -1), new Point(1, -1));
				sleep(300);}
			else if (Inventory.isFull()) {

				return true;
			}
		}
		return false;

	}

	public boolean inSpot() {
		if (Player.getPosition().getX() == 2668 && Player.getPosition().getY() == 3312) {
			return true;

		}
		return false;
	}


	public void stealCake() {
		if(Player.getAnimation() == -1 && !Player.getRSPlayer().isInCombat()){
			Mouse.setSpeed(General.random(140, 165));
			Stall[0].click("Steal-From", null,  new Point(0,30));
			println("stealking");

			Mouse.setSpeed(General.random(150, 175));
			sleep(300);}
	}


	private boolean incombat() {
		if(Skills.getCurrentLevel("Hitpoints") <= HPEat){eatFood();}
		if(Player.getRSPlayer().isInCombat()){
			PathFinding.aStarWalk(RUNSPOT);
			sleep(2000);
		}
		return true;
	}

	public void bank() {
		if(Bank.length > 0)
			Camera.turnToTile(BankLoc);
		if(Bank[0].isOnScreen()){
			DynamicClicking.clickRSObject(Bank[0], "Bank Booth");
			sleep(300);
		}
		else{
			Mouse.setSpeed(General.random(250, 300));
			PathFinding.aStarWalk(BankLoc);}
		while(Banking.isPinScreenOpen())
			Banking.inPin();
		if(Banking.isBankScreenOpen()){
			Banking.depositAll();
			Banking.close();
		}
	}

	//START: Code generated using Enfilade's Easel
	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch(IOException e) {
			return null;
		}
	}

	/**
	 *
	 * @author Cyph
	 */
	@SuppressWarnings("serial")
	class myForm extends javax.swing.JFrame {

		/**
		 * Creates new form myform
		 */
		public myForm() {
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
			HPBox = new javax.swing.JTextField();
			jLabel2 = new javax.swing.JLabel();
			Startbutton = new javax.swing.JButton();

			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

			jLabel1.setText("Cyph's Cake Stealer");
			jLabel1.setName(""); // NOI18N

			HPBox.setText("9");

			jLabel2.setText("Eat on HP:");

			Startbutton.setText("Start");
			Startbutton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					StartbuttonActionPerformed(evt);
				}
			});

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addContainerGap(63, Short.MAX_VALUE)
							.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(30, 30, 30))
							.addGroup(layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
									.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
											.addComponent(Startbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(HPBox))
											.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					);
			layout.setVerticalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
									.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
									.addComponent(HPBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
									.addGap(18, 18, 18)
									.addComponent(Startbutton, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
									.addGap(21, 21, 21))
					);

			pack();
		}// </editor-fold>                        

		private void StartbuttonActionPerformed(java.awt.event.ActionEvent evt) {                                            
			GUI_COMPLETE = true;

			CyphCakeStealer.this.HPEat = Integer.parseInt(this.HPBox.getText());
		}                                           


		// Variables declaration - do not modify                     
		private javax.swing.JTextField HPBox;
		private javax.swing.JButton Startbutton;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel2;
		// End of variables declaration                   
	}

}