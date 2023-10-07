package viaversion.viaversion.protocols.protocol1_13to1_12_2.storage;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.entities.Entity1_13Types.EntityType;
import viaversion.viaversion.api.storage.EntityTracker;

public class EntityTracker1_13 extends EntityTracker {

    public EntityTracker1_13(UserConnection user) {
        super(user, EntityType.PLAYER);
    }
}
