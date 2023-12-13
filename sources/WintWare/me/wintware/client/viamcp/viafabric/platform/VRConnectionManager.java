/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.viamcp.viafabric.platform;

import me.wintware.client.viamcp.viafabric.platform.VRClientSideUserConnection;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.platform.ViaConnectionManager;

public class VRConnectionManager
extends ViaConnectionManager {
    @Override
    public boolean isFrontEnd(UserConnection connection) {
        return !(connection instanceof VRClientSideUserConnection);
    }
}

