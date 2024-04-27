package net.ccbluex.liquidbounce.features.module.modules.misc;

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
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HytGetName", description="fix", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\b0HJ\b0HJ\b02\t0\nHJ02\b\t0\fHR0X¢\n\u0000¨\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/HytGetName;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "mode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "clearAll", "", "onDisable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "Pride"})
public final class HytGetName
extends Module {
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
    public final void onPacket(@NotNull PacketEvent event) {
        block14: {
            Intrinsics.checkParameterIsNotNull(event, "event");
            packet = event.getPacket();
            if (!MinecraftInstance.classProvider.isSPacketChat(packet)) break block14;
            var3_3 = (String)this.mode.get();
            var4_4 = false;
            v0 = var3_3;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v1 = v0.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toLowerCase()");
            var3_3 = v1;
            switch (var3_3.hashCode()) {
                case 48636014: {
                    if (!var3_3.equals("32/64")) ** break;
                    ** GOTO lbl21
                }
                case 46976214: {
                    if (!var3_3.equals("16v16")) ** break;
                    break;
                }
                case -1961702257: {
                    if (!var3_3.equals("4v4/1v1")) ** break;
lbl21:
                    // 2 sources

                    matcher = Pattern.compile("杀死了 (.*?)\\(").matcher(packet.asSPacketChat().getChatComponent().getUnformattedText());
                    matcher2 = Pattern.compile("起床战争>> (.*?) (\\((((.*?)死了!)))").matcher(packet.asSPacketChat().getChatComponent().getUnformattedText());
                    if (matcher.find()) {
                        v2 = matcher.group(1);
                        Intrinsics.checkExpressionValueIsNotNull(v2, "matcher.group(1)");
                        var7_9 = v2;
                        var8_11 = false;
                        v3 = var7_9;
                        if (v3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                        }
                        name = StringsKt.trim((CharSequence)v3).toString();
                        if (Intrinsics.areEqual(name, "") ^ true) {
                            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
                            ClientUtils.displayChatMessage("§8[§c§lRedStar提醒您§8]§c§d添加无敌人：" + name);
                            new Thread(new Runnable(name){
                                final String $name;

                                public final void run() {
                                    try {
                                        Thread.sleep(5000L);
                                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                        ClientUtils.displayChatMessage("§8[§c§lRedStar提醒您§8]§c§d删除无敌人：" + this.$name);
                                    }
                                    catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                {
                                    this.$name = string;
                                }
                            }).start();
                        }
                    }
                    if (!matcher2.find()) ** break;
                    v4 = matcher2.group(1);
                    Intrinsics.checkExpressionValueIsNotNull(v4, "matcher2.group(1)");
                    var7_9 = v4;
                    var8_11 = false;
                    v5 = var7_9;
                    if (v5 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                    }
                    name = StringsKt.trim((CharSequence)v5).toString();
                    if (!(Intrinsics.areEqual(name, "") ^ true)) ** break;
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
                    ClientUtils.displayChatMessage("§8[§c§lRedStar提醒您§8]§c§d添加无敌人：" + name);
                    new Thread(new Runnable(name){
                        final String $name;

                        public final void run() {
                            try {
                                Thread.sleep(5000L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                ClientUtils.displayChatMessage("§8[§c§lRedStar提醒您§8]§c§d删除无敌人：" + this.$name);
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        {
                            this.$name = string;
                        }
                    }).start();
                    ** break;
                }
            }
            matcher = Pattern.compile("击败了 (.*?)!").matcher(packet.asSPacketChat().getChatComponent().getUnformattedText());
            matcher2 = Pattern.compile("玩家 (.*?)死了！").matcher(packet.asSPacketChat().getChatComponent().getUnformattedText());
            if (matcher.find()) {
                v6 = matcher.group(1);
                Intrinsics.checkExpressionValueIsNotNull(v6, "matcher.group(1)");
                var7_10 = v6;
                var8_12 = false;
                v7 = var7_10;
                if (v7 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                name = StringsKt.trim((CharSequence)v7).toString();
                if (Intrinsics.areEqual(name, "") ^ true) {
                    LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
                    ClientUtils.displayChatMessage("§8[§c§lRedStar提醒您§8]§c§d添加无敌人：" + name);
                    new Thread(new Runnable(name){
                        final String $name;

                        public final void run() {
                            try {
                                Thread.sleep(10000L);
                                LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                                ClientUtils.displayChatMessage("§8[§c§lRedStar提醒您§8]§c§d删除无敌人：" + this.$name);
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        {
                            this.$name = string;
                        }
                    }).start();
                }
            }
            if (!matcher2.find()) ** break;
            v8 = matcher2.group(1);
            Intrinsics.checkExpressionValueIsNotNull(v8, "matcher2.group(1)");
            var7_10 = v8;
            var8_12 = false;
            v9 = var7_10;
            if (v9 == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
            name = StringsKt.trim((CharSequence)v9).toString();
            if (!(Intrinsics.areEqual(name, "") ^ true)) ** break;
            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(name);
            ClientUtils.displayChatMessage("§8[§c§lRedStar提醒您§8]§c§d添加无敌人：" + name);
            new Thread(new Runnable(name){
                final String $name;

                public final void run() {
                    try {
                        Thread.sleep(10000L);
                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                        ClientUtils.displayChatMessage("§8[§c§lRedStar提醒您§8]§c§d删除无敌人：" + this.$name);
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                {
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

    private final void clearAll() {
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.clearFriends();
    }
}
