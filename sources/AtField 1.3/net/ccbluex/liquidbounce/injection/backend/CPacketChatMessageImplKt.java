/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketChatMessage;
import net.ccbluex.liquidbounce.injection.backend.CPacketChatMessageImpl;
import net.minecraft.network.play.client.CPacketChatMessage;

public final class CPacketChatMessageImplKt {
    public static final CPacketChatMessage unwrap(ICPacketChatMessage iCPacketChatMessage) {
        boolean bl = false;
        return (CPacketChatMessage)((CPacketChatMessageImpl)iCPacketChatMessage).getWrapped();
    }

    public static final ICPacketChatMessage wrap(CPacketChatMessage cPacketChatMessage) {
        boolean bl = false;
        return new CPacketChatMessageImpl(cPacketChatMessage);
    }
}

