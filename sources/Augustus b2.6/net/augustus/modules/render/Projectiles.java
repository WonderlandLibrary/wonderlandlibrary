// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import net.lenni0451.eventapi.reflection.EventTarget;
import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.Entity;
import net.augustus.events.EventTick;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.util.MovingObjectPosition;
import java.util.HashMap;
import net.minecraft.util.Vec3;
import java.util.ArrayList;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.ColorSetting;
import net.augustus.modules.Module;

public class Projectiles extends Module
{
    public final ColorSetting color;
    public final BooleanValue calculatedColor;
    public final DoubleValue lineWidth;
    private final ArrayList<ArrayList<Vec3>> points;
    private final HashMap<ArrayList<Vec3>, MovingObjectPosition> hashMap;
    
    public Projectiles() {
        super("Projectiles", new Color(80, 107, 91), Categorys.RENDER);
        this.color = new ColorSetting(1, "Color", this, new Color(21, 121, 230));
        this.calculatedColor = new BooleanValue(1, "CalculateColor", this, true);
        this.lineWidth = new DoubleValue(2, "LineWidth", this, 6.0, 1.0, 12.0, 0);
        this.points = new ArrayList<ArrayList<Vec3>>();
        this.hashMap = new HashMap<ArrayList<Vec3>, MovingObjectPosition>();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.points.clear();
    }
    
    @EventTarget
    public void onEventTickEventTick(final EventTick eventTick) {
        this.points.clear();
        this.hashMap.clear();
        for (final Entity entity : Projectiles.mc.theWorld.loadedEntityList) {
            if (entity.ticksExisted >= 0 && !entity.onGround && !entity.isInWater() && (entity instanceof EntityArrow || entity instanceof EntitySnowball || entity instanceof EntityEgg || entity instanceof EntityEnderPearl || entity instanceof EntityFireball)) {
                boolean b = true;
                int ticksInAir = 0;
                double posX = entity.posX;
                double posY = entity.posY;
                double posZ = entity.posZ;
                double motionX = entity.motionX;
                double motionY = entity.motionY;
                double motionZ = entity.motionZ;
                float rotationYaw = entity.rotationYaw;
                final float rotationPitch = entity.rotationPitch;
                float prevRotationPitch = entity.prevRotationPitch;
                float prevRotationYaw = entity.prevRotationYaw;
                final ArrayList<Vec3> vec3s = new ArrayList<Vec3>();
                final MovingObjectPosition objectPosition = null;
                while (b) {
                    if (ticksInAir > 300) {
                        b = false;
                    }
                    ++ticksInAir;
                    final Vec3 vec3 = new Vec3(posX, posY, posZ);
                    final Vec3 vec4 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
                    final MovingObjectPosition movingobjectposition = Projectiles.mc.theWorld.rayTraceBlocks(vec3, vec4);
                    if (movingobjectposition != null) {
                        b = false;
                    }
                    posX += motionX;
                    posY += motionY;
                    posZ += motionZ;
                    final float f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                    rotationYaw = (float)(MathHelper.func_181159_b(motionX, motionZ) * 180.0 / 3.141592653589793);
                    if (entity instanceof EntityFireball) {
                        rotationYaw = (float)(MathHelper.func_181159_b(motionX, motionZ) * 180.0 / 3.141592653589793) + 90.0f;
                    }
                    while (rotationPitch - prevRotationPitch >= 180.0f) {
                        prevRotationPitch += 360.0f;
                    }
                    while (rotationYaw - prevRotationYaw < -180.0f) {
                        prevRotationYaw -= 360.0f;
                    }
                    while (rotationYaw - prevRotationYaw >= 180.0f) {
                        prevRotationYaw += 360.0f;
                    }
                    float f2 = 0.99f;
                    if (entity instanceof EntityFireball) {
                        f2 = 0.95f;
                    }
                    float f3 = 0.03f;
                    if (entity instanceof EntityArrow) {
                        f3 = 0.05f;
                    }
                    else if (entity instanceof EntityFireball) {
                        f3 = 0.0f;
                    }
                    if (entity instanceof EntityFireball) {
                        final EntityFireball entityFireball = (EntityFireball)entity;
                        motionX += entityFireball.accelerationX;
                        motionY += entityFireball.accelerationY;
                        motionZ += entityFireball.accelerationZ;
                    }
                    motionX *= f2;
                    motionY *= f2;
                    motionZ *= f2;
                    motionY -= f3;
                    vec3s.add(new Vec3(posX, posY, posZ));
                }
                this.points.add(vec3s);
                this.hashMap.put(vec3s, objectPosition);
            }
        }
    }
    
    @EventTarget
    public void onEventRender3D(final EventRender3D eventRender3D) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GlStateManager.disableCull();
        GL11.glDepthMask(false);
        GL11.glLineWidth((float)this.lineWidth.getValue() / 2.0f);
        for (final ArrayList<Vec3> vec3s : this.points) {
            if (vec3s.size() > 1) {
                if (this.calculatedColor.getBoolean()) {
                    final double dist = Math.min(Math.max(Projectiles.mc.thePlayer.getDistance(vec3s.get(1).xCoord, vec3s.get(1).yCoord, vec3s.get(1).zCoord), 6.0), 36.0) - 6.0;
                    final Color color = new Color(this.color.getColor().getRed(), this.color.getColor().getGreen(), this.color.getColor().getBlue(), this.color.getColor().getAlpha());
                    final float[] hsbColor = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                    final int colorRGB = Color.HSBtoRGB((float)(0.01141552533954382 * dist), hsbColor[1], hsbColor[2]);
                    final float f = (colorRGB >> 24 & 0xFF) / 255.0f;
                    final float red = (colorRGB >> 16 & 0xFF) / 255.0f;
                    final float green = (colorRGB >> 8 & 0xFF) / 255.0f;
                    final float blue = (colorRGB & 0xFF) / 255.0f;
                    GL11.glColor4f(red, green, blue, 0.85f);
                }
                else {
                    GL11.glColor4f(this.color.getColor().getRed() / 255.0f, this.color.getColor().getGreen() / 255.0f, this.color.getColor().getBlue() / 255.0f, 0.85f);
                }
                final Tessellator tessellator = Tessellator.getInstance();
                final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                worldrenderer.begin(3, DefaultVertexFormats.POSITION);
                for (final Vec3 vec3 : vec3s) {
                    worldrenderer.pos((float)vec3.xCoord - Projectiles.mc.getRenderManager().getRenderPosX(), (float)vec3.yCoord - Projectiles.mc.getRenderManager().getRenderPosY(), (float)vec3.zCoord - Projectiles.mc.getRenderManager().getRenderPosZ()).endVertex();
                }
                tessellator.draw();
            }
        }
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthMask(true);
        GlStateManager.enableCull();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2848);
    }
}
