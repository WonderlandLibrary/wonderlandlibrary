/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.EnumFacingType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;

@ModuleInfo(name="AutoBow", description="Automatically shoots an arrow whenever your bow is fully loaded.", category=ModuleCategory.COMBAT)
public final class AutoBow
extends Module {
    private final BoolValue waitForBowAimbot = new BoolValue("WaitForBowAimbot", true);

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP;
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(BowAimbot.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot");
        }
        BowAimbot bowAimbot = (BowAimbot)module;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if ((iEntityPlayerSP = iEntityPlayerSP2).isUsingItem()) {
            IItemStack iItemStack = iEntityPlayerSP.getHeldItem();
            if (MinecraftInstance.classProvider.isItemBow(iItemStack != null ? iItemStack.getItem() : null) && iEntityPlayerSP.getItemInUseDuration() > 20 && (!((Boolean)this.waitForBowAimbot.get()).booleanValue() || !bowAimbot.getState() || bowAimbot.hasTarget())) {
                iEntityPlayerSP.stopUsingItem();
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            }
        }
    }
}

