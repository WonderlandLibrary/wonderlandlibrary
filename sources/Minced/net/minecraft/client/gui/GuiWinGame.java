// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import java.io.InputStream;
import net.minecraft.client.resources.IResource;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.util.Collection;
import java.util.Random;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.util.text.TextFormatting;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class GuiWinGame extends GuiScreen
{
    private static final Logger LOGGER;
    private static final ResourceLocation MINECRAFT_LOGO;
    private static final ResourceLocation field_194401_g;
    private static final ResourceLocation VIGNETTE_TEXTURE;
    private final boolean poem;
    private final Runnable onFinished;
    private float time;
    private List<String> lines;
    private int totalScrollLength;
    private float scrollSpeed;
    
    public GuiWinGame(final boolean poemIn, final Runnable onFinishedIn) {
        this.scrollSpeed = 0.5f;
        this.poem = poemIn;
        this.onFinished = onFinishedIn;
        if (!poemIn) {
            this.scrollSpeed = 0.75f;
        }
    }
    
    @Override
    public void updateScreen() {
        GuiWinGame.mc.getMusicTicker().update();
        GuiWinGame.mc.getSoundHandler().update();
        final float f = (this.totalScrollLength + this.height + this.height + 24) / this.scrollSpeed;
        if (this.time > f) {
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
        this.onFinished.run();
        GuiWinGame.mc.displayGuiScreen(null);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    @Override
    public void initGui() {
        if (this.lines == null) {
            this.lines = (List<String>)Lists.newArrayList();
            IResource iresource = null;
            try {
                final String s = "" + TextFormatting.WHITE + TextFormatting.OBFUSCATED + TextFormatting.GREEN + TextFormatting.AQUA;
                final int i = 274;
                if (this.poem) {
                    iresource = GuiWinGame.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt"));
                    final InputStream inputstream = iresource.getInputStream();
                    final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                    final Random random = new Random(8124371L);
                    String s2;
                    while ((s2 = bufferedreader.readLine()) != null) {
                        String s3;
                        String s4;
                        for (s2 = s2.replaceAll("PLAYERNAME", GuiWinGame.mc.getSession().getUsername()); s2.contains(s); s2 = s3 + TextFormatting.WHITE + TextFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s4) {
                            final int j = s2.indexOf(s);
                            s3 = s2.substring(0, j);
                            s4 = s2.substring(j + s.length());
                        }
                        this.lines.addAll(GuiWinGame.mc.fontRenderer.listFormattedStringToWidth(s2, 274));
                        this.lines.add("");
                    }
                    inputstream.close();
                    for (int k = 0; k < 8; ++k) {
                        this.lines.add("");
                    }
                }
                final InputStream inputstream2 = GuiWinGame.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
                final BufferedReader bufferedreader2 = new BufferedReader(new InputStreamReader(inputstream2, StandardCharsets.UTF_8));
                String s5;
                while ((s5 = bufferedreader2.readLine()) != null) {
                    s5 = s5.replaceAll("PLAYERNAME", GuiWinGame.mc.getSession().getUsername());
                    s5 = s5.replaceAll("\t", "    ");
                    this.lines.addAll(GuiWinGame.mc.fontRenderer.listFormattedStringToWidth(s5, 274));
                    this.lines.add("");
                }
                inputstream2.close();
                this.totalScrollLength = this.lines.size() * 12;
            }
            catch (Exception exception) {
                GuiWinGame.LOGGER.error("Couldn't load credits", (Throwable)exception);
            }
            finally {
                IOUtils.closeQuietly((Closeable)iresource);
            }
        }
    }
    
    private void drawWinGameScreen(final int p_146575_1_, final int p_146575_2_, final float p_146575_3_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GuiWinGame.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        final int i = this.width;
        final float f = -this.time * 0.5f * this.scrollSpeed;
        final float f2 = this.height - this.time * 0.5f * this.scrollSpeed;
        final float f3 = 0.015625f;
        float f4 = this.time * 0.02f;
        final float f5 = (this.totalScrollLength + this.height + this.height + 24) / this.scrollSpeed;
        final float f6 = (f5 - 20.0f - this.time) * 0.005f;
        if (f6 < f4) {
            f4 = f6;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        f4 *= f4;
        f4 = f4 * 96.0f / 255.0f;
        bufferbuilder.pos(0.0, this.height, this.zLevel).tex(0.0, f * 0.015625f).color(f4, f4, f4, 1.0f).endVertex();
        bufferbuilder.pos(i, this.height, this.zLevel).tex(i * 0.015625f, f * 0.015625f).color(f4, f4, f4, 1.0f).endVertex();
        bufferbuilder.pos(i, 0.0, this.zLevel).tex(i * 0.015625f, f2 * 0.015625f).color(f4, f4, f4, 1.0f).endVertex();
        bufferbuilder.pos(0.0, 0.0, this.zLevel).tex(0.0, f2 * 0.015625f).color(f4, f4, f4, 1.0f).endVertex();
        tessellator.draw();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        final int i = 274;
        final int j = this.width / 2 - 137;
        final int k = this.height + 50;
        this.time += partialTicks;
        final float f = -this.time * this.scrollSpeed;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, f, 0.0f);
        GuiWinGame.mc.getTextureManager().bindTexture(GuiWinGame.MINECRAFT_LOGO);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.drawTexturedModalRect(j, k, 0, 0, 155, 44);
        this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        GuiWinGame.mc.getTextureManager().bindTexture(GuiWinGame.field_194401_g);
        Gui.drawModalRectWithCustomSizedTexture((float)(j + 88), (float)(k + 37), 0.0f, 0.0f, 98.0f, 14.0f, 128.0f, 16.0f);
        GlStateManager.disableAlpha();
        int l = k + 100;
        for (int i2 = 0; i2 < this.lines.size(); ++i2) {
            if (i2 == this.lines.size() - 1) {
                final float f2 = l + f - (this.height / 2 - 6);
                if (f2 < 0.0f) {
                    GlStateManager.translate(0.0f, -f2, 0.0f);
                }
            }
            if (l + f + 12.0f + 8.0f > 0.0f && l + f < this.height) {
                final String s = this.lines.get(i2);
                if (s.startsWith("[C]")) {
                    this.fontRenderer.drawStringWithShadow(s.substring(3), (float)(j + (274 - this.fontRenderer.getStringWidth(s.substring(3))) / 2), (float)l, 16777215);
                }
                else {
                    this.fontRenderer.fontRandom.setSeed((long)(i2 * 4238972211L + this.time / 4.0f));
                    this.fontRenderer.drawStringWithShadow(s, (float)j, (float)l, 16777215);
                }
            }
            l += 12;
        }
        GlStateManager.popMatrix();
        GuiWinGame.mc.getTextureManager().bindTexture(GuiWinGame.VIGNETTE_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        final int j2 = this.width;
        final int k2 = this.height;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0, k2, this.zLevel).tex(0.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(j2, k2, this.zLevel).tex(1.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(j2, 0.0, this.zLevel).tex(1.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(0.0, 0.0, this.zLevel).tex(0.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
        field_194401_g = new ResourceLocation("textures/gui/title/edition.png");
        VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    }
}
