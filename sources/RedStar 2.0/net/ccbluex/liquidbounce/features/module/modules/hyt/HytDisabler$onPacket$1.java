package net.ccbluex.liquidbounce.features.module.modules.hyt;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import me.utils.PacketUtils;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.modules.hyt.HytDisabler;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\u0000020H¢\b"}, d2={"onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "invoke"})
final class HytDisabler$onPacket$1
extends Lambda
implements Function1<UpdateEvent, Unit> {
    final HytDisabler this$0;

    @Override
    @EventTarget
    public final void invoke(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        String string = (String)this.this$0.getModeValue().get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case -330391888: {
                if (!string.equals("hytspartan") || !this.this$0.msTimer.hasTimePassed(3000L) || this.this$0.keepAlives.size() <= 0 || this.this$0.transactions.size() <= 0) break;
                Object e = this.this$0.keepAlives.get(this.this$0.keepAlives.size() - 1);
                Intrinsics.checkExpressionValueIsNotNull(e, "keepAlives[keepAlives.size - 1]");
                PacketUtils.INSTANCE.send((CPacketKeepAlive)e);
                Object e2 = this.this$0.transactions.get(this.this$0.transactions.size() - 1);
                Intrinsics.checkExpressionValueIsNotNull(e2, "transactions[transactions.size - 1]");
                PacketUtils.INSTANCE.send((CPacketConfirmTransaction)e2);
                this.this$0.debug("c00 no." + (this.this$0.keepAlives.size() - 1) + " sent.");
                this.this$0.debug("c0f no." + (this.this$0.transactions.size() - 1) + " sent.");
                this.this$0.keepAlives.clear();
                this.this$0.transactions.clear();
                this.this$0.msTimer.reset();
                break;
            }
        }
    }

    HytDisabler$onPacket$1(HytDisabler hytDisabler) {
        this.this$0 = hytDisabler;
        super(1);
    }
}
