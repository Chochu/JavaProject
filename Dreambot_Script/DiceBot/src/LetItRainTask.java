import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Player;

public class LetItRainTask {

	private Timer t;
	private int StartingCash = 0;
	private int CurrentCash = 0;
	private String MinBet = "10K";
	private String Maxbet = "10M";
	private int MaxRoll = 100;
	private int MinRoll = 0;
	private int advInterval = 3;
	private int tradedAmount = 0;
	private boolean dontMove = true;
	
	private Player self = null;
	private String lastTradedPlayer = ""; 
	private String TradingPlayer = "";
	private String Status = "";
	private String result = "";
	
	public Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	public LetItRainTask( Player self, int startingCash, boolean dontMove, int advInterval) {
		this.self = self;
		this.StartingCash = startingCash;
		this.dontMove = dontMove;
		this.advInterval = advInterval;
		t = new Timer();
	}

	public boolean dontMove() {
		return this.dontMove;
	}

	public void resetTimer() {
		t = new Timer();
	}

	public Timer getTimer() {
		return t;
	}

	public void setAdvTimer(String newtimer) {
		this.advInterval = Integer.parseInt(newtimer);
	}

	public int getAdvTimer() {
		return advInterval;
	}

	public void setDontMove(boolean dontMove) {
		this.dontMove = dontMove;
	}

	public void setMinBet(String min) {
		this.MinBet = min;
	}

	public int getMinBet() {
		return Integer.parseInt(HelperClass.getHS(MinBet));
	}
	
	public void setMaxBet(String max) {
		this.Maxbet = max;
	}

	public int getMaxBet() {
		return Integer.parseInt(HelperClass.getHS(Maxbet));
	}
	
	public void setTradedAmount(int amo) {
		this.tradedAmount = amo;
	}

	public int getTradedAmount() {
		return tradedAmount;
	}

	
	public String Adversite() {
		StringBuilder sb = new StringBuilder();
		sb.append("white:[" + self.getName() + "]55+ To Win x2 | Min[" + this.MinBet + "] Max ["+ this.Maxbet + "]| Try me!");
		return sb.toString();
	}
	
	public String getLastTradedPlayer() {
		return lastTradedPlayer;
	}
	
	public void setLastTradedPlayer(String playername) {
		this.lastTradedPlayer = playername;
	}
	
	public String getTradingPlayer() {
		return TradingPlayer;
	}
	
	public void setTradingPlayer(String playername) {
		this.TradingPlayer = playername;
	}
	
	public String getStatus() {
		return Status;
	}
	
	public void setStatus(String status) {
		this.Status = status;
	}
	
	public String getRollResult() {
		return result;
	}
	
	public void setRollResult(String result) {
		this.result = result;
	}
	
	public int getMaxRoll() {
		return MaxRoll;
	}
	
	public void setMaxRoll(int roll) {
		this.MaxRoll = roll;
	}
	
	public int getMinRoll() {
		return MinRoll;
	}
	
	public void setMinRoll(int roll) {
		this.MinRoll = roll;
	}
	
	public int getStartingCash() {
		return this.StartingCash;
	}
	
	public int getCash() {
		return this.CurrentCash;
	}
	public void SetCash(int cash) {
		this.CurrentCash = cash;
	}

}