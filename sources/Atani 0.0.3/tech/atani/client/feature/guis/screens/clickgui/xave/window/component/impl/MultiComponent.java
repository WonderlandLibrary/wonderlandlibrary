package tech.atani.client.feature.guis.screens.clickgui.xave.window.component.impl;

import net.minecraft.client.gui.FontRenderer;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.value.impl.MultiStringBoxValue;
import tech.atani.client.utility.render.RenderUtil;

import java.awt.*;

public class MultiComponent extends ValueComponent {

    private final MultiStringBoxValue multiStringBoxValue;
    private FontRenderer fontRenderer = FontStorage.getInstance().findFont("Roboto", 19);
    private boolean expanded;

    public MultiComponent(MultiStringBoxValue multiStringBoxValue, float posX, float posY, float height) {
        super(multiStringBoxValue, posX, posY, height);
        this.multiStringBoxValue = multiStringBoxValue;
    }

    @Override
    public float draw(int mouseX, int mouseY) {
        fontRenderer.drawCenteredStringWithShadow(multiStringBoxValue.getName(), getPosX() + getWidth() / 2, getPosY() + 1, new Color(139, 141, 145, 255).getRGB());
        RenderUtil.drawRect(getPosX() + 2, getPosY() + getHeight() - 3, getWidth() - 4, 2, new Color(72, 79, 89, 160).getRGB());
        if(expanded) {
            float posY = this.getPosY() + this.getHeight();
            for(String string : multiStringBoxValue.getValues()) {
                fontRenderer.drawTotalCenteredStringWithShadow(string, getPosX() + getWidth() / 2, posY + getHeight() / 2, !multiStringBoxValue.get(string) ? new Color(139, 141, 145, 255).getRGB() : new Color(41, 146, 222).getRGB());
                posY += this.getHeight();
            }
        }
        return fontRenderer.getStringWidthInt(multiStringBoxValue.getName()) + 6;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtil.isHovered(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight()) && mouseButton == 0) {
            expanded = !expanded;
        }
        if(expanded) {
            float posY = this.getPosY() + this.getHeight();
            String toToggle = null;
            for(String string : multiStringBoxValue.getValues()) {
                if(RenderUtil.isHovered(mouseX, mouseY, getPosX(), posY, getWidth(), getHeight()) && mouseButton == 0) {
                    toToggle = string;
                }
                posY += this.getHeight();
            }
            if(toToggle != null)
                multiStringBoxValue.toggle(toToggle);
        }
    }

    public float getFinalHeight() {
        return getHeight() + (expanded ? multiStringBoxValue.getValues().length * getHeight() : 0);
    }

}
