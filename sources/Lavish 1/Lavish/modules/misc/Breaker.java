// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.misc;

import Lavish.utils.misc.NetUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.block.BlockBed;
import net.minecraft.util.BlockPos;
import Lavish.event.events.EventMotion;
import Lavish.event.Event;
import Lavish.modules.Category;
import Lavish.utils.misc.Timer;
import Lavish.modules.Module;

public class Breaker extends Module
{
    Timer timer;
    
    public Breaker() {
        super("Breaker", 0, true, Category.Player, "Break beds in a certain radius around you");
        this.timer = new Timer();
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            for (int x = -5; x < 5; ++x) {
                for (int y = -5; y < 5; ++y) {
                    for (int z = -5; z < 5; ++z) {
                        final BlockPos pos = new BlockPos(Breaker.mc.thePlayer.posX + x, Breaker.mc.thePlayer.posY + y, Breaker.mc.thePlayer.posZ + z);
                        if (pos.getBlock() instanceof BlockBed) {
                            Breaker.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                            Breaker.mc.thePlayer.swingItem();
                            NetUtil.sendPacketNoEvents(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.NORTH));
                            if (this.timer.hasTimeElapsed(300.0, true)) {
                                NetUtil.sendPacketNoEvents(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.NORTH));
                            }
                        }
                    }
                }
            }
        }
    }
}
