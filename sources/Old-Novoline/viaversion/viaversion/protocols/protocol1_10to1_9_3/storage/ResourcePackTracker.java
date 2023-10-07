package viaversion.viaversion.protocols.protocol1_10to1_9_3.storage;

import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;

public class ResourcePackTracker extends StoredObject {
    private String lastHash = "";

    public ResourcePackTracker(UserConnection user) {
        super(user);
    }

    public String getLastHash() {
        return lastHash;
    }

    public void setLastHash(String lastHash) {
        this.lastHash = lastHash;
    }

    @Override
    public String toString() {
        return "ResourcePackTracker{" +
                "lastHash='" + lastHash + '\'' +
                '}';
    }
}
