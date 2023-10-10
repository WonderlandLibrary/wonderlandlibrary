package client.module.impl.player;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.BlockClickEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "Auto Tool", description = "", category = Category.PLAYER)
public class AutoTool extends Module {
    @EventLink
    public final Listener<BlockClickEvent> onBlockClick = event -> {
        //if (event.getPos() == null) return;
        float bestSpeed = 1;
        int bestSlot = -1;
        final IBlockState blockState = mc.theWorld.getBlockState(event.getPos());
        for (int i = 0; i < 9; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null) continue;
            final float speed = itemStack.getStrVsBlock(blockState.getBlock());
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        if (bestSlot != -1) mc.thePlayer.inventory.currentItem = bestSlot;
    };
}