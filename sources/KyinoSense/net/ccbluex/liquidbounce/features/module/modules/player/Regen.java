/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.FoodStats
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Regen", description="Regenerates your health much faster.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/Regen;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "foodValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "healthValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "noAirValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "potionEffectValue", "resetTimer", "", "speedValue", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Regen
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vanilla", "Spartan"}, "Vanilla");
    private final IntegerValue healthValue = new IntegerValue("Health", 18, 0, 20);
    private final IntegerValue foodValue = new IntegerValue("Food", 18, 0, 20);
    private final IntegerValue speedValue = new IntegerValue("Speed", 100, 1, 100);
    private final BoolValue noAirValue = new BoolValue("NoAir", false);
    private final BoolValue potionEffectValue = new BoolValue("PotionEffect", false);
    private boolean resetTimer;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.resetTimer) {
            Regen.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
        }
        this.resetTimer = false;
        if (((Boolean)this.noAirValue.get()).booleanValue() && !Regen.access$getMc$p$s1046033730().field_71439_g.field_70122_E || Regen.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ.field_75098_d) return;
        EntityPlayerSP entityPlayerSP = Regen.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        FoodStats foodStats = entityPlayerSP.func_71024_bL();
        Intrinsics.checkExpressionValueIsNotNull(foodStats, "mc.thePlayer.foodStats");
        if (foodStats.func_75116_a() <= ((Number)this.foodValue.get()).intValue()) return;
        EntityPlayerSP entityPlayerSP2 = Regen.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
        if (!entityPlayerSP2.func_70089_S()) return;
        EntityPlayerSP entityPlayerSP3 = Regen.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
        if (!(entityPlayerSP3.func_110143_aJ() < ((Number)this.healthValue.get()).floatValue())) return;
        if (((Boolean)this.potionEffectValue.get()).booleanValue() && !Regen.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76428_l)) {
            return;
        }
        String string = (String)this.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case -2011701869: {
                if (!string.equals("spartan")) return;
                break;
            }
            case 233102203: {
                if (!string.equals("vanilla")) return;
                n = ((Number)this.speedValue.get()).intValue();
                boolean bl = false;
                int n2 = 0;
                n2 = 0;
                int n3 = n;
                while (n2 < n3) {
                    int it = n2++;
                    boolean bl2 = false;
                    Minecraft minecraft = Regen.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(Regen.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                }
                return;
            }
        }
        if (MovementUtils.isMoving() || !Regen.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            return;
        }
        n = 9;
        boolean bl = false;
        int n4 = 0;
        n4 = 0;
        int n5 = n;
        while (n4 < n5) {
            int it = n4++;
            boolean bl3 = false;
            Minecraft minecraft = Regen.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(Regen.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
        }
        Regen.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 0.45f;
        this.resetTimer = true;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

