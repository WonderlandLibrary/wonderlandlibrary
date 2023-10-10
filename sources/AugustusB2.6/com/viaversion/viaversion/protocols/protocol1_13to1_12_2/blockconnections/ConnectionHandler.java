// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.blockconnections.providers.BlockConnectionProvider;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.connection.UserConnection;

public abstract class ConnectionHandler
{
    public abstract int connect(final UserConnection p0, final Position p1, final int p2);
    
    public int getBlockData(final UserConnection user, final Position position) {
        return Via.getManager().getProviders().get(BlockConnectionProvider.class).getBlockData(user, position.x(), position.y(), position.z());
    }
}
