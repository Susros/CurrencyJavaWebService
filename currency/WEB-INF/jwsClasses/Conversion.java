import seng3400a2.Currencies;

import java.util.*;
/**
 *	Login endpoint of identity
 *
 *	@author Zaw Moe Htat (c3193528@uon.edu.au)
 */

public class Conversion {
	
	/**
	 *	Get an array of all known conversion rates formatted as:
	 *	<form>-<to>:<rate>
	 *
	 *	@return String[] List of all exchange rates
	 */
	public String[] listRates() {
		
		// Store all list of rates
		LinkedList<String> tmp = new LinkedList<String>();
		
		// Get exchange rates iterator
		Iterator rateIterator = Currencies.rates.entrySet().iterator();
		
		while(rateIterator.hasNext()) {
			
			// Entry for fromCurrency
			Map.Entry fEntry = (Map.Entry) rateIterator.next();
			
			// Get the associated currencies and rate value of fromCurrency
			Map<String, Double> m = (Map<String, Double>) fEntry.getValue();
			
			// Get associated currencies and rate iterator
			Iterator mIterator = m.entrySet().iterator();
			
			while(mIterator.hasNext()) {
				
				// Entry for toCurrency and rate
				Map.Entry sEntry = (Map.Entry) mIterator.next();
				
				// Build string in XXX-YYY:ZZZ format
				tmp.add(fEntry.getKey() + "-" + sEntry.getKey() + ":" + String.format("%.4f", sEntry.getValue()));
			}
		}
		
		return tmp.toArray(new String[tmp.size()]);
	}
	
	/**
	 *	Get the rate of two currencies
	 *
	 *	@param fromCurrencyCode First currency code
	 *	@param toCurrencyCode   Second currency code
	 *
	 *	@return double Rate of currencies
	 */
	public double rateOf(String fromCurrencyCode, String toCurrencyCode) {
		if (Currencies.hasRate(fromCurrencyCode, toCurrencyCode)) {
			return Currencies.getRate(fromCurrencyCode, toCurrencyCode);
		}
		
		return -1;
	}
	
	/**
	 *	Get the service fees
	 *
	 *	@param fromCurrencyCode First currency code
	 *	@param toCurrencyCode   Second currency code
	 *	@param amount   		Charges amount for conversion
	 *
	 *	@return double Service fees; amount x rate x 0.99
	 */
	public double convert(String fromCurrencyCode, String toCurrencyCode, double amount) {
		if (Currencies.hasRate(fromCurrencyCode, toCurrencyCode)) {
			return (amount * Currencies.getRate(fromCurrencyCode, toCurrencyCode) * 0.99);
		}
		
		return -1;
	}
	
}