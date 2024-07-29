
import java.util.Scanner;


/**
 * This class represents the main entry point of the Hotel Reservation System application.
 * It provides a console-based menu for users to interact with the system.
 * Users can create hotels, view hotel details, manage hotels (such as changing hotel names,
 * adding/removing rooms, updating room prices, removing hotels), and simulate room bookings.
 * The main menu also allows users to exit the application.
 *
 * @author Miguel Angelo Ignacio - S28
 * @author Nathaniel Kurt Reyes - S27
 */
public class Driver {
    private static Scanner scanner = new Scanner(System.in);
    private static HotelManager system = new HotelManager();

    /**
     * Main method that runs the Hotel Reservation System application.
     * Displays a menu and processes user input to perform various actions such as creating,
     * viewing, managing hotels, and simulating room bookings.
     * The application continues running until the user chooses to exit.
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println(" ");
            System.out.println("HOTEL RESERVATION SYSTEM");
            System.out.println(" ");
            System.out.println("[1] Create Hotel    [2] View Hotel");
            System.out.println("[3] Manage Hotel    [4] Simulate Booking");
            System.out.println("[5] Exit");
            System.out.println(" ");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            System.out.println("============================================");
            System.out.println(" ");
            switch (choice) {
                case 1:
                    system.createHotel();
                    break;
                case 2:
                    system.viewHotel();
                    break;
                case 3:
                    system.manageHotel();
                    break;
                case 4:
                    system.simulateBooking();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    System.out.println(" ");
            }
        }
    }
}
