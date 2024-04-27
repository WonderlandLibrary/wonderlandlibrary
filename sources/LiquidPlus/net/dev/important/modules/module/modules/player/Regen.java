/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.potion.Potion
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.player;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.NotNull;

@Info(name="Regen", description="Regenerates your health much faster.", category=Category.PLAYER, cnName="\u5feb\u901f\u56de\u590d")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/dev/important/modules/module/modules/player/Regen;", "Lnet/dev/important/modules/module/Module;", "()V", "foodValue", "Lnet/dev/important/value/IntegerValue;", "healthValue", "modeValue", "Lnet/dev/important/value/ListValue;", "noAirValue", "Lnet/dev/important/value/BoolValue;", "potionEffectValue", "resetTimer", "", "speedValue", "onUpdate", "", "event", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class Regen
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final IntegerValue healthValue;
    @NotNull
    private final IntegerValue foodValue;
    @NotNull
    private final IntegerValue speedValue;
    @NotNull
    private final BoolValue noAirValue;
    @NotNull
    private final BoolValue potionEffectValue;
    private boolean resetTimer;

    public Regen() {
        String[] stringArray = new String[]{"Vanilla", "Spartan"};
        this.modeValue = new ListValue("Mode", stringArray, "Vanilla");
        this.healthValue = new IntegerValue("Health", 18, 0, 20);
        this.foodValue = new IntegerValue("Food", 18, 0, 20);
        this.speedValue = new IntegerValue("Speed", 100, 1, 100, "x");
        this.noAirValue = new BoolValue("NoAir", false);
        this.potionEffectValue = new BoolValue("PotionEffect", false);
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.resetTimer) {
            MinecraftInstance.mc.field_71428_T.field_74278_d = 1.0f;
        }
        this.resetTimer = false;
        if ((!((Boolean)this.noAirValue.get()).booleanValue() || MinecraftInstance.mc.field_71439_g.field_70122_E) && !MinecraftInstance.mc.field_71439_g.field_71075_bZ.field_75098_d && MinecraftInstance.mc.field_71439_g.func_71024_bL().func_75116_a() > ((Number)this.foodValue.get()).intValue() && MinecraftInstance.mc.field_71439_g.func_70089_S() && MinecraftInstance.mc.field_71439_g.func_110143_aJ() < (float)((Number)this.healthValue.get()).intValue()) {
            if (((Boolean)this.potionEffectValue.get()).booleanValue() && !MinecraftInstance.mc.field_71439_g.func_70644_a(Potion.field_76428_l)) {
                return;
            }
            String string = ((String)this.modeValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
            String string2 = string;
            if (Intrinsics.areEqual(string2, "vanilla")) {
                int n = ((Number)this.speedValue.get()).intValue();
                int n2 = 0;
                while (n2 < n) {
                    int n3;
                    int it = n3 = n2++;
                    boolean bl = false;
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(MinecraftInstance.mc.field_71439_g.field_70122_E));
                }
            } else if (Intrinsics.areEqual(string2, "spartan")) {
                if (MovementUtils.isMoving() || !MinecraftInstance.mc.field_71439_g.field_70122_E) {
                    return;
                }
                int n = 9;
                int n4 = 0;
                while (n4 < n) {
                    int n5;
                    int it = n5 = n4++;
                    boolean bl = false;
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(MinecraftInstance.mc.field_71439_g.field_70122_E));
                }
                MinecraftInstance.mc.field_71428_T.field_74278_d = 0.45f;
                this.resetTimer = true;
            }
        }
    }
}

