/**
 * Represents a standard room in a hotel.
 */
public class StandardRoom extends Room {

    public StandardRoom(int roomNo, float basePrice) {
        super(roomNo, basePrice);
    }

    @Override
    public float getPrice() {
        return getBasePrice(); // Standard room rate is the base price
    }
    

}
