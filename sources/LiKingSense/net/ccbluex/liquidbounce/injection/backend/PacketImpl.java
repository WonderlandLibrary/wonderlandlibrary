/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
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
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000~\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\b\u0016\u0018\u0000*\f\b\u0000\u0010\u0001*\u0006\u0012\u0002\b\u00030\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0005J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\u001eH\u0016J\b\u0010\u001f\u001a\u00020 H\u0016J\b\u0010!\u001a\u00020\"H\u0016J\b\u0010#\u001a\u00020$H\u0016J\b\u0010%\u001a\u00020&H\u0016J\b\u0010'\u001a\u00020(H\u0016J\u0013\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0096\u0002R\u0013\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006-"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "T", "Lnet/minecraft/network/Packet;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "wrapped", "(Lnet/minecraft/network/Packet;)V", "getWrapped", "()Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/Packet;", "asCPacketChatMessage", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketChatMessage;", "asCPacketCustomPayload", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCustomPayload;", "asCPacketHandshake", "Lnet/ccbluex/liquidbounce/api/minecraft/network/handshake/client/ICPacketHandshake;", "asCPacketHeldItemChange", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketHeldItemChange;", "asCPacketPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayer;", "asCPacketPlayerDigging", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging;", "asCPacketUseEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity;", "asSPacketAnimation", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketAnimation;", "asSPacketChat", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketChat;", "asSPacketCloseWindow", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ISPacketCloseWindow;", "asSPacketEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntity;", "asSPacketEntityVelocity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntityVelocity;", "asSPacketPosLook", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketPosLook;", "asSPacketResourcePackSend", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketResourcePackSend;", "asSPacketTabComplete", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketTabComplete;", "asSPacketWindowItems", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketWindowItems;", "equals", "", "other", "", "LiKingSense"})
public class PacketImpl<T extends Packet<?>>
implements IPacket {
    @NotNull
    private final T wrapped;

    @Override
    @NotNull
    public ISPacketChat asSPacketChat() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketChat");
        }
        return new SPacketChatImpl<SPacketChat>((SPacketChat)t2);
    }

    @Override
    @NotNull
    public ISPacketAnimation asSPacketAnimation() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketAnimation");
        }
        return new SPacketAnimationImpl<SPacketAnimation>((SPacketAnimation)t2);
    }

    @Override
    @NotNull
    public ISPacketEntity asSPacketEntity() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketEntity");
        }
        return new SPacketEntityImpl<SPacketEntity>((SPacketEntity)t2);
    }

    @Override
    @NotNull
    public ICPacketPlayer asCPacketPlayer() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketPlayer");
        }
        return new CPacketPlayerImpl<CPacketPlayer>((CPacketPlayer)t2);
    }

    @Override
    @NotNull
    public ICPacketUseEntity asCPacketUseEntity() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketUseEntity");
        }
        return new CPacketUseEntityImpl<CPacketUseEntity>((CPacketUseEntity)t2);
    }

    @Override
    @NotNull
    public ISPacketEntityVelocity asSPacketEntityVelocity() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketEntityVelocity");
        }
        return new SPacketEntityVelocityImpl<SPacketEntityVelocity>((SPacketEntityVelocity)t2);
    }

    @Override
    @NotNull
    public ICPacketChatMessage asCPacketChatMessage() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketChatMessage");
        }
        return new CPacketChatMessageImpl<CPacketChatMessage>((CPacketChatMessage)t2);
    }

    @Override
    @NotNull
    public ISPacketCloseWindow asSPacketCloseWindow() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketCloseWindow");
        }
        return new SPacketCloseWindowImpl<SPacketCloseWindow>((SPacketCloseWindow)t2);
    }

    @Override
    @NotNull
    public ISPacketTabComplete asSPacketTabComplete() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketTabComplete");
        }
        return new SPacketTabCompleteImpl<SPacketTabComplete>((SPacketTabComplete)t2);
    }

    @Override
    @NotNull
    public ISPacketPosLook asSPacketPosLook() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketPlayerPosLook");
        }
        return new SPacketPosLookImpl<SPacketPlayerPosLook>((SPacketPlayerPosLook)t2);
    }

    @Override
    @NotNull
    public ISPacketResourcePackSend asSPacketResourcePackSend() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketResourcePackSend");
        }
        return new SPacketResourcePackSendImpl<SPacketResourcePackSend>((SPacketResourcePackSend)t2);
    }

    @Override
    @NotNull
    public ICPacketHeldItemChange asCPacketHeldItemChange() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketHeldItemChange");
        }
        return new CPacketHeldItemChangeImpl<CPacketHeldItemChange>((CPacketHeldItemChange)t2);
    }

    @Override
    @NotNull
    public ISPacketWindowItems asSPacketWindowItems() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.SPacketWindowItems");
        }
        return new SPacketWindowItemsImpl<SPacketWindowItems>((SPacketWindowItems)t2);
    }

    @Override
    @NotNull
    public ICPacketCustomPayload asCPacketCustomPayload() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketCustomPayload");
        }
        return new CPacketCustomPayloadImpl<CPacketCustomPayload>((CPacketCustomPayload)t2);
    }

    @Override
    @NotNull
    public ICPacketHandshake asCPacketHandshake() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.handshake.client.C00Handshake");
        }
        return new CPacketHandshakeImpl<C00Handshake>((C00Handshake)t2);
    }

    @Override
    @NotNull
    public ICPacketPlayerDigging asCPacketPlayerDigging() {
        T t2 = this.wrapped;
        if (t2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.client.CPacketPlayerDigging");
        }
        return new CPacketPlayerDiggingImpl<CPacketPlayerDigging>((CPacketPlayerDigging)t2);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof PacketImpl && Intrinsics.areEqual(((PacketImpl)other).wrapped, this.wrapped);
    }

    @NotNull
    public final T getWrapped() {
        return this.wrapped;
    }

    public PacketImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

