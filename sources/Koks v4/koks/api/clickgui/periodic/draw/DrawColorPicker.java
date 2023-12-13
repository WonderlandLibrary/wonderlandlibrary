package koks.api.clickgui.periodic.draw;

import koks.Koks;
import koks.api.clickgui.Element;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.utils.ColorPicker;
import koks.api.utils.RenderUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.ByteBuffer;

public class DrawColorPicker extends Element {

    final ColorPicker colorPicker;

    public DrawColorPicker(Value<?> value) {
        super(Fonts.arial18);
        this.value = value;
        this.colorPicker = new ColorPicker(ColorPicker.Type.QUAD, value);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final String name = value.getDisplayName() != null ? value.getDisplayName() : value.getName();
        final Color color = new Color(value.castInteger());
        height = 140;
        fr.drawString(name, x + 5, y, Color.white, true);
        colorPicker.draw(x + 5, (int) (y + fr.getStringHeight(name) + 2), 100, 100, mouseX, mouseY, color);

        resetButtonDistance = (int) (5 + fr.getStringWidth(name)) + 2;
        resetButtonHeight = 1;
        hasResetButton = !value.getValue().equals(value.getDefaultValue());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        colorPicker.handleInput(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        colorPicker.handleClick(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
