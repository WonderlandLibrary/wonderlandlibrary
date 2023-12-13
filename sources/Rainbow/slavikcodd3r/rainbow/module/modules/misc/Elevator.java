package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Elevator")
public class Elevator extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		if (ClientUtils.movementInput().jump) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.5, mc.thePlayer.posZ);
		}
		else if (ClientUtils.movementInput().sneak) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.5, mc.thePlayer.posZ);
		}
	}
}
