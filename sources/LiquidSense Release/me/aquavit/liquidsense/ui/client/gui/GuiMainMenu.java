package me.aquavit.liquidsense.ui.client.gui;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.ui.client.gui.elements.GuiButtonElement;
import me.aquavit.liquidsense.ui.font.Fonts;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

import java.awt.*;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
    public void initGui() {
        this.addbutton();
        super.initGui();
    }

    public void addbutton(){
        int defaultHeight = height / 2 - 8;

        buttonList.add(new GuiButtonElement(1, width / 2 - 72, defaultHeight, 140, 20, I18n.format("menu.singleplayer")));
        buttonList.add(new GuiButtonElement(2, width / 2 - 72, defaultHeight + 24, 140, 20, I18n.format("menu.multiplayer")));
        buttonList.add(new GuiButtonElement(3, width / 2 - 72, defaultHeight + 48, 140, 20, I18n.format("Account Manager")));
        buttonList.add(new GuiButtonElement(4, width / 2 - 72, defaultHeight + 72, 140, 20, I18n.format("Background")));
        buttonList.add(new GuiButtonElement(0, width / 2 - 72, defaultHeight + 96, 140, 20, I18n.format("menu.options")));
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        //427 - 461
        //RenderUtils.drawImage(new ResourceLocation("liquidsense/LiquidSenseGirl.png"), -10, height - 220, 213, 230);
        Fonts.font20.drawStringWithShadow("Welcome, AquaVit", width - Fonts.font20.getStringWidth("Welcome, AquaVit") - 4f, height - 12f, new Color(255, 255, 255, 200).getRGB());
        Fonts.font20.drawStringWithShadow("LiquidSense Reborn!", 4f, height - 12f, new Color(255, 255, 255, 200).getRGB());
        //Fonts.logo.drawCenteredString(LiquidSense.CLIENT_NAME, this.width / 2F - 1, height / 2F - 45, new Color(236, 87, 202, 200).getRGB(), true);
        Fonts.logo.drawCenteredString(LiquidSense.CLIENT_NAME, this.width / 2F + 1, height / 2F - 49, new Color(91, 162, 234, 200).getRGB(), true);
        Fonts.logo.drawCenteredString(LiquidSense.CLIENT_NAME, this.width / 2F, height / 2F - 50, new Color(255, 255, 255, 255).getRGB(), true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) mc.displayGuiScreen(new GuiSelectWorld(this));
        if (button.id == 2) mc.displayGuiScreen(new GuiMultiplayer(this));
        if (button.id == 3) mc.displayGuiScreen(new GuiAltManager(this));
        if (button.id == 4) mc.displayGuiScreen(new GuiBackground(this));
        if (button.id == 0) mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));

        //if (p_actionPerformed_1_.id == 777) mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
        if (button.id == 666) mc.shutdown();
        if (button.id == 11) mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        if (button.id == 12) {
            ISaveFormat isaveformat = mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null) {
                GuiYesNo guiyesno = GuiSelectWorld.makeDeleteWorldYesNo(this, worldinfo.getWorldName(), 12);
                mc.displayGuiScreen(guiyesno);
            }
        }
    }

    protected void keyTyped(final char typedChar, final int keyCode) {
    }
}

