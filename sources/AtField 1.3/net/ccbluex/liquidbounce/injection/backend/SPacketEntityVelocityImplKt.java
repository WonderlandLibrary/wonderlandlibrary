/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.injection.backend.SPacketEntityVelocityImpl;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public final class SPacketEntityVelocityImplKt {
    public static final SPacketEntityVelocity unwrap(ISPacketEntityVelocity iSPacketEntityVelocity) {
        boolean bl = false;
        return (SPacketEntityVelocity)((SPacketEntityVelocityImpl)iSPacketEntityVelocity).getWrapped();
    }

    public static final ISPacketEntityVelocity wrap(SPacketEntityVelocity sPacketEntityVelocity) {
        boolean bl = false;
        return new SPacketEntityVelocityImpl(sPacketEntityVelocity);
    }
}

