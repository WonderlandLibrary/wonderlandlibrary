package net.smoothboot.client.module.combat;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.mixinterface.ISubWorldRenderEnd;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.ModeSetting;
import net.smoothboot.client.module.settings.NumberSetting;
import net.smoothboot.client.util.EntityUtil;
import net.smoothboot.client.util.MathUtil;
import net.smoothboot.client.util.Rot;
import net.smoothboot.client.util.RotationUtil;

import java.util.Comparator;
import java.util.stream.Stream;

public class AimAssist extends Mod implements ISubWorldRenderEnd {

    String legit = "Legit";
    String blatant = "Blatant";

    public NumberSetting aimDistance = new NumberSetting("Distance", 0, 6, 4, 0.1);
    public NumberSetting aimSmoothing = new NumberSetting("Speed", 0, 1, 0.5F, 0.01);
    public NumberSetting aimFOV = new NumberSetting("FOV", 0, 360, 90, 1);
    public ModeSetting aimMode = new ModeSetting("Mode", legit, legit, blatant);
    public BooleanSetting yawAssist = new BooleanSetting("Horizontal", true);
    public BooleanSetting pitchAssist = new BooleanSetting("Vertical", false);
    public BooleanSetting seeOnly = new BooleanSetting("Aim visible", true);
    public BooleanSetting swordOnly = new BooleanSetting("Sword check", true);
    public BooleanSetting aimTeamCheck = new BooleanSetting("Team check", true);

    public AimAssist() {
        super("Aim Assist", "", Category.Combat);
        addsettings(aimDistance, aimSmoothing, aimFOV, swordOnly, aimMode, aimTeamCheck);
    }

    public static boolean isOverEntity() {
        HitResult hitResult = mc.crosshairTarget;
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) hitResult).getEntity();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onWorldRenderEnd(WorldRenderContext ctx) {
        if (!this.isEnabled()) return;
        if (mc.currentScreen != null) return;
        if (swordOnly.isEnabled() && !swordCheck()) return;
        if (aimMode.isMode(legit) && isOverEntity()) return;
        boolean attackable;
        Entity targetPlayer = EntityUtil.findClosestPlayer(Entity.class, aimDistance.getValueFloat());
        if (targetPlayer == null) return;
        attackable = RotationUtil.getAngleToLookVec(targetPlayer.getBoundingBox().getCenter()) <= (aimFOV.getValue() / 2);
        if (!attackable) return;
        if ((aimTeamCheck.isEnabled() && targetPlayer.isTeammate(mc.player)) || (seeOnly.isEnabled() && !mc.player.canSee(targetPlayer))) return;

        Rot targetRot = MathUtil.getDir(mc.player, targetPlayer.getPos());

        float yawDist = MathHelper.subtractAngles((float) targetRot.yaw(), mc.player.getYaw());
        float pitchDist = MathHelper.subtractAngles((float) targetRot.pitch(), mc.player.getPitch());
        float yaw;
        float pitch;

        float stren = aimSmoothing.getValueFloat() / 10;

        yaw = mc.player.getYaw();
        if (Math.abs(yawDist) > stren) {
            yaw = mc.player.getYaw();
            if (yawDist < 0) {
            yaw += stren;
            } else if (yawDist > 0) {
            yaw -= stren;
            }
        }
            pitch = mc.player.getPitch();
            if (Math.abs(pitchDist) > stren) {
                pitch = mc.player.getPitch();
                if (pitchDist < 0) {
                    pitch += stren;
                }
                else if (pitchDist > 0) {
                    pitch -= stren;
                }
            }

            float stren2 = aimSmoothing.getValueFloat() / 50;
            yaw = MathHelper.lerpAngleDegrees(stren2, mc.player.getYaw(), (float) targetRot.yaw());
            pitch = MathHelper.lerpAngleDegrees(stren2, mc.player.getPitch(), (float) targetRot.pitch());
            if (yawAssist.isEnabled()) mc.player.setYaw(yaw);
            if (pitchAssist.isEnabled()) mc.player.setPitch(pitch);
        }

    public boolean swordCheck() {
        ItemStack getItem = mc.player.getMainHandStack();
        return (getItem.isOf(Items.NETHERITE_SWORD) || getItem.isOf(Items.DIAMOND_SWORD) || getItem.isOf(Items.GOLDEN_SWORD) || getItem.isOf(Items.IRON_SWORD) || getItem.isOf(Items.STONE_SWORD) || getItem.isOf(Items.WOODEN_SWORD));
    }

}