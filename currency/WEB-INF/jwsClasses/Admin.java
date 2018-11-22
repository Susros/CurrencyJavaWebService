import seng3400a2.Currencies;
import localhost.identity.Authorisation_jws.*;
import java.util.*;


/**
 *	Admin endpoint of currenncy
 *
 *	@author Zaw Moe Htat (c3193528@uon.edu.au)
 */
public class Admin {

	/**
	 *	Add currency
	 *
	 *	@param sessionKey   Session key of user
	 *	@param currencyCode Currency code to add
	 *
	 *	@return boolean True if successfully added, false otherwise
	 *
	 *	@throws Exception If user is not authorised
	 */
	public boolean addCurrency(String sessionKey, String currencyCode) throws Exception {
		
		// Get authorisation
		AuthorisationService authorisationService = new AuthorisationServiceLocator();
		Authorisation authorisation = authorisationService.getAuthorisation();

		if (authorisation.authorise(sessionKey) == false) {
			throw new Exception("Authorised access only.");
		}

		if (Currencies.hasCode(currencyCode)) {
			return false;
		}

		Currencies.addCode(currencyCode);

		return true;
	}

	/**
	 *	Remove currency. This will remove all associated conversion rates
	 *
	 *	@param sessionKey   Session key of user
	 *	@param currencyCode Currency code to remove
	 *
	 *	@return boolean True if successful, false otherwise
	 *
	 *	@throws Exception If user is not authorised
	 */
	public boolean removeCurrency(String sessionKey, String currencyCode) throws Exception {

		// Get authorisation
		AuthorisationService authorisationService = new AuthorisationServiceLocator();
		Authorisation authorisation = authorisationService.getAuthorisation();

		if (authorisation.authorise(sessionKey) == false) {
			throw new Exception("Authorised access only.");
		}

		// Check if currencyCode exist
		if (Currencies.hasCode(currencyCode)) {
			Currencies.removeCode(currencyCode);
			Currencies.removeAllRatesByCode(currencyCode);
			return true;
		}

		return false;
	}

	/**
	 *	List all currencies
	 *
	 *	@param sessionKey Session key of user
	 *
	 *	@return String[] List of currency codes
	 *
	 *	@throws Exception If user is not authorised
	 */
	public String[] listCurrencies(String sessionKey) throws Exception {

		// Get authorisation
		AuthorisationService authorisationService = new AuthorisationServiceLocator();
		Authorisation authorisation = authorisationService.getAuthorisation();

		if (authorisation.authorise(sessionKey) == false) {
			throw new Exception("Authorised access only.");
		}

		return Currencies.getCodes();
	}

	/**
	 *	Get conversion rates by currency code
	 *
	 *	@param sessionkey   Session key of user
	 *	@param currencyCode Currency code to search for rate
	 *
	 *	@return String[] List of rates
	 *
	 *	@throws Exception If user is not authorised
	 */
	public String[] conversionsFor(String sessionKey, String currencyCode) throws Exception {

		// Get authorisation
		AuthorisationService authorisationService = new AuthorisationServiceLocator();
		Authorisation authorisation = authorisationService.getAuthorisation();

		if (authorisation.authorise(sessionKey) == false) {
			throw new Exception("Authorised access only.");
		}

		// Check if currencyCode is listed to trade
		if (Currencies.hasCode(currencyCode) == false) {
			return null;
		}


		// Store all list of rates
		LinkedList<String> tmp = new LinkedList<String>();

		if (Currencies.rates.get(currencyCode) != null) {

			// Get exchange rates iterator
			Iterator rateIterator = Currencies.rates.get(currencyCode).entrySet().iterator();
			
			while(rateIterator.hasNext()) {

				Map.Entry e = (Map.Entry) rateIterator.next();
				tmp.add(currencyCode + "-" + e.getKey() + ":" + String.format("%.4f", (double) e.getValue()));
				tmp.add(e.getKey() + "-" + currencyCode + ":" + String.format("%.4f", (1 / (double) e.getValue())));

			}
		}
		
		return tmp.toArray(new String[tmp.size()]);
	}

	/**
	 *	Add rate of currencies
	 *
	 *	@param sessionKey       Session key of user
	 *	@param fromCurrencyCode First currency code
	 *	@param toCurrencyCode   Second currency code
	 *	@param conversionRate   Exchange rate
	 *
	 *	@return boolean True if it is successful, false otherwise
	 *
	 *	@throws Exception If user is not authorised
	 */
	public boolean addRate(String sessionKey, String fromCurrencyCode, String toCurrencyCode, double conversionRate) throws Exception {

		// Get authorisation
		AuthorisationService authorisationService = new AuthorisationServiceLocator();
		Authorisation authorisation = authorisationService.getAuthorisation();

		if (authorisation.authorise(sessionKey) == false) {
			throw new Exception("Authorised access only.");
		}

		// Check if fromCurrencyCode and toCurrencyCode the same
		if (fromCurrencyCode.equals(toCurrencyCode)) {
			return false;
		}

		// Check if conversionRate is less than or equal to zero
		if (conversionRate <= 0) {
			return false;
		}

		// Check if fromCurrencyCode and toCurrencyCode are registered
		if (Currencies.hasCode(fromCurrencyCode) == false || Currencies.hasCode(toCurrencyCode) == false) {
			return false;
		}

		// Check if this rate has been registered
		if (Currencies.hasRate(fromCurrencyCode, toCurrencyCode)) {
			return false;
		}

		// Register rate
		Currencies.addRate(fromCurrencyCode, toCurrencyCode, conversionRate);

		// Reverse it and register it
		Currencies.addRate(toCurrencyCode, fromCurrencyCode, (1 / conversionRate));

		return true;
	}

	/**
	 *	Update rate of currencies
	 *
	 *	@param sessionKey       Session key of user
	 *	@param fromCurrencyCode First currency code
	 *	@param toCurrencyCode   Second currency code
	 *	@param rate  			Exchange rate
	 *
	 *	@return boolean True if it is successful, false otherwise
	 *
	 *	@throws Exception If user is not authorised
	 */
	public boolean updateRate(String sessionKey, String fromCurrencyCode, String toCurrencyCode, double rate) throws Exception {

		// Get authorisation
		AuthorisationService authorisationService = new AuthorisationServiceLocator();
		Authorisation authorisation = authorisationService.getAuthorisation();

		if (authorisation.authorise(sessionKey) == false) {
			throw new Exception("Authorised access only.");
		}

		// Check if fromCurrencyCode and toCurrencyCode the same
		if (fromCurrencyCode.equals(toCurrencyCode)) {
			return false;
		}

		// Check if conversionRate is less than or equal to zero
		if (rate <= 0) {
			return false;
		}

		// Check if fromCurrencyCode and toCurrencyCode are registered
		if (Currencies.hasCode(fromCurrencyCode) == false || Currencies.hasCode(toCurrencyCode) == false) {
			return false;
		}

		// Check if this rate has been registered
		if (Currencies.hasRate(fromCurrencyCode, toCurrencyCode) == false) {
			return false;
		}

		// Register rate
		Currencies.addRate(fromCurrencyCode, toCurrencyCode, rate);

		// Reverse it and register it
		Currencies.addRate(toCurrencyCode, fromCurrencyCode, (1 / rate));

		return true;
	}

	/**
	 *	Remove exchange rate of two currencies
	 *
	 *	@param sessionKey       Session key of user
	 *	@param fromCurrencyCode First currency code
	 *	@param toCurrencyCode   Second currency code
	 *
	 *	@return boolean True if it is successful, false otherwise
	 *
	 *	@throws Exception If user is not authorised
	 */
	public boolean removeRate(String sessionKey, String fromCurrencyCode, String toCurrencyCode) throws Exception {
		
		// Get authorisation
		AuthorisationService authorisationService = new AuthorisationServiceLocator();
		Authorisation authorisation = authorisationService.getAuthorisation();

		if (authorisation.authorise(sessionKey) == false) {
			throw new Exception("Authorised access only.");
		}

		// Check if fromCurrencyCode and toCurrencyCode are registered
		if (Currencies.hasCode(fromCurrencyCode) == false || Currencies.hasCode(toCurrencyCode) == false) {
			return false;
		}

		// Check if rate is there for these currency codes
		if (Currencies.hasRate(fromCurrencyCode, toCurrencyCode) == false) {
			return false;
		}

		Currencies.removeRate(fromCurrencyCode, toCurrencyCode);
		Currencies.removeRate(toCurrencyCode, fromCurrencyCode);

		return true;

	}

	/**
	 *	List all rates
	 *
	 *	@param sessionKey Session key of user
	 *
	 *	@return String[] List of rates
	 *
	 *	@throws Exception If user is not authorised
	 */
	public String[] listRates(String sessionKey) throws Exception{

		// Get authorisation
		AuthorisationService authorisationService = new AuthorisationServiceLocator();
		Authorisation authorisation = authorisationService.getAuthorisation();

		if (authorisation.authorise(sessionKey) == false) {
			throw new Exception("Authorised access only.");
		}
		
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
}