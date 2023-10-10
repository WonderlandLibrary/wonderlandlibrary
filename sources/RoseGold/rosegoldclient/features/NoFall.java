package rosegoldclient.features;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.PlayerMoveEvent;

public class NoFall {

    @SubscribeEvent
    public void onPlayerMove(PlayerMoveEvent.Post event) {
        if(!Main.configFile.noFall) return;
        if (Main.mc.player.fallDistance > 3)
            Main.mc.player.connection.sendPacket(new CPacketPlayer(true));
    }
}
