/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketChatMessage;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006R$\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/CPacketChatMessageImpl;", "T", "Lnet/minecraft/network/play/client/CPacketChatMessage;", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketChatMessage;", "wrapped", "(Lnet/minecraft/network/play/client/CPacketChatMessage;)V", "value", "", "message", "getMessage", "()Ljava/lang/String;", "setMessage", "(Ljava/lang/String;)V", "LiKingSense"})
public final class CPacketChatMessageImpl<T extends CPacketChatMessage>
extends PacketImpl<T>
implements ICPacketChatMessage {
    @Override
    @NotNull
    public String getMessage() {
        String string = ((CPacketChatMessage)this.getWrapped()).field_149440_a;
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.message");
        return string;
    }

    @Override
    public void setMessage(@NotNull String value) {
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
        ((CPacketChatMessage)this.getWrapped()).field_149440_a = value;
    }

    public CPacketChatMessageImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((Packet)wrapped);
    }
}

