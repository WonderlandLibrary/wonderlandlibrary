package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "AntiBots", suffix = "Advanced")
public class AntiBots extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
	@EventTarget
    public void onUpdate(final UpdateEvent event) {
        for (final Object entity : ClientUtils.loadedEntityList()) {
            if (((EntityLivingBase)entity).isInvisible() && ((EntityLivingBase)entity).moveForward != 0 && ((EntityLivingBase)entity).moveStrafing != 0) {
                ClientUtils.world().removeEntity((EntityLivingBase)entity);
            }
        }
    }
}
