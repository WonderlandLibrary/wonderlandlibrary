package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "OneHitKill")
public class OneHitKill extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	public void disable() {
        ClientUtils.player().removePotionEffect(Potion.damageBoost.id);
		super.disable();
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		mc.thePlayer.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 0, 255));
	}
}
