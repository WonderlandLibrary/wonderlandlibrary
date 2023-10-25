package club.cloverhook.gui.screen.credits;


import club.cloverhook.gui.components.TextField;
import club.cloverhook.gui.screen.MainMenuGui;
import club.cloverhook.utils.Draw;
import com.thealtening.AltService;

import club.cloverhook.Cloverhook;
import club.cloverhook.account.Account;
import club.cloverhook.gui.components.*;
import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class CreditsGui extends GuiScreen {
    private GuiScreen parentScreen;

    public CreditsGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    public void updateScreen() {
        parentScreen.updateScreen();
    }

    public void initGui() {

        this.buttonList.add(new GuiButton(1, this.width / 2 - 40, this.height - 24, 80, 20, "Back"));

        super.initGui();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                Minecraft.getMinecraft().displayGuiScreen(parentScreen);
                break;
        }
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        MainMenuGui.drawBackground();

        drawGradientRect(0, 0, width, height, -1, new Color(80, 80, 80, 120).getRGB());

        Draw.drawRectangle(0, 0, width, 30, new Color(0,0,0,190).getRGB());
        Fonts.verdanaCredits.drawCenteredStringWithShadow("Credits", width / 2, 9, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_RETURN) {
            Minecraft.getMinecraft().displayGuiScreen(parentScreen);
        }


        if (keyCode == Keyboard.KEY_ESCAPE) {
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

}
