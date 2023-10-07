package koks.api.utils;

import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.shader.GradientShader;
import lombok.Getter;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.ByteBuffer;

import static net.minecraft.client.gui.GuiScreen.drawRect;
import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author kroko
 * @created on 20.02.2021 : 19:42
 */
@Getter
public class ColorPicker {

    final Type type;
    final Value<?> value;
    final TimeHelper timeHelper = new TimeHelper();

    int currentValue;

    int x, y, width, height, color = Color.red.getRGB();
    boolean typing;

    String hex;
    Color currentColor;

    public ColorPicker(Type type, Value<?> value) {
        this.type = type;
        this.value = value;
        currentValue = value.castInteger();
        hex = Integer.toHexString(value.castInteger()).substring(2);
    }

    public ColorPicker(Type type, int current) {
        this.type = type;
        this.value = null;
        this.currentValue = current;
        hex = Integer.toHexString(currentValue).substring(2);
    }

    private Color getHoverColor() {
        final ByteBuffer rgb = BufferUtils.createByteBuffer(100);
        GL11.glReadPixels(Mouse.getX(), Mouse.getY(), 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, rgb);
        return new Color(rgb.get(0) & 0xFF, rgb.get(1) & 0xFF, rgb.get(2) & 0xFF);
    }

    final GradientShader gradientShader = new GradientShader();

    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, Color currentColor) {
        draw(x, y, width, height, mouseX, mouseY, currentColor, true);
    }

    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, Color currentColor, boolean isFront) {
        this.currentColor = currentColor;
        final RenderUtil renderUtil = RenderUtil.getInstance();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        final float f = (float) (color >> 16 & 255) / 255.0F;
        final float f1 = (float) (color >> 8 & 255) / 255.0F;
        final float f2 = (float) (color & 255) / 255.0F;

        final double h = 1;
        for (int i = 0; i < height; i++) {
            renderUtil.drawRect(x + width + 10, y + (h * i), x + width + 20, y + (h * (i + 1)), Color.HSBtoRGB((float) i / height, 1, 1));

            if (isFront && Mouse.isButtonDown(0) && mouseX >= x + width + 10 && mouseX <= x + width + 20 && mouseY >= y + (h * i) && mouseY <= y + (h * (i + 1))) {
                color = Color.HSBtoRGB((float) i / height, 1, 1);
            }
        }

        for (int i = 0; i < height; i++) {
            if (color == Color.HSBtoRGB((float) i / height, 1, 1)) {
                renderUtil.drawRect(x + width + 10, y + (h * i) + 1, x + width + 20, y + (h * (i + 1)) + 2, Color.black.getRGB());
                renderUtil.drawRect(x + width + 10, y + (h * i) - 2, x + width + 20, y + (h * (i + 1)) - 1, Color.black.getRGB());
            }
        }

        switch (type) {
            case QUAD:
                glEnable(GL_BLEND);
                glShadeModel(GL_SMOOTH);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glDisable(GL_TEXTURE_2D);
                GL11.glBegin(GL_QUADS);
                renderUtil.setColor(new Color(f, f1, f2));
                glVertex2d(x + width, y);
                renderUtil.setColor(Color.white);
                glVertex2d(x, y);
                renderUtil.setColor(Color.BLACK);
                glVertex2d(x, y + height);
                renderUtil.setColor(Color.BLACK);
                glVertex2d(x + width, y + height);
                GL11.glEnd();
                glEnable(GL_TEXTURE_2D);
                break;
            case SHADER:
                glEnable(GL_BLEND);
                glShadeModel(GL_SMOOTH);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glDisable(GL_TEXTURE_2D);
                gradientShader.use();
                gradientShader.setAttributes(Color.white, new Color(f, f1, f2), width, height);
                GL11.glBegin(GL_QUADS);
                glVertex2d(x + width, y);
                glVertex2d(x, y);
                glVertex2d(x, y + height);
                glVertex2d(x + width, y + height);
                GL11.glEnd();
                gradientShader.unUse();
                glEnable(GL_TEXTURE_2D);
                break;
        }

        if (isFront && Mouse.isButtonDown(0)) {
            if (isHover(mouseX, mouseY)) {
                final int hoverColor = getHoverColor().getRGB();
                if (value != null) {
                    value.castIfPossible(hoverColor + "");
                }
                currentValue = hoverColor;
                hex = Integer.toHexString(hoverColor).substring(2);
            }
        }

        drawRect(x + width + 25, y, x + width + 85, y + 25, currentColor.getRGB());
        drawRect(x + width + 25, (int) (y + 27 - Fonts.arial18.getStringHeight("#" + hex)), x + width + 85, y + 25, Integer.MIN_VALUE);
        Fonts.arial18.drawString("#" + hex + (typing && !timeHelper.hasReached(250) ? "_" : ""), x + width + 27, y + 21 - Fonts.arial18.getStringHeight("#" + hex) / 2, Color.white, true);
        timeHelper.hasReached(500, true);
    }

    public void handleClick(int mouseX, int mouseY, int mouseButton) {
        //fr.drawString("#" + Integer.toHexString(currentColor.getRGB()).substring(2), x + width + 27, y + 21 - fr.FONT_HEIGHT / 2, -1);
        if (mouseButton == 0) {
            final int xPos = (int) (x + width + 27 + Fonts.arial18.getStringWidth("#")), yPos = (int) (y + 21 - Fonts.arial18.getStringHeight("#") / 2);
            if (mouseX >= xPos && mouseX <= xPos + Fonts.arial18.getStringWidth(hex) && mouseY >= yPos && mouseY <= yPos + Fonts.arial18.getStringHeight("#" + hex)) {
                typing = true;
            } else {
                typing = false;
            }
        }
    }

    public void handleInput(char typedChar, int keyCode) {
        try {
            if (typing) {
                int digit = Character.digit(typedChar, 16);
                int limit = -Integer.MIN_VALUE;
                if (hex.length() > 0 && hex.charAt(0) == '-')
                    limit = Integer.MIN_VALUE;
                int multmin = limit / 16;

                switch (keyCode) {
                    case KEY_BACK:
                        if (hex.length() > 0) {
                            hex = hex.substring(0, hex.length() - 1);
                            if (hex.replace("-", "").length() > 0) {
                                if (value != null)
                                    value.castIfPossible(Integer.parseInt(hex, 16) + "");
                                currentValue = Integer.parseInt(hex, 16);
                            }
                        }
                        break;
                    case KEY_END:
                    case KEY_RETURN:
                        typing = false;
                        if (hex.replace("-", "").length() > 0) {
                            if (value != null) {
                                value.castIfPossible(Integer.parseInt(hex, 16) + "");
                            }
                            currentValue = Integer.parseInt(hex, 16);
                        }
                        break;
                    default:
                        if ((typedChar == '-' || digit >= 0)) {
                            hex = hex + typedChar;
                            if (hex.replace("-", "").length() > 0) {
                                if (value != null)
                                    value.castIfPossible(Integer.parseInt(hex, 16) + "");
                                currentValue = Integer.parseInt(hex, 16);
                            }
                        }
                        break;
                }
            }
        } catch (NumberFormatException e) {
            if (hex.length() > 0) {
                hex = hex.substring(0, hex.length() - 1);
            }
        }
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public enum Type {
        QUAD, SHADER;
    }
}