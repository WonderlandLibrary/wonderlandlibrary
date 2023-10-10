package tech.atani.client.feature.guis.screens.clickgui.icarus.window.component.impl;

import net.minecraft.client.gui.FontRenderer;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.utility.render.shader.shaders.GradientShader;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;

public class ModeComponent extends ValueComponent {

    private final StringBoxValue stringBoxValue;
    private FontRenderer fontRenderer = FontStorage.getInstance().findFont("ArialMT", 16);
    private boolean expanded;

    public ModeComponent(StringBoxValue stringBoxValue, float posX, float posY, float height) {
        super(stringBoxValue, posX, posY, height);
        this.stringBoxValue = stringBoxValue;
    }

    @Override
    public float draw(int mouseX, int mouseY) {
        RoundedShader.drawRound(getPosX() + 4, getPosY() + 2, getWidth() - 8, getFinalHeight() - 4, 5, new Color(31, 31, 32));
        fontRenderer.drawString(stringBoxValue.getName(), getPosX() + 10, getPosY() + 6, -1);
        if(expanded) {
            GradientShader.drawGradientLR(getPosX() + 3, getPosY() + 2 + getHeight() - 4 + 1, getWidth() - 6, 1, 1, new Color(ICARUS_FIRST), new Color(ICARUS_SECOND));
            float posY = this.getPosY() + this.getHeight();
            for(String string : stringBoxValue.getValues()) {
                fontRenderer.drawTotalCenteredString(string, getPosX() + getWidth() / 2, posY + getHeight() / 2, !stringBoxValue.getValue().equalsIgnoreCase(string) ? -1 : ICARUS_FIRST);
                posY += this.getHeight();
            }
        }
        return fontRenderer.getStringWidthInt(stringBoxValue.getName()) + 6;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight()) && mouseButton == 0) {
            expanded = !expanded;
        }
        if(expanded) {
            float posY = this.getPosY() + this.getHeight();
            for(String string : stringBoxValue.getValues()) {
                if(RenderUtil.isHovered(mouseX, mouseY, getPosX(), posY, getWidth(), getHeight()) && mouseButton == 0) {
                    stringBoxValue.setValue(string);
                }
                posY += this.getHeight();
            }
        }
    }

    public float getFinalHeight() {
        return getHeight() + (expanded ? stringBoxValue.getValues().length * getHeight() : 0);
    }

}
