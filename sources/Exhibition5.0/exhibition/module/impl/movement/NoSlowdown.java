// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.movement;

import exhibition.event.RegisterEvent;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class NoSlowdown extends Module
{
    public NoSlowdown(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        final EventMotion em = (EventMotion)event;
        if (em.isPre() && NoSlowdown.mc.thePlayer.isBlocking()) {
            NoSlowdown.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        if (em.isPost() && NoSlowdown.mc.thePlayer.isBlocking()) {
            NoSlowdown.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(NoSlowdown.mc.thePlayer.inventory.getCurrentItem()));
        }
    }
}
