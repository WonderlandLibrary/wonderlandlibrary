/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C19PacketResourcePackStatus
 *  net.minecraft.network.play.client.C19PacketResourcePackStatus$Action
 *  net.minecraft.network.play.server.S48PacketResourcePackSend
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.misc;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import org.jetbrains.annotations.NotNull;

@Info(name="PackSpoofer", spacedName="Pack Spoofer", description="Prevents servers from forcing you to download their resource pack.", category=Category.MISC, cnName="\u4e0d\u4e0b\u8f7d\u670d\u52a1\u5668\u8d44\u6e90\u5305")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/misc/PackSpoofer;", "Lnet/dev/important/modules/module/Module;", "()V", "onPacket", "", "event", "Lnet/dev/important/event/PacketEvent;", "LiquidBounce"})
public final class PackSpoofer
extends Module {
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof S48PacketResourcePackSend) {
            String url = ((S48PacketResourcePackSend)packet).func_179783_a();
            String hash = ((S48PacketResourcePackSend)packet).func_179784_b();
            try {
                String scheme = new URI(url).getScheme();
                boolean isLevelProtocol = Intrinsics.areEqual("level", scheme);
                if (!(Intrinsics.areEqual("http", scheme) || Intrinsics.areEqual("https", scheme) || isLevelProtocol)) {
                    throw new URISyntaxException(url, "Wrong protocol");
                }
                if (isLevelProtocol) {
                    Intrinsics.checkNotNullExpressionValue(url, "url");
                    if (StringsKt.contains$default((CharSequence)url, "..", false, 2, null) || !StringsKt.endsWith$default(url, ".zip", false, 2, null)) {
                        String string = url.substring("level://".length());
                        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).substring(startIndex)");
                        String s2 = string;
                        File file1 = new File(MinecraftInstance.mc.field_71412_D, "saves");
                        File file2 = new File(file1, s2);
                        if (!file2.isFile() || StringsKt.contains((CharSequence)url, "liquidbounce", true)) {
                            throw new URISyntaxException(url, "Invalid levelstorage resourcepack path");
                        }
                    }
                }
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C19PacketResourcePackStatus(((S48PacketResourcePackSend)packet).func_179784_b(), C19PacketResourcePackStatus.Action.ACCEPTED));
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C19PacketResourcePackStatus(((S48PacketResourcePackSend)packet).func_179784_b(), C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
            }
            catch (URISyntaxException e) {
                ClientUtils.getLogger().error("Failed to handle resource pack", (Throwable)e);
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
            event.cancelEvent();
        }
    }
}

