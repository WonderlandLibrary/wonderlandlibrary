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
    public final void onPacket(PacketEvent packetEvent) {
        if (MinecraftInstance.classProvider.isSPacketResourcePackSend(packetEvent.getPacket())) {
            ISPacketResourcePackSend iSPacketResourcePackSend = packetEvent.getPacket().asSPacketResourcePackSend();
            String string = iSPacketResourcePackSend.getUrl();
            String string2 = iSPacketResourcePackSend.getHash();
            try {
                String string3 = new URI(string).getScheme();
                boolean bl = "level".equals(string3);
                if ("http".equals(string3) ^ true && "https".equals(string3) ^ true && !bl) {
                    throw (Throwable)new URISyntaxException(string, "Wrong protocol");
                }
                if (bl && (string.equals("..") || !StringsKt.endsWith$default((String)string, (String)"/resources.zip", (boolean)false, (int)2, null))) {
                    throw (Throwable)new URISyntaxException(string, "Invalid levelstorage resourcepack path");
                }
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createICPacketResourcePackStatus(iSPacketResourcePackSend.getHash(), ICPacketResourcePackStatus.WAction.ACCEPTED));
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createICPacketResourcePackStatus(iSPacketResourcePackSend.getHash(), ICPacketResourcePackStatus.WAction.SUCCESSFULLY_LOADED));
            }
            catch (URISyntaxException uRISyntaxException) {
                ClientUtils.getLogger().error("Failed to handle resource pack", (Throwable)uRISyntaxException);
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createICPacketResourcePackStatus(string2, ICPacketResourcePackStatus.WAction.FAILED_DOWNLOAD));
            }
        }
    }
}

