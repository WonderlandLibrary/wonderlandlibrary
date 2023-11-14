package net.ccbluex.LiquidBase.module.modules.render;

import net.ccbluex.LiquidBase.LiquidBase;
import net.ccbluex.LiquidBase.event.EventTarget;
import net.ccbluex.LiquidBase.event.events.Render2DEvent;
import net.ccbluex.LiquidBase.module.Module;
import net.ccbluex.LiquidBase.module.ModuleCategory;
import net.ccbluex.LiquidBase.module.ModuleInfo;
import net.ccbluex.LiquidBase.module.ModuleManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Comparator;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@ModuleInfo(moduleName = "HUD", moduleDescription = "meh", moduleCateogry = ModuleCategory.RENDER, defaultKey = Keyboard.KEY_NONE)
public class HUD extends Module {

    public HUD() {
        setState(true);
    }

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        if(!getState())
            return;

        final FontRenderer fontRenderer = mc.fontRendererObj;
        final ScaledResolution scaledResolution = new ScaledResolution(mc);

        GlStateManager.scale(2, 2, 2);
        fontRenderer.drawString("", 2, 2, rainbow(600), true);
        GlStateManager.scale(0.5, 0.5, 0.5);

        final int[] yDist = {2};
        ModuleManager.getModules().stream().filter(Module :: getState).sorted(Comparator.comparingInt(module -> -fontRenderer.getStringWidth(module.getModuleName()))).forEach(module -> {
            fontRenderer.drawString(module.getModuleName(), scaledResolution.getScaledWidth() - 2 - fontRenderer.getStringWidth(module.getModuleName()), yDist[0], rainbow(600), true);
            yDist[0] += fontRenderer.FONT_HEIGHT;
        });
    }
    public static int rainbow(int delay){
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360f), 0.8f, 0.7f).getRGB();
    }
}