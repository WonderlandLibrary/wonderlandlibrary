/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.server.S30PacketWindowItems
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.movement;

import java.util.Collection;
import java.util.LinkedList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventState;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.SlowDownEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.ListValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import oh.yalan.NativeClass;
import org.jetbrains.annotations.NotNull;

@NativeClass
@Info(name="NoSlow", spacedName="No Slow", description="Prevent you from getting slowed down by items (swords, foods, etc.) and liquids.", category=Category.MOVEMENT, cnName="\u683c\u6321\u65e0\u51cf\u901f")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\u0006\u0010\u001c\u001a\u00020\u0004H\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020!H\u0007J\u0010\u0010\"\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020#H\u0007J\u0010\u0010$\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020%H\u0007JB\u0010&\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020!2\u0006\u0010'\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u00042\u0006\u0010)\u001a\u00020\u00042\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u00042\b\b\u0002\u0010-\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00100\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0012\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\bR\u0014\u0010\u0014\u001a\u00020\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006."}, d2={"Lnet/dev/important/modules/module/modules/movement/NoSlow;", "Lnet/dev/important/modules/module/Module;", "()V", "lastBlockingStat", "", "liquidPushValue", "Lnet/dev/important/value/BoolValue;", "getLiquidPushValue", "()Lnet/dev/important/value/BoolValue;", "modeValue", "Lnet/dev/important/value/ListValue;", "msTimer", "Lnet/dev/important/utils/timer/MSTimer;", "nextTemp", "packetBuf", "Ljava/util/LinkedList;", "Lnet/minecraft/network/Packet;", "Lnet/minecraft/network/play/INetHandlerPlayServer;", "soulsandValue", "getSoulsandValue", "tag", "", "getTag", "()Ljava/lang/String;", "getMultiplier", "", "item", "Lnet/minecraft/item/Item;", "isForward", "onDisable", "", "onMotion", "event", "Lnet/dev/important/event/MotionEvent;", "onPacket", "Lnet/dev/important/event/PacketEvent;", "onSlowDown", "Lnet/dev/important/event/SlowDownEvent;", "sendPacket", "sendC07", "sendC08", "delay", "delayValue", "", "onGround", "watchDog", "LiquidBounce"})
public final class NoSlow
extends Module {
    @NotNull
    private final MSTimer msTimer = new MSTimer();
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final BoolValue soulsandValue;
    @NotNull
    private final BoolValue liquidPushValue;
    private boolean nextTemp;
    private boolean lastBlockingStat;
    @NotNull
    private LinkedList<Packet<INetHandlerPlayServer>> packetBuf;

    public NoSlow() {
        String[] stringArray = new String[]{"Watchdog", "NCP", "AAC", "AAC5", "Vanilla", "Vulcan"};
        this.modeValue = new ListValue("PacketMode", stringArray, "Vanilla");
        this.soulsandValue = new BoolValue("Soulsand", true);
        this.liquidPushValue = new BoolValue("LiquidPush", true);
        this.packetBuf = new LinkedList();
    }

    @NotNull
    public final BoolValue getSoulsandValue() {
        return this.soulsandValue;
    }

    @NotNull
    public final BoolValue getLiquidPushValue() {
        return this.liquidPushValue;
    }

    @Override
    public void onDisable() {
        this.msTimer.reset();
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    private final void sendPacket(MotionEvent event, boolean sendC07, boolean sendC08, boolean delay, long delayValue2, boolean onGround, boolean watchDog) {
        C07PacketPlayerDigging digging = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN);
        C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(MinecraftInstance.mc.field_71439_g.field_71071_by.func_70448_g());
        C08PacketPlayerBlockPlacement blockMent = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, MinecraftInstance.mc.field_71439_g.field_71071_by.func_70448_g(), 0.0f, 0.0f, 0.0f);
        if (onGround && !MinecraftInstance.mc.field_71439_g.field_70122_E) {
            return;
        }
        if (sendC07 && event.getEventState() == EventState.PRE) {
            if (delay && this.msTimer.hasTimePassed(delayValue2)) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)digging);
            } else if (!delay) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)digging);
            }
        }
        if (sendC08 && event.getEventState() == EventState.POST) {
            if (delay && this.msTimer.hasTimePassed(delayValue2) && !watchDog) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)blockPlace);
                this.msTimer.reset();
            } else if (!delay && !watchDog) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)blockPlace);
            } else if (watchDog) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)blockMent);
            }
        }
    }

    static /* synthetic */ void sendPacket$default(NoSlow noSlow, MotionEvent motionEvent, boolean bl, boolean bl2, boolean bl3, long l, boolean bl4, boolean bl5, int n, Object object) {
        if ((n & 0x40) != 0) {
            bl5 = false;
        }
        noSlow.sendPacket(motionEvent, bl, bl2, bl3, l, bl4, bl5);
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (StringsKt.equals((String)this.modeValue.get(), "watchdog", true) && packet instanceof S30PacketWindowItems && (MinecraftInstance.mc.field_71439_g.func_71039_bw() || MinecraftInstance.mc.field_71439_g.func_70632_aY())) {
            event.cancelEvent();
        }
        if (this.modeValue.equals("Vulcan") && (this.lastBlockingStat || MinecraftInstance.mc.field_71439_g.func_70632_aY()) && this.msTimer.hasTimePassed(230L) && this.nextTemp) {
            this.nextTemp = false;
            PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN)));
            if (!((Collection)this.packetBuf).isEmpty()) {
                boolean canAttack = false;
                for (Packet packet2 : this.packetBuf) {
                    if (packet2 instanceof C03PacketPlayer) {
                        canAttack = true;
                    }
                    if ((packet2 instanceof C02PacketUseEntity || packet2 instanceof C0APacketAnimation) && !canAttack) continue;
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)packet2);
                }
                this.packetBuf.clear();
            }
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        block23: {
            KillAura killAura;
            block22: {
                Intrinsics.checkNotNullParameter(event, "event");
                if (!MovementUtils.isMoving()) {
                    return;
                }
                MinecraftInstance.mc.field_71439_g.func_70694_bm();
                Module module2 = Client.INSTANCE.getModuleManager().get(KillAura.class);
                if (module2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.combat.KillAura");
                }
                killAura = (KillAura)module2;
                if (!StringsKt.equals((String)this.modeValue.get(), "aac5", true)) break block22;
                if (event.getEventState() == EventState.POST && (MinecraftInstance.mc.field_71439_g.func_71039_bw() || MinecraftInstance.mc.field_71439_g.func_70632_aY() || killAura.getBlockingStatus())) {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, MinecraftInstance.mc.field_71439_g.field_71071_by.func_70448_g(), 0.0f, 0.0f, 0.0f));
                }
                break block23;
            }
            if (!MinecraftInstance.mc.field_71439_g.func_70632_aY() && !killAura.getBlockingStatus()) {
                return;
            }
            String string = ((String)this.modeValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            switch (string) {
                case "aac": {
                    if (MinecraftInstance.mc.field_71439_g.field_70173_aa % 3 == 0) {
                        NoSlow.sendPacket$default(this, event, true, false, false, 0L, false, false, 64, null);
                        break;
                    }
                    NoSlow.sendPacket$default(this, event, false, true, false, 0L, false, false, 64, null);
                    break;
                }
                case "watchdog": {
                    if (killAura.getState() && killAura.getBlockingStatus() || event.getEventState() != EventState.PRE || MinecraftInstance.mc.field_71439_g.func_71011_bu() == null || MinecraftInstance.mc.field_71439_g.func_71011_bu().func_77973_b() == null) break;
                    Item item = MinecraftInstance.mc.field_71439_g.func_71011_bu().func_77973_b();
                    if (!MinecraftInstance.mc.field_71439_g.func_71039_bw() || !(item instanceof ItemFood) && !(item instanceof ItemBucketMilk) && !(item instanceof ItemPotion) || MinecraftInstance.mc.field_71439_g.func_71052_bv() < 1) break;
                    PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)new C09PacketHeldItemChange(MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c)));
                    break;
                }
                case "ncp": {
                    NoSlow.sendPacket$default(this, event, true, true, false, 0L, false, false, 64, null);
                    break;
                }
                case "hypixel": {
                    if (MinecraftInstance.mc.field_71439_g.field_70173_aa % 2 == 0) {
                        NoSlow.sendPacket$default(this, event, true, false, false, 50L, true, false, 64, null);
                        break;
                    }
                    this.sendPacket(event, false, true, false, 0L, true, true);
                }
            }
        }
    }

    @EventTarget
    public final void onSlowDown(@NotNull SlowDownEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        ItemStack itemStack = MinecraftInstance.mc.field_71439_g.func_70694_bm();
        Item heldItem = itemStack == null ? null : itemStack.func_77973_b();
        event.setForward(this.getMultiplier(heldItem, true));
        event.setStrafe(this.getMultiplier(heldItem, false));
    }

    private final float getMultiplier(Item item, boolean isForward) {
        Item item2 = item;
        return ((item2 instanceof ItemFood ? true : item2 instanceof ItemPotion) ? true : item2 instanceof ItemBucketMilk) ? (isForward ? 1.0f : 1.0f) : (item2 instanceof ItemSword ? (isForward ? 1.0f : 1.0f) : (item2 instanceof ItemBow ? (isForward ? 1.0f : 1.0f) : 0.2f));
    }
}

