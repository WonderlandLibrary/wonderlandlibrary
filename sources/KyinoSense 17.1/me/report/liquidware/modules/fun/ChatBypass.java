/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C01PacketChatMessage
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.fun;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="ChatBypass", category=ModuleCategory.FUN, description="idk")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2={"Lme/report/liquidware/modules/fun/ChatBypass;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "chanceValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "maxUnicodeValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "minUnicodeValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class ChatBypass
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Null", "RandomChar"}, "Null");
    private final FloatValue chanceValue = new FloatValue("Chance", 0.2f, 0.0f, 0.5f);
    private final IntegerValue minUnicodeValue = new IntegerValue(this, "MinUnicode", 1000, 0, 100000){
        final /* synthetic */ ChatBypass this$0;

        protected void onChanged(int oldValue, int newValue) {
            if (newValue >= ((Number)ChatBypass.access$getMaxUnicodeValue$p(this.this$0).get()).intValue()) {
                this.set(oldValue);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue maxUnicodeValue = new IntegerValue(this, "MaxUnicode", 20000, 0, 100000){
        final /* synthetic */ ChatBypass this$0;

        protected void onChanged(int oldValue, int newValue) {
            if (newValue <= ((Number)ChatBypass.access$getMinUnicodeValue$p(this.this$0).get()).intValue()) {
                this.set(oldValue);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getPacket() instanceof C01PacketChatMessage) {
            String message;
            Packet<?> packet = event.getPacket();
            String string = message = ((C01PacketChatMessage)packet).field_149440_a;
            Intrinsics.checkExpressionValueIsNotNull(string, "message");
            if (StringsKt.startsWith$default(string, "/", false, 2, null)) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            String string2 = message;
            boolean bl = false;
            char[] cArray = string2.toCharArray();
            Intrinsics.checkExpressionValueIsNotNull(cArray, "(this as java.lang.String).toCharArray()");
            block4: for (char c : cArray) {
                String string3 = (String)this.modeValue.get();
                boolean bl2 = false;
                String string4 = string3;
                if (string4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string5 = string4.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(string5, "(this as java.lang.String).toLowerCase()");
                string3 = string5;
                switch (string3.hashCode()) {
                    case 116088473: {
                        if (!string3.equals("randomchar")) continue block4;
                        break;
                    }
                    case 3392903: {
                        if (!string3.equals("null")) continue block4;
                        sb.append(c);
                        if (!(Math.random() < ((Number)this.chanceValue.get()).doubleValue())) continue block4;
                        sb.append("\uf8ff");
                        continue block4;
                    }
                }
                sb.append(c);
                if (!(Math.random() < ((Number)this.chanceValue.get()).doubleValue())) continue;
                sb.append((char)RandomUtils.nextInt(((Number)this.minUnicodeValue.get()).intValue(), ((Number)this.maxUnicodeValue.get()).intValue()));
            }
            ((C01PacketChatMessage)packet).field_149440_a = sb.toString();
            if (((C01PacketChatMessage)packet).field_149440_a.length() > 100) {
                String string6;
                C01PacketChatMessage c01PacketChatMessage = (C01PacketChatMessage)packet;
                String string7 = ((C01PacketChatMessage)packet).field_149440_a;
                Intrinsics.checkExpressionValueIsNotNull(string7, "packet.message");
                String string8 = string7;
                int n = 0;
                int n2 = 100;
                C01PacketChatMessage c01PacketChatMessage2 = c01PacketChatMessage;
                int n3 = 0;
                String string9 = string8;
                if (string9 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string10 = string9.substring(n, n2);
                Intrinsics.checkExpressionValueIsNotNull(string10, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                c01PacketChatMessage2.field_149440_a = string6 = string10;
            }
        }
    }

    public static final /* synthetic */ IntegerValue access$getMaxUnicodeValue$p(ChatBypass $this) {
        return $this.maxUnicodeValue;
    }

    public static final /* synthetic */ IntegerValue access$getMinUnicodeValue$p(ChatBypass $this) {
        return $this.minUnicodeValue;
    }
}

