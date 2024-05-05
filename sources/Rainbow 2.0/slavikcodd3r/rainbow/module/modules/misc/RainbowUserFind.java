package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "RainbowUserFind")
public class RainbowUserFind extends Module
{   
    public static boolean toggled;
	Minecraft mc = Minecraft.getMinecraft();
	
	public void enable() {
		mc.thePlayer.setRainbowUser(true);
		super.enable();
	}
	
	public void disable() {	
		toggled = false;
		mc.thePlayer.setRainbowUser(false);
		super.disable();
	}
	
	@EventTarget
    public void onUpdate(final UpdateEvent e) {
		toggled = true;
		mc.thePlayer.setRainbowUser(true);
		for (final Object theObject : Minecraft.theWorld.loadedEntityList) {
			if (theObject instanceof EntityLivingBase) {
					final EntityLivingBase entityplayer = (EntityLivingBase)theObject;
					if (!entityplayer.isRainbowUser()) {
						continue;
					}
					entityplayer.setCustomNameTag("[Rainbow User] " + entityplayer.getName());
					entityplayer.getDisplayName();
			}
		}
	}
}
