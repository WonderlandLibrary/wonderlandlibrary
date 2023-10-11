package com.initial.modules.impl.player;

import com.initial.modules.*;
import com.initial.events.impl.*;
import net.minecraft.potion.*;
import com.initial.events.*;

public class FastMine extends Module
{
    public FastMine() {
        super("FastMine", 0, Category.PLAYER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.thePlayer.removePotionEffect(Potion.digSpeed.getId());
    }
    
    @EventTarget
    public void onEvent(final EventUpdate event) {
        if (!this.isToggled()) {
            return;
        }
        this.setDisplayName("Fast Mine");
        if (this.mc.thePlayer != null && this.mc.theWorld != null) {
            this.mc.playerController.blockHitDelay = 0;
            final boolean item = this.mc.thePlayer.getCurrentEquippedItem() == null;
            this.mc.thePlayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 1000, item ? 3 : 0));
        }
    }
}
