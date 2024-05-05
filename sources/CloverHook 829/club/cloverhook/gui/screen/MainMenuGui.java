package club.cloverhook.gui.screen;

import club.cloverhook.gui.screen.account.DirectLoginGui;
import club.cloverhook.gui.screen.credits.CreditsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import club.cloverhook.Cloverhook;
import club.cloverhook.gui.components.IconButton;

import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.DefaultColors;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.font.Fonts;

import java.awt.*;
import java.io.IOException;

public class MainMenuGui extends GuiScreen implements GuiYesNoCallback {
    private static final Logger logger = LogManager.getLogger();

    private final ResourceLocation logoPath = new ResourceLocation("client/gui/logo/128x128.png");
    static ResourceLocation bg = new ResourceLocation("client/gui/main_menu/panorama/panorama_0.png");

    public static void drawBackground() {
        GlStateManager.pushMatrix();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.disableAlpha();
        //this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        //Draw.drawImg(bg, 0,0, sr.getScaledWidth(), sr.getScaledHeight());
        Gui.drawRect(0,0,width,height,new Color(0,0,0, 150).getRGB());
        //drawRect(0, 0, width, height, ColorCreator.createRainbowFromOffset(-6000, 10), new Color(0,0,0, 120).getRGB());
        drawGradientRect(0, 0, width, height, -1, new Color(80,80,80,120).getRGB());
        Fonts.verdana3.drawStringWithShadow("User Status: ", sr.getScaledWidth() -  Fonts.verdana3.getStringWidth("User Status: " + Cloverhook.user_status) - 4, sr.getScaledHeight() - 9, new Color(180,180,180).getRGB());
        Fonts.verdanaN.drawStringWithShadow(Cloverhook.user_status, sr.getScaledWidth() - Fonts.verdanaN.getStringWidth(Cloverhook.user_status)  - 1, sr.getScaledHeight() - 10, 0xFFFFFF);
        Fonts.verdana3.drawStringWithShadow("Build: ", sr.getScaledWidth() - Fonts.verdana3.getStringWidth("Build: " +Cloverhook.client_build) - 4, sr.getScaledHeight() - 21, new Color(180,180,180).getRGB());
        Fonts.verdanaN.drawStringWithShadow(Cloverhook.client_build, sr.getScaledWidth() - Fonts.verdanaN.getStringWidth(Cloverhook.client_build)  - 1, sr.getScaledHeight() - 22, 0xFFFFFF);
        GlStateManager.popMatrix();
    }

    public void initGui() {
        int j = this.height / 4 + 48;

        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, j, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, j + 24, I18n.format("menu.multiplayer", new Object[0])));
        //this.buttonList.add(new GuiButton(999, this.width / 2 - 100, j + 24 * 2, I18n.format("Alt Login", new Object[0])));
        //this.buttonList.add(new GuiButton(899, this.width / 2 - 100, j + 24 * 3, I18n.format("Credits", new Object[0])));
        this.buttonList.add(new GuiButton(999, this.width / 2 - 100, j + 24 * 2, 98, 20, I18n.format("Alt Login", new Object[0])));
        this.buttonList.add(new GuiButton(899, this.width / 2 + 2, j + 24 * 2, 98, 20, I18n.format("Credits", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        //this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, j + 72 + 12));



        this.mc.func_181537_a(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException {

        if (button.id == 999) {
            this.mc.displayGuiScreen(new DirectLoginGui(this));
        }
        if (button.id == 899) {
            this.mc.displayGuiScreen(new CreditsGui(this));
        }

        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }


        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 4) {
            this.mc.shutdown();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground();

        // this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);

        int logoPositionY = this.height / 4;
        int logoImgDimensions = 64;

        GlStateManager.color(1f, 1f, 1f);
        Draw.drawImg(logoPath, this.width / 2 - logoImgDimensions / 2, logoPositionY - 26, logoImgDimensions, logoImgDimensions);

        int spacing = 110;
        if (spacing < 110)
            spacing = 110;
        else if (spacing > 140)
            spacing = 140;

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE)
            return;

        super.keyTyped(typedChar, keyCode);
    }
}
