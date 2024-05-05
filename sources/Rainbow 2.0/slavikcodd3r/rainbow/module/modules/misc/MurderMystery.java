package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.MurderMysteryUtils;

@Module.Mod(displayName = "MurderMystery")
public class MurderMystery extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    private void onUpdate(final UpdateEvent event) {
        for (final Object theObject : Minecraft.theWorld.loadedEntityList) {
            if (theObject instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)theObject;
                if (entityplayer == mc.thePlayer) {
                    continue;
                }
                if (!MurderMysteryUtils.isMurderer(entityplayer)) {
                    continue;
                }
                ClientUtils.sendMessage(String.valueOf(entityplayer.getName()) + " is the murderer!");
            }
        }
	}
}
