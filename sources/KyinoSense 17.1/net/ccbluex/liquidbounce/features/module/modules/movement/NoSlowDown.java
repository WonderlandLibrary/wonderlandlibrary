/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.server.S30PacketWindowItems
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.SlowDownEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="NoSlow", category=ModuleCategory.MOVEMENT, description="Prevent you from getting slowed down by items (swords, foods, etc.) and liquids.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010+\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010.2\u0006\u0010/\u001a\u00020\u0018H\u0002J\b\u00100\u001a\u000201H\u0016J\b\u00102\u001a\u000201H\u0016J\u0010\u00103\u001a\u0002012\u0006\u00104\u001a\u000205H\u0007J\u0010\u00106\u001a\u0002012\u0006\u00104\u001a\u000207H\u0007J\u0010\u00108\u001a\u0002012\u0006\u00104\u001a\u000209H\u0007JB\u0010:\u001a\u0002012\u0006\u00104\u001a\u0002052\u0006\u0010;\u001a\u00020\u00182\u0006\u0010<\u001a\u00020\u00182\u0006\u0010=\u001a\u00020\u00182\u0006\u0010>\u001a\u00020$2\u0006\u0010?\u001a\u00020\u00182\b\b\u0002\u0010@\u001a\u00020\u0018H\u0002R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010%\u001a\u0004\u0018\u00010&8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b'\u0010(R\u000e\u0010)\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006A"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/movement/NoSlowDown;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "blinkPackets", "", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "blockForwardMultiplier", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "blockStrafeMultiplier", "bowForwardMultiplier", "bowStrafeMultiplier", "ciucValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "consumeForwardMultiplier", "consumeStrafeMultiplier", "customDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "customOnGround", "customPlace", "customRelease", "debugValue", "displayTag", "fasterDelay", "", "lastOnGround", "lastX", "", "lastY", "lastZ", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "msTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "packetTriggerValue", "placeDelay", "", "tag", "", "getTag", "()Ljava/lang/String;", "testValue", "timer", "getMultiplier", "", "item", "Lnet/minecraft/item/Item;", "isForward", "onDisable", "", "onEnable", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onSlowDown", "Lnet/ccbluex/liquidbounce/event/SlowDownEvent;", "sendPacket", "sendC07", "sendC08", "delay", "delayValue", "onGround", "watchDog", "KyinoClient"})
public final class NoSlowDown
extends Module {
    private final MSTimer msTimer = new MSTimer();
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Watchdog", "OldHypixel", "Blink", "Experimental", "NCP", "NewNCP", "AAC", "AAC5", "Custom"}, "NCP");
    private final FloatValue blockForwardMultiplier = new FloatValue("BlockForwardMultiplier", 1.0f, 0.2f, 1.0f, "x");
    private final FloatValue blockStrafeMultiplier = new FloatValue("BlockStrafeMultiplier", 1.0f, 0.2f, 1.0f, "x");
    private final FloatValue consumeForwardMultiplier = new FloatValue("ConsumeForwardMultiplier", 1.0f, 0.2f, 1.0f, "x");
    private final FloatValue consumeStrafeMultiplier = new FloatValue("ConsumeStrafeMultiplier", 1.0f, 0.2f, 1.0f, "x");
    private final FloatValue bowForwardMultiplier = new FloatValue("BowForwardMultiplier", 1.0f, 0.2f, 1.0f, "x");
    private final FloatValue bowStrafeMultiplier = new FloatValue("BowStrafeMultiplier", 1.0f, 0.2f, 1.0f, "x");
    private final BoolValue customRelease = new BoolValue("CustomReleasePacket", false, new Function0<Boolean>(this){
        final /* synthetic */ NoSlowDown this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)NoSlowDown.access$getModeValue$p(this.this$0).get(), "custom", true);
        }
        {
            this.this$0 = noSlowDown;
            super(0);
        }
    });
    private final BoolValue customPlace = new BoolValue("CustomPlacePacket", false, new Function0<Boolean>(this){
        final /* synthetic */ NoSlowDown this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)NoSlowDown.access$getModeValue$p(this.this$0).get(), "custom", true);
        }
        {
            this.this$0 = noSlowDown;
            super(0);
        }
    });
    private final BoolValue customOnGround = new BoolValue("CustomOnGround", false, new Function0<Boolean>(this){
        final /* synthetic */ NoSlowDown this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)NoSlowDown.access$getModeValue$p(this.this$0).get(), "custom", true);
        }
        {
            this.this$0 = noSlowDown;
            super(0);
        }
    });
    private final IntegerValue customDelayValue = new IntegerValue("CustomDelay", 60, 0, 1000, "ms", new Function0<Boolean>(this){
        final /* synthetic */ NoSlowDown this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)NoSlowDown.access$getModeValue$p(this.this$0).get(), "custom", true);
        }
        {
            this.this$0 = noSlowDown;
            super(0);
        }
    });
    private final BoolValue testValue = new BoolValue("SendPacket", false, new Function0<Boolean>(this){
        final /* synthetic */ NoSlowDown this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)NoSlowDown.access$getModeValue$p(this.this$0).get(), "watchdog", true);
        }
        {
            this.this$0 = noSlowDown;
            super(0);
        }
    });
    private final BoolValue ciucValue = new BoolValue("CheckInUseCount", false, new Function0<Boolean>(this){
        final /* synthetic */ NoSlowDown this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)NoSlowDown.access$getModeValue$p(this.this$0).get(), "blink", true);
        }
        {
            this.this$0 = noSlowDown;
            super(0);
        }
    });
    private final ListValue packetTriggerValue = new ListValue("PacketTrigger", new String[]{"PreRelease", "PostRelease"}, "PostRelease", new Function0<Boolean>(this){
        final /* synthetic */ NoSlowDown this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)NoSlowDown.access$getModeValue$p(this.this$0).get(), "blink", true);
        }
        {
            this.this$0 = noSlowDown;
            super(0);
        }
    });
    private final BoolValue debugValue = new BoolValue("Debug", false, new Function0<Boolean>(this){
        final /* synthetic */ NoSlowDown this$0;

        public final boolean invoke() {
            return StringsKt.equals((String)NoSlowDown.access$getModeValue$p(this.this$0).get(), "watchdog", true) || StringsKt.equals((String)NoSlowDown.access$getModeValue$p(this.this$0).get(), "blink", true);
        }
        {
            this.this$0 = noSlowDown;
            super(0);
        }
    });
    private final BoolValue displayTag = new BoolValue("ArrayList-Tag", true);
    private final List<Packet<INetHandlerPlayServer>> blinkPackets;
    private double lastX;
    private double lastY;
    private double lastZ;
    private boolean lastOnGround;
    private boolean fasterDelay;
    private long placeDelay;
    private final MSTimer timer;

    @Override
    public void onEnable() {
        this.blinkPackets.clear();
        this.msTimer.reset();
    }

    @Override
    public void onDisable() {
        Iterable $this$forEach$iv = this.blinkPackets;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Packet it = (Packet)element$iv;
            boolean bl = false;
            PacketUtils.sendPacketNoEvent(it);
        }
        this.blinkPackets.clear();
    }

    @Override
    @Nullable
    public String getTag() {
        return (Boolean)this.displayTag.get() != false ? (String)this.modeValue.get() : null;
    }

    private final void sendPacket(MotionEvent event, boolean sendC07, boolean sendC08, boolean delay, long delayValue2, boolean onGround, boolean watchDog) {
        C07PacketPlayerDigging digging = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN);
        C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(NoSlowDown.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g());
        C08PacketPlayerBlockPlacement blockMent = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, NoSlowDown.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g(), 0.0f, 0.0f, 0.0f);
        if (onGround && !NoSlowDown.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            return;
        }
        if (sendC07 && event.getEventState() == EventState.PRE) {
            if (delay && this.msTimer.hasTimePassed(delayValue2)) {
                Minecraft minecraft = NoSlowDown.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)digging);
            } else if (!delay) {
                Minecraft minecraft = NoSlowDown.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)digging);
            }
        }
        if (sendC08 && event.getEventState() == EventState.POST) {
            if (delay && this.msTimer.hasTimePassed(delayValue2) && !watchDog) {
                Minecraft minecraft = NoSlowDown.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)blockPlace);
                this.msTimer.reset();
            } else if (!delay && !watchDog) {
                Minecraft minecraft = NoSlowDown.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)blockPlace);
            } else if (watchDog) {
                Minecraft minecraft = NoSlowDown.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)blockMent);
            }
        }
    }

    static /* synthetic */ void sendPacket$default(NoSlowDown noSlowDown, MotionEvent motionEvent, boolean bl, boolean bl2, boolean bl3, long l, boolean bl4, boolean bl5, int n, Object object) {
        if ((n & 0x40) != 0) {
            bl5 = false;
        }
        noSlowDown.sendPacket(motionEvent, bl, bl2, bl3, l, bl4, bl5);
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
        if (module == null) {
            Intrinsics.throwNpe();
        }
        Module killAura = module;
        if (StringsKt.equals((String)this.modeValue.get(), "watchdog", true) && packet instanceof S30PacketWindowItems) {
            EntityPlayerSP entityPlayerSP = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_71039_bw()) {
                event.cancelEvent();
                if (((Boolean)this.debugValue.get()).booleanValue()) {
                    ClientUtils.displayChatMessage("\u00a7c\u00a7l>> detected reset item packet");
                }
            }
        }
        if (StringsKt.equals((String)this.modeValue.get(), "blink", true) && !killAura.getState()) {
            EntityPlayerSP entityPlayerSP = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_71011_bu() != null) {
                EntityPlayerSP entityPlayerSP2 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                ItemStack itemStack = entityPlayerSP2.func_71011_bu();
                Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.itemInUse");
                if (itemStack.func_77973_b() != null) {
                    EntityPlayerSP entityPlayerSP3 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                    ItemStack itemStack2 = entityPlayerSP3.func_71011_bu();
                    Intrinsics.checkExpressionValueIsNotNull(itemStack2, "mc.thePlayer.itemInUse");
                    Item item = itemStack2.func_77973_b();
                    EntityPlayerSP entityPlayerSP4 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
                    if (entityPlayerSP4.func_71039_bw() && (item instanceof ItemFood || item instanceof ItemBucketMilk || item instanceof ItemPotion) && (!((Boolean)this.ciucValue.get()).booleanValue() || NoSlowDown.access$getMc$p$s1046033730().field_71439_g.field_71072_f >= 1)) {
                        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                            if (NoSlowDown.access$getMc$p$s1046033730().field_71439_g.field_175168_bP >= 20 && StringsKt.equals((String)this.packetTriggerValue.get(), "postrelease", true)) {
                                ((C03PacketPlayer)packet).field_149479_a = this.lastX;
                                ((C03PacketPlayer)packet).field_149477_b = this.lastY;
                                ((C03PacketPlayer)packet).field_149478_c = this.lastZ;
                                ((C03PacketPlayer)packet).field_149474_g = this.lastOnGround;
                                if (((Boolean)this.debugValue.get()).booleanValue()) {
                                    ClientUtils.displayChatMessage("\u00a7c\u00a7l>> pos update reached 20");
                                }
                            } else {
                                event.cancelEvent();
                                if (StringsKt.equals((String)this.packetTriggerValue.get(), "postrelease", true)) {
                                    PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer(this.lastOnGround));
                                }
                                this.blinkPackets.add(packet);
                                if (((Boolean)this.debugValue.get()).booleanValue()) {
                                    ClientUtils.displayChatMessage("\u00a7c\u00a7l>> " + "packet player (movement) added at " + (this.blinkPackets.size() - 1));
                                }
                            }
                        } else if (packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
                            event.cancelEvent();
                            if (StringsKt.equals((String)this.packetTriggerValue.get(), "postrelease", true)) {
                                PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer(this.lastOnGround));
                            }
                            this.blinkPackets.add(packet);
                            if (((Boolean)this.debugValue.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("\u00a7c\u00a7l>> " + "packet player (rotation) added at " + (this.blinkPackets.size() - 1));
                            }
                        } else if (packet instanceof C03PacketPlayer && (StringsKt.equals((String)this.packetTriggerValue.get(), "prerelease", true) || ((C03PacketPlayer)packet).field_149474_g != this.lastOnGround)) {
                            event.cancelEvent();
                            this.blinkPackets.add(packet);
                            if (((Boolean)this.debugValue.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("\u00a7c\u00a7l>> " + "packet player (idle) added at " + (this.blinkPackets.size() - 1));
                            }
                        }
                        if (packet instanceof C0BPacketEntityAction) {
                            event.cancelEvent();
                            this.blinkPackets.add(packet);
                            if (((Boolean)this.debugValue.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("\u00a7c\u00a7l>> " + "packet action added at " + (this.blinkPackets.size() - 1));
                            }
                        }
                        if (packet instanceof C07PacketPlayerDigging && StringsKt.equals((String)this.packetTriggerValue.get(), "prerelease", true) && this.blinkPackets.size() > 0) {
                            Iterable $this$forEach$iv = this.blinkPackets;
                            boolean $i$f$forEach = false;
                            for (Object element$iv : $this$forEach$iv) {
                                Packet it = (Packet)element$iv;
                                boolean bl = false;
                                PacketUtils.sendPacketNoEvent(it);
                            }
                            if (((Boolean)this.debugValue.get()).booleanValue()) {
                                ClientUtils.displayChatMessage("\u00a7c\u00a7l>> " + "sent " + this.blinkPackets.size() + " packets.");
                            }
                            this.blinkPackets.clear();
                        }
                    }
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        block48: {
            block49: {
                block46: {
                    block47: {
                        Intrinsics.checkParameterIsNotNull(event, "event");
                        if (!MovementUtils.isMoving() && !StringsKt.equals((String)this.modeValue.get(), "blink", true)) {
                            return;
                        }
                        v0 = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
                        if (v0 == null) {
                            Intrinsics.throwNpe();
                        }
                        killAura = v0;
                        var3_3 = (String)this.modeValue.get();
                        v1 = Locale.getDefault();
                        Intrinsics.checkExpressionValueIsNotNull(v1, "Locale.getDefault()");
                        var4_4 = v1;
                        var5_5 = false;
                        v2 = var3_3;
                        if (v2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        v3 = v2.toLowerCase((Locale)var4_4);
                        Intrinsics.checkExpressionValueIsNotNull(v3, "(this as java.lang.String).toLowerCase(locale)");
                        var3_3 = v3;
                        switch (var3_3.hashCode()) {
                            case 545151501: {
                                if (!var3_3.equals("watchdog")) ** break;
                                break;
                            }
                            case -404562712: {
                                if (!var3_3.equals("experimental")) ** break;
                                break block46;
                            }
                            case 93826908: {
                                if (!var3_3.equals("blink")) ** break;
                                break block47;
                            }
                            case 2986066: {
                                if (!var3_3.equals("aac5")) ** break;
                                if (event.getEventState() != EventState.POST) break block48;
                                v4 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                                Intrinsics.checkExpressionValueIsNotNull(v4, "mc.thePlayer");
                                if (v4.func_71039_bw()) ** GOTO lbl38
                                v5 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                                Intrinsics.checkExpressionValueIsNotNull(v5, "mc.thePlayer");
                                if (!v5.func_70632_aY()) break block48;
lbl38:
                                // 2 sources

                                PacketUtils.sendPacketNoEvent((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, NoSlowDown.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g(), 0.0f, 0.0f, 0.0f));
                                break block48;
                            }
                        }
                        if (((Boolean)this.testValue.get()).booleanValue() && !killAura.getState() && event.getEventState() == EventState.PRE) {
                            v6 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                            Intrinsics.checkExpressionValueIsNotNull(v6, "mc.thePlayer");
                            if (v6.func_71011_bu() != null) {
                                v7 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                                Intrinsics.checkExpressionValueIsNotNull(v7, "mc.thePlayer");
                                v8 = v7.func_71011_bu();
                                Intrinsics.checkExpressionValueIsNotNull(v8, "mc.thePlayer.itemInUse");
                                if (v8.func_77973_b() != null) {
                                    v9 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                                    Intrinsics.checkExpressionValueIsNotNull(v9, "mc.thePlayer");
                                    v10 = v9.func_71011_bu();
                                    Intrinsics.checkExpressionValueIsNotNull(v10, "mc.thePlayer.itemInUse");
                                    item = v10.func_77973_b();
                                    v11 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                                    Intrinsics.checkExpressionValueIsNotNull(v11, "mc.thePlayer");
                                    if (v11.func_71039_bw() && (item instanceof ItemFood || item instanceof ItemBucketMilk || item instanceof ItemPotion) && NoSlowDown.access$getMc$p$s1046033730().field_71439_g.func_71052_bv() >= 1) {
                                        PacketUtils.sendPacketNoEvent((Packet)new C09PacketHeldItemChange(NoSlowDown.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
                                    }
                                }
                            }
                        }
                        break block48;
                    }
                    if (event.getEventState() == EventState.PRE) {
                        v12 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                        Intrinsics.checkExpressionValueIsNotNull(v12, "mc.thePlayer");
                        if (!v12.func_71039_bw()) {
                            v13 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                            Intrinsics.checkExpressionValueIsNotNull(v13, "mc.thePlayer");
                            if (!v13.func_70632_aY()) {
                                this.lastX = event.getX();
                                this.lastY = event.getY();
                                this.lastZ = event.getZ();
                                this.lastOnGround = event.getOnGround();
                                if (this.blinkPackets.size() > 0 && StringsKt.equals((String)this.packetTriggerValue.get(), "postrelease", true)) {
                                    $this$forEach$iv = this.blinkPackets;
                                    $i$f$forEach = false;
                                    for (T element$iv : $this$forEach$iv) {
                                        it = (Packet)element$iv;
                                        $i$a$-forEach-NoSlowDown$onMotion$1 = false;
                                        PacketUtils.sendPacketNoEvent(it);
                                    }
                                    if (((Boolean)this.debugValue.get()).booleanValue()) {
                                        ClientUtils.displayChatMessage("\u00a7c\u00a7l>> " + "sent " + this.blinkPackets.size() + " packets.");
                                    }
                                    this.blinkPackets.clear();
                                }
                            }
                        }
                    }
                    break block48;
                }
                v14 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(v14, "mc.thePlayer");
                if (v14.func_71039_bw()) break block49;
                v15 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(v15, "mc.thePlayer");
                if (!v15.func_70632_aY()) break block48;
            }
            if (this.timer.hasTimePassed(this.placeDelay)) {
                NoSlowDown.access$getMc$p$s1046033730().field_71442_b.func_78750_j();
                v16 = NoSlowDown.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(v16, "mc");
                v16.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
                if (event.getEventState() == EventState.POST) {
                    this.placeDelay = 200L;
                    if (this.fasterDelay) {
                        this.placeDelay = 100L;
                        this.fasterDelay = false;
                    } else {
                        this.fasterDelay = true;
                    }
                    this.timer.reset();
                }
            }
            break block48;
            v17 = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(v17, "mc.thePlayer");
            if (!v17.func_70632_aY()) {
                return;
            }
            var4_4 = (String)this.modeValue.get();
            v18 = Locale.getDefault();
            Intrinsics.checkExpressionValueIsNotNull(v18, "Locale.getDefault()");
            var5_6 = v18;
            var6_8 = false;
            v19 = var4_4;
            if (v19 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v20 = v19.toLowerCase(var5_6);
            Intrinsics.checkExpressionValueIsNotNull(v20, "(this as java.lang.String).toLowerCase(locale)");
            var4_4 = v20;
            tmp = -1;
            switch (var4_4.hashCode()) {
                case 96323: {
                    if (!var4_4.equals("aac")) break;
                    tmp = 1;
                    break;
                }
                case 1594535950: {
                    if (!var4_4.equals("oldhypixel")) break;
                    tmp = 2;
                    break;
                }
                case -1048831045: {
                    if (!var4_4.equals("newncp")) break;
                    tmp = 3;
                    break;
                }
                case -1349088399: {
                    if (!var4_4.equals("custom")) break;
                    tmp = 4;
                    break;
                }
                case 108891: {
                    if (!var4_4.equals("ncp")) break;
                    tmp = 5;
                    break;
                }
            }
            switch (tmp) {
                case 1: {
                    if (NoSlowDown.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 3 == 0) {
                        NoSlowDown.sendPacket$default(this, event, true, false, false, 0L, false, false, 64, null);
                        break;
                    }
                    NoSlowDown.sendPacket$default(this, event, false, true, false, 0L, false, false, 64, null);
                    break;
                }
                case 5: {
                    NoSlowDown.sendPacket$default(this, event, true, true, false, 0L, false, false, 64, null);
                    break;
                }
                case 3: {
                    if (NoSlowDown.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % 2 == 0) {
                        NoSlowDown.sendPacket$default(this, event, true, false, false, 50L, true, false, 64, null);
                        break;
                    }
                    this.sendPacket(event, false, true, false, 0L, true, true);
                    break;
                }
                case 2: {
                    if (event.getEventState() == EventState.PRE) {
                        v21 = NoSlowDown.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(v21, "mc");
                        v21.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                        break;
                    }
                    v22 = NoSlowDown.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(v22, "mc");
                    v22.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
                    break;
                }
                case 4: {
                    NoSlowDown.sendPacket$default(this, event, (Boolean)this.customRelease.get(), (Boolean)this.customPlace.get(), ((Number)this.customDelayValue.get()).intValue() > 0, ((Number)this.customDelayValue.get()).intValue(), (Boolean)this.customOnGround.get(), false, 64, null);
                }
            }
        }
    }

    @EventTarget
    public final void onSlowDown(@NotNull SlowDownEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        EntityPlayerSP entityPlayerSP = NoSlowDown.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        ItemStack itemStack = entityPlayerSP.func_70694_bm();
        Item heldItem = itemStack != null ? itemStack.func_77973_b() : null;
        event.setForward(this.getMultiplier(heldItem, true));
        event.setStrafe(this.getMultiplier(heldItem, false));
    }

    private final float getMultiplier(Item item, boolean isForward) {
        Item item2 = item;
        return item2 instanceof ItemFood || item2 instanceof ItemPotion || item2 instanceof ItemBucketMilk ? (isForward ? ((Number)this.consumeForwardMultiplier.get()).floatValue() : ((Number)this.consumeStrafeMultiplier.get()).floatValue()) : (item2 instanceof ItemSword ? (isForward ? ((Number)this.blockForwardMultiplier.get()).floatValue() : ((Number)this.blockStrafeMultiplier.get()).floatValue()) : (item2 instanceof ItemBow ? (isForward ? ((Number)this.bowForwardMultiplier.get()).floatValue() : ((Number)this.bowStrafeMultiplier.get()).floatValue()) : 0.2f));
    }

    public NoSlowDown() {
        List list;
        NoSlowDown noSlowDown = this;
        boolean bl = false;
        noSlowDown.blinkPackets = list = (List)new ArrayList();
        this.timer = new MSTimer();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ ListValue access$getModeValue$p(NoSlowDown $this) {
        return $this.modeValue;
    }
}

