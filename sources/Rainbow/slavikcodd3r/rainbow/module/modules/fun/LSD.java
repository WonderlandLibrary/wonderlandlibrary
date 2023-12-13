package slavikcodd3r.rainbow.module.modules.fun;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "LSD")
public class LSD extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
    public void enable() {
        mc.entityRenderer.shaderIndex = 18;
        mc.entityRenderer.activateNextShader();
    	super.enable();
    }
	
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
	if (ClientUtils.mc().entityRenderer.theShaderGroup == null) {
        mc.entityRenderer.shaderIndex = 18;
        mc.entityRenderer.activateNextShader();
	}
    }
    
	public void disable() {
        mc.entityRenderer.shaderIndex = 0;
        mc.entityRenderer.activateNextShader();
		super.disable();
	}
}
