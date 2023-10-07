/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.entity.boss.BossStatus
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C0EPacketClickWindow
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S2FPacketSetSlot
 *  net.minecraft.util.IChatComponent
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.player;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.IChatComponent;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoPlay", category=ModuleCategory.PLAYER, description="Automatically throws in a new game.(Blocksmc.com)")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0019\u001a\u00020\u0006H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0010\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\u001eH\u0007J\u0010\u0010\u001f\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020 H\u0007J\u0016\u0010!\u001a\u00020\u001b2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u001b0#H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2={"Lme/report/liquidware/modules/player/AutoPlay;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "clickState", "", "clicking", "", "dFormat", "Ljava/text/DecimalFormat;", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "errorBoxHeight", "", "fraction", "posY", "queued", "shouldChangeGame", "getShouldChangeGame", "()Z", "setShouldChangeGame", "(Z)V", "test1", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "handleEvents", "onEnable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "queueAutoPlay", "runnable", "Lkotlin/Function0;", "KyinoClient"})
public final class AutoPlay
extends Module {
    private int clickState;
    private final IntegerValue delayValue = new IntegerValue("JoinDelay", 3, 0, 7);
    private final BoolValue test1 = new BoolValue("BlocksmcTest", false);
    private boolean clicking;
    private boolean queued;
    private final float fraction;
    private float errorBoxHeight;
    private float posY = -20.0f;
    private final DecimalFormat dFormat = new DecimalFormat("0.0");
    private boolean shouldChangeGame;
    private final MSTimer timer = new MSTimer();

    public final boolean getShouldChangeGame() {
        return this.shouldChangeGame;
    }

    public final void setShouldChangeGame(boolean bl) {
        this.shouldChangeGame = bl;
    }

    @Override
    public void onEnable() {
        this.clickState = 0;
        this.clicking = false;
        this.queued = false;
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
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
            if (this.clickState == 0 && windowId == 0 && slot == 43) {
                String string = itemName;
                Intrinsics.checkExpressionValueIsNotNull(string, "itemName");
                if (StringsKt.contains((CharSequence)string, "paper", true)) {
                    this.queueAutoPlay(new Function0<Unit>(item){
                        final /* synthetic */ ItemStack $item;

                        public final void invoke() {
                            Minecraft minecraft = AutoPlay.access$getMc$p$s1046033730();
                            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                            minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(7));
                            int n = 2;
                            boolean bl = false;
                            int n2 = 0;
                            n2 = 0;
                            int n3 = n;
                            while (n2 < n3) {
                                int it = n2++;
                                boolean bl2 = false;
                                Minecraft minecraft2 = AutoPlay.access$getMc$p$s1046033730();
                                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                                minecraft2.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(this.$item));
                            }
                        }
                        {
                            this.$item = itemStack;
                            super(0);
                        }
                    });
                    this.clickState = 1;
                }
            }
            if (this.clickState == 1 && windowId != 0 && StringsKt.equals(itemName, "item.fireworks", true)) {
                Minecraft minecraft = AutoPlay.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C0EPacketClickWindow(windowId, slot, 0, 0, item, 1919));
                Minecraft minecraft2 = AutoPlay.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                minecraft2.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow(windowId));
            }
        } else if (packet instanceof S02PacketChat) {
            IChatComponent iChatComponent = ((S02PacketChat)packet).func_148915_c();
            Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "packet.chatComponent");
            String text = iChatComponent.func_150260_c();
            if (this.clickState == 1) {
                String string = text;
                Intrinsics.checkExpressionValueIsNotNull(string, "text");
                if (StringsKt.contains((CharSequence)string, "    Only VIP players can join full servers!", true)) {
                    new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                    Timer timer = new Timer();
                    long l = 1500L;
                    boolean bl = false;
                    boolean bl2 = false;
                    TimerTask timerTask2 = new TimerTask(){

                        public void run() {
                            TimerTask $this$schedule = this;
                            boolean bl = false;
                            Minecraft minecraft = AutoPlay.access$getMc$p$s1046033730();
                            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                            minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(7));
                            int n = 2;
                            boolean bl2 = false;
                            int n2 = 0;
                            n2 = 0;
                            int n3 = n;
                            while (n2 < n3) {
                                int it = n2++;
                                boolean bl3 = false;
                                Minecraft minecraft2 = AutoPlay.access$getMc$p$s1046033730();
                                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                                minecraft2.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(AutoPlay.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g()));
                            }
                        }
                    };
                    timer.schedule(timerTask2, l);
                }
            }
        }
    }

    private final void queueAutoPlay(Function0<Unit> runnable) {
        String notifyDir = "liquidbounce/notification/";
        if (this.queued) {
            return;
        }
        this.queued = true;
        if (this.getState()) {
            Timer timer = new Timer();
            long l = (long)((Number)this.delayValue.get()).intValue() * (long)1000;
            boolean bl = false;
            boolean bl2 = false;
            TimerTask timerTask2 = new TimerTask(this, runnable){
                final /* synthetic */ AutoPlay this$0;
                final /* synthetic */ Function0 $runnable$inlined;
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
            timer.schedule(timerTask2, l);
            ScaledResolution sr = new ScaledResolution(AutoPlay.access$getMc$p$s1046033730());
            if (((Boolean)this.test1.get()).booleanValue() && !this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                float f = (float)sr.func_78326_a() / 2.0f - (float)Fonts.font35.func_78256_a("Victory!") / 2.0f;
                float f2 = BossStatus.field_82827_c != null && BossStatus.field_82826_b > 0 ? 47 : 30;
                Color color = Color.YELLOW;
                Intrinsics.checkExpressionValueIsNotNull(color, "Color.YELLOW");
                Fonts.font35.func_175065_a("VICTORY!", f, f2, color.getRGB(), true);
                float f3 = (float)sr.func_78326_a() / 2.0f - (float)Fonts.font35.func_78256_a("You were the last man standing!") / 2.0f;
                float f4 = BossStatus.field_82827_c != null && BossStatus.field_82826_b > 0 ? 47 : 60;
                Color color2 = Color.GRAY;
                Intrinsics.checkExpressionValueIsNotNull(color2, "Color.GRAY");
                Fonts.font35.func_175065_a("You were the last man standing!", f3, f4, color2.getRGB(), true);
            }
            new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Sending you to next game in " + ((Number)this.delayValue.get()).intValue() + "seconds...", Notification.Type.INFO));
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.clicking = false;
        this.clickState = 0;
        this.queued = false;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

