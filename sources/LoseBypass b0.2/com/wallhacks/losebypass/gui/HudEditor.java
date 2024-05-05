/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.wallhacks.losebypass.gui;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.manager.SystemManager;
import com.wallhacks.losebypass.systems.hud.HudComponent;
import com.wallhacks.losebypass.utils.Animation;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.SnapLine;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class HudEditor
extends GuiScreen {
    public static final ResourceLocation arrow = new ResourceLocation("textures/icons/backarrow.png");
    long lastFrame;
    boolean down;
    public static final Animation animation = new Animation(1.0f, 0.005f);
    HudComponent dragging = null;
    int offsetX;
    int offsetY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float delta = (float)(System.nanoTime() - this.lastFrame) / 1000000.0f;
        this.lastFrame = System.nanoTime();
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.height = sr.getScaledHeight();
        this.width = sr.getScaledWidth();
        animation.update(0.0f, delta);
        int centerX = this.width / 2;
        centerX = (int)((float)centerX + (float)(centerX - 270) * animation.value());
        int centerY = this.height / 2;
        boolean hover = false;
        if (mouseX > centerX - 30 && mouseX < centerX + 30 && mouseY > centerY - 10 && mouseY < centerY + 10) {
            hover = true;
        }
        boolean click = false;
        if (!this.down && Mouse.isButtonDown((int)0)) {
            this.down = true;
            click = true;
            if (hover) {
                SystemManager.editingHud = false;
                this.mc.displayGuiScreen(LoseBypass.clickGuiScreen);
            }
        } else if (!Mouse.isButtonDown((int)0)) {
            this.down = false;
            this.dragging = null;
        }
        boolean finalClick = click;
        ArrayList<SnapLine> snapLines = new ArrayList<SnapLine>();
        snapLines.add(new SnapLine(SnapLine.Type.Y, 3));
        snapLines.add(new SnapLine(SnapLine.Type.X, 2));
        snapLines.add(new SnapLine(SnapLine.Type.Y, ClickGui.height - 3));
        snapLines.add(new SnapLine(SnapLine.Type.X, ClickGui.width - 3));
        SystemManager.getHudComponents().forEach(component -> snapLines.addAll(component.updateSnapLines()));
        SystemManager.getHudComponents().forEach(component -> {
            if (!component.isEnabled()) return;
            if (!component.handleHudEditor(delta, mouseX, mouseY, this.dragging == component, this.offsetX, this.offsetY, snapLines)) return;
            if (!finalClick) return;
            if (this.dragging == component) return;
            this.dragging = component;
            this.offsetX = mouseX - component.posX;
            this.offsetY = mouseY - component.posY;
        });
        GuiUtil.rounded(centerX - 30, centerY - 10, centerX + 30, centerY + 10, hover ? ClickGui.mainColor2() : ClickGui.mainColor(), 5);
        LoseBypass.fontManager.getThickFont().drawString("Return", centerX - 25, centerY - 5, -1);
        GuiUtil.drawCompleteImage(centerX + 13, centerY - 7, 12.0, 12.0, arrow, new Color(-1));
    }

    @Override
    public void initGui() {
        this.lastFrame = System.nanoTime();
        animation.forceValue(1.0f);
    }
}

