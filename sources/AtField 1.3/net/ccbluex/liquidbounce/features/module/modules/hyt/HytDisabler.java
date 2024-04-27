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
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import liying.utils.packet.PacketUtils;
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
    private final ArrayList transactions;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"HytSpartan", "HytRange", "HytAAC"}, "HytSpartan");
    private final BoolValue debugValue;
    private final MSTimer msTimer;
    private final ArrayList keepAlives;
    private final BoolValue HytAAC = new BoolValue("HytAAC", false);

    public static final MSTimer access$getMsTimer$p(HytDisabler hytDisabler) {
        return hytDisabler.msTimer;
    }

    public static final ArrayList access$getTransactions$p(HytDisabler hytDisabler) {
        return hytDisabler.transactions;
    }

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public final void debug(String string) {
        if (((Boolean)this.debugValue.get()).booleanValue()) {
            ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a7lDisabler\u00a77]\u00a7f " + string);
        }
    }

    public HytDisabler() {
        ArrayList arrayList;
        this.debugValue = new BoolValue("Debug", false);
        HytDisabler hytDisabler = this;
        boolean bl = false;
        hytDisabler.keepAlives = arrayList = new ArrayList();
        hytDisabler = this;
        bl = false;
        hytDisabler.transactions = arrayList = new ArrayList();
        this.msTimer = new MSTimer();
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @EventTarget
    public final void onPacket(PacketEvent var1_1) {
        block10: {
            block9: {
                var2_2 = var1_1.getPacket();
                var3_3 /* !! */  = (String)this.modeValue.get();
                var4_4 = false;
                v0 = var3_3 /* !! */ ;
                if (v0 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                var3_3 /* !! */  = v0.toLowerCase();
                switch (var3_3 /* !! */ .hashCode()) {
                    case 46797210: {
                        if (!var3_3 /* !! */ .equals("HytRange")) ** break;
                        break;
                    }
                    case -1202237472: {
                        if (!var3_3 /* !! */ .equals("hytaac")) ** break;
                        break block9;
                    }
                    case -330391888: {
                        if (!var3_3 /* !! */ .equals("hytspartan")) ** break;
                        if (var2_2 instanceof CPacketKeepAlive && (this.keepAlives.size() <= 0 || var2_2.equals((CPacketKeepAlive)this.keepAlives.get(this.keepAlives.size() - 1)) ^ true)) {
                            this.debug("c00 added");
                            this.keepAlives.add(var2_2);
                            var1_1.cancelEvent();
                        }
                        if (!(var2_2 instanceof CPacketConfirmTransaction) || this.transactions.size() > 0 && !(var2_2.equals((CPacketConfirmTransaction)this.transactions.get(this.transactions.size() - 1)) ^ true)) ** break;
                        this.debug("c0f added");
                        this.transactions.add(var2_2);
                        var1_1.cancelEvent();
                        ** break;
                    }
                }
                v1 = MinecraftInstance.mc.getThePlayer();
                if (v1 == null) {
                    Intrinsics.throwNpe();
                }
                if (v1.getTicksExisted() % 4 == 0 && MinecraftInstance.classProvider.isCPacketPlayer(var2_2)) {
                    var1_1.cancelEvent();
                    ** break;
                }
                break block10;
            }
            if (!((Boolean)this.HytAAC.get()).booleanValue()) ** break;
            var1_1.getPacket() instanceof SPacketPlayerPosLook;
            var4_5 = var1_1.getPacket();
            this.debug("[Disabler] \u5c4f\u853dS\u7c7b\u578b\u53d1\u5305\u6570\u636e");
            ** break;
        }
        var3_3 /* !! */  = new Function1(this){
            final HytDisabler this$0;

            @EventTarget
            public final void invoke(UpdateEvent updateEvent) {
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
                        break;
                    }
                }
            }

            public Object invoke(Object object) {
                this.invoke((UpdateEvent)object);
                return Unit.INSTANCE;
            }

            static {
            }
            {
                this.this$0 = hytDisabler;
                super(1);
            }
        };
    }

    public static final ArrayList access$getKeepAlives$p(HytDisabler hytDisabler) {
        return hytDisabler.keepAlives;
    }
}

