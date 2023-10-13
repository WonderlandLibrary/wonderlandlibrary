/*
 * Decompiled with CFR 0.150.
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
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiWinGame
extends GuiScreen {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation field_146576_f = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation field_146577_g = new ResourceLocation("textures/misc/vignette.png");
    private int field_146581_h;
    private List field_146582_i;
    private int field_146579_r;
    private float field_146578_s = 0.5f;
    private static final String __OBFID = "CL_00000719";

    @Override
    public void updateScreen() {
        ++this.field_146581_h;
        float var1 = (float)(this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        if ((float)this.field_146581_h > var1) {
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
                int var6;
                String var1 = "";
                String var2 = "" + (Object)((Object)EnumChatFormatting.WHITE) + (Object)((Object)EnumChatFormatting.OBFUSCATED) + (Object)((Object)EnumChatFormatting.GREEN) + (Object)((Object)EnumChatFormatting.AQUA);
                int var3 = 274;
                BufferedReader var4 = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream(), Charsets.UTF_8));
                Random var5 = new Random(8124371L);
                while ((var1 = var4.readLine()) != null) {
                    var1 = var1.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    while (var1.contains(var2)) {
                        var6 = var1.indexOf(var2);
                        String var7 = var1.substring(0, var6);
                        String var8 = var1.substring(var6 + var2.length());
                        var1 = String.valueOf(var7) + (Object)((Object)EnumChatFormatting.WHITE) + (Object)((Object)EnumChatFormatting.OBFUSCATED) + "XXXXXXXX".substring(0, var5.nextInt(4) + 3) + var8;
                    }
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(var1, var3));
                    this.field_146582_i.add("");
                }
                for (var6 = 0; var6 < 8; ++var6) {
                    this.field_146582_i.add("");
                }
                var4 = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream(), Charsets.UTF_8));
                while ((var1 = var4.readLine()) != null) {
                    var1 = var1.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    var1 = var1.replaceAll("\t", "    ");
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(var1, var3));
                    this.field_146582_i.add("");
                }
                this.field_146579_r = this.field_146582_i.size() * 12;
            }
            catch (Exception var9) {
                logger.error("Couldn't load credits", (Throwable)var9);
            }
        }
    }

    private void drawWinGameScreen(int p_146575_1_, int p_146575_2_, float p_146575_3_) {
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        var5.startDrawingQuads();
        var5.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f);
        int var6 = this.width;
        float var7 = 0.0f - ((float)this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        float var8 = (float)this.height - ((float)this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        float var9 = 0.015625f;
        float var10 = ((float)this.field_146581_h + p_146575_3_ - 0.0f) * 0.02f;
        float var11 = (float)(this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        float var12 = (var11 - 20.0f - ((float)this.field_146581_h + p_146575_3_)) * 0.005f;
        if (var12 < var10) {
            var10 = var12;
        }
        if (var10 > 1.0f) {
            var10 = 1.0f;
        }
        var10 *= var10;
        var10 = var10 * 96.0f / 255.0f;
        var5.func_178986_b(var10, var10, var10);
        var5.addVertexWithUV(0.0, this.height, this.zLevel, 0.0, var7 * var9);
        var5.addVertexWithUV(var6, this.height, this.zLevel, (float)var6 * var9, var7 * var9);
        var5.addVertexWithUV(var6, 0.0, this.zLevel, (float)var6 * var9, var8 * var9);
        var5.addVertexWithUV(0.0, 0.0, this.zLevel, 0.0, var8 * var9);
        var4.draw();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int var11;
        this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        int var6 = 274;
        int var7 = this.width / 2 - var6 / 2;
        int var8 = this.height + 50;
        float var9 = -((float)this.field_146581_h + partialTicks) * this.field_146578_s;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, var9, 0.0f);
        this.mc.getTextureManager().bindTexture(field_146576_f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(var7, var8, 0, 0, 155, 44);
        this.drawTexturedModalRect(var7 + 155, var8, 0, 45, 155, 44);
        var5.func_178991_c(0xFFFFFF);
        int var10 = var8 + 200;
        for (var11 = 0; var11 < this.field_146582_i.size(); ++var11) {
            float var12;
            if (var11 == this.field_146582_i.size() - 1 && (var12 = (float)var10 + var9 - (float)(this.height / 2 - 6)) < 0.0f) {
                GlStateManager.translate(0.0f, -var12, 0.0f);
            }
            if ((float)var10 + var9 + 12.0f + 8.0f > 0.0f && (float)var10 + var9 < (float)this.height) {
                String var13 = (String)this.field_146582_i.get(var11);
                if (var13.startsWith("[C]")) {
                    this.fontRendererObj.drawStringWithShadow(var13.substring(3), var7 + (var6 - this.fontRendererObj.getStringWidth(var13.substring(3))) / 2, var10, 0xFFFFFF);
                } else {
                    this.fontRendererObj.fontRandom.setSeed((long)var11 * 4238972211L + (long)(this.field_146581_h / 4));
                    this.fontRendererObj.drawStringWithShadow(var13, var7, var10, 0xFFFFFF);
                }
            }
            var10 += 12;
        }
        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(field_146577_g);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0, 769);
        var5.startDrawingQuads();
        var5.func_178960_a(1.0f, 1.0f, 1.0f, 1.0f);
        var11 = this.width;
        int var14 = this.height;
        var5.addVertexWithUV(0.0, var14, this.zLevel, 0.0, 1.0);
        var5.addVertexWithUV(var11, var14, this.zLevel, 1.0, 1.0);
        var5.addVertexWithUV(var11, 0.0, this.zLevel, 1.0, 0.0);
        var5.addVertexWithUV(0.0, 0.0, this.zLevel, 0.0, 0.0);
        var4.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

