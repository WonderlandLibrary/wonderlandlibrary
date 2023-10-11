package com.initial.ui;

import net.minecraft.util.*;
import com.initial.font.*;
import net.minecraft.client.resources.*;
import java.util.*;
import net.minecraft.client.gui.*;
import com.initial.login.alt.*;
import java.io.*;
import com.initial.*;
import net.minecraft.client.renderer.*;

public class GuiCustomMainMenu extends GuiMainMenu
{
    private ResourceLocation finalTexture;
    private ResourceLocation texture1;
    private ResourceLocation texture2;
    private ResourceLocation texture3;
    private ResourceLocation texture4;
    MCFontRenderer font;
    
    public GuiCustomMainMenu() {
        this.finalTexture = null;
        this.font = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Desync/fonts/SF-Pro.ttf"), 18.0f, 0), true, true);
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        final String strSSP = I18n.format("SINGLEPLAYER", new Object[0]);
        final String strSMP = I18n.format("MULTIPLAYER", new Object[0]);
        final String strAccounts = "ALT MANAGER";
        final int initHeight = GuiCustomMainMenu.height / 3 + 48;
        final int objHeight = 15;
        final int objWidth = 102;
        final int xMid = GuiCustomMainMenu.width / 2 - objWidth / 2;
        this.buttonList.add(new Button(0, xMid, initHeight + 30, objWidth, objHeight, strSSP));
        this.buttonList.add(new Button(1, xMid, initHeight + 18 - 2 + 30, objWidth, objHeight, strSMP));
        this.buttonList.add(new Button(4, xMid, initHeight + 35 - 3 + 30, objWidth, objHeight, strAccounts));
        try {
            this.texture1 = new ResourceLocation("textures/1.png");
            this.texture2 = new ResourceLocation("textures/2.png");
            this.texture3 = new ResourceLocation("textures/gui/title/background/panorama_0");
            this.texture4 = new ResourceLocation("textures/4.png");
            final List list = new ArrayList();
            list.add(this.texture1);
            list.add(this.texture2);
            list.add(this.texture3);
            list.add(this.texture4);
            final Random random = new Random();
            this.finalTexture = list.get(random.nextInt(list.size()));
        }
        catch (Exception var14) {
            var14.printStackTrace();
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(new GuiAltManager());
                break;
            }
            case 5: {
                this.mc.shutdown();
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.renderSkybox(mouseX, mouseY, partialTicks);
        Astomero.getDiscordRP().update("Main Menu", "");
        final String s = "§7Astomero Client";
        final String s2 = "§7Welcome, " + Astomero.getLoginUser;
        this.font.drawStringWithShadow(s, 2.0f, (float)(GuiCustomMainMenu.height - 10), -1);
        this.font.drawStringWithShadow(s2, (float)(GuiCustomMainMenu.width - this.font.getStringWidth(s2) - 2), (float)(GuiCustomMainMenu.height - 10), -1);
        GlStateManager.pushMatrix();
        for (int i = 0; i < this.buttonList.size(); ++i) {
            final GuiButton g = this.buttonList.get(i);
            g.drawButton(this.mc, mouseX, mouseY);
        }
        GlStateManager.popMatrix();
    }
}
