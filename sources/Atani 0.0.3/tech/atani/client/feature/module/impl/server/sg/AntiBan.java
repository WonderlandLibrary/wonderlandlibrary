package tech.atani.client.feature.module.impl.server.sg;

import net.minecraft.network.play.server.S02PacketChat;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.option.Security;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;

@ModuleData(name = "AntiBan", identifier = "mc.survival-games.cz AntiBan", description = "This actually works sometimes xd", category = Category.SERVER, supportedIPs = {"mc.survival-games.cz", "play.survival-games.cz"})
public class AntiBan extends Module {

    private Security security;

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if(security == null) {
            security = ModuleStorage.getInstance().getByClass(Security.class);
        }
        if(!security.antiCrashExploits.getValue()) {
            security.antiCrashExploits.setValue(true);
        }
        if(packetEvent.getPacket() instanceof S02PacketChat) {
            S02PacketChat s02 = (S02PacketChat) packetEvent.getPacket();
            if(s02.getChatComponent().getUnformattedText().contains("CHEATER CHEATER CHEATER")) {
                mc.thePlayer.sendChatMessage("/hub");
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
