// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.screen;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import exhibition.util.render.Colors;
import exhibition.util.render.ColorContainer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;

public class PanoramaScreen extends GuiScreen
{
    private static final ResourceLocation[] titlePanoramaPaths;
    protected static final ResourceLocation minecraftTitleTextures;
    private DynamicTexture viewportTexture;
    private ResourceLocation panoramaView;
    protected static int panoramaTimer;
    private static final ColorContainer c1;
    private static final ColorContainer c2;
    
    @Override
    public void updateScreen() {
        ++PanoramaScreen.panoramaTimer;
    }
    
    @Override
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.panoramaView = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        super.initGui();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ++PanoramaScreen.panoramaTimer;
        Gui.drawRect(-1, -1, this.width + 3, this.height + 3, Colors.getColor(0, 40));
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        this.drawButtons(mouseX, mouseY);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        this.drawTitle();
    }
    
    protected void drawTitle() {
    }
    
    protected void drawButtons(final int mouseX, final int mouseY) {
        final int width = 150;
        final int hei = 26;
        final boolean override = false;
        for (int i = 0; i < this.buttonList.size(); ++i) {
            final GuiButton g = this.buttonList.get(i);
            if (override) {
                final int x = g.xPosition;
                final int y = g.yPosition;
                final boolean over = mouseX >= x && mouseY >= y && mouseX < x + g.getButtonWidth() && mouseY < y + hei;
                if (over) {
                    Gui.fillHorizontalGrad(x, y, width, hei, new ColorContainer(5, 40, 85, 255), new ColorContainer(0, 0, 0, 0));
                }
                else {
                    Gui.fillHorizontalGrad(x, y, width, hei, new ColorContainer(0, 0, 0, 255), new ColorContainer(0, 0, 0, 0));
                }
                this.fontRendererObj.drawString(g.displayString, g.xPosition + 10, g.yPosition + hei / 2 - 3, -1);
            }
            else {
                g.drawButton(this.mc, mouseX, mouseY);
            }
        }
    }
    
    public void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f);
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
        final byte var6 = 8;
        for (int var7 = 0; var7 < var6 * var6; ++var7) {
            GlStateManager.pushMatrix();
            final float var8 = (var7 % var6 / var6 - 0.5f) / 64.0f;
            final float var9 = (var7 / var6 / var6 - 0.5f) / 64.0f;
            final float var10 = 0.0f;
            GlStateManager.translate(var8, var9, var10);
            GlStateManager.rotate(MathHelper.sin((PanoramaScreen.panoramaTimer + p_73970_3_) / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-(PanoramaScreen.panoramaTimer + p_73970_3_) * 0.1f, 0.0f, 1.0f, 0.0f);
            for (int var11 = 0; var11 < 6; ++var11) {
                GlStateManager.pushMatrix();
                if (var11 == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (var11 == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                this.mc.getTextureManager().bindTexture(PanoramaScreen.titlePanoramaPaths[var11]);
                var5.startDrawingQuads();
                var5.drawColor(16777215, 255 / (var7 + 1));
                final float var12 = 0.0f;
                var5.addVertexWithUV(-1.0, -1.0, 1.0, 0.0f + var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, -1.0, 1.0, 1.0f - var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, 1.0, 1.0, 1.0f - var12, 1.0f - var12);
                var5.addVertexWithUV(-1.0, 1.0, 1.0, 0.0f + var12, 1.0f - var12);
                var4.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        var5.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    
    private void rotateAndBlurSkybox(final float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.panoramaView);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator var2 = Tessellator.getInstance();
        final WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawingQuads();
        GlStateManager.disableAlpha();
        final byte var4 = 3;
        for (int var5 = 0; var5 < var4; ++var5) {
            var3.setColorRGBA(1.0f, 1.0f, 1.0f, 1.0f / (var5 + 1));
            final int var6 = this.width;
            final int var7 = this.height;
            final float var8 = (var5 - var4 / 2) / 256.0f;
            var3.addVertexWithUV(var6, var7, this.zLevel, 0.0f + var8, 1.0);
            var3.addVertexWithUV(var6, 0.0, this.zLevel, 1.0f + var8, 1.0);
            var3.addVertexWithUV(0.0, 0.0, this.zLevel, 1.0f + var8, 0.0);
            var3.addVertexWithUV(0.0, var7, this.zLevel, 0.0f + var8, 0.0);
        }
        var2.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
    
    protected void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        var5.startDrawingQuads();
        final float var6 = (this.width > this.height) ? (120.0f / this.width) : (120.0f / this.height);
        final float var7 = this.height * var6 / 256.0f;
        final float var8 = this.width * var6 / 256.0f;
        var5.setColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
        final int var9 = this.width;
        final int var10 = this.height;
        var5.addVertexWithUV(0.0, var10, this.zLevel, 0.5f - var7, 0.5f + var8);
        var5.addVertexWithUV(var9, var10, this.zLevel, 0.5f - var7, 0.5f - var8);
        var5.addVertexWithUV(var9, 0.0, this.zLevel, 0.5f + var7, 0.5f - var8);
        var5.addVertexWithUV(0.0, 0.0, this.zLevel, 0.5f + var7, 0.5f + var8);
        var4.draw();
    }
    
    static {
        titlePanoramaPaths = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        c1 = new ColorContainer(0, 0, 0, 100);
        c2 = new ColorContainer(0, 0, 0, 0);
    }
}
