package com.masterof13fps.features.ui.guiscreens.altmanager;

import com.masterof13fps.Client;
import com.masterof13fps.Wrapper;
import com.masterof13fps.utils.render.Colors;
import com.masterof13fps.manager.altmanager.AltManager;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GuiAddAlt extends GuiScreen {
    public GuiScreen parent;

    public static ArrayList altList = new ArrayList();
    public static ArrayList guiSlotList = new ArrayList();
    public static File altFile;

    private GuiScreen parentScreen;
    private GuiTextField usernameField;
    private GuiTextField passwordField;
    private GuiTextField usernamePasswordField;

    public File getAltFile() {
        return altFile;
    }

    public GuiAddAlt(GuiScreen parentScreen) {
        parent = parentScreen;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0, width / 2 - 100, height - 185, 200, 20, "Add Alt"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height - 160, 200, 20, I18n.format("gui.cancel")));
        usernameField = new GuiTextField(0, fontRendererObj, width / 2 - 100, height - 350, 200, 20);
        passwordField = new GuiTextField(1, fontRendererObj, width / 2 - 100, height - 300, 200, 20);
        usernamePasswordField = new GuiTextField(2, fontRendererObj, width / 2 - 100, height - 250, 200, 20);
        usernameField.setMaxStringLength(500);
        passwordField.setMaxStringLength(500);
        usernamePasswordField.setMaxStringLength(500);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(parent);
        }
        // Add Alt
        if (button.id == 0) {
            if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                AltManager.slotList.add(new AltSlot(usernameField.getText(), passwordField.getText()));
                Client.main().getAltManager().saveAlts();
                mc.displayGuiScreen(parent);
            } else if (!usernamePasswordField.getText().isEmpty() && usernamePasswordField.getText().contains(":") && usernamePasswordField.getText().split(":").length == 2) {
                String[] alt = usernamePasswordField.getText().split(":");
                AltManager.slotList.add(new AltSlot(alt[0], alt[1]));
                Client.main().getAltManager().saveAlts();
                mc.displayGuiScreen(parent);
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        usernameField.textboxKeyTyped(typedChar, keyCode);
        passwordField.textboxKeyTyped(typedChar, keyCode);
        usernamePasswordField.textboxKeyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        usernamePasswordField.mouseClicked(mouseX, mouseY, mouseButton);
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
        comfortaa20.drawString("ADD ALT", width / 2 - comfortaa20.getStringWidth("ADD ALT") / 2, height / 2 - 140, Colors.main().getGrey());
        comfortaa20.drawString("MAIL", width / 2 - comfortaa20.getStringWidth("MAIL") / 2, height - 365, -1);
        comfortaa20.drawString("PASSWORD", width / 2 - comfortaa20.getStringWidth("PASSWORD") / 2, height - 315, -1);
        comfortaa20.drawString("MAIL : PASS", width / 2 - comfortaa20.getStringWidth("MAIL : PASS") / 2, height - 265, -1);
        usernameField.drawTextBox();
        passwordField.drawTextBox();
        usernamePasswordField.drawTextBox();

        super.drawScreen(posX, posY, f);
    }
}