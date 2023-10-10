package client.module.impl.other.autoreport;

import client.Client;
import client.bot.BotManager;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.event.impl.other.UpdateEvent;
import client.module.ModuleManager;
import client.module.impl.combat.Friends;
import client.module.impl.other.AntiBot;
import client.module.impl.other.AutoReport;
import client.value.Mode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.concurrent.atomic.AtomicBoolean;

public class MushAutoReport extends Mode<AutoReport> {

    private int reportedTicks;

    public MushAutoReport(final String name, final AutoReport parent) {
        super(name, parent);
    }

    @Override
    public void onEnable() {
        reportedTicks = 0;
    }

    @EventLink
    public final Listener<UpdateEvent> onUpdate = event -> {
        if (reportedTicks == 0) {
            final AtomicBoolean found = new AtomicBoolean();
            mc.theWorld.loadedEntityList.stream().filter(entity -> {
                final ModuleManager moduleManager = Client.INSTANCE.getModuleManager();
                final BotManager botManager = Client.INSTANCE.getBotManager();
                return entity != mc.thePlayer && entity instanceof EntityPlayer && (!moduleManager.get(AntiBot.class).isEnabled() || !botManager.contains(entity)) && (!moduleManager.get(Friends.class).isEnabled() || !botManager.getFriends().contains(entity.getName())) && !getParent().getAlreadyReported().contains(entity.getName()) && !found.get();
            }).forEach(entity -> {
                mc.thePlayer.sendChatMessage("/report " + entity.getName() + " Trapaça");
                getParent().getAlreadyReported().add(entity.getName());
                found.set(true);
            });
            if (found.get()) reportedTicks = 100;
        } else reportedTicks--;
    };

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            final S02PacketChat wrapper = (S02PacketChat) packet;
            final String message = wrapper.getChatComponent().getUnformattedText();
            if (message.startsWith("Você denunciou ") && message.contains(" por ") && message.endsWith(":")) reportedTicks = 100;
        }
    };
}
