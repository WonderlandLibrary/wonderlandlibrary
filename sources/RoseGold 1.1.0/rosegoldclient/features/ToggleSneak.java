package rosegoldclient.features;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.TickEndEvent;

public class ToggleSneak {

    private boolean wasSneaking = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if(Main.toggleSneak) {
            Main.mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(Main.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            wasSneaking = true;
        } else if(wasSneaking) {
            Main.mc.getConnection().getNetworkManager().sendPacket(new CPacketEntityAction(Main.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            wasSneaking = false;
        }
    }
}
