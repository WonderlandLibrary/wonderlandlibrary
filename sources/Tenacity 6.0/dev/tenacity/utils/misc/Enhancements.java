// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.net.URISyntaxException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import java.net.URI;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.client.network.NetHandlerPlayClient;

public class Enhancements
{
    public static boolean validateResourcePackUrl(final NetHandlerPlayClient client, final S48PacketResourcePackSend packet) {
        try {
            String url = packet.getURL();
            final URI uri = new URI(url);
            final String scheme = uri.getScheme();
            final boolean isLevelProtocol = scheme.equals("level");
            if (!scheme.equals("http") && !scheme.equals("https") && !isLevelProtocol) {
                client.getNetworkManager().sendPacket(new C19PacketResourcePackStatus(packet.getHash(), C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                throw new URISyntaxException(url, "Wrong protocol");
            }
            url = URLDecoder.decode(url.substring("level://".length()), StandardCharsets.UTF_8.toString());
            if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
                throw new URISyntaxException(url, "Invalid levelstorage resourcepack path");
            }
            return true;
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
