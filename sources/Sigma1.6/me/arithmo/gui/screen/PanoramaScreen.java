/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Project
 */
package me.arithmo.gui.screen;

import java.util.List;
import me.arithmo.util.RenderingUtil;
import me.arithmo.util.render.ColorContainer;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class PanoramaScreen
extends GuiScreen {
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    protected static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
    private DynamicTexture viewportTexture = new DynamicTexture(256, 256);
    private ResourceLocation panoramaView;
    protected static int panoramaTimer;
    private static final ColorContainer c1;
    private static final ColorContainer c2;

    @Override
    public void updateScreen() {
        ++panoramaTimer;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ++panoramaTimer;
        PanoramaScreen.drawRect(-1, -1, this.width + 3, this.height + 3, Colors.getColor(0, 40));
        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
        this.drawButtons(mouseX, mouseY);
        GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
        this.drawTitle();
    }

    protected void drawTitle() {
    }

    protected void drawButtons(int mouseX, int mouseY) {
        int width = 150;
        int hei = 26;
        boolean override = false;
        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton g = (GuiButton)this.buttonList.get(i);
            if (override) {
                boolean over;
                int x = g.xPosition;
                int y = g.yPosition;
                boolean bl = over = mouseX >= x && mouseY >= y && mouseX < x + g.getButtonWidth() && mouseY < y + hei;
                if (over) {
                    PanoramaScreen.fillHorizontalGrad(x, y, width, hei, new ColorContainer(5, 40, 85, 255), new ColorContainer(0, 0, 0, 0));
                } else {
                    PanoramaScreen.fillHorizontalGrad(x, y, width, hei, new ColorContainer(0, 0, 0, 255), new ColorContainer(0, 0, 0, 0));
                }
                this.fontRendererObj.drawString(g.displayString, g.xPosition + 10, g.yPosition + hei / 2 - 3, -1);
                continue;
            }
            g.drawButton(this.mc, mouseX, mouseY);
        }
    }

    public static void fillHorizontalGrad(double x, double y, double x2, double y2, ColorContainer ColorContainer, ColorContainer c2)
    {
      float a1 = c2.getAlpha() / 255.0F;
      float r1 = c2.getRed() / 255.0F;
      float g1 = c2.getGreen() / 255.0F;
      float b1 = c2.getBlue() / 255.0F;
      float a2 = ColorContainer.getAlpha() / 255.0F;
      float r2 = ColorContainer.getRed() / 255.0F;
      float g2 = ColorContainer.getGreen() / 255.0F;
      float b2 = ColorContainer.getBlue() / 255.0F;
      Tessellator tess = Tessellator.getInstance();
      WorldRenderer wr = tess.getWorldRenderer();
      GlStateManager.disableTextures();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      
      wr.startDrawingQuads();
      wr.setColorRGBA(r1, g1, b1, a1);
      wr.addVertex(x + x2, y + y2, 0.0D);
      wr.addVertex(x + x2, y, 0.0D);
      
      wr.setColorRGBA(r2, g2, b2, a2);
      wr.addVertex(x, y, 0.0D);
      wr.addVertex(x, y + y2, 0.0D);
      
      tess.draw();
      GlStateManager.enableTextures();
    }
	protected void renderSkybox(int x, int y, float f) {
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        RenderingUtil.rectangle(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), Colors.getColor(23, 23, 23));
    }

    protected void drawPanorama(int x, int y, float partial) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)120.0f, (float)1.0f, (float)0.05f, (float)10.0f);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int byt = 8;
        for (int j = 0; j < byt * byt; ++j) {
            GlStateManager.pushMatrix();
            float var8 = ((float)(j % byt) / (float)byt - 0.5f) / 64.0f;
            float var9 = ((float)(j / byt) / (float)byt - 0.5f) / 64.0f;
            float var10 = 0.0f;
            GlStateManager.translate(var8, var9, var10);
            GlStateManager.rotate(MathHelper.sin(((float)panoramaTimer + partial) / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate((- (float)panoramaTimer + partial) * 0.1f, 0.0f, 1.0f, 0.0f);
            for (int i = 0; i < 6; ++i) {
                GlStateManager.pushMatrix();
                if (i == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (i == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (i == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (i == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (i == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[i]);
                wr.startDrawingQuads();
                wr.drawColor(16777215, 255 / (j + 1));
                float var12 = 0.0f;
                wr.addVertexWithUV(-1.0, -1.0, 1.0, 0.0f + var12, 0.0f + var12);
                wr.addVertexWithUV(1.0, -1.0, 1.0, 1.0f - var12, 0.0f + var12);
                wr.addVertexWithUV(1.0, 1.0, 1.0, 1.0f - var12, 1.0f - var12);
                wr.addVertexWithUV(-1.0, 1.0, 1.0, 0.0f + var12, 1.0f - var12);
                tess.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        wr.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    protected void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.panoramaView);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)256, (int)256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.startDrawingQuads();
        GlStateManager.disableAlpha();
        int byt = 3;
        for (int i = 0; i < byt; ++i) {
            wr.setColorRGBA(1.0f, 1.0f, 1.0f, 1.0f / (float)(i + 1));
            int width = this.width;
            int height = this.height;
            float var8 = (float)(i - byt / 2) / 256.0f;
            wr.addVertexWithUV(width, height, this.zLevel, 0.0f + var8, 1.0);
            wr.addVertexWithUV(width, 0.0, this.zLevel, 1.0f + var8, 1.0);
            wr.addVertexWithUV(0.0, 0.0, this.zLevel, 1.0f + var8, 0.0);
            wr.addVertexWithUV(0.0, height, this.zLevel, 0.0f + var8, 0.0);
        }
        tess.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    static {
        c1 = new ColorContainer(0, 0, 0, 100);
        c2 = new ColorContainer(0, 0, 0, 0);
    }
}

