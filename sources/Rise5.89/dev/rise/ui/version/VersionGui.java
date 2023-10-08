package dev.rise.ui.version;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.ui.alt.AltThread;
import dev.rise.ui.mainmenu.MainMenu;
import dev.rise.util.math.MathUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.UIUtil;
import dev.rise.viamcp.ViaMCP;
import dev.rise.viamcp.protocols.ProtocolCollection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.awt.*;
import java.net.URI;

public final class VersionGui extends GuiScreen {

    //Path to images
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("rise/panorama/panorama_0.png"), new ResourceLocation("rise/panorama/panorama_1.png"), new ResourceLocation("rise/panorama/panorama_2.png"), new ResourceLocation("rise/panorama/panorama_3.png"), new ResourceLocation("rise/panorama/panorama_4.png"), new ResourceLocation("rise/panorama/panorama_5.png")};
    private ResourceLocation backgroundTexture;
    private DynamicTexture viewportTexture;

    // Font renderer
    private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Dreamscape 96");
    private static final TTFFontRenderer fontRenderer2 = CustomFont.FONT_MANAGER.getFont("Dreamscape 60");

    //Positions
    private ScaledResolution sr;

    private float x;
    private float y;

    private float screenWidth;
    private float screenHeight;

    private float buttonWidth = 50;
    private float buttonHeight = 20;
    private float gap = 4;

    private AltThread thread;
    private String status = "Waiting";
    int a;
    float scrollAmount = 17.139906f;
    float scrollStrength;
    public boolean enabled;

    //Called from the main game loop to update the screen.
    public void updateScreen() {
        ++MainMenu.panoramaTimer;
    }

    public void initGui() {
        this.viewportTexture = Rise.INSTANCE.getViewportTexture();
        this.backgroundTexture = Rise.INSTANCE.getBackgroundTexture();

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        screenHeight = fontRenderer.getHeight(Rise.CLIENT_NAME);
        y = (sr.getScaledHeight() / 2.0F) - (screenHeight / 2.0F) - 6;

        status = null;
        a = 150;

    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {

        //Draws background
        mc.timer.timerSpeed = 3;
        //this.renderSkybox(mouseX, mouseY, partialTicks);

        ++MainMenu.panoramaTimer;
        RenderUtil.color(Rise.INSTANCE.getGuiTheme().getThemeColor());
        mc.getTextureManager().bindTexture(new ResourceLocation("rise/backgrounds/blue.png"));

        final float scale = 1.66f;
        final float amount = 1500;

        if (MainMenu.panoramaTimer % 100 == 0) {
            MainMenu.xOffSet = (float) (Math.random() - 0.5f) * amount;
            MainMenu.yOffSet = (float) (Math.random() - 0.5f) * amount;
        }

        MainMenu.smoothedX = (MainMenu.smoothedX * 250 + MainMenu.xOffSet) / 259;
        MainMenu.smoothedY = (MainMenu.smoothedY * 250 + MainMenu.yOffSet) / 259;

        drawModalRectWithCustomSizedTexture(0, 0, width / scale + MainMenu.smoothedX - 150, height / scale + MainMenu.smoothedY - 100, width, height, width * scale, height * scale);

        // Render the rise text
        screenWidth = fontRenderer.getWidth(Rise.CLIENT_NAME);
        screenHeight = fontRenderer.getHeight(Rise.CLIENT_NAME);

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        UIUtil.logoPosition = MathUtil.lerp(UIUtil.logoPosition, sr.getScaledHeight() / 2.0F - (screenHeight / 2.0F) - 6, 0.2f);

        x = (sr.getScaledWidth() / 2.0F) - (screenWidth / 2.0F);
        y = (sr.getScaledHeight() / 2.0F) - (screenHeight / 2.0F) - 6;

        // Box
        //RenderUtil.roundedRect(x - 10, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 - 108, 125, 118, 10, new Color(0, 0, 0, 35));

        fontRenderer.drawString(Rise.CLIENT_NAME, x, UIUtil.logoPosition, new Color(255, 255, 255, 150).getRGB());

        String msg = "Scroll to change versions";

        if (thread != null) status = thread.getStatus();
        if (status != null) msg = status;

        CustomFont.drawCenteredString(msg, sr.getScaledWidth() / 2.0F, y + screenHeight - CustomFont.getHeight() - 1, new Color(255, 255, 255, 230).hashCode());

        buttonWidth = 50;
        buttonHeight = 20;
        gap = 4;

        //Draw dev.rise.ui.clickgui.impl.astolfo.buttons

        //Singleplayer
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight(), buttonWidth * 2 + gap, buttonHeight + 2, 10, this.enabled ? new Color(255, 255, 255, 35) : new Color(255, 255, 255, 10));
        //CustomFont.drawString("Single", x + buttonWidth - 27, y + fontRenderer.getHeight() + 6, new Color(255, 255, 255, 240).hashCode());

        //Back
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Back", x + buttonWidth - 24.5, y + fontRenderer.getHeight() + buttonHeight + 13, new Color(255, 255, 255, 240).hashCode());

        if (this.enabled)
            RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 20));

        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 15));
        CustomFont.drawString("Enabled", x + buttonWidth - 33.5 + buttonWidth, y + fontRenderer.getHeight() + buttonHeight + 13, (!this.enabled ? new Color(255, 255, 255, 100) : new Color(255, 255, 255)).hashCode());


        //Quit
        /*
        RenderUtil.roundedRect(sr.getScaledWidth() - 15, 6, 10, 10, 5, new Color(255, 255, 255, 110));
        CustomFont.drawString("x", sr.getScaledWidth() - 12.5, 6, -1);

        */
        //Note
        final String message = "Made with <3 by ALLAH, zajchu, 6Sence, Tecnio, Strikeless, Nicklas, P3rZ3r0 and Auth";
        final String version = Rise.CLIENT_NAME + " " + Rise.CLIENT_VERSION;

        if (sr.getScaledHeight() > 300) {
            CustomFont.drawString(message, sr.getScaledWidth() - CustomFont.getWidth(message) - 2, sr.getScaledHeight() - 12.5, new Color(255, 255, 255, 180).hashCode());
            CustomFont.drawString(version, 2, sr.getScaledHeight() - 12.5, new Color(255, 255, 255, 180).hashCode());
        }

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(x, y + fontRenderer.getHeight(), buttonWidth * 2 + gap, buttonHeight + 2);

        float offset = scrollAmount + 3;
        for (final ProtocolCollection l : ProtocolCollection.values()) {

            CustomFont.drawCenteredString(l.toString().split(" ")[0].replaceAll("_", ".").replaceAll("R", ""), sr.getScaledWidth() / 2f, y + offset, (!this.enabled ? new Color(255, 255, 255, 100) : new Color(255, 255, 255)).hashCode());

            if (y + offset > y + fontRenderer.getHeight() && y + offset < y + fontRenderer.getHeight() + buttonHeight + 2) {
                ViaMCP.getInstance().setVersion(l.getVersion().getVersion());
            }

            offset += 17;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        //Scroll
        float wheel = Mouse.getDWheel();
        if (!this.enabled) wheel = 0;

        scrollStrength += wheel / 10.0F;

        scrollAmount += scrollStrength * 0.71;

        scrollStrength *= 0.5;

        if (scrollAmount > 51.12001) {
            scrollAmount = 51.12001f;
        }

        if (scrollAmount < -426.0) {
            scrollAmount = -426.0f;
        }

        CustomFont.drawCenteredString("Using Version Switcher can cause some AntiCheats to detect you", sr.getScaledWidth() / 2f, sr.getScaledHeight() - 12.5, new Color(255, 255, 255, 200).hashCode());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (mouseOver(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            mc.displayGuiScreen(Rise.INSTANCE.getGuiMainMenu());
        } else if (mouseOver(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            this.enabled = !this.enabled;
        }

    }

    public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
        if (mouseX > posX && mouseX < posX + width) {
            return mouseY > posY && mouseY < posY + height;
        }

        return false;
    }

    public static boolean openWebpage(final URI uri) {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean getEnabled() {
        final String ip = Rise.ip;
        if (ip != null && (ip.contains("hypixel.net") && !ip.contains("ruhypixel.net") || ip.contains("2606:4700::6810:4e15")))
            return true;
        return enabled;
    }


    //Draws the main menu panorama
    private void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        final int i = 8;
        int j = 64;

        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur1();
        }

        for (int k = 0; k < j; ++k) {
            GlStateManager.pushMatrix();

            final float f = ((float) (k % i) / (float) i - 0.5F) / 64.0F;
            final float f1 = ((float) (k / i) / (float) i - 0.5F) / 64.0F;
            final float f2 = 0.0F;

            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float) MainMenu.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) MainMenu.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l) {
                GlStateManager.pushMatrix();

                if (l == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                ResourceLocation[] aresourcelocation = titlePanoramaPaths;

                if (custompanoramaproperties != null) {
                    aresourcelocation = custompanoramaproperties.getPanoramaLocations();
                }

                this.mc.getTextureManager().bindTexture(aresourcelocation[l]);
                worldrenderer.begin(7, DefaultVertexFormats.field_181709_i);
                final int i1 = 255 / (k + 1);
                final float f3 = 0.0F;
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).func_181673_a(0.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).func_181673_a(1.0D, 0.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).func_181673_a(1.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).func_181673_a(0.0D, 1.0D).color(255, 255, 255, i1).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
        GlStateManager.disableAlpha();
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        int i = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        if (custompanoramaproperties != null) {
            i = custompanoramaproperties.getBlur3();
        }

        for (int j = 0; j < i; ++j) {
            this.rotateAndBlurSkybox(p_73971_3_);
            this.rotateAndBlurSkybox(p_73971_3_);
        }

        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final float f2 = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        final float f = (float) this.height * f2 / 256.0F;
        final float f1 = (float) this.width * f2 / 256.0F;
        final int k = this.width;
        final int l = this.height;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.field_181709_i);
        worldrenderer.pos(0.0D, l, zLevel).func_181673_a(0.5F - f, 0.5F + f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(k, l, zLevel).func_181673_a(0.5F - f, 0.5F - f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(k, 0.0D, zLevel).func_181673_a(0.5F + f, 0.5F - f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, zLevel).func_181673_a(0.5F + f, 0.5F + f1).func_181666_a(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.enableAlpha();
    }

    private void rotateAndBlurSkybox(final float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.field_181709_i);
        GlStateManager.disableAlpha();
        final int i = 3;
        final int j = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();

        for (int k = 0; k < j; ++k) {
            final float f = 1.0F / (float) (k + 1);
            final int l = this.width;
            final int i1 = this.height;
            final float f1 = (float) (k - i / 2) / 256.0F;
            worldrenderer.pos(l, i1, zLevel).func_181673_a(0.0F + f1, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(l, 0.0D, zLevel).func_181673_a(1.0F + f1, 1.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, zLevel).func_181673_a(1.0F + f1, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, i1, zLevel).func_181673_a(0.0F + f1, 0.0D).func_181666_a(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }


}