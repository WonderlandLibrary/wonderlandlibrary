/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketChat
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;

@ModuleInfo(name="AutoGG", category=ModuleCategory.PLAYER, description="Auto GG")
public final class AutoGG
extends Module {
    private int win;
    private final TextValue textValu;
    private final ListValue modeValue = new ListValue("Server", new String[]{"HuaYuTingBW", "HuaYuTingSw", "HuaYuTing16"}, "HuaYuTingBW");
    private final BoolValue prefix = new BoolValue("@", true);
    private final TextValue textValue = new TextValue("Text", "[AtField]GG");
    private int totalPlayed;

    public final int getWin() {
        return this.win;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public final void setWin(int n) {
        this.win = n;
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        Object object = packetEvent.getPacket();
        boolean bl = false;
        Packet packet = ((PacketImpl)object).getWrapped();
        if (packet instanceof SPacketChat) {
            object = ((SPacketChat)packet).func_148915_c().func_150260_c();
            String string = (String)this.modeValue.get();
            int n = 0;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string2.toLowerCase()) {
                case "huayutingbw": {
                    if (StringsKt.contains((CharSequence)((CharSequence)object), (CharSequence)"      \u559c\u6b22      \u4e00\u822c      \u4e0d\u559c\u6b22", (boolean)true)) {
                        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP.sendChatMessage(((Boolean)this.prefix.get() != false ? "@" : "") + (String)this.textValue.get());
                        ++this.win;
                        LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AutoGG", "\u606d\u559c\u80dc\u5229\uff01", NotifyType.INFO, 0, 0, 24, null));
                    }
                    if (!StringsKt.contains((CharSequence)((CharSequence)object), (CharSequence)"\u8d77\u5e8a\u6218\u4e89>> \u6e38\u620f\u5f00\u59cb ...", (boolean)true)) break;
                    n = this.totalPlayed;
                    this.totalPlayed = n + 1;
                    LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AutoGG", "\u6e38\u620f\u5f00\u59cb\uff01\uff01", NotifyType.INFO, 0, 0, 24, null));
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.sendChatMessage((String)this.textValu.get());
                    break;
                }
                case "huayuting16": {
                    if (!StringsKt.contains((CharSequence)((CharSequence)object), (CharSequence)"[\u8d77\u5e8a\u6218\u4e89] Game \u7ed3\u675f\uff01\u611f\u8c22\u60a8\u7684\u53c2\u4e0e\uff01", (boolean)true)) break;
                    LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AutoGG", "Game Over", NotifyType.INFO, 0, 0, 24, null));
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.sendChatMessage(((Boolean)this.prefix.get() != false ? "@" : "") + (String)this.textValue.get());
                    break;
                }
                case "huayutingsw": {
                    Matcher matcher = Pattern.compile("\u4f60\u5728\u5730\u56fe (.*?)\\(").matcher(((SPacketChat)packet).func_148915_c().func_150260_c());
                    if (!StringsKt.contains((CharSequence)((CharSequence)object), (CharSequence)"\u4f60\u73b0\u5728\u662f\u89c2\u5bdf\u8005\u72b6\u6001. \u6309E\u6253\u5f00\u83dc\u5355.", (boolean)true)) break;
                    LiquidBounce.INSTANCE.getHud().addNotification(new Notification("AutoGG", "Game Over", NotifyType.INFO, 0, 0, 24, null));
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.sendChatMessage(((Boolean)this.prefix.get() != false ? "@" : "") + (String)this.textValue.get());
                    break;
                }
            }
        }
    }

    public AutoGG() {
        this.textValu = new TextValue("Text2", "@\u6211\u6b63\u5728\u4f7f\u7528AtField");
    }

    public final int getTotalPlayed() {
        return this.totalPlayed;
    }

    public final void setTotalPlayed(int n) {
        this.totalPlayed = n;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

