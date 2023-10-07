package viaversion.viaversion.protocols.protocol1_9to1_8.storage;

import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;

public class InventoryTracker extends StoredObject {
    private String inventory;

    public InventoryTracker(UserConnection user) {
        super(user);
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }
}
