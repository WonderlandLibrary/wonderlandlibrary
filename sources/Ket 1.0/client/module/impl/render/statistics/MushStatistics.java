package client.module.impl.render.statistics;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.module.impl.render.Statistics;
import client.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

public class MushStatistics extends Mode<Statistics> {

    public MushStatistics(final String name, final Statistics parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S02PacketChat) {
            final S02PacketChat wrapper = (S02PacketChat) packet;
            final String message = wrapper.getChatComponent().getUnformattedText(), name = mc.thePlayer.getName();
            if (message.equals(name + " caiu no void.")) getParent().deaths++;
            if (message.equals(name + " saiu da sala.")) getParent().deaths++;
            final String s = " morreu para ";
            if (message.contains(s)) {
                final String[] strings = message.split(s);
                if (strings[0].equals(name)) getParent().deaths++;
                if (strings[1].equals(name + ".")) getParent().kills++;
            }
            final String s1 = " foi jogado no void por ";
            if (message.contains(s1)) {
                final String[] strings = message.split(s1);
                if (strings[0].equals(name)) getParent().deaths++;
                if (strings[1].equals(name + ".")) getParent().kills++;
            }
            final String s2 = " derrubou ", s3 = " de muito alto!";
            if (message.contains(s2) && message.contains(s3)) {
                final String[] strings = message.split(s2);
                if (strings[0].equals(name)) getParent().deaths++;
                if (strings[1].equals(name + s3)) getParent().kills++;
            }
            if (message.equals("Deseja jogar novamente? CLIQUE AQUI!")) getParent().gamesPlayed++;
            final String s4 = "Vencedor: ";
            if (message.contains(s4)) {
                if (message.split(s4)[1].contains(", ")) {
                    final String[] strings = message.split(s4)[1].split(", ");
                    if (strings[0].equals(name) || strings[1].equals(name)) getParent().wins++;
                } else if (message.split(s4)[1].equals(name)) getParent().wins++;
            }
        }
    };
}