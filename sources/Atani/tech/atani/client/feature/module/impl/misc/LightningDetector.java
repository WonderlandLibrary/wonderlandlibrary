package tech.atani.client.feature.module.impl.misc;

import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;

@ModuleData(name = "LightningDetector", description = "Detects lightning and sends coordinates", category = Category.MISCELLANEOUS)
public class LightningDetector extends Module {

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if(packetEvent.getPacket() instanceof S2CPacketSpawnGlobalEntity) {
            S2CPacketSpawnGlobalEntity packet = (S2CPacketSpawnGlobalEntity) packetEvent.getPacket();

            int x = packet.func_149051_d() / 32;
            int y = packet.func_149050_e() / 32;
            int z = packet.func_149049_f() / 32;

            sendMessage("§7Detected lightning at §c" + x + " " + y + " " + z + "§7.");
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}