/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketCloseWindow
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ISPacketCloseWindow;
import net.ccbluex.liquidbounce.injection.backend.SPacketCloseWindowImpl;
import net.minecraft.network.play.server.SPacketCloseWindow;

public final class SPacketCloseWindowImplKt {
    public static final SPacketCloseWindow unwrap(ISPacketCloseWindow $this$unwrap) {
        int $i$f$unwrap = 0;
        return (SPacketCloseWindow)((SPacketCloseWindowImpl)$this$unwrap).getWrapped();
    }

    public static final ISPacketCloseWindow wrap(SPacketCloseWindow $this$wrap) {
        int $i$f$wrap = 0;
        return new SPacketCloseWindowImpl<SPacketCloseWindow>($this$wrap);
    }
}

