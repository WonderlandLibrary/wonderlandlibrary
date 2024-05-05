/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.gui.components.settings;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.utils.GuiUtil;

public class BooleanComponent
extends SettingComponent {
    BooleanSetting setting;
    double state;

    public BooleanComponent(BooleanSetting setting) {
        super(setting);
        this.setting = setting;
        this.state = (Boolean)setting.getValue() != false ? 1.0 : 0.0;
    }

    @Override
    public int drawComponent(int posX, int posY, double deltaTime, int click, int mouseX, int mouseY) {
        LoseBypass.fontManager.drawString(this.setting.getName(), posX + 6, posY + 5, -1);
        GuiUtil.rounded(posX + 175, posY + 7, posX + 190, posY + 11, (Boolean)this.setting.getValue() != false ? ClickGui.mainColor2() : ClickGui.background3(), 2);
        if (click == 0 && mouseX > posX && mouseX < posX + 200 && mouseY > posY && mouseY < posY + 21) {
            this.setting.toggle();
        }
        if (((Boolean)this.setting.getValue()).booleanValue() && this.state != 1.0) {
            this.state = Math.min(1.0, this.state + deltaTime * 0.01);
        } else if (!((Boolean)this.setting.getValue()).booleanValue() && this.state != 0.0) {
            this.state = Math.max(0.0, this.state - deltaTime * 0.01);
        }
        GuiUtil.setup(ClickGui.mainColor());
        int x = (int)((double)(posX + 175) + 15.0 * this.state);
        GuiUtil.corner(x, posY + 9, 4.0, 0, 360);
        GuiUtil.finish();
        return 17;
    }

    @Override
    public int getHeight() {
        return 17;
    }

    @Override
    public boolean visible() {
        return this.setting.isVisible();
    }
}

