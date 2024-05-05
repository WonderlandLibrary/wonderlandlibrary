package slavikcodd3r.rainbow.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.event.events.Render3DEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.MathUtils;

@Module.Mod(displayName = "FPSBoost")
public class FPSBoost extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    public void onUpdate(final Render2DEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (mc.thePlayer.rotationYaw > mc.gameSettings.fovSetting + 2.0f && mc.thePlayer.rotationPitch > mc.gameSettings.fovSetting + 2.0f) {
            event.setCancelled(true);
        }
    }
	
	@EventTarget
    public void onUpdate(final Render3DEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (mc.thePlayer.rotationYaw > mc.gameSettings.fovSetting + 2.0f && mc.thePlayer.rotationPitch > mc.gameSettings.fovSetting + 2.0f) {
            event.setCancelled(true);
        }
    }
}
