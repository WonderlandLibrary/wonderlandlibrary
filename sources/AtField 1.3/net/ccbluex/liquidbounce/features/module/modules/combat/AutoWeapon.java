/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Pair
 *  kotlin.collections.CollectionsKt
 *  kotlin.collections.IntIterator
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.ranges.IntRange
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.ArrayList;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import net.ccbluex.liquidbounce.api.enums.EnchantmentType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.entity.ai.attributes.IAttributeModifier;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
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

@ModuleInfo(name="AutoWeapon", description="Automatically selects the best weapon in your hotbar.", category=ModuleCategory.COMBAT)
public final class AutoWeapon
extends Module {
    private final IntegerValue ticksValue;
    private int spoofedSlot;
    private final BoolValue silentValue = new BoolValue("SpoofItem", false);
    private boolean attackEnemy;

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @EventTarget
    public final void onPacket(PacketEvent var1_1) {
        block17: {
            if (!MinecraftInstance.classProvider.isCPacketUseEntity(var1_1.getPacket())) {
                return;
            }
            v0 = MinecraftInstance.mc.getThePlayer();
            if (v0 == null) {
                return;
            }
            var2_2 = v0;
            var3_3 = var1_1.getPacket().asCPacketUseEntity();
            if (var3_3.getAction() != ICPacketUseEntity.WAction.ATTACK || !this.attackEnemy) break block17;
            this.attackEnemy = false;
            var6_4 = 0;
            var6_5 = (Iterable)new IntRange(var6_4, 8);
            var7_6 = false;
            var8_7 = var6_5;
            var9_8 /* !! */  = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault((Iterable)var6_5, (int)10));
            var10_9 = false;
            var12_12 = var8_7.iterator();
            while (var12_12.hasNext()) {
                var15_19 = var13_15 = ((IntIterator)var12_12).nextInt();
                var17_23 = var9_8 /* !! */ ;
                var16_22 = false;
                var18_24 = new Pair((Object)var15_19, (Object)var2_2.getInventory().getStackInSlot(var15_19));
                var17_23.add(var18_24);
            }
            var6_5 = (List)var9_8 /* !! */ ;
            var7_6 = false;
            var8_7 = var6_5;
            var9_8 /* !! */  = new ArrayList<E>();
            var10_9 = false;
            var12_12 = var8_7.iterator();
            while (var12_12.hasNext()) {
                var13_16 = var12_12.next();
                var15_20 = (Pair)var13_16;
                var16_22 = false;
                if (var15_20.getSecond() == null) ** GOTO lbl-1000
                v1 = (IItemStack)var15_20.getSecond();
                if (MinecraftInstance.classProvider.isItemSword(v1 != null ? v1.getItem() : null)) ** GOTO lbl-1000
                v2 = (IItemStack)var15_20.getSecond();
                if (MinecraftInstance.classProvider.isItemTool(v2 != null ? v2.getItem() : null)) lbl-1000:
                // 2 sources

                {
                    v3 = true;
                } else lbl-1000:
                // 2 sources

                {
                    v3 = false;
                }
                if (!v3) continue;
                var9_8 /* !! */ .add(var13_16);
            }
            var6_5 = (List)var9_8 /* !! */ ;
            var7_6 = false;
            var8_7 = var6_5.iterator();
            if (!var8_7.hasNext()) {
                v4 /* !! */  = null;
            } else {
                var9_8 /* !! */  = var8_7.next();
                if (!var8_7.hasNext()) {
                    v4 /* !! */  = var9_8 /* !! */ ;
                } else {
                    var10_10 = (Pair)var9_8 /* !! */ ;
                    var12_13 = false;
                    v5 = var10_10.getSecond();
                    if (v5 == null) {
                        Intrinsics.throwNpe();
                    }
                    var10_11 = ((IAttributeModifier)CollectionsKt.first((Iterable)((IItemStack)v5).getAttributeModifier("generic.attackDamage"))).getAmount() + 1.25 * (double)ItemUtils.getEnchantment((IItemStack)var10_10.getSecond(), MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS));
                    do {
                        var12_14 = var8_7.next();
                        var13_17 = (Pair)var12_14;
                        var15_21 = false;
                        v6 = var13_17.getSecond();
                        if (v6 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (Double.compare(var10_11, var13_18 = ((IAttributeModifier)CollectionsKt.first((Iterable)((IItemStack)v6).getAttributeModifier("generic.attackDamage"))).getAmount() + 1.25 * (double)ItemUtils.getEnchantment((IItemStack)var13_17.getSecond(), MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS))) >= 0) continue;
                        var9_8 /* !! */  = var12_14;
                        var10_11 = var13_18;
                    } while (var8_7.hasNext());
                    v4 /* !! */  = var9_8 /* !! */ ;
                }
            }
            v7 = (Pair)v4 /* !! */ ;
            if (v7 == null) {
                return;
            }
            var5_25 = v7;
            var4_26 = ((Number)var5_25.component1()).intValue();
            if (var4_26 == var2_2.getInventory().getCurrentItem()) {
                return;
            }
            if (((Boolean)this.silentValue.get()).booleanValue()) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(var4_26));
                this.spoofedSlot = ((Number)this.ticksValue.get()).intValue();
            } else {
                var2_2.getInventory().setCurrentItem(var4_26);
                MinecraftInstance.mc.getPlayerController().updateController();
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(var3_3);
            var1_1.cancelEvent();
        }
    }

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        this.attackEnemy = true;
    }

    public AutoWeapon() {
        this.ticksValue = new IntegerValue("SpoofTicks", 10, 1, 20);
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        if (this.spoofedSlot > 0) {
            if (this.spoofedSlot == 1) {
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(iEntityPlayerSP.getInventory().getCurrentItem()));
            }
            int n = this.spoofedSlot;
            this.spoofedSlot = n + -1;
        }
    }
}

