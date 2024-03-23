package dev.zprestige.prestige.client.ui.drawables.gui.settings.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.setting.impl.ColorSetting;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.SettingsDrawable;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import dev.zprestige.prestige.client.util.impl.animation.Animation;
import dev.zprestige.prestige.client.util.impl.animation.Easing;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public class ColorButton extends SettingsDrawable {
    public ColorSetting color;
    public float width;
    public Animation animation;
    public float centrals;
    public float centrals2;
    public float centrals3;
    public float centrals4;
    public boolean over;
    public boolean over2;
    public boolean over3;
    public boolean expanded;

    public ColorButton(ColorSetting colorSetting, float f, float f2, float f3, float f4) {
        super(colorSetting, f, f2, f3, f4);
        color = colorSetting;
        width = 0.3f;
        animation = new Animation(500, false, Easing.BACK_OUT);
        centrals = hue(color.getObject());
        centrals4 = getAlphaSingle(color.getObject());
        centrals2 = hue2(color.getObject());
        centrals3 = 1 - hue3(color.getObject());
    }

    @Override
    public void renderButton(int n, int n2, float f, float f2, float f3, float f4, float f5, float f6) {
        super.renderButton(n, n2, f, f2, f3, f4, f5, f6);
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        width = MathUtil.interpolate(width, isOver2(n, n2) ? 0.7f : 0.3f, Interface.getDeltaTime() * 0.005f);
        animation.setState(expanded);
        float f7 = 0.95f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f7, f7, f7);
        font.drawString(color.getName(), getX() / f7, (getY() + 7.5f - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(width, f2));
        matrixStack.pop();
        RenderUtil.renderFilledCircle(getX() + getWidth() - 7.5f, getY() + 7.5f, 4.5f, RenderUtil.getColor(-3, f2));
        RenderUtil.renderFilledCircle(getX() + getWidth() - 7.5f, getY() + 7.5f, 4.0f, RenderUtil.getColor(color.getObject(), f2 * color.getObject().getAlpha() / 255));
        if (animation.getAnimationFactor() > 0.001f) {
            colorCombination(f2, f4, f6);
            centrals2 = MathUtil.interpolate(centrals2, hue2(color.getObject()), (Interface.getDeltaTime() * 0.005f) * 2);
            centrals3 = MathUtil.interpolate(centrals3, 1 - hue3(color.getObject()), (Interface.getDeltaTime() * 0.005f) * 2);
            RenderUtil.renderRoundedRectOutline(getX() + 5, getY() + 20, getX() + getWidth() - 5, getY() + 20 + 70, RenderUtil.getColor(-3, f2), 0);
            if (over) {
                color.invokeValue(Color.getHSBColor(hue(color.getObject()), MathUtil.findMiddleValue((n - (getX() + 5)) / (getWidth() - 10), 0, 1), MathUtil.findMiddleValue(1 - (n2 - (getY() + 20)) / 70, 0, 1)));
            }
            RenderUtil.renderRoundedRectOutline(getX() + 5, getY() + 95, getX() + getWidth() - 5, getY() + 105, RenderUtil.getColor(-3, f2), 0);
            for (int i = 0; i < 6; ++i) {
                Color color = RenderUtil.getColor(new Color(Color.HSBtoRGB((float)i / 6, 1, 1)), f2);
                Color color2 = RenderUtil.getColor(new Color(Color.HSBtoRGB(((float)i + 1) / 6, 1, 1)), f2);
                RenderUtil.renderColoredQuad(getX() + 5 + i * 15, getY() + 95, getX() + 5 + i * 15 + 15, getY() + 105, color, color2, color, color2);
            }
            renderCentrals(f2, n, n2);
        }
        setHeight(15 + 125 * animation.getAnimationFactor());
    }

    void renderCentrals(float f, int n, int n2) {
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        float f5 = hue(color.getObject());
        centrals = MathUtil.interpolate(centrals, f5, Interface.getDeltaTime() * 0.005f);
        RenderUtil.renderColoredQuad(getX() + 5 + (getWidth() - 10) * centrals - 1.5f, getY() + 94, getX() + 3 + (getWidth() - 10) * centrals + 1.5f, getY() + 106, RenderUtil.getColor(10, f));
        RenderUtil.renderRoundedRectOutline(getX() + 5 + (getWidth() - 10) * centrals - 1.5f, getY() + 94, getX() + 3 + (getWidth() - 10) * centrals + 1.5f, getY() + 106, RenderUtil.getColor(-5, f), 0);
        RenderUtil.renderRoundedRectOutline(getX() + 5, getY() + 110, getX() + getWidth() - 5, getY() + 120, RenderUtil.getColor(-3, f), 0.0f);
        RenderUtil.renderColoredQuad(getX() + 5, getY() + 110, getX() + getWidth() - 2, getY() + 120, RenderUtil.getColor(color.getObject(), f), new Color(0, 0, 0, 0), RenderUtil.getColor(color.getObject(), f), new Color(0, 0, 0, 0));
        centrals4 = MathUtil.interpolate(centrals4, 1 - getAlphaSingle(color.getObject()), Interface.getDeltaTime() * 0.005f);
        RenderUtil.renderColoredQuad(getX() + 5 + (getWidth() - 10) * centrals4 - 1.5f, getY() + 109, getX() + 3 + (getWidth() - 10) * centrals4 + 1.5f, getY() + 121,RenderUtil.getColor(10, f));
        RenderUtil.renderRoundedRectOutline(getX() + 5 + (getWidth() - 10) * centrals4 - 1.5f, getY() + 109, getX() + 3 + (getWidth() - 10) * centrals4 + 1.5f, getY() + 121, RenderUtil.getColor(-5, f), 0);
        RenderUtil.renderColoredQuad(getX() + 5, getY() + 125, getX() + getWidth() / 2 - 2.5f, getY() + 135, RenderUtil.getColor(-1, f));
        RenderUtil.renderRoundedRectOutline(getX() + 5, getY() + 125, getX() + getWidth() / 2 - 2.5f, getY() + 135, RenderUtil.getColor(-3, f), 0);
        RenderUtil.renderColoredQuad(getX() + getWidth() / 2 + 2.5f, getY() + 125, getX() + getWidth() - 5, getY() + 135, RenderUtil.getColor(-1, f));
        RenderUtil.renderRoundedRectOutline(getX() + getWidth() / 2 + 2.5f, getY() + 125, getX() + getWidth() - 5, getY() + 135, RenderUtil.getColor(-3, f), 0);
        float f7 = 0.8f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f7, f7, f7);
        font.drawString("Copy", (getX() + 5 + 21.25f - font.getStringWidth("Copy") * f7 / 2.0f) / f7, (getY() + 130 - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(isOver5(n, n2) ? 0.5f : 0.8f, f));
        font.drawString("Paste", (getX() + getWidth() / 2 + 2.5f + 21.25f - font.getStringWidth("Paste") * f7 / 2) / f7, (getY() + 130 - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(isOver6(n, n2) ? 0.5f : 0.8f, f));
        matrixStack.pop();
        if (over2) {
            color.invokeValue(Color.getHSBColor(MathUtil.findMiddleValue(((float)n - (getX() + 5)) / (getWidth() - 10), 0, 1), hue2(color.getObject()), hue3(color.getObject())));
            color.invokeValue(new Color(color.getObject().getRed(), color.getObject().getGreen(), color.getObject().getBlue(), color.getObject().getAlpha()));
        }
        if (over3) {
            Color color = this.color.getObject();
            this.color.invokeValue(new Color((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255, MathUtil.findMiddleValue(1 - ((float)n - (getX() + 5)) / (getWidth() - 10), 0, 1)));
        }
        GL11.glDisable(3089);
    }

    void colorCombination(float f, float f2, float f3) {
        RenderUtil.setScissorRegion(getX() - 5, f2, getX() + getWidth() + 5, Math.min(f3, getY() + getHeight() + 1.5f));
        RenderUtil.renderRoundedRectOutline(getX() - 0.5f, getY() + 14.5f, getX() + getWidth() + 0.5f, getY() + 15 + 125, RenderUtil.getColor(Color.BLACK, f * 0.3f), 0);
        RenderUtil.renderRoundedRectOutline(getX() - 1, getY() + 14, getX() + getWidth() + 1, getY() + 15.5f + 125, RenderUtil.getColor(Color.BLACK, f * 0.2f), 0);
        RenderUtil.renderRoundedRectOutline(getX() - 1.5f, getY() + 13.5f, getX() + getWidth() + 1.5f, getY() + 16 + 125, RenderUtil.getColor(Color.BLACK, f * 0.1f), 0);
        RenderUtil.renderColoredQuad(getX(), getY() + 15, getX() + getWidth(), getY() + 15 + 125, RenderUtil.getColor(0, f));
        RenderUtil.renderGradient(getX() + 5, getY() + 20, getX() + getWidth() - 5, getY() + 20 + 70, Color.getHSBColor(hue(color.getObject()), 1, 1), f);
        RenderUtil.renderFilledCircle(getX() + 5 + (getWidth() - 10) * centrals2, getY() + 20 + 70 * centrals3, 3, RenderUtil.getColor(-3, f));
        RenderUtil.renderFilledCircle(getX() + 5 + (getWidth() - 10) * centrals2, getY() + 20 + 70 * centrals3, 2.5f, RenderUtil.getColor(5, f));
    }

    @Override
    public void mouseClicked(double n, double n2, int n3) {
        if ((n3 == 0 || n3 == 1) && isOver2((int)n, (int)n2)) {
            expanded = !expanded;
        }
        if (n3 == 0 && expanded) {
            if (isOver((int) n, (int) n2)) {
                over = true;
            }
            if (isOver4((int)n, (int)n2)) {
                over2 = true;
            }
            if (isOver3((int)n, (int)n2)) {
                over3 = true;
            }
            if (this.isOver5((int)n, (int)n2)) {
                Object[] original = { color.getObject().getRGB() & 0xFFFFFF };
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(String.format("#%06x", Arrays.copyOf(original, original.length)) + ":" + color.getObject().getAlpha()), null);
            }
            if (this.isOver6((int)n, (int)n2)) {
                String string;
                try {
                    string = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
                } catch (UnsupportedFlavorException | IOException e) {
                    throw new RuntimeException(e);
                }
                if (string.contains(":")) {
                    String[] split = string.split(":");
                    if (split.length == 2) {
                        color.invokeValue(Color.decode(split[0]));
                        color.invokeValue(new Color(color.getObject().getRed(), color.getObject().getGreen(), color.getObject().getBlue(), Integer.parseInt(split[1])));
                    }
                } else {
                    color.invokeValue(Color.decode(string));
                    color.invokeValue(new Color(color.getObject().getRed(), color.getObject().getGreen(), color.getObject().getBlue(), 255));
                }
            }
        }
    }

    @Override
    public void mouseReleased(double d, double d2, int n) {
        this.over = false;
        this.over2 = false;
        this.over3 = false;
    }

    boolean isOver(final int n, final int n2) {
        return n > getX() + 5 && n < getX() + getWidth() - 5 && n2 > getY() + 20 && n2 < getY() + 90;
    }

    boolean isOver2(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + 15.0f;
    }

    boolean isOver3(int n, int n2) {
        return n > getX() + 5 && n < getX() + getWidth() - 5 && n2 > getY() + 110 && n2 < getY() + 120;
    }

    boolean isOver5(int n, int n2) {
        return n > getX() + 5 && n < getX() + getWidth() / 2 - 2.5f && n2 > getY() + 125 && n2 < getY() + 135;
    }

    boolean isOver4(int n, int n2) {
        return n > getX() + 5 && n < getX() + getWidth() - 5 && n2 > getY() + 95 && n2 < getY() + 105;
    }

    boolean isOver6(int n, int n2) {
        return n > getX() + getWidth() / 2 + 2.5f && n < getX() + getWidth() - 5 && n2 > getY() + 125 && n2 < getY() + 135;
    }

    float hue(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[0];
    }

    float hue2(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[1];
    }

    float hue3(Color color) {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null)[2];
    }

    public void applyAnimation(Animation animation) {
        this.animation = animation;
    }

    public void method1137(boolean bl) {
        this.expanded = bl;
    }

    float getAlphaSingle(Color color) {
        return (float)color.getAlpha() / 255.0f;
    }
}
