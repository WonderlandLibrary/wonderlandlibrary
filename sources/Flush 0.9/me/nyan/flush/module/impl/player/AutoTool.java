package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.module.Module;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool", Category.PLAYER);
    }

    private int lastSlot;
    private boolean clicked;

    @Override
    public void onEnable() {
        super.onEnable();
        lastSlot = -1;
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
                mc.gameSettings.keyBindAttack.isKeyDown()) {
            if (!clicked) {
                Block block = mc.objectMouseOver.getBlockPos().getBlock();
                float strengh = 1.0F;
                int best = -1;
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
                    if (stack == null) {
                        continue;
                    }

                    if (stack.getStrVsBlock(block) > strengh) {
                        strengh = stack.getStrVsBlock(block);
                        best = i;
                    }
                }

                if (mc.thePlayer.inventory.currentItem != best) {
                    lastSlot = mc.thePlayer.inventory.currentItem;
                    clicked = true;
                }

                if (best != -1 && mc.thePlayer.inventory.currentItem != best) {
                    mc.thePlayer.inventory.currentItem = best;
                }
            }
        } else if (clicked) {
            if (lastSlot != -1) {
                mc.thePlayer.inventory.currentItem = lastSlot;
            }
            clicked = false;
        }
    }
}
