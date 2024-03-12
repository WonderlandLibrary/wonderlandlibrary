// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.StencilUtil;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.settings.Setting;
import java.util.ArrayList;
import dev.tenacity.Tenacity;
import dev.tenacity.module.settings.impl.BooleanSetting;
import java.awt.Color;
import dev.tenacity.module.Category;
import dev.tenacity.utils.objects.GradientColorWheel;
import net.minecraft.entity.Entity;
import java.util.List;
import dev.tenacity.utils.objects.Dragging;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class Radar extends Module
{
    public final NumberSetting size;
    private final ColorSetting playerColor;
    private final ColorSetting mobColor;
    private final ColorSetting animalColor;
    private final ColorSetting itemColor;
    public final MultipleBoolSetting targets;
    public final Dragging drag;
    private final List<Entity> entities;
    private final GradientColorWheel colorWheel;
    
    public Radar() {
        super("Radar", "Radar", Category.RENDER, "Shows entites on a gui");
        this.size = new NumberSetting("Size", 90.0, 125.0, 75.0, 1.0);
        this.playerColor = new ColorSetting("Player Color", Color.RED);
        this.mobColor = new ColorSetting("Mob Color", Color.ORANGE);
        this.animalColor = new ColorSetting("Animal Color", Color.BLUE);
        this.itemColor = new ColorSetting("Item Color", Color.YELLOW);
        this.targets = new MultipleBoolSetting("Entities", new BooleanSetting[] { new BooleanSetting("Players", true), new BooleanSetting("Mobs", true), new BooleanSetting("Animals", true), new BooleanSetting("Items", true) });
        this.drag = Tenacity.INSTANCE.createDrag(this, "radar", 5.0f, 40.0f);
        this.entities = new ArrayList<Entity>();
        this.colorWheel = new GradientColorWheel();
        this.playerColor.addParent(this.targets, targetsSetting -> targetsSetting.getSetting("Players").isEnabled());
        this.mobColor.addParent(this.targets, targetsSetting -> targetsSetting.getSetting("Mobs").isEnabled());
        this.animalColor.addParent(this.targets, targetsSetting -> targetsSetting.getSetting("Animals").isEnabled());
        this.itemColor.addParent(this.targets, targetsSetting -> targetsSetting.getSetting("Items").isEnabled());
        this.addSettings(this.targets, this.colorWheel.createModeSetting("Color Mode", new String[0]), this.colorWheel.getColorSetting(), this.size, this.playerColor, this.mobColor, this.animalColor, this.itemColor);
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent e) {
        final float x = this.drag.getX();
        final float y = this.drag.getY();
        final float size = this.size.getValue().floatValue();
        final float middleX = x + size / 2.0f;
        final float middleY = y + size / 2.0f;
        if (e.getBloomOptions().getSetting("Radar").isEnabled()) {
            RoundedUtil.drawGradientRound(x, y, size, size, 6.0f, this.colorWheel.getColor1(), this.colorWheel.getColor4(), this.colorWheel.getColor2(), this.colorWheel.getColor3());
        }
        else {
            RoundedUtil.drawRound(x, y, size, size, 6.0f, Color.BLACK);
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent e) {
        this.getEntities();
        final float x = this.drag.getX();
        final float y = this.drag.getY();
        final float size = this.size.getValue().floatValue();
        final float middleX = x + size / 2.0f;
        final float middleY = y + size / 2.0f;
        this.drag.setWidth(size);
        this.drag.setHeight(size);
        final Color lineColor = new Color(255, 255, 255, 180);
        this.colorWheel.setColors();
        final float alpha = 0.85f;
        RoundedUtil.drawGradientRound(x, y, size, size, 6.0f, ColorUtil.applyOpacity(this.colorWheel.getColor1(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor4(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor2(), alpha), ColorUtil.applyOpacity(this.colorWheel.getColor3(), alpha));
        Gui.drawRect2(x - 1.0f, y + (size / 2.0f - 0.5), size + 2.0f, 1.0, lineColor.getRGB());
        Gui.drawRect2(x + (size / 2.0f - 0.5), y - 1.0f, 1.0, size + 2.0f, lineColor.getRGB());
        StencilUtil.initStencilToWrite();
        RenderUtil.renderRoundedRect(x, y, size, size, 6.0f, -1);
        StencilUtil.readStencilBuffer(1);
        GLUtil.startRotate(middleX, middleY, Radar.mc.thePlayer.rotationYaw);
        for (final Entity entity : this.entities) {
            final double xDiff = MathUtils.interpolate(entity.prevPosX, entity.posX, Radar.mc.timer.renderPartialTicks) - MathUtils.interpolate(Radar.mc.thePlayer.prevPosX, Radar.mc.thePlayer.posX, Radar.mc.timer.renderPartialTicks);
            final double zDiff = MathUtils.interpolate(entity.prevPosZ, entity.posZ, Radar.mc.timer.renderPartialTicks) - MathUtils.interpolate(Radar.mc.thePlayer.prevPosZ, Radar.mc.thePlayer.posZ, Radar.mc.timer.renderPartialTicks);
            if (xDiff + zDiff < size / 2.0f) {
                final float translatedX = (float)(middleX - xDiff);
                final float translatedY = (float)(middleY - zDiff);
                RoundedUtil.drawRound(translatedX, translatedY, 3.0f, 3.0f, 1.0f, this.getColor(entity));
            }
        }
        GLUtil.endRotate();
        StencilUtil.uninitStencilBuffer();
    }
    
    public Color getColor(final Entity entity) {
        Color color = Color.WHITE;
        if (entity instanceof EntityPlayer) {
            color = this.playerColor.getColor();
        }
        if (entity instanceof EntityMob || entity instanceof EntityWaterMob) {
            color = this.mobColor.getColor();
        }
        if (entity instanceof EntityAnimal) {
            color = this.animalColor.getColor();
        }
        if (entity instanceof EntityItem) {
            color = this.itemColor.getColor();
        }
        return color;
    }
    
    public void getEntities() {
        this.entities.clear();
        for (final Entity entity : Radar.mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer && this.targets.getSetting("Players").isEnabled() && entity != Radar.mc.thePlayer && !entity.isInvisible()) {
                this.entities.add(entity);
            }
            if ((entity instanceof EntityMob || entity instanceof EntityWaterMob) && this.targets.getSetting("Mobs").isEnabled()) {
                this.entities.add(entity);
            }
            if (entity instanceof EntityAnimal && this.targets.getSetting("Animals").isEnabled()) {
                this.entities.add(entity);
            }
            if (entity instanceof EntityItem && this.targets.getSetting("Items").isEnabled()) {
                this.entities.add(entity);
            }
        }
    }
}
