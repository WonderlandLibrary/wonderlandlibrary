package tech.atani.client.feature.module.impl.player;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;

@Native
@ModuleData(name = "AutoTool", description = "Switches to best tools", category = Category.PLAYER)
public class AutoTool extends Module {

    @Listen
    public void onMotionEvent(UpdateMotionEvent event) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;
        
        if(event.getType() == UpdateMotionEvent.Type.MID) {
            if (!mc.gameSettings.keyBindAttack.isKeyDown())
                return;

            if(mc.objectMouseOver == null)
                return;

            BlockPos position = mc.objectMouseOver.getBlockPos();
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

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}