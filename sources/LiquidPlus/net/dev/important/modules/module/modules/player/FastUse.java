/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MoveEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="FastUse", spacedName="Fast Use", description="Allows you to use items faster.", category=Category.PLAYER, cnName="\u5feb\u901f\u4f7f\u7528")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u0012\u0010\u0016\u001a\u00020\u00152\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0007J\u0010\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u001aH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\u0004\u0018\u00010\u000f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lnet/dev/important/modules/module/modules/player/FastUse;", "Lnet/dev/important/modules/module/Module;", "()V", "customSpeedValue", "Lnet/dev/important/value/IntegerValue;", "customTimer", "Lnet/dev/important/value/FloatValue;", "delayValue", "modeValue", "Lnet/dev/important/value/ListValue;", "msTimer", "Lnet/dev/important/utils/timer/MSTimer;", "noMoveValue", "Lnet/dev/important/value/BoolValue;", "tag", "", "getTag", "()Ljava/lang/String;", "usedTimer", "", "onDisable", "", "onMove", "event", "Lnet/dev/important/event/MoveEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class FastUse
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final BoolValue noMoveValue;
    @NotNull
    private final IntegerValue delayValue;
    @NotNull
    private final IntegerValue customSpeedValue;
    @NotNull
    private final FloatValue customTimer;
    @NotNull
    private final MSTimer msTimer;
    private boolean usedTimer;

    public FastUse() {
        String[] stringArray = new String[]{"Instant", "NCP", "AAC", "CustomDelay", "AACv4_2"};
        this.modeValue = new ListValue("Mode", stringArray, "NCP");
        this.noMoveValue = new BoolValue("NoMove", false);
        this.delayValue = new IntegerValue("CustomDelay", 0, 0, 300, new Function0<Boolean>(this){
            final /* synthetic */ FastUse this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)FastUse.access$getModeValue$p(this.this$0).get(), "customdelay", true);
            }
        });
        this.customSpeedValue = new IntegerValue("CustomSpeed", 2, 0, 35, " packet", new Function0<Boolean>(this){
            final /* synthetic */ FastUse this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)FastUse.access$getModeValue$p(this.this$0).get(), "customdelay", true);
            }
        });
        this.customTimer = new FloatValue("CustomTimer", 1.1f, 0.5f, 2.0f, "x", new Function0<Boolean>(this){
            final /* synthetic */ FastUse this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)FastUse.access$getModeValue$p(this.this$0).get(), "customdelay", true);
            }
        });
        this.msTimer = new MSTimer();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        block26: {
            Intrinsics.checkNotNullParameter(event, "event");
            if (this.usedTimer) {
                MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
                this.usedTimer = false;
            }
            if (!MinecraftInstance.mc.field_71439_g.func_71039_bw()) {
                this.msTimer.reset();
                return;
            }
            Item usingItem = MinecraftInstance.mc.field_71439_g.func_71011_bu().func_77973_b();
            if (!(usingItem instanceof ItemFood) && !(usingItem instanceof ItemBucketMilk) && !(usingItem instanceof ItemPotion)) break block26;
            String string = ((String)this.modeValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            switch (string) {
                case "instant": {
                    int n = 32;
                    int n2 = 0;
                    while (n2 < n) {
                        int n3;
                        int it = n3 = n2++;
                        boolean bl = false;
                        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(MinecraftInstance.mc.field_71439_g.field_70122_E));
                    }
                    MinecraftInstance.mc.field_71442_b.func_78766_c((EntityPlayer)MinecraftInstance.mc.field_71439_g);
                    break;
                }
                case "ncp": {
                    if (MinecraftInstance.mc.field_71439_g.func_71057_bx() <= 14) break;
                    int n = 20;
                    int n4 = 0;
                    while (n4 < n) {
                        int n5;
                        int it = n5 = n4++;
                        boolean bl = false;
                        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(MinecraftInstance.mc.field_71439_g.field_70122_E));
                    }
                    MinecraftInstance.mc.field_71442_b.func_78766_c((EntityPlayer)MinecraftInstance.mc.field_71439_g);
                    break;
                }
                case "aac": {
                    MinecraftInstance.mc.field_71428_T.field_74278_d = 1.1f;
                    this.usedTimer = true;
                    break;
                }
                case "customdelay": {
                    MinecraftInstance.mc.field_71428_T.field_74278_d = ((Number)this.customTimer.get()).floatValue();
                    this.usedTimer = true;
                    if (!this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                        return;
                    }
                    int n = ((Number)this.customSpeedValue.get()).intValue();
                    int n6 = 0;
                    while (n6 < n) {
                        int n7;
                        int it = n7 = n6++;
                        boolean bl = false;
                        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(MinecraftInstance.mc.field_71439_g.field_70122_E));
                    }
                    this.msTimer.reset();
                    break;
                }
                case "aacv4_2": {
                    MinecraftInstance.mc.field_71428_T.field_74278_d = 0.49f;
                    this.usedTimer = true;
                    if (MinecraftInstance.mc.field_71439_g.func_71057_bx() <= 13) break;
                    int n = 23;
                    int n8 = 0;
                    while (n8 < n) {
                        int n9;
                        int it = n9 = n8++;
                        boolean bl = false;
                        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(MinecraftInstance.mc.field_71439_g.field_70122_E));
                    }
                    MinecraftInstance.mc.field_71442_b.func_78766_c((EntityPlayer)MinecraftInstance.mc.field_71439_g);
                }
            }
        }
    }

    @EventTarget
    public final void onMove(@Nullable MoveEvent event) {
        if (event == null) {
            return;
        }
        if (!(this.getState() && MinecraftInstance.mc.field_71439_g.func_71039_bw() && ((Boolean)this.noMoveValue.get()).booleanValue())) {
            return;
        }
        Item usingItem = MinecraftInstance.mc.field_71439_g.func_71011_bu().func_77973_b();
        if (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion) {
            event.zero();
        }
    }

    @Override
    public void onDisable() {
        if (this.usedTimer) {
            MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
            this.usedTimer = false;
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public static final /* synthetic */ ListValue access$getModeValue$p(FastUse $this) {
        return $this.modeValue;
    }
}

