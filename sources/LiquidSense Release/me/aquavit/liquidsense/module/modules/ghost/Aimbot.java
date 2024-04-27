package me.aquavit.liquidsense.module.modules.ghost;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.StrafeEvent;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.extensions.PlayerExtensionUtils;
import me.aquavit.liquidsense.utils.misc.RandomUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.client.Rotation;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import net.minecraft.entity.Entity;

import java.util.Comparator;
import java.util.Random;

@ModuleInfo(name = "Aimbot", description = "Automatically faces selected entities around you.", category = ModuleCategory.GHOST)
public class Aimbot extends Module {
    private FloatValue rangeValue = new FloatValue("Range", 4.4F, 1F, 8F);
    private FloatValue turnSpeedValue = new FloatValue("TurnSpeed", 2F, 1F, 180F);
    private FloatValue fovValue = new FloatValue("FOV", 180F, 1F, 180F);
    private BoolValue centerValue = new BoolValue("Center", false);
    private BoolValue lockValue = new BoolValue("Lock", true);
    private BoolValue onClickValue = new BoolValue("OnClick", false);
    private BoolValue jitterValue = new BoolValue("Jitter", false);

    private MSTimer clickTimer = new MSTimer();

    @EventTarget
    public void onStrafe(StrafeEvent event) {
        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            clickTimer.reset();
        }

        if (onClickValue.get() && clickTimer.hasTimePassed(500L)) {
            return;
        }

        float range = rangeValue.get();
        Entity entity = mc.theWorld.loadedEntityList.stream().filter(it -> EntityUtils.isSelected(it, true, false) && mc.thePlayer.canEntityBeSeen(it) &&
                PlayerExtensionUtils.getDistanceToEntityBox(mc.thePlayer, it) <= range &&
                RotationUtils.getRotationDifference(it) <= fovValue.get()).min(Comparator.comparingDouble(RotationUtils::getRotationDifference)).orElse(null);

        if (entity == null) return;

        if (!lockValue.get() && RotationUtils.isFaced(entity, range)) {
            return;
        }

        if (entity.getEntityBoundingBox() == null) return;

        Rotation rotation;

        if (centerValue.get()) {
            rotation = RotationUtils.limitAngleChange(
                    new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch), RotationUtils.toRotation(RotationUtils.getCenter(entity.getEntityBoundingBox()), true),
                    (float) (turnSpeedValue.get() + Math.random()));
        } else {
            if (entity.getEntityBoundingBox() == null ||
                    RotationUtils.searchCenter((entity.getEntityBoundingBox()), range) == null ||
                    RotationUtils.searchCenter((entity.getEntityBoundingBox()), range).getRotation()  == null) return;
            rotation = RotationUtils.limitAngleChange(
                    new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch),
                    RotationUtils.searchCenter((entity.getEntityBoundingBox()), range).getRotation(),
                    (float) (turnSpeedValue.get() + Math.random()));
        }
        ;
        rotation.toPlayer(mc.thePlayer);

        if (jitterValue.get()) {
            boolean yaw = new Random().nextBoolean();
            boolean pitch = new Random().nextBoolean();
            boolean yawNegative = new Random().nextBoolean();
            boolean pitchNegative = new Random().nextBoolean();
            if (yaw) {
                mc.thePlayer.rotationYaw += yawNegative ? -RandomUtils.nextFloat(0F, 1F) : RandomUtils.nextFloat(0F, 1F);
            }

            if (pitch) {
                mc.thePlayer.rotationPitch += pitchNegative ? -RandomUtils.nextFloat(0F, 1F) : RandomUtils.nextFloat(0F, 1F);
                if (mc.thePlayer.rotationPitch > 90) {
                    mc.thePlayer.rotationPitch = 90F;
                } else if (mc.thePlayer.rotationPitch < -90) {
                    mc.thePlayer.rotationPitch = -90F;
                }
            }
        }
    }
}
