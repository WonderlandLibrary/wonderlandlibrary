/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import java.util.regex.Pattern;
import kotlin.TypeCastException;
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
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HytNoHurt", description="How dare are you", category=ModuleCategory.HYT)
public final class HytNoHurt
extends Module {
    private final BoolValue debug = new BoolValue("Debug", true);
    private final ListValue mode = new ListValue("GetNameMode", new String[]{"4V4/1V1", "32/64", "16V16"}, "4V4/1V1");

    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onPacket(PacketEvent var1_1) {
        block18: {
            var2_2 = var1_1.getPacket();
            if (!MinecraftInstance.classProvider.isSPacketChat(var2_2)) break block18;
            var3_3 = (String)this.mode.get();
            var4_4 = false;
            v0 = var3_3;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            var3_3 = v0.toLowerCase();
            switch (var3_3.hashCode()) {
                case 48636014: {
                    if (!var3_3.equals("32/64")) ** break;
                    ** GOTO lbl18
                }
                case 46976214: {
                    if (!var3_3.equals("16v16")) ** break;
                    break;
                }
                case -1961702257: {
                    if (!var3_3.equals("4v4/1v1")) ** break;
lbl18:
                    // 2 sources

                    var4_5 = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher(var2_2.asSPacketChat().getChatComponent().func_150260_c());
                    var5_7 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89>> (.*?) (\\((((.*?)\u6b7b\u4e86!)))").matcher(var2_2.asSPacketChat().getChatComponent().func_150260_c());
                    if (var4_5.find()) {
                        var7_9 = var4_5.group(1);
                        var8_11 = false;
                        v1 = var7_9;
                        if (v1 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                        }
                        var6_13 = StringsKt.trim((CharSequence)v1).toString();
                        if (var6_13.equals("") ^ true) {
                            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(var6_13);
                            if (((Boolean)this.debug.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u00a78]\u00a7c\u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a" + var6_13);
                            }
                            new Thread(new Runnable(this, var6_13){
                                final String $name;
                                final HytNoHurt this$0;
                                {
                                    this.this$0 = hytNoHurt;
                                    this.$name = string;
                                }

                                public final void run() {
                                    try {
                                        Thread.sleep(5000L);
                                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                        if (((Boolean)HytNoHurt.access$getDebug$p(this.this$0).get()).booleanValue()) {
                                            ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a" + this.$name);
                                        }
                                    }
                                    catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                }

                                static {
                                }
                            }).start();
                        }
                    }
                    if (!var5_7.find()) ** break;
                    var7_9 = var5_7.group(1);
                    var8_11 = false;
                    v2 = var7_9;
                    if (v2 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                    }
                    var6_13 = StringsKt.trim((CharSequence)v2).toString();
                    if (!(var6_13.equals("") ^ true)) ** break;
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(var6_13);
                    if (((Boolean)this.debug.get()).booleanValue()) {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a" + var6_13);
                    }
                    new Thread(new Runnable(this, var6_13){
                        final HytNoHurt this$0;
                        final String $name;
                        {
                            this.this$0 = hytNoHurt;
                            this.$name = string;
                        }

                        public final void run() {
                            try {
                                Thread.sleep(5000L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                if (((Boolean)HytNoHurt.access$getDebug$p(this.this$0).get()).booleanValue()) {
                                    ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a" + this.$name);
                                }
                            }
                            catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }

                        static {
                        }
                    }).start();
                    ** break;
                }
            }
            var4_6 = Pattern.compile("\u51fb\u8d25\u4e86 (.*?)!").matcher(var2_2.asSPacketChat().getChatComponent().func_150260_c());
            var5_8 = Pattern.compile("\u73a9\u5bb6 (.*?)\u6b7b\u4e86\uff01").matcher(var2_2.asSPacketChat().getChatComponent().func_150260_c());
            if (var4_6.find()) {
                var7_10 = var4_6.group(1);
                var8_12 = false;
                v3 = var7_10;
                if (v3 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                var6_14 = StringsKt.trim((CharSequence)v3).toString();
                if (var6_14.equals("") ^ true) {
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(var6_14);
                    if (((Boolean)this.debug.get()).booleanValue()) {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a" + var6_14);
                    }
                    new Thread(new Runnable(this, var6_14){
                        final HytNoHurt this$0;
                        final String $name;

                        public final void run() {
                            try {
                                Thread.sleep(10000L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                if (((Boolean)HytNoHurt.access$getDebug$p(this.this$0).get()).booleanValue()) {
                                    ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a" + this.$name);
                                }
                            }
                            catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                        {
                            this.this$0 = hytNoHurt;
                            this.$name = string;
                        }

                        static {
                        }
                    }).start();
                }
            }
            if (!var5_8.find()) ** break;
            var7_10 = var5_8.group(1);
            var8_12 = false;
            v4 = var7_10;
            if (v4 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            var6_14 = StringsKt.trim((CharSequence)v4).toString();
            if (!(var6_14.equals("") ^ true)) ** break;
            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(var6_14);
            if (((Boolean)this.debug.get()).booleanValue()) {
                ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a" + var6_14);
            }
            new Thread(new Runnable(this, var6_14){
                final HytNoHurt this$0;
                final String $name;
                {
                    this.this$0 = hytNoHurt;
                    this.$name = string;
                }

                static {
                }

                public final void run() {
                    try {
                        Thread.sleep(10000L);
                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                        if (((Boolean)HytNoHurt.access$getDebug$p(this.this$0).get()).booleanValue()) {
                            ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u63d0\u9192\u60a8\u00a78]\u00a7c\u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a" + this.$name);
                        }
                    }
                    catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }).start();
            ** break;
        }
    }

    public static final BoolValue access$getDebug$p(HytNoHurt hytNoHurt) {
        return hytNoHurt.debug;
    }

    @Override
    public String getTag() {
        return "Normal";
    }

    @EventTarget
    public final void onWorld(@Nullable WorldEvent worldEvent) {
        this.clearAll();
    }

    private final void clearAll() {
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.clearFriends();
    }
}

