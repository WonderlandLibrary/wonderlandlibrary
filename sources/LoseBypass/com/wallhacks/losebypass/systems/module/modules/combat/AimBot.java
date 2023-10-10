/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.wallhacks.losebypass.systems.module.modules.combat;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.InputEvent;
import com.wallhacks.losebypass.event.events.Render3DEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.module.modules.misc.AntiBot;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
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
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Mouse;

@Module.Registration(name="AimAssist", description="Helps you aim at enemies", category=Module.Category.COMBAT)
public class AimBot
extends Module {
    private final ModeSetting priority = this.modeSetting("Priority", "Distance", Arrays.asList("Distance", "FOV")).description("Distance: will target the enemy closest to your player, FOV: targets the enemy closest to your crosshair");
    private final IntSetting fov = this.intSetting("FOV", 50, 0, 180).description("The field of view the aim assist will target");
    private final DoubleSetting range = this.doubleSetting("Range", 3.0, 2.0, 6.0).description("The range aim assist will target enemies in");
    private final BooleanSetting click = this.booleanSetting("RequireClick", true).description("If the mouse needs to be held down for aim assist to work");
    private final BooleanSetting weapon = this.booleanSetting("RequireWeapon", true).description("Only aim if a weapon is in your hand");
    private final BooleanSetting whileMining = this.booleanSetting("WhileMining", true).description("Whether or not to aim while mining a block");
    private final DoubleSetting speed = this.doubleSetting("Speed", 2.0, 1.0, 10.0).description("The speed to aim at");
    private final IntSetting responseTime = this.intSetting("ResponseTime", 100, 0, 500).description("The response time to enemie movement, helps to seem more legit");
    long lastFrame;
    long lastTime;
    public ArrayList<TargetPos> targetPositions = new ArrayList();
    EntityPlayer currentTarget;

    @SubscribeEvent
    public void onRender3D(Render3DEvent event) {
        Iterator iterator;
        long deltaTime = System.currentTimeMillis() - this.lastFrame;
        this.lastFrame = System.currentTimeMillis();
        EntityPlayer target = null;
        double best = Double.MAX_VALUE;
        if (AimBot.mc.currentScreen == null && this.clickCheck() && (!((Boolean)this.weapon.getValue()).booleanValue() || AimBot.mc.thePlayer.getHeldItem() != null && AimBot.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
            if (!((Boolean)this.whileMining.getValue()).booleanValue() && AimBot.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                return;
            }
            iterator = AimBot.mc.theWorld.playerEntities.iterator();
        } else {
            this.targetPositions.clear();
            return;
        }
        while (iterator.hasNext()) {
            double distance;
            EntityPlayer player = (EntityPlayer)iterator.next();
            if (!AntiBot.allowAttack(player) || !((distance = (double)AimBot.mc.thePlayer.getDistanceToEntity(player)) < (double)this.range.getFloatValue())) continue;
            float[] rotations = RotationUtil.getViewRotations(MathUtil.getCenter(player.getEntityBoundingBox()), AimBot.mc.thePlayer);
            float fovDistance = Math.abs(Math.abs(rotations[0]) - Math.abs(AimBot.mc.thePlayer.rotationYaw));
            float pitchDistance = Math.abs(rotations[1] - Math.abs(AimBot.mc.thePlayer.rotationPitch));
            if ((fovDistance = (float)Math.sqrt(fovDistance * fovDistance + pitchDistance * pitchDistance)) > (float)((Integer)this.fov.getValue()).intValue()) continue;
            double d = this.priority.is("Distance") ? distance : (double)fovDistance;
            double score = d;
            if (!(score < best)) continue;
            target = player;
            best = score;
        }
        if (target == null) return;
        if (this.currentTarget != target) {
            this.currentTarget = target;
            this.targetPositions.clear();
        }
        this.targetPositions.add(0, new TargetPos(RenderUtil.getRenderBB(target), System.currentTimeMillis()));
        Vec3 pos = this.getPositionDelayed();
        float[] rotation = RotationUtil.getViewRotations(pos, AimBot.mc.thePlayer);
        if (AimBot.mc.objectMouseOver.entityHit == target) return;
        double step = (double)deltaTime * 0.02 * (double)this.speed.getFloatValue();
        if (AimBot.mc.thePlayer.rotationYaw < rotation[0]) {
            AimBot.mc.thePlayer.rotationYaw = (float)Math.min((double)AimBot.mc.thePlayer.rotationYaw + step, (double)rotation[0]);
            return;
        }
        if (!(AimBot.mc.thePlayer.rotationYaw > rotation[0])) return;
        AimBot.mc.thePlayer.rotationYaw = (float)Math.max((double)AimBot.mc.thePlayer.rotationYaw - step, (double)rotation[0]);
    }

    @SubscribeEvent
    public void onInput(InputEvent event) {
        if (event.getKey() != -2) return;
        this.lastTime = System.currentTimeMillis();
    }

    private boolean clickCheck() {
        if (!((Boolean)this.click.getValue()).booleanValue()) {
            return true;
        }
        if (Mouse.isButtonDown((int)0)) {
            return true;
        }
        if (System.currentTimeMillis() - this.lastTime >= 250L) return false;
        return true;
    }

    private Vec3 getPositionDelayed() {
        int size = this.targetPositions.size();
        int i = 0;
        while (i < size) {
            TargetPos targetPos = this.targetPositions.get(i);
            if (i == size - 1) {
                return MathUtil.getCenter(targetPos.bb);
            }
            if (System.currentTimeMillis() - targetPos.time >= (long)((Integer)this.responseTime.getValue()).intValue()) {
                while (this.targetPositions.size() > i + 1) {
                    this.targetPositions.remove(i + 1);
                }
                return MathUtil.getCenter(targetPos.bb);
            }
            ++i;
        }
        return null;
    }

    public static class TargetPos {
        AxisAlignedBB bb;
        long time;

        public TargetPos(AxisAlignedBB targetPos, long time) {
            this.bb = targetPos;
            this.time = time;
        }
    }
}

