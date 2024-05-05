package astronaut.modules.movement;

import astronaut.Duckware;
import astronaut.events.EventUpdate;
import astronaut.modules.Category;
import astronaut.modules.Module;
import eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import astronaut.events.EventReceivedPacket;

import java.awt.*;

public class Speed extends Module {
    private boolean wasOnGround;

    public Speed() {
        super("Speed", Type.Movement, 0, Category.MOVEMENT, Color.red, "Increases your speed");
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

        if (mc.thePlayer.onGround && !wasOnGround)
            mc.thePlayer.jump();

        if (mc.thePlayer.motionX != 0.0 || mc.thePlayer.motionZ != 0.0) {
            if (!mc.thePlayer.onGround) {
                mc.timer.timerSpeed = 1.2F;
            }
        } else
            mc.timer.timerSpeed = 1.0F;

        wasOnGround = mc.thePlayer.onGround;
    }

    @Override
    public void onEnable() {
        wasOnGround = false;
    }

    public void onDisable(){
        mc.timer.timerSpeed = 1.0F;
    }
}