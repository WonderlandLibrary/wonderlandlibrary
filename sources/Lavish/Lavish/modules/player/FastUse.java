// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class FastUse extends Module
{
    public FastUse() {
        super("FastUse", 0, true, Category.Player, "Uses items fast!");
    }
    
    @Override
    public void onUpdate() {
        if (FastUse.mc.thePlayer.getHeldItem() != null) {
            if (FastUse.mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || FastUse.mc.thePlayer.getHeldItem().getItem() instanceof ItemAppleGold || FastUse.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                if (FastUse.mc.thePlayer.isUsingItem()) {
                    FastUse.mc.timer.timerSpeed = 3.0f;
                }
                else {
                    FastUse.mc.timer.timerSpeed = 1.0f;
                }
            }
            else {
                FastUse.mc.timer.timerSpeed = 1.0f;
            }
        }
        else {
            FastUse.mc.timer.timerSpeed = 1.0f;
        }
    }
}
