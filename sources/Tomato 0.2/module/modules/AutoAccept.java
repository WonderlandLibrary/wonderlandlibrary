/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPacketReceive;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.utils.Wrapper;
import java.io.PrintStream;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;

public class AutoAccept
extends Module {
    public AutoAccept() {
        super("AutoAccept", 0, Category.PLAYER, 3394662, true, "AutoAccepts fwiieeends", new String[]{"autoaccept", "AutoAccept", "Autoaccept"});
    }

    @EventTarget
    public void onPacketReceive(EventPacketReceive event) {
        if (!this.getState()) {
            return;
        }
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat chat = (S02PacketChat)event.getPacket();
            String message = StringUtils.stripControlCodes(chat.func_148915_c().getFormattedText());
            for (FriendManager.Friend names : FriendManager.friends) {
                if (!message.contains(names.getName()) || !message.contains("has requested to teleport to you.") && !message.contains("has requested you teleport to them.")) continue;
                Wrapper.mc.thePlayer.sendChatMessage("/tpaccept");
                System.out.println("reached");
            }
        }
    }
}

