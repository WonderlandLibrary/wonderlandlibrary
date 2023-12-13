// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.utils.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import net.augustus.modules.misc.MidClick;
import net.augustus.modules.combat.AntiBot;
import net.minecraft.entity.player.EntityPlayer;
import net.augustus.events.EventTick;
import net.augustus.settings.Setting;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.entity.EntityLivingBase;
import java.util.ArrayList;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.RainbowUtil;
import net.augustus.modules.Module;

public class Tracers extends Module
{
    private final RainbowUtil rainbowUtil;
    public ColorSetting color;
    public StringValue mode;
    public BooleanValue staticColor;
    public BooleanValue rainbow;
    public DoubleValue rainbowSpeed;
    public BooleanValue player;
    public BooleanValue mob;
    public BooleanValue animal;
    public BooleanValue invisible;
    public DoubleValue lineWidth;
    public BooleansSetting entities;
    private boolean bobbing;
    private ArrayList<EntityLivingBase> livingBases;
    
    public Tracers() {
        super("Tracers", new Color(12, 58, 133), Categorys.RENDER);
        this.rainbowUtil = new RainbowUtil();
        this.color = new ColorSetting(2, "Color", this, new Color(21, 121, 230, 65));
        this.mode = new StringValue(7, "Mode", this, "Feet", new String[] { "Feet", "CrossHair" });
        this.staticColor = new BooleanValue(9, "StaticColor", this, false);
        this.rainbow = new BooleanValue(11, "Rainbow", this, false);
        this.rainbowSpeed = new DoubleValue(12, "RainbowSpeed", this, 55.0, 0.0, 1000.0, 0);
        this.player = new BooleanValue(3, "Player", this, true);
        this.mob = new BooleanValue(4, "Mob", this, true);
        this.animal = new BooleanValue(5, "Animal", this, false);
        this.invisible = new BooleanValue(6, "Invisible", this, false);
        this.lineWidth = new DoubleValue(1, "LineWidth", this, 6.0, 0.1, 15.0, 1);
        this.entities = new BooleansSetting(8, "Entities", this, new Setting[] { this.player, this.mob, this.animal, this.invisible });
        this.livingBases = new ArrayList<EntityLivingBase>();
    }
    
    @Override
    public void onEnable() {
        this.bobbing = Tracers.mc.gameSettings.viewBobbing;
        Tracers.mc.gameSettings.viewBobbing = false;
    }
    
    @Override
    public void onDisable() {
        Tracers.mc.gameSettings.viewBobbing = this.bobbing;
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        this.livingBases = new ArrayList<EntityLivingBase>();
        for (final Object object : Tracers.mc.theWorld.loadedEntityList) {
            if (object instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)object;
                if (entity.isInvisible()) {
                    if (!this.invisible.getBoolean()) {
                        continue;
                    }
                    if (entity instanceof EntityPlayer && this.player.getBoolean() && Tracers.mc.thePlayer != entity && !AntiBot.bots.contains(entity) && !Tracers.mm.teams.getTeammates().contains(entity)) {
                        if (MidClick.friends.contains(entity.getName())) {
                            if (Tracers.mm.midClick.noFiends) {
                                this.livingBases.add(entity);
                            }
                        }
                        else {
                            this.livingBases.add(entity);
                        }
                    }
                    if (entity instanceof EntityMob && this.mob.getBoolean()) {
                        this.livingBases.add(entity);
                    }
                    if (!(entity instanceof EntityAnimal) || !this.animal.getBoolean()) {
                        continue;
                    }
                    this.livingBases.add(entity);
                }
                else {
                    if (entity instanceof EntityPlayer && this.player.getBoolean() && Tracers.mc.thePlayer != entity && !AntiBot.bots.contains(entity) && !MidClick.friends.contains(entity.getName()) && !Tracers.mm.teams.getTeammates().contains(entity)) {
                        this.livingBases.add(entity);
                    }
                    if (entity instanceof EntityMob && this.mob.getBoolean()) {
                        this.livingBases.add(entity);
                    }
                    if (!(entity instanceof EntityAnimal) || !this.animal.getBoolean()) {
                        continue;
                    }
                    this.livingBases.add(entity);
                }
            }
        }
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        if (this.rainbow.getBoolean()) {
            this.rainbowUtil.updateRainbow((this.rainbowSpeed.getValue() == 1000.0) ? ((float)(this.rainbowSpeed.getValue() * 9.999999747378752E-6)) : ((float)(this.rainbowSpeed.getValue() * 9.999999974752427E-7)), 255);
        }
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GlStateManager.disableCull();
        GL11.glDepthMask(false);
        final float red = this.rainbow.getBoolean() ? (this.rainbowUtil.getColor().getRed() / 255.0f) : (this.color.getColor().getRed() / 225.0f);
        final float green = this.rainbow.getBoolean() ? (this.rainbowUtil.getColor().getGreen() / 255.0f) : (this.color.getColor().getGreen() / 225.0f);
        final float blue = this.rainbow.getBoolean() ? (this.rainbowUtil.getColor().getBlue() / 255.0f) : (this.color.getColor().getBlue() / 225.0f);
        final float alpha = this.rainbow.getBoolean() ? (this.rainbowUtil.getColor().getAlpha() / 255.0f) : (this.color.getColor().getAlpha() / 225.0f);
        for (final EntityLivingBase livingBase : this.livingBases) {
            this.render(livingBase, red, green, blue, alpha);
        }
        GL11.glDepthMask(true);
        GlStateManager.enableCull();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2848);
    }
    
    private void render(final EntityLivingBase e, float red, float green, float blue, final float alpha) {
        final float lineWidth = (float)this.lineWidth.getValue() / 2.0f;
        if (!this.staticColor.getBoolean() && !this.rainbow.getBoolean()) {
            final double dist = Math.min(Math.max(Tracers.mc.thePlayer.getDistanceToEntity(e), 6.0f), 36.0f) - 6.0f;
            final Color color = new Color(this.color.getColor().getRed(), this.color.getColor().getGreen(), this.color.getColor().getBlue(), this.color.getColor().getAlpha());
            final float[] hsbColor = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            final int colorRGB = Color.HSBtoRGB((float)(0.01141552533954382 * dist), hsbColor[1], hsbColor[2]);
            final float f = (colorRGB >> 24 & 0xFF) / 255.0f;
            red = (colorRGB >> 16 & 0xFF) / 255.0f;
            green = (colorRGB >> 8 & 0xFF) / 255.0f;
            blue = (colorRGB & 0xFF) / 255.0f;
        }
        RenderUtil.drawTracers(e, red, green, blue, alpha, lineWidth, this.mode.getSelected());
    }
}
