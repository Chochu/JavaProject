package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import org.omg.PortableServer.POAManagerPackage.State;
import org.tribot.api.ChooseOption;
import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Screen;
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
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.EnumScript;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.DGNatureCrafter.NewJFrame;

@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", description = "Black Sheep Wall  - Starcraft", version = 1.0, name = "Herb Unid Reveal")


public class DGHerbUIden extends Script implements Painting {

	//Variable---------------------------------------------------

	private STATE SS = getState();
	static String message = "";
	static int delay = 1;
	herb g1 = new herb();
	boolean startingtext = false;
	private final long startTime = System.currentTimeMillis(); //timer
	String sa; //Some String, I use it to store my player's action
	//any other variable that you want to use

	//-------------------------------------------------------------

	//Main Loop----------------------------------------------------
	@Override
	public void onPaint(Graphics g) {
		g.setColor(Color.WHITE); //set color
		g.drawString(sa , 10, 70); //Explain of using the sa variable from before
		//Any other paint stuff goes here, such as Code generated using Enfilade's Easel
	}

	@Override
	public void run() {
		println("The Bot has run");
		while(true){
		g1.setVisible(true);
		}




	}

	//-------------------------------------------------------------


	//STATE--------------------------------------------------------
	private STATE getState(){

		//So on and so forth, you know the general rule
		return STATE.CHECK; //default return
	}
	public enum STATE { 
		CHECK;
	}	
	//-------------------------------------------------------------

	//Check Method---------------------------------------------------


	//-------------------------------------------------------------

	//DO METHOD--------------------------------------------------------
	void getid(){

		getOffer();

	}
	private RSItem[] getOffer()
	{

		RSItem ret[] = new RSItem[0];
		try{
			if(Interfaces.get(335, 50) != null)
			{
				ArrayList list = new ArrayList();
				RSInterfaceComponent arsinterfacecomponent[];
				int k = (arsinterfacecomponent = Interfaces.get(335, 50).getChildren()).length;
				for(int j = 0; j < k; j++)
				{
					RSInterfaceComponent i = arsinterfacecomponent[j];
					if(i.getComponentItem() != -1)
					{
						checkname(i.getComponentItem(), i.getComponentStack());

						list.add(new RSItem(i.getIndex(), i.getComponentItem(), i.getComponentStack(), org.tribot.api2007.types.RSItem.TYPE.BANK));
						
					}
				}
				println("Total Amount Worth: "+ total);
				println("Total Herb: " + totalherb);
				println("Total Cost: " + totalherb * amountpaying);
				println("Profit: " + (total - (totalherb * amountpaying)));
				ret = (RSItem[])list.toArray(new RSItem[list.size()]);
				total = 0;
				totalherb = 0;

			}
			return ret;
		}
		catch(NullPointerException e){
			return ret;
		}
	}
	
	private int snapdragon = 8000, Marrentill = 181, Ranarr = 6000, Kwuarm = 1800, ToadFlax = 1700, Cadantine = 500, Guam = 400,Tarromin = 150 , Harralander = 500 ,Irit = 1000 ,
			Avantoe = 1500 , Lantadyme = 600 , Dwarf = 3700;
	private int total = 0, totalherb = 0, amountpaying = 600;
	
	private void checkname(int x, int z){
		if(x == 3052){
			println("SnapDragon: " + snapdragon * z);
			total += snapdragon * z;
			totalherb += z;
		}
		if(x == 202){
			println("Marrentill: " + Marrentill * z);
			total += Marrentill * z;
			totalherb += z;
		}
		if(x == 208){
			println("Ranarr Weed: " + Ranarr * z);
			total +=  Ranarr * z;
			totalherb += z;
		}
		if(x == 214){
			println("Kwuarm: "+ Kwuarm * z);
			total += Kwuarm * z;
			totalherb += z;
		}
		if(x == 3050){
			println("ToadFlax: "+ ToadFlax * z);
			total += ToadFlax * z;
			totalherb +=  z;
		}
		if(x == 216 ){
			println("Cadantine: "+ Cadantine * z);
			total += Cadantine * z;
			totalherb += z;
		}
		if(x == 200){
			println("Guam Leaf: "+ Guam * z);
			total += Guam * z;
			totalherb += z;
		}
		if(x == 204){
			println("Tarromin:  "+ Tarromin * z);
			total += Tarromin * z;
			totalherb += z;
		}
		if(x == 206){
			println("Harralander: "+ Harralander * z);
			total += Harralander * z;
			totalherb += z;
		}
		if(x == 210){
			println("Irit Leaf: "+ Irit * z);
			total +=  Irit * z;
			totalherb += z;
		}
		if(x == 212){
			println("Avantoe: "+ Avantoe * z);
			total +=  Avantoe * z;
			totalherb += z;
		}
		if(x == 2486){
			println("Lantadyme: "+ Lantadyme * z);
			total +=  Lantadyme * z;
			totalherb += z;
		}
		if(x == 218){
			println("Dwarf Weed: "+ Dwarf * z);
			total +=  Dwarf * z;
			totalherb += z;
		}

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
	}
	private void TurnCamera(RSObject EX){
		Camera.setCameraAngle(0);
		RSObject target = EX;
		Camera.turnToTile(target.getPosition());
		EX.click("Open");
		sleep(50);
	}
	//-------------------------------------------------------------

	/*
	 * To change this template, choose Tools | Templates
	 * and open the template in the editor.
	 */

	/*
	 * To change this template, choose Tools | Templates
	 * and open the template in the editor.
	 */


	/**
	 *
	 * @author John
	 */
	class herb extends javax.swing.JFrame {

		/**
		 * Creates new form herb
		 */
		public herb() {
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
			jLabel2 = new javax.swing.JLabel();
			jLabel3 = new javax.swing.JLabel();
			jLabel4 = new javax.swing.JLabel();
			jLabel5 = new javax.swing.JLabel();
			jLabel6 = new javax.swing.JLabel();
			jLabel7 = new javax.swing.JLabel();
			jLabel8 = new javax.swing.JLabel();
			jLabel9 = new javax.swing.JLabel();
			jLabel10 = new javax.swing.JLabel();
			jLabel11 = new javax.swing.JLabel();
			jLabel12 = new javax.swing.JLabel();
			jLabel13 = new javax.swing.JLabel();
			snap = new javax.swing.JTextField("8000");
			ava = new javax.swing.JTextField("1500");
			mar = new javax.swing.JTextField("181");
			ra = new javax.swing.JTextField("6000");
			la = new javax.swing.JTextField("600");
			toad = new javax.swing.JTextField("1700");
			kw = new javax.swing.JTextField("1800");
			gu = new javax.swing.JTextField("400");
			ca = new javax.swing.JTextField("500");
			har = new javax.swing.JTextField("500");
			tar = new javax.swing.JTextField("150");
			dwa = new javax.swing.JTextField("3700");
			ir = new javax.swing.JTextField("1000");
			defining = new javax.swing.JButton();
			Mes = new javax.swing.JTextField();
			settext = new javax.swing.JButton();
			start = new javax.swing.JButton();
			jLabel14 = new javax.swing.JLabel();
			money = new javax.swing.JTextField();
			Stops = new javax.swing.JButton();
			Exit = new javax.swing.JButton();

			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

			jLabel1.setText("snapdragon");

			jLabel2.setText("Avantoe");

			jLabel3.setText("Marrentill");

			jLabel4.setText("Ranarr");

			jLabel5.setText("Lantadyme");

			jLabel6.setText("ToadFlax");

			jLabel7.setText("Kwuarm");

			jLabel8.setText("Guam");

			jLabel9.setText("Cadantine");

			jLabel10.setText("Tarromin");

			jLabel11.setText("Harralander");

			jLabel12.setText("Irit");

			jLabel13.setText("Dwarf");

			defining.setText("Define all Herb");
			defining.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					definingActionPerformed(evt);
				}
			});

			settext.setText("Set Text");
			settext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					settextActionPerformed(evt);
				}
			});

			start.setText("Start");
			start.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					startActionPerformed(evt);
				}
			});

			jLabel14.setText("How Much You buying each for?");

			Stops.setText("Stop");
			Stops.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					StopsActionPerformed(evt);
				}
			});

			Exit.setText("Close");
			Exit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					ExitActionPerformed(evt);
				}
			});

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addGap(27, 27, 27)
							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(layout.createSequentialGroup()
											.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
													.addComponent(jLabel1)
													.addComponent(jLabel3)
													.addComponent(jLabel4)
													.addComponent(jLabel5)
													.addComponent(jLabel7)
													.addComponent(jLabel6)
													.addComponent(jLabel9)
													.addComponent(jLabel10))
													.addGap(18, 18, 18)
													.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
															.addComponent(snap, javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(mar, javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(ava, javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(ra)
															.addComponent(la, javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(kw)
															.addComponent(toad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
															.addComponent(ca)
															.addComponent(gu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
															.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																	.addGroup(layout.createSequentialGroup()
																			.addGap(109, 109, 109)
																			.addComponent(defining, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
																			.addGap(0, 0, Short.MAX_VALUE))
																			.addGroup(layout.createSequentialGroup()
																					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																							.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
																									.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																									.addComponent(Mes, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
																									.addGroup(layout.createSequentialGroup()
																											.addGap(75, 75, 75)
																											.addComponent(settext)
																											.addGap(33, 33, 33)
																											.addComponent(start)
																											.addGap(32, 32, 32)
																											.addComponent(Stops)
																											.addGap(0, 0, Short.MAX_VALUE)))
																											.addContainerGap())))
																											.addGroup(layout.createSequentialGroup()
																													.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																															.addGroup(layout.createSequentialGroup()
																																	.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																																			.addComponent(jLabel11)
																																			.addComponent(jLabel12)
																																			.addComponent(jLabel13))
																																			.addGap(18, 18, 18)
																																			.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																																					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
																																							.addComponent(ir)
																																							.addComponent(dwa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
																																							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
																																									.addComponent(tar)
																																									.addComponent(har, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
																																									.addComponent(jLabel2))
																																									.addContainerGap(440, Short.MAX_VALUE))
																																									.addGroup(layout.createSequentialGroup()
																																											.addComponent(jLabel8)
																																											.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																																											.addComponent(Exit)
																																											.addGap(187, 187, 187))))
																																											.addGroup(layout.createSequentialGroup()
																																													.addGap(219, 219, 219)
																																													.addComponent(jLabel14)
																																													.addGap(52, 52, 52)
																																													.addComponent(money, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
																																													.addGap(0, 0, Short.MAX_VALUE))
					);
			layout.setVerticalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addGap(36, 36, 36)
							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
									.addGroup(layout.createSequentialGroup()
											.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
													.addGroup(layout.createSequentialGroup()
															.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																	.addComponent(jLabel1)
																	.addComponent(snap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																	.addGap(32, 32, 32))
																	.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
																			.addGap(3, 3, 3)
																			.addComponent(defining, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
																			.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
																			.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																					.addComponent(jLabel3)
																					.addComponent(mar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																							.addGroup(layout.createSequentialGroup()
																									.addGap(29, 29, 29)
																									.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																											.addComponent(jLabel2)
																											.addComponent(ava, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																											.addGap(26, 26, 26)
																											.addComponent(jLabel4))
																											.addGroup(layout.createSequentialGroup()
																													.addGap(10, 10, 10)
																													.addComponent(Mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																													.addGap(27, 27, 27)
																													.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																															.addComponent(start)
																															.addComponent(settext)
																															.addComponent(Stops)))))
																															.addComponent(ra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																															.addGap(26, 26, 26)
																															.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																																	.addComponent(jLabel5)
																																	.addComponent(la, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																																	.addGap(4, 4, 4)
																																	.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
																																			.addComponent(jLabel14)
																																			.addComponent(money, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
																																			.addGap(2, 2, 2)
																																			.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																																					.addGroup(layout.createSequentialGroup()
																																							.addComponent(jLabel7)
																																							.addGap(29, 29, 29)
																																							.addComponent(jLabel6))
																																							.addGroup(layout.createSequentialGroup()
																																									.addComponent(kw, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																																									.addGap(26, 26, 26)
																																									.addComponent(toad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
																																									.addGap(19, 19, 19)
																																									.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																																											.addGroup(layout.createSequentialGroup()
																																													.addComponent(jLabel9)
																																													.addGap(29, 29, 29)
																																													.addComponent(jLabel8))
																																													.addGroup(layout.createSequentialGroup()
																																															.addComponent(ca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																																															.addGap(2, 2, 2)
																																															.addComponent(Exit)
																																															.addGap(1, 1, 1)
																																															.addComponent(gu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
																																															.addGap(20, 20, 20)
																																															.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																																																	.addGroup(layout.createSequentialGroup()
																																																			.addComponent(jLabel10)
																																																			.addGap(29, 29, 29)
																																																			.addComponent(jLabel11))
																																																			.addGroup(layout.createSequentialGroup()
																																																					.addComponent(tar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																																																					.addGap(26, 26, 26)
																																																					.addComponent(har, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
																																																					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																																																							.addGroup(layout.createSequentialGroup()
																																																									.addGap(20, 20, 20)
																																																									.addComponent(jLabel12)
																																																									.addGap(29, 29, 29)
																																																									.addComponent(jLabel13))
																																																									.addGroup(layout.createSequentialGroup()
																																																											.addGap(18, 18, 18)
																																																											.addComponent(ir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
																																																											.addGap(26, 26, 26)
																																																											.addComponent(dwa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
																																																											.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					);

			pack();
		}// </editor-fold>                        
		private void definingActionPerformed(java.awt.event.ActionEvent evt) {                                         
			// TODO add your handling code here:
			snapdragon = Integer.parseInt(snap.getText());
			Marrentill = Integer.parseInt(mar.getText());
			Ranarr = Integer.parseInt(ra.getText());
			Kwuarm = Integer.parseInt(kw.getText());
			ToadFlax = Integer.parseInt(toad.getText());
			Cadantine = Integer.parseInt(ca.getText());
			Guam = Integer.parseInt(gu.getText());
			Tarromin = Integer.parseInt(tar.getText()); 
			Harralander = Integer.parseInt(har.getText()); 
			Irit = Integer.parseInt(ir.getText()); 
			Avantoe = Integer.parseInt(ava.getText()); 
			Lantadyme = Integer.parseInt(la.getText()); 
			Dwarf = Integer.parseInt(dwa.getText());
			amountpaying =  Integer.parseInt(money .getText());

			getid();

		}                                        

		private void settextActionPerformed(java.awt.event.ActionEvent evt) {                                        
			// TODO add your handling code here:
			message = Mes.getText();;
		}                                       

		private void startActionPerformed(java.awt.event.ActionEvent evt) {                                      
			// TODO add your handling code here:


			Keyboard.typeSend(message);
			sleep(delay * 1000);

		}                                     

		private void StopsActionPerformed(java.awt.event.ActionEvent evt) {                                      
			// TODO add your handling code here:

		}                                     
		private void ExitActionPerformed(java.awt.event.ActionEvent evt) {                                     
			// TODO add your handling code here:
			g1.setVisible(false);
			stopScript();
		}                                    

		/**
		 * @param args the command line arguments
		 */
		// Variables declaration - do not modify                     
		private javax.swing.JButton Exit;
		private javax.swing.JTextField Mes;
		private javax.swing.JButton Stops;
		private javax.swing.JTextField ava;
		private javax.swing.JTextField ca;
		private javax.swing.JButton defining;
		private javax.swing.JTextField dwa;
		private javax.swing.JTextField gu;
		private javax.swing.JTextField har;
		private javax.swing.JTextField ir;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel10;
		private javax.swing.JLabel jLabel11;
		private javax.swing.JLabel jLabel12;
		private javax.swing.JLabel jLabel13;
		private javax.swing.JLabel jLabel14;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JLabel jLabel3;
		private javax.swing.JLabel jLabel4;
		private javax.swing.JLabel jLabel5;
		private javax.swing.JLabel jLabel6;
		private javax.swing.JLabel jLabel7;
		private javax.swing.JLabel jLabel8;
		private javax.swing.JLabel jLabel9;
		private javax.swing.JTextField kw;
		private javax.swing.JTextField la;
		private javax.swing.JTextField mar;
		private javax.swing.JTextField money;
		private javax.swing.JTextField ra;
		private javax.swing.JButton settext;
		private javax.swing.JTextField snap;
		private javax.swing.JButton start;
		private javax.swing.JTextField tar;
		private javax.swing.JTextField toad;
		// End of variables declaration                   
	}

} // END OF SCRIPT