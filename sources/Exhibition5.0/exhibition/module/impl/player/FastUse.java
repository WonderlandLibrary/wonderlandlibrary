// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.player;

import exhibition.event.RegisterEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class FastUse extends Module
{
    public FastUse(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre() && FastUse.mc.thePlayer.getItemInUseDuration() == 12 && !(FastUse.mc.thePlayer.getItemInUse().getItem() instanceof ItemSword) && !(FastUse.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)) {
                for (int i = 0; i < 30; ++i) {
                    FastUse.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                }
                FastUse.mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                FastUse.mc.thePlayer.stopUsingItem();
            }
        }
    }
}
