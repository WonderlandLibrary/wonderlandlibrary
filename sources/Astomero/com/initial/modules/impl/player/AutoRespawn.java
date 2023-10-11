package com.initial.modules.impl.player;

import com.initial.modules.*;
import com.initial.events.impl.*;
import com.initial.events.*;

public class AutoRespawn extends Module
{
    public AutoRespawn() {
        super("AutoRespawn", 0, Category.PLAYER);
    }
    
    @EventTarget
    public void onPre(final EventUpdate e) {
        this.setDisplayName("Auto Respawn");
        if (!this.mc.thePlayer.isEntityAlive()) {
            this.mc.thePlayer.respawnPlayer();
        }
    }
}
