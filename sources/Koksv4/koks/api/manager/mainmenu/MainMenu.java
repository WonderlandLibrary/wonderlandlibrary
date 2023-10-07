package koks.api.manager.mainmenu;

import koks.api.Methods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public abstract class MainMenu implements Methods {

    public abstract void draw(int mouseX, int mouseY, float partialTicks);

    public abstract void init();

    public abstract void mouseInput();
    public abstract void mouseRelease();
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void keyInput(char typedChar, int keyCode);

    public abstract void updateButton(GuiButton button);

    public void addButtons(GuiMainMenu screen) {
        screen.addButtons();
    }
}
