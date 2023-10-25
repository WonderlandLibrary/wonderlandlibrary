package org.dreamcore.client.feature.impl.player;

import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.player.InventoryHelper;

public class MLG extends Feature {

    public MLG() {
        super("MLG", "Ставит воду под тобой", Type.Player);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        int oldSlot = mc.player.inventory.currentItem;
        if (mc.player.fallDistance > 5) {
            RayTraceResult traceResult = mc.world.rayTraceBlocks(mc.player.getPositionVector(), mc.player.getPositionVector().addVector(0, -15, 0), true, false, false);
            if (traceResult != null && InventoryHelper.findWaterBucket() > 0 && traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                event.setPitch(86);
                mc.player.inventory.currentItem = InventoryHelper.findWaterBucket();
                mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
                mc.player.inventory.currentItem = oldSlot;
            }
        }
    }

}
