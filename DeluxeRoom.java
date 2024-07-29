/**
 * Represents a deluxe room in a hotel.
 */
public class DeluxeRoom extends Room {

    public DeluxeRoom(int roomNo, float basePrice) {
        super(roomNo, basePrice);
    }

    @Override
    public float getPrice() {
        return getBasePrice() * 1.20f; // Deluxe room rate is 20% more than base price
    }
    
}
