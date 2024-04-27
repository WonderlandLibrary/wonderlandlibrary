/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.CPacketCustomPayload
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import net.ccbluex.liquidbounce.api.network.IPacketBuffer;
import net.ccbluex.liquidbounce.injection.backend.PacketBufferImpl;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR$\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/CPacketCustomPayloadImpl;", "T", "Lnet/minecraft/network/play/client/CPacketCustomPayload;", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCustomPayload;", "wrapped", "(Lnet/minecraft/network/play/client/CPacketCustomPayload;)V", "channelName", "", "getChannelName", "()Ljava/lang/String;", "value", "Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;", "data", "getData", "()Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;", "setData", "(Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;)V", "LiKingSense"})
public final class CPacketCustomPayloadImpl<T extends CPacketCustomPayload>
extends PacketImpl<T>
implements ICPacketCustomPayload {
    @Override
    @NotNull
    public IPacketBuffer getData() {
        PacketBuffer packetBuffer = ((CPacketCustomPayload)this.getWrapped()).field_149561_c;
        Intrinsics.checkExpressionValueIsNotNull((Object)packetBuffer, (String)"wrapped.data");
        PacketBuffer $this$wrap$iv = packetBuffer;
        boolean $i$f$wrap = false;
        return new PacketBufferImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setData(@NotNull IPacketBuffer value) {
        void $this$unwrap$iv;
        PacketBuffer packetBuffer;
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
        IPacketBuffer iPacketBuffer = value;
        CPacketCustomPayload cPacketCustomPayload = (CPacketCustomPayload)this.getWrapped();
        boolean $i$f$unwrap = false;
        cPacketCustomPayload.field_149561_c = packetBuffer = ((PacketBufferImpl)$this$unwrap$iv).getWrapped();
    }

    @Override
    @NotNull
    public String getChannelName() {
        String string = ((CPacketCustomPayload)this.getWrapped()).func_149559_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.channelName");
        return string;
    }

    public CPacketCustomPayloadImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((Packet)wrapped);
    }
}

