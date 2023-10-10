/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.hud;

import cc.hyperium.utils.HyperiumFontRenderer;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import tk.rektsky.Client;
import tk.rektsky.event.EventManager;
import tk.rektsky.event.impl.HUDRenderEvent;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.combat.KillAura;
import tk.rektsky.module.impl.render.HUDModule;
import tk.rektsky.module.impl.render.KeyStrokes;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.module.impl.world.BedRekter;
import tk.rektsky.ui.HUD;
import tk.rektsky.utils.display.AnimationUtil;
import tk.rektsky.utils.display.ColorUtil;

public class HUD
extends Gui {
    Minecraft mc = Minecraft.getMinecraft();
    private static final HyperiumFontRenderer fr = Client.getFont();
    private static final HyperiumFontRenderer frbig = Client.getFontBig();
    private static final HyperiumFontRenderer frhd = Client.getHDFont();
    private static final HyperiumFontRenderer logoFont = Client.getSigmaFontWithSize(48);
    private static final HyperiumFontRenderer arrayListFont = Client.getSigmaFontWithSize(20);
    private static final HyperiumFontRenderer modeFont = Client.getSigmaFontWithSize(16);
    private static HUDModule mod = null;
    private static ArrayList<Double> avg = new ArrayList();
    private Color amogus = new Color(255, 255, 255, 150);

    public void draw(ScaledResolution sr, GuiIngame gui) {
        if (mod == null) {
            mod = ModulesManager.getModuleByClass(HUDModule.class);
        }
        if (ModulesManager.getModuleByClass(KeyStrokes.class).isToggled()) {
            this.drawKeyStrokes();
        }
        if (HUD.mod.mode.getValue().equals("RektSky")) {
            this.drawNewHud(sr, gui);
        } else if (HUD.mod.mode.getValue().equals("Legacy")) {
            this.drawRektSkyHUD(sr, gui);
        } else if (HUD.mod.mode.getValue().equals("Fireentr")) {
            this.drawFireentrHUD(sr, gui);
        } else if (HUD.mod.mode.getValue().equals("WeebClient")) {
            this.drawWeebHUD(sr, gui);
        } else {
            this.drawJelloHUD(sr, gui);
        }
        if (HUD.mod.ShowBPS.getValue().booleanValue()) {
            double speed = this.mc.thePlayer.getDistance(this.mc.thePlayer.lastTickPosX, this.mc.thePlayer.posY, this.mc.thePlayer.lastTickPosZ) * ((double)Minecraft.getMinecraft().timer.ticksPerSecond * Minecraft.getMinecraft().timer.timerSpeed);
            double avgInt = 0.0;
            avg.add(speed);
            if (avg.size() > 50) {
                avg.remove(0);
            }
            for (int i2 = 0; i2 < avg.size(); ++i2) {
                avgInt += avg.get(i2).doubleValue();
            }
            DecimalFormat df = new DecimalFormat("#.##");
            String bps = df.format(speed);
            String avgStr = df.format(avgInt /= (double)avg.size());
            String pain = "BBBBBB_0_BBBBBB_0_BBBBBB_0";
            arrayListFont.drawString("BPS: " + bps + " AVG: " + avgStr, (float)sr.getScaledWidth() - arrayListFont.getWidth("BPS: " + bps + " AVG: " + avgStr) - 10.0f, sr.getScaledHeight() - HUD.arrayListFont.FONT_HEIGHT, -1);
        }
        EventManager.callEvent(new HUDRenderEvent(sr, gui));
    }

    private void drawRoundedRect(int x2, int y2, int width, int height, int cornerRadius, Color color) {
        Gui.drawRect(x2, y2 + cornerRadius, x2 + cornerRadius, y2 + height - cornerRadius, color.getRGB());
        Gui.drawRect(x2 + cornerRadius, y2, x2 + width - cornerRadius, y2 + height, color.getRGB());
        Gui.drawRect(x2 + width - cornerRadius, y2 + cornerRadius, x2 + width, y2 + height - cornerRadius, color.getRGB());
        this.drawArc(x2 + cornerRadius, y2 + cornerRadius, cornerRadius, 0, 90, color);
        this.drawArc(x2 + width - cornerRadius, y2 + cornerRadius, cornerRadius, 270, 360, color);
        this.drawArc(x2 + width - cornerRadius, y2 + height - cornerRadius, cornerRadius, 180, 270, color);
        this.drawArc(x2 + cornerRadius, y2 + height - cornerRadius, cornerRadius, 90, 180, color);
    }

    private void drawArc(int x2, int y2, int radius, int startAngle, int endAngle, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        worldRenderer.begin(6, DefaultVertexFormats.POSITION);
        worldRenderer.pos(x2, y2, 0.0).endVertex();
        for (int i2 = (int)((double)startAngle / 360.0 * 100.0); i2 <= (int)((double)endAngle / 360.0 * 100.0); ++i2) {
            double angle = Math.PI * 2 * (double)i2 / 100.0 + Math.toRadians(180.0);
            worldRenderer.pos((double)x2 + Math.sin(angle) * (double)radius, (double)y2 + Math.cos(angle) * (double)radius, 0.0).endVertex();
        }
        Tessellator.getInstance().draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    private void drawNewHud(ScaledResolution scaledresolution, GuiIngame guiInGame) {
        ArrayList enabled = new ArrayList();
        Arrays.asList(ModulesManager.getModules()).forEach(module -> {
            if (module.isToggled() && !module.hideFromArrayList.getValue().booleanValue()) {
                enabled.add(module);
            }
        });
        Module[] enabledModules = enabled.toArray(new Module[0]);
        Arrays.sort(enabledModules, new HUD.ModuleComparator());
        int arrayIndex = 0;
        int color = Color.HSBtoRGB(ColorUtil.getRainbowHue(3000.0f, 0), 0.6f, 1.0f);
        if (ModulesManager.getModuleByClass(HUDModule.class).arrayListColor.getValue().equals("Astolfo")) {
            color = ColorUtil.AstolfoRainbow(109, 0, 1.0f, 1.0f);
        }
        for (Module m2 : enabledModules) {
            color = Color.HSBtoRGB(ColorUtil.getRainbowHue(3000.0f, arrayIndex * 100), 0.6f, 1.0f);
            if (ModulesManager.getModuleByClass(HUDModule.class).arrayListColor.getValue().equals("Astolfo")) {
                color = ColorUtil.AstolfoRainbow(109, arrayIndex * 100, 0.8f, 0.8f);
            }
            int stringWidth = (int)fr.getWidth(m2.name);
            if (!m2.getSuffix().equals("")) {
                stringWidth = (int)((float)stringWidth + fr.getWidth(m2.getSuffix() + " "));
            }
            HUD.drawRect(scaledresolution.getScaledWidth(), arrayIndex * (4 + HUD.fr.FONT_HEIGHT), scaledresolution.getScaledWidth() - (stringWidth += 2) - 3, (arrayIndex + 1) * (4 + HUD.fr.FONT_HEIGHT), -1610612736);
            HUD.drawRect(scaledresolution.getScaledWidth() - stringWidth - 3, arrayIndex * (4 + HUD.fr.FONT_HEIGHT), scaledresolution.getScaledWidth() - stringWidth - 5, (arrayIndex + 1) * (4 + HUD.fr.FONT_HEIGHT), color);
            fr.drawString(m2.name, scaledresolution.getScaledWidth() - stringWidth - 1, arrayIndex * (4 + HUD.fr.FONT_HEIGHT) + 2, color);
            if (!m2.getSuffix().equals("")) {
                fr.drawString(" " + m2.getSuffix(), (float)(scaledresolution.getScaledWidth() - stringWidth - 5) + fr.getWidth(m2.name + " "), arrayIndex * (4 + HUD.fr.FONT_HEIGHT) + 2, Color.HSBtoRGB(ColorUtil.getRainbowHue(6500.0f, arrayIndex * 60), 0.0f, 0.5f));
            }
            ++arrayIndex;
        }
        this.drawRoundedRect(5, 5, Math.max(70, (int)fr.getWidth("Rektsky")), 75, 10, new Color(0, 0, 0, 150));
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("rektsky/icons/icon.png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        HUD.drawModalRectWithCustomSizedTexture(10, 10, 0.0f, 0.0f, 55, 55, 55.0f, 55.0f);
        GlStateManager.disableBlend();
        fr.drawString("B5", Math.max(55.0f, fr.getWidth("RektSky")), 67.0f, 0xFFFFFF);
        GlStateManager.translate(30.0f, 67.0f, 0.0f);
        fr.drawCenteredString("RektSky", 0.0f, 0.0f, color);
        GlStateManager.translate(-30.0f, -67.0f, 0.0f);
        this.drawRektSkyOther(scaledresolution, guiInGame);
    }

    private void drawFireentrHUD(ScaledResolution scaledresolution, GuiIngame guiInGame) {
        ArrayList enabled = new ArrayList();
        Arrays.asList(ModulesManager.getModules()).forEach(module -> {
            if (module.isToggled() && !module.hideFromArrayList.getValue().booleanValue()) {
                enabled.add(module);
            }
        });
        Module[] enabledModules = enabled.toArray(new Module[0]);
        Arrays.sort(enabledModules, new HUD.ModuleComparator());
        int arrayIndex = 0;
        int color = Color.HSBtoRGB(ColorUtil.getRainbowHue(3000.0f, 0), 0.6f, 1.0f);
        if (ModulesManager.getModuleByClass(HUDModule.class).arrayListColor.getValue().equals("Astolfo")) {
            color = ColorUtil.AstolfoRainbow(109, 0, 1.0f, 1.0f);
        }
        for (Module m2 : enabledModules) {
            color = Color.HSBtoRGB(ColorUtil.getRainbowHue(3000.0f, arrayIndex * 100), 0.6f, 1.0f);
            if (ModulesManager.getModuleByClass(HUDModule.class).arrayListColor.getValue().equals("Astolfo")) {
                color = ColorUtil.AstolfoRainbow(109, arrayIndex * 100, 0.8f, 0.8f);
            }
            int stringWidth = (int)fr.getWidth(m2.name);
            if (!m2.getSuffix().equals("")) {
                stringWidth = (int)((float)stringWidth + fr.getWidth(m2.getSuffix() + " "));
            }
            Gui.drawRect(scaledresolution.getScaledWidth() - (stringWidth += 7) - 1, arrayIndex * (8 + HUD.fr.FONT_HEIGHT) + 2, scaledresolution.getScaledWidth() - 2, arrayIndex * (8 + HUD.fr.FONT_HEIGHT) + 15, color);
            Gui.drawRect(scaledresolution.getScaledWidth() - stringWidth - 3, arrayIndex * (8 + HUD.fr.FONT_HEIGHT), scaledresolution.getScaledWidth() - 4, arrayIndex * (8 + HUD.fr.FONT_HEIGHT) + 13, -15724528);
            fr.drawString(m2.name, scaledresolution.getScaledWidth() - stringWidth - 1, arrayIndex * (8 + HUD.fr.FONT_HEIGHT) + 2, color);
            if (!m2.getSuffix().equals("")) {
                fr.drawString(" " + m2.getSuffix(), (float)(scaledresolution.getScaledWidth() - stringWidth - 5) + fr.getWidth(m2.name + " "), arrayIndex * (8 + HUD.fr.FONT_HEIGHT) + 2, -1);
            }
            ++arrayIndex;
        }
        Gui.drawRect(4, 4, (int)frbig.getWidth("Rektsky") + 34, 18, color);
        Gui.drawRect(2, 2, (int)frbig.getWidth("Rektsky") + 32, 16, -15724528);
        fr.drawString("B5", fr.getWidth("RektSky") + 8.0f, 4.0f, 0xFFFFFF);
        GlStateManager.translate(5.0f, 4.0f, 0.0f);
        fr.drawString("RektSky", 0.0f, 0.0f, color);
        GlStateManager.translate(-5.0f, -4.0f, 0.0f);
        this.drawRektSkyOther(scaledresolution, guiInGame);
    }

    private void drawRektSkyHUD(ScaledResolution scaledresolution, GuiIngame guiInGame) {
        ArrayList enabled = new ArrayList();
        Arrays.asList(ModulesManager.getModules()).forEach(module -> {
            if (module.isToggled() && !module.hideFromArrayList.getValue().booleanValue()) {
                enabled.add(module);
            }
        });
        Module[] enabledModules = enabled.toArray(new Module[0]);
        Arrays.sort(enabledModules, new HUD.ModuleComparator());
        int arrayIndex = 0;
        for (Module m2 : enabledModules) {
            int stringWidth = (int)fr.getWidth(m2.name);
            if (!m2.getSuffix().equals("")) {
                stringWidth = (int)((float)stringWidth + fr.getWidth(m2.getSuffix() + " "));
            }
            HUD.drawRect(scaledresolution.getScaledWidth(), arrayIndex * (8 + HUD.fr.FONT_HEIGHT), scaledresolution.getScaledWidth() - (stringWidth += 2) - 12, (arrayIndex + 1) * (8 + HUD.fr.FONT_HEIGHT), -1610612736);
            HUD.drawRect(scaledresolution.getScaledWidth() - stringWidth - 12 - 2, arrayIndex * (8 + HUD.fr.FONT_HEIGHT), scaledresolution.getScaledWidth() - stringWidth - 12, (arrayIndex + 1) * (8 + HUD.fr.FONT_HEIGHT), Color.HSBtoRGB(ColorUtil.getRainbowHue(6500.0f, arrayIndex * 100), 0.5f, 1.0f));
            fr.drawString(m2.name, scaledresolution.getScaledWidth() - stringWidth - 5, arrayIndex * (8 + HUD.fr.FONT_HEIGHT) + 4, Color.HSBtoRGB(ColorUtil.getRainbowHue(6500.0f, arrayIndex * 100), 0.6f, 1.0f));
            if (!m2.getSuffix().equals("")) {
                fr.drawString(" " + m2.getSuffix(), (float)(scaledresolution.getScaledWidth() - stringWidth - 5) + fr.getWidth(m2.name + " "), arrayIndex * (8 + HUD.fr.FONT_HEIGHT) + 4, Color.HSBtoRGB(ColorUtil.getRainbowHue(6500.0f, arrayIndex * 60), 0.0f, 0.5f));
            }
            ++arrayIndex;
        }
        String posText = this.mc.thePlayer.getPosition().getX() + ", " + this.mc.thePlayer.getPosition().getY() + ", " + this.mc.thePlayer.getPosition().getZ();
        HUD.drawRect(5, 5, Math.max(75, (int)(fr.getWidth(posText) + 28.0f)), 75, -1879048192);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("rektsky/icons/icon.png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        HUD.drawModalRectWithCustomSizedTexture(10, 10, 0.0f, 0.0f, 55, 55, 55.0f, 55.0f);
        GlStateManager.disableBlend();
        fr.drawString("B5", Math.max(65.0f, fr.getWidth(posText) + 15.0f), 65.0f, 0xFFFFFF);
        GlStateManager.translate(40.0f, 65.0f, 0.0f);
        fr.drawCenteredString(posText, 0.0f, 0.0f, Color.HSBtoRGB(ColorUtil.getRainbowHue(6500.0f, 0), 0.6f, 1.0f));
        GlStateManager.translate(-40.0f, -65.0f, 0.0f);
        this.drawRektSkyOther(scaledresolution, guiInGame);
    }

    private void drawVanillaKickBar() {
    }

    private void drawKeyStrokes() {
        if (KeyStrokes.Rainbow.getValue().booleanValue()) {
            long time = System.currentTimeMillis();
            this.amogus = Color.getHSBColor((float)(time % 10000L) / 10000.0f, 0.6f, 1.0f);
        } else {
            this.amogus = new Color(255, 255, 255, 180);
        }
        if (KeyStrokes.Rounded.getValue().booleanValue()) {
            if (this.mc.gameSettings.keyBindForward.isKeyDown()) {
                this.drawRoundedRect(35, 100, 25, 25, 5, this.amogus);
                frbig.drawString("W", 42.0f, 107.0f, -16777216);
            } else if (!this.mc.gameSettings.keyBindForward.isKeyDown()) {
                this.drawRoundedRect(35, 100, 25, 25, 5, new Color(0, 0, 0, 150));
                frbig.drawString("W", 42.0f, 107.0f, -1);
            }
        }
        if (this.mc.gameSettings.keyBindLeft.isKeyDown()) {
            this.drawRoundedRect(5, 130, 25, 25, 5, this.amogus);
            frbig.drawString("A", 13.0f, 136.0f, -16777216);
        } else if (!this.mc.gameSettings.keyBindLeft.isKeyDown()) {
            this.drawRoundedRect(5, 130, 25, 25, 5, new Color(0, 0, 0, 150));
            frbig.drawString("A", 13.0f, 136.0f, -1);
        }
        if (this.mc.gameSettings.keyBindBack.isKeyDown()) {
            this.drawRoundedRect(35, 130, 25, 25, 5, this.amogus);
            frbig.drawString("S", 43.5f, 136.0f, -16777216);
        } else if (!this.mc.gameSettings.keyBindBack.isKeyDown()) {
            this.drawRoundedRect(35, 130, 25, 25, 5, new Color(0, 0, 0, 150));
            frbig.drawString("S", 43.5f, 136.0f, -1);
        }
        if (this.mc.gameSettings.keyBindRight.isKeyDown()) {
            this.drawRoundedRect(65, 130, 25, 25, 5, this.amogus);
            frbig.drawString("D", 73.5f, 136.0f, -16777216);
        } else if (!this.mc.gameSettings.keyBindRight.isKeyDown()) {
            this.drawRoundedRect(65, 130, 25, 25, 5, new Color(0, 0, 0, 150));
            frbig.drawString("D", 73.5f, 136.0f, -1);
        }
        if (this.mc.gameSettings.keyBindAttack.isKeyDown()) {
            this.drawRoundedRect(5, 160, 40, 25, 5, this.amogus);
            frbig.drawString("LMB", 11.0f, 166.0f, -16777216);
        } else if (!this.mc.gameSettings.keyBindAttack.isKeyDown()) {
            this.drawRoundedRect(5, 160, 40, 25, 5, new Color(0, 0, 0, 150));
            frbig.drawString("LMB", 11.0f, 166.0f, -1);
        }
        if (this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            this.drawRoundedRect(50, 160, 40, 25, 5, this.amogus);
            frbig.drawString("RMB", 56.0f, 166.0f, -16777216);
        } else if (!this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            this.drawRoundedRect(50, 160, 40, 25, 5, new Color(0, 0, 0, 150));
            frbig.drawString("RMB", 56.0f, 166.0f, -1);
        }
    }

    private void drawJelloHUD(ScaledResolution sr, GuiIngame gui) {
        logoFont.drawString("Rekt", 4.0f, 4.0f, -1);
        int y2 = 4;
        ArrayList enabled = new ArrayList();
        Arrays.asList(ModulesManager.getModules()).forEach(module -> {
            if (module.isToggled() && !module.hideFromArrayList.getValue().booleanValue()) {
                enabled.add(module);
            }
        });
        Module[] enabledModules = enabled.toArray(new Module[0]);
        Arrays.sort(enabledModules, new HUD.ModuleComparator());
        for (Module m2 : enabledModules) {
            if (!m2.isToggled()) continue;
            if (m2.getSuffix() != null && !m2.getSuffix().isEmpty()) {
                arrayListFont.drawString(m2.name + " | " + m2.getSuffix(), (float)sr.getScaledWidth() - arrayListFont.getWidth(m2.name + " | " + m2.getSuffix()) - 4.0f, y2, -1);
            } else {
                arrayListFont.drawString(m2.name, (float)sr.getScaledWidth() - arrayListFont.getWidth(m2.name) - 4.0f, y2, -1);
            }
            y2 += HUD.arrayListFont.FONT_HEIGHT + 5;
        }
        this.drawRektSkyOther(sr, gui);
    }

    private void drawWeebHUD(ScaledResolution scaledresolution, GuiIngame guiInGame) {
        ArrayList enabled = new ArrayList();
        Arrays.asList(ModulesManager.getModules()).forEach(module -> {
            if (module.isToggled() && !module.hideFromArrayList.getValue().booleanValue()) {
                enabled.add(module);
            }
        });
        Module[] enabledModules = enabled.toArray(new Module[0]);
        Arrays.sort(enabledModules, new HUD.ModuleComparator());
        int arrayIndex = 0;
        int color = Color.HSBtoRGB(ColorUtil.getRainbowHue(2000.0f, 0), 0.6f, 1.0f);
        if (ModulesManager.getModuleByClass(HUDModule.class).arrayListColor.getValue().equals("Astolfo")) {
            color = ColorUtil.AstolfoRainbow(309, 0, 0.8f, 0.6f);
        }
        for (Module m2 : enabledModules) {
            color = Color.HSBtoRGB(ColorUtil.getRainbowHue(2000.0f, arrayIndex * 100), 0.6f, 1.0f);
            if (ModulesManager.getModuleByClass(HUDModule.class).arrayListColor.getValue().equals("Astolfo")) {
                color = ColorUtil.AstolfoRainbow(109, arrayIndex * 100, 0.8f, 0.6f);
            }
            int stringWidth = (int)fr.getWidth(m2.name);
            if (!m2.getSuffix().equals("")) {
                stringWidth = (int)((float)stringWidth + fr.getWidth(m2.getSuffix() + " "));
            }
            HUD.drawRect(scaledresolution.getScaledWidth(), arrayIndex * (4 + HUD.fr.FONT_HEIGHT), scaledresolution.getScaledWidth() - ++stringWidth - 3, (arrayIndex + 1) * (4 + HUD.fr.FONT_HEIGHT), -1610612736);
            HUD.drawRect(scaledresolution.getScaledWidth() - stringWidth - 3, arrayIndex * (4 + HUD.fr.FONT_HEIGHT), scaledresolution.getScaledWidth() - stringWidth - 4, (arrayIndex + 1) * (4 + HUD.fr.FONT_HEIGHT), color);
            fr.drawStringWithShadow(m2.name, scaledresolution.getScaledWidth() - stringWidth - 1, arrayIndex * (4 + HUD.fr.FONT_HEIGHT) + 2, color);
            if (!m2.getSuffix().equals("")) {
                fr.drawStringWithShadow(" " + m2.getSuffix(), (float)(scaledresolution.getScaledWidth() - stringWidth - 5) + fr.getWidth(m2.name + " "), arrayIndex * (4 + HUD.fr.FONT_HEIGHT) + 2, Color.HSBtoRGB(ColorUtil.getRainbowHue(6500.0f, arrayIndex * 60), 0.0f, 0.5f));
            }
            ++arrayIndex;
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("rektsky/weebclient.png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableBlend();
        HUD.drawModalRectWithCustomSizedTexture(1, 1, 0.0f, 0.0f, 82, 82, 92.0f, 82.0f);
        GlStateManager.disableBlend();
        Client.getCasper().drawStringWithShadow("B5", Math.max(50.0f, fr.getWidth("RektSky")), 83.0f, 0xFFFFFF);
        GlStateManager.translate(5.0f, 83.0f, 0.0f);
        Client.getCasper().drawStringWithShadow("RektSky", 0.0f, 0.0f, color);
        GlStateManager.translate(-5.0f, -83.0f, 0.0f);
        this.drawRektSkyOther(scaledresolution, guiInGame);
    }

    private void DrawNew(ScaledResolution sr, GuiIngame gui) {
        logoFont.drawString("Rekt", 4.0f, 4.0f, -1);
        int x2 = 4;
        ArrayList enabled = new ArrayList();
        Arrays.asList(ModulesManager.getModules()).forEach(module -> {
            if (module.isToggled()) {
                enabled.add(module);
            }
        });
        Module[] enabledModules = enabled.toArray(new Module[0]);
        Arrays.sort(enabledModules, new HUD.ModuleComparator());
        for (Module m2 : enabledModules) {
            if (!m2.isToggled()) continue;
            arrayListFont.drawString(m2.name + " | " + m2.getSuffix(), (float)sr.getScaledWidth() - arrayListFont.getWidth(m2.name + " | " + m2.getSuffix()) - 4.0f, x2, -1);
            x2 += HUD.arrayListFont.FONT_HEIGHT + 5;
        }
        this.drawRektSkyOther(sr, gui);
    }

    private void drawRektSkyOther(ScaledResolution scaledresolution, GuiIngame guiInGame) {
        if (Notification.currentNotification != null && ModulesManager.getModuleByClass(Notification.class).isToggled()) {
            double animProg = 1.0;
            if (Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime() <= 1000L) {
                String setting = ModulesManager.getModuleByClass(Notification.class).animationSetting.getValue();
                if (setting.equals("Sine")) {
                    animProg = AnimationUtil.easeInSine((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                } else if (setting.equals("Cubic")) {
                    animProg = AnimationUtil.easeOutCubic((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                } else if (setting.equals("Quint")) {
                    animProg = AnimationUtil.easeOutQuint((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                } else if (setting.equals("Circ")) {
                    animProg = AnimationUtil.easeOutCirc((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                } else if (setting.equals("Elastic")) {
                    animProg = AnimationUtil.easeOutElastic((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                } else if (setting.equals("Quad")) {
                    animProg = AnimationUtil.easeOutQuad((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                } else if (setting.equals("Quart")) {
                    animProg = AnimationUtil.easeOutQuart((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                } else if (setting.equals("Expo")) {
                    animProg = AnimationUtil.easeOutExpo((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                } else if (setting.equals("Back")) {
                    animProg = AnimationUtil.easeOutBack((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                } else if (setting.equals("Bounce")) {
                    animProg = AnimationUtil.easeOutBounce((double)(Minecraft.getSystemTime() - Notification.currentNotification.getFirstRenderTime()) / 1000.0);
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0, animProg * (double)(70 + HUD.fr.FONT_HEIGHT + 2) + 60.0 - (double)(70 + HUD.fr.FONT_HEIGHT + 2) - 60.0, 0.0);
            Notification.PopupMessage noti = Notification.currentNotification;
            int width = (int)Math.max(fr.getWidth(noti.getTitle()), fr.getWidth(noti.getMessage()));
            HUD.drawRect(scaledresolution.getScaledWidth() / 2 - width / 2 - 20, 30, scaledresolution.getScaledWidth() / 2 + width / 2 + 20, 70 + HUD.fr.FONT_HEIGHT + 2, -1610612736);
            HUD.drawRect(scaledresolution.getScaledWidth() / 2 - width / 2 - 20, 26, scaledresolution.getScaledWidth() / 2 + width / 2 + 20, 30, noti.getTitleColor());
            fr.drawString(noti.getTitle(), (float)scaledresolution.getScaledWidth() / 2.0f - (float)width / 2.0f, 30 + HUD.fr.FONT_HEIGHT + 2, noti.getColor());
            fr.drawString(noti.getMessage(), (float)scaledresolution.getScaledWidth() / 2.0f - (float)width / 2.0f, 50 + HUD.fr.FONT_HEIGHT + 2, 0xFFFFFF);
            GlStateManager.popMatrix();
        }
        KillAura killAura = ModulesManager.getModuleByClass(KillAura.class);
        BedRekter bedRekter = ModulesManager.getModuleByClass(BedRekter.class);
        if (bedRekter.hudSetting.getValue().booleanValue()) {
            if (bedRekter.target == null) {
                bedRekter.firstHudRenderTime = null;
                bedRekter.lastHudRenderTime = null;
            }
            if (bedRekter.isToggled() && bedRekter.target != null && !killAura.isAttacking) {
                if (bedRekter.firstHudRenderTime == null) {
                    bedRekter.firstHudRenderTime = Minecraft.getSystemTime();
                }
                HUD.drawRect((int)((double)scaledresolution.getScaledWidth() / 1.8), 340, 809, 438, -872415232);
                GlStateManager.pushMatrix();
                GlStateManager.translate(598.0f, 346.0f, 0.0f);
                GlStateManager.scale(1.5, 1.5, 0.0);
                frhd.drawString("Breaking Bed", 0.0f, 0.0f, 4237818);
                GlStateManager.popMatrix();
                fr.drawString(bedRekter.target.getX() + ", " + bedRekter.target.getY() + ", " + bedRekter.target.getY(), 598.0f, 369.0f, 2160368);
                HUD.drawRect(598, 407, 600, 419, -1);
                HUD.drawRect(598, 407, 800, 409, -1);
                HUD.drawRect(798, 407, 800, 419, -1);
                HUD.drawRect(598, 419, 800, 421, -1);
                float progress = (float)(Minecraft.getSystemTime() - bedRekter.firstHudRenderTime) * (bedRekter.shouldDamage / 50.0f);
                float progressX = (progress > 1.0f ? 1.0f : progress) * 200.0f;
                fr.drawString(Math.round(progress * 100.0f) <= 100 ? Math.round(progress * 100.0f) + "%" : "...", 600.0f + progressX, 423.0f, Color.HSBtoRGB(0.0f, 0.0f, 0.5f + Math.min(progress, 1.0f) / 2.0f));
                float color = 0.5f + Math.min(progress, 1.0f) / 2.0f;
                HUD.drawRect(600, 409, Math.round(600.0f + progressX), 419, color, color, color, 1.0f);
            }
        }
        if (killAura.targetHudSetting.getValue().booleanValue() && killAura.isAttacking && killAura.t != null) {
            EntityLivingBase target = killAura.t;
            int offsetX = (int)this.fromPercentToScaledX(50.0f) + 30;
            int offsetY = (int)this.fromPercentToScaledY(50.0f) + 30;
            HUD.drawRect(offsetX, offsetY, offsetX + 200, offsetY + 75, -1442840576);
            HUD.drawRect(offsetX, offsetY, offsetX + 2, offsetY + 75, Color.HSBtoRGB(ColorUtil.getRainbowHue(6500.0f, 0), 0.7f, 1.0f));
            HUD.drawRect(offsetX + 10, offsetY + 20, offsetX + 10 + 180, offsetY + 20 + 3, -15646975);
            float health = target.getHealth();
            float maxHealth = target.getMaxHealth();
            float x2 = health / maxHealth * 180.0f;
            x2 = Math.min(x2, 360.0f);
            HUD.drawRect(offsetX + 10, offsetY + 20, offsetX + 10 + Math.round(x2), offsetY + 20 + 3, -8323244);
            fr.drawString((float)Math.round(health * 10.0f) / 10.0f + "/" + (float)Math.round(maxHealth * 10.0f) / 10.0f, offsetX + 10, offsetY + 25, 0xFFFFFF);
            Object targetInfo = null;
            int ping = 0;
            for (NetworkPlayerInfo info : this.mc.getNetHandler().getPlayerInfoMap()) {
                if (!info.getGameProfile().getName().equals(target.getName())) continue;
                ping = info.getResponseTime();
                break;
            }
            fr.drawString(ping + " ms", (float)(offsetX + 200) - fr.getWidth(ping + "ms") - 15.0f, offsetY + 10, 0xFFFFFF);
            frbig.drawString(target.getHeldItem() != null ? target.getHeldItem().getDisplayName() + " x" + target.getHeldItem().stackSize : "Nothing x0", offsetX + 10, offsetY + 37, -1);
            String text = Float.toString((float)Math.round(this.mc.thePlayer.getEntityBoundingBox().getDistanceTo(target.getPositionVector()) * 10.0) / 10.0f) + " blocks";
            fr.drawString(text, (float)(offsetX + 200) - fr.getWidth(text) - 12.0f, offsetY + 24, -1);
            for (int iii = 3; iii != -1; --iii) {
                if (target.getCurrentArmor(iii) == null) continue;
                guiInGame.itemRenderer.renderItemIntoGUI(target.getCurrentArmor(iii), iii * 16 + 10 + offsetX, offsetY + 50);
                guiInGame.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, target.getCurrentArmor(iii), iii * 16 + 10 + offsetX, offsetY + 52);
            }
            frbig.drawString(target.getName(), offsetX + 10, offsetY + 5, 0xFFFFFF);
        }
        this.drawTPS(scaledresolution);
    }

    private void drawTPS(ScaledResolution sr) {
        Client.getFont().drawString("TPS: " + Client.networkUtil.getTPS(), 4.0f, sr.getScaledHeight() - Client.getFont().FONT_HEIGHT - 4, -1);
    }

    private float fromPercentToScaledX(float x2) {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return (float)newSR.getScaledWidth() / 100.0f * x2;
    }

    private float fromPercentToScaledY(float y2) {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return (float)newSR.getScaledHeight() / 100.0f * y2;
    }

    private int getScaledX() {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return newSR.getScaledWidth();
    }

    private int getScaledY() {
        ScaledResolution newSR = new ScaledResolution(Minecraft.getMinecraft());
        return newSR.getScaledHeight();
    }
}

