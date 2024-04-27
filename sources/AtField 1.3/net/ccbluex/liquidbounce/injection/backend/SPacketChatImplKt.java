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
    public static final ISPacketChat wrap(SPacketChat sPacketChat) {
        boolean bl = false;
        return new SPacketChatImpl(sPacketChat);
    }

    public static final SPacketChat unwrap(ISPacketChat iSPacketChat) {
        boolean bl = false;
        return (SPacketChat)((SPacketChatImpl)iSPacketChat).getWrapped();
    }
}

