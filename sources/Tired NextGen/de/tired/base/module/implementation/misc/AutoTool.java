package de.tired.base.module.implementation.misc;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

@ModuleAnnotation(name = "AutoTool", category = ModuleCategory.MISC, clickG = "Select best tool while trying to destroy something")
public class AutoTool extends Module {



    private int oldSlot = -1;
    private boolean wasBreaking = false;
    @EventTarget
    public void onUpdate(UpdateEvent e) {
        if (this.MC.currentScreen == null && MC.thePlayer != null && MC.theWorld != null && this.MC.objectMouseOver != null && this.MC.objectMouseOver.getBlockPos() != null && this.MC.objectMouseOver.entityHit == null && Mouse.isButtonDown(0)) {
            float bestSpeed = 1.0F;
            int bestSlot = -1;
            Block block = MC.theWorld.getBlockState(this.MC.objectMouseOver.getBlockPos()).getBlock();
            for (int k = 0; k < 9; k++) {
                ItemStack item = MC.thePlayer.inventory.getStackInSlot(k);
                if (item != null) {
                    float speed = item.getStrVsBlock(block);
                    if (speed > bestSpeed) {
                        bestSpeed = speed;
                        bestSlot = k;
                    }
                }
            }
            if (bestSlot != -1 && MC.thePlayer.inventory.currentItem != bestSlot) {
                MC.thePlayer.inventory.currentItem = bestSlot;
                this.wasBreaking = true;
            } else if (bestSlot == -1) {
                if (this.wasBreaking) {
                    MC.thePlayer.inventory.currentItem = this.oldSlot;
                    this.wasBreaking = false;
                }
                this.oldSlot = MC.thePlayer.inventory.currentItem;
            }
        } else if (MC.thePlayer != null && MC.theWorld != null) {
            if (this.wasBreaking) {
                MC.thePlayer.inventory.currentItem = this.oldSlot;
                this.wasBreaking = false;
            }
            this.oldSlot = MC.thePlayer.inventory.currentItem;
        }
    }
    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }


}
