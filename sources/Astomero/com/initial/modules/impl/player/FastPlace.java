package com.initial.modules.impl.player;

import com.initial.modules.*;
import com.initial.events.impl.*;
import com.initial.events.*;

public class FastPlace extends Module
{
    public FastPlace() {
        super("FastPlace", 0, Category.OTHER);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    @Override
    public void onEvent(final EventNigger e) {
        this.setDisplayName("Fast Place");
        this.mc.rightClickDelayTimer = 0;
    }
}
