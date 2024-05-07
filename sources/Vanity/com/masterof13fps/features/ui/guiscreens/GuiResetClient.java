package com.masterof13fps.features.ui.guiscreens;

import com.masterof13fps.Client;
import com.masterof13fps.manager.fontmanager.FontManager;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.utils.render.RenderUtils;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class GuiResetClient extends GuiScreen {
    public GuiScreen parent;

    FontManager fM = Client.main().fontMgr();

    private GuiScreen parentScreen;

    public GuiResetClient(GuiScreen parentScreen) {
        parent = parentScreen;
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.add(new GuiButton(0, width / 2 - 120, height / 2 - 30, 120, 20, "Ich bin mir sicher!"));
        buttonList.add(new GuiButton(4, width / 2 + 10, height / 2 - 30, 120, 20, "Nein, doch nicht ..."));
        buttonList.add(new GuiButton(2, width / 2 - 120, height / 2, 250, 20, "Zurück"));
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
            case 0: {
                Client.main().getClientDir().delete();
                Client.main().getClientDir().deleteOnExit();
                notify.debug("Deleted all settings (Client Reset!)");
                mc.displayGuiScreen(new GuiMainMenu());
                break;
            }
            case 4:
            case 2: {
                mc.displayGuiScreen(parent);
                break;
            }
        }
    }

    public void drawScreen(int posX, int posY, float f) {
        ScaledResolution sr = new ScaledResolution(mc);

        mc.getTextureManager().bindTexture(new ResourceLocation(Client.main().getClientBackground()));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, sr.width(), sr.height(),
                width, height, sr.width(), sr.height());

        RenderUtils.drawRoundedRect(width / 2 - 130, height / 2 - 70, 265, 95, 10, new Color(45,45,45).getRGB());

        String warning1 = "Bist du dir sicher? (letzte Chance)";
        String warning2 = "Dies löscht alle deine Einstellungen!";
        UnicodeFontRenderer font1 = Client.main().fontMgr().font("Comfortaa", 24, Font.PLAIN);
        font1.drawStringWithShadow(warning1, sr.width() / 2 - font1.getStringWidth(warning1) / 2, sr.height() / 2 - 65, -1);
        font1.drawStringWithShadow(warning2, sr.width() / 2 - font1.getStringWidth(warning2) / 2, sr.height() / 2 - 50, -1);

        super.drawScreen(posX, posY, f);
    }
}