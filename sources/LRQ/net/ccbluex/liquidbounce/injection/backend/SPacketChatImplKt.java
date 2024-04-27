/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketChat;
import net.ccbluex.liquidbounce.injection.backend.SPacketChatImpl;
import net.minecraft.network.play.server.SPacketChat;

public final class SPacketChatImplKt {
    public static final SPacketChat unwrap(ISPacketChat $this$unwrap) {
        int $i$f$unwrap = 0;
        return (SPacketChat)((SPacketChatImpl)$this$unwrap).getWrapped();
    }

    public static final ISPacketChat wrap(SPacketChat $this$wrap) {
        int $i$f$wrap = 0;
        return new SPacketChatImpl<SPacketChat>($this$wrap);
    }
}

