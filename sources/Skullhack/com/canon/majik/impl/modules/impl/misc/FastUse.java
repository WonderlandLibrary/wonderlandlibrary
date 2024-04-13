package com.canon.majik.impl.modules.impl.misc;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemExpBottle;

public class FastUse extends Module {

    BooleanSetting exp = setting("Exp", true);
    BooleanSetting block = setting("Blocks", false);
    
    public FastUse(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onTick(TickEvent event){
        if(nullCheck()) return;
        if(exp.getValue() && mc.player.inventory.getCurrentItem().getItem() instanceof ItemExpBottle){
            mc.rightClickDelayTimer = 0;
        }
        if(block.getValue() && mc.player.inventory.getCurrentItem().getItem() instanceof ItemBlock){
            mc.rightClickDelayTimer = 0;
        }
    }
}
