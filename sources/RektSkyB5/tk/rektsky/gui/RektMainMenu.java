/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tk.rektsky.utils.LaunchWrapper;

public class RektMainMenu
extends GuiScreen {
    private static final FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ResourceLocation ICON = new ResourceLocation("rektsky/icons/icon.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation("rektsky/images/background.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        this.drawDefaultBackground();
        LaunchWrapper.logger.info("FUCK");
        this.fontRendererObj.drawString("Cyka blyat", 0, 0, 0);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures\\gui\\presets\\luck.png"));
        RektMainMenu.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, 100, 100, 100.0f, 100.0f);
        GlStateManager.popMatrix();
    }

    @Override
    public void initGui() {
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }
}

