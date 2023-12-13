package me.nyan.flush.clickgui.flush.component;

import me.nyan.flush.clickgui.flush.FlushPanel;

public abstract class Component {
    public abstract void draw(float x, float y, int mouseX, int mouseY, float partialTicks);

    public void init() {
        
    }

    public void update() {

    }

    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        
    }

    public void mouseReleased(float x, float y, int mouseX, int mouseY, int button) {
        
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        return false;
    }

    public final float getWidth() {
        return FlushPanel.WIDTH;
    }

    public abstract float getHeight();

    public float getFullHeight() {
        return getHeight();
    }

    public boolean show() {
        return true;
    }
}
