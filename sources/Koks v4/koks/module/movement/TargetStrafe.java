package koks.module.movement;

import god.buddy.aot.BCompiler;
import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.api.utils.RotationUtil;
import koks.api.manager.value.annotation.Value;
import koks.event.UpdateEvent;
import koks.module.combat.KillAura;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "TargetStrafe", description = "You strafe around a target", category = Module.Category.MOVEMENT)
public class TargetStrafe extends Module {

    @Value(name = "Mode", modes = {"Legit"})
    String mode = "Legit";

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final RotationUtil rotationUtil = RotationUtil.getInstance();
        if(event instanceof UpdateEvent) {
            if(mode.equalsIgnoreCase("Legit")) {
                if (mc.currentScreen != null) return;
                boolean keyRight = isKeyDown(getGameSettings().keyBindRight.getKeyCode());
                boolean keyLeft = isKeyDown(getGameSettings().keyBindLeft.getKeyCode());
                if (KillAura.getCurEntity() != null && !keyRight && !keyLeft) {
                    float y = rotationUtil.getYaw(KillAura.getCurEntity(), mc.thePlayer) + 180;
                    float right = rotationUtil.calculateRotationDiff(y + 0.15f, (float) (MathHelper.wrapAngleTo180_double(KillAura.getCurEntity().rotationYaw) + 180))[0];
                    float left = rotationUtil.calculateRotationDiff(y - 0.15f, (float) (MathHelper.wrapAngleTo180_double(KillAura.getCurEntity().rotationYaw) + 180))[0];
                    if (right > left) {
                        getGameSettings().keyBindRight.pressed = true;
                        getGameSettings().keyBindLeft.pressed = false;
                    } else {
                        getGameSettings().keyBindRight.pressed = false;
                        getGameSettings().keyBindLeft.pressed = true;
                    }
                } else {
                    getGameSettings().keyBindRight.pressed = keyRight;
                    getGameSettings().keyBindLeft.pressed = keyLeft;
                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
