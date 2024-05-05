package com.initial.modules.impl.player;

import com.initial.modules.*;
import com.initial.events.impl.*;
import org.lwjgl.input.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import com.initial.events.*;

public class AutoTool extends Module
{
    private int oldSlot;
    private boolean wasBreaking;
    
    public AutoTool() {
        super("AutoTool", 0, Category.PLAYER);
        this.oldSlot = -1;
        this.wasBreaking = false;
    }
    
    @EventTarget
    public void onEvent(final EventUpdate e) {
        this.setDisplayName("Auto Tool");
        if (this.mc.currentScreen == null && this.mc.thePlayer != null && this.mc.theWorld != null && this.mc.objectMouseOver != null && this.mc.objectMouseOver.getBlockPos() != null && this.mc.objectMouseOver.entityHit == null && Mouse.isButtonDown(0)) {
            float bestSpeed = 1.0f;
            int bestSlot = -1;
            final Block block = this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock();
            for (int k = 0; k < 9; ++k) {
                final ItemStack item = this.mc.thePlayer.inventory.getStackInSlot(k);
                if (item != null) {
                    final float speed = item.getStrVsBlock(block);
                    if (speed > bestSpeed) {
                        bestSpeed = speed;
                        bestSlot = k;
                    }
                }
            }
            if (bestSlot != -1 && this.mc.thePlayer.inventory.currentItem != bestSlot) {
                this.mc.thePlayer.inventory.currentItem = bestSlot;
                this.wasBreaking = true;
            }
            else if (bestSlot == -1) {
                if (this.wasBreaking) {
                    this.mc.thePlayer.inventory.currentItem = this.oldSlot;
                    this.wasBreaking = false;
                }
                this.oldSlot = this.mc.thePlayer.inventory.currentItem;
            }
        }
        else if (this.mc.thePlayer != null && this.mc.theWorld != null) {
            if (this.wasBreaking) {
                this.mc.thePlayer.inventory.currentItem = this.oldSlot;
                this.wasBreaking = false;
            }
            this.oldSlot = this.mc.thePlayer.inventory.currentItem;
        }
    }
}
