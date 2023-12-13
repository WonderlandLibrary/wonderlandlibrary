// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import com.elementars.eclient.util.Wrapper;
import net.minecraft.item.ItemExpBottle;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class EXPFast extends Module
{
    public EXPFast() {
        super("EXPFast", "XP fast zoom", 0, Category.COMBAT, true);
    }
    
    @Override
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        if (Wrapper.getMinecraft().player.inventory.getCurrentItem().getItem() instanceof ItemExpBottle) {
            Wrapper.getMinecraft().rightClickDelayTimer = 0;
        }
    }
}
