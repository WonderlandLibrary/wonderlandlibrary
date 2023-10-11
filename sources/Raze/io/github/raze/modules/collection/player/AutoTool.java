package io.github.raze.modules.collection.player;

import io.github.raze.events.collection.motion.EventMotion;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPosition;

public class AutoTool extends AbstractModule {

    public AutoTool() {
        super("AutoTool", "Equips the best tool automatically.", ModuleCategory.PLAYER);
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {

        if (!mc.gameSettings.keyBindAttack.isKeyDown())
            return;

        BlockPosition position = mc.objectMouseOver.getBlockPosition();
		if(position == null)
			return;
		
        Block block = mc.theWorld.getBlockState(position).getBlock();
		if(block == null)
			return;

        float best = 1F;
        int slot = 1000;

        for (int index = 0; index < 9; index += 1) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(index);

            if (itemStack == null)
                continue;

            float speed = itemStack.getStrVsBlock(block);

            if (speed > best) {
                best = speed;
                slot = index;
            }
        }

        if (slot == 1000)
            return;

        mc.thePlayer.inventory.currentItem = slot;
    }
    
}