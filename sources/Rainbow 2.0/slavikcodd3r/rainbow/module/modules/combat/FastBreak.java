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
import slavikcodd3r.rainbow.module.modules.movement.Fly;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "FastBreak")
public class FastBreak extends Module
{   
	
	@Op(name = "Potion")
	private boolean potion;
	Minecraft mc = Minecraft.getMinecraft();
	
	public void disable() {
		if (this.potion) {
        ClientUtils.player().removePotionEffect(Potion.digSpeed.id);
		}
		super.disable();
	}
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		if (this.potion) {
			this.setSuffix("Potion");
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 0, 1));
		} else {
			this.setSuffix("Vanilla");
		mc.playerController.blockHitDelay = 0;
        if (mc.playerController.curBlockDamageMP >= 0.7f) {
            mc.playerController.curBlockDamageMP = 1.0f;
        }
		}
	}
}