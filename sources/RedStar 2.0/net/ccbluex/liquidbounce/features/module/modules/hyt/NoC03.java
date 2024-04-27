package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.network.play.client.CPacketPlayer;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoC03", description="取消C03", category=ModuleCategory.HYT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\n\b\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J0\b2\t0\nHR0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/hyt/NoC03;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "debugValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getDebugValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "Pride"})
public final class NoC03
extends Module {
    @NotNull
    private final BoolValue debugValue = new BoolValue("Debug", false);

    @NotNull
    public final BoolValue getDebugValue() {
        return this.debugValue;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IPacket $this$unwrap$iv = event.getPacket();
        boolean $i$f$unwrap = false;
        Object packet = ((PacketImpl)$this$unwrap$iv).getWrapped();
        if (packet instanceof CPacketPlayer) {
            event.cancelEvent();
            if (((Boolean)this.debugValue.get()).booleanValue()) {
                ClientUtils.displayChatMessage("Cancel C03");
            }
        }
    }
}
