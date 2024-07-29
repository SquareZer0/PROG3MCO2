/**
 * Represents an executive room in a hotel.
 */
public class ExecutiveRoom extends Room {

    public ExecutiveRoom(int roomNo, float basePrice) {
        super(roomNo, basePrice);
    }

    @Override
    public float getPrice() {
        return getBasePrice() * 1.35f; // Executive room rate is 35% more than base price
    }
}
