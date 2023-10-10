package tech.atani.client.feature.module.impl.combat;

import net.minecraft.entity.EntityLivingBase;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.listener.event.minecraft.player.rotation.RotationEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.player.combat.FightUtil;
import tech.atani.client.utility.player.rotation.RotationUtil;

import java.util.List;

@ModuleData(name = "HitBoxes", description = "Allows you to hit further", category = Category.COMBAT)
public class HitBoxes extends Module {

    public StringBoxValue hitBoxMode = new StringBoxValue("HitBox Mode", "Which mode should hitbox use?", this, new String[] {"Normal", "Legit"});

    private float yawRot;
    private float pitchRot;
    @Listen
    public final void onRotation(RotationEvent rotationEvent) {
        if(hitBoxMode.is("Legit")) {
            //fix range!
            // Killaura, gotta make with these List<EntityLivingBase> targets = FightUtil.getMultipleTargets(findRange.getValue(), players.getValue(), animals.getValue(), walls.getValue(), monsters.getValue(), invisible.getValue());
            List<EntityLivingBase> targets = FightUtil.getMultipleTargets(3.0, true, false, false, true, true);
            if (targets.get(1) != null) {
                final float[] rotations = RotationUtil.getRotation(targets.get(1), "Bruteforce", 0, true, true, 0, 0, 0, 0, false, 40, 40, 40, 40, true, true);

                yawRot = rotations[0];
                pitchRot = rotations[1];

                mc.thePlayer.rotationYawHead = yawRot;
                rotationEvent.setYaw(yawRot);
                rotationEvent.setPitch(pitchRot);
            }
        }
    }
    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
