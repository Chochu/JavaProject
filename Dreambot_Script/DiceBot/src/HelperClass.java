import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class HelperClass {
	public HelperClass() {
	}

	/**
	 * Convert shorthand to zero
	 */
	public static String getHS(String temp) {
		if (temp.contains("M")) {
			return temp.replace("M", "000000");
		}
		if (temp.contains("m")) {
			return temp.replace("m", "000000");
		}
		if (temp.contains("K")) {
			return temp.replace("K", "000");
		}
		if (temp.contains("k")) {
			return temp.replace("k", "000");
		}
		return temp;
	}

	/**
	 * Convert zero to shortname
	 */
	public static String getSH(int num) {
		String tempstr = String.valueOf(num);
		if (tempstr.contains("000000")) {
			tempstr = tempstr.substring(0, tempstr.length() - 6) + 'M';
			return tempstr;
		}
		if (tempstr.contains("000")) {
			tempstr = tempstr.substring(0, tempstr.length() - 3) + 'K';
			return tempstr;
		}
		return tempstr;
	}

	/**
	 * Generate timestamp
	 */
	public static String getTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return "[" + cal.get(11) + ":" + cal.get(12) + ":" + cal.get(13) + "]";
	}
	
	public static int randLose() {
		return rand(0,54);
	}
	
	static int rand(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * 
	 * @param result
	 *            Win or Lose
	 * @param traderName2
	 *            Gambler Name
	 * @param diceRoll
	 *            Number rolled
	 * @return Return the string to be printed
	 */
	public static String rollphaser(String result, String traderName2, int diceRoll) {
		if (result == "Win") {
			return "green:[" + result + "] [" + traderName2 + "] You Rolled: [" + diceRoll + "] on [1-100] Side Dice! "
					+ getTime();
		}
		return "red:[" + result + "] [" + traderName2 + "] You Rolled: [" + diceRoll + "] on [1-100] Side Dice! "
				+ getTime();
	}

	/**
	 * Return sorted Map
	 */
	public static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {
		java.util.List<Map.Entry<String, Integer>> list = new LinkedList(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return ((Integer) o2.getValue()).compareTo((Integer) o1.getValue());
			}
		});

		Map<String, Integer> sortedMap = new java.util.LinkedHashMap();
		for (Map.Entry<String, Integer> entry : list) {
			sortedMap.put((String) entry.getKey(), (Integer) entry.getValue());
		}

		return sortedMap;
	}
}
