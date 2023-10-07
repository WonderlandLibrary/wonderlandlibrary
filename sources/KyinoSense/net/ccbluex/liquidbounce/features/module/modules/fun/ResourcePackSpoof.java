/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C19PacketResourcePackStatus
 *  net.minecraft.network.play.client.C19PacketResourcePackStatus$Action
 *  net.minecraft.network.play.server.S48PacketResourcePackSend
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import java.net.URI;
import java.net.URISyntaxException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="ResourcePackSpoof", description="Prevents servers from forcing you to download their resource pack.", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/fun/ResourcePackSpoof;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class ResourcePackSpoof
extends Module {
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof S48PacketResourcePackSend) {
            String url = ((S48PacketResourcePackSend)packet).func_179783_a();
            String hash = ((S48PacketResourcePackSend)packet).func_179784_b();
            try {
                String scheme = new URI(url).getScheme();
                boolean isLevelProtocol = Intrinsics.areEqual("level", scheme);
                if (Intrinsics.areEqual("http", scheme) ^ true && Intrinsics.areEqual("https", scheme) ^ true && !isLevelProtocol) {
                    throw (Throwable)new URISyntaxException(url, "Wrong protocol");
                }
                if (isLevelProtocol) {
                    String string = url;
                    Intrinsics.checkExpressionValueIsNotNull(string, "url");
                    if (StringsKt.contains$default((CharSequence)string, "..", false, 2, null) || !StringsKt.endsWith$default(url, "/resources.zip", false, 2, null)) {
                        throw (Throwable)new URISyntaxException(url, "Invalid levelstorage resourcepack path");
                    }
                }
                Minecraft minecraft = ResourcePackSpoof.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C19PacketResourcePackStatus(((S48PacketResourcePackSend)packet).func_179784_b(), C19PacketResourcePackStatus.Action.ACCEPTED));
                Minecraft minecraft2 = ResourcePackSpoof.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                minecraft2.func_147114_u().func_147297_a((Packet)new C19PacketResourcePackStatus(((S48PacketResourcePackSend)packet).func_179784_b(), C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
            }
            catch (URISyntaxException e) {
                ClientUtils.getLogger().error("Failed to handle resource pack", (Throwable)e);
                Minecraft minecraft = ResourcePackSpoof.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

