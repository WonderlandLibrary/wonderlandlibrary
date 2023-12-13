package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "Zoot")
public class Zoot extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		 if (ClientUtils.player().isPotionActive(Potion.blindness.id)) {
	            ClientUtils.player().removePotionEffect(Potion.blindness.id);
	        }
	        if (ClientUtils.player().isPotionActive(Potion.confusion.id)) {
	            ClientUtils.player().removePotionEffect(Potion.confusion.id);
	        }
	        if (ClientUtils.player().isPotionActive(Potion.digSlowdown.id)) {
	            ClientUtils.player().removePotionEffect(Potion.digSlowdown.id);
	        }
	    }
	}
