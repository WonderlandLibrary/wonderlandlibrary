package rip.helium.gui.screen.credits;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import rip.helium.buttons.UIButton;
import rip.helium.gui.screen.MainMenuGui;
import rip.helium.utils.ColorCreator;
import rip.helium.utils.Draw;
import rip.helium.utils.font.Fonts;

import java.awt.*;
import java.io.IOException;

public class CreditsGui extends GuiScreen implements GuiYesNoCallback
{
    private GuiScreen parentScreen;

    public CreditsGui(final GuiScreen parent) {
        this.parentScreen = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();
        Gui.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 150).getRGB());
        this.drawGradientRect(0, 0, this.width, this.height, new Color(0, 0, 0).getRGB(), new Color(0, 0, 0, 120).getRGB());
        this.drawGradientRect(0, 0, this.width, this.height, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 120).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Draw.drawImg(new ResourceLocation("client/Background.jpg"), 1.0, 1.0, this.width, this.height);
        final int logoPositionY = this.height / 2 - 130;
        int y = 0;
        int color = 0;
        color = ColorCreator.createRainbowFromOffset2(-6000, y * 35);
        Fonts.bf28.drawStringWithShadow("Credits: ", 15.0f, 10.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Vaziak, Spec, and AnthonyJ:", 15.0f, 30.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Client Base", 15.0f, 40.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Dort:", 15.0f, 70.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Viper and Ghostly disablers", 15.0f, 80.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("oHare:", 15.0f, 110.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Teleport Command and some visuals", 15.0f, 120.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Kansio:", 15.0f, 150.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Cleaned Code and made the ClickGUI", 15.0f, 160.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Pulse:", 15.0f, 190.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Made Nametags, Tracers, added a TabGUI, and did some other modules ", 15.0f, 200.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Auth:", 15.0f, 230.0f, new Color(255, 64, 37).getRGB());
        Fonts.bf20.drawStringWithShadow("Gave me some of the block animations like astro and in", 15.0f, 240.0f, new Color(255, 64, 37).getRGB());
        //Start beta testers
        Fonts.bf28.drawStringWithShadow("Beta Testers: ", 860.0f, 10.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("xDaddyIssues", 880.0f, 30.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("BanMeBruhh", 885.0f, 40.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("5ymb0ls", 906.0f, 50.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("gunlean", 909.0f, 60.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("vReach", 911.0f, 70.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("sedate", 913.0f, 80.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("Tissue", 914.0f, 90.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("tomas", 916.0f, 100.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("Lawin", 917.0f, 110.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("6qoh", 922.0f, 120.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("Jozz", 924.0f, 130.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf20.drawStringWithShadow("vrx", 930.0f, 140.0f, new Color(190, 37, 255).getRGB());
        Fonts.bf28.drawStringWithShadow("I love these fuckers ^", 800.0f, 160.0f, color);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
        }
        super.keyTyped(typedChar, keyCode);
    }
}