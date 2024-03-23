/*
zPrestige is a skidder lmao
*/
package dev.zprestige.prestige.client.managers;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class FontManager {
    public MatrixStack matrix;
    public FontRenderer fontRenderer = new FontRenderer(Prestige.class.getClassLoader().getResourceAsStream("assets/prestige/font/font.ttf"), 18);

    public MatrixStack getMatrixStack() {
        return this.matrix;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public void setMatrixStack(MatrixStack matrixStack) {
        this.matrix = matrixStack;
    }

}
