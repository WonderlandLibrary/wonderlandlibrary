/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

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
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="FastBow", description="Turns your bow into a machine gun.", category=ModuleCategory.COMBAT)
public final class FastBow
extends Module {
    private final IntegerValue packetsValue = new IntegerValue("Packets", 20, 3, 20);

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!iEntityPlayerSP2.isUsingItem()) {
            return;
        }
        IItemStack iItemStack = iEntityPlayerSP2.getInventory().getCurrentItemInHand();
        if (iItemStack != null && MinecraftInstance.classProvider.isItemBow(iItemStack.getItem())) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(WBlockPos.Companion.getORIGIN(), 255, iItemStack, 0.0f, 0.0f, 0.0f));
            float f = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : iEntityPlayerSP2.getRotationYaw();
            float f2 = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : iEntityPlayerSP2.getRotationPitch();
            int n = ((Number)this.packetsValue.get()).intValue();
            for (int i = 0; i < n; ++i) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerLook(f, f2, true));
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            iEntityPlayerSP2.setItemInUseCount(iItemStack.getMaxItemUseDuration() - 1);
        }
    }
}

