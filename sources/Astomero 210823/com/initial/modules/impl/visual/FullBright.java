package com.initial.modules.impl.visual;

import com.initial.modules.*;
import com.initial.events.impl.*;
import com.initial.events.*;

public class FullBright extends Module
{
    private float oldBrightness;
    
    public FullBright() {
        super("Full Bright", 0, Category.VISUAL);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        this.mc.gameSettings.gammaSetting = 100.0f;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.oldBrightness = this.mc.gameSettings.gammaSetting;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.gameSettings.gammaSetting = this.oldBrightness;
    }
}
