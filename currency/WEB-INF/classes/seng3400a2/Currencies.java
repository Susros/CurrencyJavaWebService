package seng3400a2;

import java.util.*;

/**
 *	Currencies
 *
 *	This is static class that stores all currencies and
 *	exchange rates.
 *
 *	There are four currencies listed as default:
 *		- AUD, USD, NZD, GBP
 *
 *	The default exchanged rate is as follow:
 *		AUD - USD = 0.7
 *		AUD - NZD = 1.09
 *		AUD - GBP = 0.55
 *
 *	@author Kelvin Yin (contact@kelvinyin.com)
 *
 *	@package seng3400a2
 */
public final class Currencies {
	
	/**
	 *	To store currency codes
	 */
	public static LinkedList<String> codes = new LinkedList<String>();
	
	/**
	 *	To store exchanged rates
	 */
	public static Map<String, Map<String, Double> > rates = new HashMap<String, Map<String, Double> >();
	
	/**
	 *	Add all default currency codes and rates
	 */
	static {
		// Default currency codes
		Currencies.addCode("AUD");
		Currencies.addCode("USD");
		Currencies.addCode("NZD");
		Currencies.addCode("GBP");
		
		// Default currency exchange rate
		Currencies.addRate("AUD", "USD", 0.7);
		Currencies.addRate("AUD", "NZD", 1.09);
		Currencies.addRate("AUD", "GBP", 0.55);
		Currencies.addRate("USD", "AUD", 1/0.7);
		Currencies.addRate("NZD", "AUD", 1/1.09);
		Currencies.addRate("GBP", "AUD", 1/0.55);
	}
	
	/**
	 *	Add currency code
	 *
	 *	@param code Currency code
	 */
	public static void addCode(String code) {
		Currencies.codes.add(code);
	}
	
	/**
	 *	Check if currency code exists
	 *
	 *	@param code Currency code
	 *
	 *	@return boolean True if it does, false otherwise.
	 */
	public static boolean hasCode(String code) {
		return (Currencies.codes.indexOf(code) != -1);
	}
	
	/**
	 *	Remove currency code
	 *
	 *	@param code Currency code
	 */
	public static void removeCode(String code) {
		Currencies.codes.remove(code);
	}
	
	/**
	 *	Get all list of codes
	 *
	 *	@return String[] List of currency codes
	 */
	public static String[] getCodes() {
		return Currencies.codes.toArray(new String[Currencies.codes.size()]);
	}
	
	/**
	 *	Add exchanged rate
	 *
	 *	@param fCode First currency code
	 *	@param sCode Second currency code
	 *	@param rate  Exchanged rate of these two currency code
	 */
	public static void addRate(String fCode, String sCode, double rate) {
		
		// Exchange rate from fCode to sCode
		
		Map<String, Double> tmp = new HashMap<String, Double>();
		
		if (Currencies.rates.containsKey(fCode)) {
			tmp = Currencies.rates.get(fCode);
		}
		
		tmp.put(sCode, rate);
		
		Currencies.rates.put(fCode, tmp);
	}

	/**
	 *	Get rate of two currencies
	 *
	 *	@param fCode First currency code
	 *	@param sCode Second currency code
	 *
	 *	@return double Exchange rate of two currency
	 */
	public static double getRate(String fCode, String sCode) {
		return Currencies.rates.get(fCode).get(sCode);
	}
	
	/**
	 *	Check rate of two currencies is registered
	 *
	 *	@param fCode First currency code
	 *	@param sCode Second currency code
	 *
	 *	@return boolean True if it is, false otherwise
	 */
	public static boolean hasRate(String fCode, String sCode) {

		if (Currencies.rates.get(fCode) == null) {
			return false;
		}

		if (Currencies.rates.get(fCode).get(sCode) == null) {
			return false;
		}

		return true;
	}
	
	/**
	 *	Remove entry of currency exchange rate
	 *
	 *	@param fCode First currency code
	 *	@param sCode Second currency code
	 *
	 *	@return boolean True if it is successful, false otherwise
	 */
	public static boolean removeRate(String fCode, String sCode) {
		
		if (Currencies.rates.get(fCode).remove(sCode) != null) {
			if (Currencies.rates.get(fCode).isEmpty()) {
				return (Currencies.rates.remove(fCode) != null);
			}
			
			return true;
		}
		
		return false;
	}

	/**
	 *	Remove all rates that associated with code
	 *
	 *	@param code Currency code to remove
	 *
	 *	@return boolean True if it is successful, false otherwise
	 */
	public static boolean removeAllRatesByCode(String code) {

		boolean flag = false;

		if (Currencies.rates.remove(code) != null) {
			flag = true;
		}

		// Get exchange rates iterator
		Iterator rateIterator = Currencies.rates.entrySet().iterator();
		
		while(rateIterator.hasNext()) {
			
			// Entry for fromCurrency
			Map.Entry e = (Map.Entry) rateIterator.next();
			
			// Delete associated rate of code if exists
			if (Currencies.rates.get(e.getKey()).remove(code) != null) {
				flag = true;
			}
		}

		return flag;
	}
	
}