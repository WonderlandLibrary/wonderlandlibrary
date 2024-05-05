package club.cloverhook.ui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import club.cloverhook.Cloverhook;
import club.cloverhook.utils.GuiUtil;

import java.io.IOException;

/**
 * @author antja03
 */
public class Screen extends GuiScreen {

    public final Minecraft game = Minecraft.getMinecraft();
    public Interface theInterface = new Interface(this);
    public boolean firstKeyPressed = false;

    @Override public void initGui() {
        firstKeyPressed = false;
        theInterface.initializeInterface();
        super.initGui();
    }

    @Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        theInterface.drawInterface(mouseX, mouseY);

        for (KeyBinding keyBinding : new KeyBinding[]{
                Minecraft.getMinecraft().gameSettings.keyBindForward, Minecraft.getMinecraft().gameSettings.keyBindBack,
                Minecraft.getMinecraft().gameSettings.keyBindLeft, Minecraft.getMinecraft().gameSettings.keyBindRight,
                Minecraft.getMinecraft().gameSettings.keyBindJump, Minecraft.getMinecraft().gameSettings.keyBindSprint})
        {
            KeyBinding.setKeyBindState(keyBinding.getKeyCode(), Keyboard.isKeyDown(keyBinding.getKeyCode()));
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        theInterface.mouseButtonClicked(mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override protected void mouseReleased(int mouseX, int mouseY, int state) {
        theInterface.mouseButtonReleased(state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override public void handleMouseInput() throws IOException {
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            if (wheel > 1) {
                wheel = -1;
            }
            if (wheel < -1) {
                wheel = 1;
            }
            theInterface.mouseScrolled(wheel);
        }
        super.handleMouseInput();
    }

    @Override protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!theInterface.keyTyped(typedChar, keyCode)) {
            if (keyCode == Cloverhook.instance.cheatManager.getCheatRegistry().get("Interface").getBind() || keyCode == Keyboard.KEY_ESCAPE) {
                closeInterface();
            }
        }
    }

    @Override public boolean doesGuiPauseGame() {
        return false;
    }

    public void closeInterface() {
        theInterface.setClosing(true);
        GuiUtil.closeScreenAndReturn();
    }

    public ScaledResolution getResolution() {
        return new ScaledResolution(game);
    }
}
