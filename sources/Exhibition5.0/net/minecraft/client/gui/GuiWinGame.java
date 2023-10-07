// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import java.util.Collection;
import java.util.Random;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.io.Charsets;
import net.minecraft.util.EnumChatFormatting;
import com.google.common.collect.Lists;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C16PacketClientStatus;
import java.io.IOException;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class GuiWinGame extends GuiScreen
{
    private static final Logger logger;
    private static final ResourceLocation field_146576_f;
    private static final ResourceLocation field_146577_g;
    private int field_146581_h;
    private List field_146582_i;
    private int field_146579_r;
    private float field_146578_s;
    private static final String __OBFID = "CL_00000719";
    
    public GuiWinGame() {
        this.field_146578_s = 0.5f;
    }
    
    @Override
    public void updateScreen() {
        ++this.field_146581_h;
        final float var1 = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        if (this.field_146581_h > var1) {
            this.sendRespawnPacket();
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
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
                String var1 = "";
                final String var2 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN + EnumChatFormatting.AQUA;
                final short var3 = 274;
                BufferedReader var4 = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream(), Charsets.UTF_8));
                final Random var5 = new Random(8124371L);
                while ((var1 = var4.readLine()) != null) {
                    String var7;
                    String var8;
                    for (var1 = var1.replaceAll("PLAYERNAME", this.mc.getSession().getUsername()); var1.contains(var2); var1 = var7 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, var5.nextInt(4) + 3) + var8) {
                        final int var6 = var1.indexOf(var2);
                        var7 = var1.substring(0, var6);
                        var8 = var1.substring(var6 + var2.length());
                    }
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(var1, var3));
                    this.field_146582_i.add("");
                }
                for (int var6 = 0; var6 < 8; ++var6) {
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
                GuiWinGame.logger.error("Couldn't load credits", (Throwable)var9);
            }
        }
    }
    
    private void drawWinGameScreen(final int p_146575_1_, final int p_146575_2_, final float p_146575_3_) {
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        var5.startDrawingQuads();
        var5.setColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
        final int var6 = this.width;
        final float var7 = 0.0f - (this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        final float var8 = this.height - (this.field_146581_h + p_146575_3_) * 0.5f * this.field_146578_s;
        final float var9 = 0.015625f;
        float var10 = (this.field_146581_h + p_146575_3_ - 0.0f) * 0.02f;
        final float var11 = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        final float var12 = (var11 - 20.0f - (this.field_146581_h + p_146575_3_)) * 0.005f;
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
        var5.addVertexWithUV(var6, this.height, this.zLevel, var6 * var9, var7 * var9);
        var5.addVertexWithUV(var6, 0.0, this.zLevel, var6 * var9, var8 * var9);
        var5.addVertexWithUV(0.0, 0.0, this.zLevel, 0.0, var8 * var9);
        var4.draw();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        final short var6 = 274;
        final int var7 = this.width / 2 - var6 / 2;
        final int var8 = this.height + 50;
        final float var9 = -(this.field_146581_h + partialTicks) * this.field_146578_s;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, var9, 0.0f);
        this.mc.getTextureManager().bindTexture(GuiWinGame.field_146576_f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(var7, var8, 0, 0, 155, 44);
        this.drawTexturedModalRect(var7 + 155, var8, 0, 45, 155, 44);
        var5.func_178991_c(16777215);
        int var10 = var8 + 200;
        for (int var11 = 0; var11 < this.field_146582_i.size(); ++var11) {
            if (var11 == this.field_146582_i.size() - 1) {
                final float var12 = var10 + var9 - (this.height / 2 - 6);
                if (var12 < 0.0f) {
                    GlStateManager.translate(0.0f, -var12, 0.0f);
                }
            }
            if (var10 + var9 + 12.0f + 8.0f > 0.0f && var10 + var9 < this.height) {
                final String var13 = this.field_146582_i.get(var11);
                if (var13.startsWith("[C]")) {
                    this.fontRendererObj.drawStringWithShadow(var13.substring(3), var7 + (var6 - this.fontRendererObj.getStringWidth(var13.substring(3))) / 2, var10, 16777215);
                }
                else {
                    this.fontRendererObj.fontRandom.setSeed(var11 * 4238972211L + this.field_146581_h / 4);
                    this.fontRendererObj.drawStringWithShadow(var13, var7, var10, 16777215);
                }
            }
            var10 += 12;
        }
        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(GuiWinGame.field_146577_g);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0, 769);
        var5.startDrawingQuads();
        var5.setColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
        int var11 = this.width;
        final int var14 = this.height;
        var5.addVertexWithUV(0.0, var14, this.zLevel, 0.0, 1.0);
        var5.addVertexWithUV(var11, var14, this.zLevel, 1.0, 1.0);
        var5.addVertexWithUV(var11, 0.0, this.zLevel, 1.0, 0.0);
        var5.addVertexWithUV(0.0, 0.0, this.zLevel, 0.0, 0.0);
        var4.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    static {
        logger = LogManager.getLogger();
        field_146576_f = new ResourceLocation("textures/gui/title/minecraft.png");
        field_146577_g = new ResourceLocation("textures/misc/vignette.png");
    }
}
