package info.sigmaclient.sigma.gui.mainmenu;

import com.google.common.collect.Lists;
import info.sigmaclient.sigma.gui.JelloTextField;
import info.sigmaclient.sigma.gui.altmanager.JelloAltManager;
import info.sigmaclient.sigma.gui.clickgui.config.ConfigManagerGUI;
import info.sigmaclient.sigma.gui.configscreen.GuiConfigScreen;
import info.sigmaclient.sigma.sigma5.utils.ConfigButton;
import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;
import info.sigmaclient.sigma.premium.PremiumManager;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.swoothui.AnimatedFloat;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.utils.render.swoothui.AnimatedColor;
import info.sigmaclient.sigma.utils.UpdateLogs;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.gui.othergui.altmanager.AltManager;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static info.sigmaclient.sigma.sigma5.utils.SomeAnim.欫좯콵甐鶲㥇;
import static info.sigmaclient.sigma.modules.Module.mc;

public class SigmaGuiMainMenu extends Screen {
    private static final Random RANDOM = new Random();

    public static float animatedMouseX;
    public static float animatedMouseY;
    public static class Particle {
        public float x, y, radius, speed, ticks, opacity;

        public Particle(ScaledResolution sr, float r, float s) {
            x = new Random().nextFloat() * sr.getScaledWidth() * 1.5f;
            y = new Random().nextFloat() * sr.getScaledHeight() * 1.5f;
            ticks = new Random().nextFloat() * sr.getScaledHeight() / 2;
            radius = r;
            speed = s;
        }
    }

    public static class ParticleEngine {
        public CopyOnWriteArrayList<Particle> particles = Lists.newCopyOnWriteArrayList();
        public float lastMouseX;
        public float lastMouseY;

        public void render(float mouseX, float mouseY) {
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.color(1, 1, 1, 1);
            ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
            float xOffset = sr.getScaledWidth() / 2f - mouseX;
            float yOffset = sr.getScaledHeight() / 2f - mouseY;
            while (particles.size() < (int) (sr.getScaledWidth() / 19.2f))
                particles.add(new Particle(sr, new Random().nextFloat() * 2 + 2, new Random().nextFloat() * 5 + 5));
            List<Particle> toremove = Lists.newArrayList();
            for (Particle p : particles) {
                if (p.opacity < 32) {
                    p.opacity += 2;
                }
                if (p.opacity > 32) {
                    p.opacity = 32;
                }
                Color c = new Color(255, 255, 255, (int) p.opacity);
                RenderUtils.drawFilledCircleNoGL((float) (p.x + Math.sin(p.ticks / 2) * 50 + -xOffset / 5), (p.ticks * p.speed) * p.ticks / 10 + -yOffset / 5, p.radius * (p.opacity / 32), c.getRGB(), 2);
                p.ticks += 0.05;// +(0.005*1.777*(GLUtils.getMouseX()-lastMouseX) + 0.005*(GLUtils.getMouseY()-lastMouseY));
                if (((p.ticks * p.speed) * p.ticks / 10 + -yOffset / 5) > sr.getScaledHeight() || ((p.ticks * p.speed) * p.ticks / 10 + -yOffset / 5) < 0 || (p.x + Math.sin(p.ticks / 2) * 50 + -xOffset / 5) > sr.getScaledWidth() || (p.x + Math.sin(p.ticks / 2) * 50 + -xOffset / 5) < 0) {
                    toremove.add(p);
                }
            }

            particles.removeAll(toremove);
        }
    }

    public float zoom1 = 1, zoom2 = 1, zoom3 = 1, zoom4 = 1, zoom5 = 1;

    public ParticleEngine pe = new ParticleEngine();
    public static double introTrans;

    public boolean isMouseHoveringRect1(float x, float y, float width, float height, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }

    public boolean isMouseHoveringRect2(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }

    public SigmaGuiMainMenu() {
        super(new StringTextComponent("MainMenu-Jello"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private final AnimatedColor exitButtonColor = new AnimatedColor(new Color(255, 255, 255, 130), new Color(255, 255, 255, 190), 10);
    private AnimatedFloat exitButtonBarWidth = null;
    private final AnimatedColor changelogButtonColor = new AnimatedColor(new Color(255, 255, 255, 130), new Color(255, 255, 255, 190), 10);
    private AnimatedFloat changelogButtonBarWidth = null;

    public static float renderYOffsets = 0;
    public static float renderAlpha = 1f;

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        if (exitButtonBarWidth == null) {
            exitButtonBarWidth = new AnimatedFloat(0f, (float) JelloFontUtil.jelloFont20.getStringWidth("Exit"), 0.26f, true);
            exitButtonBarWidth.switchToFrom(); // bar to 0
        }

        if (changelogButtonBarWidth == null) {
            changelogButtonBarWidth = new AnimatedFloat(0f, (float) JelloFontUtil.jelloFont20.getStringWidth("Changelog"), 0.26f, true);
            changelogButtonBarWidth.switchToFrom(); // bar to 0
        }
    }

    private void drawTexture(float x, float y, int u, int v, float w, float h, float w2, float h2) {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderUtils.drawTextureCustom(x, y, w, h, 1f);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(animatedMouseX < 0){
            animatedMouseX = 0;
        }
        if(animatedMouseX > width){
            animatedMouseX = width;
        }
        if(animatedMouseY < 0){
            animatedMouseY = 0;
        }
        if(animatedMouseY > height){
            animatedMouseY = height;
        }
        if (introTrans > 0) {
            introTrans -= introTrans / 7;
        }
        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float m1 = sr.getScaledWidth() / 960f * 3840 * 0.5f;
        float h1 = sr.getScaledHeight() / 501f * 1080 * 0.5f;
        float m = 1.0f;
        float addM = m1 / 2f - sr.getScaledWidth() / 2F;
        float addH = h1 / 2f - sr.getScaledHeight() / 2F;
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigmang/images/background.png"));
        drawTexture(-addM + -(animatedMouseX / sr.getScaledWidth() - 0.5f) * 2 * addM, -addH + -(animatedMouseY / sr.getScaledHeight() - 0.5f) * 2 * addH, 0, 0, m1 * m, h1 * m, m1 * m, h1 * m);

        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigmang/images/middle.png"));
        drawTexture(-addM + -(animatedMouseX * 0.9f / sr.getScaledWidth() - 0.5f) * 2 * addM, -addH + -(animatedMouseY * 0.9f / sr.getScaledHeight() - 0.5f) * 2 * addH, 0, 0, m1 * m, h1 * m, m1 * m, h1 * m);

        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigmang/images/foreground.png"));
        drawTexture(-addM + -(animatedMouseX * 0.8f / sr.getScaledWidth() - 0.5f) * 2 * addM, -addH + -(animatedMouseY * 0.8f / sr.getScaledHeight() - 0.5f) * 2 * addH, 0, 0, m1 * m, h1 * m, m1 * m, h1 * m);

//        final 웎璧㮃㞈㔢㹔 竬픓贞곻핇 = 뚔弻缰硙柿.竬픓贞곻핇;
//        final int 䈔褕Ꮤ䎰ᔎ㢸 = 竬픓贞곻핇.䈔褕Ꮤ䎰ᔎ㢸();
//        final int 괠꿩罡㢸쥅聛 = 竬픓贞곻핇.괠꿩罡㢸쥅聛();
//        if (좯綋쥅䄟弻.䡸綋釒欫属 > 1.0f) {
//            final 웎璧㮃㞈㔢㹔 뼢뗴啖㻣ಽ = 뚔弻缰硙柿.뼢뗴啖㻣ಽ;
//        }
//        㕠鄡呓ᢻ낛.drawTextureSigma(
//                (float)(this.㦖缰뫤랾퉧() / 2 - 䈔褕Ꮤ䎰ᔎ㢸 / 2),
//                (float)(this.柿ᜄ顸佉쥦() / 2 - 괠꿩罡㢸쥅聛),
//                (float)䈔褕Ꮤ䎰ᔎ㢸,
//                (float)괠꿩罡㢸쥅聛,
//                뚔弻缰硙柿.竬픓贞곻핇,
//                堧鏟ᔎ㕠釒.霥瀳놣㠠釒(핇댠䂷呓贞.white.哺卫콗鱀ಽ, n)
//        );

        if(renderAlpha != 0) {
            GL11.glTranslatef(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
            float s = Math.max(1 - (1 - renderAlpha) * 0.16f, 0);
            GL11.glScalef(s, s, 1f);
            GL11.glTranslatef(sr.getScaledWidth() / -2f, sr.getScaledHeight() / -2f, 0);
        }

        int intAlpha = (int)(255 * renderAlpha);
        pe.render(-animatedMouseX, -animatedMouseY);
        GlStateManager.color(1, 1, 1, renderAlpha);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/jellologo.png"));
        drawTexture(sr.getScaledWidth() / 2F - 323 / 4f, sr.getScaledHeight() / 2F - 161 / 2f + 11 - 32 / 2f + 0.5f + renderYOffsets, 0, 0, 323 / 2F, 161 / 2F, 323 / 2F, 161 / 2F);

        RenderUtils.drawFilledCircleNoGL(sr.getScaledWidth() - 7.5F - 15 - 5, 12 + 7.5F + 5 + renderYOffsets, 15, new Color(255, 255, 255, (int)(renderAlpha * 110)).getRGB(), 1);

        JelloFontUtil.jelloFont20.drawString(PremiumManager.userName, sr.getScaledWidth() - 50 - 4 - JelloFontUtil.jelloFont20.getStringWidth(PremiumManager.userName) + 2, renderYOffsets+ 8 + 6 + 5 + 1 + 2, new Color(255, 255, 255, (int)(renderAlpha * 160)).getRGB());
        boolean premium = PremiumManager.isPremium;
        float offset1 = premium ? -(187 * 0.5f) : 0;
        if(!premium) {
            GlStateManager.color(1, 1, 1, renderAlpha);
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/title.png"));
            drawTexture(15, 15.5f + renderYOffsets, 0, 0, 187 * 0.5f, 36 * 0.5f, 187 * 0.5f, 36 * 0.5f);
        }

        if (isMouseHoveringRect1(125F + offset1, (float) (8 + 6 + 5 + 3), (float) JelloFontUtil.jelloFont20.getStringWidth("Exit"), JelloFontUtil.jelloFont20.getHeight(), mouseX, mouseY)) {
            exitButtonColor.switchToTo(); // color to white
            exitButtonBarWidth.switchToTo(); // bar to full
        } else {
            exitButtonColor.switchToFrom(); // color to grayed
            exitButtonBarWidth.switchToFrom(); // bar to 0
        }

        JelloFontUtil.jelloFont20.drawString("Exit", 125 + offset1, 8 + 6 + 5 + 3 + renderYOffsets, ColorUtils.reAlpha(exitButtonColor.getColor(), exitButtonColor.getColor().getAlpha() / 255f * renderAlpha).getRGB());

        // draw bar under exit button
        float barWidth = exitButtonBarWidth.getValue();
        RenderUtils.drawRect(
                125 + exitButtonBarWidth.to / 2 - barWidth / 2 + offset1,
                8 + 6 + 5 + 3 + JelloFontUtil.jelloFont20.getHeight() + 2 + renderYOffsets,
                125 + exitButtonBarWidth.to / 2 + barWidth / 2 + offset1,
                8 + 6 + 5 + 3 + JelloFontUtil.jelloFont20.getHeight() + 2 + 1 + renderYOffsets,
                new Color(255, 255, 255, 130).getRGB()
        );

        if (isMouseHoveringRect1(155F + offset1, (float) (8 + 6 + 5 + 3), (float) JelloFontUtil.jelloFont20.getStringWidth("Changelog"), JelloFontUtil.jelloFont20.getHeight(), mouseX, mouseY)) {
            changelogButtonColor.switchToTo(); // color to white
            changelogButtonBarWidth.switchToTo(); // bar to full
        } else {
            changelogButtonColor.switchToFrom(); // color to grayed
            changelogButtonBarWidth.switchToFrom(); // bar to 0
        }

        JelloFontUtil.jelloFont20.drawString("Changelog", 155 + offset1, 8 + 6 + 5 + 3 + renderYOffsets, ColorUtils.reAlpha(changelogButtonColor.getColor(), changelogButtonColor.getColor().getAlpha() / 255f * renderAlpha).getRGB());
        // draw bar under exit button
        barWidth = changelogButtonBarWidth.getValue();
        RenderUtils.drawRect(
                155 + changelogButtonBarWidth.to / 2 - barWidth / 2 + offset1,
                8 + 6 + 5 + 3 + JelloFontUtil.jelloFont20.getHeight() + 2 + renderYOffsets,
                155 + changelogButtonBarWidth.to / 2 + barWidth / 2 + offset1,
                8 + 6 + 5 + 3 + JelloFontUtil.jelloFont20.getHeight() + 2 + 1 + renderYOffsets,
                new Color(255, 255, 255, 130).getRGB()
        );

        float offset = -16 + (float) sr.getScaledWidth() / 2 - 289 / 2f + 8;
        float height = (float) sr.getScaledHeight() / 2 + 29 / 2f - 8 + 0.5f;

        JelloFontUtil.jelloFont20.drawNoBSStringWithBloom("© Sigma Prod", 5, sr.getScaledHeight() - 5 - JelloFontUtil.jelloFont20.getHeight() + renderYOffsets, -1,0.4f);
        String str = "Jello for Sigma 5.0.0 b" + SigmaNG.getClientVersion() + " - 1.8 to 1.19";
        JelloFontUtil.jelloFont20.drawNoBSStringWithBloom(str, sr.getScaledWidth() - 0.5f - JelloFontUtil.jelloFont20.getStringWidth(str) - 2, sr.getScaledHeight() - 5 - JelloFontUtil.jelloFont20.getHeight() + 1 + renderYOffsets, -1,0.4f);

        GlStateManager.pushMatrix();
        if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
            if (zoom1 < 1.2) zoom1 += 0.0500001;
        } else {
            if (zoom1 > 1) {
                zoom1 -= 0.0500001;
            }
        }
        if (zoom1 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1.2, zoom1), Math.min(1.2, zoom1), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            GlStateManager.color(1, 1, 1, 1);
        }
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/singleplayer.png"));
        drawTexture(offset, height, 0, 0, 64, 64, 64, 64);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (zoom1 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1, zoom1 - .2), Math.min(1, zoom1 - .2), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            JelloFontUtil.jelloFont24.drawNoBSStringWithBloom("Singleplayer", offset + 32 - JelloFontUtil.jelloFont24.getStringWidth("Singleplayer") / 2F + 0.5f, height + 140 / 2 + 1 - 4, new Color(255 / 255f, 255 / 255f, 255 / 255f, Math.max(0, Math.min(1, 0.5f + (zoom1 - 1) * 2.5f)) * 0.6f).getRGB(),0.4f);
        }
        GlStateManager.popMatrix();



        offset += 122 / 2f;
        GlStateManager.pushMatrix();
        if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
            if (zoom2 < 1.2) zoom2 += 0.0500001;
        } else {
            if (zoom2 > 1) {
                zoom2 -= 0.0500001;
            }
        }
        if (zoom2 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1.2, zoom2), Math.min(1.2, zoom2), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            GlStateManager.color(1, 1, 1, 1);
        }
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/multiplayer.png"));
        drawTexture(offset, height, 0, 0, 64, 64, 64, 64);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (zoom2 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1, zoom2 - .2), Math.min(1, zoom2 - .2), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            JelloFontUtil.jelloFont24.drawNoBSStringWithBloom("Multiplayer", offset + 32 - JelloFontUtil.jelloFont24.getStringWidth("Multiplayer") / 2F + 0.5f, height + 140 / 2 + 1 - 4, new Color(255 / 255f, 255 / 255f, 255 / 255f, Math.max(0, Math.min(1, 0.5f + (zoom2 - 1) * 2.5f)) * 0.6f).getRGB(),0.4f);
        }
        GlStateManager.popMatrix();



        offset += 122 / 2f;
        GlStateManager.pushMatrix();
        if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
            if (zoom5 < 1.2) zoom5 += 0.0500001;
        } else {
            if (zoom5 > 1) {
                zoom5 -= 0.0500001;
            }
        }

        if (zoom5 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1.2, zoom5), Math.min(1.2, zoom5), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            GlStateManager.color(1, 1, 1, 1);
        }

        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/shop.png"));
        drawTexture(offset, height, 0, 0, 64, 64, 64, 64);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (zoom5 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1, zoom5 - .2), Math.min(1, zoom5 - .2), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            JelloFontUtil.jelloFont24.drawNoBSStringWithBloom("Realms", offset + 32 - JelloFontUtil.jelloFont24.getStringWidth("Realms") / 2F + 0.5f, height + 140 / 2 + 1 - 4, new Color(255 / 255f, 255 / 255f, 255 / 255f, Math.max(0, Math.min(1, 0.5f + (zoom5 - 1) * 2.5f)) * 0.6f).getRGB(),0.4f);
        }
        GlStateManager.popMatrix();



        offset += 122 / 2f;
        GlStateManager.pushMatrix();
        if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
            if (zoom4 < 1.2) zoom4 += 0.0500001;
        } else {
            if (zoom4 > 1) {
                zoom4 -= 0.0500001;
            }
        }
        if (zoom4 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1.2, zoom4), Math.min(1.2, zoom4), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            GlStateManager.color(1, 1, 1, 1);
        }
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/options.png"));
        drawTexture(offset, height, 0, 0, 64, 64, 64, 64);
        GlStateManager.popMatrix();
        
        GlStateManager.pushMatrix();
        if (zoom4 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1, zoom4 - .2), Math.min(1, zoom4 - .2), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            JelloFontUtil.jelloFont24.drawNoBSStringWithBloom("Settings", offset + 32 - JelloFontUtil.jelloFont24.getStringWidth("Settings") / 2F + 0.5f, height + 140 / 2 + 1 - 4, new Color(255 / 255f, 255 / 255f, 255 / 255f, Math.max(0, Math.min(1, 0.5f + (zoom4 - 1) * 2.5f)) * 0.6f).getRGB(),0.4f);
        }
        GlStateManager.popMatrix();



        offset += 122 / 2f;
        GlStateManager.pushMatrix();
        if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
            if (zoom3 < 1.2) zoom3 += 0.0500001;
        } else {
            if (zoom3 > 1) {
                zoom3 -= 0.0500001;
            }
        }
        if (zoom3 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1.2, zoom3), Math.min(1.2, zoom3), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            GlStateManager.color(1, 1, 1, 1);
        }
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/alt.png"));
        drawTexture(offset, height, 0, 0, 64, 64, 64, 64);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        if (zoom3 > 1) {
            GlStateManager.translate(offset + 32, height + 64, 0);
            GlStateManager.scale(Math.min(1, zoom3 - .2), Math.min(1, zoom3 - .2), 1);
            GlStateManager.translate(-(offset + 32), -(height + 64), 0);
            JelloFontUtil.jelloFont24.drawNoBSStringWithBloom("Alt Manager", offset + 32 - JelloFontUtil.jelloFont24.getStringWidth("Alt Manager") / 2F + 0.5f, height + 140 / 2 + 1 - 4, new Color(255 / 255f, 255 / 255f, 255 / 255f, Math.max(0, Math.min(1, 0.5f + (zoom3 - 1) * 2.5f)) * 0.6f).getRGB(),0.4f);
        }
        GlStateManager.popMatrix();

        animatedMouseX += (float) (((mouseX - animatedMouseX) / 3) + 0.5);
        animatedMouseY += (float) (((mouseY - animatedMouseY) / 3) + 0.5);

        GL11.glPopMatrix();
    }

    private void switchToRealms() {
        RealmsBridgeScreen realmsbridge = new RealmsBridgeScreen();
        mc.displayGuiScreen(realmsbridge);
    }
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     *
     * @return
     */
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());

        if (mouseButton == 0) {
            boolean premium = PremiumManager.isPremium;
            float offset1 = premium ? -(187 * 0.5f) : 0;
            if (ClickUtils.isClickableWithRect(155 + offset1, 8 + 6 + 5 + 3, JelloFontUtil.jelloFont20.getStringWidth("Changelog"), JelloFontUtil.jelloFont20.getHeight(), mouseX, mouseY)) {
                Minecraft.getInstance().displayGuiScreen(new SigmaChangelogGui(this));
                return false;
            }

            float offset = -16 + sr.getScaledWidth() / 2F - 289 / 2f + 8;
            float height = sr.getScaledHeight() / 2F + 29 / 2f - 8;

            if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
                mc.displayGuiScreen(new WorldSelectionScreen(this));
                return false;
            }
            if(!PremiumManager.isPremium && ClickUtils.isClickableWithRect(15, 15.5f + renderYOffsets,  187 * 0.5f, 36 * 0.5f, mouseX,mouseY)){
                mc.displayGuiScreen(new SigmaGetPremiumGui(this));
                return false;
            }
            offset += 122 / 2f;
            if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
                Minecraft.getInstance().displayGuiScreen(new MultiplayerScreen(this));
                return false;
            }
            offset += 122 / 2f;
            if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
                switchToRealms();
                return false;
            }
            offset += 122 / 2f;
            if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
                mc.displayGuiScreen(new OptionsScreen(this, mc.gameSettings));
                return false;
            }
            offset += 122 / 2f;
            if (isMouseHoveringRect1(offset + 4, height + 4, 64 - 8, 64 - 8, mouseX, mouseY)) {
                mc.displayGuiScreen(new JelloAltManager(this));
            }
            if (isMouseHoveringRect1(125F + offset1, (float) (8 + 6 + 5 + 3), (float) JelloFontUtil.jelloFont20.getStringWidth("Exit"), JelloFontUtil.jelloFont20.getHeight(), mouseX, mouseY)) {
                Minecraft.getInstance().displayGuiScreen(new SigmaCloseGameGui(this));
                return false;
            }

            if (isMouseHoveringRect1((float) (sr.getScaledWidth() - 50 - 8 - JelloFontUtil.jelloFont20.getStringWidth(PremiumManager.userName)), (float) (8 + 6 + 5 + 1), (float) JelloFontUtil.jelloFont20.getStringWidth(PremiumManager.userName), JelloFontUtil.jelloFont20.getHeight(), mouseX, mouseY)) {
                Minecraft.getInstance().displayGuiScreen(new SigmaRegisterGui(this));
                return false;
            }
        }

        return false;
    }



    private static class SigmaChangelogGui extends BlurredSigmaMainMenuGui {
        float scroll = 0;
        float maxScroll = 0;
        ArrayList<UpdateLogs.UpdateLog> logs = new ArrayList<>();
        public SigmaChangelogGui(SigmaGuiMainMenu parentScreen) {
            super(parentScreen);
            closeOnEsc = false;
            closeOnLClick = true;
            closeOnRClick = false;
            maxScroll = 0;
            scroll = 0;
            if(UpdateLogs.logs.isEmpty())
                UpdateLogs.loadLogs();
            int s = 50, w = JelloFontUtil.jelloFont20.getHeight() + 3;
            for (Map.Entry<String, UpdateLogs.UpdateLog> ss : UpdateLogs.logs.entrySet()) {
                s += JelloFontUtil.jelloFontBold40.getHeight() + 7;
                UpdateLogs.UpdateLog log = ss.getValue();
                for(String ignored : log.strings)
                    s += w;
                s += 50;
            }
            for (Map.Entry<String, UpdateLogs.UpdateLog> ss : UpdateLogs.logs.entrySet()) {
                this.logs.add(ss.getValue());
            }
            logs.sort((a,b)->b.id - a.id);
            maxScroll = s + 10;
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
            if(delta != 0){
                scroll += delta > 0 ? -15 : 15;
                if(scroll < 0) scroll = 0;
                if(scroll > maxScroll) scroll = maxScroll;
            }
            return super.mouseScrolled(mouseX, mouseY, delta);
        }

        @Override
        public void drawBlurredScreen(int mouseX, int mouseY, float partialTicks) {
            float aa = getBlurAlphaValue();
            int offset = (int) (100 - aa * 100 - scroll);
            JelloFontUtil.jelloFont36.drawNoBSString("Changelog", 50, 53 + offset, new Color(255, 255, 255, (int)(255 * aa)).getRGB());
            JelloFontUtil.jelloFont22.drawNoBSString("You're currently using Sigma 5.0.0", 50, 53 + 22 + offset, new Color(200, 200, 200, (int)(255 * aa)).getRGB());

            int s = 50, w = JelloFontUtil.jelloFont20.getHeight() + 3;
            for (UpdateLogs.UpdateLog log : logs) {
                float animOff = Math.min(Math.max(s * 1 * (1 - aa), 0), 100);
                float animOffAlpha = 1 - Math.min(Math.max(s / 100f * (1 - aa), 0), 1);
                JelloFontUtil.jelloFontBold40.drawNoBSString(log.title, 50, 53 + s + offset + animOff, new Color(255, 255, 255, (int) (255 * aa * animOffAlpha)).getRGB());
                s += JelloFontUtil.jelloFontBold40.getHeight() + 7;
                for(String l : log.strings) {
                    animOff = Math.min(Math.max(s * 1 * (1 - aa), 0), 100);
                    animOffAlpha = 1 - Math.min(Math.max(s / 100f * (1 - aa), 0), 1);
                    JelloFontUtil.jelloFont20.drawNoBSString("- " + l, 50 + 5, 53 + s + offset + animOff, new Color(230, 230, 230, (int) (255 * aa * animOffAlpha)).getRGB());
                    s += w;
                }
                s += 50;
            }
        }
    }

    private static class SigmaGetPremiumGui extends BlurredSigmaMainMenuGui {
        JelloTextField re;
        public static boolean registing = false;
        public static boolean close = false;
        public SigmaGetPremiumGui(SigmaGuiMainMenu parentScreen) {
            super(parentScreen);
            registing = false;
            close = false;
            re = new JelloTextField(0, mc.fontRenderer, 0, 0, 150, 30, "Premium Code");
            closeOnEsc = true;
            closeOnLClick = false;
            closeOnRClick = false;
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
            return super.mouseScrolled(mouseX, mouseY, delta);
        }

        @Override
        public void drawBlurredScreen(int mouseX, int mouseY, float partialTicks) {
            float aa = getBlurAlphaValue();
            int offset = (int) (100 - aa * 100);
            JelloFontUtil.jelloFont36.drawNoBSString("Redeem Premium", 50, 53 + offset, new Color(255, 255, 255, (int)(255 * aa)).getRGB());
            JelloFontUtil.jelloFont22.drawNoBSString("Visit SigmaNG Discord for more info", 50, 53 + 22 + offset, new Color(200, 200, 200, (int)(255 * aa)).getRGB());
            re.y = 53 + 22 + 30 + offset;
            re.x = 50 + 10;
            re.drawTextBoxCustom(aa, 10, 200, 235);
            if(close){
                isClosing = true;
            }
            if(!registing)
                JelloFontUtil.jelloFont18.drawNoBSString("Redeem", 50 + 13, re.y + 50, new Color(255, 255, 255, (int)(255 * aa)).getRGB());
            ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
            if(ss) {
                float centerX = sr.getScaledWidth() / 2f;
                float centerY = sr.getScaledHeight() / 2f;
                int sX = (int) (centerX - 140), sY = (int) (centerY - 175 / 2f+10);
                int endX = (int) (centerX - 140 + 140 * 2), endY = (int) (centerY - 175 / 2f+10 + 165);
                float scale2 = Math.min(1, alertAnimation.getAnim() * alertAnimation.getAnim());
                if((int)(20 * scale2) != 0){
//                    Sigma5BlurUtils.vblur(20 * scale2);
                }
                GL11.glTranslatef(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
//                float s = Math.max(1 - (1 - renderAlpha) * 0.16f, 0);
                GL11.glScalef(scale2, scale2, 1f);
                GL11.glTranslatef(sr.getScaledWidth() / -2f, sr.getScaledHeight() / -2f, 0);

                RenderUtils.drawRoundShadow(centerX - 80, centerY - 100 / 2f, 80 * 2, 100, new Color(1,1,1,aa * scale2).getRGB());

                JelloFontUtil.jelloFontBold38.drawNoBSString(title, centerX - 80 + 12, centerY - 100 / 2f + 12, new Color(137,137,136,(int)(aa * scale2 * 255)).getRGB());
                JelloFontUtil.jelloFont18.drawNoBSString(text, centerX - 80 + 12, centerY - 100 / 2f + 12 + 30, new Color(100,100,100,(int)(aa * scale2 * 255)).getRGB());

                RenderUtils.drawRoundShadow(centerX - 80 + 12, centerY - 100 / 2f + 12 + 30 + 30, 50, 20, new Color(59, 153, 253,(int)(aa * scale2 * 255)).getRGB());
                JelloFontUtil.jelloFontBold20.drawNoBSString("close", centerX - 80 + 12 + 7, centerY - 100 / 2f + 12 + 30 + 30 + 6, new Color(255,255,255,(int)(aa * scale2 * 255)).getRGB());

            }
        }

        @Override
        public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
            re.keyReleased(keyCode, scanCode, modifiers);
            return super.keyReleased(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            re.keyPressed(keyCode, scanCode, modifiers);
            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
            if(re.mouseClicked(mouseX, mouseY, mouseButton)){
                return true;
            }
            if(PremiumManager.userName.isEmpty() || PremiumManager.password.isEmpty()){
                return false;
            }
            if(ClickUtils.isClickableWithRect(50 + 13, re.y + 50, 50, 10, mouseX, mouseY)){
//                if(re.getText().equals("Nyax-ontp-iam-1efg")){
//                    PremiumManager.isPremium = true;
//                    SigmaNG.getSigmaNG().premiumManager.save(PremiumManager.userName, "Nyax-ontp-iam-1efg");
//                    SigmaNG.getSigmaNG().premiumManager.init();
//                }
                registing = true;
//                PremiumManager.reset();
                PremiumManager.register(
                        PremiumManager.userName,
                        PremiumManager.password,
                        re.getText(),
                        ()->{
                            registing = false;
                            alert("OK!", "Now you are premium user");
                        },
                        ()->{
                            alert("Failed!", "Maybe duplicate username or bad key");
                        }
                );
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        public boolean charTyped(char codePoint, int modifiers) {
            if((codePoint >= 'A' && codePoint <= 'z') || (codePoint >= '0' && codePoint <= '9') || codePoint == '-'){
                re.charTyped(codePoint, modifiers);
            }
            return super.charTyped(codePoint, modifiers);
        }
        Sigma5AnimationUtil alertAnimation = new Sigma5AnimationUtil(300, 300);
        boolean showAlert = false, ss = false;
        String title, text;
        public void alert(String title, String text){
            this.title = title;
            this.text = text;
            alertAnimation = new Sigma5AnimationUtil(300, 300);
            showAlert = true;
            ss = true;
        }
    }

    private static class SigmaRegisterGui extends BlurredSigmaMainMenuGui {
        JelloTextField username;
        JelloTextField password;
        ConfigButton login, register, forgotPassword;
        Sigma5AnimationUtil alertAnimation = new Sigma5AnimationUtil(300, 300);
        boolean showAlert = false, ss = false;
        String title, text;
        public void alert(String title, String text){
            this.title = title;
            this.text = text;
            alertAnimation = new Sigma5AnimationUtil(300, 300);
            showAlert = true;
            ss = true;
        }
        public SigmaRegisterGui(SigmaGuiMainMenu parentScreen) {
            super(parentScreen);
            ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
            username = new JelloTextField(0, mc.fontRenderer, 0, 0, 150, 30, "Username");
            password = new JelloTextField(0, mc.fontRenderer, 0, 0, 150, 30, "Password");
            username.setMaxStringLength(20);
            password.setMaxStringLength(20);
            float centerX = sr.getScaledWidth() / 2f;
            float centerY = sr.getScaledHeight() / 2f;
            int sX = (int) (centerX - 140), sY = (int) (centerY - 175 / 2f+10);
            int endX = (int) (centerX - 140 + 140 * 2), endY = (int) (centerY - 175 / 2f+10 + 165);
            login = new ConfigButton(endX - 45, endY - 45 + 5, 30, 15, "Login", (n)->{
                boolean legit = true;
                if(username.getText().trim().length() == 0 || password.getText().trim().length() == 0){
                    alert("Error", "Empty?");
                    return;
                }
                for(char c : username.getText().toCharArray()){
                    if(!((c >= 'A' && c <= 'z') || (c >= '0' && c <= '9'))){
                        legit = false;
                        break;
                    }
                }
                if(!legit){
                    alert("Error", "Bad username");
                    return;
                }
                boolean legit2 = true;
                for(char c : password.getText().toCharArray()){
                    if(!((c >= 'A' && c <= 'z') || (c >= '0' && c <= '9'))){
                        legit2 = false;
                        break;
                    }
                }
                if(!legit2){
                    alert("Error", "Bad password");
                    return;
                }
                alert("Success!", "Log in " + username.getText());
                PremiumManager.userName = username.getText();
                PremiumManager.password = password.getText();
                PremiumManager.isPremium = false;
                SigmaNG.getSigmaNG().premiumManager.save(PremiumManager.userName, PremiumManager.password);
                PremiumManager.login(PremiumManager.userName, PremiumManager.password);
            }, JelloFontUtil.jelloFont22);
            register = new ConfigButton(sX + 45, endY - 45, 30, 15, "Register", (n)->{

            }, JelloFontUtil.jelloFont16);
            forgotPassword = new ConfigButton(sX + 45, endY - 45 + 10 + 3, 30, 15, "Forgot Password?", (n)->{

            }, JelloFontUtil.jelloFont16);
            closeOnEsc = true;
            closeOnLClick = false;
            closeOnRClick = false;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            password.keyPressed(keyCode, scanCode, modifiers);
            username.keyPressed(keyCode, scanCode, modifiers);
            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
            password.keyReleased(keyCode, scanCode, modifiers);
            username.keyReleased(keyCode, scanCode, modifiers);
            return super.keyReleased(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean charTyped(char codePoint, int modifiers) {
            if((codePoint >= 'A' && codePoint <= 'z') ||(codePoint >= '0' && codePoint <= '9')){
                password.charTyped(codePoint, modifiers);
                username.charTyped(codePoint, modifiers);
            }
            return super.charTyped(codePoint, modifiers);
        }
        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
            return super.mouseScrolled(mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
            if(showAlert){
                showAlert = false;
                return false;
            }
            if(login.mouseClicked(mouseX, mouseY, mouseButton)){
                return false;
            }
            if(password.mouseClicked(mouseX, mouseY, mouseButton)){
            }
            if(username.mouseClicked(mouseX, mouseY, mouseButton)){
            }
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        public void drawBlurredScreen(int mouseX, int mouseY, float partialTicks) {
            float aa = getBlurAlphaValue();
            ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float m1 = sr.getScaledWidth() / 960f * 3840 * 0.5f;
            float h1 = sr.getScaledHeight() / 501f * 1080 * 0.5f;
            float m = 1.0f;
            float addM = m1 / 2f - sr.getScaledWidth() / 2F;
            float addH = h1 / 2f - sr.getScaledHeight() / 2F;
            float scale = aa;
            GL11.glPushMatrix();
            float centerX = sr.getScaledWidth() / 2f;
            float centerY = sr.getScaledHeight() / 2f;
                GL11.glTranslatef(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
//                float s = Math.max(1 - (1 - renderAlpha) * 0.16f, 0);
                GL11.glScalef(scale, scale, 1f);
                GL11.glTranslatef(sr.getScaledWidth() / -2f, sr.getScaledHeight() / -2f, 0);
            RenderUtils.drawRoundShadow(centerX - 140, centerY - 175 / 2f+10, 140 * 2, 165, new Color(1,1,1,aa).getRGB());
                RenderUtils.drawTextureLocationZoom(centerX - 120, centerY - 90 + 30 + 2, 295 / 4f, 296 / 4f, "mentalfrostbyte/sigma2", new Color(1,1,1,aa).getRGB());
            username.x = (int) (centerX - 120 + 295 * 0.25f + 10 + 5);
            password.x = username.x;
            username.y = (int) (centerY - 90 + 30 + 5 + 25);
            password.y = (int) (centerY - 90 + 30 + 5 + 55);
            JelloFontUtil.jelloFontBold38.drawSmoothString("Login", username.x, centerY - 90 + 30 + 5, new Color(137,137,136,(int)(aa * 255)).getRGB());
//            username.x = centerX - 120 + 295 * 0.25f + 10 + 5;
            username.drawTextBoxCustom(aa, 10, 230, 125);
            password.drawTextBoxCustom(aa, 10, 230, 125);
            if(login != null){
                login.myRender(mouseX, mouseY, new Color(59, 153, 253), aa);
            }
            if(register != null){
                register.myRender(mouseX, mouseY, new Color(59, 153, 253), aa);
            }
            if(forgotPassword != null){
                forgotPassword.myRender(mouseX, mouseY, new Color(59, 153, 253), aa);
            }
                alertAnimation.animTo(showAlert ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
            if(ss) {
                float scale2 = Math.min(1, alertAnimation.getAnim() * alertAnimation.getAnim());
                if((int)(20 * scale2) != 0){
//                    Sigma5BlurUtils.vblur(20 * scale2);
                }
                GL11.glTranslatef(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
//                float s = Math.max(1 - (1 - renderAlpha) * 0.16f, 0);
                GL11.glScalef(scale2, scale2, 1f);
                GL11.glTranslatef(sr.getScaledWidth() / -2f, sr.getScaledHeight() / -2f, 0);

                RenderUtils.drawRoundShadow(centerX - 80, centerY - 100 / 2f, 80 * 2, 100, new Color(1,1,1,aa * scale2).getRGB());

                JelloFontUtil.jelloFontBold38.drawNoBSString(title, centerX - 80 + 12, centerY - 100 / 2f + 12, new Color(137,137,136,(int)(aa * scale2 * 255)).getRGB());
                JelloFontUtil.jelloFont18.drawNoBSString(text, centerX - 80 + 12, centerY - 100 / 2f + 12 + 30, new Color(100,100,100,(int)(aa * scale2 * 255)).getRGB());

                RenderUtils.drawRoundShadow(centerX - 80 + 12, centerY - 100 / 2f + 12 + 30 + 30, 50, 20, new Color(59, 153, 253,(int)(aa * scale2 * 255)).getRGB());
                JelloFontUtil.jelloFontBold20.drawNoBSString("close", centerX - 80 + 12 + 7, centerY - 100 / 2f + 12 + 30 + 30 + 6, new Color(255,255,255,(int)(aa * scale2 * 255)).getRGB());

            }

            GL11.glPopMatrix();
        }


    }
    private static class SigmaCloseGameGui extends BlurredSigmaMainMenuGui {
        private long time = -1;
        String text, secondText;
        public SigmaCloseGameGui(SigmaGuiMainMenu parentScreen) {
            super(parentScreen);
            closeOnEsc = false;
            closeOnLClick = false;
            closeOnRClick = false;
            text = texts[new Random().nextInt(texts.length)];
            secondText = texts2[new Random().nextInt(texts2.length)];
        }
        static String[] texts = new String[]{"Hey, ", "Hello, ", "Farewell, ", "Howdy, ", "Good evening, ", "Good bye, ", "Bye, ", "Later, ", "See you next time, ", "See you later, ", "Welcome to 2b2t.org, ", "Greetings, ", "Catch ya later, ", "Good to see you, ", "Hope you had a good time, ", "Aww, it's you ", "Well, It was nice to have you here, ", "Bye, Bye "};
        static String[] texts2 = new String[]{
                "With a true friend, the world is but a little place, after all.",
                "Time is flying away，and years are passing by. Only our friendship is always in my heart.",
                "You are in my thoughts every minute of the day, in my dream every hour of the night.",
                "True friends are never apart, maybe in distance but never in heart.",
                "Don‘t try so hard, the best things come when you least expect them to.",
                "To the world you may be one person, but to one person you may be the world.",
                "The sandglass remembers the time we lost.",
                "The heartbreaking departure always happens in a fraction of a moment.",
                "My end is no regrets, the Iraqi people to wave.",
                "Never frown, even when you are sad, because you never know who is falling in love with your smile.",
                "No man or woman is worth your tears, and the one who is, won‘t make you cry."
        };
        @Override
        public void drawBlurredScreen(int mouseX, int mouseY, float partialTicks) {
            JelloFontUtil.jelloFontBold38.drawNoBSString(text, 50, 53, new Color(255, 255, 255, (int)(255 * getBlurAlphaValue())).getRGB());
            JelloFontUtil.jelloFont25.drawNoBSString(secondText, 50, 53 + 25, new Color(200, 200, 200, (int)(255  * getBlurAlphaValue())).getRGB());

            if (getBlurAlphaValue() == 1 && time == -1) {
                time = System.currentTimeMillis();
            }

            if (time != -1 && System.currentTimeMillis() - time > 4000) {
                Minecraft.getInstance().shutdown();
            }
        }
    }


    public abstract static class BlurredSigmaMainMenuGui extends Screen {
        private final SigmaGuiMainMenu parentScreen;
        private final ResourceLocation BACKGROUND = new ResourceLocation("sigmang/images/jellobg.png");
        private final ResourceLocation BLURRED_BACKGROUND = new ResourceLocation("sigmang/images/jelloblur.png");

        private final Sigma5AnimationUtil blurAlpha;
        @Getter
        private float blurAlphaValue = 0F;

        public ParticleEngine pe = new ParticleEngine();

        boolean isClosing = false;

        // properties
        protected boolean closeOnEsc = true;
        protected boolean closeOnLClick = false;
        protected boolean closeOnRClick = false;


        public BlurredSigmaMainMenuGui(SigmaGuiMainMenu parentScreen) {
            super(new StringTextComponent("BlurredSigmaMainMenuGui"));
            this.parentScreen = parentScreen;
            blurAlpha = new Sigma5AnimationUtil(300, 300);
            blurAlpha.animTo(Sigma5AnimationUtil.AnimState.ANIMING);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());

            float m1 = sr.getScaledWidth() / 960f * 3840 * 0.5f;
            float h1 = sr.getScaledHeight() / 501f * 1080 * 0.5f;
            float m = 1.0f;
            float addM = m1 / 2f - sr.getScaledWidth() / 2F;
            float addH = h1 / 2f - sr.getScaledHeight() / 2F;


            float blurAlpha = this.blurAlpha.getAnim();
            blurAlpha = !isClosing ? 欫좯콵甐鶲㥇(blurAlpha, 0.17, 1.0, 0.51, 1.0) : (1 - 欫좯콵甐鶲㥇(1 - blurAlpha, 0.17, 1.0, 0.51, 1.0));

            SigmaGuiMainMenu.renderAlpha = 1 - blurAlpha;
            SigmaGuiMainMenu.renderYOffsets = 0;

            blurAlphaValue = blurAlpha;

            if (isClosing && blurAlpha == 0) {
                mc.displayGuiScreen(parentScreen);
                return;
            }

            if (blurAlpha < 1F) {
                parentScreen.drawScreen(mouseX, mouseY, partialTicks);
            }

            Minecraft.getInstance().getTextureManager().bindTexture(BLURRED_BACKGROUND);
            drawTexture(-addM + -(animatedMouseX / sr.getScaledWidth() - 0.5f) * 2 * addM, -addH + -(animatedMouseY / sr.getScaledHeight() - 0.5f) * 2 * addH, 0, 0, m1 * m, h1 * m, m1 * m, h1 * m, blurAlpha);

            RenderUtils.drawRect(0, 0, width, height, new Color(0, 0, 0, (int) (15 * blurAlpha)).getRGB());

            if (blurAlpha == 1F) {
//                pe.render(-animatedMouseX, -animatedMouseY);
            }

            // update mouse position
            animatedMouseX += (float) (((mouseX - animatedMouseX) / 1.8) + 0.1);
            animatedMouseY += (float) (((mouseY - animatedMouseY) / 1.8) + 0.1);

            drawBlurredScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
            return super.mouseScrolled(mouseX, mouseY, delta);
        }

        public abstract void drawBlurredScreen(int mouseX, int mouseY, float partialTicks);

        public void closeGui() {
            isClosing = true;
            blurAlpha.animTo(Sigma5AnimationUtil.AnimState.SLEEPING);
        }

        private void drawTexture(float x, float y, int u, int v, float w, float h, float w2, float h2, float alpha) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            RenderUtils.drawTextureCustom(x, y, w, h, alpha);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if(keyCode == InputMappings.getInputByName("key.keyboard.escape").getKeyCode()) {
                if (closeOnEsc) {
                    closeGui();
                    return false;
                }else return false;
            }
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseButton){
            if ((closeOnLClick && mouseButton == 0) || (closeOnRClick && mouseButton == 1)) {
                closeGui();
            } else {
                super.mouseClicked(mouseX, mouseY, mouseButton);
            }
            return false;
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }

}
