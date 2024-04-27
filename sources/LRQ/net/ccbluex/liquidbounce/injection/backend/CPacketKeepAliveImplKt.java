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
    public static final CPacketKeepAlive unwrap(ICPacketKeepAlive $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketKeepAlive)((CPacketKeepAliveImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketKeepAlive wrap(CPacketKeepAlive $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketKeepAliveImpl<CPacketKeepAlive>($this$wrap);
    }
}

