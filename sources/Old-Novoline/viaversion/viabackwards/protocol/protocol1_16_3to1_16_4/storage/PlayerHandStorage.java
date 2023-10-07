package viaversion.viabackwards.protocol.protocol1_16_3to1_16_4.storage;

import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;

public class PlayerHandStorage extends StoredObject {

    private int currentHand;

    public PlayerHandStorage(UserConnection user) {
        super(user);
    }

    public int getCurrentHand() {
        return currentHand;
    }

    public void setCurrentHand(int currentHand) {
        this.currentHand = currentHand;
    }
}
