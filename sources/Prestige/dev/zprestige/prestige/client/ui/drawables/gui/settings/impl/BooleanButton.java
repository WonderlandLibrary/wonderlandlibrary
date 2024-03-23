package dev.zprestige.prestige.client.ui.drawables.gui.settings.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
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

public class BooleanButton extends SettingsDrawable {
    public BooleanSetting setting;
    public Animation anims;
    public float x;

    public BooleanButton(BooleanSetting booleanSetting, float f, float f2, float f3, float f4) {
        super(booleanSetting, f, f2, f3, f4);
        setting = booleanSetting;
        anims = new Animation(500, setting.getObject(), Easing.EXPO_IN_OUT);
        x = setting.getObject() ? 1 : 0.3f;
    }

    @Override
    public void renderButton(int n, int n2, float f, float f2, float f3, float f4, float f5, float f6) {
        super.renderButton(n, n2, f, f2, f3, f4, f5, f6);
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        x = MathUtil.interpolate(x, setting.getObject() ? (isOver(n, n2) ? 0.8f : 1) : (isOver(n, n2) ? 0.7f : 0.3f), Interface.getDeltaTime() * 0.005f);
        anims.setState(setting.getObject());
        float f7 = 0.95f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f7, f7, f7);
        font.drawString(setting.getName(), getX() / f7, (getY() + getHeight() / 2.0f - font.getStringHeight() / 1.5f) / f7, RenderUtil.getColor(x, f2));
        matrixStack.pop();
        RenderUtil.renderRoundedRect(getX() + getWidth() - 20, getY() + 3, getX() + getWidth(), getY() + 12, RenderUtil.getColor(3, f2), 4.5f);
        RenderUtil.setScissorRegion(f3, f4, Math.min(getX() + getWidth() - 20 * (1 - anims.getAnimationFactor()), f5), f6);
        RenderUtil.renderRoundedRect(getX() + getWidth() - 20, getY() + 3, getX() + getWidth(), getY() + 12, RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), f2 * 0.5f), 4.5f);
        GL11.glDisable(3089);
        RenderUtil.setScissorRegion(f3, f4, f5, f6);
        RenderUtil.renderRoundedRectOutline(getX() + getWidth() - 20, getY() + 3, getX() + getWidth(), getY() + 12,RenderUtil.getColor(-3, f2), 4.5f);
        RenderUtil.renderFilledCircle(getX() + getWidth() - 20 * (1 - anims.getAnimationFactor()), getY() + 7.5f, 5.5f, RenderUtil.getColor(-3, f2));
        RenderUtil.renderFilledCircle(getX() + getWidth() - 20 * (1 - anims.getAnimationFactor()), getY() + 7.5f, 5, RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), f2));
        GL11.glDisable(3089);
        setHeight(15);
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        if (n == 0 && isOver((int)d, (int)d2)) {
            setting.invokeValue(!setting.getObject());
        }
    }

    boolean isOver(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + getHeight();
    }

}