package de.tired.base.guis;

import de.tired.base.interfaces.IHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class CustomScreen extends GuiScreen implements IHook {

    private final boolean isCheckForAnimation;

    public boolean pressedEscape;
    public boolean isAnimationDone;

    public CustomScreen(boolean checkForAnimation) {
        this.isCheckForAnimation = checkForAnimation;
    }


    @Override
    public void initGui() {
        this.pressedEscape = false;
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isAnimationDone) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.pressedEscape = keyCode == Keyboard.KEY_ESCAPE && !pressedEscape;
        if (isCheckForAnimation) {
            if (isAnimationDone) {
                super.keyTyped(typedChar, keyCode);
            }
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    public void setAnimationDone(boolean animationDone) {
        isAnimationDone = animationDone;
    }
}