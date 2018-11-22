import localhost.identity.Login_jws.*;
import localhost.currency.Admin_jws.*;
import java.util.*;
import java.io.Console;

/**
 *	Admin Client
 *
 *	Client application for administrator to manage currencies and exchange rates.
 *
 *	This application accepts the following commands
 *	---------------------------------------------------------------------------------
 *	1 . addCurrency <currencyCode>
 *  2 . removeCurrency <currencyCode>
 *	3 . listCurrencies
 *	4 . conversionsFor <currencyCode>
 *	5 . addRate <fromCurrency> <toCurrency> <rate>
 *	6 . updateRate <fromCurrency> <toCurrency> <rate>
 *	7 . removeRate <fromCurrency> <toCurrency>
 *	8 . listRates
 *	9 . help
 *  10. logout
 *
 *	@author Kelvin Yin (contact@kelvinyin.com)
 */
public class AdminClient {

	private String session;
	private String username;
	private String password;

	public AdminClient() {
		this.session = "";
		this.username = "";
		this.password = "";
	}
	
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
		String ftitle = "";

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

		System.out.println("1. addCurrency <currencyCode>");
		System.out.println("2. removeCurrency <currencyCode>");
		System.out.println("3. listCurrencies");
		System.out.println("4. conversionsFor <currencyCode>");
		System.out.println("5. addRate <fromCurrency> <toCurrency> <rate>");
		System.out.println("6. updateRate <fromCurrency> <toCurrency> <rate>");
		System.out.println("7. removeRate <fromCurrency> <toCurrency>");
		System.out.println("8. listRates");
		System.out.println("9. logout");
	}

	public void run(String[] args) {
		try {

			// Get login service
			LoginService loginService = new LoginServiceLocator();
			Login login = loginService.getLogin();

			// Get admin service
			AdminService adminService = new AdminServiceLocator();
			Admin admin = adminService.getAdmin();

			// Space out
			System.out.println();
			
			// Print header title
			this.printHeader("Admin Client Application");

			// Get keyboard input
			Scanner keyboard = new Scanner(System.in);

			// Counter to remember how many times have user already tried to login
			int attempt = 0;

			System.out.println();
			System.out.println("- Login -");

			// Make sure the attempt won't go more than 3
			while(attempt < 3 && this.session.equals("")) {

				if (args.length == 1 && attempt == 0) {
					this.username = args[0];

					System.out.println("Username: " + username);
				} else {
					System.out.print("Username: ");
					this.username = keyboard.nextLine();
				}

				// Hidden password
				Console console = System.console();

				char passwordChunk[] = console.readPassword("Password: ");
				this.password = new String(passwordChunk);

				// Session
				this.session = login.login(username, password);

				if (this.session.equals("INVALID")) {
					attempt++;

					System.out.println("Username and password do not match. Attempt left: " + (3 - attempt));
					System.out.println();

					this.session = "";
				}
			}

			if (attempt == 3) {
				System.out.println("Login failed.");
				System.exit(0);
			}

			// =====================
			// Login successful.
			// =====================
			
			System.out.println("Welcome, " + username);

			while(true) {

				System.out.println();
				System.out.print("What would you like to do >> ");
				
				// Get command
				String command = keyboard.nextLine();
				
				String[] commandChunk = command.split("\\s");
				
				if (commandChunk.length > 0) {
					
					// Command for addCurrency
					if (commandChunk[0].equals("addCurrency")) {
						
						// This requires three arguments <currencyCode>
						if (commandChunk.length == 2) {
							
							String currencyCode = commandChunk[1];
							
							if (admin.addCurrency(this.session, currencyCode)) {
								System.out.println("Currency '" + currencyCode + "' added.");
							} else {
								System.out.println("Currency '" + currencyCode + "' already exists.");
							}
							
						} else {
							this.err("Invalid parameters. Correct: addCurrency <currencyCode>");
						}
						
					} 

					// Command for removeCurrency
					else if (commandChunk[0].equals("removeCurrency")) {
						
						// This requires three arguments <currencyCode>
						if (commandChunk.length == 2) {
							
							String currencyCode = commandChunk[1];
							
							if (admin.removeCurrency(this.session, currencyCode)) {
								System.out.println("Currency '"+ currencyCode +"' removed.");
							} else {
								System.out.println("Currency '"+ currencyCode +"' does not exist.");
							}
							
						} else {
							this.err("Invalid parameters. Correct: removeCurrency <currencyCode>");
						}
						
					} 
					
					// Command for listCurrencies
					else if (commandChunk[0].equals("listCurrencies")) {

						// This requires no argument
						if (commandChunk.length == 1) {
							String[] currencies = admin.listCurrencies(this.session);

							if (currencies.length > 0) {
								for(int i = 0; i < currencies.length; i++) {
									System.out.println(currencies[i]);
								}
							} else {
								System.out.println("No currenncies to show.");
							}
						} else {
							this.err("Invalid parameters. Correct: listCurrencies --This command does not need parameter");
						}
					}
					
					// Command for conversionsFor
					else if (commandChunk[0].equals("conversionsFor")) {
						
						// This requires three arguments <currencyCode>
						if (commandChunk.length == 2) {
							
							String currencyCode = commandChunk[1];

							String[] rates = admin.conversionsFor(this.session, currencyCode);

							if (rates == null) {
								System.out.println("Currency code '"+ currencyCode +"' is not registered yet.");
							} else {
								if (rates.length > 0) {
									for(int i = 0; i < rates.length; i++) {
										System.out.println(rates[i]);
									}
								} else {
									System.out.println("No rates to show for '"+ currencyCode +"'.");
								}
							}
							
						} else {
							this.err("Invalid parameters. Correct: conversionsFor <currencyCode>");
						}
						
					}

					// Command for addRate
					else if (commandChunk[0].equals("addRate")) {
						
						// This requires three arguments <currencyCode>
						if (commandChunk.length == 4) {
							
							String fromCurrency = commandChunk[1];
							String toCurrency 	= commandChunk[2];
							double rate 		= Double.parseDouble(commandChunk[3]);

							if (rate <= 0) {
								System.out.println("Conversion rate is less than or equal to zero.");
							} else {

								if (admin.addRate(this.session, fromCurrency, toCurrency, rate)) {
									System.out.println("Rate '"+ fromCurrency + "-" + toCurrency + ":" + rate +"' added.");
								} else {
									System.out.println("Failed to add rate: currency codes is not registered or conversion of these currency codes alreayd exist.");
								}
							}
							
						} else {
							this.err("Invalid parameters. Correct: addRate <fromCurrency> <toCurrency> <rate>");
						}
						
					}

					// Command for updateRate
					else if (commandChunk[0].equals("updateRate")) {
						
						// This requires three arguments <currencyCode>
						if (commandChunk.length == 4) {
							
							String fromCurrency = commandChunk[1];
							String toCurrency 	= commandChunk[2];
							double rate 		= Double.parseDouble(commandChunk[3]);

							if (rate <= 0) {
								System.out.println("Conversion rate is less than or equal to zero.");
							} else {
								if (admin.updateRate(this.session, fromCurrency, toCurrency, rate)) {
									System.out.println("Rate '"+ fromCurrency + "-" + toCurrency + ":" + rate +"' updated.");
								} else {
									System.out.println("Failed to update rate: currency codes are not registered or conversion of these currency codes does not exist.");
								}
							}
							
						} else {
							this.err("Invalid parameters. Correct: updateRate <fromCurrency> <toCurrency> <rate>");
						}
						
					}

					// Command for removeRate
					else if (commandChunk[0].equals("removeRate")) {
						
						// This requires three arguments <currencyCode>
						if (commandChunk.length == 3) {
							
							String fromCurrency = commandChunk[1];
							String toCurrency 	= commandChunk[2];

							if (admin.removeRate(this.session, fromCurrency, toCurrency)) {
								System.out.println("Rate '"+ fromCurrency + "-" + toCurrency + "' removed.");
							} else {
								System.out.println("Failed to remove rate: currency codes are not registered or conversion of these currency codes do not exist.");
							}
							
						} else {
							this.err("Invalid parameters. Correct: removeRate <fromCurrency> <toCurrency>");
						}
						
					}

					// Command for listRates
					else if (commandChunk[0].equals("listRates")) {

						// This requires no argument
						if (commandChunk.length == 1) {
							String[] rates = admin.listRates(this.session);

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
					else if (commandChunk[0].equals("logout")) {

						// This requires no argument
						if (commandChunk.length == 1) {

							if (login.logout(this.session)) {
								System.out.println("Goodbye!");
								break;
							} else {
								System.out.println("Logout failed. Session has expired.");
							}
						} else {
							this.err("Invalid parameters. Correct: logout --This command does not need parameter.");
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
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		AdminClient adminClient = new AdminClient();
		adminClient.run(args);
	}
}
