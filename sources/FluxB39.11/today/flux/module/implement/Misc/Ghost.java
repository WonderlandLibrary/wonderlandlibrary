package today.flux.module.implement.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.event.PacketSendEvent;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;

/**
 * Created by John on 2016/11/22.
 */
public class Ghost extends Module {
    public Ghost() {
        super("Ghost", Category.Misc, false);
    }

    private boolean bypassdeath = true;


    @Override
    public void onDisable() {
        super.onDisable();

        mc.thePlayer.respawnPlayer();
        bypassdeath = false;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.theWorld == null) {
            return;
        }
        if (mc.thePlayer.getHealth() == 0) {
            mc.thePlayer.setHealth(20);
            mc.thePlayer.isDead = false;
            bypassdeath = true;
            mc.displayGuiScreen((GuiScreen) null);
            mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        }
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent packet) {
        if (bypassdeath) {
            if ((packet.getPacket() instanceof C03PacketPlayer)) {
                packet.setCancelled(true);
            }
        }
    }
}
