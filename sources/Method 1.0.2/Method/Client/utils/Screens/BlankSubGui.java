package Method.Client.utils.Screens;

import Method.Client.utils.visual.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class BlankSubGui extends GuiScreen {
    public SubGui subGui;

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (subGui != null) {
            int mouseX = Mouse.getEventX() * width / mc.displayWidth;
            int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
            subGui.handleMouseInput(mouseX, mouseY);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        if (subGui != null) {
            subGui.draw(mouseX, mouseY, partialTicks);
            subGui.updatePosition(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        mouseX = (int) (mouseX / RenderUtils.simpleScale(false));
        mouseY = (int) (mouseY / RenderUtils.simpleScale(true));
        if (subGui != null) {
            subGui.mouseClicked(mouseX, mouseY, mouseButton);
        }

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (subGui != null) {
            subGui.keyTyped(typedChar, keyCode);
            if (keyCode == 1)
                subGui = null;
        }

    }

    @Override
    public void updateScreen() {
        if (subGui != null) {
            subGui.updateScreen();
        }
        super.updateScreen();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (subGui != null) {
            subGui.mouseReleased();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
