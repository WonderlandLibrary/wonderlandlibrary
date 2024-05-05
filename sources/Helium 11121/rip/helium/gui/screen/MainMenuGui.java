package rip.helium.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import rip.helium.buttons.BigUIButton;
import rip.helium.buttons.SmallUIButton;
import rip.helium.gui.screen.account.DirectLoginGui;
import rip.helium.gui.screen.credits.CreditsGui;
import rip.helium.utils.ColorCreator;
import rip.helium.utils.Draw;
import rip.helium.utils.font.Fonts;

import java.awt.*;
import java.io.IOException;

public class MainMenuGui extends GuiScreen implements GuiYesNoCallback {
    private static final Logger logger;
    static ResourceLocation bg;

    static {
        logger = LogManager.getLogger();
        MainMenuGui.bg = new ResourceLocation("client/gui/main_menu/panorama/panorama_0.png");
    }

    private final ResourceLocation logoPath;
    private int topButtonHeight;

    public MainMenuGui() {
        this.logoPath = new ResourceLocation("client/gui/logo/title.png");
    }

    public static void drawBackground() {
        GlStateManager.pushMatrix();
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();
        Gui.drawRect(0, 0, MainMenuGui.width, MainMenuGui.height, new Color(164, 79, 1, 150).getRGB());
        Gui.drawRect(0, 0, MainMenuGui.width, MainMenuGui.height, ColorCreator.createRainbowFromOffset(-6000, 10));
        Gui.drawGradientRect(0, 0, MainMenuGui.width, MainMenuGui.height, -1, new Color(184, 97, 26, 120).getRGB());
        Fonts.verdana3.drawStringWithShadow("User Status: ", (float) (ScaledResolution.getScaledWidth() - Fonts.verdana3.getStringWidth("User Status: Free") - 4), (float) (ScaledResolution.getScaledHeight() - 9), new Color(180, 180, 180).getRGB());
        Fonts.verdanaN.drawStringWithShadow("Free", (float) (ScaledResolution.getScaledWidth() - Fonts.verdanaN.getStringWidth("Free") - 1), (float) (ScaledResolution.getScaledHeight() - 10), 16777215);
        Fonts.verdana3.drawStringWithShadow("Build: ", (float) (ScaledResolution.getScaledWidth() - Fonts.verdana3.getStringWidth("Build: 1") - 4), (float) (ScaledResolution.getScaledHeight() - 21), new Color(180, 180, 180).getRGB());
        Fonts.verdanaN.drawStringWithShadow("1", (float) (ScaledResolution.getScaledWidth() - Fonts.verdanaN.getStringWidth("1") - 1), (float) (ScaledResolution.getScaledHeight() - 22), 16777215);
        GlStateManager.popMatrix();
    }


    @Override
    public void initGui() {
        //Fonts.verdana3.drawStringWithShadow("User Status: ", (float)(sr.getScaledWidth() - Fonts.verdana3.getStringWidth("User Status: Free") - 4), (float)(sr.getScaledHeight() - 9), new Color(180, 180, 180).getRGB());
        final int j = MainMenuGui.height / 4 + 48;

        this.buttonList.add(new BigUIButton(1, MainMenuGui.width / 2 - 100, j, I18n.format("menu.singleplayer")));
        this.buttonList.add(new BigUIButton(2, MainMenuGui.width / 2 - 100, j + 40, I18n.format("menu.multiplayer")));
        this.buttonList.add(new BigUIButton(999, MainMenuGui.width / 2 - 100, j + 80, I18n.format("Alt Login")));
        this.buttonList.add(new SmallUIButton(0, MainMenuGui.width / 2 - 160, j + 120, I18n.format("Options")));
        this.buttonList.add(new SmallUIButton(69, MainMenuGui.width / 2 - -100, j + 120, I18n.format("Credits")));
        this.buttonList.add(new SmallUIButton(4, MainMenuGui.width / 2 - 40, j + 120, I18n.format("Quit")));
        this.mc.func_181537_a(false);
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 999) {
            this.mc.displayGuiScreen(new DirectLoginGui(this));
            //clip.stop();
        }
        if (button.id == 899) {
            this.mc.displayGuiScreen(new CreditsGui(this));
            //clip.stop();
        }
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            //clip.stop();
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
            //clip.stop();
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
            //clip.stop();
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 69) {
            this.mc.displayGuiScreen(new CreditsGui(this));
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {


        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Fonts.verdana3.drawStringWithShadow("User Status: ", (float) (ScaledResolution.getScaledWidth() - Fonts.verdana3.getStringWidth("User Status: Beta") - 4), (float) (ScaledResolution.getScaledHeight() - 9), new Color(180, 180, 180).getRGB());

       // Use this for regular version
        Draw.drawImg(new ResourceLocation("client/Background.jpg"), 0.0, 0.0, width, height);

        //Use this for 18+ version
        //Draw.drawImg(new ResourceLocation("client/g.jpg"), 0.0, 0.0, width, height);
        GlStateManager.popMatrix();
        final int logoPositionY = this.topButtonHeight - 30;
        Draw.drawImg(new ResourceLocation("client/gui/logo/64x64.png"), width / 2 - 32, logoPositionY - 64, 64.0, 64.0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        int y = 0;
        int color = 0;
        GL11.glPushMatrix();
        GL11.glScaled(5, 5, 5);
        color = ColorCreator.createRainbowFromOffset2(-6000, y * 35);

        //Use this for regular version
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Helium", width / 10 - (mc.fontRendererObj.getStringWidth("Helium")) / 2, height / 22, color);

        //Use this for 18+ Edition
       // Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Hentai", width / 10 - (mc.fontRendererObj.getStringWidth("Helium")) / 2.0f, height / 22, color);

        GL11.glPopMatrix();
    }


    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
