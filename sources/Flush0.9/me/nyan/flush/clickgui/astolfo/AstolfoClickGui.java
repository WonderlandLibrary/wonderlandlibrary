package me.nyan.flush.clickgui.astolfo;

import me.nyan.flush.clickgui.ClickGui;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.render.ModuleClickGui;

import java.io.IOException;
import java.util.ArrayList;

public class AstolfoClickGui extends ClickGui {
    private final ArrayList<AstolfoPanel> panels = new ArrayList<>();

    public AstolfoClickGui() {
        int x = 0;
        for (Module.Category category : Module.Category.values()) {
            panels.add(new AstolfoPanel(category, 20 + 140 * x, 20));
            x++;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (AstolfoPanel panel : panels) {
            panel.draw(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        for (AstolfoPanel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, button);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (AstolfoPanel panel : panels) {
            panel.mouseReleased();
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void keyTyped(char typedChar, int keyCode) {
        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuiClosed() {
        moduleManager.getModule(ModuleClickGui.class).disable();

        for (AstolfoPanel panel : panels) {
            panel.dragging = false;
        }
    }
}