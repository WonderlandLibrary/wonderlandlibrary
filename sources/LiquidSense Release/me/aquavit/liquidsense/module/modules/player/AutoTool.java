package me.aquavit.liquidsense.module.modules.player;

import me.aquavit.liquidsense.event.events.ClickBlockEvent;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "AutoTool", description = "Automatically selects the best tool in your inventory to mine a block.", category = ModuleCategory.PLAYER)
public class AutoTool extends Module {

    @EventTarget
    public void onClick(ClickBlockEvent event){
        BlockPos blockPos = event.getClickedBlock();
        if (blockPos == null) return;
        switchSlot(blockPos);
    }
    public void switchSlot(BlockPos pos) {
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        float strength = 1.0f;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || itemStack.getStrVsBlock(block) <= strength) continue;
            strength = itemStack.getStrVsBlock(block);
            bestItemIndex = i;
        }
        if (bestItemIndex != -1)
            mc.thePlayer.inventory.currentItem = bestItemIndex;
    }
}
