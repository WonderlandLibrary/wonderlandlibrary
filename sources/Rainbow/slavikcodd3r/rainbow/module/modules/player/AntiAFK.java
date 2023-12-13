package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "AntiAFK")
public class AntiAFK extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
		mc.gameSettings.keyBindJump.pressed = true;
        if (mc.thePlayer.ticksExisted % 10 == 0) {
            mc.thePlayer.rotationYaw += 180f;
        }
	}
	
	public void disable() {
		mc.gameSettings.keyBindJump.pressed = false;
		super.disable();
	}
}
