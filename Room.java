/**
 * Represents a room in a hotel.
 */
public abstract class Room {

    private int roomNo;
    private float basePrice;
    private boolean[] availability;
    private float[] datePriceModifiers; // Array to store price modifiers for each day of the month

    /**
     * Initializes a room with the specified room number and base price.
     * By default, the room is available for all 31 days of the month.
     *
     * @param roomNo The room number.
     * @param basePrice The base price per night for the room.
     */
    public Room(int roomNo, float basePrice) {
        this.roomNo = roomNo;
        this.basePrice = basePrice;
        this.availability = new boolean[31]; // 0 if booked, 1 if available
        for (int i = 0; i < 31; i++) {
            this.availability[i] = true;
        }
        this.datePriceModifiers = new float[31]; // Initialize with default modifiers (1.0 for 100%)
        for (int i = 0; i < datePriceModifiers.length; i++) {
            datePriceModifiers[i] = 1.0f; // Default modifier is 100%
        }
    }
    
    public void setDatePriceModifier(int day, float modifier) {
        datePriceModifiers[day - 1] = modifier;
    }
    
    /**
     * Gets the price modifier for a given day.
     *
     * @param day The day of the month.
     * @return The price modifier for the given day.
     */
    public float getDatePriceModifier(int day) {
        return datePriceModifiers[day - 1];
    }

    public int getRoomNo() {
        return roomNo;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public abstract float getPrice();

    public void setPrice(float newPrice) {
        this.basePrice = newPrice;
    }

    public boolean isAvailable(int day) {
        return availability[day - 1];
    }

    public void bookRoom(int checkIn, int checkOut) {
        for (int i = checkIn; i < checkOut; i++) {
            availability[i - 1] = false;
        }
    }

    public void cancelBooking(int checkIn, int checkOut) {
        for (int i = checkIn; i < checkOut; i++) {
            availability[i - 1] = true;
        }
    }
    

}
