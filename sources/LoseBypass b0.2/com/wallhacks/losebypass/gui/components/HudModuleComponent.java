/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.gui.components;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.systems.clientsetting.clientsettings.ClickGuiConfig;
import com.wallhacks.losebypass.systems.hud.HudComponent;
import com.wallhacks.losebypass.utils.Animation;
import com.wallhacks.losebypass.utils.ColorAnimation;
import com.wallhacks.losebypass.utils.GuiUtil;
import java.awt.Color;
import java.util.ArrayList;

public class HudModuleComponent {
    public HudComponent component;
    boolean open = false;
    ArrayList<SettingComponent> components = new ArrayList();
    private final ColorAnimation enabledCircleHover = new ColorAnimation(new Color(0, 0, 0, 0), 0.01f);
    private final ColorAnimation enabledColorAnimation = new ColorAnimation(new Color(0x232323), 0.0075f);
    private final Animation openAnimation = new Animation(0.0f, 0.005f);

    public HudModuleComponent(HudComponent component) {
        this.component = component;
        this.components.addAll(GuiUtil.getComponents(component));
    }

    /*
     * Unable to fully structure code
     */
    public int drawComponent(double posX, double posY, int smoothScroll, double deltaTime, int click, int mouseX, int mouseY) {
        if (!((double)mouseX > posX) || !((double)mouseX < posX + 200.0) || !((double)mouseY > posY - (double)smoothScroll)) ** GOTO lbl-1000
        v0 = mouseY;
        v1 = posY - (double)smoothScroll + 30.0;
        v2 = this.open != false ? 3 : 0;
        if (v0 < v1 - (double)v2) {
            v3 = true;
        } else lbl-1000:
        // 2 sources

        {
            v3 = insideCircle = false;
        }
        if (click >= 0 && insideCircle) {
            if (click == 0) {
                this.component.toggle();
            } else if (click == 1) {
                this.open = this.open == false;
            }
        }
        offset = 5;
        for (SettingComponent setting : this.components) {
            if (!setting.visible()) continue;
            offset += setting.getHeight();
        }
        this.openAnimation.update(this.open != false ? 1.0f : 0.0f, deltaTime);
        renderOffset = (int)(this.openAnimation.value() * (float)offset);
        GuiUtil.rounded((int)posX, (int)posY - smoothScroll, (int)posX + 201, (int)posY + 31 - smoothScroll + renderOffset, new Color(0, 0, 0, 30).getRGB(), 5);
        GuiUtil.rounded((int)posX, (int)posY - smoothScroll, (int)posX + 200, (int)posY + 30 - smoothScroll + renderOffset, ClickGui.background3(), 5);
        LoseBypass.fontManager.getThickFont().drawString(this.component.getName(), (int)posX + 5, (int)posY + 10 - smoothScroll, -1);
        GuiUtil.setup(new Color(0, 0, 0, 50).getRGB());
        GuiUtil.corner((float)(posX + 186.5), (float)posY - (float)smoothScroll + 15.5f, 4.0, 0, 360);
        GuiUtil.finish();
        this.enabledColorAnimation.update(this.component.isEnabled() != false ? ClickGuiConfig.getInstance().getMainColor() : new Color(0x232323), deltaTime);
        GuiUtil.setup(this.enabledColorAnimation.value().getRGB());
        GuiUtil.corner((int)posX + 186, (int)posY - smoothScroll + 15, 4.0, 0, 360);
        GuiUtil.finish();
        target = insideCircle != false ? new Color(100, 100, 100, 50) : new Color(0, 0, 0, 0);
        this.enabledCircleHover.update(target, deltaTime);
        GuiUtil.setup(this.enabledCircleHover.value().getRGB());
        GuiUtil.corner((int)posX + 186, (int)posY - smoothScroll + 15, 4.0, 0, 360);
        GuiUtil.finish();
        max = (double)ClickGui.minOffset > posY - (double)smoothScroll + 25.0 ? ClickGui.maxOffset : (int)Math.max((double)(ClickGui.minOffset + ClickGui.maxOffset) - (posY - (double)smoothScroll + 25.0), 0.0);
        min = Math.max((int)posY - smoothScroll + 25, ClickGui.minOffset);
        if (renderOffset == 0) return 35 + renderOffset;
        currentY = (int)((double)(-offset + renderOffset) + posY + 25.0);
        ClickGui.maxOffset = max;
        ClickGui.minOffset = min;
        GuiUtil.glScissor((int)posX, ClickGui.minOffset, 200, ClickGui.maxOffset);
        GuiUtil.drawRect((float)posX, currentY - smoothScroll, (float)posX + 200.0f, (float)posY + (float)renderOffset - (float)smoothScroll + 20.0f, ClickGui.background2());
        var18_16 = this.components.iterator();
        while (var18_16.hasNext() != false) {
            setting = var18_16.next();
            if (!setting.visible()) continue;
            if (currentY - smoothScroll > ClickGui.minOffset + ClickGui.maxOffset) {
                return 35 + renderOffset;
            }
            if (setting.getHeight() + currentY - smoothScroll < ClickGui.minOffset) {
                currentY += setting.getHeight();
                continue;
            }
            GuiUtil.glScissor((int)posX, ClickGui.minOffset, 200, ClickGui.maxOffset);
            currentY += setting.drawComponent((int)posX, currentY - smoothScroll, deltaTime, this.openAnimation.done() != false ? click : -1, mouseX, mouseY, this.openAnimation.done() == false);
        }
        return 35 + renderOffset;
    }
}

