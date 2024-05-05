/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.CrossVersionUtilsKt;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.item.ArmorComparator;
import net.ccbluex.liquidbounce.utils.item.ArmorPiece;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="AutoArmor", description="Automatically equips the best armor in your inventory.", category=ModuleCategory.COMBAT)
public class AutoArmor
extends Module {
    public static final ArmorComparator ARMOR_COMPARATOR = new ArmorComparator();
    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 100, 0, 400){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int maxDelay = (Integer)AutoArmor.this.maxDelayValue.get();
            if (maxDelay < newValue) {
                this.set(maxDelay);
            }
        }
    };
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 200, 0, 400){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int minDelay = (Integer)AutoArmor.this.minDelayValue.get();
            if (minDelay > newValue) {
                this.set(minDelay);
            }
        }
    };
    private final BoolValue invOpenValue = new BoolValue("InvOpen", false);
    private final BoolValue simulateInventory = new BoolValue("SimulateInventory", true);
    private final BoolValue noMoveValue = new BoolValue("NoMove", false);
    private final IntegerValue itemDelayValue = new IntegerValue("ItemDelay", 0, 0, 5000);
    private final BoolValue hotbarValue = new BoolValue("Hotbar", true);
    private long delay;
    private boolean locked = false;

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (!InventoryUtils.CLICK_TIMER.hasTimePassed(this.delay) || mc.getThePlayer() == null || mc.getThePlayer().getOpenContainer() != null && mc.getThePlayer().getOpenContainer().getWindowId() != 0) {
            return;
        }
        Map<Integer, List<ArmorPiece>> armorPieces = IntStream.range(0, 36).filter(i -> {
            IItemStack itemStack = mc.getThePlayer().getInventory().getStackInSlot(i);
            return itemStack != null && classProvider.isItemArmor(itemStack.getItem()) && (i < 9 || System.currentTimeMillis() - itemStack.getItemDelay() >= (long)((Integer)this.itemDelayValue.get()).intValue());
        }).mapToObj(i -> new ArmorPiece(mc.getThePlayer().getInventory().getStackInSlot(i), i)).collect(Collectors.groupingBy(ArmorPiece::getArmorType));
        ArmorPiece[] bestArmor = new ArmorPiece[4];
        for (Map.Entry<Integer, List<ArmorPiece>> armorEntry : armorPieces.entrySet()) {
            bestArmor[armorEntry.getKey().intValue()] = armorEntry.getValue().stream().max(ARMOR_COMPARATOR).orElse(null);
        }
        for (int i2 = 0; i2 < 4; ++i2) {
            ArmorPiece armorPiece = bestArmor[i2];
            if (armorPiece == null) continue;
            int armorSlot = 3 - i2;
            ArmorPiece oldArmor = new ArmorPiece(mc.getThePlayer().getInventory().armorItemInSlot(armorSlot), -1);
            if (!ItemUtils.isStackEmpty(oldArmor.getItemStack()) && classProvider.isItemArmor(oldArmor.getItemStack().getItem()) && ARMOR_COMPARATOR.compare(oldArmor, armorPiece) >= 0) continue;
            if (!ItemUtils.isStackEmpty(oldArmor.getItemStack()) && this.move(8 - (3 - armorSlot), true)) {
                this.locked = true;
                return;
            }
            if (!ItemUtils.isStackEmpty(mc.getThePlayer().getInventory().armorItemInSlot(armorSlot)) || !this.move(armorPiece.getSlot(), false)) continue;
            this.locked = true;
            return;
        }
        this.locked = false;
    }

    public boolean isLocked() {
        return this.getState() && this.locked;
    }

    private boolean move(int item, boolean isArmorSlot) {
        if (!isArmorSlot && item < 9 && ((Boolean)this.hotbarValue.get()).booleanValue() && !classProvider.isGuiInventory(mc.getCurrentScreen())) {
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(item));
            mc.getNetHandler().addToSendQueue(CrossVersionUtilsKt.createUseItemPacket(mc.getThePlayer().getInventoryContainer().getSlot(item).getStack(), WEnumHand.MAIN_HAND));
            mc.getNetHandler().addToSendQueue(classProvider.createCPacketHeldItemChange(mc.getThePlayer().getInventory().getCurrentItem()));
            this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            return true;
        }
        if (!(((Boolean)this.noMoveValue.get()).booleanValue() && MovementUtils.isMoving() || ((Boolean)this.invOpenValue.get()).booleanValue() && !classProvider.isGuiInventory(mc.getCurrentScreen()) || item == -1)) {
            boolean full;
            boolean openInventory;
            boolean bl = openInventory = (Boolean)this.simulateInventory.get() != false && !classProvider.isGuiInventory(mc.getCurrentScreen());
            if (openInventory) {
                mc.getNetHandler().addToSendQueue(CrossVersionUtilsKt.createOpenInventoryPacket());
            }
            if (full = isArmorSlot) {
                for (IItemStack iItemStack : mc.getThePlayer().getInventory().getMainInventory()) {
                    if (!ItemUtils.isStackEmpty(iItemStack)) continue;
                    full = false;
                    break;
                }
            }
            if (full) {
                mc.getPlayerController().windowClick(mc.getThePlayer().getInventoryContainer().getWindowId(), item, 1, 4, mc.getThePlayer());
            } else {
                mc.getPlayerController().windowClick(mc.getThePlayer().getInventoryContainer().getWindowId(), isArmorSlot ? item : (item < 9 ? item + 36 : item), 0, 1, mc.getThePlayer());
            }
            this.delay = TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get());
            if (openInventory) {
                mc.getNetHandler().addToSendQueue(classProvider.createCPacketCloseWindow());
            }
            return true;
        }
        return false;
    }
}

