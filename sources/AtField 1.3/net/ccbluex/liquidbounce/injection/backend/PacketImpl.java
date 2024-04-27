/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketCustomPayload
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.server.SPacketAnimation
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.network.play.server.SPacketCloseWindow
 *  net.minecraft.network.play.server.SPacketEntity
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketResourcePackSend
 *  net.minecraft.network.play.server.SPacketTabComplete
 *  net.minecraft.network.play.server.SPacketWindowItems
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.handshake.client.ICPacketHandshake;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketChatMessage;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ISPacketCloseWindow;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketAnimation;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketChat;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntity;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketPosLook;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketResourcePackSend;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketTabComplete;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketWindowItems;
import net.ccbluex.liquidbounce.injection.backend.CPacketChatMessageImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketCustomPayloadImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketHandshakeImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketHeldItemChangeImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketPlayerDiggingImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketPlayerImpl;
import net.ccbluex.liquidbounce.injection.backend.CPacketUseEntityImpl;
import net.ccbluex.liquidbounce.injection.backend.SPacketAnimationImpl;
import net.ccbluex.liquidbounce.injection.backend.SPacketChatImpl;
import net.ccbluex.liquidbounce.injection.backend.SPacketCloseWindowImpl;
import net.ccbluex.liquidbounce.injection.backend.SPacketEntityImpl;
import net.ccbluex.liquidbounce.injection.backend.SPacketEntityVelocityImpl;
import net.ccbluex.liquidbounce.injection.backend.SPacketPosLookImpl;
import net.ccbluex.liquidbounce.injection.backend.SPacketResourcePackSendImpl;
import net.ccbluex.liquidbounce.injection.backend.SPacketTabCompleteImpl;
import net.ccbluex.liquidbounce.injection.backend.SPacketWindowItemsImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketWindowItems;
import org.jetbrains.annotations.Nullable;

public class PacketImpl
implements IPacket {
    private final Packet wrapped;

    @Override
    public ICPacketHandshake asCPacketHandshake() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.handshake.client.C00Handshake");
        }
        return new CPacketHandshakeImpl((C00Handshake)packet);
    }

    @Override
    public ICPacketUseEntity asCPacketUseEntity() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketUseEntity");
        }
        return new CPacketUseEntityImpl((CPacketUseEntity)packet);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof PacketImpl && ((PacketImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public ISPacketAnimation asSPacketAnimation() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketAnimation");
        }
        return new SPacketAnimationImpl((SPacketAnimation)packet);
    }

    @Override
    public ISPacketResourcePackSend asSPacketResourcePackSend() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketResourcePackSend");
        }
        return new SPacketResourcePackSendImpl((SPacketResourcePackSend)packet);
    }

    @Override
    public ISPacketEntityVelocity asSPacketEntityVelocity() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketEntityVelocity");
        }
        return new SPacketEntityVelocityImpl((SPacketEntityVelocity)packet);
    }

    @Override
    public ICPacketPlayerDigging asCPacketPlayerDigging() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketPlayerDigging");
        }
        return new CPacketPlayerDiggingImpl((CPacketPlayerDigging)packet);
    }

    public PacketImpl(Packet packet) {
        this.wrapped = packet;
    }

    @Override
    public ICPacketChatMessage asCPacketChatMessage() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketChatMessage");
        }
        return new CPacketChatMessageImpl((CPacketChatMessage)packet);
    }

    @Override
    public ISPacketWindowItems asSPacketWindowItems() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketWindowItems");
        }
        return new SPacketWindowItemsImpl((SPacketWindowItems)packet);
    }

    @Override
    public ISPacketEntity asSPacketEntity() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketEntity");
        }
        return new SPacketEntityImpl((SPacketEntity)packet);
    }

    @Override
    public ISPacketCloseWindow asSPacketCloseWindow() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketCloseWindow");
        }
        return new SPacketCloseWindowImpl((SPacketCloseWindow)packet);
    }

    @Override
    public ISPacketTabComplete asSPacketTabComplete() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketTabComplete");
        }
        return new SPacketTabCompleteImpl((SPacketTabComplete)packet);
    }

    @Override
    public ICPacketHeldItemChange asCPacketHeldItemChange() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketHeldItemChange");
        }
        return new CPacketHeldItemChangeImpl((CPacketHeldItemChange)packet);
    }

    @Override
    public ICPacketPlayer asCPacketPlayer() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketPlayer");
        }
        return new CPacketPlayerImpl((CPacketPlayer)packet);
    }

    public final Packet getWrapped() {
        return this.wrapped;
    }

    @Override
    public ICPacketCustomPayload asCPacketCustomPayload() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketCustomPayload");
        }
        return new CPacketCustomPayloadImpl((CPacketCustomPayload)packet);
    }

    @Override
    public ISPacketPosLook asSPacketPosLook() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketPlayerPosLook");
        }
        return new SPacketPosLookImpl((SPacketPlayerPosLook)packet);
    }

    @Override
    public ISPacketChat asSPacketChat() {
        Packet packet = this.wrapped;
        if (packet == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketChat");
        }
        return new SPacketChatImpl((SPacketChat)packet);
    }
}

