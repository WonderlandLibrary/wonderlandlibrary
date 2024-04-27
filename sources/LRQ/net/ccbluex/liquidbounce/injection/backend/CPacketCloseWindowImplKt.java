/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketCloseWindow
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCloseWindow;
import net.ccbluex.liquidbounce.injection.backend.CPacketCloseWindowImpl;
import net.minecraft.network.play.client.CPacketCloseWindow;

public final class CPacketCloseWindowImplKt {
    public static final CPacketCloseWindow unwrap(ICPacketCloseWindow $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketCloseWindow)((CPacketCloseWindowImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketCloseWindow wrap(CPacketCloseWindow $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketCloseWindowImpl<CPacketCloseWindow>($this$wrap);
    }
}

