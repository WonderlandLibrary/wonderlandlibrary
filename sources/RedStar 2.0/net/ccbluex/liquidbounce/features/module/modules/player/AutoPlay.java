package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.Timer;
import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.sound.SoundPlayer;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.AutoPlay;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.MiscUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketOpenWindow;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoPlay", category=ModuleCategory.PLAYER, description="idk")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000V\n\n\n\b\n\n\u0000\n\n\u0000\n\b\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\u0000\b\u000020B¢J\b0\nHJ\b0HJ020HJ020HJ02\f\b00HR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R\t0\nX¢\n\u0000R0\fX¢\n\u0000R\r0X¢\n\u0000R0\nX¢\n\u0000R08VX¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/AutoPlay;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "Clientname", "Lnet/ccbluex/liquidbounce/value/TextValue;", "autogg", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "clickState", "", "clicking", "", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "queued", "tag", "", "getTag", "()Ljava/lang/String;", "handleEvents", "onEnable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "queueAutoPlay", "runnable", "Lkotlin/Function0;", "Pride"})
public final class AutoPlay
extends Module {
    private int clickState;
    private final BoolValue autogg = new BoolValue("AutoGG", true);
    private final TextValue Clientname = new TextValue("Clientname", "RedStar");
    private final ListValue modeValue = new ListValue("Server", new String[]{"RedeSky", "Minemora", "HuaYuTing"}, "HuaYuTingGG");
    private final IntegerValue delayValue = new IntegerValue("JoinDelay", 3, 0, 7);
    private boolean clicking;
    private boolean queued;

    @Override
    public void onEnable() {
        this.clickState = 0;
        this.clicking = false;
        this.queued = false;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        $this$unwrap$iv = event.getPacket();
        $i$f$unwrap = false;
        packet = ((PacketImpl)$this$unwrap$iv).getWrapped();
        $this$unwrap$iv = (String)this.modeValue.get();
        var4_3 = false;
        v0 = $this$unwrap$iv;
        if (v0 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v1 = v0.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toLowerCase()");
        $this$unwrap$iv = v1;
        switch ($this$unwrap$iv.hashCode()) {
            case 1381910549: {
                if (!$this$unwrap$iv.equals("hypixel")) ** break;
                break;
            }
            case 1083223725: {
                if (!$this$unwrap$iv.equals("redesky")) ** break;
                if (this.clicking && (packet instanceof CPacketClickWindow || packet instanceof CPacketPlayerDigging)) {
                    event.cancelEvent();
                    return;
                }
                if (this.clickState != 2 || !(packet instanceof SPacketOpenWindow)) ** break;
                event.cancelEvent();
                ** break;
            }
        }
        if (this.clickState == 1 && packet instanceof SPacketOpenWindow) {
            event.cancelEvent();
            ** break;
        }
lbl29:
        // 8 sources

        if (packet instanceof SPacketChat) {
            v2 = ((SPacketChat)packet).getChatComponent();
            Intrinsics.checkExpressionValueIsNotNull(v2, "packet.chatComponent");
            text = v2.getUnformattedText();
            var4_4 = (String)this.modeValue.get();
            var5_6 = 0;
            v3 = var4_4;
            if (v3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v4 = v3.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(v4, "(this as java.lang.String).toLowerCase()");
            var4_4 = v4;
            tmp = -1;
            switch (var4_4.hashCode()) {
                case -1362756060: {
                    if (!var4_4.equals("minemora")) break;
                    tmp = 1;
                    break;
                }
                case -1777040898: {
                    if (!var4_4.equals("huayuting")) break;
                    tmp = 2;
                    break;
                }
            }
            switch (tmp) {
                case 1: {
                    v5 = text;
                    Intrinsics.checkExpressionValueIsNotNull(v5, "text");
                    if (!StringsKt.contains((CharSequence)v5, "w", true)) break;
                    this.queueAutoPlay(onPacket.1.INSTANCE);
                    break;
                }
                case 2: {
                    v6 = text;
                    Intrinsics.checkExpressionValueIsNotNull(v6, "text");
                    if (StringsKt.contains((CharSequence)v6, "      喜欢      一般      不喜欢", true)) {
                        LiquidBounce.INSTANCE.getHud().addNotification(new Notification(this.getName(), "You Win！", NotifyType.INFO, 0, 0, 24, null));
                        new SoundPlayer().playSound(SoundPlayer.SoundType.VICTORY, LiquidBounce.INSTANCE.getModuleManager().getToggleVolume());
                        if (((Boolean)this.autogg.get()).booleanValue()) {
                            v7 = MinecraftInstance.mc.getThePlayer();
                            if (v7 == null) {
                                Intrinsics.throwNpe();
                            }
                            v7.sendChatMessage("[" + (String)this.Clientname.get() + "] GG  ");
                        }
                        new SoundPlayer().playSound(SoundPlayer.SoundType.VICTORY, LiquidBounce.INSTANCE.getModuleManager().getToggleVolume());
                        v8 = Recorder.INSTANCE;
                        var5_6 = v8.getTotalPlayed();
                        v8.setTotalPlayed(var5_6 + 1);
                        break;
                    }
                    if (StringsKt.contains((CharSequence)text, "你现在是观察者状态. 按E打开菜单.", true)) {
                        LiquidBounce.INSTANCE.getHud().addNotification(new Notification(this.getName(), "You Win！", NotifyType.INFO, 0, 0, 24, null));
                        if (((Boolean)this.autogg.get()).booleanValue()) {
                            v9 = MinecraftInstance.mc.getThePlayer();
                            if (v9 == null) {
                                Intrinsics.throwNpe();
                            }
                            v9.sendChatMessage("[" + (String)this.Clientname.get() + "] GG");
                        }
                        new SoundPlayer().playSound(SoundPlayer.SoundType.VICTORY, LiquidBounce.INSTANCE.getModuleManager().getToggleVolume());
                        v10 = Recorder.INSTANCE;
                        var5_6 = v10.getTotalPlayed();
                        v10.setTotalPlayed(var5_6 + 1);
                        break;
                    }
                    if (!StringsKt.contains((CharSequence)text, "[起床战争] Game 结束！感谢您的参与！", true)) break;
                    LiquidBounce.INSTANCE.getHud().addNotification(new Notification(this.getName(), "Game Over", NotifyType.INFO, 0, 0, 24, null));
                    if (((Boolean)this.autogg.get()).booleanValue()) {
                        v11 = MinecraftInstance.mc.getThePlayer();
                        if (v11 == null) {
                            Intrinsics.throwNpe();
                        }
                        v11.sendChatMessage("[" + (String)this.Clientname.get() + "] GG ");
                    }
                    new SoundPlayer().playSound(SoundPlayer.SoundType.VICTORY, LiquidBounce.INSTANCE.getModuleManager().getToggleVolume());
                    v12 = Recorder.INSTANCE;
                    var5_6 = v12.getTotalPlayed();
                    v12.setTotalPlayed(var5_6 + 1);
                    break;
                }
            }
        }
    }

    private final void queueAutoPlay(Function0<Unit> runnable) {
        if (this.queued) {
            return;
        }
        this.queued = true;
        if (this.getState()) {
            Timer timer = new Timer();
            long l = (long)((Number)this.delayValue.get()).intValue() * (long)1000;
            boolean bl = false;
            boolean bl2 = false;
            TimerTask timerTask = new TimerTask(this, runnable){
                final AutoPlay this$0;
                final Function0 $runnable$inlined;
                {
                    this.this$0 = autoPlay;
                    this.$runnable$inlined = function0;
                }

                public void run() {
                    TimerTask $this$schedule = this;
                    boolean bl = false;
                    if (this.this$0.getState()) {
                        this.$runnable$inlined.invoke();
                    }
                }
            };
            timer.schedule(timerTask, l);
            new MiscUtils().playSound(MiscUtils.SoundType.VICTORY, -8.0f);
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification(this.getName(), "Sending you to next game in " + ((Number)this.delayValue.get()).intValue() + "s...", NotifyType.INFO, 0, 0, 24, null));
        }
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.clicking = false;
        this.clickState = 0;
        this.queued = false;
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}
