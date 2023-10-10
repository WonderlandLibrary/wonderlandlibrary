package tech.atani.client.feature.module.impl.misc;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import tech.atani.client.feature.anticheat.AntiCheat;
import tech.atani.client.feature.anticheat.data.PlayerData;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;

@ModuleData(category = Category.MISCELLANEOUS, description = "Detects other cheaters", name = "HackerDetector")
public class HackerDetector extends Module {

    @Listen
    public final void onUpdate(UpdateEvent updateEvent) {
        if(mc.thePlayer != null) {
            AntiCheat.INSTANCE.handlePlayers();
        }
    }

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if(mc.thePlayer != null) {
            AntiCheat.INSTANCE.getPlayers().values().stream().filter(d -> d.getTicksExisted() > 80).forEach(d -> d.handle(packetEvent.getPacket()));
        }
    }

    @Override
    public void onEnable() {
        AntiCheat.INSTANCE.getPlayers().clear();

        mc.theWorld.playerEntities.stream().filter(e -> e != mc.thePlayer)
            .forEach(e -> AntiCheat.INSTANCE.getPlayers().put(e.getUniqueID(), new PlayerData((EntityOtherPlayerMP) e)));
    }

    @Override
    public void onDisable() {
        AntiCheat.INSTANCE.getPlayers().clear();
    }

}
