// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class Eagle extends Module
{
    public Eagle() {
        super("Eagle", 0, "Automatically shifts for you", Category.PLAYER);
    }
    
    @Override
    public void onDisable() {
        Eagle.mc.gameSettings.keyBindSneak.pressed = false;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventUpdate && Eagle.mc.thePlayer != null && Eagle.mc.theWorld != null) {
            final ItemStack i = Eagle.mc.thePlayer.getCurrentEquippedItem();
            final BlockPos Bp = new BlockPos(Eagle.mc.thePlayer.posX, Eagle.mc.thePlayer.posY - 1.0, Eagle.mc.thePlayer.posZ);
            if (i != null && i.getItem() instanceof ItemBlock) {
                Eagle.mc.gameSettings.keyBindSneak.pressed = false;
                if (Eagle.mc.theWorld.getBlockState(Bp).getBlock() == Blocks.air) {
                    Eagle.mc.gameSettings.keyBindSneak.pressed = true;
                }
            }
        }
    }
}
