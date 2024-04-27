/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.net.URI;
import java.net.URISyntaxException;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketResourcePackStatus;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketResourcePackSend;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

@ModuleInfo(name="ResourcePackSpoof", description="Prevents servers from forcing you to download their resource pack.", category=ModuleCategory.MISC)
public final class ResourcePackSpoof
extends Module {
    @EventTarget
    public final void onPacket(PacketEvent event) {
        if (MinecraftInstance.classProvider.isSPacketResourcePackSend(event.getPacket())) {
            ISPacketResourcePackSend packet = event.getPacket().asSPacketResourcePackSend();
            String url = packet.getUrl();
            String hash = packet.getHash();
            try {
                String scheme = new URI(url).getScheme();
                boolean isLevelProtocol = "level".equals(scheme);
                if ("http".equals(scheme) ^ true && "https".equals(scheme) ^ true && !isLevelProtocol) {
                    throw (Throwable)new URISyntaxException(url, "Wrong protocol");
                }
                if (isLevelProtocol && (url.equals("..") || !StringsKt.endsWith$default((String)url, (String)"/resources.zip", (boolean)false, (int)2, null))) {
                    throw (Throwable)new URISyntaxException(url, "Invalid levelstorage resourcepack path");
                }
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createICPacketResourcePackStatus(packet.getHash(), ICPacketResourcePackStatus.WAction.ACCEPTED));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createICPacketResourcePackStatus(packet.getHash(), ICPacketResourcePackStatus.WAction.SUCCESSFULLY_LOADED));
            }
            catch (URISyntaxException e) {
                ClientUtils.getLogger().error("Failed to handle resource pack", (Throwable)e);
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createICPacketResourcePackStatus(hash, ICPacketResourcePackStatus.WAction.FAILED_DOWNLOAD));
            }
        }
    }
}

