// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.storage;

import com.google.common.collect.Sets;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Set;
import com.viaversion.viaversion.api.connection.StoredObject;

public class ClientChunks extends StoredObject
{
    private final Set<Long> loadedChunks;
    
    public ClientChunks(final UserConnection connection) {
        super(connection);
        this.loadedChunks = Sets.newConcurrentHashSet();
    }
    
    public static long toLong(final int msw, final int lsw) {
        return ((long)msw << 32) + lsw + 2147483648L;
    }
    
    public Set<Long> getLoadedChunks() {
        return this.loadedChunks;
    }
}
