/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.network.play.server;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\r\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0018\u0010\u0006\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u0007\u0010\u0005\"\u0004\b\b\u0010\tR\u0018\u0010\n\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u000b\u0010\u0005\"\u0004\b\f\u0010\tR\u0018\u0010\r\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u0005\"\u0004\b\u000f\u0010\t\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntityVelocity;", "", "entityID", "", "getEntityID", "()I", "motionX", "getMotionX", "setMotionX", "(I)V", "motionY", "getMotionY", "setMotionY", "motionZ", "getMotionZ", "setMotionZ", "LiKingSense"})
public interface ISPacketEntityVelocity {
    public int getMotionX();

    public void setMotionX(int var1);

    public int getMotionY();

    public void setMotionY(int var1);

    public int getMotionZ();

    public void setMotionZ(int var1);

    public int getEntityID();
}

