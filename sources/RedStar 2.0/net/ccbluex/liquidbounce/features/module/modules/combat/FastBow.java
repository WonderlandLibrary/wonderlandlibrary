package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="FastBow", description="Turns your bow into a machine gun.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\b\u000020B¢J020\bHR0X¢\n\u0000¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/FastBow;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "packetsValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class FastBow
extends Module {
    private final IntegerValue packetsValue = new IntegerValue("Packets", 20, 3, 20);

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (!thePlayer.isUsingItem()) {
            return;
        }
        IItemStack currentItem = thePlayer.getInventory().getCurrentItemInHand();
        if (currentItem != null && MinecraftInstance.classProvider.isItemBow(currentItem.getItem())) {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerBlockPlacement(WBlockPos.Companion.getORIGIN(), 255, currentItem, 0.0f, 0.0f, 0.0f));
            float yaw = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : thePlayer.getRotationYaw();
            float pitch = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : thePlayer.getRotationPitch();
            int n = 0;
            int n2 = ((Number)this.packetsValue.get()).intValue();
            while (n < n2) {
                void i;
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerLook(yaw, pitch, true));
                ++i;
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.Companion.getORIGIN(), MinecraftInstance.classProvider.getEnumFacing(EnumFacingType.DOWN)));
            thePlayer.setItemInUseCount(currentItem.getMaxItemUseDuration() - 1);
        }
    }
}
