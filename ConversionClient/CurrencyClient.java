import localhost.currency.Conversion_jws.*;
import java.util.*;

/**
 *	CurrencyClient allows a user to calculate currency conversions.
 *
 *	This application accepts the following commands
 *	---------------------------------------------------------------------------------
 *	1. convert <fromCurrency> <toCurrency> <amount>
 *	2. rateOf <fromCurrency> <toCurrency>
 *	3. listRates
 *	4. help
 *	5. exit
 *
 *	@author Kelvin Yin (contact@kelvinyin.com)
 */
public class CurrencyClient {
	
	private void err(String message) {
		System.out.println("Error: " + message);
	}

	private void printHeader(String title) {
		// Width of application window
		int appWidth = 40;
		
		// Calculate padding size
		int paddingSize = appWidth - title.length() - 2;

		// Calculate prePadding
		int prePadding = paddingSize / 2;

		// Calculate postPadding
		int postPadding = (paddingSize % 2 == 0) ? prePadding : prePadding + 1;

		// Formatted title with padding
		String ftitle   = "";

		// Add padding at the front
		for(int i = 0; i < prePadding; i++) {
			ftitle += " ";
		}

		ftitle += title;

		// Add padding at the back
		for(int i = 0; i < postPadding; i++) {
			ftitle += " ";
		}

		// Print line with char '+'
		for(int i = 0; i < appWidth; i++) {
			System.out.print("+");
		}

		// Print title
		System.out.println();
		System.out.println("+" + ftitle + "+");

		// Print line with char '+'
		for(int i = 0; i < appWidth; i++) {
			System.out.print("+");
		}
	}

	private void printManual() {
		System.out.println("-- Manual --");
		System.out.println("The following commands are supported by this application:");

		System.out.println("1. convert <fromCurrency> <toCurrency> <amount>");
		System.out.println("2. rateOf <fromCurrency> <toCurrency>");
		System.out.println("3. listRates");
		System.out.println("4. exit");
	}
	
	public void run() {
		try {
			
			// Get Conversion from conversion service
			ConversionService conversionService = new ConversionServiceLocator();
			Conversion conversion = conversionService.getConversion();
			
			System.out.println();

			this.printHeader("Conversion Client Application");
			
			Scanner keyboard = new Scanner(System.in);
			
			while(true) {

				System.out.println();
				System.out.print("What would you like to do >> ");
				
				String command = keyboard.nextLine();
				
				String[] commandChunk = command.split("\\s");
				
				if (commandChunk.length > 0) {
					
					// Command for convert
					if (commandChunk[0].equals("convert")) {
						
						// This requires three arguments <fromCurrency> <toCurrency> <amount>
						if (commandChunk.length == 4) {
							
							String fromCurrency = commandChunk[1];
							String toCurrency   = commandChunk[2];
							double amount       = Double.parseDouble(commandChunk[3]);
							
							double convertAmount = conversion.convert(fromCurrency, toCurrency, amount);
							
							if (convertAmount == -1) {
								System.out.println("The currencies, " + fromCurrency + " and " + toCurrency + ", cannot be traded.");
							} else {
								System.out.println("Converted amount = " + convertAmount);
							}
							
						} else {
							this.err("Invalid parameters. Correct: convert <fromCurrency> <toCurrency> <amount>");
						}
						
					} 
					
					// Command for rateOf
					else if (commandChunk[0].equals("rateOf")) {

						// This requires two arguments <fromCurrency> <toCurrency>
						if (commandChunk.length == 3) {
							String fromCurrency = commandChunk[1];
							String toCurrency   = commandChunk[2];
						
							double rate = conversion.rateOf(fromCurrency, toCurrency);

							if (rate == -1) {
								System.out.println("The currencies, " + fromCurrency + " and " + toCurrency + ", cannot be traded");
							} else {
								System.out.println(fromCurrency + "-" + toCurrency + ":" + String.format("%.4f", rate));
							}
						} else {
							this.err("Invalid parameters. Correct: rateOf <fromCurrency> <toCurrency>");
						}
					}

					// Command for listRates
					else if (commandChunk[0].equals("listRates")) {

						// This requires no argument
						if (commandChunk.length == 1) {
							String[] rates = conversion.listRates();

							if (rates.length > 0) {
								for(int i = 0; i < rates.length; i++) {
									System.out.println(rates[i]);
								}
							} else {
								System.out.println("No rates to show.");
							}
						} else {
							this.err("Invalid parameters. Correct: listRates --This command does not need parameter.");
						}
					}

					// Command for exit
					else if (commandChunk[0].equals("exit")) {

						// This requires no argument
						if (commandChunk.length == 1) {
							break;
						} else {
							this.err("Invalid parameters. Correct: exit --This command does not need parameter.");
						}
					}

					// Command for help
					else if (commandChunk[0].equals("help")) {
						System.out.println();
						this.printManual();
					}

					// Invalid command
					else {
						this.err("Command '" + command + "' is invalid. See 'help'.");
					}
				}
				
			}

			System.out.println();
			System.out.println("Bye Bye");
			System.out.println();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CurrencyClient currencyClient = new CurrencyClient();
		currencyClient.run();
	}
}
