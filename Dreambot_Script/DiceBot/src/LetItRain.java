import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.listener.AdvancedMessageListener;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.message.Message;

@ScriptManifest(author = "Chochu", description = "I Disagree Dice Bot", name = "IDDicer", version = 3.0, category = Category.MONEYMAKING)

public class LetItRain extends AbstractScript implements AdvancedMessageListener  {

	private Timer timer = new Timer();
	private State state;
	private LetItRainTask currTask = null;
	private int taskPlace = 0;
	private boolean started = false;
	private LetItRainGUI gui = null;
	private boolean debug = true;
	private String Owner = "";
	private Tile startTile = null;	
	public List<String> blackLists = new ArrayList<String>();

		
//	private int transferAmount = 0;
	
	public boolean OverrideTrade = false;
	public int OverrideTransferAmount = 0;
	public String OverridePlayerName = "";
	
	private enum State {
		Adver, Trade, Roll, Trade2, Transfer, Init, Pause
	}
	/*
	 * Adver, Trade, Roll, Trade2, Transfer, GUI, Init 
	 * Init: Task, Gui 
	 * Adver: Adversited Bot 
	 * Trade: When someone trade you 
	 *        Set Player Trade Player 
	 * Roll: If CashPool increase then Trade Success
	 * 		 Generate a random number
	 * 		 Print Result
	 * Trade2: If Win, Trade Player 
	 * 			If Lose,Update CashPool to same 
	 * Adver: If CashPool Decrease, trade Success
	 * 
	 */
	private State getState() {
		if (!started) {
			log("Init");
			return State.Init;
		}		
//		if (OverrideTrade){
//			log("Transfer");
//			return State.Transfer;
//		}		
		if(currTask.getStatus().equals("Open")) {
			if (!currTask.getTradingPlayer().equals("") && currTask.getCash() == getInvCash()) {
				log("Open:Trade");
				return State.Trade;
			} else if (!currTask.getTradingPlayer().equals("") && currTask.getCash() != getInvCash() && currTask.getRollResult().equals("")) {
				log("Open:Roll");
				return State.Roll;
			} else if (!currTask.getTradingPlayer().equals("") && currTask.getCash() != getInvCash() && !currTask.getRollResult().equals("")) {
				log("Open:Trade2");
				return State.Trade2;
			} else {
				log("Adver");
				return State.Adver;
			}
		}
		else if(currTask.getStatus().equals("Close")){
			log("Closed for Transfer");
			return State.Transfer;
		}
		else {
			log("Pause");
			return State.Pause;
		}
		
	}
//	TraderName.setText(_taskClass.getTradingPlayer());
//	TradingAmount.setText(String.valueOf(_taskClass.getTradedAmount()));
//	TraderTotalAmount.setText(String.valueOf(_taskClass.getCash()));
	@Override
	public void onStart() {
		log("Starting DreamBot Dicer script!");
		while(!getTabs().isOpen(Tab.INVENTORY)) {
			sleepUntil(() -> getTabs().isOpen(Tab.INVENTORY), 1000);
		}
		currTask = new LetItRainTask(getLocalPlayer(), getInvCash(), true, 10000);
		currTask.SetCash(getInvCash());
		currTask.setMinBet(JOptionPane.showInputDialog("Min Coins","1k"));
		currTask.setMaxBet(JOptionPane.showInputDialog("Max Coins","20M"));
		currTask.setAdvTimer(JOptionPane.showInputDialog("Ads Speed","2"));
		Owner = JOptionPane.showInputDialog("Owner","");
		
//		currTask.setMinBet("1");
//		currTask.setMaxBet("300");
//		currTask.setAdvTimer("200");		
//		getMouse().getMouseSettings().setWordsPerMinute(420);
		startTile = getStartTile();
		if(JOptionPane.showInputDialog("Debug Mode","y").contains("y")) {
			debug = true;
		}else {
			debug = false;
		}		
		currTask.setStatus("Open");
		/*JOptionPane.showInputDialog("First Name");
		 * Min Coin
		 * Max Coin
		 * Ads Speed
		 * debug mode
		 * Owner
		 * */
	}

	@Override
	public int onLoop() {
		if (started) {
//			if(getLocalPlayer().getTile() != startTile) {
//				getWalking().walk(startTile);
//				sleepUntil(() -> !getLocalPlayer().isMoving(), 5200);
//			}
			if(gui != null) {
				gui.TraderName.setText(currTask.getTradingPlayer());
				gui.TradingAmount.setText(String.valueOf((getInvCash()-currTask.getCash())));
				gui.ProfitTxt.setText(String.valueOf(getInvCash() - currTask.getStartingCash()));
			}

		}
		
		state = getState();
		switch (state) {
		case Init:
			if (gui == null) {
				gui = new LetItRainGUI(this, currTask);
				started = true;
				sleep(300);
			} else if (!gui.isVisible()) {
				gui.setVisible(true);
				sleep(1000);
			}
			break;
		case Transfer:
			if(currTask.getTradingPlayer().equals("")) {			
				transferTo(OverridePlayerName, OverrideTransferAmount);
				
				log("Resetting: Transfer success");
				currTask.setLastTradedPlayer(currTask.getTradingPlayer());
				currTask.SetCash(getInvCash());
				currTask.setTradedAmount(0);		
				
				log("Resetting Override");
				OverridePlayerName = "";
				OverrideTransferAmount = 0;
				OverrideTrade = false;
				currTask.setStatus("Open");		
							
			}
			log("Trade Complete");
			break;
		case Trade:
			if(currTask.getTradingPlayer() !="") {
				if(!acceptMoney(currTask.getTradingPlayer())) {
					getTrade().close();
				}
			}
			break;
		case Roll:
			taskPlace++;
			if(currTask.getTradingPlayer() !="" && currTask.getTradedAmount() != 0) {
				int rollResult = (currTask.getTradedAmount() * 2 > getInvCash()) ? HelperClass.randLose() : HelperClass.rand(currTask.getMinRoll(), currTask.getMaxRoll());
				currTask.setRollResult(rollResult < 55 ? "Lose" : "Win");
				String outputString = HelperClass.rollphaser(currTask.getRollResult(), currTask.getTradingPlayer(), rollResult);
				
				instantType(outputString);
				sleep(1000);
			}
			break;
		case Trade2:
			log("Paying to:" + currTask.getTradingPlayer() + "| Result: " + currTask.getRollResult().toLowerCase());
			if(currTask.getRollResult().toLowerCase().equals("win") && !currTask.getTradingPlayer().equals("")) {
				log("Transfering to Winner");

				if(transferTo(currTask.getTradingPlayer(), currTask.getTradedAmount()*2)) {
					getTrade().close();
					sleep(1000);
					if(getInvCash() == (currTask.getCash() - currTask.getTradedAmount())) {				
						log("Resetting: Trade success2");
						gui.AddEntry(currTask.getTradingPlayer(),currTask.getTradedAmount());
						currTask.setLastTradedPlayer(currTask.getTradingPlayer());
						currTask.setTradingPlayer("");
						currTask.SetCash(getInvCash());
						currTask.setTradedAmount(0);		
						currTask.setRollResult("");
					}
				}else if(getInvCash() == (currTask.getCash() - currTask.getTradedAmount())){
					
					gui.AddEntry(currTask.getTradingPlayer(),-currTask.getTradedAmount());
					log("Resetting: Trade success");
					instantType(HelperClass.getTime() + "Paid:" + (currTask.getTradedAmount() *2 ) + " To " + currTask.getTradingPlayer() + "Congrats!");
					currTask.setLastTradedPlayer(currTask.getTradingPlayer());
					currTask.setTradingPlayer("");
					currTask.SetCash(getInvCash());
					currTask.setRollResult("");
					currTask.setTradedAmount(0);	
				}
				sleep(1000);
			}
			else {
				log("Resetting: Losing Roll");
				currTask.setRollResult("");
				currTask.setTradedAmount(0);
				currTask.setTradingPlayer("");
				currTask.SetCash(getInvCash());
			}
			break;
		case Adver:
			if(getTrade().isOpen())
				getTrade().close();
			advertise();
			sleepUntil(() -> currTask.getTradingPlayer() != "" || !currTask.getStatus().equals("Open") || getTrade().isOpen(), currTask.getAdvTimer()*1000);
			break;
		case Pause:
			sleepUntil(() -> currTask.getStatus().equals("Open"), 100000);
			log("pause Done");
			break;
		default:
			break;
		}		
		return 0;
	}
	
	public void advertise() {
		instantType(currTask.Adversite());		
	}

	public void instantType(final String message) {
//		log(message);
		final String message2 = message;
		if(debug) {
			log(message2);
		}
		else {
			Canvas canvas = getClient().getInstance().getCanvas();
			for (char c : message2.toCharArray()) {
				canvas.dispatchEvent(new KeyEvent(canvas, KeyEvent.KEY_TYPED,System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, c));
			}
			canvas.dispatchEvent(new KeyEvent(canvas, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 
					KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED));
			canvas.dispatchEvent(new KeyEvent(canvas, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0,
					KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED));
		}
	}
	
	
	public void logging(String logtext) {
		log(logtext);
	}
	
	
	/***
	 * Transfer money to player
	 */
	public boolean transferTo(String name, int ptransferAmount) {
		int tradeCounter = 0;
		while(!getTrade().isOpen()) {
			tradeCounter++;
			getTrade().tradeWithPlayer(name);
			sleepUntil(() -> getTrade().isOpen() , 4000);
			if(tradeCounter > 3)
				break;
		}	
			
		if(compareName(getTrade().getTradingWith(), name) && getTrade().isOpen()) {
			log("Transferring:" + ptransferAmount + " Gold");
			if(!hasCoin(true)) {
				getTrade().addItem(995, ptransferAmount);
			}
			
			sleepUntil(() -> getTrade().isOpen(1) && hasCoin(true) , 3000);			
			if(getTrade().isOpen(1) && getTrade().contains(true, ptransferAmount, 995)) {
				
				int acceptCounter = 0;
				do {
					getTrade().acceptTrade(1);
					sleepUntil(() -> getTrade().isOpen(2) , 1000);
					acceptCounter++;
				}while(acceptCounter < 5);
				
				log("Sleeping till Screen 2");
				
				if(getTrade().isOpen(2)) {
					log("Accepting Trade 2");
					getTrade().acceptTrade(2);
					sleepUntil(() -> !getTrade().isOpen() , 4000);
					if(!getTrade().isOpen()) {
						log("Trade Success");
						return true;
					}
				}else {
					log("Trade screen 2 is not open");
					return false;
					
				}
			}	
		}	
		log("Trade screen is not open");
		return false;
		
	}
	
	private boolean compareName(String tradingWith, String name) {
//		String tradingname = new String(tradingWith.toString());
//		log(tradingname.replace(" ", "") + "=" + name.replace(" ", ""));
		
//		replaceAll(""+((char)thatCode)," "); 
		//name.charAt(index)
	
		String newname = "";
		int x = tradingWith.length();
		for(int i = 0; i < x; i++) {
			if((int)tradingWith.charAt(i) != 160){
				newname+=tradingWith.charAt(i);
			}
		}
		
		return newname.equals(name.replace(" ", ""));
	}
	private boolean hasCoin(boolean myScreen) {
		Item[] tradeScreenItem = getTrade().getItems(myScreen);
		if(tradeScreenItem == null || tradeScreenItem.length == 0) {
			return false;
		}
		for(Item x: tradeScreenItem) {
			if(x.getID() == 995) {
				return true;
			}
		}
		return false;
	}
	
	private boolean acceptMoney(String name) {
		if(!getTrade().isOpen()) {
			getTrade().tradeWithPlayer(name);
			sleepUntil(() -> getTrade().isOpen() , 5000);
			
			if(getTrade().isOpen(1)) {
				log("Checking Trade Player: ["+ currTask.getTradingPlayer() + "=" + getTrade().getTradingWith()+"]");

				if(compareName(getTrade().getTradingWith(), name)) {
					log("Waiting for Cash");
					sleepUntil(() -> hasCoin(false) || !getTrade().isOpen() , 6000);
					//if the traded money is not within Range of max and min
					if(!getTrade().isOpen() || !hasCoin(false)) {
						log("Trade Cancel");
						currTask.setTradingPlayer("");
						getTrade().close();
						return false;
					}
					if(getTrade().getItem(false, 995).getAmount() < currTask.getMinBet() || getTrade().getItem(false, 995).getAmount() > currTask.getMaxBet()) {
						log("Cash doesnt fit requirement");
						currTask.setTradingPlayer("");		
						getTrade().close();
						return false;
					}
					int acceptCounter = 0;
					if(getTrade().isOpen(1) && getTrade().contains(false, 995)) {
						do {
							getTrade().acceptTrade(1);
							sleepUntil(() -> getTrade().isOpen(2) , 1000);
							acceptCounter++;
						}while(acceptCounter < 5);
						
						if(getTrade().isOpen(2)) {
							getTrade().acceptTrade(2);
							sleepUntil(() -> !getTrade().isOpen() , 5000);

							if(currTask.getCash() < getInvCash()) {			
								currTask.setTradedAmount(getInvCash() - currTask.getCash());
								return true;
							}
						}
					}
				}
				else {
					log("Resetting: Name doesnt match");					
					return false;
				}
			}
		}	
		log("Resetting: TradeScreen not Open");
		if(currTask.getCash() < getInvCash()) {			
			currTask.setTradedAmount(getInvCash() - currTask.getCash());
			return true;
		}
		currTask.setTradingPlayer("");
		return false;			
			
	}
	
	/***
	 * return cash amount in inventory
	 */
	public int getInvCash() {
		return getInventory().getRandom(995).getAmount();
	}
	
	@Override
	public void onExit() {
		log("Stopping testing!");
	}
	
	@Override
	public void onPaint(Graphics g) {
			g.setColor(Color.green);
			
			g.drawString("State: " 			+ state.toString()											, 5, 50);
			g.drawString("Status: " 		+ currTask.getStatus()										, 100, 50);
			g.drawString("Owner: " 			+ Owner.toString()											, 5, 65);
			g.drawString("Min roll: " 		+ currTask.getMinRoll()										, 5, 80);
			g.drawString("Min roll: " 		+ currTask.getMaxRoll()										, 5, 95);
			g.drawString("Inventory Cash: " + getInvCash()												, 5, 110);
			g.drawString("Trade Counter: " 	+ String.valueOf(taskPlace)									, 5, 125);

			g.drawString("Debug: " 			+ (debug ? "On":"Off")										, 5, 155);
			g.drawString("Last Client: " 	+ currTask.getLastTradedPlayer().toString()					, 5, 170);
			g.drawString("Current Client: " + currTask.getTradingPlayer().toString()					, 5, 185);
			g.drawString("Amount Traded: " 	+ String.valueOf(currTask.getTradedAmount())				, 5, 200);
	}

	public boolean walkOnScreen(Tile t) {
		getMouse().move(getClient().getViewportTools().tileToScreen(t));
		String action = getClient().getMenu().getDefaultAction();
		if (action != null && action.equals("Walk here")) {
			return getMouse().click();
		} else {
			getMouse().click(true);
			sleepUntil(() -> getClient().getMenu().isMenuVisible(), 600);
			return getClient().getMenu().clickAction("Walk here");
		}
	}


	public void onAutoMessage(Message arg0) {
		// TODO Auto-generated method stub

	}

	public void onClanMessage(Message arg0) {
		// TODO Auto-generated method stub

	}

	public void onGameMessage(Message arg0) {
		// TODO Auto-generated method stub

	}

	public void onPlayerMessage(Message arg0) {
		// TODO Auto-generated method stub

	}

	public void onPrivateInMessage(Message message) {
		if(message.getMessage().contains("Override:")) {
			String[] splitStr = message.getMessage().split(" ");
			if (splitStr[0].equals("Override:")) {
			     try {  
					OverrideTransferAmount = Integer.parseInt(splitStr[2]); 
					OverrideTrade = true;
					OverridePlayerName = splitStr[1];
					currTask.setTradedAmount(getInvCash() - OverrideTransferAmount);
			      } catch (NumberFormatException e) {  
			    	log("Fail to convert to TransferAmount to int");
			      }  

			}
		}
	} 

	public void onPrivateInfoMessage(Message arg0) {
		// TODO Auto-generated method stub

	}

	public void onPrivateOutMessage(Message arg0) {
		// TODO Auto-generated method stub

	}

	public void onTradeMessage(Message message) {
		String trade_username = message.getUsername();
		if(!blackLists.contains(trade_username) && currTask.getStatus().equals("Open"))
			currTask.setTradingPlayer(trade_username);
	}

}
