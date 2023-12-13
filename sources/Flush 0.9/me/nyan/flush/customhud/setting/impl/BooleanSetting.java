package me.nyan.flush.customhud.setting.impl;

import me.nyan.flush.customhud.setting.Setting;
import me.nyan.flush.ui.elements.CheckBox;
import net.minecraft.client.renderer.GlStateManager;

import java.util.function.BooleanSupplier;

public class BooleanSetting extends Setting {
    private final CheckBox checkBox;

    public BooleanSetting(String name, boolean value) {
        this(name, value, null);
    }

    public BooleanSetting(String name, boolean value, BooleanSupplier supplier) {
        super(name, supplier);
        checkBox = new CheckBox(name, 0, 0, getWidth(), getHeight(), value, 0xFFBB86FC);
    }

    public boolean getValue() {
        return checkBox.getValue();
    }

    public void setValue(boolean value) {
        checkBox.setValue(value);
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        checkBox.drawScreen(mouseX - (int) x, mouseY - (int) y, partialTicks);
        GlStateManager.popMatrix();
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        checkBox.mouseClicked(mouseX - (int) x, mouseY - (int) y, button);
    }

    @Override
    public void mouseReleased(float x, float y, int mouseX, int mouseY, int button) {
        super.mouseReleased(x, y, mouseX, mouseY, button);
    }

    @Override
    public float getWidth() {
        return 120;
    }

    @Override
    public float getHeight() {
        return 16;
    }
}
