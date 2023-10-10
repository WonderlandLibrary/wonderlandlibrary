/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import tk.rektsky.Client;
import tk.rektsky.gui.AltManagerScreen;
import tk.rektsky.gui.GuiWelcomeScreen;
import tk.rektsky.utils.display.AnimationUtil;

public class GuiMainMenu
extends GuiScreen
implements GuiYesNoCallback {
    public static Boolean rendered = false;
    private static Color altManagerBtnColor = new Color(61, 199, 171);
    private static HyperiumFontRenderer fr = Client.getFont();
    private static HyperiumFontRenderer frJoin = Client.getFontWithSize(40);
    private static HyperiumFontRenderer frAuthor = Client.getFontWithSize(18);
    private static HyperiumFontRenderer frVersion;
    private static HyperiumFontRenderer frDiscord;
    private static HyperiumFontRenderer frChannel;
    private long firstRenderTime = 0L;

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 57) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (keyCode == 28) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseX > this.width - 42 && mouseX < this.width - 10 && mouseY > this.height - 42 && mouseY < this.height - 10 && mouseButton == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        } else if (mouseX > this.width - 42 && mouseX < this.width - 10 && mouseY > this.height - 80 && mouseY < this.height - 40 && mouseButton == 0) {
            this.mc.displayGuiScreen(new AltManagerScreen(this));
        }
    }

    @Override
    public void initGui() {
        this.firstRenderTime = Minecraft.getSystemTime();
        rendered = true;
    }

    public static void Translate3DLayerEffect(int width, int height, int mouseX, int mouseY, int layer) {
        GlStateManager.translate(((float)(-mouseX) + (float)width / 2.0f) / ((float)layer * 20.0f), ((float)(-mouseY) + (float)height / 2.0f) / ((float)layer * 20.0f), 0.0f);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        long renderedTime = Minecraft.getSystemTime() - this.firstRenderTime;
        this.mc.mcMusicTicker.currentMusic = null;
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.3f, 1.3f, 0.0f);
        GuiMainMenu.Translate3DLayerEffect(this.width, this.height, mouseX, mouseY, 5);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("rektsky/images/background.png"));
        GuiMainMenu.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        GlStateManager.popMatrix();
        if (renderedTime <= 4000L) {
            GlStateManager.translate((1.0 - AnimationUtil.easeOutElastic((double)renderedTime / 4000.0)) * -150.0, 0.0, 0.0);
        }
        GlStateManager.pushMatrix();
        GuiMainMenu.Translate3DLayerEffect(this.width, this.height, mouseX, mouseY, 5);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("rektsky/icons/icon.png"));
        GuiMainMenu.drawModalRectWithCustomSizedTexture(this.y(20.0f), this.y(10.0f), 0.0f, 0.0f, this.y(50.0f), this.y(50.0f), this.y(50.0f), this.y(50.0f));
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GuiMainMenu.Translate3DLayerEffect(this.width, this.height, mouseX, mouseY, 4);
        frVersion.drawString("B5", this.y(65.0f), this.y(61.0f), 0xFFFFFF);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GuiMainMenu.Translate3DLayerEffect(this.width, this.height, mouseX, mouseY, 3);
        float f2 = this.y(65.0f);
        frChannel.getClass();
        frChannel.drawString("Private", f2 + 9.0f / 2.0f, this.y(63.0f), 0xFFFFFF);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GuiMainMenu.Translate3DLayerEffect(this.width, this.height, mouseX, mouseY, 3);
        frAuthor.drawString("Made by fan87, CemrK, Hackage & Thereallo", 2.0f, this.height - GuiMainMenu.frAuthor.FONT_HEIGHT - 2, 0xFFFFFF);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GuiMainMenu.Translate3DLayerEffect(this.width, this.height, mouseX, mouseY, 3);
        frDiscord.drawString("Welcome back, [" + Client.user.role.name + "] " + Client.user.username + "!", 34.0f, this.height - GuiMainMenu.frJoin.FONT_HEIGHT - GuiMainMenu.frDiscord.FONT_HEIGHT - 16, 0xFFFFFF);
        if (GuiWelcomeScreen.avatar != null) {
            this.mc.getTextureManager().bindTexture(GuiWelcomeScreen.avatar);
            GuiMainMenu.drawModalRectWithCustomSizedTexture(0, this.height - GuiMainMenu.frJoin.FONT_HEIGHT - GuiMainMenu.frDiscord.FONT_HEIGHT - 32, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("rektsky/images/settings.png"));
        GuiMainMenu.drawModalRectWithCustomSizedTexture(this.width - 42, this.height - 42, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.mc.fontRendererObj.drawString("", 0, 0, altManagerBtnColor.getRGB());
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("rektsky/images/altmanager.png"));
        GuiMainMenu.drawModalRectWithCustomSizedTexture(this.width - 42, this.height - 80, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GuiMainMenu.Translate3DLayerEffect(this.width, this.height, mouseX, mouseY, 2);
        GlStateManager.translate(0.0f, this.height - 40, 0.0f);
        float f3 = 1.8f - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0f * (float)Math.PI * 2.0f) * 0.15f);
        f3 = f3 * 100.0f / (float)(GuiMainMenu.frJoin.FONT_HEIGHT + 32);
        frJoin.drawString("Press [Space] to enter multiplayer", (float)(sr.getScaledWidth() / 2) - frJoin.getWidth("Press [Space] to enter multiplayer") / 2.0f, -GuiMainMenu.frJoin.FONT_HEIGHT - 14, 0xFFFFFF);
        frJoin.drawString("Press [Enter] to enter singleplayer", (float)(sr.getScaledWidth() / 2) - frJoin.getWidth("Press [Enter] to enter singleplayer") / 2.0f, 0.0f, 0xFFFFFF);
        GlStateManager.popMatrix();
        if (renderedTime <= 4000L) {
            GlStateManager.translate((1.0 - AnimationUtil.easeOutElastic((double)renderedTime / 4000.0)) * 150.0, 0.0, 0.0);
        }
        if (renderedTime <= 3000L) {
            Color color = new Color(0.0f, 0.0f, 0.0f, 1.0f - (float)renderedTime / 3000.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, (float)renderedTime / 3000.0f);
            this.drawGradientRect(0, 0, this.width, this.height, color.getRGB(), color.getRGB());
        }
    }

    public int x(float x2) {
        return Math.round((float)this.width / 100.0f * x2);
    }

    public int y(float x2) {
        return Math.round((float)this.height / 100.0f * x2);
    }

    static {
        frDiscord = frVersion = Client.getFontWithSize(28);
        frChannel = frAuthor;
    }
}

