package vestige.ui.click.simple;

import net.minecraft.client.gui.GuiScreen;
import vestige.Vestige;
import vestige.api.module.Category;
import vestige.impl.module.render.ClickGuiModule;
import vestige.ui.click.simple.components.CategoryHolder;
import vestige.util.misc.TimerUtil;

import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleClickGUI extends GuiScreen {

    private TimerUtil timer;

    private boolean holdingMouseButton;

    private final ArrayList<CategoryHolder> categories = new ArrayList<>();

    public SimpleClickGUI() {
        timer = new TimerUtil();

        for(Category c : Category.values()) {
            categories.add(new CategoryHolder(this, c, timer));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int categoryX = 40;
        int categoryY = 50;

        for(CategoryHolder c : categories) {
            c.drawScreen(categoryX, categoryY, mouseX, mouseY, partialTicks, holdingMouseButton);
            categoryX += 110;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        int categoryX = 40;
        int categoryY = 50;
        
        for(CategoryHolder c : categories) {
            c.mouseClicked(categoryX, categoryY, mouseX, mouseY, button);
            categoryX += 110;
        }

        holdingMouseButton = true;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen)null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
            onClickGuiDisabled();
        } else {
        	categories.forEach(c -> c.keyTyped(typedChar, keyCode));
        }
    }

    @Override
    public void onGuiClosed() {
        onClickGuiDisabled();
    }

    private void onClickGuiDisabled() {
        Vestige.getInstance().getModuleManager().getModule(ClickGuiModule.class).setEnabled(false);
        holdingMouseButton = false;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        holdingMouseButton = false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
