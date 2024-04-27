/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketChat
 */
package net.ccbluex.liquidbounce.features.module.modules.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;

@ModuleInfo(name="AntiFakePlayer", description="Made and Fixed by RyF with \u2764", category=ModuleCategory.HYT)
public final class AntiFakePlayer
extends Module {
    private final BoolValue isFriendDebuggerChat;
    private final Value showMyKillDeathChatValue;
    private final Value hideKitCoinGetChat;
    private final String[] kitSpecialDeathChats;
    private final Value sTHideAll;
    private final Value sTHideAllAutoSwitcher;
    private final String[] multiKillMessageList;
    private final BoolValue autoSwitchLogger;
    private final BoolValue showHideChat;
    private final BoolValue autoBotGetter;
    private final ListValue botGetterModeValue;
    private final Value sTHideBotsCounterSetting;
    private final Value sTHideLogStyle;
    private final Value hideKitSpecialDeathChat;
    private final Value logStyleValue;
    private int logNumber;
    private final Value sTHideAutoSwitcherState;
    private final BoolValue hideValue;
    private final Value autoSwitchDelay;
    private final MSTimer ms;
    private final Value autoSwitchMode;
    private final Value sTHideAutoSwitcherMode;
    private final BoolValue showMyMultiKillChat;
    private final String[] kitSpecialSkillChats;
    private int bots;
    private final Value hideKitDeathStreakChat;
    private final BoolValue protectDeadPlayerName;
    private final Value sTHideAutoSwitcherStateSetting;
    private final BoolValue hideKillChatValue;
    private String protectedname = "";
    private final Value sTHideBotsCounter;
    private final BoolValue hideMultiKillChat;
    private final Value sTHideAutoSwitcherDelay;
    private final Value hideKitUpgradeChat;
    private final BoolValue sTShowOptions;
    private final BoolValue printLogger;
    private final String[] logStyles = new String[]{"RyFNew", "FDPAntibot", "FDPChat", "Leave", "Special", "Special2", "Shitty", "WaWa1", "WaWa2", "NullClient", "Normal", "WindX", "Old", "Old1", "Arene"};
    private final Value hideKitSkillChat;
    private final IntegerValue kitCustomDelay;
    private final Value sTHideMode;

    public static final ListValue access$getBotGetterModeValue$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.botGetterModeValue;
    }

    public static final Value access$getSTHideBotsCounter$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.sTHideBotsCounter;
    }

    public static final void access$setBots$p(AntiFakePlayer antiFakePlayer, int n) {
        antiFakePlayer.bots = n;
    }

    public static final BoolValue access$getSTShowOptions$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.sTShowOptions;
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        Packet packet;
        Object object;
        block50: {
            block51: {
                block52: {
                    int n;
                    object = packetEvent.getPacket();
                    boolean bl = false;
                    packet = ((PacketImpl)object).getWrapped();
                    if (packet instanceof SPacketChat && !((SPacketChat)packet).func_148915_c().func_150260_c().equals(":") && (StringsKt.startsWith$default((String)((SPacketChat)packet).func_148915_c().func_150260_c(), (String)"\u8d77\u5e8a\u6218\u4e89", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)((SPacketChat)packet).func_148915_c().func_150260_c(), (String)"[\u8d77\u5e8a\u6218\u4e89", (boolean)false, (int)2, null) || StringsKt.startsWith$default((String)((SPacketChat)packet).func_148915_c().func_150260_c(), (String)"\u82b1\u96e8\u5ead", (boolean)false, (int)2, null))) {
                        object = ((SPacketChat)packet).func_148915_c().func_150260_c();
                        String string = (String)this.botGetterModeValue.get();
                        n = 0;
                        String string2 = string;
                        if (string2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        switch (string2.toLowerCase()) {
                            case "4v4/2v2/1v1": {
                                String string3;
                                boolean bl2;
                                String string4;
                                Matcher matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher((CharSequence)object);
                                Object object2 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89>> (.*?) (\\(((.*?)\u6b7b\u4e86!))").matcher((CharSequence)object);
                                if (matcher.find()) {
                                    string4 = matcher.group(1);
                                    bl2 = false;
                                    String string5 = string4;
                                    if (string5 == null) {
                                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                                    }
                                    string3 = ((Object)StringsKt.trim((CharSequence)string5)).toString();
                                    this.playerDeathAction(string3, 4988L);
                                    this.playerDeathMsgAction(packetEvent);
                                }
                                if (!((Matcher)object2).find()) break;
                                string4 = ((Matcher)object2).group(1);
                                bl2 = false;
                                String string6 = string4;
                                if (string6 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                                }
                                string3 = ((Object)StringsKt.trim((CharSequence)string6)).toString();
                                this.playerDeathAction(string3, 4988L);
                                this.playerDeathMsgAction(packetEvent);
                                break;
                            }
                            case "bwxp32": {
                                String string3;
                                boolean bl2;
                                String string4;
                                Matcher matcher = Pattern.compile("\u6740\u6b7b\u4e86 (.*?)\\(").matcher((CharSequence)object);
                                Object object2 = Pattern.compile("\u8d77\u5e8a\u6218\u4e89 >> (.*?) (\\(((.*?)\u6b7b\u4e86!))").matcher((CharSequence)object);
                                if (matcher.find()) {
                                    string4 = matcher.group(1);
                                    bl2 = false;
                                    String string7 = string4;
                                    if (string7 == null) {
                                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                                    }
                                    string3 = ((Object)StringsKt.trim((CharSequence)string7)).toString();
                                    this.playerDeathAction(string3, 7400L);
                                    this.playerDeathMsgAction(packetEvent);
                                }
                                if (!((Matcher)object2).find()) break;
                                string4 = ((Matcher)object2).group(1);
                                bl2 = false;
                                String string8 = string4;
                                if (string8 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                                }
                                string3 = ((Object)StringsKt.trim((CharSequence)string8)).toString();
                                this.playerDeathAction(string3, 4988L);
                                this.playerDeathMsgAction(packetEvent);
                                break;
                            }
                            case "bwxp16": {
                                String string3;
                                boolean bl2;
                                String string4;
                                Matcher matcher = Pattern.compile("\u51fb\u8d25\u4e86 (.*?)!").matcher((CharSequence)object);
                                Object object2 = Pattern.compile("\u73a9\u5bb6 (.*?)\u6b7b\u4e86\uff01").matcher((CharSequence)object);
                                if (matcher.find()) {
                                    string4 = matcher.group(1);
                                    bl2 = false;
                                    String string9 = string4;
                                    if (string9 == null) {
                                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                                    }
                                    string3 = ((Object)StringsKt.trim((CharSequence)string9)).toString();
                                    this.playerDeathAction(string3, 9700L);
                                    this.playerDeathMsgAction(packetEvent);
                                }
                                if (!((Matcher)object2).find()) break;
                                string4 = ((Matcher)object2).group(1);
                                bl2 = false;
                                String string10 = string4;
                                if (string10 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                                }
                                string3 = ((Object)StringsKt.trim((CharSequence)string10)).toString();
                                this.playerDeathAction(string3, 9700L);
                                this.playerDeathMsgAction(packetEvent);
                                break;
                            }
                            case "kitbattle": {
                                String string3;
                                boolean bl2;
                                String string4;
                                Matcher matcher = Pattern.compile("\u51fb\u6740\u4e86(.*?) !").matcher((CharSequence)object);
                                Object object2 = Pattern.compile("\u82b1\u96e8\u5ead >>(.*?) \u88ab").matcher((CharSequence)object);
                                if (matcher.find()) {
                                    string4 = matcher.group(1);
                                    bl2 = false;
                                    String string11 = string4;
                                    if (string11 == null) {
                                        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                                    }
                                    string3 = ((Object)StringsKt.trim((CharSequence)string11)).toString();
                                    this.playerDeathAction(string3, ((Number)this.kitCustomDelay.get()).intValue());
                                    this.playerDeathMsgAction(packetEvent);
                                }
                                if (!((Matcher)object2).find()) break;
                                string4 = ((Matcher)object2).group(1);
                                bl2 = false;
                                String string12 = string4;
                                if (string12 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
                                }
                                string3 = ((Object)StringsKt.trim((CharSequence)string12)).toString();
                                this.playerDeathAction(string3, ((Number)this.kitCustomDelay.get()).intValue());
                                this.playerDeathMsgAction(packetEvent);
                                break;
                            }
                        }
                    }
                    if (packet instanceof SPacketChat && ((SPacketChat)packet).func_148915_c().func_150260_c().equals(":")) {
                        String string = ((SPacketChat)packet).func_148915_c().func_150260_c();
                        StringBuilder stringBuilder = new StringBuilder();
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!string.equals(stringBuilder.append(iEntityPlayerSP.getDisplayNameString()).append(":").toString()) && ((SPacketChat)packet).func_148915_c().func_150260_c().equals("\u8d77\u5e8a\u6218\u4e89") && !((SPacketChat)packet).func_148915_c().func_150260_c().equals("01:00:00 \u662f\u8fd9\u4e2a\u5730\u56fe\u7684\u8bb0\u5f55!") && !((SPacketChat)packet).func_148915_c().func_150260_c().equals("\u4e4b\u961f\u961f\u8bbe\u7f6e\u4e00\u4e2a\u65b0\u7684\u8bb0\u5f55:") && ((Boolean)this.hideValue.get()).booleanValue()) {
                            packetEvent.cancelEvent();
                            if (((Boolean)this.showHideChat.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("\u00a7bAtField \u00a77\u00bb \u00a7c\u9690\u85cf\u4e86AntiGetname\u6d88\u606f\u3002");
                            }
                        }
                    }
                    if (packet instanceof SPacketChat) {
                        object = ((SPacketChat)packet).func_148915_c().func_150260_c();
                        for (String object3 : this.multiKillMessageList) {
                            if (!((String)object).equals(object3) && !((String)object).equals(object3)) continue;
                            if (((Boolean)this.showMyMultiKillChat.get()).booleanValue()) {
                                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                                if (iEntityPlayerSP == null) {
                                    Intrinsics.throwNpe();
                                }
                                if (((String)object).equals(iEntityPlayerSP.getDisplayNameString())) continue;
                            }
                            if (!((Boolean)this.hideMultiKillChat.get()).booleanValue()) continue;
                            packetEvent.cancelEvent();
                        }
                    }
                    if (!(packet instanceof SPacketChat)) break block50;
                    object = ((SPacketChat)packet).func_148915_c().func_150260_c();
                    String string = (String)this.botGetterModeValue.get();
                    n = 0;
                    String string13 = string;
                    if (string13 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    if (!string13.toLowerCase().equals("kitbattle") || !StringsKt.startsWith$default((String)object, (String)"\u82b1\u96e8\u5ead", (boolean)false, (int)2, null)) break block50;
                    if (((Boolean)this.hideKitCoinGetChat.get()).booleanValue() && (((String)object).equals("\u82b1\u96e8\u5ead >>\u4f60\u6d88\u706d") && ((String)object).equals("% \u7684\u4f24\u5bb3\u5e76\u4e14\u83b7\u5f97\u4e86") && ((String)object).equals("\u786c\u5e01!") || ((String)object).equals("\u4f60\u7684 coins \u88ab\u4fee\u6b63\u4e3a"))) {
                        packetEvent.cancelEvent();
                    }
                    if (!((String)object).equals("\u82b1\u96e8\u5ead >>") || !((String)object).equals("\u5b8c\u6210\u4e86") || !((String)object).equals("\u8fde\u6740!")) break block51;
                    if (!((Boolean)this.showMyMultiKillChat.get()).booleanValue()) break block52;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (((String)object).equals(iEntityPlayerSP.getDisplayNameString())) break block51;
                }
                if (((Boolean)this.hideMultiKillChat.get()).booleanValue()) {
                    packetEvent.cancelEvent();
                }
            }
            if (((String)object).equals("\u82b1\u96e8\u5ead >>") && (((String)object).equals("has ended his deathstreak and lost his buff") || ((String)object).equals("is now receiving a buff for his deathstreak") || ((String)object).equals("\u7ec8\u7ed3\u4e86\u4ed6\u7684\u8fde\u7eed\u6b7b\u4ea1") || ((String)object).equals("\u83b7\u5f97\u4e86\u4e00\u4e2abuff\u56e0\u4e3a\u4ed6\u521a\u521a\u5b8c\u6210\u4e86")) && ((Boolean)this.hideKitDeathStreakChat.get()).booleanValue()) {
                packetEvent.cancelEvent();
            }
            for (String string : this.kitSpecialDeathChats) {
                if (!((String)object).equals(string) && !((String)object).equals(string) || !((Boolean)this.hideKitSpecialDeathChat.get()).booleanValue()) continue;
                packetEvent.cancelEvent();
            }
            for (String string : this.kitSpecialSkillChats) {
                if (!((String)object).equals(string) && !((String)object).equals(string) || !((Boolean)this.hideKitSkillChat.get()).booleanValue()) continue;
                packetEvent.cancelEvent();
            }
            if (((Boolean)this.hideKitUpgradeChat.get()).booleanValue() && ((String)object).equals("\u901a\u8fc7\u51fb\u6740\u83b7\u5f97\u80dc\u70b9\u7684\u65b9\u5f0f\u664b\u7ea7\u4e3a")) {
                packetEvent.cancelEvent();
            }
        }
        if (packet instanceof SPacketChat && ((Boolean)this.autoBotGetter.get()).booleanValue()) {
            object = ((SPacketChat)packet).func_148915_c().func_150254_d();
            if (((String)object).equals("\u00a7b\u82b1\u96e8\u5ead \u00a77>>")) {
                this.botGetterModeValue.set("KitBattle");
            }
            if (((String)object).equals("\u00a7b\u8d77\u5e8a\u6218\u4e89\u00a77>>")) {
                this.botGetterModeValue.set("4v4/2v2/1v1");
            }
            if (((String)object).equals("\u00a7b\u8d77\u5e8a\u6218\u4e89 \u00a7f>>")) {
                this.botGetterModeValue.set("BWXP32");
            }
            if (((String)object).equals("\u00a7f[\u8d77\u5e8a\u6218\u4e89]")) {
                this.botGetterModeValue.set("BWXP16");
            }
        }
    }

    @Override
    public String getTag() {
        return (Boolean)this.sTHideAll.get() == false ? this.tagReturner() : null;
    }

    public static final String access$getProtectedname$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.protectedname;
    }

    public static final Value access$getSTHideAllAutoSwitcher$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.sTHideAllAutoSwitcher;
    }

    @Override
    @EventTarget
    public void onDisable() {
        this.bots = 0;
        this.clearAll();
        super.onDisable();
    }

    private final void playerDeathMsgAction(PacketEvent packetEvent) {
        block4: {
            block5: {
                IPacket iPacket = packetEvent.getPacket();
                boolean bl = false;
                Packet packet = ((PacketImpl)iPacket).getWrapped();
                if (!(packet instanceof SPacketChat) || !((Boolean)this.hideKillChatValue.get()).booleanValue()) break block4;
                if (!((Boolean)this.showMyKillDeathChatValue.get()).booleanValue()) break block5;
                String string = ((SPacketChat)packet).func_148915_c().func_150260_c();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                if (string.equals(iEntityPlayerSP.getDisplayNameString())) break block4;
            }
            packetEvent.cancelEvent();
        }
    }

    public static final BoolValue access$getHideKillChatValue$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.hideKillChatValue;
    }

    private final void playerDeathAction(String string, long l) {
        int n = this.bots;
        this.bots = n + 1;
        if (!LiquidBounce.INSTANCE.getFileManager().friendsConfig.isFriend(string)) {
            if (((Boolean)this.isFriendDebuggerChat.get()).booleanValue()) {
                ClientUtils.displayChatMessage("\u00a7f[\u00a7c!\u00a7f] \u00a77[\u00a7bAtField\u00a77] \u00a7f\u5224\u5b9a \u00a77" + string + " \u00a7f\u662f\u5426\u5728\u597d\u53cb\u5217\u8868\u3002");
            }
            LiquidBounce.INSTANCE.getFileManager().friendsConfig.addFriend(string);
        }
        String string2 = this.protectedname = (Boolean)this.protectDeadPlayerName.get() != false ? "\u00a77\u00a7k" + string : "\u00a77" + string;
        if (((Boolean)this.printLogger.get()).booleanValue()) {
            this.printLogger(this.protectedname, "add");
        }
        new Thread(new Runnable(this, l, string){
            final AntiFakePlayer this$0;
            final String $name;
            final long $cd;

            static {
            }

            public final void run() {
                try {
                    Thread.sleep(this.$cd);
                    AntiFakePlayer.access$setProtectedname$p(this.this$0, (Boolean)AntiFakePlayer.access$getProtectDeadPlayerName$p(this.this$0).get() != false ? "\u00a77\u00a7k" + this.$name : "\u00a77" + this.$name);
                    if (LiquidBounce.INSTANCE.getFileManager().friendsConfig.isFriend(this.$name)) {
                        if (((Boolean)AntiFakePlayer.access$isFriendDebuggerChat$p(this.this$0).get()).booleanValue()) {
                            ClientUtils.displayChatMessage("\u00a7f[\u00a7c!\u00a7f] \u00a77[\u00a7bAtField\u00a77] \u00a7f\u5224\u5b9a \u00a77" + this.$name + " \u00a7f\u662f\u5426\u5728\u597d\u53cb\u5217\u8868\u3002");
                        }
                        LiquidBounce.INSTANCE.getFileManager().friendsConfig.removeFriend(this.$name);
                    }
                    AntiFakePlayer antiFakePlayer = this.this$0;
                    int n = AntiFakePlayer.access$getBots$p(antiFakePlayer);
                    AntiFakePlayer.access$setBots$p(antiFakePlayer, n + -1);
                    if (((Boolean)AntiFakePlayer.access$getPrintLogger$p(this.this$0).get()).booleanValue()) {
                        AntiFakePlayer.access$printLogger(this.this$0, AntiFakePlayer.access$getProtectedname$p(this.this$0), "remove");
                    }
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            {
                this.this$0 = antiFakePlayer;
                this.$cd = l;
                this.$name = string;
            }
        }).start();
    }

    public static final BoolValue access$getAutoSwitchLogger$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.autoSwitchLogger;
    }

    public static final BoolValue access$getProtectDeadPlayerName$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.protectDeadPlayerName;
    }

    public static final BoolValue access$getPrintLogger$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.printLogger;
    }

    private final String tagReturner() {
        String string = "";
        if (((Boolean)this.sTHideAll.get()).booleanValue()) {
            string = "";
        } else {
            boolean bl;
            String string2;
            if (!((Boolean)this.sTHideMode.get()).booleanValue()) {
                string = (String)this.botGetterModeValue.get();
            }
            if (!((Boolean)this.sTHideLogStyle.get()).booleanValue() && ((Boolean)this.printLogger.get()).booleanValue()) {
                String string3 = string = string.equals("") ^ true ? string + ", " + (String)this.logStyleValue.get() : (String)this.logStyleValue.get();
            }
            if (!((Boolean)this.sTHideBotsCounter.get()).booleanValue()) {
                if (string.equals("") ^ true) {
                    string = string + ", Bots: " + this.bots;
                } else {
                    String cfr_ignored_0 = "Bots: " + this.bots;
                }
            } else {
                string2 = (String)this.sTHideBotsCounterSetting.get();
                bl = false;
                String string4 = string2;
                if (string4 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                if (string4.toLowerCase().equals("onlynumber")) {
                    if (string.equals("") ^ true) {
                        string = string + ", " + this.bots;
                    }
                }
            }
            if (((Boolean)this.autoSwitchLogger.get()).booleanValue() && !((Boolean)this.sTHideAllAutoSwitcher.get()).booleanValue()) {
                if (string.equals("") ^ true && !((Boolean)this.sTHideAutoSwitcherState.get()).booleanValue()) {
                    string = string + ", AutoSwitch:" + ((Boolean)this.autoSwitchLogger.get() != false ? "On" : "Off");
                }
                if (((Boolean)this.sTHideAutoSwitcherState.get()).booleanValue()) {
                    string2 = (String)this.sTHideAutoSwitcherStateSetting.get();
                    bl = false;
                    String string5 = string2;
                    if (string5 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    if (string5.toLowerCase().equals("onlyshowstate")) {
                        String string6 = string.equals("") ^ true ? string + ", " + ((Boolean)this.autoSwitchLogger.get() != false ? "On" : "Off") : (string = (Boolean)this.autoSwitchLogger.get() != false ? "On" : "Off");
                    }
                }
                if (!((Boolean)this.sTHideAutoSwitcherMode.get()).booleanValue()) {
                    String string7 = string = string.equals("") ^ true ? string + ", " + (String)this.autoSwitchMode.get() : (String)this.autoSwitchMode.get();
                }
                if (!((Boolean)this.sTHideAutoSwitcherDelay.get()).booleanValue()) {
                    string = string.equals("") ^ true ? string + ", " + ((Number)this.autoSwitchDelay.get()).intValue() : String.valueOf(((Number)this.autoSwitchDelay.get()).intValue());
                }
            }
        }
        return string;
    }

    public static final int access$getBots$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.bots;
    }

    public static final BoolValue access$isFriendDebuggerChat$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.isFriendDebuggerChat;
    }

    private final void clearAll() {
        LiquidBounce.INSTANCE.getFileManager().friendsConfig.clearFriends();
    }

    public static final void access$printLogger(AntiFakePlayer antiFakePlayer, String string, String string2) {
        antiFakePlayer.printLogger(string, string2);
    }

    public AntiFakePlayer() {
        this.multiKillMessageList = new String[]{"\u6b63\u5728\u5927\u6740\u7279\u6740\uff01", "\u4e3b\u5bb0\u670d\u52a1\u5668\uff01", "\u6740\u4eba\u5982\u9ebb\uff01", "\u65e0\u4eba\u80fd\u6321\uff01", "\u6740\u5f97\u53d8\u6001\u4e86\uff01", "\u6b63\u5728\u50cf\u5996\u602a\u822c\u6740\u622e\uff01", "\u5982\u540c\u795e\u4e00\u822c\uff01", "\u5df2\u7ecf\u8d85\u8d8a\u795e\u4e86\uff01\u62dc\u6258\u8c01\u53bb\u6740\u4e86\u4ed6\u5427\uff01"};
        this.kitSpecialDeathChats = new String[]{"\u8d70\u7740\u8d70\u7740\u7a81\u7136\u66b4\u6bd9\u4e86!", "Boom\uff01\uff01!", "", ""};
        this.kitSpecialSkillChats = new String[]{"\u5bf9\u4f60\u7728\u773c\u4e86!", "\u8bc5\u5492\u4e86!", "\u5e76\u6ca1\u6709\u4f7f\u7528\u4f5c\u5f0a!", "", ""};
        this.botGetterModeValue = new ListValue("Mode", new String[]{"4v4/2v2/1v1", "BWXP32", "BWXP16", "KitBattle"}, "4V4/2v2/1V1");
        this.autoBotGetter = new BoolValue("AutoSwitchMode(Test", false);
        this.isFriendDebuggerChat = new BoolValue("isFriendDebuggerChat", false);
        this.printLogger = new BoolValue("ShowChatMessage", false);
        this.logStyleValue = new ListValue("LogStyle", this.logStyles, "Normal").displayable(new Function0(this){
            final AntiFakePlayer this$0;

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getPrintLogger$p(this.this$0).get();
            }

            static {
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.hideKillChatValue = new BoolValue("HideKillChat", false);
        this.showMyKillDeathChatValue = new BoolValue("ShowMyKillDeathChat", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getHideKillChatValue$p(this.this$0).get();
            }

            public Object invoke() {
                return this.invoke();
            }

            static {
            }
        });
        this.kitCustomDelay = new IntegerValue("KitCustomDelay", 4700, 4000, 8000);
        this.hideKitCoinGetChat = new BoolValue("HideKitBattleCoinChat", true).displayable(new Function0(this){
            final AntiFakePlayer this$0;

            static {
            }

            public Object invoke() {
                return this.invoke();
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public final boolean invoke() {
                String string = (String)AntiFakePlayer.access$getBotGetterModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("kitbattle");
            }
        });
        this.hideKitDeathStreakChat = new BoolValue("HideKitDeathStreakChat", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                String string = (String)AntiFakePlayer.access$getBotGetterModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("kitbattle");
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            static {
            }
        });
        this.hideKitSpecialDeathChat = new BoolValue("HideKitSpecialDeathChat", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;

            static {
            }

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                String string = (String)AntiFakePlayer.access$getBotGetterModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("kitbattle");
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }
        });
        this.hideKitSkillChat = new BoolValue("HideKitSkillChat", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public final boolean invoke() {
                String string = (String)AntiFakePlayer.access$getBotGetterModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("kitbattle");
            }

            static {
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.hideKitUpgradeChat = new BoolValue("HideKitUpgradeChat", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public final boolean invoke() {
                String string = (String)AntiFakePlayer.access$getBotGetterModeValue$p(this.this$0).get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                return string2.toLowerCase().equals("kitbattle");
            }

            static {
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.hideMultiKillChat = new BoolValue("HideMultiKillChat", true);
        this.showMyMultiKillChat = new BoolValue("ShowMyMultiKillChat", true);
        this.protectDeadPlayerName = new BoolValue("ShittyNameProtect(OnlyEnglish", false);
        this.hideValue = new BoolValue("IgnoreAntiGetname", false);
        this.showHideChat = new BoolValue("IgnoredChat", false);
        this.autoSwitchLogger = new BoolValue("AutoSwitchLogger", false);
        this.autoSwitchMode = new ListValue("AutoSwitchMode", new String[]{"Random", "List"}, "Random").displayable(new Function0(this){
            final AntiFakePlayer this$0;

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getAutoSwitchLogger$p(this.this$0).get();
            }

            static {
            }

            public Object invoke() {
                return this.invoke();
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }
        });
        this.autoSwitchDelay = new IntegerValue("AutoSwitchDelay", 3000, 1500, 7000).displayable(new Function0(this){
            final AntiFakePlayer this$0;
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getAutoSwitchLogger$p(this.this$0).get();
            }

            static {
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.sTShowOptions = new BoolValue("ShowShortTagOptions", false);
        this.sTHideAll = new BoolValue("HideAllTag", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;

            public Object invoke() {
                return this.invoke();
            }

            static {
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get();
            }
        });
        this.sTHideMode = new BoolValue("HideMode", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;

            static {
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAll$p(this.this$0).get() == false;
            }

            public Object invoke() {
                return this.invoke();
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }
        });
        this.sTHideLogStyle = new BoolValue("HideLogStyle", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAll$p(this.this$0).get() == false;
            }

            static {
            }
        });
        this.sTHideBotsCounter = new BoolValue("HideBotsCounter", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;

            static {
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAll$p(this.this$0).get() == false;
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.sTHideBotsCounterSetting = new ListValue("HideBotsCounterMode", new String[]{"FullHide", "OnlyNumber"}, "OnlyNumber").displayable(new Function0(this){
            final AntiFakePlayer this$0;

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAll$p(this.this$0).get() == false && (Boolean)AntiFakePlayer.access$getSTHideBotsCounter$p(this.this$0).get() != false;
            }

            static {
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.sTHideAllAutoSwitcher = new BoolValue("HideAllAutoSwitcher", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public Object invoke() {
                return this.invoke();
            }

            static {
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAll$p(this.this$0).get() == false;
            }
        });
        this.sTHideAutoSwitcherState = new BoolValue("HideAutoSwitcherState", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            static {
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAllAutoSwitcher$p(this.this$0).get() == false && (Boolean)AntiFakePlayer.access$getSTHideAll$p(this.this$0).get() == false;
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.sTHideAutoSwitcherStateSetting = new ListValue("HideAutoSwitcherMode", new String[]{"FullHide", "OnlyShowState"}, "FullHide").displayable(new Function0(this){
            final AntiFakePlayer this$0;

            static {
            }

            public Object invoke() {
                return this.invoke();
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAllAutoSwitcher$p(this.this$0).get() == false && (Boolean)AntiFakePlayer.access$getSTHideAutoSwitcherState$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAll$p(this.this$0).get() == false;
            }
        });
        this.sTHideAutoSwitcherMode = new BoolValue("HideAutoSwitcherMode", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAllAutoSwitcher$p(this.this$0).get() == false && (Boolean)AntiFakePlayer.access$getSTHideAll$p(this.this$0).get() == false;
            }
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            static {
            }

            public Object invoke() {
                return this.invoke();
            }
        });
        this.sTHideAutoSwitcherDelay = new BoolValue("HideAutoSwitcherDelay", false).displayable(new Function0(this){
            final AntiFakePlayer this$0;
            {
                this.this$0 = antiFakePlayer;
                super(0);
            }

            static {
            }

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                return (Boolean)AntiFakePlayer.access$getSTShowOptions$p(this.this$0).get() != false && (Boolean)AntiFakePlayer.access$getSTHideAllAutoSwitcher$p(this.this$0).get() == false && (Boolean)AntiFakePlayer.access$getSTHideAll$p(this.this$0).get() == false;
            }
        });
        this.ms = new MSTimer();
    }

    public static final Value access$getSTHideAutoSwitcherState$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.sTHideAutoSwitcherState;
    }

    public static final void access$setProtectedname$p(AntiFakePlayer antiFakePlayer, String string) {
        antiFakePlayer.protectedname = string;
    }

    private final void printLogger(String string, String string2) {
        String string3 = string2;
        boolean bl = false;
        String string4 = string3;
        if (string4 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        block4 : switch (string4.toLowerCase()) {
            case "add": {
                String string5 = (String)this.logStyleValue.get();
                boolean bl2 = false;
                String string6 = string5;
                if (string6 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string5 = string6.toLowerCase();
                int n = -1;
                switch (string5.hashCode()) {
                    case -916350399: {
                        if (!string5.equals("ryfnew")) break block4;
                        n = 1;
                        break;
                    }
                    case -1039745817: {
                        if (!string5.equals("normal")) break block4;
                        n = 2;
                        break;
                    }
                    case 112906142: {
                        if (!string5.equals("wawa2")) break block4;
                        n = 3;
                        break;
                    }
                    case 110119: {
                        if (!string5.equals("old")) break block4;
                        n = 4;
                        break;
                    }
                    case -2132879719: {
                        if (!string5.equals("special2")) break block4;
                        n = 5;
                        break;
                    }
                    case 112906141: {
                        if (!string5.equals("wawa1")) break block4;
                        n = 6;
                        break;
                    }
                    case 93078283: {
                        if (!string5.equals("arene")) break block4;
                        n = 7;
                        break;
                    }
                    case -903325499: {
                        if (!string5.equals("shitty")) break block4;
                        n = 8;
                        break;
                    }
                    case 113135984: {
                        if (!string5.equals("windx")) break block4;
                        n = 9;
                        break;
                    }
                    case -2008465223: {
                        if (!string5.equals("special")) break block4;
                        n = 10;
                        break;
                    }
                    case -994503222: {
                        if (!string5.equals("fdpchat")) break block4;
                        n = 11;
                        break;
                    }
                    case -2146994317: {
                        if (!string5.equals("fdpantibot")) break block4;
                        n = 12;
                        break;
                    }
                    case 102846135: {
                        if (!string5.equals("leave")) break block4;
                        n = 13;
                        break;
                    }
                    case -1912063982: {
                        if (!string5.equals("nullclient")) break block4;
                        n = 14;
                        break;
                    }
                    case 3413738: {
                        if (!string5.equals("old1")) break block4;
                        n = 15;
                        break;
                    }
                }
                switch (n) {
                    case 1: {
                        ClientUtils.displayChatMessage("\u00a7bAtField \u00a77\u00bb \u00a7aAdded\u00a7f HYT Bot \u00a77-> \u00a7e" + string);
                        break block4;
                    }
                    case 2: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a76AtField\u00a77] \u00a7fAdded HYT Bot: \u00a77" + string);
                        break block4;
                    }
                    case 4: {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u63d0\u9192\u60a8\u00a78] \u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a\u00a77" + string);
                        break block4;
                    }
                    case 15: {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u00a78] \u00a7d\u6dfb\u52a0\u65e0\u654c\u4eba\uff1a\u00a77" + string);
                        break block4;
                    }
                    case 12: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7cAntiBot\u00a77] \u00a7fAdded \u00a77" + string + "\u00a7f due to it being a bot.");
                        break block4;
                    }
                    case 11: {
                        ClientUtils.displayChatMessage("\u00a7f[\u00a7c!\u00a7f] \u00a77[\u00a7b\u00a7lAtField\u00a77] \u00a7aAdded \u00a76HYT bot\u00a7f[" + string + "\u00a7f]\u00a76.");
                        break block4;
                    }
                    case 10: {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7dAtField\u00a78] \u00a7a" + string + "\u00a7d\u88ab\u00a7bRyF\u00a7d\u5403\u6389\u5566! \u00a7bCiallo(\u2220\u30fb\u03c9< )\u2312\u2606");
                        break block4;
                    }
                    case 5: {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7bAtField\u00a78] \u00a7a" + string + "\u00a7b\u88abRyF\u5403\u6389\u5566! Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
                        break block4;
                    }
                    case 14: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7cAntiBots\u00a77] \u00a7fAdded a bot(\u00a77" + string + "\u00a7f)");
                        break block4;
                    }
                    case 9: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7c!\u00a77] \u00a7bColorByte \u00a7aClient \u00a77=> \u00a7aAdded \u00a7fa bot(\u00a77" + string + "\u00a7f)");
                        break block4;
                    }
                    case 8: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7aAtField\u00a77] \u00a77" + string + "\u00a7f\u88ab\u00a7bRyF\u00a7f\u5403\u6389\u5566! \u00a7aawa~");
                        break block4;
                    }
                    case 6: {
                        ClientUtils.displayChatMessage("\u00a76AtField \u00a77=> \u00a7fAdded Bot \u00a77" + string + "\u00a7f.");
                        break block4;
                    }
                    case 3: {
                        ClientUtils.displayChatMessage("\u00a76AtField \u00a77\u00bb \u00a7f\u73a9\u5bb6\u6b7b\u4ea1: \u00a77" + string);
                        break block4;
                    }
                    case 7: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7fAtField\u00a77] \u00a7fAdd a Bot(\u00a77" + string + "\u00a7f)");
                        break block4;
                    }
                    case 13: {
                        ClientUtils.displayChatMessage("\u00a7bAtField \u00a78[\u00a7eWARNING\u00a78] \u00a76\u6dfb\u52a0\u65e0\u654c\u4eba: " + string);
                        break block4;
                    }
                }
                break;
            }
            case "remove": {
                String string7 = (String)this.logStyleValue.get();
                boolean bl3 = false;
                String string8 = string7;
                if (string8 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                string7 = string8.toLowerCase();
                int n = -1;
                switch (string7.hashCode()) {
                    case -916350399: {
                        if (!string7.equals("ryfnew")) break block4;
                        n = 1;
                        break;
                    }
                    case -1039745817: {
                        if (!string7.equals("normal")) break block4;
                        n = 2;
                        break;
                    }
                    case 112906142: {
                        if (!string7.equals("wawa2")) break block4;
                        n = 3;
                        break;
                    }
                    case 110119: {
                        if (!string7.equals("old")) break block4;
                        n = 4;
                        break;
                    }
                    case -2132879719: {
                        if (!string7.equals("special2")) break block4;
                        n = 5;
                        break;
                    }
                    case 112906141: {
                        if (!string7.equals("wawa1")) break block4;
                        n = 6;
                        break;
                    }
                    case 93078283: {
                        if (!string7.equals("arene")) break block4;
                        n = 7;
                        break;
                    }
                    case -903325499: {
                        if (!string7.equals("shitty")) break block4;
                        n = 8;
                        break;
                    }
                    case 113135984: {
                        if (!string7.equals("windx")) break block4;
                        n = 9;
                        break;
                    }
                    case -2008465223: {
                        if (!string7.equals("special")) break block4;
                        n = 10;
                        break;
                    }
                    case -994503222: {
                        if (!string7.equals("fdpchat")) break block4;
                        n = 11;
                        break;
                    }
                    case -2146994317: {
                        if (!string7.equals("fdpantibot")) break block4;
                        n = 12;
                        break;
                    }
                    case 102846135: {
                        if (!string7.equals("leave")) break block4;
                        n = 13;
                        break;
                    }
                    case -1912063982: {
                        if (!string7.equals("nullclient")) break block4;
                        n = 14;
                        break;
                    }
                    case 3413738: {
                        if (!string7.equals("old1")) break block4;
                        n = 15;
                        break;
                    }
                }
                switch (n) {
                    case 1: {
                        ClientUtils.displayChatMessage("\u00a7bAtField \u00a77\u00bb \u00a7cRemoved\u00a7f HYT Bot \u00a77-> \u00a7e" + string);
                        break block4;
                    }
                    case 2: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a76AtField\u00a77] \u00a7fRemoved HYT Bot: \u00a77" + string);
                        break block4;
                    }
                    case 4: {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u63d0\u9192\u60a8\u00a78] \u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a\u00a77" + string);
                        break block4;
                    }
                    case 15: {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7c\u00a7lAtField\u00a78] \u00a7d\u5220\u9664\u65e0\u654c\u4eba\uff1a\u00a77" + string);
                        break block4;
                    }
                    case 12: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7cAntiBot\u00a77] \u00a7fRemoved \u00a77" + string + "\u00a7f due to respawn.");
                        break block4;
                    }
                    case 11: {
                        ClientUtils.displayChatMessage("\u00a7f[\u00a7c!\u00a7f] \u00a77[\u00a7b\u00a7lAtField\u00a77] \u00a7cRemoved \u00a76HYT bot\u00a7f[" + string + "\u00a7f]\u00a76.");
                        break block4;
                    }
                    case 10: {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7dAtField\u00a78] \u00a7a" + string + "\u00a7d\u88ab\u00a7bRyF\u00a7d\u5410\u51fa\u6765\u54af~ \u00a7bCiallo(\u2220\u30fb\u03c9< )\u2312\u2606");
                        break block4;
                    }
                    case 5: {
                        ClientUtils.displayChatMessage("\u00a78[\u00a7bAtField\u00a78] \u00a7a" + string + "\u00a7b\u88abRyF\u5410\u51fa\u6765\u54af~ Ciallo(\u2220\u30fb\u03c9< )\u2312\u2606");
                        break block4;
                    }
                    case 14: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7cAntiBots\u00a77] \u00a7fRemoved a bot(\u00a77" + string + "\u00a7f)");
                        break block4;
                    }
                    case 8: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7bAtField\u00a77] \u00a77" + string + "\u00a7f\u88ab\u00a7bRyF\u00a7f\u5410\u51fa\u6765\u54af~ \u00a7dqwq");
                        break block4;
                    }
                    case 9: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7c!\u00a77] \u00a7bColorByte \u00a7aClient \u00a77=> \u00a7cRemoved \u00a7fa bot(\u00a77" + string + "\u00a7f)");
                        break block4;
                    }
                    case 6: {
                        ClientUtils.displayChatMessage("\u00a76AtField \u00a77=> \u00a7fRemoved Bot \u00a77" + string + "\u00a7f.");
                        break block4;
                    }
                    case 3: {
                        ClientUtils.displayChatMessage("\u00a76AtField \u00a77\u00bb \u00a7f\u73a9\u5bb6\u91cd\u751f: \u00a77" + string);
                        break block4;
                    }
                    case 7: {
                        ClientUtils.displayChatMessage("\u00a77[\u00a7fAtField\u00a77] \u00a7fDel a Bot(\u00a77" + string + "\u00a7f)");
                        break block4;
                    }
                    case 13: {
                        ClientUtils.displayChatMessage("\u00a7bAtField \u00a78[\u00a7eWARNING\u00a78] \u00a76\u5220\u9664\u65e0\u654c\u4eba: " + string);
                        break block4;
                    }
                }
                break;
            }
        }
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        if (((Boolean)this.autoSwitchLogger.get()).booleanValue() && this.ms.hasTimePassed(((Number)this.autoSwitchDelay.get()).intValue())) {
            String string = (String)this.autoSwitchMode.get();
            int n = 0;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string2.toLowerCase()) {
                case "random": {
                    this.logStyleValue.set(this.logStyles[RandomUtils.nextInt(0, this.logStyles.length - 1)]);
                    break;
                }
                case "list": {
                    if (this.logNumber != this.logStyles.length - 1) {
                        n = this.logNumber;
                        this.logNumber = n + 1;
                        this.logStyleValue.set(this.logStyles[this.logNumber]);
                        break;
                    }
                    this.logNumber = 0;
                    this.logStyleValue.set(this.logStyles[this.logNumber]);
                    break;
                }
            }
            this.ms.reset();
        }
    }

    public static final Value access$getSTHideAll$p(AntiFakePlayer antiFakePlayer) {
        return antiFakePlayer.sTHideAll;
    }
}

