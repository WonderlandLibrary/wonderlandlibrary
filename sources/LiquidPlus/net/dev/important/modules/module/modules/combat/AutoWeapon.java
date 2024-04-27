/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
package net.dev.important.modules.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import net.dev.important.event.AttackEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.item.ItemUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.jetbrains.annotations.NotNull;

@Info(name="AutoWeapon", spacedName="Auto Weapon", description="Automatically selects the best weapon in your hotbar.", category=Category.COMBAT)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0010H\u0007J\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2={"Lnet/dev/important/modules/module/modules/combat/AutoWeapon;", "Lnet/dev/important/modules/module/Module;", "()V", "attackEnemy", "", "silentValue", "Lnet/dev/important/value/BoolValue;", "spoofedSlot", "", "ticksValue", "Lnet/dev/important/value/IntegerValue;", "onAttack", "", "event", "Lnet/dev/important/event/AttackEvent;", "onPacket", "Lnet/dev/important/event/PacketEvent;", "onUpdate", "update", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class AutoWeapon
extends Module {
    @NotNull
    private final BoolValue silentValue = new BoolValue("SpoofItem", false);
    @NotNull
    private final IntegerValue ticksValue = new IntegerValue("SpoofTicks", 10, 1, 20);
    private boolean attackEnemy;
    private int spoofedSlot;

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.attackEnemy = true;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.getPacket()).func_149565_c() == C02PacketUseEntity.Action.ATTACK && this.attackEnemy) {
            Object v0;
            void $this$filterTo$iv$iv;
            Iterable $this$mapTo$iv$iv;
            this.attackEnemy = false;
            Iterable $this$map$iv = new IntRange(0, 8);
            boolean $i$f$map = false;
            Iterable iterable = $this$map$iv;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            boolean $i$f$mapTo = false;
            Iterator iterator2 = $this$mapTo$iv$iv.iterator();
            while (iterator2.hasNext()) {
                void it;
                int item$iv$iv;
                int n = item$iv$iv = ((IntIterator)iterator2).nextInt();
                Collection collection = destination$iv$iv;
                boolean bl = false;
                collection.add(new Pair<Integer, ItemStack>((int)it, MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a((int)it)));
            }
            Iterable $this$filter$iv = (List)destination$iv$iv;
            boolean $i$f$filter = false;
            $this$mapTo$iv$iv = $this$filter$iv;
            destination$iv$iv = new ArrayList();
            boolean $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                Pair it2 = (Pair)element$iv$iv;
                boolean bl = false;
                if (!(it2.getSecond() != null && (((ItemStack)it2.getSecond()).func_77973_b() instanceof ItemSword || ((ItemStack)it2.getSecond()).func_77973_b() instanceof ItemTool))) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            Iterable $this$maxByOrNull$iv = (List)destination$iv$iv;
            boolean $i$f$maxByOrNull = false;
            Iterator iterator$iv = $this$maxByOrNull$iv.iterator();
            if (!iterator$iv.hasNext()) {
                v0 = null;
            } else {
                Object maxElem$iv = iterator$iv.next();
                if (!iterator$iv.hasNext()) {
                    v0 = maxElem$iv;
                } else {
                    Pair it = (Pair)maxElem$iv;
                    boolean bl = false;
                    Collection element$iv$iv = ((ItemStack)it.getSecond()).func_111283_C().get((Object)"generic.attackDamage");
                    Intrinsics.checkNotNullExpressionValue(element$iv$iv, "it.second.attributeModif\u2026s[\"generic.attackDamage\"]");
                    AttributeModifier it2 = (AttributeModifier)CollectionsKt.first(element$iv$iv);
                    double maxValue$iv = (it2 == null ? 0.0 : (element$iv$iv = it2.func_111164_d())) + 1.25 * (double)ItemUtils.getEnchantment((ItemStack)it.getSecond(), Enchantment.field_180314_l);
                    do {
                        double d;
                        Object e$iv = iterator$iv.next();
                        Pair it3 = (Pair)e$iv;
                        $i$a$-maxByOrNull-AutoWeapon$onPacket$3 = false;
                        Collection collection = ((ItemStack)it3.getSecond()).func_111283_C().get((Object)"generic.attackDamage");
                        Intrinsics.checkNotNullExpressionValue(collection, "it.second.attributeModif\u2026s[\"generic.attackDamage\"]");
                        AttributeModifier attributeModifier = (AttributeModifier)CollectionsKt.first(collection);
                        double v$iv = (attributeModifier == null ? 0.0 : (d = attributeModifier.func_111164_d())) + 1.25 * (double)ItemUtils.getEnchantment((ItemStack)it3.getSecond(), Enchantment.field_180314_l);
                        if (Double.compare(maxValue$iv, v$iv) >= 0) continue;
                        maxElem$iv = e$iv;
                        maxValue$iv = v$iv;
                    } while (iterator$iv.hasNext());
                    v0 = maxElem$iv;
                }
            }
            Pair pair = v0;
            if (pair == null) {
                return;
            }
            int slot = ((Number)pair.component1()).intValue();
            if (slot == MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c) {
                return;
            }
            if (((Boolean)this.silentValue.get()).booleanValue()) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(slot));
                this.spoofedSlot = ((Number)this.ticksValue.get()).intValue();
            } else {
                MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                MinecraftInstance.mc.field_71442_b.func_78765_e();
            }
            MinecraftInstance.mc.func_147114_u().func_147297_a(event.getPacket());
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent update) {
        Intrinsics.checkNotNullParameter(update, "update");
        if (this.spoofedSlot > 0) {
            if (this.spoofedSlot == 1) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c));
            }
            int n = this.spoofedSlot;
            this.spoofedSlot = n + -1;
        }
    }
}

