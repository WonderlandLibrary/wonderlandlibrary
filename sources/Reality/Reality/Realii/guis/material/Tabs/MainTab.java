package Reality.Realii.guis.material.Tabs;

import Reality.Realii.Client;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.guis.material.Main;
import Reality.Realii.guis.material.Tab;

import java.awt.*;

public class MainTab extends Tab {
    public MainTab() {
        name = "Info";
    }

    @Override
    public void render(float mouseX, float mouseY) {
        super.render(mouseX, mouseY);
        FontLoaders.arial24.drawString("Welcome!", Main.windowX + Main.animListX + 155, Main.windowY + 100, new Color(255, 255, 255).getRGB());
        FontLoaders.arial22.drawString("Current Build Reality Beta 1.4", Main.windowX + Main.animListX + 110, Main.windowY + 120, new Color(255, 255, 255).getRGB());
       
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY) {
        super.mouseClicked(mouseX, mouseY);
    }
}
