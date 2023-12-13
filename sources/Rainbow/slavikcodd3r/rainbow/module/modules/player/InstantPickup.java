package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;

import java.util.Iterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

@Module.Mod(displayName = "InstantPickup")
public class InstantPickup extends Module
{
    @Op(name = "Packets", min = 10.0, max = 1000.0, increment = 10.0)
    private static double packets;
    Minecraft mc = Minecraft.getMinecraft();
    
    static {
        InstantPickup.packets = 200.0;
    }
    
    public Entity getItems(final double range) {
        Entity tempEntity = null;
        double dist = range;
        for (final Object i : Minecraft.theWorld.loadedEntityList) {
            final Entity entity = (Entity)i;
            if (ClientUtils.mc().thePlayer.onGround && ClientUtils.mc().thePlayer.isCollidedVertically && entity instanceof EntityItem) {
                final double curDist = ClientUtils.mc().thePlayer.getDistanceToEntity(entity);
                if (curDist > dist) {
                    continue;
                }
                dist = curDist;
                tempEntity = entity;
            }
        }
        return tempEntity;
    }
    
    @EventTarget
    public void onPreTick(final TickEvent e) {
    	if (mc.theWorld != null) {
        if (this.isEnabled()) {
            final Entity items = this.getItems(1.0);
            if (items != null) {
                for (int i = 0; i < InstantPickup.packets; ++i) {
                    ClientUtils.mc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
            }
        }
    	}
    }
}
