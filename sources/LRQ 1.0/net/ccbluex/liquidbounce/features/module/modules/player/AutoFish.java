/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.MinecraftVersion;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;

@ModuleInfo(name="AutoFish", description="Automatically catches fish when using a rod.", category=ModuleCategory.PLAYER, supportedVersions={MinecraftVersion.MC_1_8})
public final class AutoFish
extends Module {
    private final MSTimer rodOutTimer = new MSTimer();

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        IEntityPlayerSP thePlayer;
        block6: {
            block5: {
                IEntityPlayerSP iEntityPlayerSP = thePlayer = MinecraftInstance.mc.getThePlayer();
                if ((iEntityPlayerSP != null ? iEntityPlayerSP.getHeldItem() : null) == null) break block5;
                IItemStack iItemStack = thePlayer.getHeldItem();
                if (iItemStack == null) {
                    Intrinsics.throwNpe();
                }
                if (MinecraftInstance.classProvider.isItemFishingRod(iItemStack.getItem())) break block6;
            }
            return;
        }
        IEntity fishEntity = thePlayer.getFishEntity();
        if (this.rodOutTimer.hasTimePassed(500L) && fishEntity == null || fishEntity != null && fishEntity.getMotionX() == 0.0 && fishEntity.getMotionZ() == 0.0 && fishEntity.getMotionY() != 0.0) {
            MinecraftInstance.mc.rightClickMouse();
            this.rodOutTimer.reset();
        }
    }
}

