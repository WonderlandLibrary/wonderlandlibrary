// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite;

import net.minecraft.client.gui.GuiScreen;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class HudEditor extends Module
{
    public HudEditor() {
        super("HudEditor", "Editor for HUD elements", 0, Category.CORE, false);
    }
    
    @Override
    public void setup() {
        this.setKey(0);
    }
    
    @Override
    public void onEnable() {
        HudEditor.mc.displayGuiScreen((GuiScreen)Xulu.hud);
        this.toggle();
    }
}
