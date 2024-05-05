package club.dortware.client.module.impl.misc;

import club.dortware.client.Client;
import club.dortware.client.event.impl.PacketEvent;
import club.dortware.client.event.impl.enums.PacketDirection;
import club.dortware.client.module.Module;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

@ModuleData(name = "Name Protect", category = ModuleCategory.MISC)
public class NameProtect extends Module {

    public static boolean isEnabled() {
        return Client.INSTANCE.getModuleManager().get(NameProtect.class).isToggled();
    }

    public static String getProtectedName() {
        return "Dort";
    }

    @Subscribe
    public void onPacket(PacketEvent packetEvent) {
        if (packetEvent.getPacketDirection() == PacketDirection.INBOUND) {
            if (packetEvent.getPacket() instanceof S02PacketChat) {
                S02PacketChat chat = (S02PacketChat) packetEvent.getPacket();
                IChatComponent chatComponent = chat.func_148915_c();
                if (chatComponent instanceof ChatComponentText && chat.isChat()) {
                    String text = chatComponent.getFormattedText().replace(mc.thePlayer.getName(), getProtectedName());
                    packetEvent.setPacket(new S02PacketChat(new ChatComponentText(text)));
                }
            }
        }
    }

    @Override
    public void setup() {

    }
}
