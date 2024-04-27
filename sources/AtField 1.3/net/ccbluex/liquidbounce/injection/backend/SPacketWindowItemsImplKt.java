/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketWindowItems
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketWindowItems;
import net.ccbluex.liquidbounce.injection.backend.SPacketWindowItemsImpl;
import net.minecraft.network.play.server.SPacketWindowItems;

public final class SPacketWindowItemsImplKt {
    public static final SPacketWindowItems unwrap(ISPacketWindowItems iSPacketWindowItems) {
        boolean bl = false;
        return (SPacketWindowItems)((SPacketWindowItemsImpl)iSPacketWindowItems).getWrapped();
    }

    public static final ISPacketWindowItems wrap(SPacketWindowItems sPacketWindowItems) {
        boolean bl = false;
        return new SPacketWindowItemsImpl(sPacketWindowItems);
    }
}

