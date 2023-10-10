package summer.cheat.cheats.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Timer;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.cheat.eventsystem.events.player.EventStep;
import summer.base.manager.config.Cheats;
import summer.base.utilities.MoveUtils;

public class Step extends Cheats {

    private final double[] offsets = {0.42f, 0.7532f};
    private final float timerWhenStepping = 1.0f / (offsets.length + 1);
    private boolean cancelMorePackets;
    private byte cancelledPackets;
    public static boolean cancelStep;

    public Step() {
        super("Step", "Allows you to step up blocks", Selection.MOVEMENT);
    }

    @EventTarget
    public void onStepEvent(EventStep e) {
        if (!CheatManager.getInstance(Flight.class).isToggled() && !CheatManager.getInstance(Speed.class).isToggled() && !MoveUtils.isInLiquid() && MoveUtils.isOnGround()) {
            if (e.isPre()) {
                e.setStepHeight(cancelStep ? 0.0f : 1.0f);
            } else {
                if (e.getHeightStepped() > 0.6) {
                    for (double offset : offsets) {
                        mc.getNetHandler()
                                .addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX,
                                        Minecraft.thePlayer.posY + offset * e.getHeightStepped(), Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
                    }
                    cancelMorePackets = true;
                }
            }
        }
    }

    @EventTarget
    public void onPacketSend(EventPacket event) {
        if (event.isSending() && event.getPacket() instanceof C03PacketPlayer) {
            if (cancelledPackets > 0) {
                cancelMorePackets = false;
                cancelledPackets = 0;
                Timer.timerSpeed = 1.0f;
            }
            if (cancelMorePackets) {
                Timer.timerSpeed = timerWhenStepping;
                cancelledPackets++;
            }
        }
    }
}
