package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.net.URI;
import java.net.URISyntaxException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="ResourcePackSpoof", description="Prevents servers from forcing you to download their resource pack.", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J020H¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/ResourcePackSpoof;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "Pride"})
public final class ResourcePackSpoof
extends Module {
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (MinecraftInstance.classProvider.isSPacketResourcePackSend(event.getPacket())) {
            ISPacketResourcePackSend packet = event.getPacket().asSPacketResourcePackSend();
            String url = packet.getUrl();
            String hash = packet.getHash();
            try {
                String scheme = new URI(url).getScheme();
                boolean isLevelProtocol = Intrinsics.areEqual("level", scheme);
                if (Intrinsics.areEqual("http", scheme) ^ true && Intrinsics.areEqual("https", scheme) ^ true && !isLevelProtocol) {
                    throw (Throwable)new URISyntaxException(url, "Wrong protocol");
                }
                if (isLevelProtocol && (StringsKt.contains$default((CharSequence)url, "..", false, 2, null) || !StringsKt.endsWith$default(url, "/resources.zip", false, 2, null))) {
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
