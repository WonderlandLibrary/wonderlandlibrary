/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, d1={"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\t\u0010\u0000\u001a\u00020\u0001H\u0086\b\u001a\u001b\u0010\u0002\u001a\u00020\u00012\b\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\b\u00a8\u0006\u0007"}, d2={"createOpenInventoryPacket", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "createUseItemPacket", "itemStack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "hand", "Lnet/ccbluex/liquidbounce/api/enums/WEnumHand;", "LiKingSense"})
public final class CrossVersionUtilsKt {
    @NotNull
    public static final IPacket createUseItemPacket(@Nullable IItemStack itemStack, @NotNull WEnumHand hand) {
        int $i$f$createUseItemPacket = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)hand), (String)"hand");
        return WrapperImpl.INSTANCE.getClassProvider().createCPacketTryUseItem(hand);
    }

    @NotNull
    public static final IPacket createOpenInventoryPacket() {
        int $i$f$createOpenInventoryPacket = 0;
        return WrapperImpl.INSTANCE.getClassProvider().createCPacketEntityAction(LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer(), ICPacketEntityAction.WAction.OPEN_INVENTORY);
    }
}

