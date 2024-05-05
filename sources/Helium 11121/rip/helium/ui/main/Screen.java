package rip.helium.ui.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import rip.helium.Helium;
import rip.helium.utils.GuiUtil;

import java.io.IOException;

public class Screen extends GuiScreen {
    public final Minecraft game;
    public Interface theInterface;
    public boolean firstKeyPressed;

    public Screen() {
        this.game = Minecraft.getMinecraft();
        this.theInterface = new Interface(this);
        this.firstKeyPressed = false;
    }

    @Override
    public void initGui() {
        this.firstKeyPressed = false;
        this.theInterface.initializeInterface();
        super.initGui();
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.theInterface.drawInterface(mouseX, mouseY);
        KeyBinding[] array;
        for (int length = (array = new KeyBinding[]{Minecraft.getMinecraft().gameSettings.keyBindForward, Minecraft.getMinecraft().gameSettings.keyBindBack, Minecraft.getMinecraft().gameSettings.keyBindLeft, Minecraft.getMinecraft().gameSettings.keyBindRight, Minecraft.getMinecraft().gameSettings.keyBindJump, Minecraft.getMinecraft().gameSettings.keyBindSprint}).length, i = 0; i < length; ++i) {
            final KeyBinding keyBinding = array[i];
            try {
                KeyBinding.setKeyBindState(keyBinding.getKeyCode(), Keyboard.isKeyDown(keyBinding.getKeyCode()));
            } catch (Exception e) {

            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.theInterface.mouseButtonClicked(mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.theInterface.mouseButtonReleased(state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            if (wheel > 1) {
                wheel = -1;
            }
            if (wheel < -1) {
                wheel = 1;
            }
            this.theInterface.mouseScrolled(wheel);
        }
        super.handleMouseInput();
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!this.theInterface.keyTyped(typedChar, keyCode) && (keyCode == Helium.instance.cheatManager.getCheatRegistry().get("Interface").getBind() || keyCode == 1)) {
            this.closeInterface();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void closeInterface() {
        if (mc.entityRenderer.theShaderGroup != null) {
            mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            mc.entityRenderer.theShaderGroup = null;
        }
        this.theInterface.setClosing(true);
        GuiUtil.closeScreenAndReturn();
    }

    public ScaledResolution getResolution() {
        return new ScaledResolution(this.game);
    }
}
