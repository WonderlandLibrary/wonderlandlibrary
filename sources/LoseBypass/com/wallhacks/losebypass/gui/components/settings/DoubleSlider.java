/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.wallhacks.losebypass.gui.components.settings;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.TextEditComponent;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.MathUtil;
import org.lwjgl.input.Mouse;

public class DoubleSlider
extends SettingComponent {
    DoubleSetting setting;
    double progress;
    boolean sliding;
    double target;
    TextEditComponent value;
    Runnable submitText = new Runnable(){

        @Override
        public void run() {
            try {
                DoubleSlider.this.setting.setValue(MathUtil.clamp(Double.parseDouble(DoubleSlider.this.value.getText()), DoubleSlider.this.setting.getMin(), DoubleSlider.this.setting.getMax()));
                return;
            }
            catch (NumberFormatException e) {
                DoubleSlider.this.value.setText(DoubleSlider.this.setting.getValueString());
            }
        }
    };

    public DoubleSlider(DoubleSetting setting) {
        super(setting);
        this.setting = setting;
        this.target = this.progress = ((Double)setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin()) * 180.0;
        this.value = new TextEditComponent(setting.getValueString(), LoseBypass.fontManager.getFont(), this.submitText, true);
    }

    @Override
    public int drawComponent(int posX, int posY, double deltaTime, int click, int mouseX, int mouseY) {
        LoseBypass.fontManager.drawString(this.setting.getName(), posX + 6, posY + 5, -1);
        if (!this.value.isTyping()) {
            this.value.setText(this.setting.getValueString());
        }
        int length = LoseBypass.fontManager.getTextWidth(this.value.getText());
        GuiUtil.rounded(posX + 180 - length, posY + 2, posX + 190, posY + 14, ClickGui.background3(), 3);
        this.value.updatePosition(posX + 180 - length, posY + 2, posX + 190, posY + 14);
        if (click == 0) {
            this.value.mouseClicked(mouseX, mouseY);
        }
        this.value.drawString();
        if (click == 0 && mouseX > posX + 2 && mouseX < posX + 198 && mouseY > posY + 14 && mouseY < posY + 24) {
            this.sliding = true;
        }
        if (this.sliding) {
            if (!Mouse.isButtonDown((int)0)) {
                this.sliding = false;
            } else {
                double mouse = (float)MathUtil.clamp(mouseX - posX - 10, 0, 180) / 180.0f;
                double value = (this.setting.getMax() - this.setting.getMin()) * mouse;
                value += this.setting.getSliderStep() * 0.5;
                value -= value % this.setting.getSliderStep();
                this.setting.setValue(value + this.setting.getMin());
            }
        }
        this.target = ((Double)this.setting.getValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()) * 180.0;
        if (this.target != this.progress) {
            this.progress = MathUtil.lerp(this.progress, this.target, deltaTime * 0.02);
            if (Math.abs(this.progress - this.target) < 1.0) {
                this.progress = this.target;
            }
        }
        GuiUtil.rounded(posX + 10, posY + 16, (int)((double)(posX + 10) + this.progress), posY + 20, ClickGui.mainColor2(), 2);
        GuiUtil.rounded((int)((double)(posX + 10) + this.progress), posY + 16, posX + 190, posY + 20, ClickGui.background3(), 2);
        GuiUtil.setup(ClickGui.mainColor());
        GuiUtil.corner((int)((double)(posX + 10) + this.progress), posY + 18, 4.0, 0, 360);
        GuiUtil.finish();
        return this.getHeight();
    }

    @Override
    public int getHeight() {
        return 23;
    }

    @Override
    public boolean visible() {
        return this.setting.isVisible();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        this.value.keyTyped(typedChar, keyCode);
    }
}

