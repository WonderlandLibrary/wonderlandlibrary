package de.tired.util.render;

import de.tired.base.guis.newclickgui.setting.impl.ColorPickerSetting;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectGradient;
import de.tired.util.render.shaderloader.list.RoundedRectOutlineShader;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.util.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.ByteBuffer;

public class ColorPicker {


    final ColorPickerSetting value;

    private int x, y, width, height, color = Color.red.getRGB();
    private Double colorFieldDotX, lastColorFieldDotX, colorFieldDotY, lastColorFieldDotY;
    private boolean draggingHUE;

    private int selectionMouseX, selectionMouseY;

    public ColorPicker(ColorPickerSetting value) {
        this.value = value;
    }

    private Color getHoverColor() {
        final ByteBuffer rgb = BufferUtils.createByteBuffer(100);
        GL11.glReadPixels((int) selectionMouseX, (int) selectionMouseY, 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, rgb);
        return new Color(rgb.get(0) & 0xFF, rgb.get(1) & 0xFF, rgb.get(2) & 0xFF);
    }

    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, Color currentColor) {
        draw(x, y, width, height, mouseX, mouseY, currentColor, true);
    }

    public void setColor(Color color) {
        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, Color currentColor, boolean isFront) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
        final float f = (float) (color >> 16 & 255) / 255.0F;
        final float f1 = (float) (color >> 8 & 255) / 255.0F;
        final float f2 = (float) (color & 255) / 255.0F;



     /*  for (int i = 0; i < height; i++) {
            int color2 = Color.HSBtoRGB((float) i / height, 1, 1);
            float r = (float) (color2 >> 16 & 255) / 255.0F;
            float g = (float) (color2 >> 8 & 255) / 255.0F;
            float b = (float) (color2 & 255) / 255.0F;
            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) (x + width + 6), (float) (y + (h * i)), 12, 1, 2, new Color(r, g, b));
            if (isFront && Mouse.isButtonDown(0) && mouseX >= x + width + 10 && mouseX <= x + width + 20 && mouseY >= y + (h * i) && mouseY <= y + (h * (i + 1))) {
                color = Color.HSBtoRGB((float) i / height, 1, 1);
                this.draggingHUE = true;
            } else if (!Mouse.isButtonDown(0)) {
                this.draggingHUE = false;
            }
        }*/
        final double h = 1;


        for (int i = 0; i < width + 1; i++) {
            int color2 = Color.HSBtoRGB((float) i / height, 1, 1);
            float r = (float) (color2 >> 16 & 255) / 255.0F;
            float g = (float) (color2 >> 8 & 255) / 255.0F;
            float b = (float) (color2 & 255) / 255.0F;

            ShaderManager.shaderBy(RoundedRectShader.class).drawRound((float) (x + 3.5 + (h * i)), (float) (y + height - 9), 1, 8, 2, new Color(r, g, b));
            if (isFront && Mouse.isButtonDown(0) && isOver((int) (x + 3.5 + (h * i)), (int) (y + height - 11), width, 12, mouseX, mouseY)) {
                color = Color.HSBtoRGB((float) i / height, 1, 1);
                this.draggingHUE = true;
            } else if (!Mouse.isButtonDown(0)) {
                this.draggingHUE = false;
            }
        }

        ShaderManager.shaderBy(RoundedRectGradient.class).drawRound(x + 4, y, width, height - 12, 4, Color.white, Color.black, new Color(f, f1, f2), Color.black);
        //   ShaderManager.shaderBy(RoundedRectGradient.class).drawRound(x + 4, y, width, height, 5, Color.black, Color.WHITE, Color.BLACK, new Color(f, f1, f2));

        ShaderManager.shaderBy(RoundedRectOutlineShader.class).drawRound(x + 3, y - 1, width + 2, height - 10, 4, .6f, Color.white);
        if (isFront && Mouse.isButtonDown(0) && (isOver(x + 4, y, width, height - 12, mouseX, mouseY) || draggingHUE)) {
            final Color hoverColor = getHoverColor();
            if (value != null) {
                value.setColorPickerC(hoverColor);
            }

        }
        if (isFront) {
            if (isHover(mouseX, mouseY)) {
                colorFieldDotX = (double) mouseX;
                colorFieldDotY = (double) mouseY;

                colorFieldDotX = MathHelper.clamp_double(colorFieldDotX, x, x + width - 5);
                colorFieldDotY = MathHelper.clamp_double(colorFieldDotY, y, y + height - 5);

                lastColorFieldDotX = colorFieldDotX - x;
                lastColorFieldDotY = colorFieldDotY - y;
            }
        }
        if (colorFieldDotY != null && colorFieldDotX != null) {
            this.selectionMouseX = Mouse.getX();
            this.selectionMouseY = Mouse.getY();
        }
        if (!Mouse.isButtonDown(0) && colorFieldDotX != null && colorFieldDotY != null) {
            lastColorFieldDotX = (double) (selectionMouseX - x);
            lastColorFieldDotY = (double) (selectionMouseY - y);
        }

        if (isFront && lastColorFieldDotX != null && lastColorFieldDotY != null) {
            colorFieldDotX = lastColorFieldDotX + x;
            colorFieldDotY = lastColorFieldDotY + y;
            RenderUtil.instance.drawCircle(colorFieldDotX, colorFieldDotY, 4, Color.WHITE.getRGB());
            assert value != null;
            RenderUtil.instance.drawCircle(colorFieldDotX, colorFieldDotY, 3, value.ColorPickerC.getRGB());
        }
    }

    public boolean isOver(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX > x - 3 && mouseX < x - 3 + width && mouseY > y && mouseY < y + height;
    }
}