package dev.zprestige.prestige.client.ui.drawables.gui.settings.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.setting.impl.MultipleSetting;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.SettingsDrawable;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import dev.zprestige.prestige.client.util.impl.animation.Animation;
import dev.zprestige.prestige.client.util.impl.animation.Easing;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class MultipleButton extends SettingsDrawable {
    public MultipleSetting comboSetting;
    public Animation anims;
    public float x;
    public boolean swap;

    public MultipleButton(MultipleSetting multipleSetting, float f, float f2, float f3, float f4) {
        super(multipleSetting, f, f2, f3, f4);
        this.comboSetting = multipleSetting;
        this.anims = new Animation(500, false, Easing.BACK_OUT);
        this.x = 0.3f;
    }

    @Override
    public void renderButton(int n, int n2, float f, float f2, float f3, float f4, float f5, float f6) {
        super.renderButton(n, n2, f, f2, f3, f4, f5, f6);
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        x = MathUtil.interpolate(x, isInsideCross(n, n2) ? 0.7f : 0.3f, Interface.getDeltaTime() * 0.005f);
        float f7 = 0.95f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f7, f7, f7);
        font.drawString(comboSetting.getName() + ":", getX() / f7, (getY() + 7.5f - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(x, f2));
        matrixStack.pop();
        anims.setState(swap);
        RenderUtil.renderArrows(getX() + getWidth() - 9.5f, getY() + 8, 1 - anims.getAnimationFactor(), RenderUtil.getColor(new Color(0, 0, 0, 150), f2));
        RenderUtil.renderArrows(getX() + getWidth() - 10, getY() + 7.5f, 1 - anims.getAnimationFactor(), RenderUtil.getColor(Color.WHITE, f2));
        float f11 = comboSetting.getOptions().length * 15;
        if (anims.getAnimationFactor() > 0.001f) {
            RenderUtil.setScissorRegion(getX() - 5, f4, getX() + getWidth() + 5, Math.min(f6, getY() + getHeight() + 1.5f));
            RenderUtil.renderRoundedRectOutline(getX() - 0.5f, getY() + 14.5f, getX() + getWidth() + 0.5f, getY() + 15 + f11, RenderUtil.getColor(Color.BLACK, f2 * 0.3f), 0);
            RenderUtil.renderRoundedRectOutline(getX() - 1, getY() + 14, getX() + getWidth() + 1, getY() + 15.5f + f11, RenderUtil.getColor(Color.BLACK, f2 * 0.2f), 0);
            RenderUtil.renderRoundedRectOutline(getX() - 1.5f, getY() + 13.5f, getX() + getWidth() + 1.5f, getY() + 16 + f11, RenderUtil.getColor(Color.BLACK, f2 * 0.1f), 0);
            RenderUtil.renderColoredQuad(getX(), getY() + 15, getX() + getWidth(), getY() + 15 + f11, RenderUtil.getColor(0, f2));
            float f25 = 0.8f;
            float f26 = getY() + 15;
            String[] stringArray = comboSetting.getOptions();
            for (int i = 0; i < stringArray.length; ++i) {
                String string = stringArray[i];
                matrixStack.push();
                matrixStack.scale(f25, f25, f25);
                font.drawString(string, (getX() + 15) / f25, (f26 + 7.5f - font.getStringHeight() / 1.5f) / f25, RenderUtil.getColor(n > getX() && n < getX() + getWidth() && n2 > f26 && n2 < f26 + 15 ? 1 : 0.5f, f2));
                matrixStack.pop();
                RenderUtil.renderFilledCircle(getX() + 5, f26 + 7.5f, 3, RenderUtil.getColor(-3, f2));
                RenderUtil.renderFilledCircle(getX() + 5, f26 + 7.5f, 2.5f, RenderUtil.getColor(-1, f2));
                if (comboSetting.getValue(string)) {
                    RenderUtil.renderFilledCircle(getX() + 5, f26 + 7.5f, 2.5f, RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), f2));
                }
                f26 += 15;
            }
            GL11.glDisable(3089);
        }
        setHeight(15 + f11 * anims.getAnimationFactor());
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        if (n == 0 && swap && d > getX() && d < getX() + getWidth()) {
            float f = getY() + 15;
            String[] stringArray = comboSetting.getOptions();
            for (String string : stringArray) {
                if (d2 > f && d2 < f + 15) {
                    comboSetting.invokeValue(string, !comboSetting.getValue(string));
                }
                f += 15;
            }
        }
        if (n == 1 && isInsideCross((int)d, (int)d2)) {
            swap = !swap;
        }
    }

    boolean isInsideCross(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + 15;
    }
}