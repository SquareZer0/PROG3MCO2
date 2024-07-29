
import java.util.ArrayList;

/**
 * Represents a Hotel with rooms and reservations.
 */
public class Hotel {

    private String name;
    private Room[] rooms;
    private int numRooms;
    private ArrayList<Reservation> reservations;
    private float totalEarnings;
    private float basePrice;
    
    

    /**
     * Constructs a new Hotel with a specified name and initial number of rooms.
     *
     * @param name The name of the hotel.
     * @param numRooms The initial number of rooms the hotel can accommodate.
     */
    public Hotel(String name, int numRooms) {
        this.name = name;
        this.rooms = new Room[50]; // Assuming maximum capacity of 50 rooms
        this.numRooms = numRooms;
        this.reservations = new ArrayList<>();
        this.totalEarnings = 0;
        this.basePrice = 1229.0f; // Default base price per night
        
    }
    
    public void updateDatePriceModifier(int day, float modifier) {
        for (int i = 0; i < numRooms; i++) {
            rooms[i].setDatePriceModifier(day, modifier);
        }
    }
    

    /**
     * Retrieves the name of the hotel.
     *
     * @return The name of the hotel.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets a new name for the hotel.
     *
     * @param newName The new name for the hotel.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Retrieves the total earnings of the hotel from reservations.
     *
     * @return The total earnings of the hotel.
     */
    public float getTotalEarnings() {
        return totalEarnings;
    }

    /**
     * Retrieves an array of all rooms in the hotel.
     *
     * @return An array containing all the rooms in the hotel.
     */
    public Room[] getRooms() {
        return rooms;
    }

    /**
     * Retrieves the current number of rooms in the hotel.
     *
     * @return The number of rooms in the hotel.
     */
    public int getNumRooms() {
        return numRooms;
    }

    /**
     * Adds a new room to the hotel if the maximum capacity has not been reached.
     *
     * @param room The room to be added to the hotel.
     */
    public void addRoom(Room room) {
        if (numRooms < 50) {
            rooms[numRooms] = room;
            numRooms++;
        } else {
            System.out.println("Cannot add more rooms. Maximum capacity reached.");
        }
    }

    /**
     * Removes a room from the hotel.
     *
     * @param room The room to be removed from the hotel.
     */
    public void removeRoom(Room room) {
        // Find the index of the room to remove
        int indexToRemove = -1;
        for (int i = 0; i < numRooms; i++) {
            if (rooms[i].getRoomNo() == room.getRoomNo()) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove == -1) {
            System.out.println("Room not found in hotel.");
            return;
        }

        // Shift elements in the array to remove the room
        for (int i = indexToRemove; i < numRooms - 1; i++) {
            rooms[i] = rooms[i + 1];
        }

        // Set the last element to null and decrement numRooms
        rooms[numRooms - 1] = null;
        numRooms--;
    }

    /**
     * Finds a room in the hotel based on its room number.
     *
     * @param roomNo The room number to search for.
     * @return The Room object if found, or null if not found.
     */
    public Room findRoom(int roomNo) {
        for (Room room : rooms) {
            if (room != null && room.getRoomNo() == roomNo) {
                return room;
            }
        }
        return null;
    }

    /**
     * Retrieves the base price per night for rooms in the hotel.
     *
     * @return The base price per night.
     */
    public float getBasePrice() {
        return this.basePrice;
    }

    /**
     * Updates the base price per night for all rooms in the hotel.
     *
     * @param newPrice The new base price per night.
     */
    public void updateBasePrice(float newPrice) {
        this.basePrice = newPrice;
        for (int i = 0; i < numRooms; i++) {
            rooms[i].setPrice(newPrice);
        }
    }

    /**
     * Retrieves a list of reservations made in the hotel.
     *
     * @return An ArrayList containing all reservations made in the hotel.
     */
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Adds a reservation to the hotel and updates total earnings.
     *
     * @param reservation The reservation to be added.
     */
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        totalEarnings += reservation.getTotalPrice();
    }

    /**
     * Removes a reservation from the hotel and updates total earnings.
     *
     * @param reservation The reservation to be removed.
     */
    public void removeReservation(Reservation reservation) {
        if (reservations.remove(reservation)) {
            totalEarnings -= reservation.getTotalPrice();
            reservation.getRoom().cancelBooking(reservation.getCheckIn(), reservation.getCheckOut());
        } else {
            System.out.println("Reservation not found.");
        }
    }

    /**
     * Finds the next available room number in the hotel.
     *
     * @return The next available room number, or -1 if maximum capacity is reached.
     */
    public int getNextAvailableRoomNumber() {
        if (numRooms >= 50) {
            return -1; // Return -1 if hotel has reached maximum room capacity
        }

        boolean[] roomExists = new boolean[51]; // Index from 1 to 50 for rooms 101 to 510

        // Mark existing room numbers
        for (int i = 0; i < numRooms; i++) {
            int roomNo = rooms[i].getRoomNo();
            int floor = roomNo / 100;
            int roomIndex = roomNo % 100;
            roomExists[(floor - 1) * 10 + roomIndex] = true;
        }

        // Find the first missing room number
        for (int i = 1; i < roomExists.length; i++) {
            if (!roomExists[i]) {
                int floor = (i - 1) / 10 + 1;
                int roomIndex = (i - 1) % 10 + 1;
                return floor * 100 + roomIndex;
            }
        }

        // If no missing room number, calculate the next room number
        int floor = numRooms / 10 + 1; // Calculate floor (1-based index)
        int roomIndex = numRooms % 10 + 1; // Calculate room index within the floor (1-based index)
        return floor * 100 + roomIndex;
    }
}
