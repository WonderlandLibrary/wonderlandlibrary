/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C16PacketClientStatus
 *  net.minecraft.network.play.client.C16PacketClientStatus$EnumState
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.implementations.IItemStack;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.item.ArmorComparator;
import net.ccbluex.liquidbounce.utils.item.ArmorPiece;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

@ModuleInfo(name="AutoArmor", description="Automatically equips the best armor in your inventory.", category=ModuleCategory.COMBAT)
public class AutoArmor
extends Module {
    public static final ArmorComparator ARMOR_COMPARATOR = new ArmorComparator();
    private final BoolValue invOpenValue = new BoolValue("InvOpen", false);
    private final BoolValue simulateInventory = new BoolValue("SimulateInventory", false);
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 14, 0, 1000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int minDelay = (Integer)AutoArmor.this.minDelayValue.get();
            if (minDelay > newValue) {
                this.set(minDelay);
            }
        }
    };
    private final BoolValue noMoveValue = new BoolValue("NoMove", false);
    private final IntegerValue itemDelayValue = new IntegerValue("ItemDelay", 300, 0, 5000);
    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 10, 0, 1000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int maxDelay = (Integer)AutoArmor.this.maxDelayValue.get();
            if (maxDelay < newValue) {
                this.set(maxDelay);
            }
        }
    };
    private final BoolValue hotbarValue = new BoolValue("Hotbar", false);
    private long delay;

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (!InventoryUtils.CLICK_TIMER.hasTimePassed(this.delay) || AutoArmor.mc.field_71439_g.field_71070_bA != null && AutoArmor.mc.field_71439_g.field_71070_bA.field_75152_c != 0) {
            return;
        }
        Map<Integer, List<ArmorPiece>> armorPieces = IntStream.range(0, 36).filter(i -> {
            ItemStack itemStack = AutoArmor.mc.field_71439_g.field_71071_by.func_70301_a(i);
            return itemStack != null && itemStack.func_77973_b() instanceof ItemArmor && (i < 9 || System.currentTimeMillis() - ((IItemStack)itemStack).getItemDelay() >= (long)((Integer)this.itemDelayValue.get()).intValue());
        }).mapToObj(i -> new ArmorPiece(AutoArmor.mc.field_71439_g.field_71071_by.func_70301_a(i), i)).collect(Collectors.groupingBy(ArmorPiece::getArmorType));
        ArmorPiece[] bestArmor = new ArmorPiece[4];
        for (Map.Entry<Integer, List<ArmorPiece>> armorEntry : armorPieces.entrySet()) {
            bestArmor[armorEntry.getKey().intValue()] = armorEntry.getValue().stream().max(ARMOR_COMPARATOR).orElse(null);
        }
        for (int i2 = 0; i2 < 4; ++i2) {
            int armorSlot;
            ArmorPiece oldArmor;
            ArmorPiece armorPiece = bestArmor[i2];
            if (armorPiece == null || (oldArmor = new ArmorPiece(AutoArmor.mc.field_71439_g.field_71071_by.func_70440_f(armorSlot = 3 - i2), -1)).getItemStack() != null && oldArmor.getItemStack().func_77973_b() instanceof ItemArmor && ARMOR_COMPARATOR.compare(oldArmor, armorPiece) >= 0) continue;
            if (oldArmor.getItemStack() != null && this.move(8 - armorSlot, true)) {
                return;
            }
            if (AutoArmor.mc.field_71439_g.field_71071_by.func_70440_f(armorSlot) != null || !this.move(armorPiece.getSlot(), false)) continue;
            return;
        }
    }

    private boolean move(int item, boolean isArmorSlot) {
        if (!isArmorSlot && item < 9 && ((Boolean)this.hotbarValue.get()).booleanValue() && !(AutoArmor.mc.field_71462_r instanceof GuiInventory)) {
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(item));
            mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(item).func_75211_c()));
            mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(AutoArmor.mc.field_71439_g.field_71071_by.field_70461_c));
            this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            return true;
        }
        if (!(((Boolean)this.noMoveValue.get()).booleanValue() && MovementUtils.isMoving() || ((Boolean)this.invOpenValue.get()).booleanValue() && !(AutoArmor.mc.field_71462_r instanceof GuiInventory) || item == -1)) {
            boolean openInventory;
            boolean bl = openInventory = (Boolean)this.simulateInventory.get() != false && !(AutoArmor.mc.field_71462_r instanceof GuiInventory);
            if (openInventory) {
                mc.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            }
            AutoArmor.mc.field_71442_b.func_78753_a(AutoArmor.mc.field_71439_g.field_71069_bz.field_75152_c, isArmorSlot ? item : (item < 9 ? item + 36 : item), 0, 1, (EntityPlayer)AutoArmor.mc.field_71439_g);
            this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            if (openInventory) {
                mc.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
            }
            return true;
        }
        return false;
    }
}

