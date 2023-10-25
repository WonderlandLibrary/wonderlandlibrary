package org.dreamcore.security.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.ui.button.GuiAltButton;
import org.dreamcore.security.utils.HashUtil;
import org.dreamcore.security.utils.HwidUtils;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class AuthenticationGui extends GuiScreen {

    public static GuiSearcher uid;

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        buttonList.clear();
        uid = new GuiSearcher(2, mc.fontRendererObj, width / 2 - 100, height / 2 - 60, 200, 20);
        uid.setMaxStringLength(20);
        buttonList.add(new GuiAltButton(1, width / 2 - 100, height / 2 - 20, "Login"));

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        RectHelper.drawRect(1, 1.4F, sr.getScaledWidth() - 1, sr.getScaledHeight() - 1.7F, (new Color(12, 12, 12)).getRGB());
        RectHelper.drawRect(sr.getScaledWidth() / 2F - 120, sr.getScaledHeight() / 2F - 90, sr.getScaledWidth() - 220, sr.getScaledHeight() - 150, new Color(16, 15, 15).getRGB());

        uid.drawTextBox();

        //  mc.robotoRegularFontRender.drawStringWithShadow("Your key:", width / 2F - 25, height / 2F - 82, -1);
        if (!uid.isFocused) {
            mc.robotoRegularFontRender.drawStringWithShadow("Your key...", width / 2F - 96, height / 2F - 53, Color.GRAY.getRGB());
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        try {
            uid.textboxKeyTyped(typedChar, keyCode);
        } catch (Exception e) {

        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        uid.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
        super.actionPerformed(button);
    }
}
