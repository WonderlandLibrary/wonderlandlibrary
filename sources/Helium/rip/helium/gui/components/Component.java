package rip.helium.gui.components;

/**
 * @author antja03
 */
public abstract class Component {

    protected int posX;
    protected int posY;

    public Component(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public abstract void drawComponent(int mouseX, int mouseY);

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public void mouseScrolled(int direction) {
    }

    public void onKeyPressed(char typedChar, int keyCode) {
    }

}
