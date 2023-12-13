package me.nyan.flush.customhud.setting;

import java.util.function.BooleanSupplier;

public abstract class Setting {
    protected final String name;
    private final BooleanSupplier supplier;

    public Setting(String name, BooleanSupplier supplier) {
        this.name = name;
        this.supplier = supplier == null ? () -> true : supplier;
    }

    public String getName() {
        return name;
    }

    public abstract void draw(float x, float y, int mouseX, int mouseY, float partialTicks);

    public void init() {

    }

    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {

    }

    public void mouseReleased(float x, float y, int mouseX, int mouseY, int button) {

    }

    public boolean keyTyped(char typedChar, int keyCode) {
        return false;
    }

    public abstract float getWidth();
    public abstract float getHeight();

    public float getFullHeight() {
        return getHeight();
    }

    public boolean shouldShow() {
        return supplier.getAsBoolean();
    }
}