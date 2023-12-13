package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;

@Module.Mod
public class CivBreak extends Module
{
	Minecraft mc = Minecraft.getMinecraft();
    private C07PacketPlayerDigging dig;
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
        if (this.dig != null && this.mc.thePlayer.getDistanceSq(this.dig.func_179715_a()) < 25.0) {
            this.mc.getNetHandler().addToSendQueue(this.dig);
        }
    }
    
    @EventTarget
    private void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C07PacketPlayerDigging && (this.dig == null || event.getPacket() != this.dig)) {
            final C07PacketPlayerDigging packet = (C07PacketPlayerDigging)event.getPacket();
            if (packet.func_180762_c() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.dig = packet;
            }
        }
    }
}
