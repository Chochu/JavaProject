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

import scripts.DGFireCraft.STATE;
@ScriptManifest(authors = { "DoomGuard" }, category = "DoomGuard", name = "DG Bone Burial", description = "Bury Bone and Get your hand Dirty", version = 1.0)


public class DGBoneBurial extends Script implements Painting {

//Variable---------------------------------------------------
	
	NewJFrame g = new NewJFrame();
	private boolean waitGUI = true;
	private STATE SS = getState();
	private int BONESs = 0; // a array of ID
	private final long startTime = System.currentTimeMillis(); //timer
	private int START_XP = Skills.getXP("Prayer");
	private int CURRENT_XP = this.START_XP;
	private int XP_TILL = Skills.getPercentToNextLevel("Prayer");
	private int lvl = Skills.getCurrentLevel("Prayer");
	String sa; //Some String, I use it to store my player's action
	//any other variable that you want to use
	
//-------------------------------------------------------------
	
//Main Loop----------------------------------------------------
    private final Color color1 = new Color(255, 255, 255);
    private final Color color2 = new Color(255, 51, 51);

    private final Font font1 = new Font("Arial Black", 0, 20);
    private final Font font2 = new Font("Arial Black", 0, 13);
    private final Image img1 = getImage("http://i444.photobucket.com/albums/qq162/doomguardex/Part2_zpsbfa485f3.jpg");

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }
  

	@Override
	public void onPaint(Graphics g) {
		g.drawImage(img1, 6, 345, null);	
        g.setFont(font1);
        g.setColor(color1);
        g.drawString("DoomGuard Bone DiGGER", 128, 467-45);
        g.setFont(font2);
        g.setColor(color2);
        g.drawString("Exp Gain :", 37, 504-45);
        g.drawString("Percent Till Lvl :", 266, 484-45);
        g.drawString("Current Lvl :", 266, 508-45);
        g.drawString("State", 37, 483-45);
        g.setColor(Color.BLACK);
        g.drawString("" + SS, 89, 484-45);
        g.drawString(""+ ((START_XP) - (CURRENT_XP)) , 117, 504-45);
        g.drawString("" + XP_TILL , 389, 483-45);
        g.drawString("" + lvl, 389, 507-45);
        g.drawString("Time Ran : " + Timing.msToString(Timing.timeFromMark(this.startTime)) , 10, 90);
  
	}
	

	
	@Override
	public void run() {
		g.setVisible(true);
		while(waitGUI) 
			sleep(500);
		
		println("The Bot has run");
		while(true){
			START_XP = Skills.getXP("Prayer");
			XP_TILL = Skills.getPercentToNextLevel("Prayer");
			lvl = Skills.getCurrentLevel("Prayer");
			Mouse.setSpeed(160); //this is mouse speed, not necessay 
			SS = getState(); //get the state of what you should do right now
			//put everything else that you want to loop, such as reseting the EXP variable or LVL variable
			
			switch(SS){ //loop through the enum array and check what state does it return
			case CHECK: //this is default return of getState method
				SS = getState();
				break;
			case OPENingBANKthevagin:
				sa = "OPENING THE BIG V";
				PUTTINGITIN();
				break;
			case BURYINGBItCH:
				sa = "Burying THE BIG D";
				BURYINGTHEBONER();
				break;
			//So on and So forth
			default:
				break;
			
			}
		}
	}

//-------------------------------------------------------------
	
	
//STATE--------------------------------------------------------
		private STATE getState(){
			if(Inventory.find(BONESs).length == 0){
				return STATE.OPENingBANKthevagin;
			}
			if(Inventory.find(BONESs).length >0){
				return STATE.BURYINGBItCH;
			}
			return STATE.CHECK; //default return
		}
		public enum STATE { 
			CHECK, OPENingBANKthevagin, BURYINGBItCH
		}	
//-------------------------------------------------------------

//Check Method---------------------------------------------------

	//Any other thing you check, such as near this or have this thing on inventory		

//-------------------------------------------------------------

//DO METHOD--------------------------------------------------------


	private boolean PUTTINGITIN() { 
		while(!Banking.isBankScreenOpen())
		Banking.openBankBooth();
		if(Banking.isBankScreenOpen()){
			Banking.depositAll();
			if(withdrawItem(BONESs, "All")){
				Banking.close();
				return true;
			}
		}
		return false;
	}

	private boolean BURYINGTHEBONER(){
		while(Inventory.find(BONESs).length > 0){
			RSItem [] boner = Inventory.find(BONESs);
			if(boner.length > 0)
				if(boner[0] != null)
					boner[0].click();
		}
		return true;
	}
	
//-------------------------------------------------------------

//Other Method-------------------------------------------------
//Other method such as...
    private boolean withdrawItem(int itemID, String amount) {
        if (Banking.isBankScreenOpen()) {
            RSItem[] item = Banking.find(itemID);
            if (item != null) {
                if (amount == "10" || amount == "5" || amount == "1" || amount == "All") {
                    item[0].click("Withdraw " + amount);
                    sleep(800, 1000);                    
                } else {
                    item[0].click("Withdraw X");
                    RSInterfaceChild WithdrawInterface = Interfaces.get(548, 93);
                    if (WithdrawInterface != null) {
                        Keyboard.typeSend(amount);
                    } else if (WithdrawInterface == null) {
                        sleep(800, 1000);
                    }
                }
                return true;
            }
        }
        return false;
    }
    

//-------------------------------------------------------------
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
            userinput = new javax.swing.JTextField();
            jLabel2 = new javax.swing.JLabel();
            jButton1 = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setTitle("Number");

            jLabel1.setText("Enter the BONE id");

            userinput.setText("ID");

            jLabel2.setText("DG BONE Bury");

            jButton1.setText("Start");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
            
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(userinput, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(60, 60, 60))))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(146, 146, 146)
                            .addComponent(jButton1)))
                    .addContainerGap(63, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(userinput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addGap(20, 20, 20))
            );

            pack();
        }// </editor-fold>                        

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    		BONESs = Integer.parseInt(g.userinput.getText());
            waitGUI = false;
            g.setVisible(false);
        }                                        

        /**
         * @param args the command line arguments
         */
     
        // Variables declaration - do not modify                     
        private javax.swing.JButton jButton1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JTextField userinput;
        // End of variables declaration                   
    }



} // END OF SCRIPT