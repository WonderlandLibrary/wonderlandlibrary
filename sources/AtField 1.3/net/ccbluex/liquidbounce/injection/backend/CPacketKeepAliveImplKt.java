/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketKeepAlive
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketKeepAlive;
import net.ccbluex.liquidbounce.injection.backend.CPacketKeepAliveImpl;
import net.minecraft.network.play.client.CPacketKeepAlive;

public final class CPacketKeepAliveImplKt {
    public static final ICPacketKeepAlive wrap(CPacketKeepAlive cPacketKeepAlive) {
        boolean bl = false;
        return new CPacketKeepAliveImpl(cPacketKeepAlive);
    }

    public static final CPacketKeepAlive unwrap(ICPacketKeepAlive iCPacketKeepAlive) {
        boolean bl = false;
        return (CPacketKeepAlive)((CPacketKeepAliveImpl)iCPacketKeepAlive).getWrapped();
    }
}

