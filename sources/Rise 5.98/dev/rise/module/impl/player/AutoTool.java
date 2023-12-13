package dev.rise.module.impl.player;

import dev.rise.event.impl.motion.PostMotionEvent;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.other.BlockBreakEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "AutoTool", description = "Switches to the best tool for the job", category = Category.PLAYER)
public final class AutoTool extends Module {

    private Integer sword, tool;
    private BlockPos blockPos;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (tool != null && blockPos != null) {
            if (mc.thePlayer.inventory.currentItem != tool)
                mc.thePlayer.inventory.currentItem = tool;

            blockPos = null;
            tool = null;
        }

        if (sword != null) {
            if (mc.thePlayer.inventory.currentItem != sword && !(mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver.getBlockPos() != null))
                mc.thePlayer.inventory.currentItem = sword;

            sword = null;
        }
    }

    @Override
    public void onPostMotion(final PostMotionEvent event) {
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
        final int slot = PlayerUtil.findSword();

        if (slot != -1)
            sword = slot;
    }

    @Override
    public void onBlockBreak(final BlockBreakEvent event) {
        final int slot = getBestItem(event.getBlockPos());

        if (slot != -1) {
            tool = slot;
            blockPos = event.getBlockPos();
        }
    }

    private int getBestItem(final BlockPos blockPos) {
        float bestSpeed = 1F;
        int bestSlot = -1;

        final IBlockState blockState = mc.theWorld.getBlockState(blockPos);

        for (int i = 0; i < 9; i++) {
            final ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
            if (item == null) continue;

            final float speed = item.getStrVsBlock(blockState.getBlock());

            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        return bestSlot;
    }
}