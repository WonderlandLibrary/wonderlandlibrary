package koks.irc.events;

import de.liquiddev.ircclient.api.CustomDataListener;
import de.liquiddev.ircclient.client.ClientType;
import de.liquiddev.ircclient.client.IrcPlayer;
import god.buddy.aot.BCompiler;
import lombok.SneakyThrows;

import java.awt.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class IrcCustomDataEvent implements CustomDataListener {

    @SneakyThrows
    @Override
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void onCustomDataReceived(IrcPlayer ircPlayer, String s, byte[] bytes) {
        final String info = new String(bytes, StandardCharsets.UTF_8);
        if (ircPlayer.getClientName().equalsIgnoreCase(ClientType.QUERY.getName()) && info.startsWith("https://discord.gg"))
            if (s.equalsIgnoreCase("sendKoksInvite")) {
                Desktop.getDesktop().browse(new URI(info));
            }
    }
}
