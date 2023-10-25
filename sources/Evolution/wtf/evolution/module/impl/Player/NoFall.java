package wtf.evolution.module.impl.Player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(name = "NoFall", type = Category.Player)
public class NoFall extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla").call(this);

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mode.get().equals("Vanilla")) {
            if (mc.player.fallDistance >= 3) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                mc.player.fallDistance = 0;
            }
        }

    }

}
