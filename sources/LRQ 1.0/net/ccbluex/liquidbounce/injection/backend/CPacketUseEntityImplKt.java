/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketUseEntity
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.injection.backend.CPacketUseEntityImpl;
import net.minecraft.network.play.client.CPacketUseEntity;

public final class CPacketUseEntityImplKt {
    public static final CPacketUseEntity unwrap(ICPacketUseEntity $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketUseEntity)((CPacketUseEntityImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketUseEntity wrap(CPacketUseEntity $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketUseEntityImpl<CPacketUseEntity>($this$wrap);
    }
}

