package info.sigmaclient.sigma.modules.player;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.network.play.client.CClientStatusPacket;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoRespawn extends Module {

    public AutoRespawn() {
        super("AutoRespawn", Category.Player, "Auto respawn");
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(mc.currentScreen instanceof DeathScreen){
                mc.getConnection().sendPacket(new CClientStatusPacket(CClientStatusPacket.State.PERFORM_RESPAWN));
                mc.displayGuiScreen(null);
            }
        }
        super.onUpdateEvent(event);
    }
}
