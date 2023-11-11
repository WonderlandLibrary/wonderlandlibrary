package me.nyan.flush.clickgui.sigma.component;

public abstract class Component {
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
}
