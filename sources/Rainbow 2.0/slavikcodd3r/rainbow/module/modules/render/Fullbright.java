package slavikcodd3r.rainbow.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.Option.Op;
@Mod(displayName = "Fullbright")
public class Fullbright extends Module
{
    @Op(name = "Potion")
    private boolean potion;
    Minecraft mc = Minecraft.getMinecraft();
    
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		if (this.potion) {
			this.setSuffix("Potion");
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 0, 1));
		} else {
			this.setSuffix("Gamma");
			mc.gameSettings.gammaSetting = 1337.0f;
		}
	}
}
