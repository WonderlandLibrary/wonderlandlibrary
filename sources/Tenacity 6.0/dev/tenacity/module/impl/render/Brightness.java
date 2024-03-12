// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.module.Category;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;

public final class Brightness extends Module
{
    @Override
    public void onMotionEvent(final MotionEvent event) {
        Brightness.mc.gameSettings.gammaSetting = 100.0f;
    }
    
    @Override
    public void onDisable() {
        Brightness.mc.gameSettings.gammaSetting = 0.0f;
        super.onDisable();
    }
    
    public Brightness() {
        super("Brightness", "Brightness", Category.RENDER, "changes the game brightness");
    }
}
