// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2;

import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage.TabCompleteTracker;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.Via;

public class TabCompleteThread implements Runnable
{
    @Override
    public void run() {
        for (final UserConnection info : Via.getManager().getConnectionManager().getConnections()) {
            if (info.getProtocolInfo() == null) {
                continue;
            }
            if (!info.getProtocolInfo().getPipeline().contains(Protocol1_13To1_12_2.class) || !info.getChannel().isOpen()) {
                continue;
            }
            info.get(TabCompleteTracker.class).sendPacketToServer(info);
        }
    }
}
