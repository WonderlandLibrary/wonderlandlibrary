package slavikcodd3r.rainbow.module.modules.movement;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.ItemSlowEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.utils.ClientUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
@Mod(displayName = "NoSlow")
public class NoSlow extends Module
{
    @Option.Op(name = "Hypixel")
    public boolean hypixel;
    public static boolean wasOnground;
    
    static {
        NoSlow.wasOnground = true;
    }
    
    @EventTarget
    private void onItemUse(final ItemSlowEvent event) {
        event.setCancelled(true);
    }
    
    @EventTarget(4)
    private void onUpdate(final UpdateEvent event) {
        if (this.hypixel) {
            this.setSuffix("Hypixel");
        } else {
            this.setSuffix("Vanilla");
        }
        if (!this.hypixel && ClientUtils.player().isBlocking() && (ClientUtils.player().motionX != 0.0 || ClientUtils.player().motionZ != 0.0) && NoSlow.wasOnground) {
            if (event.getState() == Event.State.PRE) {
                ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            else if (event.getState() == Event.State.POST) {
                ClientUtils.packet(new C08PacketPlayerBlockPlacement(ClientUtils.player().inventory.getCurrentItem()));
            }
        }
    }
}
