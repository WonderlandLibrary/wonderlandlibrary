package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Timer;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.BlockStepEvent;
import rip.helium.utils.Stopwatch;

import java.util.Arrays;
import java.util.List;

public class Step extends Cheat {

    public static Stopwatch lastStep = new Stopwatch();
    private boolean resetTimer;
    private final Stopwatch timer = new Stopwatch();

    public Step() {
        super("Step", "Spideyman", CheatCategory.MOVEMENT);
    }

    @Collect
    public void onStep(BlockStepEvent e) {
        if (resetTimer) {
            resetTimer = !resetTimer;
            Timer.timerSpeed = 1;
        }
        if (!mc.thePlayer.isInWater() && mc.getCurrentServerData() != null)
            if (e.isPre()) {
                if (mc.thePlayer.isCollidedVertically && !mc.gameSettings.keyBindJump.isKeyDown() && timer.hasPassed(300)) {
                    e.stepHeight = 2.5F;
                    //mc.timer.timerSpeed = 0.37F;
                    //mc.timer.timerSpeed = 1f;
                    //sigmaSkiddedStepYesIkItsSkiddedNowStfu(2.5f);
                }

                if ((Helium.instance.cheatManager.isCheatEnabled("Flight") || Helium.instance.cheatManager.isCheatEnabled("Speed") || Helium.instance.cheatManager.isCheatEnabled("LongJump")))
                    e.stepHeight = 0F;

            } else {

                double rheight = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
                boolean canStep = rheight >= 0.625;
                if (canStep) {
                    lastStep.reset();
                    timer.reset();
                }
                if (canStep) {
                    Timer.timerSpeed = 0.37F - (rheight >= 1 ? Math.abs(1 - (float) rheight) * (0.37F * 0.55f) : 0);
                    if (Timer.timerSpeed <= 0.05f) {
                        Timer.timerSpeed = 0.05f;
                    }
                    resetTimer = true;
                    sigmaSkiddedStepYesIkItsSkiddedNowStfu(rheight);
                }
            }
    }

    private void sigmaSkiddedStepYesIkItsSkiddedNowStfu(double height) {
        if (Helium.instance.cheatManager.isCheatEnabled("Flight") || Helium.instance.cheatManager.isCheatEnabled("Speed") || Helium.instance.cheatManager.isCheatEnabled("LongJump"))
            height = 0;
        List<Double> offset = Arrays.asList(0.42, 0.333, 0.248, 0.083, -0.078);
        double posX = mc.thePlayer.posX;
        double posZ = mc.thePlayer.posZ;
        double y = mc.thePlayer.posY;
        if (height < 1.1) {
            double first = 0.42;
            double second = 0.75;
            if (height != 1) {
                first *= height;
                second *= height;
                if (first > 0.425) {
                    first = 0.425;
                }
                if (second > 0.78) {
                    second = 0.78;
                }
                if (second < 0.49) {
                    second = 0.49;
                }
            }
            if (first == 0.42)
                first = 0.41999998688698;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
            if (y + second < y + height)
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
            return;
        } else if (height < 1.6) {
            for (int i = 0; i < offset.size(); i++) {
                double off = offset.get(i);
                y += off;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
            }
        } else if (height < 2.1) {
            double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
            for (double off : heights) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        } else {
            double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
            for (double off : heights) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        }
    }
}

