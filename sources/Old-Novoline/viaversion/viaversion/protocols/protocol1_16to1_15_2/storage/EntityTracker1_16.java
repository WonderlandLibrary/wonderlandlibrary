package viaversion.viaversion.protocols.protocol1_16to1_15_2.storage;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.entities.Entity1_16Types.EntityType;
import viaversion.viaversion.api.storage.EntityTracker;

public class EntityTracker1_16 extends EntityTracker {

    public EntityTracker1_16(UserConnection user) {
        super(user, EntityType.PLAYER);
    }
}
