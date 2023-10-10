/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tk.rektsky.Client;
import tk.rektsky.gui.GuiWelcomeScreen;

public class GuiLoading
extends GuiScreen {
    private static long firstRenderTime = 0L;
    private static HyperiumFontRenderer frTitle = Client.getFontWithSize(112);
    private static HyperiumFontRenderer frDescription = Client.getFontWithSize(36);

    public GuiLoading() {
        firstRenderTime = Minecraft.getSystemTime();
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        long renderedTime = 0L;
        if (Minecraft.getSystemTime() - firstRenderTime >= 5000L) {
            renderedTime = Minecraft.getSystemTime() - firstRenderTime - 5000L;
        }
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("rektsky/icons/icon.png"));
        GuiLoading.drawModalRectWithCustomSizedTexture(this.width / 2 - 128, this.height / 2 - 128, 0.0f, 0.0f, 256, 256, 256.0f, 256.0f);
        float f2 = (float)this.width / 2.0f - frDescription.getWidth("RektSky, made by fan87, CeMrK, Hackage & Thereallo") / 2.0f;
        float f3 = (float)this.height / 2.0f + 198.0f;
        frTitle.getClass();
        float f4 = f3 + 9.0f;
        frDescription.getClass();
        frDescription.drawString("RektSky, made by fan87, CeMrK, Hackage & Thereallo", f2, f4 + 9.0f, 0xFFFFFF);
        if (renderedTime >= 9999L) {
            this.mc.displayGuiScreen(new GuiWelcomeScreen());
        }
        if (renderedTime >= 7000L) {
            Color color = new Color(0.0f, 0.0f, 0.0f, Math.min(((float)renderedTime - 5000.0f) / 3000.0f, 1.0f));
            GlStateManager.color(1.0f, 1.0f, 1.0f, (float)(renderedTime - 8000L) / 3000.0f);
            this.drawGradientRect(0, 0, this.width, this.height, color.getRGB(), color.getRGB());
        }
    }
}

