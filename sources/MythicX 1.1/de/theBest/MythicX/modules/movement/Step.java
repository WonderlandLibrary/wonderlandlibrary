package de.theBest.MythicX.modules.movement;

import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.CombatTracker;

import java.awt.*;

public class Step extends Module {
    private CombatTracker time1;

    public Step() {
        super("Step", Type.Movement, 0, Category.MOVEMENT, Color.green, "Teleported up");
    }
    @EventTarget
    public void onUpdate(EventUpdate e) {
        if((mc.thePlayer.isCollidedHorizontally) && ((mc.gameSettings.keyBindForward.pressed) || (mc.gameSettings.keyBindBack.pressed) || (mc.gameSettings.keyBindRight.pressed) || (mc.gameSettings.keyBindLeft.pressed)) && (mc.thePlayer.onGround) && (!mc.thePlayer.isOnLadder())) {
            if(this.time1.isDelayComplete()) {
                mc.thePlayer.stepHeight = 1F;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42D, mc.thePlayer.posZ, mc.thePlayer.onGround));
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.753D, mc.thePlayer.posZ, mc.thePlayer.onGround));
                time1.reset();
            }
        } else {
            mc.timer.timerSpeed = 1F;
            mc.thePlayer.stepHeight = 1.5F;
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
    }

    @Override
    public void onEnable() {

    }
}
