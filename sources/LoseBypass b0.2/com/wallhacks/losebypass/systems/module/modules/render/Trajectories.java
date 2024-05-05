/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.render;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.Render3DEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.module.modules.misc.AntiBot;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.ColorSetting;
import com.wallhacks.losebypass.utils.MathUtil;
import com.wallhacks.losebypass.utils.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

@Module.Registration(name="Trajectories", description="Predicts where projectiles go", category=Module.Category.RENDER)
public class Trajectories
extends Module {
    private final BooleanSetting onlySelf = this.booleanSetting("OnlySelf", false);
    private final ColorSetting lineColor = this.colorSetting("Color", new Color(40, 190, 40, 220));

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        if (!this.onlySelf.isOn()) {
            Trajectories.mc.theWorld.playerEntities.stream().filter(AntiBot::allowRender).forEach(entityPlayer -> this.renderPath(event.partialTicks, (EntityPlayer)entityPlayer));
            this.renderPath(event.partialTicks, Trajectories.mc.thePlayer);
            return;
        }
        this.renderPath(event.partialTicks, Trajectories.mc.thePlayer);
    }

    private void renderPath(float partialTicks, EntityPlayer player) {
        ArrayList<Vec3> path = new ArrayList<Vec3>();
        ItemStack stack = player.getHeldItem();
        if (stack == null) {
            return;
        }
        Item item = stack.getItem();
        if (item == null) return;
        if (!this.isThrowable(item)) {
            return;
        }
        double yaw = Math.toRadians(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks);
        double pitch = Math.toRadians(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks);
        double arrowPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks - Math.cos(yaw) * 0.16;
        double arrowPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks + (double)player.getEyeHeight() - 0.1;
        double arrowPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks - Math.sin(yaw) * 0.16;
        double arrowMotionFactor = item == Items.bow ? 1.0 : 0.4;
        double arrowMotionX = -Math.sin(yaw) * Math.cos(pitch) * arrowMotionFactor;
        double arrowMotionY = -Math.sin(pitch) * arrowMotionFactor;
        double arrowMotionZ = Math.cos(yaw) * Math.cos(pitch) * arrowMotionFactor;
        double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
        arrowMotionX /= arrowMotion;
        arrowMotionY /= arrowMotion;
        arrowMotionZ /= arrowMotion;
        if (item == Items.bow) {
            float bowPower = (float)(72000 - player.getItemInUseCount()) / 20.0f;
            if ((bowPower = (bowPower * bowPower + bowPower * 2.0f) / 3.0f) > 1.0f || bowPower <= 0.1f) {
                bowPower = 1.0f;
            }
            arrowMotionX *= (double)(bowPower *= 3.0f);
            arrowMotionY *= (double)bowPower;
            arrowMotionZ *= (double)bowPower;
        } else {
            arrowMotionX *= 1.5;
            arrowMotionY *= 1.5;
            arrowMotionZ *= 1.5;
        }
        double gravity = this.getProjectileGravity(item);
        Vec3 eyesPos = MathUtil.getEyesPos(player);
        for (int i = 0; i < 200; ++i) {
            Vec3 arrowPos = new Vec3(arrowPosX, arrowPosY, arrowPosZ);
            arrowPosX += arrowMotionX;
            arrowPosY += arrowMotionY;
            arrowPosZ += arrowMotionZ;
            arrowMotionX *= 0.99004444;
            arrowMotionY *= 0.99004444;
            arrowMotionZ *= 0.99004444;
            arrowMotionY -= gravity;
            MovingObjectPosition res = Trajectories.mc.theWorld.rayTraceBlocks(eyesPos, arrowPos, false, true, false);
            for (Entity entity : Trajectories.mc.theWorld.loadedEntityList) {
                MovingObjectPosition entityRaytrace;
                if (entity == player || !entity.canBeCollidedWith() || (entityRaytrace = entity.getEntityBoundingBox().calculateIntercept(eyesPos, arrowPos)) == null) continue;
                res = new MovingObjectPosition(entity, entityRaytrace.hitVec);
                break;
            }
            if (res != null) {
                if (res.entityHit != null && res.entityHit != Trajectories.mc.thePlayer) {
                    RenderUtil.boundingESPBoxFilled(RenderUtil.getRenderBB(res.entityHit), this.lineColor.getColor());
                } else {
                    this.TrajectoriesTarget(res.hitVec, res.sideHit, this.lineColor.getColor());
                }
                path.add(res.hitVec);
                break;
            }
            path.add(arrowPos);
            eyesPos = arrowPos;
        }
        RenderUtil.renderLineList(path, this.lineColor.getColor());
    }

    private double getProjectileGravity(Item item) {
        if (item != Items.bow) return 0.03;
        return 0.05;
    }

    private boolean isThrowable(Item item) {
        if (item == Items.bow) return true;
        if (item == Items.snowball) return true;
        if (item == Items.egg) return true;
        if (item == Items.ender_pearl) return true;
        return false;
    }

    public void TrajectoriesTarget(Vec3 pos, EnumFacing facing, Color c) {
        double x = pos.xCoord;
        double y = pos.yCoord;
        double z = pos.zCoord;
        if (facing == null) return;
        double xDir = facing.getFrontOffsetX() == 0 ? 0.3 : 0.0;
        double yDir = facing.getFrontOffsetY() == 0 ? 0.3 : 0.0;
        double zDir = facing.getFrontOffsetZ() == 0 ? 0.3 : 0.0;
        RenderUtil.boundingESPBoxFilled(new AxisAlignedBB(x - xDir, y - yDir, z - zDir, x + xDir, y + yDir, z + zDir), c);
    }
}

