/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
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
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    private int field_146581_h;
    private List<String> field_146582_i;
    private int field_146579_r;
    private float field_146578_s = 0.5f;

    @Override
    public void updateScreen() {
        MusicTicker musicticker = this.mc.func_181535_r();
        SoundHandler soundhandler = this.mc.getSoundHandler();
        if (this.field_146581_h == 0) {
            musicticker.playBeatGameMusic();
            musicticker.playMusicWithType(MusicTicker.MusicType.CREDITS);
            soundhandler.resumeSounds();
        }
        soundhandler.update();
        ++this.field_146581_h;
        float f2 = (float)(this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        if ((float)this.field_146581_h > f2) {
            this.sendRespawnPacket();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.sendRespawnPacket();
        }
    }

    private void sendRespawnPacket() {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
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
                String s2 = "";
                String s1 = "" + (Object)((Object)EnumChatFormatting.WHITE) + (Object)((Object)EnumChatFormatting.OBFUSCATED) + (Object)((Object)EnumChatFormatting.GREEN) + (Object)((Object)EnumChatFormatting.AQUA);
                int i2 = 274;
                InputStream inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
                Random random = new Random(8124371L);
                while ((s2 = bufferedreader.readLine()) != null) {
                    s2 = s2.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    while (s2.contains(s1)) {
                        int j2 = s2.indexOf(s1);
                        String s22 = s2.substring(0, j2);
                        String s3 = s2.substring(j2 + s1.length());
                        s2 = s22 + (Object)((Object)EnumChatFormatting.WHITE) + (Object)((Object)EnumChatFormatting.OBFUSCATED) + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3;
                    }
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s2, i2));
                    this.field_146582_i.add("");
                }
                inputstream.close();
                for (int k2 = 0; k2 < 8; ++k2) {
                    this.field_146582_i.add("");
                }
                inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
                bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
                while ((s2 = bufferedreader.readLine()) != null) {
                    s2 = s2.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    s2 = s2.replaceAll("\t", "    ");
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s2, i2));
                    this.field_146582_i.add("");
                }
                inputstream.close();
                this.field_146579_r = this.field_146582_i.size() * 12;
            }
            catch (Exception exception) {
                logger.error("Couldn't load credits", (Throwable)exception);
            }
        }
    }

    private void drawWinGameScreen(int p_146575_1_, int p_146575_2_, float p_146575_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        int i2 = this.width;
        float f2 = 0.0f - ((float)this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        float f1 = (float)this.height - ((float)this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        float f22 = 0.015625f;
        float f3 = ((float)this.field_146581_h + p_146575_3_ - 0.0f) * 0.02f;
        float f4 = (float)(this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        float f5 = (f4 - 20.0f - ((float)this.field_146581_h + p_146575_3_)) * 0.005f;
        if (f5 < f3) {
            f3 = f5;
        }
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        f3 *= f3;
        f3 = f3 * 96.0f / 255.0f;
        worldrenderer.pos(0.0, this.height, this.zLevel).tex(0.0, f2 * f22).color(f3, f3, f3, 1.0f).endVertex();
        worldrenderer.pos(i2, this.height, this.zLevel).tex((float)i2 * f22, f2 * f22).color(f3, f3, f3, 1.0f).endVertex();
        worldrenderer.pos(i2, 0.0, this.zLevel).tex((float)i2 * f22, f1 * f22).color(f3, f3, f3, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, this.zLevel).tex(0.0, f1 * f22).color(f3, f3, f3, 1.0f).endVertex();
        tessellator.draw();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i2 = 274;
        int j2 = this.width / 2 - i2 / 2;
        int k2 = this.height + 50;
        float f2 = -((float)this.field_146581_h + partialTicks) * this.field_146578_s;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, f2, 0.0f);
        this.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(j2, k2, 0, 0, 155, 44);
        this.drawTexturedModalRect(j2 + 155, k2, 0, 45, 155, 44);
        int l2 = k2 + 200;
        for (int i1 = 0; i1 < this.field_146582_i.size(); ++i1) {
            float f1;
            if (i1 == this.field_146582_i.size() - 1 && (f1 = (float)l2 + f2 - (float)(this.height / 2 - 6)) < 0.0f) {
                GlStateManager.translate(0.0f, -f1, 0.0f);
            }
            if ((float)l2 + f2 + 12.0f + 8.0f > 0.0f && (float)l2 + f2 < (float)this.height) {
                String s2 = this.field_146582_i.get(i1);
                if (s2.startsWith("[C]")) {
                    this.fontRendererObj.drawStringWithShadow(s2.substring(3), j2 + (i2 - this.fontRendererObj.getStringWidth(s2.substring(3))) / 2, l2, 0xFFFFFF);
                } else {
                    this.fontRendererObj.fontRandom.setSeed((long)i1 * 4238972211L + (long)(this.field_146581_h / 4));
                    this.fontRendererObj.drawStringWithShadow(s2, j2, l2, 0xFFFFFF);
                }
            }
            l2 += 12;
        }
        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0, 769);
        int j1 = this.width;
        int k1 = this.height;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, k1, this.zLevel).tex(0.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(j1, k1, this.zLevel).tex(1.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(j1, 0.0, this.zLevel).tex(1.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, this.zLevel).tex(0.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

