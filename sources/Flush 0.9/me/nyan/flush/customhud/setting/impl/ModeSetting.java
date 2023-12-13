package me.nyan.flush.customhud.setting.impl;

import me.nyan.flush.customhud.setting.Setting;
import me.nyan.flush.ui.elements.ModeSwitch;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

public class ModeSetting extends Setting {
    private final ModeSwitch modeSwitch;

    public ModeSetting(String name, String value, String... options) {
        this(name, null, value, options);
    }

    public ModeSetting(String name, BooleanSupplier supplier, String value, String... options) {
        this(name, value, new ArrayList<>(Arrays.asList(options)), supplier);
    }

    public ModeSetting(String name, String value, ArrayList<String> options) {
        this(name, value, options, null);
    }

    public ModeSetting(String name, String value, ArrayList<String> options, BooleanSupplier supplier) {
        super(name, supplier);
        modeSwitch = new ModeSwitch(name, 0, 0, getWidth(), getHeight(), value, 0xFFBB86FC, options);
    }

    public void setValue(String value) {
        modeSwitch.setValue(value);
    }

    public String getValue() {
        return modeSwitch.getValue();
    }

    public List<String> getOptions() {
        return modeSwitch.getOptions();
    }

    public boolean is(String value) {
        return modeSwitch.is(value);
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        modeSwitch.drawScreen(mouseX - (int) x, mouseY - (int) y, partialTicks);
        GlStateManager.popMatrix();
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        modeSwitch.mouseClicked(mouseX - (int) x, mouseY - (int) y, button);
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

    @Override
    public float getFullHeight() {
        return modeSwitch.getFullHeight();
    }
}
