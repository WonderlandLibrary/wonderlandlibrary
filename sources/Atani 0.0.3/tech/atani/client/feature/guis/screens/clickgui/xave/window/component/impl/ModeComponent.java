package tech.atani.client.feature.guis.screens.clickgui.xave.window.component.impl;

import net.minecraft.client.gui.FontRenderer;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.feature.value.impl.StringBoxValue;

import java.awt.*;

public class ModeComponent extends ValueComponent {

    private final StringBoxValue stringBoxValue;
    private FontRenderer fontRenderer = FontStorage.getInstance().findFont("Roboto", 19);
    private boolean expanded;

    public ModeComponent(StringBoxValue stringBoxValue, float posX, float posY, float height) {
        super(stringBoxValue, posX, posY, height);
        this.stringBoxValue = stringBoxValue;
    }

    @Override
    public float draw(int mouseX, int mouseY) {
        fontRenderer.drawCenteredStringWithShadow(stringBoxValue.getName(), getPosX() + getWidth() / 2, getPosY() + 1, new Color(139, 141, 145, 255).getRGB());
        RenderUtil.drawRect(getPosX() + 2, getPosY() + getHeight() - 3, getWidth() - 4, 2, new Color(72, 79, 89, 160).getRGB());
        if(expanded) {
            float posY = this.getPosY() + this.getHeight();
            for(String string : stringBoxValue.getValues()) {
                fontRenderer.drawTotalCenteredStringWithShadow(string, getPosX() + getWidth() / 2, posY + getHeight() / 2, !stringBoxValue.getValue().equalsIgnoreCase(string) ? new Color(139, 141, 145, 255).getRGB() : new Color(41, 146, 222).getRGB());
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
