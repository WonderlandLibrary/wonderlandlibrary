package koks.manager.module.impl.gui;

import koks.Koks;
import koks.api.util.Animation;
import koks.api.util.RenderUtil;
import koks.api.util.fonts.GlyphPageFontRenderer;
import koks.manager.cl.Role;
import koks.manager.event.Event;
import koks.manager.event.impl.EventKeyPress;
import koks.manager.event.impl.EventRender2D;
import koks.manager.event.impl.EventTick;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import koks.manager.module.impl.combat.KillAura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Comparator;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 10:20
 */

@ModuleInfo(name = "HUD", description = "Draws the HUD", category = Module.Category.GUI)
public class HUD extends Module {

    public final FontRenderer fr = mc.fontRendererObj;
    public Setting clientName = new Setting("Name", Koks.getKoks().NAME, this);
    public Setting waterMark = new Setting("Watermark", true, this);
    public Setting waterMarkFont = new Setting("Font", "NONE", this);
    public Setting arrayList = new Setting("ArrayList", true, this);
    public Setting hotbar = new Setting("Hotbar", true, this);
    public Setting showTags = new Setting("Show Tags", true, this);
    public Setting tabGUI = new Setting("TabGUI", true, this);
    public Setting arrayListMode = new Setting("ArrayListMode", new String[]{"Mode1", "Mode2", "Mode3"}, "Mode1", this);
    public Setting x = new Setting("X", 5, 0, 20, true, this);
    public Setting y = new Setting("Y", 30, 20, 50, true, this);
    public Setting width = new Setting("Width", 91, 80, 100, true, this);
    public Setting height = new Setting("height", 13, 10, 20, true, this);

    public HUD() {
        setToggled(true);
    }

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventTick) {
            if (Koks.getKoks().CLManager.getUser().getRole() != Role.Developer) {
                waterMarkFont.setTyped("NONE");
            }
        }

        if (event instanceof EventRender2D) {

            drawHotbar();
            if (tabGUI.isToggled())
                Koks.getKoks().tabGUI.drawScreen((int) x.getCurrentValue(), (int) y.getCurrentValue(), (int) width.getCurrentValue(), (int) height.getCurrentValue());
            if (waterMark.isToggled())
                drawWaterMark();
            if (arrayList.isToggled())
                drawArrayList();
            KillAura killAura = (KillAura) Koks.getKoks().moduleManager.getModule(KillAura.class);
            killAura.preferType.setVisible(!killAura.attackType.getCurrentMode().equals("Switch"));
            killAura.noSwingType.setVisible(killAura.noSwing.isToggled());
            killAura.blockMode.setVisible(killAura.autoBlock.isToggled());
        }

        if (event instanceof EventKeyPress) {
            if (tabGUI.isToggled())
                Koks.getKoks().tabGUI.manageKeys((EventKeyPress) event);
        }
    }

    public GlyphPageFontRenderer waterFont;

    public ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    public Animation hotbarAnimation = new Animation(0, 0, sr.getScaledHeight() - 22, sr.getScaledHeight() - 22, 0.4F);

    public Animation fpsAnimation = new Animation(0, 0, 0, 0, 0.4F);

    public void drawHotbar() {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        long ping = 0;
        if (mc.getCurrentServerData() != null)
            ping = mc.getCurrentServerData().pingToServer;

        double x = Math.round(mc.thePlayer.posX * 10);
        double y = Math.round(mc.thePlayer.posY * 10);
        double z = Math.round(mc.thePlayer.posZ * 10);

        String fps = "FPS: §7" + mc.getDebugFPS() + " §rPing: §7" + ping;
        /*String xPos = "X: §7" + (x / 10);
        String yPos = "Y: §7" + (y / 10);
        String zPos = "Z: §7" + (z / 10);*/


        if (mc.currentScreen instanceof GuiChat || !hotbar.isToggled()) {
            fpsAnimation.setGoalX(-fr.getStringWidth(fps));
            fpsAnimation.setSpeed((float) 25);
            if (fpsAnimation.hasXReached(-fr.getStringWidth(fps))) {
                if (timeHelper.hasReached(350))
                    hotbarAnimation.setGoalY(sr.getScaledHeight());
            } else {
                timeHelper.reset();
            }
        } else {
            hotbarAnimation.setGoalY(sr.getScaledHeight() - 22);

            if (hotbarAnimation.hasYReached(sr.getScaledHeight() - 22)) {
                if (timeHelper.hasReached(350)) {
                    fpsAnimation.setSpeed(25);
                    fpsAnimation.setGoalX(5);
                }
            } else {
                timeHelper.reset();
            }
        }

        hotbarAnimation.setSpeed((float) Math.toRadians(Math.abs(hotbarAnimation.getGoalY() - hotbarAnimation.getY())));


        renderUtil.drawRect(0, hotbarAnimation.getAnimationY(), sr.getScaledWidth(), sr.getScaledHeight(), Integer.MIN_VALUE);

        fr.drawString(fps, fpsAnimation.getAnimationX(), sr.getScaledHeight() - 20, -1, true);

        /*fr.drawString(xPos, fpsAnimation.getAnimationX(), sr.getScaledHeight() - 10, -1, true);
        fr.drawString(yPos, fpsAnimation.getAnimationX() + fr.getStringWidth(xPos) + 3, sr.getScaledHeight() - 10, -1, true);
        fr.drawString(zPos, fpsAnimation.getAnimationX() + fr.getStringWidth(xPos) + fr.getStringWidth(yPos) + 6, sr.getScaledHeight() - 10, -1, true);*/


    }

    public void drawWaterMark() {
        GL11.glPushMatrix();
        double scale = 2.5;
        GL11.glScaled(scale, scale, scale);

        if (waterMarkFont.getTyped().equalsIgnoreCase("NONE")) {
            fr.drawStringWithShadow(clientName.getTyped(), 3, 3, Koks.getKoks().clientColor.getRGB());
        } else {
            waterFont = GlyphPageFontRenderer.create(waterMarkFont.getTyped(), 50, true, true, true);
            waterFont.drawString(Koks.getKoks().NAME, 3, 3, Koks.getKoks().clientColor.getRGB(), true);
        }
        GL11.glPopMatrix();

        float x = waterMarkFont.getTyped().equalsIgnoreCase("NONE") ? (float) (fr.getStringWidth(clientName.getTyped()) * scale) : waterFont.getStringWidth(clientName.getTyped());

        ScaledResolution sr = new ScaledResolution(mc);
        fr.drawStringWithShadow("v" + Koks.getKoks().VERSION, x + 10, 18, Color.LIGHT_GRAY.getRGB());
    }

    public void drawArrayList() {
        ScaledResolution sr = new ScaledResolution(mc);
        int[] currentOffset = {0};
        int[] offset = {fr.FONT_HEIGHT + 2};
        int[] y = {0};
        switch (arrayListMode.getCurrentMode()) {
            case "Mode1":
                Koks.getKoks().moduleManager.getModules().stream().sorted(Comparator.comparingDouble(module -> -fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))).forEach(module -> {
                    if (module.isToggled()) {
                        if (module.getAnimation() < fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))
                            module.setAnimation(module.getAnimation() + 0.5);
                        if (module.getAnimation() > fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))
                            module.setAnimation(module.getAnimation() - 0.5);

                        Gui.drawRect((int) (sr.getScaledWidth() - module.getAnimation() - 5), y[0], sr.getScaledWidth(), y[0] + offset[0], 0xBB000000);
                        Gui.drawRect((int) (sr.getScaledWidth() - module.getAnimation() - 7), y[0], (int) (sr.getScaledWidth() - module.getAnimation() - 5), y[0] + offset[0], Koks.getKoks().clientColor.getRGB());
                        fr.drawStringWithShadow(module.getArrayName("§7", showTags.isToggled()), (float) (sr.getScaledWidth() - module.getAnimation() - 2), y[0] + 1.5F, Koks.getKoks().clientColor.getRGB());
                        y[0] += offset[0];
                    }
                });
                break;
            case "Mode2":

                Koks.getKoks().moduleManager.getModules().stream().sorted(Comparator.comparingDouble(module -> -fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))).forEach(module -> {
                    if (module.isToggled()) {
                        if (module.getAnimation() < fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))
                            module.setAnimation(module.getAnimation() + 0.5);
                        if (module.getAnimation() > fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))
                            module.setAnimation(module.getAnimation() - 0.5);

                        renderUtil.drawRect(sr.getScaledWidth() - 1.5, y[0], sr.getScaledWidth(), y[0] + offset[0], renderUtil.getRainbow(currentOffset[0], 3000, 1, 1).getRGB());
                        renderUtil.drawRect(sr.getScaledWidth() - (module.getName() != null ? 2 : 0) - 3 - module.getAnimation(), y[0], sr.getScaledWidth() - 1.5, y[0] + offset[0], renderUtil.getAlphaColor(new Color(25, 25, 25), 170).getRGB());

                        fr.drawStringWithShadow(module.getArrayName("§7", showTags.isToggled()), (float) (sr.getScaledWidth() - module.getAnimation() - 2), y[0] + 1.5F, Color.white.getRGB());

                        y[0] += offset[0];
                        currentOffset[0] += 100;
                    }
                });
                break;
            case "Mode3":
                Koks.getKoks().moduleManager.getModules().stream().sorted(Comparator.comparingDouble(module -> -fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))).forEach(module -> {
                    if (module.isToggled()) {
                        if (module.getAnimation() < fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))
                            module.setAnimation(module.getAnimation() + 0.5);
                        if (module.getAnimation() > fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))
                            module.setAnimation(module.getAnimation() - 0.5);

                        fr.drawString(module.getArrayName("§7", showTags.isToggled()), (float) (sr.getScaledWidth() - module.getAnimation() - 1), y[0] + 1.5f, renderUtil.getRainbow(currentOffset[0], 3000, 0.6f, 1).getRGB());
                        y[0] += offset[0];
                        currentOffset[0] += 100;
                    }
                });
                break;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}