/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.gui.components;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.systems.setting.SettingGroup;
import com.wallhacks.losebypass.utils.GuiUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class SettingGroupComponent
extends SettingComponent {
    ArrayList<SettingComponent> components = new ArrayList();
    SettingGroup group;
    boolean open = false;
    double state;

    public SettingGroupComponent(SettingGroup group) {
        super(null);
        this.group = group;
        Iterator<Setting<?>> iterator = group.getSettings().iterator();
        while (iterator.hasNext()) {
            Setting<?> setting = iterator.next();
            SettingComponent component = GuiUtil.getComponent(setting, false);
            if (component == null) continue;
            this.components.add(component);
        }
    }

    @Override
    public int drawComponent(int posX, int posY, double deltaTime, int click, int mouseX, int mouseY) {
        boolean hover = false;
        if (mouseX > posX && mouseX < posX + 200 && mouseY > posY && mouseY < posY + 20) {
            hover = true;
            if (click == 1) {
                this.open = !this.open;
            }
        }
        int offset = 0;
        for (SettingComponent setting : this.components) {
            offset += setting.getHeight();
        }
        int renderOffset = (int)(this.state * (double)offset);
        if (this.open && this.state != 1.0) {
            this.state = Math.min(1.0, this.state + deltaTime * 0.01);
        } else if (!this.open && this.state != 0.0) {
            this.state = Math.max(0.0, this.state - deltaTime * 0.01);
        }
        GuiUtil.drawRect(posX, posY, posX + 200, posY + 20, hover ? ClickGui.background5() : ClickGui.background4());
        GuiUtil.drawCompleteImageRotated(posX + 182, posY + 7, 5.0f, 5.0f, (float)(90.0 * this.state), ClickGui.arrow, Color.WHITE);
        LoseBypass.fontManager.drawString(this.group.getName(), posX + 5, posY + 7, -1);
        int max = ClickGui.minOffset > posY + 20 ? ClickGui.maxOffset : Math.max(ClickGui.minOffset + ClickGui.maxOffset - (posY + 20), 0);
        int min = Math.max(posY + 20, ClickGui.minOffset);
        if (renderOffset == 0) return 20 + renderOffset;
        int currentY = -offset + renderOffset + posY + 20;
        ClickGui.maxOffset = max;
        ClickGui.minOffset = min;
        GuiUtil.glScissor(posX, ClickGui.minOffset, 200, ClickGui.maxOffset);
        GuiUtil.drawRect(posX, currentY, (float)posX + 200.0f, (float)posY + (float)renderOffset + 20.0f, ClickGui.background6());
        Iterator<SettingComponent> iterator = this.components.iterator();
        while (iterator.hasNext()) {
            SettingComponent setting = iterator.next();
            if (currentY > ClickGui.minOffset + ClickGui.maxOffset) {
                return 20 + renderOffset;
            }
            if (setting.getHeight() + currentY < ClickGui.minOffset) {
                currentY += setting.getHeight();
                continue;
            }
            GuiUtil.glScissor(posX, ClickGui.minOffset, 200, ClickGui.maxOffset);
            currentY += setting.drawComponent(posX, currentY, deltaTime, click, mouseX, mouseY, !ClickGui.animation.done());
        }
        return 20 + renderOffset;
    }

    @Override
    public int getHeight() {
        int offset = 0;
        Iterator<SettingComponent> iterator = this.components.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                offset = (int)((double)offset * this.state);
                return offset + 20;
            }
            SettingComponent setting = iterator.next();
            offset += setting.getHeight();
        }
    }

    @Override
    public boolean visible() {
        return this.group.isVisible();
    }
}

