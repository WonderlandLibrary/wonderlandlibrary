/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.event.ClickEvent
 *  net.minecraft.event.ClickEvent$Action
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C0EPacketClickWindow
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S2DPacketOpenWindow
 *  net.minecraft.network.play.server.S2FPacketSetSlot
 *  net.minecraft.network.play.server.S45PacketTitle
 *  net.minecraft.util.IChatComponent
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.misc;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.misc.AutoDisable;
import net.dev.important.modules.module.modules.misc.AutoPlay;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

@Info(name="GamePlay", spacedName="Auto Play", description="Automatically move you to another game after finishing it.", category=Category.MISC, cnName="\u81ea\u52a8\u4e0b\u4e00\u5c40\u6e38\u620f")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0014H\u0007J \u0010\u0015\u001a\u00020\u000f2\b\b\u0002\u0010\u0016\u001a\u00020\r2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0018H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lnet/dev/important/modules/module/modules/misc/AutoPlay;", "Lnet/dev/important/modules/module/Module;", "()V", "clickState", "", "clicking", "", "delayValue", "Lnet/dev/important/value/IntegerValue;", "modeValue", "Lnet/dev/important/value/ListValue;", "queued", "startTime", "", "onEnable", "", "onPacket", "event", "Lnet/dev/important/event/PacketEvent;", "onWorld", "Lnet/dev/important/event/WorldEvent;", "queueAutoPlay", "delay", "runnable", "Lkotlin/Function0;", "Companion", "LiquidBounce"})
public final class AutoPlay
extends Module {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private int clickState;
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final IntegerValue delayValue;
    private boolean clicking;
    private boolean queued;
    private long startTime;
    private static int totalPlayed;
    private static int win;

    public AutoPlay() {
        String[] stringArray = new String[]{"RedeSky", "BlocksMC", "Minemora", "Hypixel", "Jartex"};
        this.modeValue = new ListValue("Server", stringArray, "RedeSky");
        this.delayValue = new IntegerValue("JoinDelay", 3, 0, 7, " seconds");
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onEnable() {
        this.clickState = 0;
        this.clicking = false;
        this.queued = false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        String string;
        Packet<?> packet;
        block44: {
            String title;
            Intrinsics.checkNotNullParameter(event, "event");
            packet = event.getPacket();
            if (event.getPacket() instanceof C00Handshake) {
                this.startTime = System.currentTimeMillis();
            }
            if (packet instanceof S45PacketTitle) {
                int n;
                Object object;
                title = ((S45PacketTitle)packet).func_179805_b().func_150254_d();
                Intrinsics.checkNotNullExpressionValue(title, "title");
                if (StringsKt.contains$default((CharSequence)title, "CHAMPION", false, 2, null)) {
                    object = Companion;
                    n = win;
                    win = n + 1;
                }
                if (StringsKt.contains$default((CharSequence)title, "YOU DIED", false, 2, null)) {
                    object = Companion;
                    n = totalPlayed;
                    totalPlayed = n + 1;
                }
            }
            string = ((String)this.modeValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            title = string;
            if (Intrinsics.areEqual(title, "redesky")) {
                if (this.clicking && (packet instanceof C0EPacketClickWindow || packet instanceof C07PacketPlayerDigging)) {
                    event.cancelEvent();
                    return;
                }
                if (this.clickState == 2 && packet instanceof S2DPacketOpenWindow) {
                    event.cancelEvent();
                }
            } else if (Intrinsics.areEqual(title, "hypixel") && this.clickState == 1 && packet instanceof S2DPacketOpenWindow) {
                event.cancelEvent();
            }
            if (!(packet instanceof S2FPacketSetSlot)) break block44;
            ItemStack itemStack = ((S2FPacketSetSlot)packet).func_149174_e();
            if (itemStack == null) {
                return;
            }
            ItemStack item = itemStack;
            int windowId = ((S2FPacketSetSlot)packet).func_149175_c();
            int slot = ((S2FPacketSetSlot)packet).func_149173_d();
            String itemName2 = item.func_77977_a();
            String displayName = item.func_82833_r();
            String string2 = ((String)this.modeValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String).toLowerCase()");
            switch (string2) {
                case "redesky": {
                    if (this.clickState == 0 && windowId == 0 && slot == 42) {
                        Intrinsics.checkNotNullExpressionValue(itemName2, "itemName");
                        if (StringsKt.contains((CharSequence)itemName2, "paper", true)) {
                            Intrinsics.checkNotNullExpressionValue(displayName, "displayName");
                            if (StringsKt.contains((CharSequence)displayName, "Jogar novamente", true)) {
                                this.clickState = 1;
                                this.clicking = true;
                                AutoPlay.queueAutoPlay$default(this, 0L, new Function0<Unit>(item, this){
                                    final /* synthetic */ ItemStack $item;
                                    final /* synthetic */ AutoPlay this$0;
                                    {
                                        this.$item = $item;
                                        this.this$0 = $receiver;
                                        super(0);
                                    }

                                    public final void invoke() {
                                        AutoPlay.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(6));
                                        AutoPlay.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(this.$item));
                                        AutoPlay.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoPlay.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
                                        AutoPlay.access$setClickState$p(this.this$0, 2);
                                    }
                                }, 1, null);
                                return;
                            }
                        }
                    }
                    if (this.clickState != 2 || windowId == 0 || slot != 11) return;
                    Intrinsics.checkNotNullExpressionValue(itemName2, "itemName");
                    if (!StringsKt.contains((CharSequence)itemName2, "enderPearl", true)) return;
                    Timer timer = new Timer();
                    long l = 500L;
                    TimerTask timerTask2 = new TimerTask(this, windowId, slot, item){
                        final /* synthetic */ AutoPlay this$0;
                        final /* synthetic */ int $windowId$inlined;
                        final /* synthetic */ int $slot$inlined;
                        final /* synthetic */ ItemStack $item$inlined;
                        {
                            this.this$0 = autoPlay;
                            this.$windowId$inlined = n;
                            this.$slot$inlined = n2;
                            this.$item$inlined = itemStack;
                        }

                        public void run() {
                            TimerTask $this$onPacket_u24lambda_u2d0 = this;
                            boolean bl = false;
                            AutoPlay.access$setClicking$p(this.this$0, false);
                            AutoPlay.access$setClickState$p(this.this$0, 0);
                            AutoPlay.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C0EPacketClickWindow(this.$windowId$inlined, this.$slot$inlined, 0, 0, this.$item$inlined, 1919));
                        }
                    };
                    timer.schedule(timerTask2, l);
                    return;
                }
                case "hypixel": 
                case "blocksmc": {
                    if (this.clickState == 0 && windowId == 0 && slot == 43) {
                        Intrinsics.checkNotNullExpressionValue(itemName2, "itemName");
                        if (StringsKt.contains((CharSequence)itemName2, "paper", true)) {
                            AutoPlay.queueAutoPlay$default(this, 0L, new Function0<Unit>(item){
                                final /* synthetic */ ItemStack $item;
                                {
                                    this.$item = $item;
                                    super(0);
                                }

                                public final void invoke() {
                                    AutoPlay.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(7));
                                    int n = 2;
                                    ItemStack itemStack = this.$item;
                                    int n2 = 0;
                                    while (n2 < n) {
                                        int n3;
                                        int it = n3 = n2++;
                                        boolean bl = false;
                                        AutoPlay.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(itemStack));
                                    }
                                }
                            }, 1, null);
                            this.clickState = 1;
                        }
                    }
                    if (!this.modeValue.equals("hypixel") || this.clickState != 1 || windowId == 0 || !StringsKt.equals(itemName2, "item.fireworks", true)) return;
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0EPacketClickWindow(windowId, slot, 0, 0, item, 1919));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow(windowId));
                }
                default: {
                    return;
                }
            }
        }
        if (!(packet instanceof S02PacketChat)) return;
        String text = ((S02PacketChat)packet).func_148915_c().func_150260_c();
        Object itemName2 = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(itemName2, "this as java.lang.String).toLowerCase()");
        switch (itemName2) {
            case "minemora": {
                Intrinsics.checkNotNullExpressionValue(text, "text");
                if (!StringsKt.contains((CharSequence)text, "Has click en alguna de las siguientes opciones", true)) return;
                AutoPlay.queueAutoPlay$default(this, 0L, onPacket.4.INSTANCE, 1, null);
                return;
            }
            case "blocksmc": {
                if (this.clickState != 1) return;
                Intrinsics.checkNotNullExpressionValue(text, "text");
                if (!StringsKt.contains((CharSequence)text, "Only VIP players can join full servers!", true)) return;
                Client.INSTANCE.getHud().addNotification(new Notification("Join failed! trying again...", Notification.Type.WARNING, 3000L));
                Timer slot = new Timer();
                long itemName2 = 1500L;
                TimerTask timerTask3 = new TimerTask(){

                    public void run() {
                        TimerTask $this$onPacket_u24lambda_u2d2 = this;
                        boolean bl = false;
                        AutoPlay.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(7));
                        int n = 2;
                        int n2 = 0;
                        while (n2 < n) {
                            int n3;
                            int it = n3 = n2++;
                            boolean bl2 = false;
                            AutoPlay.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(AutoPlay.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g()));
                        }
                    }
                };
                slot.schedule(timerTask3, itemName2);
                return;
            }
            case "jartex": {
                IChatComponent component = ((S02PacketChat)packet).func_148915_c();
                Intrinsics.checkNotNullExpressionValue(text, "text");
                if (!StringsKt.contains((CharSequence)text, "Click here to play again", true)) return;
                itemName2 = component.func_150253_a();
                Intrinsics.checkNotNullExpressionValue(itemName2, "component.siblings");
                Iterable $this$forEach$iv = (Iterable)itemName2;
                boolean $i$f$forEach = false;
                for (Object element$iv : $this$forEach$iv) {
                    IChatComponent sib = (IChatComponent)element$iv;
                    boolean bl = false;
                    ClickEvent clickEvent = sib.func_150256_b().func_150235_h();
                    if (clickEvent == null || clickEvent.func_150669_a() != ClickEvent.Action.RUN_COMMAND) continue;
                    String string3 = clickEvent.func_150668_b();
                    Intrinsics.checkNotNullExpressionValue(string3, "clickEvent.value");
                    if (!StringsKt.startsWith$default(string3, "/", false, 2, null)) continue;
                    AutoPlay.queueAutoPlay$default(this, 0L, new Function0<Unit>(clickEvent){
                        final /* synthetic */ ClickEvent $clickEvent;
                        {
                            this.$clickEvent = $clickEvent;
                            super(0);
                        }

                        public final void invoke() {
                            AutoPlay.access$getMc$p$s1046033730().field_71439_g.func_71165_d(this.$clickEvent.func_150668_b());
                        }
                    }, 1, null);
                }
                return;
            }
            case "hypixel": {
                string = ((S02PacketChat)packet).func_148915_c();
                Intrinsics.checkNotNullExpressionValue(string, "packet.chatComponent");
                AutoPlay.onPacket$process(this, (IChatComponent)string);
            }
        }
    }

    private final void queueAutoPlay(long delay, Function0<Unit> runnable) {
        if (this.queued) {
            return;
        }
        this.queued = true;
        AutoDisable.Companion.handleGameEnd();
        if (this.getState()) {
            Timer timer = new Timer();
            TimerTask timerTask2 = new TimerTask(this, runnable){
                final /* synthetic */ AutoPlay this$0;
                final /* synthetic */ Function0 $runnable$inlined;
                {
                    this.this$0 = autoPlay;
                    this.$runnable$inlined = function0;
                }

                public void run() {
                    TimerTask $this$queueAutoPlay_u24lambda_u2d5 = this;
                    boolean bl = false;
                    AutoPlay.access$setQueued$p(this.this$0, false);
                    if (this.this$0.getState()) {
                        this.$runnable$inlined.invoke();
                    }
                }
            };
            timer.schedule(timerTask2, delay);
            Client.INSTANCE.getHud().addNotification(new Notification("Sending you to next game in " + ((Number)this.delayValue.get()).intValue() + "s...", Notification.Type.INFO, (long)((Number)this.delayValue.get()).intValue() * 1000L));
        }
    }

    static /* synthetic */ void queueAutoPlay$default(AutoPlay autoPlay, long l, Function0 function0, int n, Object object) {
        if ((n & 1) != 0) {
            l = (long)((Number)autoPlay.delayValue.get()).intValue() * (long)1000;
        }
        autoPlay.queueAutoPlay(l, function0);
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.clicking = false;
        this.clickState = 0;
        this.queued = false;
    }

    private static final void onPacket$process(AutoPlay this$0, IChatComponent component) {
        String value;
        ClickEvent clickEvent = component.func_150256_b().func_150235_h();
        String string = value = clickEvent == null ? null : clickEvent.func_150668_b();
        if (value != null && StringsKt.startsWith(value, "/play", true)) {
            AutoPlay.queueAutoPlay$default(this$0, 0L, new Function0<Unit>(value){
                final /* synthetic */ String $value;
                {
                    this.$value = $value;
                    super(0);
                }

                public final void invoke() {
                    AutoPlay.access$getMc$p$s1046033730().field_71439_g.func_71165_d(this.$value);
                }
            }, 1, null);
        }
        List list = component.func_150253_a();
        Intrinsics.checkNotNullExpressionValue(list, "component.siblings");
        Iterable $this$forEach$iv = list;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            IChatComponent it = (IChatComponent)element$iv;
            boolean bl = false;
            Intrinsics.checkNotNullExpressionValue(it, "it");
            AutoPlay.onPacket$process(this$0, it);
        }
    }

    public static final int getTotalPlayed() {
        return Companion.getTotalPlayed();
    }

    public static final void setTotalPlayed(int n) {
        Companion.setTotalPlayed(n);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ void access$setClickState$p(AutoPlay $this, int n) {
        $this.clickState = n;
    }

    public static final /* synthetic */ void access$setClicking$p(AutoPlay $this, boolean bl) {
        $this.clicking = bl;
    }

    public static final /* synthetic */ void access$setQueued$p(AutoPlay $this, boolean bl) {
        $this.queued = bl;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R$\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0014\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\u0007\"\u0004\b\f\u0010\t\u00a8\u0006\r"}, d2={"Lnet/dev/important/modules/module/modules/misc/AutoPlay$Companion;", "", "()V", "totalPlayed", "", "getTotalPlayed$annotations", "getTotalPlayed", "()I", "setTotalPlayed", "(I)V", "win", "getWin", "setWin", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        public final int getTotalPlayed() {
            return totalPlayed;
        }

        public final void setTotalPlayed(int n) {
            totalPlayed = n;
        }

        @JvmStatic
        public static /* synthetic */ void getTotalPlayed$annotations() {
        }

        public final int getWin() {
            return win;
        }

        public final void setWin(int n) {
            win = n;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

