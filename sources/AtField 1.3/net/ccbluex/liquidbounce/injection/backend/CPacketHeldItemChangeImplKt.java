/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.injection.backend.CPacketHeldItemChangeImpl;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public final class CPacketHeldItemChangeImplKt {
    public static final ICPacketHeldItemChange wrap(CPacketHeldItemChange cPacketHeldItemChange) {
        boolean bl = false;
        return new CPacketHeldItemChangeImpl(cPacketHeldItemChange);
    }

    public static final CPacketHeldItemChange unwrap(ICPacketHeldItemChange iCPacketHeldItemChange) {
        boolean bl = false;
        return (CPacketHeldItemChange)((CPacketHeldItemChangeImpl)iCPacketHeldItemChange).getWrapped();
    }
}

