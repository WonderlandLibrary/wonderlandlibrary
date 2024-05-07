package com.masterof13fps.manager.clickguimanager.components;

/**
 * @author sendQueue <Vinii>
 * <p>
 * Further info at Vinii.de or github@vinii.de, file created at
 * 11.11.2020. Use is only authorized if given credit!
 */
public interface GuiComponent {

    void render(int posX, int posY, int width, int mouseX, int mouseY);

    void mouseClicked(int mouseX, int mouseY, int mouseButton);

    void keyTyped(int keyCode, char typedChar);

    int getWidth();

    int getHeight();

}
