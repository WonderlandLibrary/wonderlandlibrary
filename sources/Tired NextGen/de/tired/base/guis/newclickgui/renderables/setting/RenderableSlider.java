package de.tired.base.guis.newclickgui.renderables.setting;

import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.util.animation.Animation;
import de.tired.util.animation.ColorAnimation;
import de.tired.util.animation.Easings;

import de.tired.util.math.MathUtil;
import de.tired.util.render.ColorUtil;
import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.abstracts.ClickGUIHandler;
import de.tired.util.render.Translate;
import de.tired.base.font.CustomFont;
import de.tired.base.font.FontManager;
import de.tired.util.render.AnimationUtil;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectGradient;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderableSlider extends ClickGUIHandler {

    public NumberSetting setting;

    public float animationX;
    public double animation;
    private boolean dragging;
    private int x, y;

    public float float3;

    private ColorAnimation circleColor = new ColorAnimation();

    private Animation circleSize = new Animation();

    private Animation fadeInAlpha = new Animation();

    public Translate translateText;

    private Animation textFadeInAnimation = new Animation();

    public RenderableSlider(NumberSetting setting) {
        this.setting = setting;
        translateText = new Translate(0, 0);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        dragging = false;
        setting.dragged = false;
    }

    public void onReset() {
        fadeInAlpha.setValue(0);
        translateText.interpolate(0, 0, 5);
        textFadeInAnimation.setValue(.5f);
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseKey) {
        if (isOver(x + 2, y + 1, width - 12, 10, mouseX, mouseY)) {
            this.dragging = true;
            setting.dragged = true;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int x, int y) {
        this.x = x;
        this.y = y;
        animation = AnimationUtil.getAnimationState(animation, width, 1222);
        double value = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());

        if (this.dragging) {
            double valueX = (float) (mouseX) - (this.x + 10);

            value = valueX / (float) (this.width - 38);

            if (value < 0.0F) {
                value = 0F;
            }

            if (value > 1.0F) {
                value = 1.0F;
            }

        }


        fadeInAlpha.update();
        fadeInAlpha.animate(255, .1, Easings.BOUNCE_IN);

        double _value = setting.getMin() + (setting.getMax() - setting.getMin()) * value;

        double disValue = Math.round(_value / setting.getInc()) * setting.getInc();
        final double percent = this.setting.getValue() * 100 / this.setting.getMax();
        final double calcWidth = (width - 38) * percent / 100;
        this.animationX = (float) AnimationUtil.getAnimationState(this.animationX, calcWidth, Math.max(2.6D, Math.abs((double) this.animationX - calcWidth) * 2));
        setting.setValue(disValue);

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + 10, y + 4, width - 38, 2, 1, setting.getModule().isState() ? new Color(244, 244, 244, 122) : new Color(66, 69, 80, (int) fadeInAlpha.getValue()));

        String name = setting.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        final String toString = String.valueOf(Math.round(disValue * 10.0) / 10.0).replace(".0", "");


        Color sliderColor = ColorUtil.interpolateColorsBackAndForth((int) (12), 0, (new Color(0, 105, 255, (int) fadeInAlpha.getValue())), new Color(0, 105, 255, (int) fadeInAlpha.getValue()).darker(), false);

        circleColor.update();
        circleColor.animate(setting.getModule().isState() ? new Color(244, 244, 244, 255) : sliderColor, .2);

        GlStateManager.disableBlend();

        circleSize.update();
        circleSize.animate(isOver(x + 2, y + 1, width - 12, 10, mouseX, mouseY) ? 5 : 4, .03, Easings.CIRC_IN);

        ShaderManager.shaderBy(RoundedRectGradient.class).drawRound(x + 10, y + 4, animationX, 2, 1, sliderColor, sliderColor, sliderColor.brighter(), sliderColor.brighter());

        RenderUtil.instance.doRenderShadow(Color.BLACK, x + animationX + 9, y + 3, 5, 5, 12);
        RenderUtil.instance.drawCircle(x + animationX + 9, y + 5, circleSize.getValue(), circleColor.getColor().getRGB());

        //   Extension.EXTENSION.getGenerallyProcessor().renderProcessor.renderSmoothCircle(x + animationX + 4, y + 5, 3, Color.WHITE.getRGB());

        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        FontManager.interMedium14.drawString(name, x + 2, y - 5, -1);


        if (isOver(x + 2, y + 1, width - 12, 10, mouseX, mouseY)) {
            if (float3 < 0.5F) {
                translateText.interpolate(scaledResolution.getScaledWidth() / 2f, scaledResolution.getScaledHeight() / 2f, 8);
                float3 += 0.001 * RenderUtil.instance.delta;
            }
        } else {
            float3 = 0;
            translateText.interpolate(0, 0, 4);
        }

        textFadeInAnimation.update();
        textFadeInAnimation.animate(isOver(x + 2, y + 1, width - 12, 10, mouseX, mouseY) ? 0 : .5, .1, Easings.NONE);

        float i = calculateMiddle(toString + "", FontManager.raleWay10, x + animationX + 4 - FontManager.raleWay10.getStringWidth(toString), (FontManager.raleWay10.getStringWidth(toString) * 2) + 10);
        GL11.glPushMatrix();
        GL11.glTranslatef((i + 4), y + (float) height, 0);
        GL11.glScaled(0.5 + translateText.getX() / scaledResolution.getScaledWidth() - textFadeInAnimation.getValue(), 0.5 + translateText.getY() / scaledResolution.getScaledHeight() - textFadeInAnimation.getValue(), 0);
        GL11.glTranslatef(-(i + 4), -(y + (float) height), 0);

        RenderUtil.instance.doRenderShadow(Color.BLACK, x + animationX + 6, y + 14, 6, 3, 12);
        FontManager.raleWay10.drawString(toString, x + animationX + 6, y + 14, -1);


        GlStateManager.popMatrix();

    }


    public boolean isOver(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public int calculateMiddle(String text, CustomFont fontRenderer, int x, int widht) {
        return (int) ((float) (x + widht) - (fontRenderer.getStringWidth(text) / 2f) - (float) widht / 2);
    }


}