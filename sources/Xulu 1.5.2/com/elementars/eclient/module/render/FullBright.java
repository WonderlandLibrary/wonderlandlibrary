// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class FullBright extends Module
{
    float oldBrightness;
    
    public FullBright() {
        super("Fullbright", "Permanent brightness", 0, Category.RENDER, true);
    }
    
    @Override
    public void onEnable() {
        this.oldBrightness = FullBright.mc.gameSettings.gammaSetting;
        FullBright.mc.gameSettings.gammaSetting = 1000.0f;
    }
    
    @Override
    public void onDisable() {
        FullBright.mc.gameSettings.gammaSetting = this.oldBrightness;
    }
    
    @Override
    public void onUpdate() {
        if (FullBright.mc.gameSettings.gammaSetting != 1000.0f) {
            FullBright.mc.gameSettings.gammaSetting = 1000.0f;
        }
    }
}
