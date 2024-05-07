package com.masterof13fps.features.ui.guiscreens.altmanager;

import com.masterof13fps.Client;
import com.masterof13fps.Wrapper;
import com.masterof13fps.utils.render.Colors;
import com.masterof13fps.utils.render.RenderUtils;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GuiProxy extends GuiScreen {
    public GuiScreen parent;

    public static ArrayList altList = new ArrayList();
    public static ArrayList guiSlotList = new ArrayList();
    public static File altFile;

    private GuiScreen parentScreen;
    private GuiTextField sessionID;

    public File getAltFile() {
        return altFile;
    }

    public GuiProxy(GuiScreen parentScreen) {
        parent = parentScreen;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0, width / 2 - 100, height - 185, 200, 20, "Proxy verwenden"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height - 160, 200, 20, I18n.format("gui.cancel")));
        sessionID = new GuiTextField(0, fontRendererObj, width / 2 - 100, height - 250, 200, 20);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch(button.id){
            case 1: {
                mc.displayGuiScreen(parent);
                break;
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        sessionID.textboxKeyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        sessionID.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int posX, int posY, float f) {
        drawString(mc.fontRendererObj, "", width / 2 - 100, 79, 10526880);

        ScaledResolution sr = new ScaledResolution(Wrapper.mc);
        if (Keyboard.isKeyDown(1)) {
            mc.displayGuiScreen(parent);
        }

        mc.getTextureManager().bindTexture(new ResourceLocation(Client.main().getClientBackground()));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, sr.width(), sr.height(),
                width, height, sr.width(), sr.height());

        int x = width / 2 - 150;
        int darkGray = -15658735;
        int lightGray = -15066598;
        RenderUtils.drawBorderedRect(width / 2 - 150, height / 2 - 150, width / 2 + 150, height / 2 + 150, 1, darkGray, lightGray);
        UnicodeFontRenderer comfortaa20 = Client.main().fontMgr().font("Comfortaa", 20, Font.PLAIN);
        String use_proxy = "USE PROXY";
        comfortaa20.drawString(use_proxy, width / 2 - comfortaa20.getStringWidth(use_proxy) / 2, height / 2 - 140, Colors.main().getGrey());
        String title = "PROXY";
        comfortaa20.drawString(title, width / 2 - comfortaa20.getStringWidth(title) / 2, height - 265, -1);

        String t1 = "Unterstützte Proxys:";
        String t2 = "HTTP, HTTPS, SOCKS4, SOCKS5";
        String t3 = "Diese Funktion ist derzeit noch nicht verfügbar!";
        String t4 = "Schaue später erneut hier vorbei!";

        UnicodeFontRenderer comfortaa18 = Client.main().fontMgr().font("Comfortaa", 18, Font.PLAIN);
        comfortaa18.drawString(t1, width / 2 - comfortaa18.getStringWidth(t1) / 2, height / 2 - 125, -1);
        comfortaa18.drawString(t2, width / 2 - comfortaa18.getStringWidth(t2) / 2, height / 2 - 112, -1);
        comfortaa18.drawString(t3, width / 2 - comfortaa18.getStringWidth(t3) / 2, height / 2 - 80, -1);
        comfortaa18.drawString(t4, width / 2 - comfortaa18.getStringWidth(t4) / 2, height / 2 - 67, -1);
        sessionID.drawTextBox();
        super.drawScreen(posX, posY, f);
    }
}