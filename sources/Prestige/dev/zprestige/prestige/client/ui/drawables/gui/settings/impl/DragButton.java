package dev.zprestige.prestige.client.ui.drawables.gui.settings.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.ui.Interface;
import dev.zprestige.prestige.client.ui.drawables.gui.settings.SettingsDrawable;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.MathUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import net.minecraft.client.util.math.MatrixStack;

public class DragButton extends SettingsDrawable {
    public DragSetting setting;
    public float first;
    public float x;
    public float animX;
    public boolean dragging;
    public boolean active;

    public DragButton(DragSetting dragSetting, float f, float f2, float f3, float f4) {
        super(dragSetting, f, f2, f3, f4);
        setting = dragSetting;
        first = setting.getFirst();
        x = setting.getSecond();
        animX = 0.3f;
    }

    @Override
    public void renderButton(int n, int n2, float f, float f2, float f3, float f4, float f5, float f6) {
        super.renderButton(n, n2, f, f2, f3, f4, f5, f6);
        FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
        animX = MathUtil.interpolate(animX, isInsideCross(n, n2) ? 0.7f : 0.3f, Interface.getDeltaTime() * 0.005f);
        float f10 = 0.95f;
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.push();
        matrixStack.scale(f10, f10, f10);
        font.drawString(setting.getName(), getX() / f10, (getY() - 5) / f10, RenderUtil.getColor(animX, f2));
        font.drawString(setting.getFirst() + " - " + setting.getSecond(), (getX() + getWidth() - 2.5f - font.getStringWidth(String.valueOf(setting.getMax())) * 2) / f10, (getY() - 5) / f10, RenderUtil.getColor(animX, f2));
        matrixStack.pop();
        RenderUtil.renderColoredQuad(getX(), getY() + getHeight() - 2, getX() + getWidth(), getY() + getHeight(), RenderUtil.getColor(1, f2));
        RenderUtil.renderRoundedRectOutline(getX(), getY() + getHeight() - 2, getX() + getWidth(), getY() + getHeight(),RenderUtil.getColor(-3, f2), 0);
        first = MathUtil.interpolate(first, setting.getFirst(), Interface.getDeltaTime() * 0.005f);
        float f11 = (first - setting.getMin()) / (setting.getMax() - setting.getMin());
        RenderUtil.renderFilledCircle(getX() + getWidth() * f11, getY() + getHeight() - 1, 4, RenderUtil.getColor(-3, f2));
        RenderUtil.renderFilledCircle(getX() + getWidth() * f11, getY() + getHeight() - 1, 3.5f, RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), f2));
        x = MathUtil.interpolate(x, setting.getSecond(), Interface.getDeltaTime() * 0.005f);
        float f12 = (x - setting.getMin()) / (setting.getMax() - setting.getMin());
        RenderUtil.renderFilledCircle(getX() + getWidth() * f12, getY() + getHeight() - 1, 4, RenderUtil.getColor(-3, f2));
        RenderUtil.renderFilledCircle(getX() + getWidth() * f12, getY() + getHeight() - 1, 3.5f, RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), f2));
        RenderUtil.renderColoredQuad(getX() + getWidth() * f11, getY() + getHeight() - 2, getX() + getWidth() * f12, getY() + getHeight(), RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), f2));
        if (dragging) {
            setting.setFirst(MathUtil.scaleAndRoundFloat(MathUtil.findMiddleValue(setting.getMin() + (setting.getMax() - setting.getMin()) * ((n - getX()) / getWidth()), setting.getMin(), setting.getMax()), 1));
            if (setting.getFirst() > setting.getSecond()) {
                setting.setSecond(setting.getFirst());
            }
        }
        if (active) {
            setting.setSecond(MathUtil.scaleAndRoundFloat(MathUtil.findMiddleValue(setting.getMin() + (setting.getMax() - setting.getMin()) * ((n - getX()) / getWidth()), setting.getMin(), setting.getMax()), 1));
            if (setting.getSecond() < setting.getFirst()) {
                setting.setFirst(setting.getSecond());
            }
        }
    }

    @Override
    public void mouseClicked(double d, double d2, int n) {
        if (n == 0 && isInsideCross((int)d, (int)d2)) {
            double d3 = (getX() + getWidth() * ((setting.getFirst() - setting.getMin()) / (setting.getMax() - setting.getMin()))) - d;
            double d4 = (getX() + getWidth() * ((setting.getSecond() - setting.getMin()) / (setting.getMax() - setting.getMin()))) - d;
            if (d3 < 0.0) {
                d3 *= -1;
            }
            if (d4 < 0.0) {
                d4 *= -1;
            }
            dragging = d4 > d3;
            active = !dragging;
        }
    }

    @Override
    public void mouseReleased(double d, double d2, int n) {
        if (n == 0) {
            this.dragging = false;
            this.active = false;
        }
    }

    boolean isInsideCross(int n, int n2) {
        return n > getX() && n < getX() + getWidth() && n2 > getY() && n2 < getY() + getHeight();
    }

}