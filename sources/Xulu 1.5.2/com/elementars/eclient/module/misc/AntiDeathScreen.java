// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiGameOver;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class AntiDeathScreen extends Module
{
    public AntiDeathScreen() {
        super("AntiDeathScreen", "AntiDeathScreen apparently", 0, Category.MISC, true);
    }
    
    @Override
    public void onUpdate() {
        if (this.isToggled() && AntiDeathScreen.mc.currentScreen instanceof GuiGameOver && AntiDeathScreen.mc.player.getHealth() > 0.0f) {
            AntiDeathScreen.mc.player.respawnPlayer();
            AntiDeathScreen.mc.displayGuiScreen((GuiScreen)null);
        }
    }
}
