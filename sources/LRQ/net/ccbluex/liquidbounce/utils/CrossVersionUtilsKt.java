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
    public static final IPacket createUseItemPacket(@Nullable IItemStack itemStack, WEnumHand hand) {
        int $i$f$createUseItemPacket = 0;
        return WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand);
    }

    public static final IPacket createOpenInventoryPacket() {
        int $i$f$createOpenInventoryPacket = 0;
        IClassProvider iClassProvider = WrapperImpl.INSTANCE.getClassProvider();
        IEntityPlayerSP iEntityPlayerSP = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        return iClassProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.OPEN_INVENTORY);
    }
}

