// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.providers;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;

public class EntityIdProvider implements Provider
{
    public int getEntityId(final UserConnection user) throws Exception {
        return user.getEntityTracker(Protocol1_9To1_8.class).clientEntityId();
    }
}
