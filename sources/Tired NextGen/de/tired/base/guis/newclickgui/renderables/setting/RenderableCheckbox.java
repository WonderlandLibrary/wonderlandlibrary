package de.tired.base.guis.newclickgui.renderables.setting;


import de.tired.base.guis.newclickgui.abstracts.ClickGUIHandler;
import de.tired.util.animation.Animation;
import de.tired.util.animation.ColorAnimation;
import de.tired.util.animation.Easings;

import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.util.render.Scissoring;
import de.tired.util.render.Translate;
import de.tired.base.font.FontManager;
import de.tired.util.render.AnimationUtil;
import de.tired.util.render.ColorUtil;
import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderableCheckbox extends ClickGUIHandler {

    public BooleanSetting checkBox;

    private float animation = 0;

    public float float2;
    public float float3;

    private Animation fadeInAlpha = new Animation();

    private ColorAnimation colorAnimation = new ColorAnimation();

    private Animation roundessAnimation = new Animation();

    public Translate translate;
    private boolean hovering;

    boolean reverse = false;

    private Animation movingAnimation = new Animation();

    private Animation movingAnimation2 = new Animation();

    private Animation checkBoxAnimation = new Animation();

    private Animation textFadeInAnimation = new Animation();

    public Translate translateText;

    public RenderableCheckbox(BooleanSetting checkBox) {
        this.checkBox = checkBox;
        this.translate = new Translate(0, 0);
        translateText = new Translate(0, 0);
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY) {

    }

    public void onReset() {
        translate.interpolate(0, 0, 5);
        float2 = 0;
        fadeInAlpha.setValue(0);

        translateText.interpolate(0, 0, 5);
        textFadeInAnimation.setValue(.5f);

    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseKey) {

        if (!hovering) return;

        if (mouseKey == 0)
            checkBox.setValue(!checkBox.getValue());

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int x, int y) {

        fadeInAlpha.update();
        fadeInAlpha.animate(255, .1, Easings.BOUNCE_IN);


        // Gui.drawRect(x, y, x + width, y + height, new Color(35, 36, 42, (int) fadeInAlpha.getValue()).getRGB());

        hovering = this.isHovered(mouseX, mouseY, x + width - 35, y, 20, 15);

        roundessAnimation.update();
        roundessAnimation.animate(checkBox.getValue() ? 3 : 2, .1, Easings.NONE);

        colorAnimation.update();

        Color checkBoxColor = checkBox.getModule().isState() ? (checkBox.getValue() ? new Color(31, 94, 255) : new Color(244, 244, 244, 122)) : (checkBox.getValue() ? new Color(31, 94, 255) : new Color(40, 40, 40));
        new Color(50, 55, 65, (int) fadeInAlpha.getValue());

        colorAnimation.animate(checkBoxColor, .1);


        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x + width - 33, y + 2.5f, 10, 10, roundessAnimation.getValue(), colorAnimation.getColor());

        String name = checkBox.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        //  Gui.drawRect(x - checkBoxWidth + width - checkBoxWidth, y + 2 - checkBoxHeight + height - checkBoxHeight, x + width - 5, y + 2 + height - 5, hovering ? new Color(10, 10, 10).getRGB() : new Color(20, 20, 20).getRGB());


        animation = (float) AnimationUtil.getAnimationState(animation, checkBox.getValue() ? 0 : 30, 70);

        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        checkBoxAnimation.update();
        checkBoxAnimation.animate(checkBox.getValue() ? 0 : .5, .1, Easings.NONE);

        if (checkBox.getValue()) {
            if (float2 < 0.5F) {
                translate.interpolate(scaledResolution.getScaledWidth() / 2f, scaledResolution.getScaledHeight() / 2f, 8);
                float2 += 0.001 *RenderUtil.instance.delta;
            }
        } else {
            translate.interpolate(0, 0, 5);
            float2 = 0;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef((x + width - 30f), y + 8.5f, 0);
        GL11.glScaled(0.5 + translate.getX() / scaledResolution.getScaledWidth() - checkBoxAnimation.getValue(), 0.5 + translate.getY() / scaledResolution.getScaledHeight() - checkBoxAnimation.getValue(), 0);
        GL11.glTranslatef(-(x + width - 30f), -(y + 8.5f), 0);

        if (this.translate.getX() > 280.0F) {
            RenderUtil.instance.doRenderShadow(Color.WHITE, calculateMiddle("5", FontManager.iconFontSmall, x + width - 33, 11) - .5f, y + 2.5f, 5, 5, 12);
           RenderUtil.instance.drawCheckMark(x + width - 23, y + .5f, 5, -1);
            // FontManager.iconFontSmall.drawString("5", calculateMiddle("5", FontManager.iconFontSmall, x + width - 33, 11) + .5f, y + 8.5f, new Color(255, 255, 255, (int) fadeInAlpha.getValue()).getRGB());
        }
        GL11.glPopMatrix();


        if (float3 < 0.5F) {
            translateText.interpolate(scaledResolution.getScaledWidth() / 2f, scaledResolution.getScaledHeight() / 2f, 8);
            float3 += 0.001 *RenderUtil.instance.delta;
        }
        textFadeInAnimation.update();
        textFadeInAnimation.animate(0, .1, Easings.NONE);

        Animation usingAnimation = null;


        movingAnimation2.update();

        movingAnimation.update();

        movingAnimation.animate(10, .2, Easings.NONE);

        movingAnimation2.animate(FontManager.interMedium14.getStringWidth(name) + 20, 5, true);

        if (Math.round(movingAnimation2.getValue()) >= FontManager.interMedium14.getStringWidth(name) + 19) {
            movingAnimation2.setValue(-FontManager.interMedium14.getStringWidth(name));
        }


        Color firstColor = ColorUtil.interpolateColorsBackAndForth(24, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);

        Color color1 = checkBox.getModule().isState() ? firstColor : new Color(45, 45, 45);

        final double posX = FontManager.interMedium14.getStringWidth(name) <= 76 ? 2 : movingAnimation2.getValue();
        Scissoring.SCISSORING.startScissor();
        Scissoring.SCISSORING.doScissor((float) (x + 2), y - 15, x + 80, y + 20);
        FontManager.interMedium14.drawString(name, (float) (x + posX), y + 8, -1);
        if (FontManager.interMedium14.getStringWidth(name) >= 76) {
            FontManager.interMedium14.drawString(name, (float) (x + posX + FontManager.interMedium14.getStringWidth(name) + 10), y + 8, -1);
            FontManager.interMedium14.drawString(name, (float) (x + posX - FontManager.interMedium14.getStringWidth(name) - 10), y + 8, -1);
            RenderUtil.instance.doRenderShadow(color1, x + width - 45, y + 3, 40, 4, 22);
        }
        // if (FontManager.interMedium14.getStringWidth(name) >= 76)
        // FontManager.interMedium14.drawString(name, (float) (x  -posX + 80), y + 8, -1);
        Scissoring.SCISSORING.disableScissor();
        //    Color firstColor = ColorUtil.interpolateColorsBackAndForth(24, 0, new Color(84, 51, 158).brighter(), new Color(104, 127, 203), true);
        Color secondColor = ColorUtil.interpolateColorsBackAndForth(43, 90, new Color(84, 51, 158), new Color(104, 127, 203).darker(), false);


        //  fontRenderer.drawString(name, x + 3, y + 7, new Color(255, 255, 255, (int) fadeInAlpha.getValue()).getRGB());
    }
}
