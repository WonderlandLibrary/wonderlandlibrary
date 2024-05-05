// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.render;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.lwjgl.opengl.GL11;
import Lavish.utils.color.ColorUtil;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import Lavish.modules.combat.Killaura;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import java.util.List;
import Lavish.event.events.EventRenderGUI;
import Lavish.event.Event;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import java.util.ArrayList;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class TargetHUD extends Module
{
    private float lastHealth;
    private double healthBarWidth;
    public int height;
    public int width;
    public int x;
    public int y;
    
    public TargetHUD() {
        super("TargetHUD", 0, true, Category.Render, "See players info that you are attacking");
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Astolfo");
        options.add("Flux");
        options.add("Exhi");
        Client.instance.setmgr.rSetting(new Setting("TargetHUD Mode", this, "Astolfo", options));
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRenderGUI) {
            List<Entity> targets = TargetHUD.mc.theWorld.loadedEntityList.stream().filter(Entity.class::isInstance).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
            targets = targets.stream().filter(entity -> entity.getDistanceToEntity(TargetHUD.mc.thePlayer) < Client.instance.setmgr.getSettingByName("Reach").getValDouble() && entity != TargetHUD.mc.thePlayer && !entity.isDead).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
            targets.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(TargetHUD.mc.thePlayer)));
            targets = targets.stream().filter(EntityPlayer.class::isInstance).collect((Collector<? super Object, ?, List<Entity>>)Collectors.toList());
            if (!targets.isEmpty() && Client.instance.moduleManager.getModuleByName("Killaura").isEnabled()) {
                Entity target = targets.get(0);
                target = targets.get(0);
                if (Client.instance.setmgr.getSettingByName("TargetHUD Mode").getValString().equalsIgnoreCase("Astolfo")) {
                    final FontRenderer fr = Killaura.mc.fontRendererObj;
                    if (!(target instanceof EntityPlayer)) {
                        return;
                    }
                    GlStateManager.pushMatrix();
                    final ScaledResolution rolf = new ScaledResolution(TargetHUD.mc);
                    final float width = (float)(rolf.getScaledWidth() / 2.0 + 100.0);
                    final float height = (float)(rolf.getScaledHeight() / 2.0);
                    Gui.drawRect(width - 70.0f, height + 15.0f, width + 88.0f, height + 89.0f, new Color(0, 0, 0, 190).getRGB());
                    final float health = ((EntityLivingBase)target).getHealth();
                    final float healthPercentage = health / ((EntityLivingBase)target).getMaxHealth();
                    float targetHealthPercentage = 0.0f;
                    if (healthPercentage != this.lastHealth) {
                        final float diff = healthPercentage - this.lastHealth;
                        targetHealthPercentage = this.lastHealth;
                        this.lastHealth += diff / 8.0f;
                    }
                    Color healthcolor = Color.WHITE;
                    if (healthPercentage * 100.0f > 75.0f) {
                        healthcolor = Color.GREEN;
                    }
                    else if (healthPercentage * 100.0f > 50.0f && healthPercentage * 100.0f < 75.0f) {
                        healthcolor = Color.YELLOW;
                    }
                    else if (healthPercentage * 100.0f < 50.0f && healthPercentage * 100.0f > 25.0f) {
                        healthcolor = Color.ORANGE;
                    }
                    else if (healthPercentage * 100.0f < 25.0f) {
                        healthcolor = Color.RED;
                    }
                    Gui.drawRect(width - 36.0f, height + 78.0f, width - 36.0f + 120.0, height + 85.0f, ColorUtil.pulseBrightness(new Color(14, 60, 190), 2, 2).getRGB());
                    if (healthPercentage * 100.0f <= 75.0f) {
                        Gui.drawRect(width - 36.0f, height + 78.0f, width - 36.0f + 126.0f * targetHealthPercentage, height + 85.0f, ColorUtil.pulseBrightness(new Color(13, 108, 244), 2, 2).getRGB());
                        Gui.drawRect(width - 36.0f, height + 78.0f, width - 36.0f + 120.0f * targetHealthPercentage, height + 85.0f, ColorUtil.pulseBrightness(new Color(13, 108, 214), 2, 2).getRGB());
                    }
                    else {
                        Gui.drawRect(width - 36.0f, height + 78.0f, width - 36.0f + 120.0f * targetHealthPercentage, height + 85.0f, ColorUtil.pulseBrightness(new Color(13, 108, 214), 2, 2).getRGB());
                    }
                    final int x1 = (int)(width - 50.0f);
                    final int y1 = (int)(height + 32.0f);
                    GL11.glPushMatrix();
                    GlStateManager.translate((float)x1, (float)y1, 1.0f);
                    GL11.glScaled(1.1, 1.1, 1.1);
                    GlStateManager.translate((float)(-x1), (float)(-y1), 1.0f);
                    fr.drawStringWithShadow(((EntityPlayer)target).getName(), x1 + 13.5f, y1 + 7.5f, -1);
                    GL11.glPopMatrix();
                    final int x2 = (int)(width - 64.0f);
                    final int y2 = (int)(height + 40.0f);
                    GL11.glPushMatrix();
                    GlStateManager.translate((float)x2, (float)y2, 1.0f);
                    GL11.glScalef(2.0f, 2.0f, 2.0f);
                    GlStateManager.translate((float)(-x2), (float)(-y2), 1.0f);
                    Killaura.mc.fontRendererObj.drawStringWithShadow(String.valueOf(String.valueOf(String.format("%.1f", ((EntityLivingBase)target).getHealth() / 2.0f))) + " \u2764", x2 + 13.5f, y2 + 7.5f, ColorUtil.pulseBrightness(new Color(13, 108, 214), 2, 2).getRGB());
                    GL11.glPopMatrix();
                    GlStateManager.popMatrix();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GuiInventory.drawEntityOnScreen((int)(width - 53.0f), (int)(height + 85.0f), 24, 25.0f, 25.0f, (EntityLivingBase)target);
                }
                else {
                    if (Client.instance.setmgr.getSettingByName("TargetHUD Mode").getValString().equalsIgnoreCase("Exhi")) {
                        final ScaledResolution sr = new ScaledResolution(TargetHUD.mc);
                        this.height = 35;
                        this.width = Math.max(110, 45 + TargetHUD.mc.fontRendererObj.getStringWidth(target.getName()));
                        this.x = sr.getScaledWidth() / 2;
                        this.y = sr.getScaledHeight() / 2 + 50;
                    }
                    if (Client.instance.setmgr.getSettingByName("TargetHUD Mode").getValString().equalsIgnoreCase("Flux")) {
                        final FontRenderer fr = TargetHUD.mc.fontRendererObj;
                        if (!(target instanceof EntityPlayer)) {
                            return;
                        }
                        GlStateManager.pushMatrix();
                        final ScaledResolution rolf = new ScaledResolution(TargetHUD.mc);
                        final float width = (float)(rolf.getScaledWidth() / 2.0 + 100.0);
                        final float height = (float)(rolf.getScaledHeight() / 2.0);
                        Gui.drawRect(width - 72.0f, height + 35.0f, width - 30.0f + 120.0f, height + 81.0f, new Color(42, 42, 50, 255).getRGB());
                        final float health = ((EntityLivingBase)target).getHealth();
                        final float healthPercentage = health / ((EntityLivingBase)target).getMaxHealth();
                        float targetHealthPercentage = 0.0f;
                        if (healthPercentage != this.lastHealth) {
                            final float diff = healthPercentage - this.lastHealth;
                            targetHealthPercentage = this.lastHealth;
                            this.lastHealth += diff / 8.0f;
                        }
                        Color healthcolor = Color.WHITE;
                        if (healthPercentage * 100.0f > 75.0f) {
                            healthcolor = Color.GREEN;
                        }
                        else if (healthPercentage * 100.0f > 50.0f && healthPercentage * 100.0f < 75.0f) {
                            healthcolor = Color.YELLOW;
                        }
                        else if (healthPercentage * 100.0f < 50.0f && healthPercentage * 100.0f > 25.0f) {
                            healthcolor = Color.ORANGE;
                        }
                        else if (healthPercentage * 100.0f < 25.0f) {
                            healthcolor = Color.RED;
                        }
                        Gui.drawRect(width - 71.0f, height + 69.0f, width - 30.0f + 119.0f, height + 72.0f, Color.BLACK.getRGB());
                        Gui.drawRect(width - 70.0f, height + 70.0f, width - 70.0f + 158.0f * targetHealthPercentage, height + 71.0f, new Color(237, 167, 121).getRGB());
                        Gui.drawRect(width - 70.0f, height + 70.0f, width - 70.0f + 158.0f * healthPercentage, height + 71.0f, Color.GREEN.getRGB());
                        final float armorValue = (float)((EntityPlayer)target).getTotalArmorValue();
                        final double armorWidth = armorValue / 20.0;
                        Gui.drawRect(width - 71.0f, height + 76.0f, width - 30.0f + 119.0f, height + 79.0f, Color.BLACK.getRGB());
                        Gui.drawRect(width - 70.0f, height + 77.0f, width - 70.0f + 158.0 * armorWidth, height + 78.0f, new Color(85, 128, 208).getRGB());
                        fr.drawStringWithShadow(((EntityPlayer)target).getName(), (float)(int)(width - 38.0f), (float)(int)(height + 40.0f), -1);
                        fr.drawStringWithShadow(String.valueOf(String.valueOf(String.format("%.1f", ((EntityLivingBase)target).getHealth() / 2.0f))) + " \u2764", (float)(int)(width - 38.0f), (float)(int)(height + 55.0f), -1);
                        this.drawFace((int)(width - 70.0f), (int)(height + 37.0f), 8.0f, 8.0f, 8, 8, 30, 30, 64.0f, 64.0f, (AbstractClientPlayer)target);
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }
    
    private void drawFace(final double x, final double y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight, final AbstractClientPlayer target) {
        try {
            final ResourceLocation skin = target.getLocationSkin();
            TargetHUD.mc.getTextureManager().bindTexture(skin);
            GL11.glEnable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawScaledCustomSizeModalRect((int)x, (int)y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(3042);
        }
        catch (Exception ex) {}
    }
}
