package net.ccbluex.liquidbounce.api.minecraft.network.play.server;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\b\bf\u000020R0X¦¢\f\b\"\bR\b0X¦¢\f\b\t\"\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketPosLook;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "pitch", "", "getPitch", "()F", "setPitch", "(F)V", "yaw", "getYaw", "setYaw", "Pride"})
public interface ISPacketPosLook
extends IPacket {
    public float getYaw();

    public void setYaw(float var1);

    public float getPitch();

    public void setPitch(float var1);
}
