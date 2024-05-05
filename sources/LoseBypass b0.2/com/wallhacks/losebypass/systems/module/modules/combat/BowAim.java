/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.wallhacks.losebypass.systems.module.modules.combat;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.Render3DEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.module.modules.misc.AntiBot;
import com.wallhacks.losebypass.systems.setting.settings.DoubleSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import com.wallhacks.losebypass.systems.setting.settings.ModeSetting;
import com.wallhacks.losebypass.utils.MathUtil;
import com.wallhacks.losebypass.utils.RenderUtil;
import com.wallhacks.losebypass.utils.RotationUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Mouse;

@Module.Registration(name="BowAim", description="Helps aiming with the bow", category=Module.Category.COMBAT)
public class BowAim
extends Module {
    public final ArrayList<TargetPos> targetPositions = new ArrayList();
    private final ModeSetting mode = this.modeSetting("Mode", "Full", Arrays.asList("Yaw", "Pitch", "Full"));
    private final ModeSetting priority = this.modeSetting("Priority", "Distance", Arrays.asList("Distance", "FOV")).description("Distance: will target the enemy closest to your player, FOV: targets the enemy closest to your crosshair");
    private final IntSetting fov = this.intSetting("FOV", 50, 0, 180).description("The field of view the bow aim will target");
    private final IntSetting range = this.intSetting("Range", 30, 10, 100).description("The range bow aim will target enemies in");
    private final DoubleSetting ySpeed = this.doubleSetting("YawSpeed", 3.0, 1.0, 10.0).visibility(v -> {
        if (this.mode.is("Pitch")) return false;
        return true;
    }).description("The vertical speed that bow aim will move your mouse with");
    private final DoubleSetting pSpeed = this.doubleSetting("PitchSpeed", 1.0, 1.0, 10.0).visibility(v -> {
        if (this.mode.is("Yaw")) return false;
        return true;
    }).description("The Horizontal speed that bow aim will move your mouse with");
    private final IntSetting responseTime = this.intSetting("ResponseTime", 100, 0, 500).description("The response time helps you seem more legit and also smooths out the aiming");
    private long lastFrame;

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        EntityPlayer target;
        long deltaTime = System.currentTimeMillis() - this.lastFrame;
        this.lastFrame = System.currentTimeMillis();
        if (BowAim.mc.thePlayer.getHeldItem() != null && BowAim.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow && Mouse.isButtonDown((int)1) && (target = this.getTarget()) != null) {
            this.targetPositions.add(0, new TargetPos(target));
            TargetPos targetPos = this.getPositionDelayed();
            if (targetPos == null) {
                return;
            }
            float[] rotation = this.getRot(targetPos);
            double yStep = (double)deltaTime * 0.01 * (double)this.ySpeed.getFloatValue();
            double pStep = (double)deltaTime * 0.01 * (double)this.pSpeed.getFloatValue();
            if (!this.mode.is("Pitch")) {
                if (BowAim.mc.thePlayer.rotationYaw < rotation[0]) {
                    BowAim.mc.thePlayer.rotationYaw = (float)Math.min((double)BowAim.mc.thePlayer.rotationYaw + yStep, (double)rotation[0]);
                } else if (BowAim.mc.thePlayer.rotationYaw > rotation[0]) {
                    BowAim.mc.thePlayer.rotationYaw = (float)Math.max((double)BowAim.mc.thePlayer.rotationYaw - yStep, (double)rotation[0]);
                }
            }
            if (this.mode.is("Yaw")) return;
            if (BowAim.mc.thePlayer.rotationPitch > rotation[1]) {
                BowAim.mc.thePlayer.rotationPitch = (float)Math.max((double)BowAim.mc.thePlayer.rotationPitch - pStep, (double)rotation[1]);
                return;
            }
            BowAim.mc.thePlayer.rotationPitch = (float)Math.min((double)BowAim.mc.thePlayer.rotationPitch + pStep, (double)rotation[1]);
            return;
        }
        this.targetPositions.clear();
    }

    public float[] getRot(TargetPos target) {
        double distance = BowAim.mc.thePlayer.getDistance(target.pos.xCoord, target.pos.yCoord, target.pos.zCoord);
        Vec3 cen = target.pos.addVector((distance += (double)((float)((Integer)this.responseTime.getValue()).intValue() / 50.0f)) / 1.5 * target.motionX, 0.0, distance / 1.5 * target.motionZ);
        Vec3 dir = cen.subtract(MathUtil.getEyesPos(BowAim.mc.thePlayer));
        float velocity = 1.0f;
        double hDistance = Math.sqrt(dir.xCoord * dir.xCoord + dir.zCoord * dir.zCoord);
        double hDistanceSq = hDistance * hDistance;
        float g = 0.006f;
        float velocitySq = velocity * velocity;
        float velocityPow4 = velocitySq * velocitySq;
        float pitch = (float)(-Math.toDegrees(Math.atan(((double)velocitySq - Math.sqrt((double)velocityPow4 - (double)g * ((double)g * hDistanceSq + 2.0 * dir.yCoord * (double)velocitySq))) / ((double)g * hDistance))));
        float yaw = (float)Math.toDegrees(Math.atan2(dir.zCoord, dir.xCoord)) - 90.0f;
        float[] myRot = new float[]{BowAim.mc.thePlayer.rotationYaw, BowAim.mc.thePlayer.rotationPitch};
        return new float[]{myRot[0] + MathUtil.wrapDegrees(yaw - myRot[0]), pitch};
    }

    private TargetPos getPositionDelayed() {
        int size = this.targetPositions.size();
        int i = 0;
        while (i < size) {
            TargetPos targetPos = this.targetPositions.get(i);
            if (i == size - 1) {
                return targetPos;
            }
            if (System.currentTimeMillis() - targetPos.time >= (long)((Integer)this.responseTime.getValue()).intValue()) {
                while (this.targetPositions.size() > i + 1) {
                    this.targetPositions.remove(i + 1);
                }
                return targetPos;
            }
            ++i;
        }
        return null;
    }

    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        double best = Double.MAX_VALUE;
        Iterator iterator = BowAim.mc.theWorld.playerEntities.iterator();
        while (iterator.hasNext()) {
            float[] rotations;
            float fovDistance;
            double distance;
            EntityPlayer player = (EntityPlayer)iterator.next();
            if (!AntiBot.allowAttack(player) || !((distance = (double)BowAim.mc.thePlayer.getDistanceToEntity(player)) < (double)((Integer)this.range.getValue()).intValue()) || (fovDistance = Math.abs(Math.abs((rotations = RotationUtil.getViewRotations(MathUtil.getCenter(player.getEntityBoundingBox()), BowAim.mc.thePlayer))[0]) - Math.abs(BowAim.mc.thePlayer.rotationYaw))) > (float)((Integer)this.fov.getValue()).intValue()) continue;
            double d = this.priority.is("Distance") ? distance : (double)fovDistance;
            double score = d;
            if (!(score < best)) continue;
            target = player;
            best = score;
        }
        return target;
    }

    public static class TargetPos {
        Vec3 pos;
        long time;
        double motionX;
        double motionZ;

        public TargetPos(EntityPlayer target) {
            this.pos = MathUtil.getCenter(RenderUtil.getRenderBB(target));
            this.pos = new Vec3(this.pos.xCoord, (int)this.pos.yCoord, this.pos.zCoord);
            this.time = System.currentTimeMillis();
            this.motionX = target.posX - target.prevPosX;
            this.motionZ = target.posZ - target.prevPosZ;
        }
    }
}

