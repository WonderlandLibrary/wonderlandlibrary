package tech.atani.client.feature.guis.screens.clickgui.icarus.window.component.impl;

import net.minecraft.client.gui.FontRenderer;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.value.impl.MultiStringBoxValue;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.shader.shaders.GradientShader;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;

public class MultiComponent extends ValueComponent {

    private final MultiStringBoxValue multiStringBoxValue;
    private FontRenderer fontRenderer = FontStorage.getInstance().findFont("ArialMT", 16);
    private boolean expanded;

    public MultiComponent(MultiStringBoxValue multiStringBoxValue, float posX, float posY, float height) {
        super(multiStringBoxValue, posX, posY, height);
        this.multiStringBoxValue = multiStringBoxValue;
    }

    @Override
    public float draw(int mouseX, int mouseY) {
        RoundedShader.drawRound(getPosX() + 4, getPosY() + 2, getWidth() - 8, getFinalHeight() - 4, 5, new Color(31, 31, 32));
        fontRenderer.drawString(multiStringBoxValue.getName(), getPosX() + 10, getPosY() + 6, -1);
        if(expanded) {
            GradientShader.drawGradientLR(getPosX() + 3, getPosY() + 2 + getHeight() - 4 + 1, getWidth() - 6, 1, 1, new Color(ICARUS_FIRST), new Color(ICARUS_SECOND));
            float posY = this.getPosY() + this.getHeight();
            for(String string : multiStringBoxValue.getValues()) {
                fontRenderer.drawTotalCenteredString(string, getPosX() + getWidth() / 2, posY + getHeight() / 2, !multiStringBoxValue.get(string) ? -1 : ICARUS_FIRST);
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
