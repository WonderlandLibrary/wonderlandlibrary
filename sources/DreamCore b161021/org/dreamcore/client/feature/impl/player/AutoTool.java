package org.dreamcore.client.feature.impl.player;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventBlockInteract;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class AutoTool extends Feature {

    public AutoTool() {
        super("AutoTool", "Автоматически берет лучший инструмент в руки при ломании блока", Type.Player);
    }

    @EventTarget
    public void onBlockInteract(EventBlockInteract event) {
        BlockPos blockPos = mc.objectMouseOver.getBlockPos();
        Block block = mc.world.getBlockState(blockPos).getBlock();
        float power = 1;
        int itemCount = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.inventory.mainInventory.get(i);
            ItemStack current = mc.player.inventory.getCurrentItem();
            if (itemStack.getStrVsBlock(block.getDefaultState()) > power && !(current.getStrVsBlock(block.getDefaultState()) > power)) {
                power = itemStack.getStrVsBlock(block.getDefaultState());
                itemCount = i;
            }
        }
        if (itemCount != -1) {
            mc.player.inventory.currentItem = itemCount;
        }
    }
}