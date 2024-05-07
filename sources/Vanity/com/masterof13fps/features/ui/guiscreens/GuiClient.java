package com.masterof13fps.features.ui.guiscreens;

import com.masterof13fps.Client;
import com.masterof13fps.features.ui.guiscreens.altmanager.GuiAltManager;
import com.masterof13fps.manager.fontmanager.FontManager;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class GuiClient extends GuiScreen {
    public GuiScreen parent;

    FontManager fM = Client.main().fontMgr();

    private GuiScreen parentScreen;

    public GuiClient(GuiScreen parentScreen) {
        parent = parentScreen;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0, width / 2 - 120, height / 2 - 60, 120, 20, "AltManager"));
        buttonList.add(new GuiButton(4, width / 2 + 10, height / 2 - 60, 120, 20, "Credits"));
        buttonList.add(new GuiButton(2, width / 2 - 120, height / 2, 120, 20, "Changelog"));
        buttonList.add(new GuiButton(3, width / 2 + 10, height / 2, 120, 20, "Reset Client"));
        buttonList.add(new GuiButton(1, width / 2 - 120, height / 2 - 30, 250, 20, "Zurück"));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            mc.displayGuiScreen(parentScreen);
        }
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                mc.displayGuiScreen(parent);
                break;
            }
            case 0: {
                mc.displayGuiScreen(new GuiAltManager(this));
                break;
            }
            case 4: {
                mc.displayGuiScreen(new GuiCredits(this));
                break;
            }
            case 2: {
                mc.displayGuiScreen(new GuiChangelog(this));
                break;
            }
            case 3: {
                mc.displayGuiScreen(new GuiResetClient(this));
                break;
            }
        }
    }

    public void drawScreen(int posX, int posY, float f) {
        ScaledResolution sr = new ScaledResolution(mc);

        mc.getTextureManager().bindTexture(new ResourceLocation(Client.main().getClientBackground()));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, sr.width(), sr.height(),
                width, height, sr.width(), sr.height());

        RenderUtils.drawRoundedRect(width / 2 - 130, height / 2 - 70, 265, 95, 10, new Color(45, 45, 45).getRGB());

        super.drawScreen(posX, posY, f);
    }
}