/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C0EPacketClickWindow
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S2DPacketOpenWindow
 *  net.minecraft.network.play.server.S2FPacketSetSlot
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.misc;

import java.util.Timer;
import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.utils.timer.TickTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.TextValue;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import org.jetbrains.annotations.NotNull;

@Info(name="AutoKit", spacedName="Auto Kit", description="Automatically selects kits for you in BlocksMC Skywars.", category=Category.MISC, cnName="\u81ea\u52a8\u9009\u804c\u4e1a")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0012H\u0002J\b\u0010\u001a\u001a\u00020\u0018H\u0016J\u0010\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u001dH\u0007J\u0010\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020\u001fH\u0007J\u0010\u0010 \u001a\u00020\u00182\u0006\u0010\u001c\u001a\u00020!H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2={"Lnet/dev/important/modules/module/modules/misc/AutoKit;", "Lnet/dev/important/modules/module/Module;", "()V", "clickStage", "", "debugValue", "Lnet/dev/important/value/BoolValue;", "delayTimer", "Lnet/dev/important/utils/timer/MSTimer;", "editMode", "expectSlot", "kitNameValue", "Lnet/dev/important/value/TextValue;", "kitTimeOutValue", "Lnet/dev/important/value/IntegerValue;", "listening", "", "tag", "", "getTag", "()Ljava/lang/String;", "timeoutTimer", "Lnet/dev/important/utils/timer/TickTimer;", "debug", "", "s", "onEnable", "onPacket", "event", "Lnet/dev/important/event/PacketEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "onWorld", "Lnet/dev/important/event/WorldEvent;", "LiquidBounce"})
public final class AutoKit
extends Module {
    @NotNull
    private final TextValue kitNameValue = new TextValue("Kit-Name", "Armorer");
    @NotNull
    private final IntegerValue kitTimeOutValue = new IntegerValue("Timeout-After", 40, 40, 100, " tick");
    @NotNull
    private final BoolValue editMode = new BoolValue(){

        protected void onChanged(boolean oldValue, boolean newValue) {
            if (newValue) {
                Client.INSTANCE.getHud().addNotification(new Notification("Change default kit by right clicking the kit selector and select.", Notification.Type.INFO));
            }
        }
    };
    @NotNull
    private final BoolValue debugValue = new BoolValue("Debug", false);
    private int clickStage;
    private boolean listening;
    private int expectSlot = -1;
    @NotNull
    private TickTimer timeoutTimer = new TickTimer();
    @NotNull
    private MSTimer delayTimer = new MSTimer();

    private final void debug(String s) {
        if (((Boolean)this.debugValue.get()).booleanValue()) {
            ClientUtils.displayChatMessage(Intrinsics.stringPlus("\u00a77[\u00a74\u00a7lAuto Kit\u00a77] \u00a7r", s));
        }
    }

    @Override
    public void onEnable() {
        this.clickStage = 0;
        this.listening = false;
        this.expectSlot = -1;
        this.timeoutTimer.reset();
        this.delayTimer.reset();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.clickStage == 1) {
            if (!this.delayTimer.hasTimePassed(1000L)) {
                return;
            }
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(this.expectSlot - 36));
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a(this.expectSlot).func_75211_c()));
            this.clickStage = 2;
            this.delayTimer.reset();
            this.debug("clicked kit selector");
        } else if (!this.listening) {
            this.delayTimer.reset();
        }
        if (this.clickStage == 2) {
            this.timeoutTimer.update();
            if (this.timeoutTimer.hasTimePassed(((Number)this.kitTimeOutValue.get()).intValue())) {
                this.clickStage = 0;
                this.listening = false;
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c));
                Client.INSTANCE.getHud().addNotification(new Notification("Kit checker timed out. Please use the right kit name.", Notification.Type.ERROR));
                this.debug("can't find any kit with that name");
            }
        } else {
            this.timeoutTimer.reset();
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (!((Boolean)this.editMode.get()).booleanValue() && this.listening && packet instanceof S2DPacketOpenWindow) {
            event.cancelEvent();
            this.debug("listening so cancel open window packet");
            return;
        }
        if (packet instanceof C0DPacketCloseWindow && ((Boolean)this.editMode.get()).booleanValue()) {
            this.editMode.set(false);
            Client.INSTANCE.getHud().addNotification(new Notification("Edit mode aborted.", Notification.Type.INFO));
            this.debug("abort edit mode");
            return;
        }
        if (packet instanceof S2FPacketSetSlot) {
            ItemStack itemStack = ((S2FPacketSetSlot)packet).func_149174_e();
            if (itemStack == null) {
                return;
            }
            ItemStack item = itemStack;
            int windowId = ((S2FPacketSetSlot)packet).func_149175_c();
            int slot = ((S2FPacketSetSlot)packet).func_149173_d();
            String itemName = item.func_77977_a();
            String displayName = item.func_82833_r();
            if (this.clickStage == 0 && windowId == 0 && slot >= 36 && slot <= 44) {
                Intrinsics.checkNotNullExpressionValue(itemName, "itemName");
                if (StringsKt.contains((CharSequence)itemName, "bow", true)) {
                    Intrinsics.checkNotNullExpressionValue(displayName, "displayName");
                    if (StringsKt.contains((CharSequence)displayName, "kit selector", true)) {
                        if (((Boolean)this.editMode.get()).booleanValue()) {
                            this.listening = true;
                            this.debug("found item, listening to kit selection cuz of edit mode");
                            return;
                        }
                        this.listening = true;
                        this.clickStage = 1;
                        this.expectSlot = slot;
                        this.debug("found item, sent trigger");
                        return;
                    }
                }
            }
            if (this.clickStage == 2) {
                Intrinsics.checkNotNullExpressionValue(displayName, "displayName");
                if (StringsKt.contains((CharSequence)displayName, (CharSequence)this.kitNameValue.get(), true)) {
                    this.timeoutTimer.reset();
                    this.clickStage = 3;
                    this.debug("detected kit selection");
                    Timer timer = new Timer();
                    long l = 250L;
                    TimerTask timerTask2 = new TimerTask(windowId, slot, item, this){
                        final /* synthetic */ int $windowId$inlined;
                        final /* synthetic */ int $slot$inlined;
                        final /* synthetic */ ItemStack $item$inlined;
                        final /* synthetic */ AutoKit this$0;
                        {
                            this.$windowId$inlined = n;
                            this.$slot$inlined = n2;
                            this.$item$inlined = itemStack;
                            this.this$0 = autoKit;
                        }

                        public void run() {
                            TimerTask $this$onPacket_u24lambda_u2d0 = this;
                            boolean bl = false;
                            AutoKit.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C0EPacketClickWindow(this.$windowId$inlined, this.$slot$inlined, 0, 0, this.$item$inlined, 1919));
                            AutoKit.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow(this.$windowId$inlined));
                            AutoKit.access$getMc$p$s1046033730().func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoKit.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
                            AutoKit.access$debug(this.this$0, "selected");
                        }
                    };
                    timer.schedule(timerTask2, l);
                    return;
                }
            }
        }
        if (packet instanceof S02PacketChat) {
            String text = ((S02PacketChat)packet).func_148915_c().func_150260_c();
            Intrinsics.checkNotNullExpressionValue(text, "text");
            if (StringsKt.contains((CharSequence)text, "kit has been selected", true)) {
                if (((Boolean)this.editMode.get()).booleanValue()) {
                    String kitName = StringsKt.replace$default(text, " kit has been selected!", "", false, 4, null);
                    this.kitNameValue.set(kitName);
                    this.editMode.set(false);
                    this.clickStage = 0;
                    this.listening = false;
                    Client.INSTANCE.getHud().addNotification(new Notification("Successfully detected and added " + kitName + " kit.", Notification.Type.SUCCESS));
                    this.debug("finished detecting kit");
                    return;
                }
                this.listening = false;
                event.cancelEvent();
                Client.INSTANCE.getHud().addNotification(new Notification("Successfully selected " + (String)this.kitNameValue.get() + " kit.", Notification.Type.SUCCESS));
                this.debug("finished");
                return;
            }
        }
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.clickStage = 0;
        this.listening = false;
        this.expectSlot = -1;
        this.timeoutTimer.reset();
        this.delayTimer.reset();
    }

    @Override
    @NotNull
    public String getTag() {
        return (Boolean)this.editMode.get() != false && this.listening ? "Listening..." : (String)this.kitNameValue.get();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ void access$debug(AutoKit $this, String s) {
        $this.debug(s);
    }
}

