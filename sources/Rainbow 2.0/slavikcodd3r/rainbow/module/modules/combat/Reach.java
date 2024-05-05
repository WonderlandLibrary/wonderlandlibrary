package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.ReachEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Reach")
public class Reach extends Module
{   
    @Op(name = "Reach", min = 3.0, max = 6.0, increment = 0.01)
    private float reach;
	Minecraft mc = Minecraft.getMinecraft();
	
	public Reach() {
		this.reach = 6;
	}
	
    @EventTarget
    public void onReach(final ReachEvent event) {
        event.setReach(this.reach);
    }
}