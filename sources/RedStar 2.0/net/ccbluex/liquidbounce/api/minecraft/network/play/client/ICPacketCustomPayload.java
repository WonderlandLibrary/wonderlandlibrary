package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.network.IPacketBuffer;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\n\n\b\bf\u000020R0X¦¢\bR0X¦¢\f\b\b\t\"\b\n¨\f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCustomPayload;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "channelName", "", "getChannelName", "()Ljava/lang/String;", "data", "Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;", "getData", "()Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;", "setData", "(Lnet/ccbluex/liquidbounce/api/network/IPacketBuffer;)V", "Pride"})
public interface ICPacketCustomPayload
extends IPacket {
    @NotNull
    public IPacketBuffer getData();

    public void setData(@NotNull IPacketBuffer var1);

    @NotNull
    public String getChannelName();
}
