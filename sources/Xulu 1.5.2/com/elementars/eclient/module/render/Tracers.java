// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import java.util.Objects;
import net.minecraft.entity.monster.EntitySpider;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.ColorUtils;
import java.awt.Color;
import com.elementars.eclient.friend.Friends;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import java.util.function.Predicate;
import com.elementars.eclient.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.event.events.RenderEvent;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.util.HueCycler;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Tracers extends Module
{
    private final Value<Boolean> spine;
    private final Value<Boolean> players;
    private final Value<Boolean> friends;
    private final Value<Boolean> animals;
    private final Value<Boolean> mobs;
    private final Value<Float> range;
    private final Value<Float> opacity;
    HueCycler cycler;
    
    public Tracers() {
        super("Tracers", "Draws a line to entities in render distance", 0, Category.RENDER, true);
        this.spine = this.register(new Value<Boolean>("Spine", this, false));
        this.players = this.register(new Value<Boolean>("Players", this, true));
        this.friends = this.register(new Value<Boolean>("Friends", this, true));
        this.animals = this.register(new Value<Boolean>("Animals", this, false));
        this.mobs = this.register(new Value<Boolean>("Mobs", this, false));
        this.range = this.register(new Value<Float>("Range", this, 200.0f, 1.0f, 1000.0f));
        this.opacity = this.register(new Value<Float>("Opacity", this, 1.0f, 0.0f, 1.0f));
        this.cycler = new HueCycler(3600);
    }
    
    @Override
    public void onEnable() {
        Tracers.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void onDisable() {
        Tracers.EVENT_BUS.unregister((Object)this);
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        GlStateManager.pushMatrix();
        int colour;
        final float r;
        final float g;
        final float b;
        Minecraft.getMinecraft().world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> !EntityUtil.isFakeLocalPlayer(entity)).filter(entity -> (entity instanceof EntityPlayer) ? (this.players.getValue() && Tracers.mc.player != entity) : (EntityUtil.isPassive(entity) ? this.animals.getValue() : ((boolean)this.mobs.getValue()))).filter(entity -> Tracers.mc.player.getDistance(entity) < this.range.getValue()).forEach(entity -> {
            colour = this.getColour(entity);
            if (colour == Integer.MIN_VALUE) {
                if (!this.friends.getValue()) {
                    return;
                }
                else {
                    colour = this.cycler.current();
                }
            }
            r = (colour >>> 16 & 0xFF) / 255.0f;
            g = (colour >>> 8 & 0xFF) / 255.0f;
            b = (colour & 0xFF) / 255.0f;
            drawLineToEntity(entity, r, g, b, this.opacity.getValue());
            return;
        });
        GlStateManager.popMatrix();
    }
    
    @Override
    public void onUpdate() {
        this.cycler.next();
    }
    
    private void drawRainbowToEntity(final Entity entity, final float opacity) {
        final Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-(float)Math.toRadians(Minecraft.getMinecraft().player.rotationPitch)).rotateYaw(-(float)Math.toRadians(Minecraft.getMinecraft().player.rotationYaw));
        final double[] xyz = interpolate(entity);
        final double posx = xyz[0];
        final double posy = xyz[1];
        final double posz = xyz[2];
        final double posx2 = eyes.x;
        final double posy2 = eyes.y + Tracers.mc.player.getEyeHeight();
        final double posz2 = eyes.z;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        this.cycler.reset();
        this.cycler.setNext(opacity);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        Tracers.mc.entityRenderer.orientCamera(Tracers.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        this.cycler.setNext(opacity);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor3d(1.0, 1.0, 1.0);
        GlStateManager.enableLighting();
    }
    
    private int getColour(final Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (Friends.isFriend(entity.getName())) {
                return new Color(0.27f, 0.7f, 0.92f).getRGB();
            }
            final float distance = Tracers.mc.player.getDistance(entity);
            if (distance <= 32.0f) {
                return new Color(1.0f - distance / 32.0f / 2.0f, distance / 32.0f, 0.0f).getRGB();
            }
            return new Color(0.0f, 0.9f, 0.0f).getRGB();
        }
        else {
            if (EntityUtil.isPassive(entity)) {
                return ColorUtils.Colors.GREEN;
            }
            return ColorUtils.Colors.RED;
        }
    }
    
    public static double interpolate(final double now, final double then) {
        return then + (now - then) * Tracers.mc.getRenderPartialTicks();
    }
    
    public static double[] interpolate(final Entity entity) {
        final double posX = interpolate(entity.posX, entity.lastTickPosX) - Tracers.mc.getRenderManager().renderPosX;
        final double posY = interpolate(entity.posY, entity.lastTickPosY) - Tracers.mc.getRenderManager().renderPosY;
        final double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - Tracers.mc.getRenderManager().renderPosZ;
        return new double[] { posX, posY, posZ };
    }
    
    public static void drawLineToEntity(final Entity e, final float red, final float green, final float blue, final float opacity) {
        final double[] xyz = interpolate(e);
        drawLine(xyz[0], xyz[1], xyz[2], e.height, red, green, blue, opacity);
    }
    
    public static void drawLine(final double posx, final double posy, final double posz, final double up, final float red, final float green, final float blue, final float opacity) {
        final Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-(float)Math.toRadians(Minecraft.getMinecraft().player.rotationPitch)).rotateYaw(-(float)Math.toRadians(Minecraft.getMinecraft().player.rotationYaw));
        drawLineFromPosToPos(eyes.x, eyes.y + Tracers.mc.player.getEyeHeight(), eyes.z, posx, posy, posz, up, red, green, blue, opacity);
    }
    
    public static void drawLineFromPosToPos(final double posx, final double posy, final double posz, final double posx2, final double posy2, final double posz2, final double up, final float red, final float green, final float blue, final float opacity) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, opacity);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLoadIdentity();
        final boolean bobbing = Tracers.mc.gameSettings.viewBobbing;
        Tracers.mc.gameSettings.viewBobbing = false;
        Tracers.mc.entityRenderer.orientCamera(Tracers.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        if (Xulu.MODULE_MANAGER.getModuleT(Tracers.class).spine.getValue()) {
            GL11.glVertex3d(posx2, posy2, posz2);
            GL11.glVertex3d(posx2, posy2 + up, posz2);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glColor3d(1.0, 1.0, 1.0);
        Tracers.mc.gameSettings.viewBobbing = bobbing;
    }
    
    private EntityType getType(final Entity entity) {
        if (EntityUtil.isDrivenByPlayer(entity) || EntityUtil.isFakeLocalPlayer(entity)) {
            return EntityType.INVALID;
        }
        if (EntityUtil.isPlayer(entity)) {
            return EntityType.PLAYER;
        }
        if (EntityUtil.isPassive(entity)) {
            return EntityType.ANIMAL;
        }
        if (!EntityUtil.isPassive(entity) || entity instanceof EntitySpider) {
            return EntityType.HOSTILE;
        }
        return EntityType.HOSTILE;
    }
    
    private enum EntityType
    {
        PLAYER, 
        HOSTILE, 
        ANIMAL, 
        INVALID;
    }
    
    private class EntityRelations implements Comparable<EntityRelations>
    {
        private final Entity entity;
        private final EntityType entityType;
        
        public EntityRelations(final Entity entity) {
            Objects.requireNonNull(entity);
            this.entity = entity;
            this.entityType = Tracers.this.getType(entity);
        }
        
        public Entity getEntity() {
            return this.entity;
        }
        
        public EntityType getEntityType() {
            return this.entityType;
        }
        
        public Color getColor() {
            switch (this.entityType) {
                case PLAYER: {
                    return Color.YELLOW;
                }
                case HOSTILE: {
                    return Color.RED;
                }
                default: {
                    return Color.GREEN;
                }
            }
        }
        
        public float getDepth() {
            switch (this.entityType) {
                case PLAYER: {
                    return 15.0f;
                }
                case HOSTILE: {
                    return 10.0f;
                }
                case ANIMAL: {
                    return 5.0f;
                }
                default: {
                    return 0.0f;
                }
            }
        }
        
        public boolean isOptionEnabled() {
            switch (this.entityType) {
                case PLAYER: {
                    return Tracers.this.players.getValue();
                }
                case HOSTILE: {
                    return Tracers.this.mobs.getValue();
                }
                default: {
                    return Tracers.this.animals.getValue();
                }
            }
        }
        
        @Override
        public int compareTo(final EntityRelations o) {
            return this.getEntityType().compareTo(o.getEntityType());
        }
    }
}
