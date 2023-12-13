package dev.rise.ui.mainmenu;

import dev.rise.Rise;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.ui.guitheme.GuiTheme;
import dev.rise.ui.guitheme.Theme;
import dev.rise.ui.proxy.ProxyGUI;
import dev.rise.ui.version.VersionGui;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.UIUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;

public final class MainMenu extends GuiScreen {

    //Timer used to rotate the panorama, increases every tick.
    public static int panoramaTimer = 1500;

    //Path to images
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("rise/panorama/panorama_0.png"), new ResourceLocation("rise/panorama/panorama_1.png"), new ResourceLocation("rise/panorama/panorama_2.png"), new ResourceLocation("rise/panorama/panorama_3.png"), new ResourceLocation("rise/panorama/panorama_4.png"), new ResourceLocation("rise/panorama/panorama_5.png")};

    // Font renderer
    private static final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Dreamscape 96");

    //Positions
    private ScaledResolution sr;

    private float x;
    private float y;

    private int cocks;
    private static boolean rolled;

    private float screenWidth;
    private float screenHeight;

    private float buttonWidth = 50;
    private float buttonHeight = 20;
    private float gap = 4;
    public static float smoothedX, smoothedY;
    public static float xOffSet;
    public static float yOffSet;

    private boolean easterEgg;

    public float pitch;

    private float themeX;
    private float themeY;
    private float themeWidth;
    private float themeHeight;

    //Called from the main game loop to update the screen.
    public void updateScreen() {
        ++panoramaTimer;
    }

    public void initGui() {
        System.out.println("Opened Main Menu");
        panoramaTimer = 150;

        easterEgg = Math.random() > 0.99;

        /*if (AuthGUI.sr == null) {
            while (0 < 1) {

            }
        }*/
    }

    @Override
    public void onGuiClosed() {
        mc.timer.timerSpeed = 1;
    }

    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {

        if (mc.mouseHelper != null) mc.mouseHelper.mouseGrab(false);

        mc.timer.timerSpeed = 3f;

        //Draws background
        //this.renderSkybox(mouseX, mouseY, partialTicks);
        if (Rise.INSTANCE.getGuiTheme() == null)
            Rise.INSTANCE.guiTheme = new GuiTheme();

        RenderUtil.color(Rise.INSTANCE.getGuiTheme().getThemeColor());
        mc.getTextureManager().bindTexture(new ResourceLocation("rise/backgrounds/blue.png"));

        final float scale = 1.66f;
        final float amount = height;

        if (panoramaTimer % 100 == 0) {
            xOffSet = (float) (Math.random() - 0.5f) * amount;
            yOffSet = (float) (Math.random() - 0.5f) * amount;
        }

        smoothedX = (smoothedX * 250 + xOffSet) / 259;
        smoothedY = (smoothedY * 250 + yOffSet) / 259;

        drawModalRectWithCustomSizedTexture(0, 0, width / scale + smoothedX - 150, height / scale + smoothedY - 100, width, height, width * scale, height * scale);

        // Render the rise text
        screenWidth = fontRenderer.getWidth(Rise.CLIENT_NAME);
        screenHeight = fontRenderer.getHeight(Rise.CLIENT_NAME);

        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        UIUtil.logoPosition = /*MathUtil.lerp(UIUtil.logoPosition, */sr.getScaledHeight() / 2.0F - (screenHeight / 2.0F) - 6/*, 0.2f)*/;

        x = (sr.getScaledWidth() / 2.0F) - (screenWidth / 2.0F);
        y = (sr.getScaledHeight() / 2.0F) - (screenHeight / 2.0F) - 6;

        // Box
        //RenderUtil.roundedRect(x - 10, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 - 108, 125, 145, 10, new Color(0, 0, 0, 35));

        if (easterEgg) {
            fontRenderer.drawString("RICE", x, UIUtil.logoPosition, new Color(255, 255, 255, 150).getRGB());
        } else {
            fontRenderer.drawString(Rise.CLIENT_NAME, x, UIUtil.logoPosition, new Color(255, 255, 255, 150).getRGB());
        }

        buttonWidth = 50;
        buttonHeight = 20;
        gap = 4;

        final ArrayList<String> changes = new ArrayList();

        changes.add("");
        changes.add("5.89");
        changes.add("+ Added near full Watchdog disabler");
        changes.add("+ Added new Hypixel Timer disabler");
        changes.add("+ Made Hypixel flight faster");
        changes.add("+ Made Hypixel speed faster");
        changes.add("+ Added Hypixel tower");
        changes.add("+ Added new nofall");
        changes.add("");
        changes.add("+ Fps may not be as good on Hypixel this update,");
        changes.add(" due to a complex issue, will certainly be fixed next update.");

        changes.add("");
        changes.add("5.90");
        changes.add("+ Added new Scaffold rotation mode, can be used to bypass sprint checks on some AntiCheats");
        changes.add("+ Added new Watchdog Disabler");
        changes.add("+ Added Fastest Hypixel speed");
        changes.add("+ Added new Hypixel NoVoid");
        changes.add("+ Made Hypixel fly more stable");

        if (sr.getScaledWidth() > 600 && sr.getScaledHeight() > 300) {
            CustomFont.drawString("Changelog:", 5, 5, new Color(255, 255, 255, 220).hashCode());

            for (int i = 0; i < changes.size(); i++) {
                CustomFont.drawString(changes.get(i), 5, 16 + i * 12, new Color(255, 255, 255, 220).hashCode());
            }
        }

        //Close
        //CustomFont.drawString("X", x + 103.6F + buttonWidth - 48, y + fontRenderer.getHeight() - 56, new Color(255, 255, 255, 200).hashCode());

        //Singleplayer
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Single", x + buttonWidth - 28, y + fontRenderer.getHeight() + 1 + 6, new Color(255, 255, 255, 240).hashCode());

        //Multiplayer
        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Multi", x + buttonWidth * 2 + gap - 27, y + fontRenderer.getHeight() + 6 + 1, new Color(255, 255, 255, 240).hashCode());

        //Altmanager
        RenderUtil.roundedRect(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Alt", x + buttonWidth * 2 + gap - 19, y + fontRenderer.getHeight() + buttonHeight + 10 + 3, new Color(255, 255, 255, 240).hashCode());

        //Version
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Vers", x + gap + 23, y + fontRenderer.getHeight() + buttonHeight + 10 + 3, new Color(255, 255, 255, 240).hashCode());

        //Settings
        RenderUtil.roundedRect(x, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Settings", x + buttonWidth - 35, y + fontRenderer.getHeight() + buttonHeight * 2 + 19.5, new Color(255, 255, 255, 240).hashCode());

        //Proxy
        RenderUtil.roundedRect(x + gap + buttonWidth, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString("Proxy", x + buttonWidth * 2 + gap - 28, y + fontRenderer.getHeight() + buttonHeight * 2 + 19.5, new Color(255, 255, 255, 240).hashCode());

        /*
        RenderUtil.roundedRect(4, 4, buttonWidth, buttonHeight + gap, 10, new Color(255, 255, 255, 35));
        CustomFont.drawString(mc.riseMusicTicker.shouldKeepPlaying ? "Stop music" : "Start Music", buttonWidth - CustomFont.getWidth("Stop music") - 2.5f + 4, 8 + 4, new Color(255, 255, 255, 240).hashCode());

        if (mc.riseMusicTicker.shouldKeepPlaying) {
            final String currentlyPlaying = "Currently playing: The Final Flash of Existence (SCP: SL Main Theme) by Jacek 'Fragik' Rogal";
            CustomFont.drawString(currentlyPlaying, buttonWidth + 8, 4, new Color(255, 255, 255, 240).hashCode());
        }*/

        //Quit
        /*
        RenderUtil.roundedRect(sr.getScaledWidth() - 15, 6, 10, 10, 5, new Color(255, 255, 255, 110));
        CustomFont.drawString("x", sr.getScaledWidth() - 12.5, 6, -1);
        */

        //Note
        final String message = "Made with <3 by ALLAH, zajchu, 6Sence, Tecnio, Strikeless, Nicklas, P3rZ3r0 and Auth";

        if (sr.getScaledHeight() > 300) {
            CustomFont.drawString(message, sr.getScaledWidth() - CustomFont.getWidth(message) - 2, sr.getScaledHeight() - 12.5, new Color(255, 255, 255, 180).hashCode());

            if (Rise.INSTANCE.isViaHasFailed())
                CustomFont.drawString("Version Switcher failed to initialize.", 10, sr.getScaledHeight() - 80, new Color(255, 255, 255, 180).hashCode());

            //Theme selector
            themeX = 10;
            themeY = sr.getScaledHeight() - 61;
            themeWidth = 1920f / 22;
            themeHeight = 1080f / 22;

            float offset = 0;
            boolean mouseOverAnyThemes = false;
            for (final Theme theme : Theme.values()) {
                final boolean mouseOver = mouseOver(themeX + offset, themeY, themeWidth, themeHeight, mouseX, mouseY);

                if (!mouseOverAnyThemes && mouseOver) mouseOverAnyThemes = true;

                for (int i = 1; i <= (mouseOver ? 7 : 6); i++)
                    RenderUtil.roundedRect(themeX - i + offset, themeY - i, themeWidth + i * 2, themeHeight + i * 2, 9, new Color(0, 0, 0, 6));

                final Color themeColor = Rise.INSTANCE.getGuiTheme().getThemeColor(theme);

                RenderUtil.color(themeColor);

                int opacity = 100;

                if (theme == Rise.INSTANCE.getGuiTheme().getCurrentTheme())
                    opacity = 200;
                else if (mouseOver)
                    opacity = 150;

                if (theme.opacityInMainMenu < opacity) opacity = (int) theme.opacityInMainMenu;

                RenderUtil.color(new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), opacity));

                if (mouseOver) {
                    RenderUtil.image(new ResourceLocation("rise/backgrounds/blue.png"), themeX + offset - 1, themeY - 1, themeWidth + 2, themeHeight + 2);
                    theme.nameOpacityInMainMenu += 20;
                } else {
                    RenderUtil.image(new ResourceLocation("rise/backgrounds/blue.png"), themeX + offset, themeY, themeWidth, themeHeight);
                    theme.nameOpacityInMainMenu -= 20;
                }

                theme.nameOpacityInMainMenu = Math.max(0, Math.min(225, theme.nameOpacityInMainMenu));

                if (theme.nameOpacityInMainMenu > 1)
                    CustomFont.drawCenteredString(theme.getName(), themeX + offset + 44, themeY + 2, new Color(255, 255, 255, Math.round(theme.getNameOpacityInMainMenu())).getRGB());

                offset += (themeWidth + 10);
            }

            for (final Theme theme : Theme.values()) {
                if (mouseOverAnyThemes) {
                    theme.opacityInMainMenu += 4;
                } else {
                    theme.opacityInMainMenu -= 2;
                }

                if (theme.opacityInMainMenu > 255) theme.opacityInMainMenu = 255;
                if (theme.opacityInMainMenu < 15) theme.opacityInMainMenu = 15;
            }
        }


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        // Rick roll
        if (mouseOver(x, y, fontRenderer.getWidth(Rise.CLIENT_NAME), fontRenderer.getHeight(Rise.CLIENT_NAME) - 8, mouseX, mouseY)) {
            if (++cocks > 1) {
                try {
                    openWebpage(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (Rise.INSTANCE.isFirstBoot() && !rolled) {
//            try {
//                openWebpage(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
//            } catch (final Exception e) {
//                e.printStackTrace();
//            }

            rolled = true;
        }

        float offset = 0;
        for (final Theme theme : Theme.values()) {

            if (mouseOver(themeX + offset, themeY, themeWidth, themeHeight, mouseX, mouseY)) {
                Rise.INSTANCE.getGuiTheme().setCurrentTheme(theme);
            }
            offset += (themeWidth + 10);
        }

        //Close
//        if (mouseOver(x + 103.6F + buttonWidth - 48, y + fontRenderer.getHeight() - 56, buttonWidth / 7, buttonHeight / 2 - 1, mouseX, mouseY)) {
//            System.exit(-1);
//        }

        //Singleplayer
        if (mouseOver(x, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        //Multiplayer
        if (mouseOver(x + buttonWidth + gap, y + fontRenderer.getHeight(), buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            mc.displayGuiScreen(Rise.INSTANCE.getGuiMultiplayer());
        }

        //Altmanager
        if (mouseOver(x + buttonWidth + gap, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            mc.displayGuiScreen(Rise.INSTANCE.getAltGUI() /*Rise.INSTANCE.getAltManagerGUI()*/);
        }

        //Settings
        if (mouseOver(x, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, mouseX, mouseY)) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }

        //Proxy
        if (mouseOver(x + gap + buttonWidth, y + fontRenderer.getHeight() + buttonHeight * 2 + gap * 2 + 2 + 2, buttonWidth, buttonHeight + gap, mouseX, mouseY)) {
            mc.displayGuiScreen(new ProxyGUI(this));
        }

        //Version
        if (mouseOver(x, y + fontRenderer.getHeight() + 2 + buttonHeight + gap, buttonWidth, buttonHeight + 2, mouseX, mouseY)) {
            if (!Rise.INSTANCE.isViaHasFailed()) {
                if (Rise.INSTANCE.versionSwitcher == null) {
                    Rise.INSTANCE.versionSwitcher = new VersionGui();
                }

                mc.displayGuiScreen(Rise.INSTANCE.versionSwitcher);
            }
        }

        if (mouseOver(0, 0, buttonWidth, buttonHeight + gap, mouseX, mouseY)) {
            if (!mc.riseMusicTicker.shouldKeepPlaying) {
                mc.riseMusicTicker.shouldKeepPlaying = true;
                mc.mcMusicTicker.func_181557_a();
            } else {
                mc.riseMusicTicker.shouldKeepPlaying = false;
                mc.riseMusicTicker.stopPlaying();
            }
        }

    }

    public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
        if (mouseX > posX && mouseX < posX + width) {
            return mouseY > posY && mouseY < posY + height;
        }

        return false;
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
            GlStateManager.rotate(MathHelper.sin(((float) panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

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
        this.mc.getTextureManager().bindTexture(Rise.INSTANCE.getBackgroundTexture());
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

}
