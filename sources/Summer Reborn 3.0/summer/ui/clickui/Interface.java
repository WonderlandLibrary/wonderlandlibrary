package summer.ui.clickui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import summer.base.manager.Selection;
import summer.ui.clickui.panel.Panel;
import summer.ui.clickui.panel.panels.CheatTypePanel;
import summer.ui.clickuiutils.Manager;

/**
 * @author : AmirCC
 * @created : 1:17 PM, 10/27/2020, Tuesday
 **/
public class Interface extends GuiScreen implements Manager {

    private ArrayList<Panel> panels;

    @Override
    public void setup() {
        panels = new ArrayList<>();
        int offsetX = 20;
        for (Selection value : Selection.values()) {
            panels.add(new CheatTypePanel(value, offsetX, 20));
            offsetX += 130;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.panels.forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.panels.forEach(panel -> panel.mouseClicked(mouseX, mouseY, mouseButton));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.panels.forEach(panel -> panel.mouseReleased(mouseX, mouseY, state));
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        this.panels.forEach(panel -> panel.handleMouseInput());
        super.handleMouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.panels.forEach(panel -> panel.keyTyped(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }
}
