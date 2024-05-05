package slavikcodd3r.rainbow.module.modules.fun;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.PacketReceiveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.option.OptionManager;

@Module.Mod(displayName = "KillSults")
public class KillSults extends Module
{   
	Minecraft mc = Minecraft.getMinecraft();
	
    public static String randomMessage() {
        final String[] randomMessages = { "L", "ez4", "ezzz", "noob", "ahah", "nooooob", "ahahahaha", "0", "kid", "XDD" };
        return randomMessages[RandomUtils.nextInt(0, randomMessages.length - 1)];
    }
	
	@EventTarget
	public void onPacket(final PacketReceiveEvent event) {
        if (mc.thePlayer != null && mc.thePlayer.ticksExisted >= 0 && event.getPacket() instanceof S02PacketChat) {
            final String look = "killed by " + mc.thePlayer.getName();
            final String cubecraftlook = "was stain by " + mc.thePlayer.getName();
            final S02PacketChat cp = (S02PacketChat)event.getPacket();
            final String cp2 = cp.func_148915_c().getUnformattedText();
            if ((cp2.startsWith(mc.thePlayer.getName() + "(") && cp2.contains("asesino ha")) || cp2.contains(look) || cp2.contains(cubecraftlook) || cp2.contains("You have been rewarded $50 and 2 point(s)!")) {
                mc.thePlayer.sendChatMessage(randomMessage());
            }
        }
    }
}

