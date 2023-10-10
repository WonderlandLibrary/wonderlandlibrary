/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.RENDER;

import com.darkmagician6.eventapi.EventTarget;
import java.util.List;
import me.AveReborn.Value;
import me.AveReborn.events.EventRender;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Projectiles
extends Mod {
    public Value<Boolean> arrowESP = new Value<Boolean>("Projectiles_ArrowESP", true);
    private EntityLivingBase entity;
    private MovingObjectPosition blockCollision;
    private MovingObjectPosition entityCollision;
    private AxisAlignedBB aim;

    public Projectiles() {
        super("Projectiles", Category.RENDER);
    }

    @EventTarget
    public void arrowESP(EventRender event) {
        int item;
        EntityPlayerSP player = Minecraft.thePlayer;
        ItemStack stack = player.inventory.getCurrentItem();
        if (Minecraft.thePlayer.inventory.getCurrentItem() != null && ((item = Item.getIdFromItem(Minecraft.thePlayer.getHeldItem().getItem())) == 261 || item == 368 || item == 332 || item == 344)) {
            double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - Math.cos(Math.toRadians(player.rotationYaw)) * 0.1599999964237213;
            double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)this.mc.timer.renderPartialTicks + (double)player.getEyeHeight() - 0.1;
            double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - Math.sin(Math.toRadians(player.rotationYaw)) * 0.1599999964237213;
            double itemBow = stack.getItem() instanceof ItemBow ? 1.0f : 0.4f;
            double yaw = Math.toRadians(player.rotationYaw);
            double pitch = Math.toRadians(player.rotationPitch);
            double trajectoryX = (- Math.sin(yaw)) * Math.cos(pitch) * itemBow;
            double trajectoryY = (- Math.sin(pitch)) * itemBow;
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
                trajectoryX *= (double)(bowPower *= 3.0f);
                trajectoryY *= (double)bowPower;
                trajectoryZ *= (double)bowPower;
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
            GL11.glColor4f(0.0f, 1.0f, 0.2f, 0.5f);
            GL11.glBegin(3);
            int i2 = 0;
            while (i2 < 2000) {
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                GL11.glVertex3d(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);
                trajectoryY *= 0.999;
                Vec3 vec = new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
                this.blockCollision = this.mc.theWorld.rayTraceBlocks(vec, new Vec3(posX += (trajectoryX *= 0.999) * 0.1, posY += (trajectoryY -= gravity * 0.1) * 0.1, posZ += (trajectoryZ *= 0.999) * 0.1));
                for (Entity o2 : this.mc.theWorld.getLoadedEntityList()) {
                    if (!(o2 instanceof EntityLivingBase) || o2 instanceof EntityPlayerSP) continue;
                    this.entity = (EntityLivingBase)o2;
                    AxisAlignedBB entityBoundingBox = this.entity.getEntityBoundingBox().expand(0.3, 0.3, 0.3);
                    this.entityCollision = entityBoundingBox.calculateIntercept(vec, new Vec3(posX, posY, posZ));
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
                ++i2;
            }
            GL11.glEnd();
            this.mc.getRenderManager();
            double renderX = posX - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double renderY = posY - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double renderZ = posZ - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glTranslated(renderX - 0.5, renderY - 0.5, renderZ - 0.5);
            switch (this.blockCollision.sideHit.getIndex()) {
                case 2: 
                case 3: {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                    this.aim = new AxisAlignedBB(0.0, 0.5, -1.0, 1.0, 0.45, 0.0);
                    break;
                }
                case 4: 
                case 5: {
                    GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                    this.aim = new AxisAlignedBB(0.0, -0.5, 0.0, 1.0, -0.45, 1.0);
                    break;
                }
                default: {
                    this.aim = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 0.45, 1.0);
                }
            }
            this.drawBox(this.aim);
            RenderGlobal.func_181561_a(this.aim);
            GL11.glPopMatrix();
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glPopMatrix();
        }
        if (this.arrowESP.getValueState().booleanValue()) {
            for (Object object : this.mc.theWorld.loadedEntityList) {
                if (!(object instanceof EntityArrow)) continue;
                EntityArrow arrow = (EntityArrow)object;
                if (arrow.inGround) continue;
                double posX = arrow.posX;
                double posY = arrow.posY;
                double posZ = arrow.posZ;
                double motionX = arrow.motionX;
                double motionY = arrow.motionY;
                double motionZ = arrow.motionZ;
                MovingObjectPosition landingPosition2 = null;
                boolean hasLanded2 = false;
                float gravity2 = 0.05f;
                Projectiles.enableRender3D(true);
                Projectiles.setColor(3196666);
                GL11.glLineWidth(2.0f);
                GL11.glBegin(3);
                int limit2 = 0;
                while (!hasLanded2 && limit2 < 300) {
                    BlockPos var20;
                    Block var21;
                    Vec3 posBefore2 = new Vec3(posX, posY, posZ);
                    Vec3 posAfter2 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
                    landingPosition2 = this.mc.theWorld.rayTraceBlocks(posBefore2, posAfter2, false, true, false);
                    if (landingPosition2 != null) {
                        hasLanded2 = true;
                    }
                    if ((var21 = this.mc.theWorld.getBlockState(var20 = new BlockPos(posX += motionX, posY += motionY, posZ += motionZ)).getBlock()).getMaterial() == Material.water) {
                        motionX *= 0.6;
                        motionY *= 0.6;
                        motionZ *= 0.6;
                    } else {
                        motionX *= 0.99;
                        motionY *= 0.99;
                        motionZ *= 0.99;
                    }
                    motionY -= 0.05000000074505806;
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    this.mc.getRenderManager();
                    GL11.glVertex3d(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ);
                    ++limit2;
                }
                GL11.glEnd();
                Projectiles.disableRender3D(true);
            }
        }
    }

    public void drawBox(AxisAlignedBB bb2) {
        GL11.glBegin(7);
        GL11.glVertex3d(bb2.minX, bb2.minY, bb2.minZ);
        GL11.glVertex3d(bb2.maxX, bb2.minY, bb2.minZ);
        GL11.glVertex3d(bb2.maxX, bb2.minY, bb2.maxZ);
        GL11.glVertex3d(bb2.minX, bb2.minY, bb2.maxZ);
        GL11.glVertex3d(bb2.minX, bb2.maxY, bb2.minZ);
        GL11.glVertex3d(bb2.minX, bb2.maxY, bb2.maxZ);
        GL11.glVertex3d(bb2.maxX, bb2.maxY, bb2.maxZ);
        GL11.glVertex3d(bb2.maxX, bb2.maxY, bb2.minZ);
        GL11.glVertex3d(bb2.minX, bb2.minY, bb2.minZ);
        GL11.glVertex3d(bb2.minX, bb2.maxY, bb2.minZ);
        GL11.glVertex3d(bb2.maxX, bb2.maxY, bb2.minZ);
        GL11.glVertex3d(bb2.maxX, bb2.minY, bb2.minZ);
        GL11.glVertex3d(bb2.maxX, bb2.minY, bb2.minZ);
        GL11.glVertex3d(bb2.maxX, bb2.maxY, bb2.minZ);
        GL11.glVertex3d(bb2.maxX, bb2.maxY, bb2.maxZ);
        GL11.glVertex3d(bb2.maxX, bb2.minY, bb2.maxZ);
        GL11.glVertex3d(bb2.minX, bb2.minY, bb2.maxZ);
        GL11.glVertex3d(bb2.maxX, bb2.minY, bb2.maxZ);
        GL11.glVertex3d(bb2.maxX, bb2.maxY, bb2.maxZ);
        GL11.glVertex3d(bb2.minX, bb2.maxY, bb2.maxZ);
        GL11.glVertex3d(bb2.minX, bb2.minY, bb2.minZ);
        GL11.glVertex3d(bb2.minX, bb2.minY, bb2.maxZ);
        GL11.glVertex3d(bb2.minX, bb2.maxY, bb2.maxZ);
        GL11.glVertex3d(bb2.minX, bb2.maxY, bb2.minZ);
        GL11.glEnd();
    }

    public static void setColor(int colorHex) {
        float alpha = (float)(colorHex >> 24 & 255) / 255.0f;
        float red = (float)(colorHex >> 16 & 255) / 255.0f;
        float green = (float)(colorHex >> 8 & 255) / 255.0f;
        float blue = (float)(colorHex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha == 0.0f ? 1.0f : alpha);
    }

    public static void enableRender3D(boolean disableDepth) {
        if (disableDepth) {
            GL11.glDepthMask(false);
            GL11.glDisable(2929);
        }
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.0f);
    }

    public static void disableRender3D(boolean enableDepth) {
        if (enableDepth) {
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
        }
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

