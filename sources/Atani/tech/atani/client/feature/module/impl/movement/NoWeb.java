package tech.atani.client.feature.module.impl.movement;

import cn.muyang.nativeobfuscator.Native;
import tech.atani.client.listener.event.minecraft.input.MoveButtonEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.StringBoxValue;

@Native
@ModuleData(name = "NoWeb", description = "Prevents slowdown in Webs", category = Category.MOVEMENT)
public class NoWeb extends Module {
    private final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[]{"Vanilla", "Intave", "Fast Fall", "AAC 4", "AAC 5"});

    @Override
    public String getSuffix() {
    	return mode.getValue();
    }
    
    @Listen
    public final void onUpdate(UpdateEvent updateEvent) {
        if (!mc.thePlayer.isInWeb) {
            return;
        }

        switch (mode.getValue()) {
            case "Vanilla":
                mc.thePlayer.isInWeb = false;
                break;
            case "Fast Fall":
                if (mc.thePlayer.onGround) mc.thePlayer.jump();
                if (mc.thePlayer.motionY > 0f) {
                    mc.thePlayer.motionY -= mc.thePlayer.motionY * 2;
                }
                break;
            case "AAC 4":
                mc.timer.timerSpeed = 0.99F;
                mc.thePlayer.jumpMovementFactor = 0.02958f;
                mc.thePlayer.motionY -= 0.00775;
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.4050;
                    mc.timer.timerSpeed = 1.35F;
                }
                break;
            case "AAC 5":
                mc.thePlayer.jumpMovementFactor = 0.42f;

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                break;
            case "Intave":
                if (mc.thePlayer.movementInput.moveStrafe == 0.0F && mc.gameSettings.keyBindForward.isKeyDown() && mc.thePlayer.isCollidedVertically) {
                    mc.thePlayer.jumpMovementFactor = 0.74F;
                } else {
                    mc.thePlayer.jumpMovementFactor = 0.2F;
                    mc.thePlayer.onGround = true;
                }
                break;
        }
    }

    @Listen
    public final void onMove(MoveButtonEvent moveButtonEvent) {
        if(mode.is("AAC 4") && mc.thePlayer.isInWeb) {
            moveButtonEvent.setJump(false);
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
