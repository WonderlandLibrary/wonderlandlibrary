/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="FastUse", category=ModuleCategory.PLAYER, description="Allows you to use items faster.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0017J\u0012\u0010\u0016\u001a\u00020\u00152\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0007J\u0010\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u001aH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\u0004\u0018\u00010\u000f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/FastUse;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "customSpeedValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "customTimer", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "delayValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "msTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "noMoveValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "tag", "", "getTag", "()Ljava/lang/String;", "usedTimer", "", "onDisable", "", "onMove", "event", "Lnet/ccbluex/liquidbounce/event/MoveEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class FastUse
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Instant", "Vanilla", "NCP", "Packet", "AAC", "AAC5.2.0", "CustomDelay", "AAC4.4.0", "Redesky", "HuaYuTing"}, "NCP");
    private final BoolValue noMoveValue = new BoolValue("NoMove", false);
    private final IntegerValue delayValue = new IntegerValue("CustomDelay", 0, 0, 300);
    private final IntegerValue customSpeedValue = new IntegerValue("CustomSpeed", 2, 0, 35);
    private final FloatValue customTimer = new FloatValue("CustomTimer", 1.1f, 0.5f, 2.0f);
    private final MSTimer msTimer = new MSTimer();
    private boolean usedTimer;

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.usedTimer) {
            FastUse.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
            this.usedTimer = false;
        }
        EntityPlayerSP entityPlayerSP = FastUse.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (!entityPlayerSP.func_71039_bw()) {
            this.msTimer.reset();
            return;
        }
        EntityPlayerSP entityPlayerSP2 = FastUse.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        ItemStack itemStack = entityPlayerSP2.func_71011_bu();
        Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.itemInUse");
        Item usingItem = itemStack.func_77973_b();
        if (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion) {
            String string = (String)this.modeValue.get();
            int n = 0;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "instant": {
                    n = 35;
                    boolean bl = false;
                    int n2 = 0;
                    n2 = 0;
                    int n3 = n;
                    while (n2 < n3) {
                        int it = n2++;
                        boolean bl2 = false;
                        Minecraft minecraft = FastUse.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(FastUse.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                    }
                    FastUse.access$getMc$p$s1046033730().field_71442_b.func_78766_c((EntityPlayer)FastUse.access$getMc$p$s1046033730().field_71439_g);
                    break;
                }
                case "vanilla": {
                    n = 35;
                    boolean bl = false;
                    int n4 = 0;
                    n4 = 0;
                    int n5 = n;
                    while (n4 < n5) {
                        int it = n4++;
                        boolean bl3 = false;
                        Minecraft minecraft = FastUse.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(FastUse.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                    }
                    FastUse.access$getMc$p$s1046033730().field_71442_b.func_78766_c((EntityPlayer)FastUse.access$getMc$p$s1046033730().field_71439_g);
                    break;
                }
                case "ncp": {
                    EntityPlayerSP entityPlayerSP3 = FastUse.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                    if (entityPlayerSP3.func_71057_bx() <= 14) break;
                    n = 20;
                    boolean bl = false;
                    int n6 = 0;
                    n6 = 0;
                    int n7 = n;
                    while (n6 < n7) {
                        int it = n6++;
                        boolean bl4 = false;
                        Minecraft minecraft = FastUse.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(FastUse.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                    }
                    FastUse.access$getMc$p$s1046033730().field_71442_b.func_78766_c((EntityPlayer)FastUse.access$getMc$p$s1046033730().field_71439_g);
                    break;
                }
                case "packet": {
                    n = 35;
                    boolean bl = false;
                    int n8 = 0;
                    n8 = 0;
                    int n9 = n;
                    while (n8 < n9) {
                        int it = n8++;
                        boolean bl5 = false;
                        Minecraft minecraft = FastUse.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(FastUse.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                    }
                    FastUse.access$getMc$p$s1046033730().field_71442_b.func_78766_c((EntityPlayer)FastUse.access$getMc$p$s1046033730().field_71439_g);
                    break;
                }
                case "aac": {
                    FastUse.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.1f;
                    this.usedTimer = true;
                    break;
                }
                case "aac5.2.0": {
                    FastUse.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.1f;
                    this.usedTimer = true;
                    break;
                }
                case "customdelay": {
                    FastUse.access$getMc$p$s1046033730().field_71428_T.field_74278_d = ((Number)this.customTimer.get()).floatValue();
                    this.usedTimer = true;
                    if (!this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                        return;
                    }
                    n = ((Number)this.customSpeedValue.get()).intValue();
                    boolean bl = false;
                    int n10 = 0;
                    n10 = 0;
                    int n11 = n;
                    while (n10 < n11) {
                        int it = n10++;
                        boolean bl6 = false;
                        Minecraft minecraft = FastUse.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(FastUse.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                    }
                    this.msTimer.reset();
                    break;
                }
                case "aac4.4.0": {
                    FastUse.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.49f;
                    this.usedTimer = true;
                    EntityPlayerSP entityPlayerSP4 = FastUse.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc.thePlayer");
                    if (entityPlayerSP4.func_71057_bx() <= 13) break;
                    n = 23;
                    boolean bl = false;
                    int n12 = 0;
                    n12 = 0;
                    int n13 = n;
                    while (n12 < n13) {
                        int it = n12++;
                        boolean bl7 = false;
                        Minecraft minecraft = FastUse.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(FastUse.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                    }
                    FastUse.access$getMc$p$s1046033730().field_71442_b.func_78766_c((EntityPlayer)FastUse.access$getMc$p$s1046033730().field_71439_g);
                    break;
                }
                case "redesky": {
                    FastUse.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.49f;
                    this.usedTimer = true;
                    EntityPlayerSP entityPlayerSP5 = FastUse.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP5, "mc.thePlayer");
                    if (entityPlayerSP5.func_71057_bx() <= 13) break;
                    n = 23;
                    boolean bl = false;
                    int n14 = 0;
                    n14 = 0;
                    int n15 = n;
                    while (n14 < n15) {
                        int it = n14++;
                        boolean bl8 = false;
                        Minecraft minecraft = FastUse.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(FastUse.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                    }
                    FastUse.access$getMc$p$s1046033730().field_71442_b.func_78766_c((EntityPlayer)FastUse.access$getMc$p$s1046033730().field_71439_g);
                    break;
                }
                case "huayuting": {
                    FastUse.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.49f;
                    this.usedTimer = true;
                    n = 2;
                    boolean bl = false;
                    int n16 = 0;
                    n16 = 0;
                    int n17 = n;
                    while (n16 < n17) {
                        int it = n16++;
                        boolean bl9 = false;
                        Minecraft minecraft = FastUse.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(FastUse.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                    }
                    break;
                }
            }
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onMove(@Nullable MoveEvent event) {
        block6: {
            block5: {
                if (event == null) {
                    return;
                }
                if (!this.getState()) break block5;
                EntityPlayerSP entityPlayerSP = FastUse.access$getMc$p$s1046033730().field_71439_g;
                Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                if (entityPlayerSP.func_71039_bw() && ((Boolean)this.noMoveValue.get()).booleanValue()) break block6;
            }
            return;
        }
        EntityPlayerSP entityPlayerSP = FastUse.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        ItemStack itemStack = entityPlayerSP.func_71011_bu();
        Intrinsics.checkExpressionValueIsNotNull(itemStack, "mc.thePlayer.itemInUse");
        Item usingItem = itemStack.func_77973_b();
        if (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion) {
            event.zero();
        }
    }

    @Override
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public void onDisable() {
        if (this.usedTimer) {
            FastUse.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
            this.usedTimer = false;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case -1777040898: {
                if (!string.equals("huayuting")) break;
                FastUse.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
            }
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

