

import java.util.Scanner;
import java.util.ArrayList;

/**
 * The HotelManager class manages operations related to hotels, rooms, and reservations.
 * It provides functionalities for creating, viewing, and managing hotels, as well as simulating bookings.
 */
public class HotelManager {
    private ArrayList<Hotel> hotels; // List to store hotels
    private static Scanner scanner = new Scanner(System.in); // Scanner object for user input\
    private static final String DISCOUNT_I_WORK_HERE = "I_WORK_HERE";
    private static final String DISCOUNT_STAY4_GET1 = "STAY4_GET1";
    private static final String DISCOUNT_PAYDAY = "PAYDAY";

    /**
     * Constructor to initialize an empty list of hotels.
     */
    public HotelManager() {
        this.hotels = new ArrayList<>();
    }

    /**
     * Finds a hotel by name.
     * @param name The name of the hotel to find.
     * @return The Hotel object if found, null otherwise.
     */
    public Hotel findHotel(String name) {
        for (Hotel hotel : hotels) {
            if (hotel.getName().equalsIgnoreCase(name)) {
                return hotel;
            }
        }
        return null;
    }

    /**
    * Allows the user to create a new hotel by providing a name and number of rooms.
    * Validates the number of rooms input and adds rooms to the hotel.
    */
   public void createHotel() {
       Scanner scanner = new Scanner(System.in);

       System.out.print("Enter hotel name: ");
       String name = scanner.nextLine();
       if (findHotel(name) != null) {
           System.out.println("Hotel with this name already exists.");
           return;
       }

       int totalRooms = 0;
       int standardRooms = 0;
       int deluxeRooms = 0;
       int executiveRooms = 0;

       while (totalRooms <= 0 || totalRooms > 50) {
           System.out.print("Enter number of standard rooms (1-50): ");
           standardRooms = scanner.nextInt();

           System.out.print("Enter number of deluxe rooms (0-50): ");
           deluxeRooms = scanner.nextInt();

           System.out.print("Enter number of executive rooms (0-50): ");
           executiveRooms = scanner.nextInt();

           totalRooms = standardRooms + deluxeRooms + executiveRooms;
           if (totalRooms > 50) {
               System.out.println("Total number of rooms exceeds the maximum limit of 50. Please try again.");
           } else if (totalRooms <= 0) {
               System.out.println("Invalid number of rooms. Please enter a positive total number of rooms.");
           }
       }

       scanner.nextLine();  // Consume newline

       Hotel hotel = new Hotel(name, 0);
       for (int i = 0; i < standardRooms; i++) {
           int roomNo = hotel.getNextAvailableRoomNumber();
           hotel.addRoom(new StandardRoom(roomNo, hotel.getBasePrice()));
       }
       for (int i = 0; i < deluxeRooms; i++) {
           int roomNo = hotel.getNextAvailableRoomNumber();
           hotel.addRoom(new DeluxeRoom(roomNo, hotel.getBasePrice()));
       }
       for (int i = 0; i < executiveRooms; i++) {
           int roomNo = hotel.getNextAvailableRoomNumber();
           hotel.addRoom(new ExecutiveRoom(roomNo, hotel.getBasePrice()));
       }

       hotels.add(hotel);
       System.out.println("Hotel created successfully with " + hotel.getNumRooms() + " rooms.");
       System.out.println("============================================");
   }


    /**
     * Displays a list of available hotels and provides options to view detailed
     * information about a specific hotel.
     */
    public void viewHotel() {
        System.out.println("===== AVAILABLE HOTELS =====");
        for (Hotel hotel : hotels) {
            System.out.println("Hotel Name: " + hotel.getName());
            System.out.println("Total Rooms: " + hotel.getNumRooms());
            System.out.println("Base Price per Night: Php" + hotel.getBasePrice());
            System.out.println("--------------------------------------------");
        }
        System.out.println("");
        System.out.print("Enter hotel name: ");
        String name = scanner.nextLine();
        Hotel hotel = findHotel(name);
        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        boolean back = false;
        do {
            System.out.println(" ");
            System.out.print("VIEW HOTEL INFORMATION\n\n");
            System.out.print("[1] High Level    [2] Low level\n");
            System.out.print("[3] Back\n");

            System.out.print("Option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); //consume new line

            System.out.println(" ");

            switch (option) {
                case 1:
                    System.out.println("Hotel Name: " + hotel.getName());
                    System.out.println("Total Rooms: " + hotel.getNumRooms());
                    System.out.println("Total Earnings: Php" + hotel.getTotalEarnings());
                    break;

                case 2:
                    boolean exit = false;
                    do {
                        System.out.println("[1] View Available and Booked Rooms [2] View Room Information");
                        System.out.println("[3] View Reservation Information    [4] Back");
                        System.out.print("Select an option: ");
                        int choice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.println(" ");
                        switch (choice) {
                            case 1:
                                viewAvailableAndBookedRooms(hotel);
                                break;
                            case 2:
                                viewRoomInformation(hotel);
                                break;
                            case 3:
                                viewReservationInformation(hotel);
                                break;
                            case 4:
                                exit = true;
                                break;
                            default:
                                System.out.println("Invalid option. Please try again.");
                        }
                    } while (!exit);
                    break;

                case 3:
                    back = true;
            }
        } while (!back);
    }

    /**
    * Displays the number of available and booked rooms for a specified date range in the given hotel.
    * Prompts the user to enter check-in and check-out dates and checks the availability of rooms.
    *
    * @param hotel The hotel for which to view available and booked rooms.
    */
   public void viewAvailableAndBookedRooms(Hotel hotel) {
       System.out.print("Enter day of the month for check-in (1-31): ");
       int checkIn = scanner.nextInt();
       scanner.nextLine();  // Consume newline

       System.out.print("Enter day of the month for check-out (1-31): ");
       int checkOut = scanner.nextInt();
       scanner.nextLine();  // Consume newline

       // Validate the check-in and check-out dates
       if (checkIn < 1 || checkIn > 30) {
           System.out.println("Invalid check-in date. Check-in must be between 1 and 30.");
           return;
       }

       if (checkOut < 2 || checkOut > 31) {
           System.out.println("Invalid check-out date. Check-out must be between 2 and 31.");
           return;
       }

       if (checkIn >= checkOut) {
           System.out.println("Invalid date range. Check-out date must be after check-in date.");
           return;
       }

       System.out.println("");

       int available = 0;
       int booked = 0;

       for (int i = 0; i < hotel.getNumRooms(); i++) {
           if (isRoomAvailable(hotel.getRooms()[i], checkIn, checkOut)) {
               available++;
           } else {
               booked++;
           }
       }

       System.out.println("Available Rooms: " + available);
       System.out.println("Booked Rooms: " + booked);
   }


    /**
     * Displays detailed room information for the specified hotel, grouped by floors.
     *
     * @param hotel The Hotel object to display room information for.
     */
    public void viewRoomInformation(Hotel hotel) {
        // Create an array to store all available room numbers in the hotel
        int[] availableRoomNumbers = new int[hotel.getNumRooms()];
        for (int i = 0; i < hotel.getNumRooms(); i++) {
            availableRoomNumbers[i] = hotel.getRooms()[i].getRoomNo();
        }

        displayRooms(hotel);

        System.out.println();
        System.out.print("Enter room number: ");
        int roomNo = scanner.nextInt();

        // Check if the entered room number exists in availableRoomNumbers array
        boolean roomExists = false;
        for (int i = 0; i < availableRoomNumbers.length; i++) {
            if (availableRoomNumbers[i] == roomNo) {
                roomExists = true;
                break;
            }
        }

        if (!roomExists) {
            System.out.println("Room not found.");
            return;
        }

        // Find the room object in the hotel
        Room room = hotel.findRoom(roomNo);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        
        System.out.println();

        // Display room information
        System.out.println("Room Number: " + room.getRoomNo());
        System.out.println("Room Type: " + getRoomType(room));
        System.out.println("Price per Night: " + room.getPrice());
        System.out.println("Availability:");
        for (int day = 1; day <= 31; day++) { // Assuming 31 days in the month
            System.out.println("Day " + day + ": " + (room.isAvailable(day) ? "Available" : "Booked"));
        }
    }

    /**
     * Displays reservation information for the specified hotel, including guest name,
     * room number, check-in and check-out dates, and total price.
     *
     * @param hotel The Hotel object to display reservation information for.
     */
    public void viewReservationInformation(Hotel hotel) {
        // Display a list of all guests with reservations
        System.out.println("GUEST LIST");
        for (Reservation reservation : hotel.getReservations()) {
            System.out.println("" + reservation.getName());
        }

        System.out.println("");
        System.out.print("Enter guest name: ");
        String guestName = scanner.nextLine().trim(); // Trim to remove any leading/trailing whitespace

        // Validate input
        if (guestName.isEmpty()) {
            System.out.println("Invalid guest name. Please enter a valid name.");
            return;
        }

        // Search for the reservation
        boolean found = false;
        for (Reservation reservation : hotel.getReservations()) {
            if (reservation.getName().equalsIgnoreCase(guestName)) {
                System.out.println("Guest Name: " + reservation.getName());
                System.out.println("Room Number: " + reservation.getRoom().getRoomNo());
                System.out.println("Check-in: Day " + reservation.getCheckIn());
                System.out.println("Check-out: Day " + reservation.getCheckOut());
                System.out.println("Total Price: Php" + reservation.getTotalPrice());
                System.out.println("Breakdown of Price: " + (reservation.getCheckOut() - reservation.getCheckIn()) +
                        " nights * Php" + reservation.getRoom().getPrice() + "(" + getRoomType(reservation.getRoom()) + " Price per Night) = Php" + reservation.getTotalPrice());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("No reservations found for guest: " + guestName);
        }
        System.out.println("");
    }

    /**
    * Displays a list of available hotels and allows management operations on a selected hotel.
    * Operations include changing hotel name, adding rooms, removing rooms, updating base price,
    * removing reservations, and removing the hotel itself.
    * Prompts the user to select an operation until they choose to go back.
    */
    public void manageHotel() {
        System.out.println("===== AVAILABLE HOTELS =====");
        for (Hotel hotel : hotels) {
            System.out.println("Hotel Name: " + hotel.getName());
            System.out.println("Total Rooms: " + hotel.getNumRooms());
            System.out.println("Base Price per Night: Php" + hotel.getBasePrice());
            System.out.println("--------------------------------------------");
        }
        System.out.println("");
        System.out.print("Enter hotel name: ");
        String name = scanner.nextLine();
        Hotel hotel = findHotel(name);
        if (hotel == null) {
            System.out.println("Hotel not found.");
            return;
        }

        do {
            System.out.println(" ");
            System.out.printf("MANAGE HOTEL\n");
            System.out.println(" ");
            System.out.printf("[1] Change Name  [4] Update Base Price\n");
            System.out.printf("[2] Add Rooms    [5] Update Date Price Modifier\n");
            System.out.printf("[3] Remove Rooms [6] Remove Reservation\n");
            System.out.printf("[7] Remove Hotel [8] Back\n");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    changeHotelName(hotel);
                    break;
                case 2:
                    addRoom(hotel);
                    break;
                case 3:
                    removeRoom(hotel);
                    break;
                case 4:
                    updateRoomBasePrice(hotel);
                    break;
                case 5:
                    datePriceModifier(hotel);
                    break;
                case 6:
                    removeReservation(hotel);
                    break;
                case 7:
                    removeHotel(hotel);
                    break;
                case 8:
                    break; // This will exit the switch statement
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            // Move this condition outside the switch to avoid running an extra loop iteration
            if (choice == 8 || choice == 7) {
                break; // This will exit the do-while loop
            }
        } while (true);

    }

    /**
     * Changes the name of the specified hotel.
     * Prompts the user to enter a new hotel name and verifies if another hotel
     * with the same name already exists. If so, it displays an error message and
     * does not change the hotel name. Otherwise, it updates the hotel's name
     * and confirms the successful change.
     *
     * @param hotel The hotel object whose name is to be changed.
     */
    public void changeHotelName(Hotel hotel) {
        System.out.print("Enter new hotel name: ");
        String newName = scanner.nextLine();

        Hotel existingHotel = findHotel(newName);
        if (existingHotel != null) {
            System.out.println("Hotel with this name already exists.");
            return;
        }

        hotel.setName(newName);
        System.out.println("Hotel name changed successfully.");
    }


    /**
    * Adds rooms to the specified hotel.
    * Prompts the user to enter the type of rooms they want to add and how many rooms.
    * Checks if the hotel has reached its maximum room capacity (50 rooms). If the requested number of
    * rooms exceeds the available capacity, it adds as many rooms as possible and
    * informs the user accordingly.
    *
    * @param hotel The hotel object to which rooms are to be added.
    */
   public void addRoom(Hotel hotel) {
       Scanner scanner = new Scanner(System.in);
       int maxRoomsToAdd = 50 - hotel.getNumRooms(); // Calculate remaining room capacity

       if (maxRoomsToAdd <= 0) {
           System.out.println("Cannot add more rooms. Hotel already has maximum rooms.");
           return;
       }

       System.out.println("Enter type of room to add (1: Standard, 2: Deluxe, 3: Executive): ");
       int roomType = scanner.nextInt();
       scanner.nextLine(); // Consume newline

       Room room = null;
       switch (roomType) {
           case 1:
               room = new StandardRoom(hotel.getNextAvailableRoomNumber(), hotel.getBasePrice());
               break;
           case 2:
               room = new DeluxeRoom(hotel.getNextAvailableRoomNumber(), hotel.getBasePrice());
               break;
           case 3:
               room = new ExecutiveRoom(hotel.getNextAvailableRoomNumber(), hotel.getBasePrice());
               break;
           default:
               System.out.println("Invalid room type selected. Operation canceled.");
               return;
       }

       System.out.print("Enter number of rooms to add (max " + maxRoomsToAdd + "): ");
       int numRoomsToAdd = scanner.nextInt();
       scanner.nextLine(); // Consume newline

       if (numRoomsToAdd <= 0) {
           System.out.println("Invalid number of rooms. Please enter a positive number.");
           return;
       }

       if (numRoomsToAdd > maxRoomsToAdd) {
           System.out.println("Cannot add more than " + maxRoomsToAdd + " rooms. Adding " + maxRoomsToAdd + " rooms.");
           numRoomsToAdd = maxRoomsToAdd;
       }

       for (int i = 0; i < numRoomsToAdd; i++) {
           int nextRoomNumber = hotel.getNextAvailableRoomNumber();
           if (nextRoomNumber == -1) {
               System.out.println("Cannot add more rooms. Hotel already has maximum rooms.");
               break; // Exit loop if no more rooms can be added
           }

           Room newRoom = null;
           if (room instanceof StandardRoom) {
               newRoom = new StandardRoom(nextRoomNumber, hotel.getBasePrice());
           } else if (room instanceof DeluxeRoom) {
               newRoom = new DeluxeRoom(nextRoomNumber, hotel.getBasePrice());
           } else if (room instanceof ExecutiveRoom) {
               newRoom = new ExecutiveRoom(nextRoomNumber, hotel.getBasePrice());
           }

           hotel.addRoom(newRoom);
           System.out.println("Room " + nextRoomNumber + " (" + newRoom.getClass().getSimpleName() + ") added successfully.");
       }
   }




    /**
    * Removes a room from the specified hotel after verifying its availability and absence of reservations.
    * Displays all available rooms grouped by floors and prompts the user to enter the room number to remove.
    * Validates the entered room number and checks if it exists in the hotel's available rooms.
    * Ensures the room is not booked for any reservations before proceeding with removal.
    * 
    * @param hotel The hotel object from which a room is to be removed.
    */
   public void removeRoom(Hotel hotel) {
       // Create an array to store all available room numbers in the hotel
       int[] availableRoomNumbers = new int[hotel.getNumRooms()];
       for (int i = 0; i < hotel.getNumRooms(); i++) {
           availableRoomNumbers[i] = hotel.getRooms()[i].getRoomNo();
       }

       // Display all rooms
        displayRooms(hotel);

       // Prompt user to enter room number to remove
       System.out.print("Enter room number: ");
       int roomNo = scanner.nextInt();

       // Check if the entered room number exists in availableRoomNumbers array
       boolean roomExists = false;
       for (int i = 0; i < availableRoomNumbers.length; i++) {
           if (availableRoomNumbers[i] == roomNo) {
               roomExists = true;
               break;
           }
       }

       if (!roomExists) {
           System.out.println("Room not found.");
           return;
       }

       // Find the room object in the hotel
       Room room = hotel.findRoom(roomNo);
       if (room == null) {
           System.out.println("Room not found.");
           return;
       }

       // Check if the room has reservations
       if (!isRoomAvailable(room, 1, 31)) {
           System.out.println("Room has a reservation and cannot be removed!");
           return;
       }

       // Remove the room from the hotel
       hotel.removeRoom(room);
       System.out.println("Room removed successfully.");
   }

    /**
     * Updates the base price of rooms in the specified hotel after validating that there are no existing reservations.
     * Prompts the user to enter a new base price, ensuring it meets the minimum requirement of Php 100.
     * Continues to prompt until a valid price is entered, then updates the hotel's base price and confirms the update.
     * 
     * @param hotel The hotel object whose base price is to be updated.
     */
    public void updateRoomBasePrice(Hotel hotel) {
        if (hotel.getReservations().size() != 0) {
            System.out.println("Cannot update Base Price as there are still existing reservations");
            return;
        }

        System.out.print("Enter new base price (Php 100+): ");
        float newPrice = scanner.nextFloat();
        scanner.nextLine();  // Consume newline

        do{
            if(newPrice < 100){
                System.out.println("The Price is too low! Base Prices should be Php100 minimum");
                System.out.print("Enter new base price: ");
                newPrice = scanner.nextFloat();
                scanner.nextLine();  // Consume newline
            }

        }while (newPrice < 100);

        hotel.updateBasePrice(newPrice);
        System.out.println("Room base price updated successfully.");
    }
    
    /**
     * Updates the date price modifier for a specific day in the hotel.
     *
     * @param hotel The hotel for which to update the date price modifier.
     */
    public void datePriceModifier(Hotel hotel) {
        int day;
        float modifier;

        // Loop for day input validation
        do {
            System.out.print("Enter the day of the month to update (1-31): ");
            day = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (day < 1 || day > 31) {
                System.out.println("Invalid day. Please enter a day between 1 and 31.");
            }
        } while (day < 1 || day > 31);

        // Loop for modifier input validation
        do {
            System.out.print("Enter the new price modifier (e.g., 1.1 for 110%, 0.9 for 90%): ");
            modifier = scanner.nextFloat();
            scanner.nextLine(); // Consume newline

            if (modifier < 0.5 || modifier > 1.5) {
                System.out.println("Invalid modifier. Please enter a value between 0.5 and 1.5.");
            }
        } while (modifier < 0.5 || modifier > 1.5);

        hotel.updateDatePriceModifier(day, modifier);
        System.out.println("Date price modifier updated for day " + day + ".");
    }


    /**
    * Removes a reservation from the specified hotel based on the guest's name.
    * Lists all guests with reservations in the hotel and prompts the user to enter the guest's name for removal.
    * If a reservation is found matching the guest's name, it is removed; otherwise, a message indicates no reservation found.
    * 
    * @param hotel The hotel object from which a reservation is to be removed.
    */
    public void removeReservation(Hotel hotel) {
        System.out.println("GUEST LIST");
        for (Reservation reservation : hotel.getReservations()) {
            System.out.println("" + reservation.getName());
        }
        System.out.println();
        System.out.print("Enter guest name for the reservation to be removed: ");
        String guestName = scanner.nextLine();

        Reservation reservationToRemove = null;
        for (Reservation reservation : hotel.getReservations()) {
            if (reservation.getName().equalsIgnoreCase(guestName)) {
                reservationToRemove = reservation;
                break;
            }
        }

        if (reservationToRemove == null) {
            System.out.println("Reservation not found for guest: " + guestName);
        } else {
            hotel.removeReservation(reservationToRemove);
            System.out.println("Reservation removed successfully.");
        }
    }

    
    /**
    * Removes the specified hotel from the list of hotels.
    * Removes the hotel object from the 'hotels' list and prints a success message indicating the hotel removal.
    * 
    * @param hotel The hotel object to be removed from the list.
    */
    public void removeHotel(Hotel hotel) {
        hotels.remove(hotel);
        System.out.println("Hotel removed successfully.");
    }

    
    /**
    * Simulates the process of booking a room in a hotel based on user input.
    * Displays available hotels with their details and prompts the user to select a hotel.
    * Then, prompts for check-in and check-out dates, validates them, and displays available rooms.
    * Allows the user to choose a room and enter guest details to make a reservation.
    * 
    * If no rooms are available for the selected dates or if invalid input is provided,
    * appropriate error messages are displayed, and the process exits.
    */
   public void simulateBooking() {
       System.out.println("===== AVAILABLE HOTELS =====");
       for (Hotel hotel : hotels) {
           System.out.println("Hotel Name: " + hotel.getName());
           System.out.println("Total Rooms: " + hotel.getNumRooms());
           System.out.println("Base Price per Night: Php" + hotel.getBasePrice());
           System.out.println("--------------------------------------------");
       }
       System.out.println("");
       System.out.print("Enter hotel name: ");
       String hotelName = scanner.nextLine();
       Hotel hotel = findHotel(hotelName);
       if (hotel == null) {
           System.out.println("Hotel not found.");
           return;
       }

       System.out.print("Enter check-in day (1-30): ");
       int checkIn = scanner.nextInt();
       scanner.nextLine();  // Consume newline

       System.out.print("Enter check-out day (" + (checkIn + 1) + "-31): ");
       int checkOut = scanner.nextInt();
       scanner.nextLine();  // Consume newline

       if (checkIn < 1 || checkIn >= checkOut || checkOut > 31) {
           System.out.println("Invalid check-in or check-out dates.");
           return;
       }

       Room[] availableRooms = new Room[hotel.getNumRooms()];
       int j = 0;
       for (int i = 0; i < hotel.getNumRooms(); i++) {
           Room room = hotel.getRooms()[i];
           if (isRoomAvailable(room, checkIn, checkOut)) {
               availableRooms[j] = room;
               j++;
           }
       }

       if (j == 0) {
           System.out.println("No rooms available for the selected dates.");
           return;
       } else {
           System.out.println("Available Rooms:");
           int numberOfFloors = (hotel.getNumRooms() + 9) / 10;
           for (int floor = 1; floor <= numberOfFloors; floor++) {
               System.out.print("Floor " + floor + ": ");
               boolean floorHasRooms = false;
               for (int i = 0; i < j; i++) {
                   if (availableRooms[i].getRoomNo() / 100 == floor) {
                       Room room = availableRooms[i];
                       String roomType = getRoomType(room);
                       System.out.print("Room " + room.getRoomNo() + " (" + roomType + ") ");
                       floorHasRooms = true;
                   }
               }
               if (!floorHasRooms) {
                   System.out.print("No available rooms");
               }
               System.out.println(); // Move to the next line for the next floor
           }

           Room selectedRoom = null;
           do {
               System.out.println(" ");
               System.out.print("Enter any room number of the available rooms: ");
               int roomNo = scanner.nextInt();
               scanner.nextLine(); // Consume newline
               for (int i = 0; i < j; i++) {
                   if (roomNo == availableRooms[i].getRoomNo()) {
                       selectedRoom = availableRooms[i];
                       break;
                   }
               }

               if (selectedRoom == null) {
                   System.out.println("Invalid input! Please enter the ROOM NUMBER of any of the available rooms: ");
               }
           } while (selectedRoom == null);

           System.out.print("Enter guest name: ");
           String guestName = scanner.nextLine();

           // Display available discount codes and prompt for selection
           String discountCode = "";
           do {
               discountCode = "";
               System.out.println("Available discount codes:");
               System.out.println("1. " + DISCOUNT_I_WORK_HERE + " - 10% discount");
               System.out.println("2. " + DISCOUNT_STAY4_GET1 + " - Free first day for reservations of 5 days or more");
               System.out.println("3. " + DISCOUNT_PAYDAY + " - 7% discount if the reservation includes day 15 or 30");
               System.out.print("Enter discount code (or press Enter to skip): ");
               discountCode = scanner.nextLine().trim();

               if (!discountCode.isEmpty()) {
                   if (!isValidDiscountCode(discountCode)) {
                       System.out.println("Invalid discount code. Please try again.");
                   } else if (!isApplicableDiscountCode(discountCode, checkIn, checkOut)) {
                       System.out.println("This discount code is not applicable to your check-in and check-out dates. Please try again.");
                   }
               }
                       
               System.out.println();
           } while (!isValidDiscountCode(discountCode) || !isApplicableDiscountCode(discountCode, checkIn, checkOut));

           Reservation reservation = new Reservation(guestName, checkIn, checkOut, selectedRoom, discountCode);
           hotel.addReservation(reservation);
           selectedRoom.bookRoom(checkIn, checkOut);
           System.out.println("Reservation successful.");
           System.out.println("Total Price: Php" + reservation.getTotalPrice());
           System.out.println("");
       }
   }

    /**
     * Checks if a room is available during specified dates.
     * @param room The room to check availability for.
     * @param checkIn The day of check-in.
     * @param checkOut The day of check-out.
     * @return True if the room is available, false otherwise.
     */
    private boolean isRoomAvailable(Room room, int checkIn, int checkOut) {
        for (int day = checkIn; day < checkOut; day++) {
            if (!room.isAvailable(day)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if a discount code is valid.
     * 
     * @param discountCode The discount code to check.
     * @return True if the discount code is valid, false otherwise.
     */
    private boolean isValidDiscountCode(String discountCode) {
        return DISCOUNT_I_WORK_HERE.equals(discountCode) ||
               DISCOUNT_STAY4_GET1.equals(discountCode) ||
               DISCOUNT_PAYDAY.equals(discountCode) || discountCode.isEmpty();
    }
    
    
    /**
     * Checks if a discount code is applicable based on the check-in and check-out dates.
     *
     * @param discountCode The discount code to be checked.
     * @param checkIn      The check-in date.
     * @param checkOut     The check-out date.
     * @return True if the discount code is applicable, false otherwise.
     */
    public boolean isApplicableDiscountCode(String discountCode, int checkIn, int checkOut) {
        switch (discountCode) {
            case "I_WORK_HERE":
                return true; // Always applicable

            case "STAY4_GET1":
                return (checkOut - checkIn) >= 5; // Applicable for stays of 5 days or more

            case "PAYDAY":
                if ((checkIn <= 15 && checkOut > 15) || (checkIn <= 30 && checkOut > 30)) {
                        return true; // Applicable if stay includes day 15 or 30
                    }
                
               return false;

            default:
                return true; // Unknown discount code
        }
    }
    
    /**
    * Gets the type of the room based on its class.
    * 
    * @param room The room object.
    * @return A string representing the type of the room.
    */
   private String getRoomType(Room room) {
       if (room instanceof StandardRoom) {
           return "Standard";
       } else if (room instanceof DeluxeRoom) {
           return "Deluxe";
       } else if (room instanceof ExecutiveRoom) {
           return "Executive";
       } else {
           return "Unknown";
       }
   }
   
   /**
    * Displays all rooms in a hotel, grouped by floors, and includes room type.
    * 
    * @param hotel The hotel object whose rooms are to be displayed.
    */
   public void displayRooms(Hotel hotel) {
       int numberOfFloors = (hotel.getNumRooms() + 9) / 10;

       System.out.println("===== ALL ROOMS =====");
       for (int floor = 1; floor <= numberOfFloors; floor++) {
           System.out.print("Floor " + floor + ": ");
           boolean floorHasRooms = false;
           for (int i = 0; i < hotel.getNumRooms(); i++) {
               Room room = hotel.getRooms()[i];
               if (room.getRoomNo() / 100 == floor) {
                   System.out.print("Room " + room.getRoomNo() + " (" + getRoomType(room) + ") ");
                   floorHasRooms = true;
               }
           }
           if (!floorHasRooms) {
               System.out.print("No rooms on this floor");
           }
           System.out.println(); // Move to the next line for the next floor
       }
   }
}