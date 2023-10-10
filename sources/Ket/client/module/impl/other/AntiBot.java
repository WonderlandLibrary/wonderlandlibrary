package client.module.impl.other;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.Render3DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import net.minecraft.entity.player.EntityPlayer;

@ModuleInfo(name = "Anti Bot", description = "Removes bots used by servers to detect Aura", category = Category.OTHER)
public class AntiBot extends Module {
    @EventLink
    public final Listener<Render3DEvent> onRender3D = event -> mc.theWorld.loadedEntityList.stream().filter(entity -> entity != mc.thePlayer && entity instanceof EntityPlayer && entity.isEntityAlive() && !Client.INSTANCE.getBotManager().contains(entity) && entity.getName().startsWith("§8")).forEach(entity -> Client.INSTANCE.getBotManager().add(entity));
}
