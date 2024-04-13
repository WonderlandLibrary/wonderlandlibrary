package wtf.dawn.ui;

import wtf.dawn.ui.components.BasicComponent;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class Screen extends GuiScreen {

    public ArrayList<BasicComponent> components = new ArrayList<BasicComponent>();

    public void render(int mouseX, int mouseY) {
        components.forEach(basicComponent -> {
            basicComponent.renderComponent(mouseX, mouseY);
        });
    }
    public void init() {}
    public void componentAction(int componentId) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        render(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();

        init();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        components.forEach(basicComponent -> {
            basicComponent.renderComponent(mouseX, mouseY);
        });
    }
}
