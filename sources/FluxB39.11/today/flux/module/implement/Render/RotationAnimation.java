package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.util.MathHelper;
import today.flux.event.EventRotationAnimation;
import today.flux.event.PostUpdateEvent;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.utility.RotationUtils;

public class RotationAnimation extends Module {
    public RotationAnimation() {
        super("RotationAnimation", Category.Render, false);
    }

    float yaw, pitch, prevYaw, prevPitch;
    boolean needMod;

    @EventTarget(value = Priority.LOWEST)
    public void onPre(PostUpdateEvent e) {
        if (ModuleManager.scaffoldMod.isEnabled()) {
            yaw = e.yaw;
            pitch = e.pitch;
            needMod = true;
            return;
        }

        if (ModuleManager.killAuraMod.isEnabled() && KillAura.target != null) {
            float[] rotations = RotationUtils.getRotations(KillAura.target);
            yaw = rotations[0];
            pitch = rotations[1];
            needMod = true;
            return;
        }

        needMod = false;
        yaw = e.yaw;
        pitch = e.pitch;
    }

    @EventTarget
    public void onTick(TickEvent e) {
        prevYaw = yaw;
        prevPitch = pitch;
    }

    @EventTarget
    public void onRotationAnimation(EventRotationAnimation e) {
        if (e.getEntity() == mc.thePlayer && e.getPartialTicks() != 1.0F && mc.thePlayer.ridingEntity == null) {
            if (needMod) {
                e.setRenderYawOffset(interpolateAngle(e.getPartialTicks(), prevYaw, yaw));
                e.setRenderHeadYaw(interpolateAngle(e.getPartialTicks(), prevYaw, yaw) - e.getRenderYawOffset());
                e.setRenderHeadPitch(lerp(e.getPartialTicks(), prevPitch, pitch));
            }
            e.setRenderYawOffset(interpolateAngle(e.getPartialTicks(), prevYaw, yaw));
            e.setRenderHeadYaw(interpolateAngle(e.getPartialTicks(), prevYaw, yaw) - e.getRenderYawOffset());
            e.setRenderHeadPitch(lerp(e.getPartialTicks(), prevPitch, pitch));
        }
    }

    public static float interpolateAngle(float p_219805_0_, float p_219805_1_, float p_219805_2_) {
        return p_219805_1_ + p_219805_0_ * MathHelper.wrapAngleTo180_float(p_219805_2_ - p_219805_1_);
    }

    public static float lerp(float pct, float start, float end) {
        return start + pct * (end - start);
    }
}

