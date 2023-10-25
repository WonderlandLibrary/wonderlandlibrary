/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.io.Charsets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiWinGame
extends GuiScreen {
    private static final ResourceLocation VIGNETTE_TEXTURE;
    private static final Logger logger;
    private float field_146578_s = 0.5f;
    private int field_146579_r;
    private int field_146581_h;
    private List<String> field_146582_i;
    private static final ResourceLocation MINECRAFT_LOGO;

    static {
        logger = LogManager.getLogger();
        MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
        VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        if (n == 1) {
            this.sendRespawnPacket();
        }
    }

    private void drawWinGameScreen(int n, int n2, float f) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        int n3 = width;
        float f2 = 0.0f - ((float)this.field_146581_h + f) * 0.5f * this.field_146578_s;
        float f3 = (float)height - ((float)this.field_146581_h + f) * 0.5f * this.field_146578_s;
        float f4 = 0.015625f;
        float f5 = ((float)this.field_146581_h + f - 0.0f) * 0.02f;
        float f6 = (float)(this.field_146579_r + height + height + 24) / this.field_146578_s;
        float f7 = (f6 - 20.0f - ((float)this.field_146581_h + f)) * 0.005f;
        if (f7 < f5) {
            f5 = f7;
        }
        if (f5 > 1.0f) {
            f5 = 1.0f;
        }
        f5 *= f5;
        f5 = f5 * 96.0f / 255.0f;
        worldRenderer.pos(0.0, height, zLevel).tex(0.0, f2 * f4).color(f5, f5, f5, 1.0f).endVertex();
        worldRenderer.pos(n3, height, zLevel).tex((float)n3 * f4, f2 * f4).color(f5, f5, f5, 1.0f).endVertex();
        worldRenderer.pos(n3, 0.0, zLevel).tex((float)n3 * f4, f3 * f4).color(f5, f5, f5, 1.0f).endVertex();
        worldRenderer.pos(0.0, 0.0, zLevel).tex(0.0, f3 * f4).color(f5, f5, f5, 1.0f).endVertex();
        tessellator.draw();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawWinGameScreen(n, n2, f);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        int n3 = 274;
        int n4 = width / 2 - n3 / 2;
        int n5 = height + 50;
        float f2 = -((float)this.field_146581_h + f) * this.field_146578_s;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, f2, 0.0f);
        this.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(n4, n5, 0, 0, 155, 44);
        this.drawTexturedModalRect(n4 + 155, n5, 0, 45, 155, 44);
        int n6 = n5 + 200;
        int n7 = 0;
        while (n7 < this.field_146582_i.size()) {
            float f3;
            if (n7 == this.field_146582_i.size() - 1 && (f3 = (float)n6 + f2 - (float)(height / 2 - 6)) < 0.0f) {
                GlStateManager.translate(0.0f, -f3, 0.0f);
            }
            if ((float)n6 + f2 + 12.0f + 8.0f > 0.0f && (float)n6 + f2 < (float)height) {
                String string = this.field_146582_i.get(n7);
                if (string.startsWith("[C]")) {
                    this.fontRendererObj.drawStringWithShadow(string.substring(3), n4 + (n3 - this.fontRendererObj.getStringWidth(string.substring(3))) / 2, n6, 0xFFFFFF);
                } else {
                    this.fontRendererObj.fontRandom.setSeed((long)n7 * 4238972211L + (long)(this.field_146581_h / 4));
                    this.fontRendererObj.drawStringWithShadow(string, n4, n6, 0xFFFFFF);
                }
            }
            n6 += 12;
            ++n7;
        }
        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0, 769);
        n7 = width;
        int n8 = height;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldRenderer.pos(0.0, n8, zLevel).tex(0.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldRenderer.pos(n7, n8, zLevel).tex(1.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldRenderer.pos(n7, 0.0, zLevel).tex(1.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldRenderer.pos(0.0, 0.0, zLevel).tex(0.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        super.drawScreen(n, n2, f);
    }

    @Override
    public void updateScreen() {
        MusicTicker musicTicker = this.mc.func_181535_r();
        SoundHandler soundHandler = this.mc.getSoundHandler();
        if (this.field_146581_h == 0) {
            musicTicker.func_181557_a();
            musicTicker.func_181558_a(MusicTicker.MusicType.CREDITS);
            soundHandler.resumeSounds();
        }
        soundHandler.update();
        ++this.field_146581_h;
        float f = (float)(this.field_146579_r + height + height + 24) / this.field_146578_s;
        if ((float)this.field_146581_h > f) {
            this.sendRespawnPacket();
        }
    }

    private void sendRespawnPacket() {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        this.mc.displayGuiScreen(null);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void initGui() {
        if (this.field_146582_i == null) {
            this.field_146582_i = Lists.newArrayList();
            try {
                String string = "";
                String string2 = "" + (Object)((Object)EnumChatFormatting.WHITE) + (Object)((Object)EnumChatFormatting.OBFUSCATED) + (Object)((Object)EnumChatFormatting.GREEN) + (Object)((Object)EnumChatFormatting.AQUA);
                int n = 274;
                InputStream inputStream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
                Random random = new Random(8124371L);
                while ((string = bufferedReader.readLine()) != null) {
                    string = string.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    while (string.contains(string2)) {
                        int n2 = string.indexOf(string2);
                        String string3 = string.substring(0, n2);
                        String string4 = string.substring(n2 + string2.length());
                        string = String.valueOf(string3) + (Object)((Object)EnumChatFormatting.WHITE) + (Object)((Object)EnumChatFormatting.OBFUSCATED) + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + string4;
                    }
                    this.field_146582_i.addAll(Minecraft.fontRendererObj.listFormattedStringToWidth(string, n));
                    this.field_146582_i.add("");
                }
                inputStream.close();
                int n3 = 0;
                while (n3 < 8) {
                    this.field_146582_i.add("");
                    ++n3;
                }
                inputStream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
                while ((string = bufferedReader.readLine()) != null) {
                    string = string.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    string = string.replaceAll("\t", "    ");
                    this.field_146582_i.addAll(Minecraft.fontRendererObj.listFormattedStringToWidth(string, n));
                    this.field_146582_i.add("");
                }
                inputStream.close();
                this.field_146579_r = this.field_146582_i.size() * 12;
            }
            catch (Exception exception) {
                logger.error("Couldn't load credits", (Throwable)exception);
            }
        }
    }
}

