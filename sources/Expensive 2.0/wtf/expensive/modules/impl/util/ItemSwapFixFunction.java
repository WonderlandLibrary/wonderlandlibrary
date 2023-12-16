package wtf.expensive.modules.impl.util;

import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.server.SHeldItemChangePacket;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

/**
 * @author dedinside
 * @since 12.06.2023
 */
@FunctionAnnotation(name = "ItemSwapFix", type = Type.Util)
public class ItemSwapFixFunction extends Function {
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventPacket packetEvent) {
            if (packetEvent.isReceivePacket()) {
                if (packetEvent.getPacket() instanceof SHeldItemChangePacket packetHeldItemChange) {
                    mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                    event.setCancel(true);
                }
            }
        }
    }
}
