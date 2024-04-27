/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import org.jetbrains.annotations.Nullable;

public final class CrossVersionUtilsKt {
    public static final IPacket createOpenInventoryPacket() {
        boolean bl = false;
        IClassProvider iClassProvider = WrapperImpl.INSTANCE.getClassProvider();
        IEntityPlayerSP iEntityPlayerSP = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        return iClassProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.OPEN_INVENTORY);
    }

    public static final IPacket createUseItemPacket(@Nullable IItemStack iItemStack, WEnumHand wEnumHand) {
        boolean bl = false;
        return WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(wEnumHand);
    }
}

