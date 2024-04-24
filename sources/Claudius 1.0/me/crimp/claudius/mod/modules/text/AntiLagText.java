package me.crimp.claudius.mod.modules.text;

import me.crimp.claudius.event.events.PacketEvent;
import me.crimp.claudius.mod.modules.Module;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiLagText extends Module {
    public AntiLagText() {
        super("AntiLagTxt", "Stops Annoying Chinese Lag Text", Category.Text, false, false);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            String text = ((SPacketChat) event.getPacket()).getChatComponent().getUnformattedText();
            if (text.contains("\u0B01") || text.contains("\u0201") || text.contains("\u2701")) {
                event.setCanceled(true);
            }
        }
    }
}
