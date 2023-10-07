package dev.rise.ui.clickgui.impl.tecnio.elements.buttons;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.ui.clickgui.impl.tecnio.elements.buttontype.Button;
import dev.rise.util.render.RenderUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.MathHelper;

import java.awt.*;

@Getter
@Setter
public final class NumberButton extends Button {

    private NumberSetting setting;
    private Color color;

    private boolean dragged;

    public NumberButton(final float x, final float y, final float width, final float height, final NumberSetting set, final Color col) {
        super(x, y, width, height);

        color = col;
        setting = set;
    }

    @Override
    public void drawPanel(final int mouseX, final int mouseY) {
        final double diff = setting.maximum - setting.minimum;

        final double percentWidth = (setting.getValue() - setting.minimum) / (setting.maximum - setting.minimum);

        final float settingsOffset = 2;

        if (dragged) {
            final double val = setting.minimum + (MathHelper.clamp_double((double) ((mouseX - settingsOffset * 2) - (this.getX()) + settingsOffset) / (this.getWidth() - settingsOffset * 4), 0, 1)) * diff;
            setting.setValue(Math.round(val * 100D) / 100D);
        }

        RenderUtil.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), new Color(0xff181A17));
        RenderUtil.rect(this.getX() + 1 + settingsOffset, this.getY(), (float) (percentWidth * (this.getWidth() - 3 - settingsOffset * 2)), this.getHeight(), new Color(Rise.CLIENT_THEME_COLOR));

        CustomFont.drawString(setting.name + ": " + Math.round(setting.getValue() * 100D) / 100D, this.getX() + 1 + 1 + settingsOffset, this.getY() + this.getHeight() / 2 - CustomFont.getHeight() / 2.0F, 0xffffffff);
    }

    @Override
    public void mouseAction(final int mouseX, final int mouseY, final boolean click, final int button) {
        if (isHovered(mouseX, mouseY)) {
            dragged = true;
        }

        if (!click) dragged = false;
    }
}
