/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.util.text.ChatType
 *  net.minecraft.util.text.ITextComponent
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketChat;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

public final class SPacketChatImpl
extends PacketImpl
implements ISPacketChat {
    @Override
    public ITextComponent getChatComponent() {
        return ((SPacketChat)this.getWrapped()).func_148915_c();
    }

    public SPacketChatImpl(SPacketChat sPacketChat) {
        super((Packet)sPacketChat);
    }

    @Override
    public ITextComponent getGetChat() {
        return ((SPacketChat)this.getWrapped()).func_148915_c();
    }

    @Override
    public ChatType getType() {
        return ((SPacketChat)this.getWrapped()).func_192590_c();
    }
}

