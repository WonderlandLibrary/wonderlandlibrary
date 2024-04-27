package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoWeapon", description="Automatically selects the best weapon in your hotbar.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\n\n\b\n\n\u0000\n\n\u0000\n\b\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\b\u000020B¢J0\f2\r0HJ0\f2\r0HJ0\f20HR0X¢\n\u0000R0X¢\n\u0000R0\bX¢\n\u0000R\t0\nX¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoWeapon;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "attackEnemy", "", "silentValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "spoofedSlot", "", "ticksValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "update", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class AutoWeapon
extends Module {
    private final BoolValue silentValue = new BoolValue("SpoofItem", false);
    private final IntegerValue ticksValue = new IntegerValue("SpoofTicks", 10, 1, 20);
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
        block17: {
            Intrinsics.checkParameterIsNotNull(event, "event");
            if (!MinecraftInstance.classProvider.isCPacketUseEntity(event.getPacket())) {
                return;
            }
            v0 = MinecraftInstance.mc.getThePlayer();
            if (v0 == null) {
                return;
            }
            thePlayer = v0;
            packet = event.getPacket().asCPacketUseEntity();
            if (packet.getAction() != ICPacketUseEntity.WAction.ATTACK || !this.attackEnemy) break block17;
            this.attackEnemy = false;
            var6_4 = 0;
            $this$map$iv = new IntRange(var6_4, 8);
            $i$f$map = false;
            var8_7 = $this$map$iv;
            destination$iv$iv = new ArrayList<E>(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            $i$f$mapTo = false;
            var12_12 = $this$mapTo$iv$iv.iterator();
            while (var12_12.hasNext()) {
                var15_19 = item$iv$iv = ((IntIterator)var12_12).nextInt();
                var17_23 = destination$iv$iv;
                $i$a$-map-AutoWeapon$onPacket$1 = false;
                var18_24 = new Pair<Integer, IItemStack>((int)it, thePlayer.getInventory().getStackInSlot((int)it));
                var17_23.add(var18_24);
            }
            $this$filter$iv = (List)destination$iv$iv;
            $i$f$filter = false;
            $this$mapTo$iv$iv = $this$filter$iv;
            destination$iv$iv = new ArrayList<E>();
            $i$f$filterTo = false;
            for (T element$iv$iv : $this$filterTo$iv$iv) {
                it = (Pair)element$iv$iv;
                $i$a$-filter-AutoWeapon$onPacket$2 = false;
                if (it.getSecond() == null) ** GOTO lbl-1000
                v1 = (IItemStack)it.getSecond();
                if (MinecraftInstance.classProvider.isItemSword(v1 != null ? v1.getItem() : null)) ** GOTO lbl-1000
                v2 = (IItemStack)it.getSecond();
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
                destination$iv$iv.add(element$iv$iv);
            }
            $this$maxBy$iv = (List)destination$iv$iv;
            $i$f$maxBy = false;
            iterator$iv = $this$maxBy$iv.iterator();
            if (!iterator$iv.hasNext()) {
                v4 = null;
            } else {
                maxElem$iv = iterator$iv.next();
                if (!iterator$iv.hasNext()) {
                    v4 = maxElem$iv;
                } else {
                    it = (Pair)maxElem$iv;
                    $i$a$-maxBy-AutoWeapon$onPacket$3 = false;
                    v5 = it.getSecond();
                    if (v5 == null) {
                        Intrinsics.throwNpe();
                    }
                    maxValue$iv = ((IAttributeModifier)CollectionsKt.first((Iterable)((IItemStack)v5).getAttributeModifier("generic.attackDamage"))).getAmount() + 1.25 * (double)ItemUtils.getEnchantment((IItemStack)it.getSecond(), MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS));
                    do {
                        e$iv = iterator$iv.next();
                        it = (Pair)e$iv;
                        $i$a$-maxBy-AutoWeapon$onPacket$3 = false;
                        v6 = it.getSecond();
                        if (v6 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (Double.compare(maxValue$iv, v$iv = ((IAttributeModifier)CollectionsKt.first((Iterable)((IItemStack)v6).getAttributeModifier("generic.attackDamage"))).getAmount() + 1.25 * (double)ItemUtils.getEnchantment((IItemStack)it.getSecond(), MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS))) >= 0) continue;
                        maxElem$iv = e$iv;
                        maxValue$iv = v$iv;
                    } while (iterator$iv.hasNext());
                    v4 = maxElem$iv;
                }
            }
            v7 = v4;
            if (v7 == null) {
                return;
            }
            var5_25 = v7;
            slot = ((Number)var5_25.component1()).intValue();
            if (slot == thePlayer.getInventory().getCurrentItem()) {
                return;
            }
            if (((Boolean)this.silentValue.get()).booleanValue()) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketHeldItemChange(slot));
                this.spoofedSlot = ((Number)this.ticksValue.get()).intValue();
            } else {
                thePlayer.getInventory().setCurrentItem(slot);
                MinecraftInstance.mc.getPlayerController().updateController();
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(packet);
            event.cancelEvent();
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent update) {
        Intrinsics.checkParameterIsNotNull(update, "update");
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
