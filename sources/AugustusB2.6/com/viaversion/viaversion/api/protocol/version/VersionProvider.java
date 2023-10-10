// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.api.protocol.version;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;

public interface VersionProvider extends Provider
{
    int getClosestServerProtocol(final UserConnection p0) throws Exception;
}
