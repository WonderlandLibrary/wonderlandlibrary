package tech.atani.client.feature.module.impl.option;

import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.value.impl.CheckBoxValue;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@ModuleData(name = "Security", description = "Patches common exploits", category = Category.OPTIONS, frozenState = true, enabled = true)
public class Security extends Module {

    public final CheckBoxValue antiResourcePackExploit = new CheckBoxValue("Anti Resource Pack Exploit", "Prevent servers using the resource pack exploit to scan your files?", this, true);
    public final CheckBoxValue antiCrashExploits = new CheckBoxValue("Anti Crash Exploits", "Prevent servers using exploits to try and crash the client?", this, true);

    @Listen
    public void onUpdate(PacketEvent event) {
        if(mc.thePlayer == null || mc.theWorld == null)
    		return;
        if(antiResourcePackExploit.getValue()) {
            if (event.getType() == PacketEvent.Type.INCOMING) {
                if(event.getPacket() instanceof S48PacketResourcePackSend) {
                    final S48PacketResourcePackSend s48 = (S48PacketResourcePackSend) event.getPacket();
                    String url = s48.getURL(), hash = s48.getHash();
                    try {
                        URI uri = new URI(url);
                        String scheme = uri.getScheme();
                        boolean isLevelProtocol = "level".equals(scheme);
                        if (!"http".equals(scheme) && !"https".equals(scheme) && !isLevelProtocol) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                            event.setCancelled(true);
                        }
                        url = URLDecoder.decode(url.substring("level://".length()), StandardCharsets.UTF_8.toString());
                        if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
                            sendMessage("SERVER TRIED TO ACCESS YOUR FILE USING EXPLOITS: " + uri);
                            event.setCancelled(true);
                        }
                    } catch (URISyntaxException | UnsupportedEncodingException ex) {
                        event.setCancelled(true);
                    }
                }
            }
        }
        if(antiCrashExploits.getValue()) {
            if (event.getPacket() instanceof S2APacketParticles) {
                final S2APacketParticles packetParticles = (S2APacketParticles) event.getPacket();
                if (packetParticles.getParticleCount() > 400 || Math.abs(packetParticles.getParticleSpeed()) > 400) {
                    event.setCancelled(true);
                    sendMessage("Server tried to crash the client with particles packet");
                }
            }
            if (event.getPacket() instanceof S27PacketExplosion) {
                final S27PacketExplosion ePacket = (S27PacketExplosion) event.getPacket();
                if (Math.abs(ePacket.getStrength()) > 99 || Math.abs(ePacket.getX()) > 99 || Math.abs(ePacket.getZ()) > 99 || Math.abs(ePacket.getY()) > 99) {
                    event.setCancelled(true);
                    sendMessage("Server tried to crash the client with explosion packet");
                }
            }
            if (event.getPacket() instanceof S2DPacketOpenWindow) {
                final S2DPacketOpenWindow packetOpenWindow = (S2DPacketOpenWindow) event.getPacket();
                if (Math.abs(packetOpenWindow.getSlotCount()) > 70) {
                    sendMessage("Server tried to crash the client with window packet");
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}