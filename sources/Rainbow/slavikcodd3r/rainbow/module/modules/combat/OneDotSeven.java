package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "1.7")
public class OneDotSeven extends Module
{
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		if (mc.thePlayer.isBlocking() && mc.gameSettings.keyBindAttack.pressed || mc.thePlayer.isEating() && mc.gameSettings.keyBindAttack.pressed) {
			mc.clickMouse();
		}
	}
}
