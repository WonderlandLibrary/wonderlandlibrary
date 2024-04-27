/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketEntity
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntity;
import net.ccbluex.liquidbounce.injection.backend.SPacketEntityImpl;
import net.minecraft.network.play.server.SPacketEntity;

public final class SPacketEntityImplKt {
    public static final SPacketEntity unwrap(ISPacketEntity $this$unwrap) {
        int $i$f$unwrap = 0;
        return (SPacketEntity)((SPacketEntityImpl)$this$unwrap).getWrapped();
    }

    public static final ISPacketEntity wrap(SPacketEntity $this$wrap) {
        int $i$f$wrap = 0;
        return new SPacketEntityImpl<SPacketEntity>($this$wrap);
    }
}

