/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.wintware.client.module.visual;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventRender3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Trajectories
extends Module {
    private double x;
    private double y;
    private double z;
    private double motionX;
    private double motionY;
    private double motionZ;
    private final boolean hitEntity = false;
    private double r;
    private double g;
    private double b;
    public double pX = -9000.0;
    public double pY = -9000.0;
    public double pZ = -9000.0;
    private EntityLivingBase entity;
    private RayTraceResult blockCollision;
    private RayTraceResult entityCollision;
    private static AxisAlignedBB aim;

    public Trajectories() {
        super("Trajectories", Category.Visuals);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        try {
            if (Minecraft.player.inventory.getCurrentItem() != null) {
                EntityPlayerSP player = Minecraft.player;
                ItemStack stack = player.inventory.getCurrentItem();
                int itemMain = Item.getIdFromItem(Minecraft.player.getHeldItem(EnumHand.MAIN_HAND).getItem());
                int itemOff = Item.getIdFromItem(Minecraft.player.getHeldItem(EnumHand.OFF_HAND).getItem());
                if (itemMain == 261 || itemOff == 261 || itemMain == 368 || itemOff == 368 || itemMain == 332 || itemOff == 332 || itemMain == 344 || itemOff == 344) {
                    double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)Trajectories.mc.timer.renderPartialTicks - Math.cos(Math.toRadians(player.rotationYaw)) * (double)0.16f;
                    double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)Trajectories.mc.timer.renderPartialTicks + (double)player.getEyeHeight() - 0.1;
                    double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)Trajectories.mc.timer.renderPartialTicks - Math.sin(Math.toRadians(player.rotationYaw)) * (double)0.16f;
                    double itemBow = stack.getItem() instanceof ItemBow ? 1.0 : (double)0.4f;
                    double yaw = Math.toRadians(player.rotationYaw);
                    double pitch = Math.toRadians(player.rotationPitch);
                    double trajectoryX = -Math.sin(yaw) * Math.cos(pitch) * itemBow;
                    double trajectoryY = -Math.sin(pitch) * itemBow;
                    double trajectoryZ = Math.cos(yaw) * Math.cos(pitch) * itemBow;
                    double trajectory = Math.sqrt(trajectoryX * trajectoryX + trajectoryY * trajectoryY + trajectoryZ * trajectoryZ);
                    trajectoryX /= trajectory;
                    trajectoryY /= trajectory;
                    trajectoryZ /= trajectory;
                    if (stack.getItem() instanceof ItemBow) {
                        float bowPower = (float)(72000 - player.getItemInUseCount()) / 20.0f;
                        if ((bowPower = (bowPower * bowPower + bowPower * 2.0f) / 3.0f) > 1.0f) {
                            bowPower = 1.0f;
                        }
                        trajectoryX *= bowPower *= 3.0f;
                        trajectoryY *= bowPower;
                        trajectoryZ *= bowPower;
                    } else {
                        trajectoryX *= 1.5;
                        trajectoryY *= 1.5;
                        trajectoryZ *= 1.5;
                    }
                    GL11.glPushMatrix();
                    GL11.glDisable(3553);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                    GL11.glEnable(2848);
                    GL11.glLineWidth(2.0f);
                    double gravity = stack.getItem() instanceof ItemBow ? 0.05 : 0.03;
                    GL11.glColor4f(255.0f, 255.0f, 255.0f, 0.5f);
                    GL11.glBegin(3);
                    for (int i = 0; i < 2000; ++i) {
                        mc.getRenderManager();
                        mc.getRenderManager();
                        mc.getRenderManager();
                        GL11.glVertex3d(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);
                        trajectoryY *= 0.999;
                        Vec3d vec = new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
                        this.blockCollision = Trajectories.mc.world.rayTraceBlocks(vec, new Vec3d(posX += (trajectoryX *= 0.999) * 0.1, posY += (trajectoryY -= gravity * 0.1) * 0.1, posZ += (trajectoryZ *= 0.999) * 0.1));
                        for (Entity o : Trajectories.mc.world.getLoadedEntityList()) {
                            if (!(o instanceof EntityLivingBase) || o instanceof EntityPlayerSP) continue;
                            this.entity = (EntityLivingBase)o;
                            AxisAlignedBB entityBoundingBox = this.entity.getEntityBoundingBox().expand(0.3, 0.3, 0.3);
                            this.entityCollision = entityBoundingBox.calculateIntercept(vec, new Vec3d(posX, posY, posZ));
                            if (this.entityCollision != null) {
                                this.blockCollision = this.entityCollision;
                            }
                            if (this.entityCollision != null) {
                                GL11.glColor4f(1.0f, 0.0f, 0.2f, 0.5f);
                            }
                            if (this.entityCollision == null) continue;
                            this.blockCollision = this.entityCollision;
                        }
                        if (this.blockCollision != null) break;
                    }
                    GL11.glEnd();
                    mc.getRenderManager();
                    double renderX = posX - RenderManager.renderPosX;
                    mc.getRenderManager();
                    double renderY = posY - RenderManager.renderPosY;
                    mc.getRenderManager();
                    double renderZ = posZ - RenderManager.renderPosZ;
                    GL11.glPushMatrix();
                    GL11.glTranslated(renderX - 0.5, renderY - 0.5, renderZ - 0.5);
                    switch (this.blockCollision.sideHit.getIndex()) {
                        case 2: 
                        case 3: {
                            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                            aim = new AxisAlignedBB(0.0, 0.5, -1.0, 1.0, 0.45, 0.0);
                            break;
                        }
                        case 4: 
                        case 5: {
                            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                            aim = new AxisAlignedBB(0.0, -0.5, 0.0, 1.0, -0.45, 1.0);
                            break;
                        }
                        default: {
                            aim = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 0.45, 1.0);
                        }
                    }
                    Trajectories.drawBox(aim);
                    Trajectories.func_181561_a(aim);
                    GL11.glPopMatrix();
                    GL11.glDisable(3042);
                    GL11.glEnable(3553);
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                    GL11.glDisable(2848);
                    GL11.glPopMatrix();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void func_181561_a(AxisAlignedBB p_181561_0_) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBox(AxisAlignedBB bb) {
        GL11.glBegin(7);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();
    }
}

