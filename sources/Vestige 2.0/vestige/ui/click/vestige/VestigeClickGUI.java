package vestige.ui.click.vestige;

import net.minecraft.client.gui.GuiScreen;
import vestige.Vestige;
import vestige.api.module.Category;
import vestige.impl.module.render.ClickGuiModule;
import vestige.ui.click.vestige.components.CategoryHolder;
import vestige.util.misc.TimerUtil;

import org.lwjgl.input.Keyboard;

import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;

public class VestigeClickGUI extends GuiScreen {

    private TimerUtil timer;
    
    @Getter
    private boolean holdingMouseButton;

    private final ArrayList<CategoryHolder> categories = new ArrayList<>();

    public VestigeClickGUI() {
        timer = new TimerUtil();
        
        int categoryX = 40;
        int categoryY = 50;
        
        for(Category c : Category.values()) {
            categories.add(new CategoryHolder(this, c, timer, categoryX, categoryY));
            categoryX += 110;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
       categories.forEach(c -> c.drawScreen(mouseX, mouseY, partialTicks, holdingMouseButton));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
    	categories.forEach(c -> c.mouseClicked(mouseX, mouseY, button));
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
