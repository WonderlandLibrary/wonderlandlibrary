package dev.zprestige.prestige.client.ui.drawables.gui.settings.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.SettingsDrawable;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.util.math.MatrixStack;

public class IntButton extends SettingsDrawable {
    public IntSetting setting;
    public float value;
    public float x;
    public boolean dragging;

    public IntButton(IntSetting intSetting, float f, float f2, float f3, float f4) {
        super(intSetting, f, f2, f3, f4);
        this.setting = intSetting;
        this.value = setting.getObject();
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
        font.drawString(setting.getName(), getX() / f7, (getY() - 5) / f7, RenderUtil.getColor(x, f2));
        font.drawString(String.valueOf(setting.getObject()), (getX() + getWidth() - 2.5f - font.getStringWidth(String.valueOf(setting.getMax()))) / f7, (getY() - 5) / f7, RenderUtil.getColor(x, f2));
        matrixStack.pop();
        RenderUtil.renderColoredQuad(getX(), getY() + getHeight() - 2, getX() + getWidth(), getY() + getHeight(),RenderUtil.getColor(1, f2));
        RenderUtil.renderRoundedRectOutline(getX(), getY() + getHeight() - 2, getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(-3, f2), 0);
        value = MathUtil.interpolate(value, setting.getObject(), Interface.getDeltaTime() * 0.005f);
        float f8 = (value - setting.getMin()) / (setting.getMax() - setting.getMin());
        RenderUtil.renderColoredQuad(getX(), getY() + getHeight() - 2, getX() + getWidth() * f8, getY() + getHeight(), RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), f2));
        RenderUtil.renderFilledCircle(getX() + getWidth() * f8, getY() + getHeight() - 1, 4, RenderUtil.getColor(-3, f2));
        RenderUtil.renderFilledCircle(getX() + getWidth() * f8, getY() + getHeight() - 1, 3.5f, RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), f2));
        if (dragging) {
            setting.invokeValue(Math.round(MathUtil.findMiddleValue( setting.getMin() + (setting.getMax() - setting.getMin()) * ((n - getX()) / getWidth()), setting.getMin(), setting.getMax())));
        }
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        if (n == 0 && isInsideCross((int)d, (int)d2)) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseReleased(double d, double d2, int n) {
        if (n == 0) {
            this.dragging = false;
        }
    }
    boolean isInsideCross(final int n, final int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + getHeight();
    }
}
