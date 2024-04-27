package net.ccbluex.liquidbounce.api.minecraft.network.play.server;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\b\n\b\r\bf\u000020R0X¦¢\bR0X¦¢\f\b\"\b\b\tR\n0X¦¢\f\b\"\b\f\tR\r0X¦¢\f\b\"\b\t¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntityVelocity;", "", "entityID", "", "getEntityID", "()I", "motionX", "getMotionX", "setMotionX", "(I)V", "motionY", "getMotionY", "setMotionY", "motionZ", "getMotionZ", "setMotionZ", "Pride"})
public interface ISPacketEntityVelocity {
    public int getMotionX();

    public void setMotionX(int var1);

    public int getMotionY();

    public void setMotionY(int var1);

    public int getMotionZ();

    public void setMotionZ(int var1);

    public int getEntityID();
}
