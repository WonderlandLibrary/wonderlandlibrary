// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.movement;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBow;
import Lavish.event.events.EventMotion;
import Lavish.event.Event;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class LongJump extends Module
{
    public LongJump() {
        super("LongJump", 0, true, Category.Movement, "Makes you jump higher and longer");
    }
    
    @Override
    public void onEnable() {
    }
    
    @Override
    public void onDisable() {
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion) {
            final int prevSlot = LongJump.mc.thePlayer.inventory.currentItem;
            final int i = 0;
            while (i < 9) {
                final ItemStack itemStack = LongJump.mc.thePlayer.inventory.getStackInSlot(i);
                if (itemStack != null) {
                    final boolean b = itemStack.getItem() instanceof ItemBow;
                }
            }
        }
    }
}
