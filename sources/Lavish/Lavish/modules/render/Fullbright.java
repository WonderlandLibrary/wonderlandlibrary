// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.render;

import Lavish.modules.Category;
import Lavish.modules.Module;

public class Fullbright extends Module
{
    public Fullbright() {
        super("Fullbright", 0, true, Category.Render, "Makes you see in the dark");
    }
    
    @Override
    public void onUpdate() {
        Fullbright.mc.gameSettings.gammaSetting = 1000.0f;
    }
    
    @Override
    public void onDisable() {
        Fullbright.mc.gameSettings.gammaSetting = 1.0f;
    }
}
