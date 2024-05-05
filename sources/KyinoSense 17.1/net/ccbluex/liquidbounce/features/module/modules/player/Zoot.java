/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="Zoot", description="Removes all bad potion effects/fire.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/Zoot;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "badEffectsValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "fireValue", "noAirValue", "hasBadEffect", "", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class Zoot
extends Module {
    private final BoolValue badEffectsValue = new BoolValue("BadEffects", true);
    private final BoolValue fireValue = new BoolValue("Fire", true);
    private final BoolValue noAirValue = new BoolValue("NoAir", false);

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        int n;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.noAirValue.get()).booleanValue() && !Zoot.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
            return;
        }
        if (((Boolean)this.badEffectsValue.get()).booleanValue()) {
            Object v2;
            EntityPlayerSP entityPlayerSP = Zoot.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            Collection collection = entityPlayerSP.func_70651_bq();
            Intrinsics.checkExpressionValueIsNotNull(collection, "mc.thePlayer.activePotionEffects");
            Iterable $this$maxBy$iv = collection;
            boolean $i$f$maxBy = false;
            Iterator iterator$iv = $this$maxBy$iv.iterator();
            if (!iterator$iv.hasNext()) {
                v2 = null;
            } else {
                Object maxElem$iv = iterator$iv.next();
                if (!iterator$iv.hasNext()) {
                    v2 = maxElem$iv;
                } else {
                    PotionEffect it = (PotionEffect)maxElem$iv;
                    boolean bl = false;
                    PotionEffect potionEffect = it;
                    Intrinsics.checkExpressionValueIsNotNull(potionEffect, "it");
                    int maxValue$iv = potionEffect.func_76459_b();
                    do {
                        Object e$iv = iterator$iv.next();
                        PotionEffect it2 = (PotionEffect)e$iv;
                        $i$a$-maxBy-Zoot$onUpdate$effect$1 = false;
                        PotionEffect potionEffect2 = it2;
                        Intrinsics.checkExpressionValueIsNotNull(potionEffect2, "it");
                        int v$iv = potionEffect2.func_76459_b();
                        if (maxValue$iv >= v$iv) continue;
                        maxElem$iv = e$iv;
                        maxValue$iv = v$iv;
                    } while (iterator$iv.hasNext());
                    v2 = maxElem$iv;
                }
            }
            PotionEffect effect = v2;
            if (effect != null) {
                int n2 = effect.func_76459_b() / 20;
                n = 0;
                int n3 = 0;
                n3 = 0;
                int maxElem$iv = n2;
                while (n3 < maxElem$iv) {
                    int it = n3++;
                    boolean bl = false;
                    Minecraft minecraft = Zoot.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(Zoot.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                }
            }
        }
        if (((Boolean)this.fireValue.get()).booleanValue() && !Zoot.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ.field_75098_d) {
            EntityPlayerSP entityPlayerSP = Zoot.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (entityPlayerSP.func_70027_ad()) {
                int n4 = 9;
                boolean bl = false;
                n = 0;
                n = 0;
                int n5 = n4;
                while (n < n5) {
                    int it = n++;
                    boolean bl2 = false;
                    Minecraft minecraft = Zoot.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer(Zoot.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                }
            }
        }
    }

    private final boolean hasBadEffect() {
        return Zoot.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76438_s) || Zoot.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76421_d) || Zoot.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76419_f) || Zoot.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76433_i) || Zoot.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76431_k) || Zoot.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76440_q) || Zoot.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76437_t) || Zoot.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_82731_v) || Zoot.access$getMc$p$s1046033730().field_71439_g.func_70644_a(Potion.field_76436_u);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

