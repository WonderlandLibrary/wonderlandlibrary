package markgg.ui.click.astolfo;

import markgg.RazeClient;
import markgg.modules.Module;
import markgg.modules.impl.render.ClickGUI;
import markgg.modules.impl.render.HUD2;
import markgg.ui.click.astolfo.buttons.Button;
import markgg.ui.click.astolfo.buttons.ModuleButton;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class AstolfoGui extends GuiScreen {

    public ArrayList<CategoryPanel> categoryPanels = new ArrayList<>();

    public AstolfoGui() {
        int count = 4;
        for(Module.Category category : Module.Category.values()) {
            categoryPanels.add(new CategoryPanel(count, 4, 100, 18, category, Module.getCategoryColor(category)));
            count += 120;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (CategoryPanel categoryPanel : categoryPanels){
            categoryPanel.draw(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(CategoryPanel categoryPanel : categoryPanels){
            categoryPanel.action(mouseX, mouseY, true, mouseButton);
            if(categoryPanel.category.expanded) {
                for(ModuleButton moduleButton : categoryPanel.moduleButtons) {
                    moduleButton.action(mouseX, mouseY, true, mouseButton);
                    if(moduleButton.module.expanded) {
                        for(Button panel : moduleButton.buttons) {
                            panel.action(mouseX, mouseY, true, mouseButton);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(CategoryPanel categoryPanel : categoryPanels){
            categoryPanel.action(mouseX, mouseY, false, state);
            if(categoryPanel.category.expanded) {
                for(ModuleButton moduleButton : categoryPanel.moduleButtons) {
                    moduleButton.action(mouseX, mouseY, false, state);
                    if(moduleButton.module.expanded) {
                        for(Button panel : moduleButton.buttons) {
                            panel.action(mouseX, mouseY, false, state);
                        }
                    }
                }
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (CategoryPanel catPanel : categoryPanels) {
            if (catPanel.category.expanded) {
                for (ModuleButton modPan : catPanel.moduleButtons) {
                    modPan.key(typedChar, keyCode);
                }

            }
        }

        if(keyCode == 1) {
            mc.currentScreen = null;
            this.mc.setIngameFocus();
        }
    }
}
