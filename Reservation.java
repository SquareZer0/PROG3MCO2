

/**
 * Represents a reservation made by a guest for a specific room in a hotel.
 */
public class Reservation {
    private String guestName;
    private int checkIn;
    private int checkOut;
    private Room room;
    private float totalPrice;
    private String discountCode;
    
    /**
     * Constructs a new Reservation object with the specified guest name, check-in date,
     * check-out date, and room.
     *
     * @param guestName The name of the guest making the reservation.
     * @param checkIn The check-in day of the reservation (1-31).
     * @param checkOut The check-out day of the reservation (greater than check-in day).
     * @param room The Room object being reserved.
     */
    public Reservation(String guestName, int checkIn, int checkOut, Room room, String discountCode) {
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.room = room;
        this.discountCode = discountCode;
        this.totalPrice = calculateTotalPrice();
        room.bookRoom(checkIn, checkOut); // Books the room for the reservation period
    }
    
    /**
     * Sets the discount code for the reservation.
     *
     * @param discountCode The discount code to set.
     */
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
        this.totalPrice = calculateTotalPrice(); // Recalculate total price with the new discount code
    }
    
    /**
     * Calculates the total price for the reservation including any applicable discount.
     *
     * @return The total price of the reservation after applying the discount.
     */
    private float calculateTotalPrice() {
        float totalBasePrice = 0.0f;

        // Calculate base price considering date price modifiers
        for (int day = checkIn; day < checkOut; day++) {
            float modifier = room.getDatePriceModifier(day);
            float dailyPrice = room.getPrice() * modifier; // Apply modifier to base price
            totalBasePrice += dailyPrice;
        }

        float discountedPrice = totalBasePrice;

        // Apply discount based on the code
        switch (discountCode) {
            case "I_WORK_HERE":
                discountedPrice *= 0.90; // 10% discount
                break;
            case "STAY4_GET1":
                if ((checkOut - checkIn) >= 5) {
                    discountedPrice -= room.getPrice(); // First day free
                }
                break;
            case "PAYDAY":
                if ((checkIn <= 15 && checkOut > 15) || (checkIn <= 30 && checkOut > 30)) {
                    discountedPrice *= 0.93; // 7% discount
                }
                break;
            default:
                // No discount applied
                break;
        }

        return discountedPrice;
    }

    /**
     * Retrieves the name of the guest who made the reservation.
     *
     * @return The guest name.
     */
    public String getName() {
        return guestName;
    }

    /**
     * Retrieves the check-in day of the reservation.
     *
     * @return The check-in day.
     */
    public int getCheckIn() {
        return checkIn;
    }

    /**
     * Retrieves the check-out day of the reservation.
     *
     * @return The check-out day.
     */
    public int getCheckOut() {
        return checkOut;
    }

    /**
     * Retrieves the Room object associated with this reservation.
     *
     * @return The Room object reserved.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Retrieves the total price for the reservation.
     *
     * @return The total price of the reservation.
     */
    public float getTotalPrice() {
        return totalPrice;
    }
}
