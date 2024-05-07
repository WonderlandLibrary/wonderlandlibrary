package com.masterof13fps.features.ui.guiscreens;

import com.masterof13fps.Client;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class GuiFirstUse extends GuiScreen implements GuiYesNoCallback {

    public void initGui() {
        buttonList.add(new GuiButton(0, width / 2 - 50, height - 50, 120, 20, "Ok, verstanden!"));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();
        drawGradientRect(0, 0, width, height, -2130706433, 16777215);
        drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        ScaledResolution sr = new ScaledResolution(mc);
        mc.getTextureManager().bindTexture(new ResourceLocation(Client.main().getClientBackground()));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, sr.width(), sr.height(),
                width, height, sr.width(), sr.height());

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(
                MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        GlStateManager.scale(f, f, f);
        GlStateManager.popMatrix();

        UnicodeFontRenderer titleFont = Client.main().fontMgr().font("BigNoodleTitling", 50, Font.PLAIN);
        String title = "§cWillkommen!";
        titleFont.drawStringWithShadow(title, width / 2 - titleFont.getStringWidth(title) / 2, 40, -1);
        UnicodeFontRenderer messageFont = Client.main().fontMgr().font("Comfortaa", 22, Font.PLAIN);

        String m1 = "§cRSHIFT §8- §6Öffnet das ClickGUI";
        String m2 = "§cLCONTROL + RCONTROL §8- §6Aktiviert den Invis Mode";
        String m3 = "§cSHIFT + RCONTROL §8- §6Deaktiviert den Invis Mode";
        String m4 = "§cEND §8- §6Öffnet den 'Item & PlayerUtilities' GuiScreen";
        String m5 = "§cChat-Prefix§8: §4" + Client.main().getClientPrefix() + " §8(§c" + Client.main().getClientPrefixWorded() + "§8)";

        messageFont.drawStringWithShadow(m1, width / 2 - messageFont.getStringWidth(m1) / 2, height / 2 - 100, -1);
        messageFont.drawStringWithShadow(m2, width / 2 - messageFont.getStringWidth(m2) / 2, height / 2 - 80, -1);
        messageFont.drawStringWithShadow(m3, width / 2 - messageFont.getStringWidth(m3) / 2, height / 2 - 60, -1);
        messageFont.drawStringWithShadow(m4, width / 2 - messageFont.getStringWidth(m4) / 2, height / 2 - 40, -1);
        messageFont.drawStringWithShadow(m5, width / 2 - messageFont.getStringWidth(m5) / 2, height / 2 - 20, -1);

        float scale = 5.0F;
        GL11.glScalef(scale, scale, scale);
        GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}