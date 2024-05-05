/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.gui.components.settings;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import com.wallhacks.losebypass.utils.GuiUtil;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;

public class ModeSelector
extends SettingComponent {
    ModeSetting setting;
    int width;
    boolean open = false;
    double state = 0.0;

    public ModeSelector(ModeSetting setting) {
        super(setting);
        this.setting = setting;
        Iterator<String> iterator = setting.getModes().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.width += 12;
                return;
            }
            String s = iterator.next();
            int t = LoseBypass.fontManager.getTextWidth(s);
            if (t <= this.width) continue;
            this.width = t;
        }
    }

    @Override
    public int drawComponent(int posX, int posY, double deltaTime, int click, int mouseX, int mouseY) {
        LoseBypass.fontManager.drawString(this.setting.getName(), posX + 6, posY + 5, -1);
        String display = this.setting.getValueString();
        int length = LoseBypass.fontManager.getTextWidth(display);
        GuiUtil.rounded(posX + 180 - this.width, posY + 2, posX + 190, (int)((double)(posY + 14) + (double)this.offset() * this.state), ClickGui.background3(), 3);
        Gui.drawRect(0, 0, 0, 0, 0);
        int offset = (int)((double)(-this.offset()) + (double)this.offset() * this.state);
        LoseBypass.fontManager.drawString(display, posX + 185 - length, posY + 5, -1);
        GuiUtil.drawCompleteImageRotated(posX + 184 - this.width, (float)posY + 5.5f, 5.0f, 5.0f, (float)(90.0 * this.state), ClickGui.arrow, Color.WHITE);
        if (this.state != 0.0) {
            GuiUtil.glScissor(posX, Math.max(posY + 14, ClickGui.minOffset), 200, ClickGui.minOffset > posY + 14 ? ClickGui.maxOffset : Math.max(ClickGui.maxOffset + ClickGui.minOffset - (posY + 14), 0));
            for (String mode : this.setting.getModes()) {
                if (mode.equals(this.setting.getValue())) continue;
                boolean hover = false;
                if (mouseX > posX + 180 - this.width && mouseX < posX + 190 && mouseY > posY + 12 + offset && mouseY < posY + 25 + offset) {
                    hover = true;
                    if (click == 0) {
                        click = -1;
                        this.setting.setValue(mode);
                    }
                }
                GuiUtil.drawRect(posX + 180 - this.width, posY + 12 + offset, posX + 190, posY + 25 + offset, hover ? ClickGui.background() : ClickGui.background4());
                LoseBypass.fontManager.drawString(mode, posX + 185 - LoseBypass.fontManager.getTextWidth(mode), posY + 16 + offset, -1);
                offset += 13;
            }
        }
        if (mouseX > posX && mouseX < posX + 200 && mouseY > posY && mouseY < posY + 21) {
            if (click == 0) {
                this.setting.increment();
            } else if (click == 1) {
                boolean bl = this.open = !this.open;
            }
        }
        if (this.open && this.state != 1.0) {
            this.state = Math.min(1.0, this.state + deltaTime * 0.01);
            return (int)(17.0 + (double)offset * this.state);
        }
        if (this.open) return (int)(17.0 + (double)offset * this.state);
        if (this.state == 0.0) return (int)(17.0 + (double)offset * this.state);
        this.state = Math.max(0.0, this.state - deltaTime * 0.01);
        return (int)(17.0 + (double)offset * this.state);
    }

    private int offset() {
        return (this.setting.getModes().size() - 1) * 13;
    }

    @Override
    public int getHeight() {
        return (int)(17.0 + (double)this.offset() * this.state);
    }

    @Override
    public boolean visible() {
        return this.setting.isVisible();
    }
}

