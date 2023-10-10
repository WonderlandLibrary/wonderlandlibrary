package tech.atani.client.feature.module.impl.movement;

import cn.muyang.nativeobfuscator.Native;
import tech.atani.client.listener.event.minecraft.player.movement.MovePlayerEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.combat.KillAura;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.player.movement.MoveUtil;
import tech.atani.client.utility.player.rotation.RotationUtil;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;

@Native
@ModuleData(name = "TargetStrafe", description = "Strafe around entities", category = Category.MOVEMENT)
public class TargetStrafe extends Module {
    private final SliderValue<Float> strafeSize = new SliderValue<Float>("Size", "How far should the player strafe from the target?", this, 2.5F, 1F, 6F, 1);
    private final CheckBoxValue jumpOnly = new CheckBoxValue("Jump Only", "Should the module only strafe when the jump key is pressed?", this, false),
            controllable = new CheckBoxValue("Controllable", "Should the strafe change direction when left or right key is pressed?", this, true),
            voidCheck = new CheckBoxValue("Void Check", "Should the strafe change direction when void is below?", this, true),
            collideCheck = new CheckBoxValue("Collide Check", "Should the strafe change direction when colliding on a wall?", this, true);

    private int direction = 1;

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        if(KillAura.curEntity != null) {
            if(controllable.getValue()) {
                if(Methods.mc.gameSettings.keyBindLeft.isKeyDown()) {
                    direction = 1;
                }

                if(Methods.mc.gameSettings.keyBindRight.isKeyDown()) {
                    direction = -1;
                }
            }

            if (isVoidBelow(Methods.mc.thePlayer.getPosition()) && voidCheck.getValue()) {
                direction = -direction;
            }

            if (collideCheck.getValue() && Methods.mc.thePlayer.isCollidedHorizontally) {
                direction = -direction;
            }
        }
    }

    @Listen
    public void onMove(MovePlayerEvent movePlayerEvent) {
        KillAura killAura = ModuleStorage.getInstance().getModule("KillAura");

        if(killAura.isEnabled() && KillAura.curEntity != null) {
            if(jumpOnly.getValue() && !isKeyDown(Methods.mc.gameSettings.keyBindJump.getKeyCode())) {
                return;
            }
            float yaw = RotationUtil.getRotation(KillAura.curEntity, "", 0, false, false, 0, 0, 0, 0, false, 180, 180, 180, 180, false, false)[0];

            MoveUtil.setMoveSpeed(MoveUtil.getSpeed(), yaw, direction, (Methods.mc.thePlayer.getDistanceToEntity(KillAura.curEntity) <= strafeSize.getValue() - 1) ? 0 : 1);
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
