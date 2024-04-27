package net.ccbluex.liquidbounce.api.minecraft.network.handshake.client;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IEnumConnectionState;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\n\u0000\n\n\b\n\b\n\b\n\n\b\bf\u000020R0X¦¢\f\b\"\bR\b0\tX¦¢\b\nR\f0\rX¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/handshake/client/ICPacketHandshake;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "ip", "", "getIp", "()Ljava/lang/String;", "setIp", "(Ljava/lang/String;)V", "port", "", "getPort", "()I", "requestedState", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IEnumConnectionState;", "getRequestedState", "()Lnet/ccbluex/liquidbounce/api/minecraft/network/IEnumConnectionState;", "Pride"})
public interface ICPacketHandshake
extends IPacket {
    public int getPort();

    @NotNull
    public String getIp();

    public void setIp(@NotNull String var1);

    @NotNull
    public IEnumConnectionState getRequestedState();
}
