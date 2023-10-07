package viaversion.viaversion.protocols.protocol1_9to1_8.providers;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.platform.providers.Provider;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;

public class EntityIdProvider implements Provider {

    public int getEntityId(UserConnection user) throws Exception {
        return user.get(EntityTracker1_9.class).getClientEntityId();
    }
}
