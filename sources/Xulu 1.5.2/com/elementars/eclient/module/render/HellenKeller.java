// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.SoundCategory;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class HellenKeller extends Module
{
    float masterLevel;
    
    public HellenKeller() {
        super("HellenKeller", "Play like Hellen Keller", 0, Category.RENDER, true);
    }
    
    @Override
    public void onEnable() {
        this.masterLevel = HellenKeller.mc.gameSettings.getSoundLevel(SoundCategory.MASTER);
        HellenKeller.mc.gameSettings.setSoundLevel(SoundCategory.MASTER, 0.0f);
    }
    
    @Override
    public void onRender() {
        GlStateManager.pushMatrix();
        Gui.drawRect(0, 0, HellenKeller.mc.displayWidth, HellenKeller.mc.displayHeight, new Color(0, 0, 0, 255).getRGB());
        GlStateManager.popMatrix();
    }
    
    @Override
    public void onDisable() {
        HellenKeller.mc.gameSettings.setSoundLevel(SoundCategory.MASTER, this.masterLevel);
    }
}
