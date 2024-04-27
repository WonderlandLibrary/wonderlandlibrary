package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import kotlin.jvm.JvmName;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\n\u0000\n\n\b\b\n\n\b\b\n\n\b\bf\u000020R0X¦¢\f\b\"\bR\b0X¦¢\f\b\t\"\b\nR0\fX¦¢\f\b\r\"\bR08gX¦¢\f\b\"\bR0X¦¢\f\b\"\bR0X¦¢\f\b\"\bR0\fX¦¢\f\b\"\bR 0X¦¢\f\b!\"\b\"¨#"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayer;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "ismoving", "", "getIsmoving", "()Z", "setIsmoving", "(Z)V", "onGround", "getOnGround", "setOnGround", "pitch", "", "getPitch", "()F", "setPitch", "(F)V", "rotating", "isRotating", "setRotating", "x", "", "getX", "()D", "setX", "(D)V", "y", "getY", "setY", "yaw", "getYaw", "setYaw", "z", "getZ", "setZ", "Pride"})
public interface ICPacketPlayer
extends IPacket {
    public double getX();

    public void setX(double var1);

    public double getY();

    public void setY(double var1);

    public double getZ();

    public void setZ(double var1);

    public float getYaw();

    public void setYaw(float var1);

    public float getPitch();

    public void setPitch(float var1);

    public boolean getOnGround();

    public void setOnGround(boolean var1);

    public boolean getIsmoving();

    public void setIsmoving(boolean var1);

    @JvmName(name="isRotating")
    public boolean isRotating();

    public void setRotating(boolean var1);
}
