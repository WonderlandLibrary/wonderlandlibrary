/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.InventoryUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="Gapple", description="Eat Gapples.", category=Category.PLAYER, cnName="\u81ea\u52a8\u91d1\u82f9\u679c")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0012\u0010\u0017\u001a\u00020\u00142\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lnet/dev/important/modules/module/modules/player/Gapple;", "Lnet/dev/important/modules/module/Module;", "()V", "delayValue", "Lnet/dev/important/value/IntegerValue;", "healthValue", "Lnet/dev/important/value/FloatValue;", "modeValue", "Lnet/dev/important/value/ListValue;", "getModeValue", "()Lnet/dev/important/value/ListValue;", "noAbsorption", "Lnet/dev/important/value/BoolValue;", "tag", "", "getTag", "()Ljava/lang/String;", "timer", "Lnet/dev/important/utils/timer/MSTimer;", "doEat", "", "warn", "", "onUpdate", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Gapple
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final FloatValue healthValue;
    @NotNull
    private final IntegerValue delayValue;
    @NotNull
    private final BoolValue noAbsorption;
    @NotNull
    private final MSTimer timer;

    public Gapple() {
        String[] stringArray = new String[]{"Auto", "Once", "Head"};
        this.modeValue = new ListValue("Mode", stringArray, "Once");
        this.healthValue = new FloatValue("Health", 10.0f, 1.0f, 20.0f);
        this.delayValue = new IntegerValue("Delay", 150, 0, 1000, "ms");
        this.noAbsorption = new BoolValue("NoAbsorption", true);
        this.timer = new MSTimer();
    }

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        String string = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "once": {
                this.doEat(true);
                this.setState(false);
                break;
            }
            case "auto": {
                if (!this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                    return;
                }
                if (!(MinecraftInstance.mc.field_71439_g.func_110143_aJ() <= ((Number)this.healthValue.get()).floatValue())) break;
                this.doEat(false);
                this.timer.reset();
                break;
            }
            case "head": {
                int headInHotbar;
                if (!this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                    return;
                }
                if (!(MinecraftInstance.mc.field_71439_g.func_110143_aJ() <= ((Number)this.healthValue.get()).floatValue()) || (headInHotbar = InventoryUtils.findItem(36, 45, Items.field_151144_bL)) == -1) break;
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(headInHotbar - 36));
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(MinecraftInstance.mc.field_71439_g.func_70694_bm()));
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c));
                this.timer.reset();
            }
        }
    }

    private final void doEat(boolean warn) {
        float abAmount;
        if (((Boolean)this.noAbsorption.get()).booleanValue() && !warn && (abAmount = MinecraftInstance.mc.field_71439_g.func_110139_bj()) > 0.0f) {
            return;
        }
        int gappleInHotbar = InventoryUtils.findItem(36, 45, Items.field_151153_ao);
        if (gappleInHotbar != -1) {
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(gappleInHotbar - 36));
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(MinecraftInstance.mc.field_71439_g.func_70694_bm()));
            int n = 35;
            int n2 = 0;
            while (n2 < n) {
                int n3;
                int it = n3 = n2++;
                boolean bl = false;
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(MinecraftInstance.mc.field_71439_g.field_70122_E));
            }
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c));
        } else if (warn) {
            Client.INSTANCE.getHud().addNotification(new Notification("No Gapple were found in hotbar", Notification.Type.ERROR));
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

