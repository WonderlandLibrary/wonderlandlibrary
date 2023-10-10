// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.augustus.utils.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import net.augustus.modules.combat.AntiBot;
import net.minecraft.entity.player.EntityPlayer;
import net.augustus.events.EventTick;
import net.augustus.settings.Setting;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.RainbowUtil;
import net.minecraft.entity.EntityLivingBase;
import java.util.ArrayList;
import net.augustus.modules.Module;

public class ESP extends Module
{
    public final ArrayList<EntityLivingBase> livingBases;
    private final RainbowUtil rainbowUtil;
    public StringValue mode;
    public ColorSetting color;
    public ColorSetting outlineColor;
    public BooleanValue rainbow;
    public DoubleValue rainbowSpeed;
    public DoubleValue rainbowAlpha;
    public BooleanValue teamColor;
    public BooleanValue otherColorOnHit;
    public ColorSetting hitColor;
    public BooleanValue player;
    public BooleanValue mob;
    public BooleanValue animal;
    public BooleanValue invisible;
    public DoubleValue lineWidth;
    public BooleansSetting entities;
    
    public ESP() {
        super("ESP", new Color(47, 134, 124, 255), Categorys.RENDER);
        this.livingBases = new ArrayList<EntityLivingBase>();
        this.rainbowUtil = new RainbowUtil();
        this.mode = new StringValue(7, "Mode", this, "Box", new String[] { "Box", "FakeCorner", "Fake2D", "Vanilla" });
        this.color = new ColorSetting(2, "Color", this, new Color(21, 121, 230, 65));
        this.outlineColor = new ColorSetting(9, "Color", this, new Color(21, 121, 230, 65));
        this.rainbow = new BooleanValue(11, "Rainbow", this, false);
        this.rainbowSpeed = new DoubleValue(12, "RainbowSpeed", this, 55.0, 0.0, 1000.0, 0);
        this.rainbowAlpha = new DoubleValue(13, "RainbowAlpha", this, 80.0, 0.0, 255.0, 0);
        this.teamColor = new BooleanValue(10, "TeamColor", this, true);
        this.otherColorOnHit = new BooleanValue(15, "HitColor", this, true);
        this.hitColor = new ColorSetting(14, "HitColor", this, new Color(255, 0, 0));
        this.player = new BooleanValue(3, "Player", this, true);
        this.mob = new BooleanValue(4, "Mob", this, true);
        this.animal = new BooleanValue(5, "Animal", this, false);
        this.invisible = new BooleanValue(6, "Invisible", this, false);
        this.lineWidth = new DoubleValue(1, "LineWidth", this, 6.0, 0.0, 15.0, 0);
        this.entities = new BooleansSetting(8, "Entities", this, new Setting[] { this.player, this.mob, this.animal, this.invisible });
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        if (ESP.mc.theWorld == null) {
            return;
        }
        this.setDisplayName(this.getName() + " §8" + this.mode.getSelected());
        this.livingBases.clear();
        if (this.mode.getSelected().equals("Vanilla")) {
            return;
        }
        for (final Object object : ESP.mc.theWorld.loadedEntityList) {
            if (object instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)object;
                if (entity.isInvisible()) {
                    if (!this.invisible.getBoolean()) {
                        continue;
                    }
                    if (entity instanceof EntityPlayer && this.player.getBoolean() && ESP.mc.thePlayer != entity) {
                        if (ESP.mm.antiBot.isToggled()) {
                            if (!AntiBot.bots.contains(entity)) {
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
                    if (entity instanceof EntityPlayer && this.player.getBoolean() && ESP.mc.thePlayer != entity) {
                        if (ESP.mm.antiBot.isToggled()) {
                            if (!AntiBot.bots.contains(entity)) {
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
            }
        }
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        if (this.rainbow.getBoolean()) {
            this.rainbowUtil.updateRainbow((this.rainbowSpeed.getValue() == 1000.0) ? ((float)(this.rainbowSpeed.getValue() * 9.999999747378752E-6)) : ((float)(this.rainbowSpeed.getValue() * 9.999999974752427E-7)), (int)this.rainbowAlpha.getValue());
        }
        if (this.livingBases.isEmpty()) {
            return;
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
    
    private void render(final EntityLivingBase entity, float red, float green, float blue, final float alpha) {
        float lineWidth = (float)this.lineWidth.getValue() / 2.0f;
        int i = 16777215;
        if (this.teamColor.getBoolean() && entity instanceof EntityPlayer) {
            final ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entity.getTeam();
            if (scoreplayerteam != null) {
                final String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
                if (s.length() >= 2) {
                    i = ESP.mc.fontRendererObj.getColorCode(s.charAt(1));
                }
            }
            red = (i >> 16 & 0xFF) / 255.0f;
            green = (i >> 8 & 0xFF) / 255.0f;
            blue = (i & 0xFF) / 255.0f;
        }
        if (entity.hurtTime > 0 && this.otherColorOnHit.getBoolean()) {
            red = this.hitColor.getColor().getRed() / 255.0f;
            green = this.hitColor.getColor().getGreen() / 255.0f;
            blue = this.hitColor.getColor().getBlue() / 255.0f;
        }
        if (ESP.mc.thePlayer.getDistanceToEntity(entity) > 1.0f) {
            double d0 = 1.0f - ESP.mc.thePlayer.getDistanceToEntity(entity) / 20.0f;
            if (d0 < 0.3) {
                d0 = 0.3;
            }
            lineWidth *= (float)d0;
        }
        final String selected = this.mode.getSelected();
        switch (selected) {
            case "Box": {
                RenderUtil.drawEntityESP(entity, red, green, blue, alpha, 1.0f, lineWidth);
                break;
            }
            case "FakeCorner": {
                RenderUtil.drawCornerESP(entity, red, green, blue);
                break;
            }
            case "Fake2D": {
                RenderUtil.drawFake2DESP(entity, red, green, blue);
                break;
            }
        }
    }
    
    public boolean canRender(final EntityLivingBase entity) {
        if (entity.isInvisible()) {
            return this.invisible.getBoolean() && ((entity instanceof EntityPlayer && this.player.getBoolean() && ESP.mc.thePlayer != entity) || (entity instanceof EntityMob && this.mob.getBoolean()) || (entity instanceof EntityAnimal && this.animal.getBoolean()));
        }
        return (entity instanceof EntityPlayer && this.player.getBoolean() && ESP.mc.thePlayer != entity) || (entity instanceof EntityMob && this.mob.getBoolean()) || (entity instanceof EntityAnimal && this.animal.getBoolean());
    }
    
    public RainbowUtil getRainbowUtil() {
        return this.rainbowUtil;
    }
}
