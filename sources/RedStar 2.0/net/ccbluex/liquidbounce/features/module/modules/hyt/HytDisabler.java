package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import me.utils.PacketUtils;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="HytDisabler", description="修复版", category=ModuleCategory.HYT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000J\n\n\n\b\n\n\b\n\n\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\b\u000020B¢J020J020HR0X¢\n\u0000R0X¢\n\u0000R0\b0j\b0\b`\tX¢\n\u0000R\n0¢\b\n\u0000\b\f\rR0X¢\n\u0000R00j\b0`\tX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/hyt/HytDisabler;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "HytAAC", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "debugValue", "keepAlives", "Ljava/util/ArrayList;", "Lnet/minecraft/network/play/client/CPacketKeepAlive;", "Lkotlin/collections/ArrayList;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "msTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "transactions", "Lnet/minecraft/network/play/client/CPacketConfirmTransaction;", "debug", "", "s", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "Pride"})
public final class HytDisabler
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"HytSpartan", "hytaac"}, "HytSpartan");
    private final BoolValue HytAAC = new BoolValue("HytAAC", false);
    private final BoolValue debugValue = new BoolValue("Debug", false);
    private final ArrayList<CPacketKeepAlive> keepAlives;
    private final ArrayList<CPacketConfirmTransaction> transactions;
    private final MSTimer msTimer;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public final void debug(@NotNull String s) {
        Intrinsics.checkParameterIsNotNull(s, "s");
        if (((Boolean)this.debugValue.get()).booleanValue()) {
            ClientUtils.displayChatMessage("§7[§3§lDisabler§7]§f " + s);
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        packet = event.getPacket();
        var3_3 = (String)this.modeValue.get();
        var4_4 = false;
        v0 = var3_3;
        if (v0 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v1 = v0.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toLowerCase()");
        var3_3 = v1;
        switch (var3_3.hashCode()) {
            case -1202237472: {
                if (!var3_3.equals("hytaac")) ** break;
                break;
            }
            case -330391888: {
                if (!var3_3.equals("hytspartan")) ** break;
                if (packet instanceof CPacketKeepAlive && (this.keepAlives.size() <= 0 || Intrinsics.areEqual(packet, this.keepAlives.get(this.keepAlives.size() - 1)) ^ true)) {
                    this.debug("c00 added");
                    this.keepAlives.add((CPacketKeepAlive)packet);
                    event.cancelEvent();
                }
                if (!(packet instanceof CPacketConfirmTransaction) || this.transactions.size() > 0 && !(Intrinsics.areEqual(packet, this.transactions.get(this.transactions.size() - 1)) ^ true)) ** break;
                this.debug("c0f added");
                this.transactions.add((CPacketConfirmTransaction)packet);
                event.cancelEvent();
                ** break;
            }
        }
        if (!((Boolean)this.HytAAC.get()).booleanValue()) ** break;
        event.getPacket() instanceof SPacketPlayerPosLook;
        s08 = event.getPacket();
        this.debug("[Disabler] 屏蔽S类型发包数据");
        ** break;
lbl34:
        // 7 sources

        $fun$onUpdate$1 = new Function1<UpdateEvent, Unit>(this){
            final HytDisabler this$0;

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
                        if (!string.equals("hytspartan") || !HytDisabler.access$getMsTimer$p(this.this$0).hasTimePassed(3000L) || HytDisabler.access$getKeepAlives$p(this.this$0).size() <= 0 || HytDisabler.access$getTransactions$p(this.this$0).size() <= 0) break;
                        E e = HytDisabler.access$getKeepAlives$p(this.this$0).get(HytDisabler.access$getKeepAlives$p(this.this$0).size() - 1);
                        Intrinsics.checkExpressionValueIsNotNull(e, "keepAlives[keepAlives.size - 1]");
                        PacketUtils.INSTANCE.send((CPacketKeepAlive)e);
                        E e2 = HytDisabler.access$getTransactions$p(this.this$0).get(HytDisabler.access$getTransactions$p(this.this$0).size() - 1);
                        Intrinsics.checkExpressionValueIsNotNull(e2, "transactions[transactions.size - 1]");
                        PacketUtils.INSTANCE.send((CPacketConfirmTransaction)e2);
                        this.this$0.debug("c00 no." + (HytDisabler.access$getKeepAlives$p(this.this$0).size() - 1) + " sent.");
                        this.this$0.debug("c0f no." + (HytDisabler.access$getTransactions$p(this.this$0).size() - 1) + " sent.");
                        HytDisabler.access$getKeepAlives$p(this.this$0).clear();
                        HytDisabler.access$getTransactions$p(this.this$0).clear();
                        HytDisabler.access$getMsTimer$p(this.this$0).reset();
                        break;
                    }
                }
            }
            {
                this.this$0 = hytDisabler;
                super(1);
            }
        };
    }

    public HytDisabler() {
        HytDisabler hytDisabler = this;
        boolean bl = false;
        ArrayList arrayList = new ArrayList();
        hytDisabler.keepAlives = arrayList;
        hytDisabler = this;
        bl = false;
        arrayList = new ArrayList();
        hytDisabler.transactions = arrayList;
        this.msTimer = new MSTimer();
    }

    public static final ArrayList access$getKeepAlives$p(HytDisabler $this) {
        return $this.keepAlives;
    }

    public static final ArrayList access$getTransactions$p(HytDisabler $this) {
        return $this.transactions;
    }

    public static final MSTimer access$getMsTimer$p(HytDisabler $this) {
        return $this.msTimer;
    }
}
