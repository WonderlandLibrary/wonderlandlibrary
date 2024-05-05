/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoSword", description="Automatically selects the best weapon in your hotbar.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0010H\u0007J\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoSword;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "attackEnemy", "", "silentValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "spoofedSlot", "", "ticksValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "update", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AutoSword
extends Module {
    private final BoolValue silentValue = new BoolValue("Spoof Item", false);
    private final IntegerValue ticksValue = new IntegerValue("Spoof Ticks", 10, 1, 20);
    private boolean attackEnemy;
    private int spoofedSlot;

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.attackEnemy = true;
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        block13: {
            Intrinsics.checkParameterIsNotNull(event, "event");
            if (!(event.getPacket() instanceof C02PacketUseEntity) || ((C02PacketUseEntity)event.getPacket()).func_149565_c() != C02PacketUseEntity.Action.ATTACK || !this.attackEnemy) break block13;
            this.attackEnemy = false;
            var4_2 = 0;
            $this$map$iv = new IntRange(var4_2, 8);
            $i$f$map = false;
            var6_5 = $this$map$iv;
            destination$iv$iv = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            $i$f$mapTo = false;
            var10_10 = $this$mapTo$iv$iv.iterator();
            while (var10_10.hasNext()) {
                var13_17 = item$iv$iv = ((IntIterator)var10_10).nextInt();
                var15_21 = destination$iv$iv;
                $i$a$-map-AutoSword$onPacket$1 = false;
                var16_22 = new Pair<Integer, ItemStack>((int)it, AutoSword.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70301_a((int)it));
                var15_21.add(var16_22);
            }
            $this$filter$iv = (List)destination$iv$iv;
            $i$f$filter = false;
            $this$mapTo$iv$iv = $this$filter$iv;
            destination$iv$iv = new ArrayList<E>();
            $i$f$filterTo = false;
            for (T element$iv$iv : $this$filterTo$iv$iv) {
                it = (Pair)element$iv$iv;
                $i$a$-filter-AutoSword$onPacket$2 = false;
                if (it.getSecond() == null) ** GOTO lbl-1000
                v0 = it.getSecond();
                Intrinsics.checkExpressionValueIsNotNull(v0, "it.second");
                if (((ItemStack)v0).func_77973_b() instanceof ItemSword) ** GOTO lbl-1000
                v1 = it.getSecond();
                Intrinsics.checkExpressionValueIsNotNull(v1, "it.second");
                if (((ItemStack)v1).func_77973_b() instanceof ItemTool) lbl-1000:
                // 2 sources

                {
                    v2 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v2 = false;
                }
                if (!v2) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            $this$maxBy$iv = (List)destination$iv$iv;
            $i$f$maxBy = false;
            iterator$iv = $this$maxBy$iv.iterator();
            if (!iterator$iv.hasNext()) {
                v3 = null;
            } else {
                maxElem$iv = iterator$iv.next();
                if (!iterator$iv.hasNext()) {
                    v3 = maxElem$iv;
                } else {
                    it = (Pair)maxElem$iv;
                    $i$a$-maxBy-AutoSword$onPacket$3 = false;
                    v4 = it.getSecond();
                    Intrinsics.checkExpressionValueIsNotNull(v4, "it.second");
                    v5 = ((ItemStack)v4).func_111283_C().get((Object)"generic.attackDamage");
                    Intrinsics.checkExpressionValueIsNotNull(v5, "it.second.attributeModif\u2026s[\"generic.attackDamage\"]");
                    v6 = (AttributeModifier)CollectionsKt.first(v5);
                    maxValue$iv = (v6 != null ? v6.func_111164_d() : 0.0) + 1.25 * (double)ItemUtils.getEnchantment((ItemStack)it.getSecond(), Enchantment.field_180314_l);
                    do {
                        e$iv = iterator$iv.next();
                        it = (Pair)e$iv;
                        $i$a$-maxBy-AutoSword$onPacket$3 = false;
                        v7 = it.getSecond();
                        Intrinsics.checkExpressionValueIsNotNull(v7, "it.second");
                        v8 = ((ItemStack)v7).func_111283_C().get((Object)"generic.attackDamage");
                        Intrinsics.checkExpressionValueIsNotNull(v8, "it.second.attributeModif\u2026s[\"generic.attackDamage\"]");
                        v9 = (AttributeModifier)CollectionsKt.first(v8);
                        v$iv = (v9 != null ? v9.func_111164_d() : 0.0) + 1.25 * (double)ItemUtils.getEnchantment((ItemStack)it.getSecond(), Enchantment.field_180314_l);
                        if (Double.compare(maxValue$iv, v$iv) >= 0) continue;
                        maxElem$iv = e$iv;
                        maxValue$iv = v$iv;
                    } while (iterator$iv.hasNext());
                    v3 = maxElem$iv;
                }
            }
            v10 = v3;
            if (v10 == null) {
                return;
            }
            var3_23 = v10;
            slot = ((Number)var3_23.component1()).intValue();
            if (slot == AutoSword.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c) {
                return;
            }
            if (((Boolean)this.silentValue.get()).booleanValue()) {
                v11 = AutoSword.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(v11, "mc");
                v11.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(slot));
                this.spoofedSlot = ((Number)this.ticksValue.get()).intValue();
            } else {
                AutoSword.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c = slot;
                AutoSword.access$getMc$p$s1046033730().field_71442_b.func_78765_e();
            }
            v12 = AutoSword.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(v12, "mc");
            v12.func_147114_u().func_147297_a(event.getPacket());
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent update) {
        Intrinsics.checkParameterIsNotNull(update, "update");
        if (this.spoofedSlot > 0) {
            if (this.spoofedSlot == 1) {
                Minecraft minecraft = AutoSword.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoSword.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
            }
            int n = this.spoofedSlot;
            this.spoofedSlot = n + -1;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

