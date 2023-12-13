package de.tired.util.misc;

import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectShader;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class RenderableTextfield {
    public final KeyInputUtil keyInputUtil = new KeyInputUtil();

    private final String text;

    public int x, y, width, height;

    private boolean hovering;

    public RenderableTextfield(String text, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public void mouseClicked() {
        if (!hovering) return;
        this.keyInputUtil.typing = !keyInputUtil.typing;

    }

    public void drawTextBox(int mouseX, int mouseY) {
        this.hovering = isHovered(mouseX, mouseY, x, y, width, height);


        final AtomicInteger additionalXAxis = new AtomicInteger(0);

        final CustomFont fontRenderer = FontManager.interMedium14;

        if (!keyInputUtil.typedString.isEmpty()) {
            if (fontRenderer.getStringWidth(keyInputUtil.typedString) >= width - 20) {
                final char[] by = keyInputUtil.typedString.toCharArray();
                for (int xChar = 0; xChar < keyInputUtil.typedString.length(); xChar++)
                    additionalXAxis.addAndGet(fontRenderer.getStringWidth(String.valueOf(by[xChar])));
            }
        }

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x - 1, y - 1, width + 2, height + 2, 2, new Color(190, 190, 190, 255));
        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x, y, width, height, 1, new Color(242, 242, 242, 255));
        //    ShaderManager.shaderBy(RoundedRectOutlineShader.class).drawRound(x - 2, y, width + 4, height, 4, 1,  new Color(30, 121, 246, 174));


        if (keyInputUtil.typedString.isEmpty())
            fontRenderer.drawString(text, x + 4, y + 8, Integer.MIN_VALUE);
        else {
            fontRenderer.drawString(keyInputUtil.typedString, x + 3 - ((additionalXAxis.get() - 34) * .2f), y + 8, new Color(45, 46, 52).getRGB());
        }

    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {

        this.keyInputUtil.registerInput(typedChar, keyCode);
    }


    public int calculateMiddle(String text, CustomFont fontRenderer, double x, double width) {
        return (int) ((float) (x + width) - (fontRenderer.getStringWidth(text) / 2f) - (float) width / 2);
    }

    public boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

}