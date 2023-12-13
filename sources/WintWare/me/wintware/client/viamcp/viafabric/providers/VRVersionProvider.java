/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.viamcp.viafabric.providers;

import me.wintware.client.viamcp.viafabric.ViaFabric;
import me.wintware.client.viamcp.viafabric.platform.VRClientSideUserConnection;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.base.VersionProvider;

public class VRVersionProvider
extends VersionProvider {
    @Override
    public int getServerProtocol(UserConnection connection) throws Exception {
        if (connection instanceof VRClientSideUserConnection) {
            return ViaFabric.clientSideVersion;
        }
        return super.getServerProtocol(connection);
    }
}

