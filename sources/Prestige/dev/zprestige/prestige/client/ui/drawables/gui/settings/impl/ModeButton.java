package dev.zprestige.prestige.client.ui.drawables.gui.settings.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.SettingsDrawable;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import dev.zprestige.prestige.client.util.impl.animation.Animation;
import dev.zprestige.prestige.client.util.impl.animation.Easing;
import java.awt.Color;
import java.util.stream.IntStream;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public class ModeButton extends SettingsDrawable {
    public ModeSetting setting;
    public Animation anims;
    public float y;
    public boolean swap;

    public ModeButton(ModeSetting modeSetting, float f, float f2, float f3, float f4) {
        super(modeSetting, f, f2, f3, f4);
        setting = modeSetting;
        anims = new Animation(500, false, Easing.BACK_OUT);
        y = 0.3f;
    }

    @Override
    public void renderButton(int n, int n2, float f, float f2, float f3, float f4, float f5, float f6) {
        super.renderButton(n, n2, f, f2, f3, f4, f5, f6);
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        y = MathUtil.interpolate(y, !isInsideCross(n, n2) ? 0.3f : 0.7f, Interface.getDeltaTime() * 0.005f);
        anims.setState(swap);
        float f7 = 0.95f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f7, f7, f7);
        font.drawString(setting.getName() + ":", getX() / f7, (getY() + 7.5f - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(y, f2));
        font.drawString(setting.getObject(), (getX() + getWidth() - 12.5f - font.getStringWidth(setting.getObject()) * f7) / f7, (getY() + 7.5f - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(y, f2));
        matrixStack.pop();
        RenderUtil.renderArrows(getX() + getWidth() - 9.5f, getY() + 8, 1 - anims.getAnimationFactor(), RenderUtil.getColor(new Color(0, 0, 0, 150), f2));
        RenderUtil.renderArrows(getX() + getWidth() - 10, getY() + 7.5f, 1 - anims.getAnimationFactor(), RenderUtil.getColor(Color.WHITE, f2));
        float f11 = setting.getValues().length * 15.0f;
        if (anims.getAnimationFactor() > 0.001f) {
            RenderUtil.setScissorRegion(getX() - 5, f4, getX() + getWidth() + 5, Math.min(f6, getY() + getHeight() + 1.5f));
            RenderUtil.renderRoundedRectOutline(getX() - 0.5f, getY() + 14.5f, getX() + getWidth() + 0.5f, getY() + 15 + f11, RenderUtil.getColor(Color.BLACK, f2 * 0.3f), 0);
            RenderUtil.renderRoundedRectOutline(getX() - 1, getY() + 14, getX() + getWidth() + 1, getY() + 15.5f + f11, RenderUtil.getColor(Color.BLACK, f2 * 0.2f), 0);
            RenderUtil.renderRoundedRectOutline(getX() - 1.5f, getY() + 13.5f, getX() + getWidth() + 1.5f, getY() + 16 + f11, RenderUtil.getColor(Color.BLACK, f2 * 0.1f), 0);
            RenderUtil.renderColoredQuad(getX(), getY() + 15, getX() + getWidth(), getY() + 15 + f11, RenderUtil.getColor(0, f2));
            float f25 = 0.8f;
            float f26 = getY() + 15.0f;
            String[] stringArray = setting.getValues();
            for (int i = 0; i < stringArray.length; ++i) {
                String string = stringArray[i];
                matrixStack.push();
                matrixStack.scale(f25, f25, f25);
                Prestige.Companion.getFontManager().getFontRenderer().drawString(string, (getX() + 15) / f25, (f26 + 7.5f - Prestige.Companion.getFontManager().getFontRenderer().getStringHeight() / 1.5f) / f25, RenderUtil.getColor(n > getX() && n < getX() + getWidth() && n2 > f26 && n2 < f26 + 15 ? 1 : 0.5f, f2));
                matrixStack.pop();
                RenderUtil.renderFilledCircle(getX() + 5, f26 + 7.5f, 3, RenderUtil.getColor(-3, f2));
                RenderUtil.renderFilledCircle(getX() + 5, f26 + 7.5f, 2.5f, RenderUtil.getColor(-1, f2));
                if (setting.getObject().equals(string)) {
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
        if (n == 0) {
            if (isInsideCross((int)d, (int)d2)) {
                int n3 = IntStream.range(0, setting.getValues().length).filter(arg_0 -> ModeButton.isModeByIndex(this, arg_0)).findFirst().orElse(-1);
                setting.invokeValue(setting.getValues()[n3 + 1 >= setting.getValues().length ? 0 : n3 + 1]);
            }
            if (swap && d > getX() && d < getX() + getWidth()) {
                float f = getY() + 15.0f;
                String[] stringArray = setting.getValues();
                for (String string : stringArray) {
                    if (d2 > f && d2 < f + 15) {
                        setting.invokeValue(string);
                    }
                    f += 15;
                }
            }
        }
        if (n == 1 && isInsideCross((int)d, (int)d2)) {
            swap = !swap;
        }
    }

    static boolean isModeByIndex(ModeButton modeButton, int n) {
        return modeButton.setting.getValues()[n].equals(modeButton.setting.getObject());
    }

    public boolean getSwap() {
        return this.swap;
    }

    public void setSwap(boolean bl) {
        this.swap = bl;
    }

    boolean isInsideCross(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + 15;
    }
}