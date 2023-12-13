// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import net.minecraft.entity.Entity;
import com.elementars.eclient.command.Command;
import net.minecraft.entity.passive.EntityDonkey;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class DonkeyAlert extends Module
{
    int delay;
    
    public DonkeyAlert() {
        super("DonkeyAlert", "Alerts you when a donkey is in your render distance", 0, Category.MISC, true);
    }
    
    @Override
    public void onEnable() {
        this.delay = 0;
    }
    
    @Override
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
        DonkeyAlert.mc.world.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityDonkey && this.delay == 0) {
                Command.sendChatMessage("Donkey spotted!");
                this.delay = 200;
            }
        });
    }
}
