package io.github.nevalackin.client.ui.nl;

import io.github.nevalackin.client.ui.nl.components.RootComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public final class GuiNLUIScreen extends GuiScreen {

    private final RootComponent rootComponent;

    public GuiNLUIScreen() {
        this.mc = Minecraft.getMinecraft();
        this.rootComponent = new RootComponent(new ScaledResolution(this.mc));
    }

    @Override
    public void onGuiClosed() {
        this.rootComponent.resetAnimationState();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        glDisable(GL_ALPHA_TEST);
        this.rootComponent.onDraw(new ScaledResolution(mc), mouseX, mouseY);
        glEnable(GL_ALPHA_TEST);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.rootComponent.onMouseClick(mouseX, mouseY, mouseButton);
    }

    private static boolean ignoreInput;

    @Override
    public void initGui() {
        ignoreInput = true;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (ignoreInput) {
            ignoreInput = false;
            return;
        }

        switch (keyCode) {
            case Keyboard.KEY_ESCAPE:
            case Keyboard.KEY_INSERT:
            case Keyboard.KEY_RSHIFT:
                this.mc.displayGuiScreen(null);
                return;
        }

        this.rootComponent.onKeyPress(keyCode);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        this.rootComponent.onMouseRelease(state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

