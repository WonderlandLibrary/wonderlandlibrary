/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.network.play.server.SPacketChat
 */
package net.ccbluex.liquidbounce.features.module.modules.hyt;

import jx.utils.Recorder;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.hyt.FadeState;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.network.play.server.SPacketChat;

@ModuleInfo(name="AutoGG", category=ModuleCategory.HYT, description="Tomk")
public final class AutoGG
extends Module {
    private FadeState fadeState = FadeState.NO;
    private final ListValue mode = new ListValue("GG-Mode", new String[]{"HYT4V4", "HYTSKY"}, "HYT4V4");
    private final TextValue textValue = new TextValue("Text", "GG");

    public final FadeState getFadeState() {
        return this.fadeState;
    }

    public final void setFadeState(FadeState fadeState) {
        this.fadeState = fadeState;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onPacket(PacketEvent event) {
        int n;
        IPacket $this$unwrap$iv = event.getPacket();
        boolean $i$f$unwrap = false;
        IPacket iPacket = $this$unwrap$iv;
        if (iPacket == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.PacketImpl<*>");
        }
        Object packet = ((PacketImpl)iPacket).getWrapped();
        if (!(packet instanceof SPacketChat)) return;
        String text = ((SPacketChat)packet).func_148915_c().func_150260_c();
        String string = (String)this.mode.get();
        switch (string.hashCode()) {
            case 2146076862: {
                if (!string.equals("HYTSKY")) return;
                break;
            }
            case 2146047375: {
                int n2;
                if (!string.equals("HYT4V4")) return;
                if (StringsKt.contains((CharSequence)text, (CharSequence)"\u606d\u559c", (boolean)true)) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.sendChatMessage((String)this.textValue.get());
                    Recorder recorder = Recorder.INSTANCE;
                    n2 = recorder.getWin();
                    recorder.setWin(n2 + 1);
                    this.fadeState = FadeState.FRIST;
                }
                if (!StringsKt.contains((CharSequence)text, (CharSequence)"\u6e38\u620f\u5f00\u59cb", (boolean)true)) return;
                Recorder recorder = Recorder.INSTANCE;
                n2 = recorder.getTotalPlayed();
                recorder.setTotalPlayed(n2 + 1);
                return;
            }
        }
        if (StringsKt.contains((CharSequence)text, (CharSequence)"\u4f60\u73b0\u5728\u662f\u89c2\u5bdf\u8005\u72b6\u6001. \u6309E\u6253\u5f00\u83dc\u5355.", (boolean)true)) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.sendChatMessage((String)this.textValue.get());
            Recorder recorder = Recorder.INSTANCE;
            n = recorder.getWin();
            recorder.setWin(n + 1);
            this.fadeState = FadeState.FRIST;
            LiquidBounce.INSTANCE.getModuleManager().get(ChestStealer.class).setState(false);
            LiquidBounce.INSTANCE.getModuleManager().get(InventoryCleaner.class).setState(false);
        }
        if (!StringsKt.contains((CharSequence)text, (CharSequence)"\u5f00\u59cb\u5012\u8ba1\u65f6:", (boolean)true)) return;
        Recorder recorder = Recorder.INSTANCE;
        n = recorder.getTotalPlayed();
        recorder.setTotalPlayed(n + 1);
        LiquidBounce.INSTANCE.getModuleManager().get(ChestStealer.class).setState(true);
        LiquidBounce.INSTANCE.getModuleManager().get(InventoryCleaner.class).setState(true);
    }

    @Override
    public String getTag() {
        return "HuaYuTing";
    }
}

