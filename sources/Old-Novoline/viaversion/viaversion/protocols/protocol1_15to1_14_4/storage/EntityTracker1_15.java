package viaversion.viaversion.protocols.protocol1_15to1_14_4.storage;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.entities.Entity1_15Types.EntityType;
import viaversion.viaversion.api.storage.EntityTracker;

public class EntityTracker1_15 extends EntityTracker {

    public EntityTracker1_15(UserConnection user) {
        super(user, EntityType.PLAYER);
    }
}
