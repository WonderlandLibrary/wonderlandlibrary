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
 *  net.minecraft.util.IChatComponent
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.fun;

import java.util.Timer;
import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.TextValue;
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
import net.minecraft.util.IChatComponent;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoKit", category=ModuleCategory.FUN, description="skiderMC")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0012H\u0016J\u0010\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J\u0010\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u001aH\u0007J\u0010\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u001cH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lme/report/liquidware/modules/fun/AutoKit;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "clickStage", "", "debugValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "delayTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "editMode", "expectSlot", "kitNameValue", "Lnet/ccbluex/liquidbounce/value/TextValue;", "listening", "", "timeoutTimer", "Lnet/ccbluex/liquidbounce/utils/timer/TickTimer;", "debug", "", "s", "", "onEnable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "KyinoClient"})
public final class AutoKit
extends Module {
    private final TextValue kitNameValue = new TextValue("Kit-Name", "Armorer");
    private final BoolValue editMode = new BoolValue("EditMode", false);
    private final BoolValue debugValue = new BoolValue("Debug", false);
    private int clickStage;
    private boolean listening;
    private int expectSlot = -1;
    private TickTimer timeoutTimer = new TickTimer();
    private MSTimer delayTimer = new MSTimer();

    private final void debug(String s) {
        if (((Boolean)this.debugValue.get()).booleanValue()) {
            ClientUtils.displayChatMessage("\u00a77[\u00a74\u00a7lKyinoSense\u00a77] \u00a7r" + s);
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
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.clickStage == 1) {
            if (!this.delayTimer.hasTimePassed(1000L)) {
                return;
            }
            Minecraft minecraft = AutoKit.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(this.expectSlot - 36));
            Minecraft minecraft2 = AutoKit.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            minecraft2.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(AutoKit.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a(this.expectSlot).func_75211_c()));
            this.clickStage = 2;
            this.delayTimer.reset();
            this.debug("clicked kit selector");
        } else if (!this.listening) {
            this.delayTimer.reset();
        }
        if (this.clickStage == 2) {
            this.timeoutTimer.update();
            if (this.timeoutTimer.hasTimePassed(40)) {
                this.clickStage = 0;
                this.listening = false;
                Minecraft minecraft = AutoKit.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
                Minecraft minecraft3 = AutoKit.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                minecraft3.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoKit.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Kit checker timed out. Please use the right kit name.", Notification.Type.INFO));
                this.debug("can't find any kit with that name");
            }
        } else {
            this.timeoutTimer.reset();
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (!((Boolean)this.editMode.get()).booleanValue() && this.listening && packet instanceof S2DPacketOpenWindow) {
            event.cancelEvent();
            this.debug("listening so cancel open window packet");
            return;
        }
        if (packet instanceof C0DPacketCloseWindow && ((Boolean)this.editMode.get()).booleanValue()) {
            this.editMode.set(false);
            new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Edit mode aborted.", Notification.Type.INFO));
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
                String string = itemName;
                Intrinsics.checkExpressionValueIsNotNull(string, "itemName");
                if (StringsKt.contains((CharSequence)string, "bow", true)) {
                    String string2 = displayName;
                    Intrinsics.checkExpressionValueIsNotNull(string2, "displayName");
                    if (StringsKt.contains((CharSequence)string2, "kit selector", true)) {
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
                String string = displayName;
                Intrinsics.checkExpressionValueIsNotNull(string, "displayName");
                if (StringsKt.contains((CharSequence)string, (CharSequence)this.kitNameValue.get(), true)) {
                    this.timeoutTimer.reset();
                    this.clickStage = 3;
                    this.debug("detected kit selection");
                    Timer timer = new Timer();
                    long l = 250L;
                    boolean bl = false;
                    boolean bl2 = false;
                    TimerTask timerTask2 = new TimerTask(this, windowId, slot, item){
                        final /* synthetic */ AutoKit this$0;
                        final /* synthetic */ int $windowId$inlined;
                        final /* synthetic */ int $slot$inlined;
                        final /* synthetic */ ItemStack $item$inlined;
                        {
                            this.this$0 = autoKit;
                            this.$windowId$inlined = n;
                            this.$slot$inlined = n2;
                            this.$item$inlined = itemStack;
                        }

                        public void run() {
                            TimerTask $this$schedule = this;
                            boolean bl = false;
                            Minecraft minecraft = AutoKit.access$getMc$p$s1046033730();
                            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                            minecraft.func_147114_u().func_147297_a((Packet)new C0EPacketClickWindow(this.$windowId$inlined, this.$slot$inlined, 0, 0, this.$item$inlined, 1919));
                            Minecraft minecraft2 = AutoKit.access$getMc$p$s1046033730();
                            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                            minecraft2.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow(this.$windowId$inlined));
                            Minecraft minecraft3 = AutoKit.access$getMc$p$s1046033730();
                            Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                            minecraft3.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoKit.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
                            AutoKit.access$debug(this.this$0, "selected");
                        }
                    };
                    timer.schedule(timerTask2, l);
                    return;
                }
            }
        }
        if (packet instanceof S02PacketChat) {
            String text;
            IChatComponent iChatComponent = ((S02PacketChat)packet).func_148915_c();
            Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "packet.chatComponent");
            String string = text = iChatComponent.func_150260_c();
            Intrinsics.checkExpressionValueIsNotNull(string, "text");
            if (StringsKt.contains((CharSequence)string, "kit has been selected", true)) {
                if (((Boolean)this.editMode.get()).booleanValue()) {
                    String kitName = StringsKt.replace$default(text, " kit has been selected!", "", false, 4, null);
                    this.kitNameValue.set(kitName);
                    this.editMode.set(false);
                    this.clickStage = 0;
                    this.listening = false;
                    new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                    LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Successfully selected " + (String)this.kitNameValue.get() + " kit.", Notification.Type.INFO));
                    this.debug("finished detecting kit");
                    return;
                }
                this.listening = false;
                event.cancelEvent();
                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Successfully selected " + (String)this.kitNameValue.get() + " kit.", Notification.Type.INFO));
                this.debug("finished");
                return;
            }
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.clickStage = 0;
        this.listening = false;
        this.expectSlot = -1;
        this.timeoutTimer.reset();
        this.delayTimer.reset();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ void access$debug(AutoKit $this, String s) {
        $this.debug(s);
    }
}

