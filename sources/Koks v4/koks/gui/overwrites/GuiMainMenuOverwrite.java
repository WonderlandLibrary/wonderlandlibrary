package koks.gui.overwrites;

import god.buddy.aot.BCompiler;
import koks.Koks;
import koks.api.Methods;
import koks.api.manager.mainmenu.MainMenuManager;
import koks.secrets.KonamiCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

public class GuiMainMenuOverwrite implements Methods {

    public final Minecraft mc = Minecraft.getMinecraft();
    public final FontRenderer fontRendererObj = mc.fontRendererObj;

    public GuiMainMenuOverwrite() {
    }

    public void init() {
        Koks.getKoks().particleManager.init();
        MainMenuManager.currentMainMenu.init();
    }


    public void updateButton(GuiButton button) {
        MainMenuManager.currentMainMenu.updateButton(button);
    }



    public void draw(int mouseX, int mouseY, float partialTicks) {
        MainMenuManager.currentMainMenu.draw(mouseX, mouseY, partialTicks);
    }

    public void mouseInput() throws IOException {
        MainMenuManager.currentMainMenu.mouseInput();
    }

    public void mouseRelease() {
        MainMenuManager.currentMainMenu.mouseRelease();
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        MainMenuManager.currentMainMenu.mouseClicked(mouseX,mouseY,mouseButton);
    }

    public void keyTyped(char typedChar, int keyCode) {
        MainMenuManager.currentMainMenu.keyInput(typedChar, keyCode);
        KonamiCode.addKey(keyCode);
    }
}
