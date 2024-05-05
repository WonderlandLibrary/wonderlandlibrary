package rip.helium.cheat.impl.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.utils.Stopwatch;

public class AntiVoid extends Cheat {

    private boolean shouldSave;
    private final Stopwatch timer;

    public AntiVoid() {
        super("AntiVoid", "Lags you back up when you fall into the void.", CheatCategory.PLAYER);
        timer = new Stopwatch();
    }

    @Collect
    public void onMove(PlayerMoveEvent e) {
        if ((shouldSave && timer.hasPassed(150)) || mc.thePlayer.isCollidedVertically) {
            shouldSave = false;
            timer.reset();
        }
        if (Helium.instance.cheatManager.isCheatEnabled("Flight"))
            return;
        if (mc.thePlayer.fallDistance > 5) {
            if (!isBlockUnder()) {
                if (!shouldSave) {
                    shouldSave = true;
                    timer.reset();
                }
                mc.thePlayer.fallDistance = 0;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 12, mc.thePlayer.posZ, false));
            }
        }
    }

    private boolean isBlockUnder() {
        if (mc.thePlayer.posY < 0)
            return false;
        for (int off = 0; off < (int) mc.thePlayer.posY + 2; off += 2) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -off, 0);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }


}
