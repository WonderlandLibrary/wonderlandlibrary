package viaversion.viaversion.protocols.protocol1_9to1_8.storage;

import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.minecraft.Position;

public class PlaceBlockTracker extends StoredObject {
    private long lastPlaceTimestamp = 0;
    private Position lastPlacedPosition = null;

    public PlaceBlockTracker(UserConnection user) {
        super(user);
    }

    /**
     * Check if a certain amount of time has passed
     *
     * @param ms The amount of time in MS
     * @return True if it has passed
     */
    public boolean isExpired(int ms) {
        return System.currentTimeMillis() > (lastPlaceTimestamp + ms);
    }

    /**
     * Set the last place time to the current time
     */
    public void updateTime() {
        lastPlaceTimestamp = System.currentTimeMillis();
    }

    public long getLastPlaceTimestamp() {
        return lastPlaceTimestamp;
    }

    public Position getLastPlacedPosition() {
        return lastPlacedPosition;
    }

    public void setLastPlacedPosition(Position lastPlacedPosition) {
        this.lastPlacedPosition = lastPlacedPosition;
    }
}
