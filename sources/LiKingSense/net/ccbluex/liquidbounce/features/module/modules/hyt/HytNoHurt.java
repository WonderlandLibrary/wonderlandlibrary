/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HytNoHurt", description="How dare are you", category=ModuleCategory.HYT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0016J\u0010\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\u0012\u0010\u0011\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0012H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/hyt/HytNoHurt;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "debug", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "mode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "tag", "", "getTag", "()Ljava/lang/String;", "clearAll", "", "onDisable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "LiKingSense"})
public final class HytNoHurt
extends Module {
    public final BoolValue debug = new BoolValue("Debug", true);
    public final ListValue mode = new ListValue("GetNameMode", new String[]{"4V4/1V1", "32/64", "16V16"}, "4V4/1V1");

    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }

    @Override
    @NotNull
    public String getTag() {
        return "Normal";
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        block18: {
            Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
            packet = event.getPacket();
            if (!MinecraftInstance.classProvider.isSPacketChat(packet)) break block18;
            v0 = var3_3 = (String)this.mode.get();
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v1 = v0.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull((Object)v1, (String)"(this as java.lang.String).toLowerCase()");
            var3_3 = v1;
            switch (var3_3.hashCode()) {
                case 48636014: {
                    if (!var3_3.equals("32/64")) ** break;
                    ** GOTO lbl19
                }
                case 46976214: {
                    if (!var3_3.equals("16v16")) ** break;
                    break;
                }
                case -1961702257: {
                    if (!var3_3.equals("4v4/1v1")) ** break;
lbl19:
                    // 2 sources

                    matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher(packet.asSPacketChat().getChatComponent().func_150260_c());
                    matcher2 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89>> (.*?) (\\((((.*?)\u6b7b\u4e86!)))").matcher(packet.asSPacketChat().getChatComponent().func_150260_c());
                    if (matcher.find()) {
                        v2 = matcher.group(1);
                        Intrinsics.checkExpressionValueIsNotNull((Object)v2, (String)"matcher.group(1)");
                        v3 = var7_8 = v2;
                        if (v3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                        }
                        name = StringsKt.trim((CharSequence)v3).toString();
                        if ((Intrinsics.areEqual((Object)name, (Object)"") ^ 1) != 0) {
                            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
                            if (((Boolean)this.debug.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lLiKingSense\u00a78]\u00a7c\u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a" + name);
                            }
                            new Thread(new Runnable(this, name){
                                public final /* synthetic */ HytNoHurt this$0;
                                public final /* synthetic */ String $name;

                                public final void run() {
                                    try {
                                        Thread.sleep(196L - 230L + 72L + 4962L);
                                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                        if (((Boolean)HytNoHurt.access$getDebug$p(this.this$0).get()).booleanValue()) {
                                            ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lLiKingSense\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a" + this.$name);
                                        }
                                    }
                                    catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                {
                                    this.this$0 = hytNoHurt;
                                    this.$name = string;
                                }
                            }).start();
                        }
                    }
                    if (!matcher2.find()) ** break;
                    v4 = matcher2.group(1);
                    Intrinsics.checkExpressionValueIsNotNull((Object)v4, (String)"matcher2.group(1)");
                    v5 = var7_8 = v4;
                    if (v5 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                    }
                    name = StringsKt.trim((CharSequence)v5).toString();
                    if ((Intrinsics.areEqual((Object)name, (Object)"") ^ 1) == 0) ** break;
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
                    if (((Boolean)this.debug.get()).booleanValue()) {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lLiKingSense\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a" + name);
                    }
                    new Thread(new Runnable(this, name){
                        public final /* synthetic */ HytNoHurt this$0;
                        public final /* synthetic */ String $name;

                        public final void run() {
                            try {
                                Thread.sleep(196L - 312L + 206L - 9L + 4919L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                if (((Boolean)HytNoHurt.access$getDebug$p(this.this$0).get()).booleanValue()) {
                                    ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lLiKingSense\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a" + this.$name);
                                }
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        {
                            this.this$0 = hytNoHurt;
                            this.$name = string;
                        }
                    }).start();
                    ** break;
                }
            }
            matcher = Pattern.compile("\u51fb\u8d25\u4e86 (.*?)!").matcher(packet.asSPacketChat().getChatComponent().func_150260_c());
            matcher2 = Pattern.compile("\u73a9\u5bb6 (.*?)\u6b7b\u4e86\uff01").matcher(packet.asSPacketChat().getChatComponent().func_150260_c());
            if (matcher.find()) {
                v6 = matcher.group(1);
                Intrinsics.checkExpressionValueIsNotNull((Object)v6, (String)"matcher.group(1)");
                v7 = var7_9 = v6;
                if (v7 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                name = StringsKt.trim((CharSequence)v7).toString();
                if ((Intrinsics.areEqual((Object)name, (Object)"") ^ 1) != 0) {
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
                    if (((Boolean)this.debug.get()).booleanValue()) {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lLiKingSense\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a" + name);
                    }
                    new Thread(new Runnable(this, name){
                        public final /* synthetic */ HytNoHurt this$0;
                        public final /* synthetic */ String $name;

                        public final void run() {
                            try {
                                Thread.sleep(70L - 131L + 112L - 87L + 10036L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                if (((Boolean)HytNoHurt.access$getDebug$p(this.this$0).get()).booleanValue()) {
                                    ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lLiKingSense\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a" + this.$name);
                                }
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        {
                            this.this$0 = hytNoHurt;
                            this.$name = string;
                        }
                    }).start();
                }
            }
            if (!matcher2.find()) ** break;
            v8 = matcher2.group(1);
            Intrinsics.checkExpressionValueIsNotNull((Object)v8, (String)"matcher2.group(1)");
            v9 = var7_9 = v8;
            if (v9 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            name = StringsKt.trim((CharSequence)v9).toString();
            if ((Intrinsics.areEqual((Object)name, (Object)"") ^ 1) == 0) ** break;
            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
            if (((Boolean)this.debug.get()).booleanValue()) {
                ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lLiKingSense\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a" + name);
            }
            new Thread(new Runnable(this, name){
                public final /* synthetic */ HytNoHurt this$0;
                public final /* synthetic */ String $name;

                public final void run() {
                    try {
                        Thread.sleep(268L - 433L + 63L - 3L + 10105L);
                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                        if (((Boolean)HytNoHurt.access$getDebug$p(this.this$0).get()).booleanValue()) {
                            ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lLiKingSense\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a" + this.$name);
                        }
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                {
                    this.this$0 = hytNoHurt;
                    this.$name = string;
                }
            }).start();
            ** break;
        }
    }

    @EventTarget
    public final void onWorld(@Nullable WorldEvent event) {
        this.clearAll();
    }

    public final void clearAll() {
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.clearFriends();
    }

    public static final /* synthetic */ BoolValue access$getDebug$p(HytNoHurt $this) {
        return $this.debug;
    }
}

