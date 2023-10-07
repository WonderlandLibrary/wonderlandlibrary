package viaversion.viaversion.protocols.protocol1_16_2to1_16_1.storage;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.entities.Entity1_16_2Types.EntityType;
import viaversion.viaversion.api.storage.EntityTracker;

public class EntityTracker1_16_2 extends EntityTracker {

    public EntityTracker1_16_2(UserConnection user) {
        super(user, EntityType.PLAYER);
    }
}
