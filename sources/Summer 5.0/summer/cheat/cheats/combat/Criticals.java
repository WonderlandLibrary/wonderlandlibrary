package summer.cheat.cheats.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.MoveUtils;
import summer.base.utilities.TimerUtils;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventPacket;
import summer.cheat.eventsystem.events.player.EventMotion;

public class Criticals extends Cheats {
    public String mode;
    private final double[] watchdogOffsets = {0.056f, 0.016f, 0.003f};
    private final TimerUtils timer = new TimerUtils();
    private int groundTicks;

    public Criticals() {
        super("Criticals", "Automatically crit your enemy", Selection.COMBAT);
    }

    @EventTarget
    private void onUpdatePosition(EventMotion e) {
        groundTicks = MoveUtils.isOnGround() ? groundTicks + 1 : 0;
    }

    @EventTarget
    public void onPacketSendEvent(EventPacket e) {
        this.setDisplayName("Criticals\u00A77 " + "Packet");
        if (e.getPacket() instanceof C0APacketAnimation) {
            if (groundTicks > 1 && timer.hasReached(800L)) {
                for (double offset : watchdogOffsets) {
                    mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(
                            Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + offset, Minecraft.thePlayer.posZ, false));
                }
                timer.reset();
            }
        }
    }
}
