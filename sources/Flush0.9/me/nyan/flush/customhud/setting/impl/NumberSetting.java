package me.nyan.flush.customhud.setting.impl;

import me.nyan.flush.customhud.setting.Setting;
import me.nyan.flush.ui.elements.Slider;
import net.minecraft.client.renderer.GlStateManager;

import java.util.function.BooleanSupplier;

public class NumberSetting extends Setting {
    private final Slider slider;

    public NumberSetting(String name, double value, double min, double max, double stepValue) {
        this(name, value, min, max, stepValue, null);
    }

    public NumberSetting(String name, double value, double min, double max, double stepValue, BooleanSupplier supplier) {
        super(name, supplier);
        slider = new Slider(name, 0, 0, getWidth(), getHeight(), min, max, value, stepValue, 0xFFBB86FC);
    }

    public NumberSetting(String name, double value, double min, double max) {
        this(name, value, min, max, 0.1);
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        slider.drawScreen(mouseX - (int) x, mouseY - (int) y);
        GlStateManager.popMatrix();
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        slider.mouseClicked(mouseX - (int) x, mouseY - (int) y, button);
    }

    @Override
    public void mouseReleased(float x, float y, int mouseX, int mouseY, int button) {
        slider.mouseReleased();
    }

    public void setValue(double value) {
        slider.setValue(value);
    }

    public double getValue() {
        return slider.getValue();
    }

    public float getValueFloat() {
        return (float) getValue();
    }

    public int getValueInt() {
        return (int) getValue();
    }

    public double getMax() {
        return slider.getMax();
    }

    public double getMin() {
        return slider.getMin();
    }

    @Override
    public float getWidth() {
        return 120;
    }

    @Override
    public float getHeight() {
        return 16;
    }

    @Override
    public String toString() {
        String s = String.valueOf(getValue());
        return getValue() % 1D == 0 ? s.substring(0, s.length() - 2) : s;
    }
}