// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.modules.Module;

public class FullBright extends Module
{
    private float oldGamma;
    
    public FullBright() {
        super("FullBright", new Color(188, 213, 25), Categorys.RENDER);
        this.oldGamma = 1.0f;
    }
    
    @Override
    public void onEnable() {
        this.oldGamma = FullBright.mc.gameSettings.gammaSetting;
        FullBright.mc.gameSettings.gammaSetting = 10.0f;
    }
    
    @Override
    public void onDisable() {
        FullBright.mc.gameSettings.gammaSetting = this.oldGamma;
    }
}
