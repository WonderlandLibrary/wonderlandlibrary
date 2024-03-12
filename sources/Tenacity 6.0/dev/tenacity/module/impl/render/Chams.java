// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import dev.tenacity.event.impl.render.RenderModelEvent;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.event.impl.render.RenderChestEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import java.awt.Color;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.Module;

public class Chams extends Module
{
    private final MultipleBoolSetting entities;
    private final BooleanSetting lighting;
    private final BooleanSetting onlyBehindWalls;
    private final ModeSetting behindWalls;
    private final ColorSetting wallColor;
    private final ModeSetting visibleColorMode;
    private final ColorSetting visibleColor;
    
    public Chams() {
        super("Chams", "Chams", Category.RENDER, "See people through walls");
        this.entities = new MultipleBoolSetting("Entities", new String[] { "Players", "Animals", "Mobs", "Chests" });
        this.lighting = new BooleanSetting("Lighting", true);
        this.onlyBehindWalls = new BooleanSetting("Only behind walls", false);
        this.behindWalls = new ModeSetting("Not Visible", "Sync", new String[] { "Sync", "Opposite", "Red", "Custom" });
        this.wallColor = new ColorSetting("Not Visible Color", Color.red);
        this.visibleColorMode = new ModeSetting("Visible", "Sync", new String[] { "Sync", "Custom" });
        this.visibleColor = new ColorSetting("Visible Color", Color.BLUE);
        this.wallColor.addParent(this.behindWalls, behindWalls -> behindWalls.is("Custom"));
        this.visibleColorMode.addParent(this.onlyBehindWalls, ParentAttribute.BOOLEAN_CONDITION.negate());
        this.visibleColor.addParent(this.visibleColorMode, modeSetting -> modeSetting.is("Custom"));
        this.addSettings(this.entities, this.lighting, this.onlyBehindWalls, this.behindWalls, this.wallColor, this.visibleColorMode, this.visibleColor);
    }
    
    @Override
    public void onRenderChestEvent(final RenderChestEvent event) {
        if (!this.entities.isEnabled("Chests")) {
            return;
        }
        Color behindWallsColor = Color.WHITE;
        final Pair<Color, Color> colors = HUDMod.getClientColors();
        final String mode = this.behindWalls.getMode();
        switch (mode) {
            case "Sync": {
                behindWallsColor = colors.getSecond();
                break;
            }
            case "Opposite": {
                behindWallsColor = ColorUtil.getOppositeColor(colors.getFirst());
                break;
            }
            case "Red": {
                behindWallsColor = new Color(-1104346);
                break;
            }
            case "Custom": {
                behindWallsColor = this.wallColor.getColor();
                break;
            }
        }
        Color visibleColor = Color.WHITE;
        final String mode2 = this.visibleColorMode.getMode();
        switch (mode2) {
            case "Sync": {
                visibleColor = colors.getFirst();
                break;
            }
            case "Custom": {
                visibleColor = this.visibleColor.getColor();
                break;
            }
        }
        GL11.glPushMatrix();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glDisable(3553);
        RenderUtil.color(behindWallsColor.getRGB());
        if (!this.lighting.isEnabled()) {
            GL11.glDisable(2896);
        }
        event.drawChest();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        RenderUtil.resetColor();
        if (!this.onlyBehindWalls.isEnabled()) {
            RenderUtil.color(visibleColor.getRGB());
            event.drawChest();
            event.cancel();
        }
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glPolygonOffset(1.0f, -1000000.0f);
        GL11.glDisable(10754);
        GL11.glPopMatrix();
    }
    
    @Override
    public void onRenderModelEvent(final RenderModelEvent event) {
        if (!this.isValidEntity(event.getEntity())) {
            return;
        }
        final Pair<Color, Color> colors = HUDMod.getClientColors();
        if (event.isPre()) {
            Color behindWallsColor = Color.WHITE;
            final String mode = this.behindWalls.getMode();
            switch (mode) {
                case "Sync": {
                    behindWallsColor = colors.getSecond();
                    break;
                }
                case "Opposite": {
                    behindWallsColor = ColorUtil.getOppositeColor(colors.getFirst());
                    break;
                }
                case "Red": {
                    behindWallsColor = new Color(-1104346);
                    break;
                }
                case "Custom": {
                    behindWallsColor = this.wallColor.getColor();
                    break;
                }
            }
            GL11.glPushMatrix();
            GL11.glEnable(10754);
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(3553);
            if (!this.lighting.isEnabled()) {
                GL11.glDisable(2896);
            }
            RenderUtil.color(behindWallsColor.getRGB());
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
        }
        else {
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            Color visibleColor = Color.WHITE;
            final String mode2 = this.visibleColorMode.getMode();
            switch (mode2) {
                case "Sync": {
                    visibleColor = colors.getFirst();
                    break;
                }
                case "Custom": {
                    visibleColor = this.visibleColor.getColor();
                    break;
                }
            }
            if (this.onlyBehindWalls.isEnabled()) {
                GL11.glDisable(3042);
                GL11.glEnable(3553);
                GL11.glEnable(2896);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
            else {
                if (!this.lighting.isEnabled()) {
                    GL11.glDisable(2896);
                }
                RenderUtil.color(visibleColor.getRGB());
            }
            event.drawModel();
            GL11.glEnable(3553);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glPolygonOffset(1.0f, -1000000.0f);
            GL11.glDisable(10754);
            GL11.glPopMatrix();
        }
    }
    
    private boolean isValidEntity(final Entity entity) {
        return (this.entities.isEnabled("Players") && entity instanceof EntityPlayer) || (this.entities.isEnabled("Animals") && entity instanceof EntityAnimal) || (this.entities.isEnabled("Mobs") && entity instanceof EntityMob);
    }
}
