/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.ArrayList;
import jx.utils.packet.PacketUtils;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@ModuleInfo(name="HytDisabler", description="\u4fee\u590d\u7248", category=ModuleCategory.HYT)
public final class HytDisabler
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"HytSpartan", "HytRange", "HytAAC"}, "HytSpartan");
    private final BoolValue HytAAC = new BoolValue("HytAAC", false);
    private final BoolValue debugValue = new BoolValue("Debug", false);
    private final ArrayList<CPacketKeepAlive> keepAlives;
    private final ArrayList<CPacketConfirmTransaction> transactions;
    private final MSTimer msTimer;

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public final void debug(String s) {
        if (((Boolean)this.debugValue.get()).booleanValue()) {
            ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lDisabler\u00a77]\u00a7f " + s);
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onPacket(PacketEvent event) {
        block10: {
            block9: {
                packet = event.getPacket();
                var3_3 = (String)this.modeValue.get();
                var4_4 = false;
                v0 = var3_3;
                if (v0 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var3_3 = v0.toLowerCase();
                switch (var3_3.hashCode()) {
                    case 46797210: {
                        if (!var3_3.equals("HytRange")) ** break;
                        break;
                    }
                    case -1202237472: {
                        if (!var3_3.equals("hytaac")) ** break;
                        break block9;
                    }
                    case -330391888: {
                        if (!var3_3.equals("hytspartan")) ** break;
                        if (packet instanceof CPacketKeepAlive && (this.keepAlives.size() <= 0 || packet.equals(this.keepAlives.get(this.keepAlives.size() - 1)) ^ true)) {
                            this.debug("c00 added");
                            this.keepAlives.add((CPacketKeepAlive)packet);
                            event.cancelEvent();
                        }
                        if (!(packet instanceof CPacketConfirmTransaction) || this.transactions.size() > 0 && !(packet.equals(this.transactions.get(this.transactions.size() - 1)) ^ true)) ** break;
                        this.debug("c0f added");
                        this.transactions.add((CPacketConfirmTransaction)packet);
                        event.cancelEvent();
                        ** break;
                    }
                }
                v1 = MinecraftInstance.mc.getThePlayer();
                if (v1 == null) {
                    Intrinsics.throwNpe();
                }
                if (v1.getTicksExisted() % 4 == 0 && MinecraftInstance.classProvider.isCPacketPlayer(packet)) {
                    event.cancelEvent();
                    ** break;
                }
                break block10;
            }
            if (!((Boolean)this.HytAAC.get()).booleanValue()) ** break;
            event.getPacket() instanceof SPacketPlayerPosLook;
            s08 = event.getPacket();
            this.debug("[Disabler] \u5c4f\u853dS\u7c7b\u578b\u53d1\u5305\u6570\u636e");
        }
        $fun$onUpdate$1 = new Function1<UpdateEvent, Unit>(this){
            final /* synthetic */ HytDisabler this$0;

            @EventTarget
            public final void invoke(UpdateEvent event) {
                String string = (String)this.this$0.getModeValue().get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string = string2.toLowerCase();
                switch (string.hashCode()) {
                    case -330391888: {
                        if (!string.equals("hytspartan") || !HytDisabler.access$getMsTimer$p(this.this$0).hasTimePassed(3000L) || HytDisabler.access$getKeepAlives$p(this.this$0).size() <= 0 || HytDisabler.access$getTransactions$p(this.this$0).size() <= 0) break;
                        PacketUtils.send((CPacketKeepAlive)HytDisabler.access$getKeepAlives$p(this.this$0).get(HytDisabler.access$getKeepAlives$p(this.this$0).size() - 1));
                        PacketUtils.send((CPacketConfirmTransaction)HytDisabler.access$getTransactions$p(this.this$0).get(HytDisabler.access$getTransactions$p(this.this$0).size() - 1));
                        this.this$0.debug("c00 no." + (HytDisabler.access$getKeepAlives$p(this.this$0).size() - 1) + " sent.");
                        this.this$0.debug("c0f no." + (HytDisabler.access$getTransactions$p(this.this$0).size() - 1) + " sent.");
                        HytDisabler.access$getKeepAlives$p(this.this$0).clear();
                        HytDisabler.access$getTransactions$p(this.this$0).clear();
                        HytDisabler.access$getMsTimer$p(this.this$0).reset();
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

    public static final /* synthetic */ ArrayList access$getKeepAlives$p(HytDisabler $this) {
        return $this.keepAlives;
    }

    public static final /* synthetic */ ArrayList access$getTransactions$p(HytDisabler $this) {
        return $this.transactions;
    }

    public static final /* synthetic */ MSTimer access$getMsTimer$p(HytDisabler $this) {
        return $this.msTimer;
    }
}

