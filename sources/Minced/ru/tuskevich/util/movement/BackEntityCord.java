// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.movement;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import ru.tuskevich.util.color.ColorUtility;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import ru.tuskevich.Minced;
import ru.tuskevich.modules.impl.COMBAT.BackTrack;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.entity.Entity;

public class BackEntityCord
{
    public Entity entity;
    public List<Point> positions;
    Minecraft mc;
    
    public BackEntityCord(final Entity entityCoord) {
        this.positions = new ArrayList<Point>();
        this.mc = Minecraft.getMinecraft();
        this.entity = entityCoord;
    }
    
    public void tick() {
        if (this.entity instanceof EntityPlayerSP) {
            return;
        }
        if (Math.abs(this.entity.posX - this.entity.prevPosX) > 0.0 || Math.abs(this.entity.posZ - this.entity.prevPosZ) > 0.0 || Math.abs(this.entity.posY - this.entity.prevPosY) > 0.0) {
            final Point p = new Point(this.entity.getPositionVector().add(-0.5, 0.0, -0.5));
            p.yaw = (int)this.entity.rotationYaw;
            this.positions.add(p);
        }
        if (this.positions.size() > ((BackTrack)Minced.getInstance().manager.getModule("BackTrack")).range.getIntValue()) {
            this.positions.remove(0);
        }
        this.positions.removeIf(point -> point.ticks > ((BackTrack)Minced.getInstance().manager.getModule("BackTrack")).range.getIntValue());
    }
    
    public void click() {
        for (final Point point2 : this.positions) {
            final Point point = point2;
            ++point2.ticks;
            if (this.entity.getDistance(this.positions.get(0).vec3d.x, this.positions.get(0).vec3d.y, this.positions.get(0).vec3d.z) == 0.7071067690849304) {
                continue;
            }
            if (this.positions.size() <= 0) {
                continue;
            }
            Minecraft.getMinecraft();
            final Vec3d src = Minecraft.player.getPositionEyes(this.mc.getRenderPartialTicks());
            final Minecraft mc = this.mc;
            final Vec3d vectorForRotation = Minecraft.player.getLookVec();
            final Vec3d dest = src.add(vectorForRotation.x * 3.1, vectorForRotation.y * 3.1, vectorForRotation.z * 3.1);
            final RayTraceResult rayTraceResult = new AxisAlignedBB(point.vec3d.x + 0.8, point.vec3d.y, point.vec3d.z + 0.2, point.vec3d.x + 0.2, point.vec3d.y + 1.8, point.vec3d.z + 0.8).calculateIntercept(src, dest);
            if (rayTraceResult != null) {
                final PlayerControllerMP playerController = this.mc.playerController;
                final Minecraft mc2 = this.mc;
                playerController.attackEntity(Minecraft.player, this.entity);
                break;
            }
        }
    }
    
    public void click(final float yaw, final float pitch, final float dist) {
        for (final Point point2 : this.positions) {
            final Point point = point2;
            ++point2.ticks;
            if (this.entity.getDistance(this.positions.get(0).vec3d.x, this.positions.get(0).vec3d.y, this.positions.get(0).vec3d.z) == 0.7071067690849304) {
                continue;
            }
            if (this.positions.size() <= 0) {
                continue;
            }
            Minecraft.getMinecraft();
            final Vec3d src = Minecraft.player.getPositionEyes(this.mc.getRenderPartialTicks());
            final Minecraft mc = this.mc;
            final EntityPlayerSP player = Minecraft.player;
            final Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
            final Vec3d dest = src.add(vectorForRotation.x * dist, vectorForRotation.y * dist, vectorForRotation.z * dist);
            final RayTraceResult rayTraceResult = new AxisAlignedBB(point.vec3d.x + 0.8, point.vec3d.y, point.vec3d.z + 0.2, point.vec3d.x + 0.2, point.vec3d.y + 1.8, point.vec3d.z + 0.8).calculateIntercept(src, dest);
            if (rayTraceResult != null) {
                final PlayerControllerMP playerController = this.mc.playerController;
                final Minecraft mc2 = this.mc;
                playerController.attackEntity(Minecraft.player, this.entity);
                break;
            }
        }
    }
    
    public boolean checkClick(final float yaw, final float pitch, final float dist) {
        for (final Point point : this.positions) {
            if (this.entity.getDistance(this.positions.get(0).vec3d.x, this.positions.get(0).vec3d.y, this.positions.get(0).vec3d.z) == 0.7071067690849304) {
                continue;
            }
            if (this.positions.size() <= 0) {
                continue;
            }
            Minecraft.getMinecraft();
            final Vec3d src = Minecraft.player.getPositionEyes(this.mc.getRenderPartialTicks());
            final Minecraft mc = this.mc;
            final EntityPlayerSP player = Minecraft.player;
            final Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
            final Vec3d dest = src.add(vectorForRotation.x * dist, vectorForRotation.y * dist, vectorForRotation.z * dist);
            final RayTraceResult rayTraceResult = new AxisAlignedBB(point.vec3d.x + 0.8, point.vec3d.y, point.vec3d.z + 0.2, point.vec3d.x + 0.2, point.vec3d.y + 1.8, point.vec3d.z + 0.8).calculateIntercept(src, dest);
            if (rayTraceResult != null) {
                return true;
            }
        }
        return false;
    }
    
    public void renderPositions() {
        for (final Point pos : this.positions) {
            final Vec3d vec3d = pos.vec3d;
            if (this.entity.getDistance(this.positions.get(0).vec3d.x, this.positions.get(0).vec3d.y, this.positions.get(0).vec3d.z) == 0.7071067690849304) {
                continue;
            }
            double x = vec3d.x - this.mc.getRenderManager().renderPosX;
            final double y = vec3d.y - this.mc.getRenderManager().renderPosY;
            double z = vec3d.z - this.mc.getRenderManager().renderPosZ;
            x += 0.5;
            z += 0.5;
            GL11.glPushMatrix();
            GL11.glDisable(3008);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDepthMask(true);
            ColorUtility.setColor(new Color(255, 255, 255, 55));
            Render<Entity> render = null;
            try {
                render = this.mc.getRenderManager().getEntityRenderObject(this.entity);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (render != null) {
                RenderLivingBase.class.cast(render).doRenderT((EntityLivingBase)this.entity, x, y, z, (float)pos.yaw, this.mc.getRenderPartialTicks());
            }
            GL11.glEnable(3553);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glEnable(3008);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        }
    }
    
    public static class Point
    {
        public Vec3d vec3d;
        public int ticks;
        public int yaw;
        
        public Point(final Vec3d vec3d) {
            this.vec3d = vec3d;
        }
        
        public Vec3d getVec3d() {
            return this.vec3d;
        }
        
        public void update() {
            ++this.ticks;
        }
    }
}
