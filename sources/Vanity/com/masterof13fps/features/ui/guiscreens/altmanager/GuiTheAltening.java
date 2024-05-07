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
import java.io.IOException;

public class GuiTheAltening extends GuiScreen {
    public GuiScreen parent;

    private GuiScreen parentScreen;
    private GuiTextField token, apiKey;

    public GuiTheAltening(GuiScreen parentScreen) {
        parent = parentScreen;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 70, 200, 20, "Token verwenden"));
        buttonList.add(new GuiButton(2, width / 2 - 100, height / 2 + 95, 200, 20, "Account generieren"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 + 120, 200, 20, I18n.format("gui.cancel")));
        token = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 2 - 25, 200, 20);
        apiKey = new GuiTextField(1, fontRendererObj, width / 2 - 100, height / 2 + 25, 200, 20);
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
                Client.main().getLoginUtil().login(token.getText());
                mc.displayGuiScreen(parent);
                break;
            }
            case 2: {
                Client.main().getLoginUtil().generate(apiKey.getText());
                mc.displayGuiScreen(parent);
                break;
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        token.textboxKeyTyped(typedChar, keyCode);
        apiKey.textboxKeyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        token.mouseClicked(mouseX, mouseY, mouseButton);
        apiKey.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int posX, int posY, float f) {
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
        String rectTitle = "REEDEM TOKEN (THEALTENING)";
        comfortaa20.drawStringWithShadow(rectTitle, width / 2 - comfortaa20.getStringWidth(rectTitle) / 2, height / 2 - 140, Colors.main().getGrey());
        String token = "TOKEN";
        String apiKey = "API KEY";
        comfortaa20.drawStringWithShadow(token, width / 2 - comfortaa20.getStringWidth(token) / 2, height / 2 - 42, -1);
        comfortaa20.drawStringWithShadow(apiKey, width / 2 - comfortaa20.getStringWidth(apiKey) / 2, height / 2 + 8, -1);

        this.token.drawTextBox();
        this.apiKey.drawTextBox();
        super.drawScreen(posX, posY, f);
    }
}