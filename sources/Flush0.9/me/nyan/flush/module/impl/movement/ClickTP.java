package me.nyan.flush.module.impl.movement;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.utils.movement.PathFinder;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ClickTP extends Module {
    public ClickTP() {
        super("ClickTP", Category.MOVEMENT);
    }
    
    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (!mc.gameSettings.keyBindAttack.isKeyDown()) return;

        Vec3 eyesPos = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
        Vec3 look = mc.thePlayer.getLook(mc.timer.renderPartialTicks);
        MovingObjectPosition movingObject = mc.theWorld.rayTraceBlocks(eyesPos, eyesPos.addVector(
                look.xCoord * 100, look.yCoord * 100, look.zCoord * 100),
                false, true, true);
        if (movingObject == null) return;

        Vec3 hitVec = movingObject.hitVec.addVector(0, 1, 0);
        for (Vec3 vec3 : PathFinder.findPathTo(hitVec, 8)) {
            mc.thePlayer.sendQueue.addToSendQueue(
                    new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord, true)
            );
        }
        mc.thePlayer.setPosition(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord);
        disable();
    }
}
