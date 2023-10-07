package viaversion.viaversion.protocols.protocol1_12to1_11_1.storage;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.entities.Entity1_12Types.EntityType;
import viaversion.viaversion.api.storage.EntityTracker;

public class EntityTracker1_12 extends EntityTracker {

    public EntityTracker1_12(UserConnection user) {
        super(user, EntityType.PLAYER);
    }
}
