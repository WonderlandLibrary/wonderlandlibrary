package client.module.impl.player;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.Render3DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.MoveUtil;
import client.util.liquidbounce.*;
import client.value.impl.BooleanValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.minecraft.network.play.client.C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT;

@ModuleInfo(name = "Auto Armor", description = "", category = Category.PLAYER)
public class AutoArmor extends Module {

    public static final ArmorComparator ARMOR_COMPARATOR = new ArmorComparator();
    private final int maxDelay = 50, minDelay = 25;
    private final BooleanValue invOpenValue = new BooleanValue("InvOpen", this, false), simulateInventory = new BooleanValue("SimulateInventory", this, true, () -> !invOpenValue.getValue()), noMoveValue = new BooleanValue("NoMove", this, false);
    private final int itemDelay = 0;
    private final BooleanValue hotbarValue = new BooleanValue("Hotbar", this, true);

    private long delay;

    private boolean locked;

    @EventLink
    public final Listener<Render3DEvent> onRender3D = event -> {
        if (!InventoryUtils.CLICK_TIMER.hasTimePassed(delay) || mc.thePlayer.openContainer != null && mc.thePlayer.openContainer.windowId != 0) return;
        final Map<Integer, List<ArmorPiece>> armorPieces = IntStream.range(0, 36).filter(i -> {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            return itemStack != null && itemStack.getItem() instanceof ItemArmor && (i < 9 || System.currentTimeMillis() - itemStack.getTimeCreated() >= itemDelay);
        }).mapToObj(i -> new ArmorPiece(mc.thePlayer.inventory.getStackInSlot(i), i)).collect(Collectors.groupingBy(ArmorPiece::getArmorType));
        final ArmorPiece[] bestArmor = new ArmorPiece[4];
        for (final Map.Entry<Integer, List<ArmorPiece>> armorEntry : armorPieces.entrySet()) bestArmor[armorEntry.getKey()] = armorEntry.getValue().stream().max(ARMOR_COMPARATOR).orElse(null);
        for (int i = 0; i < 4; i++) {
            final ArmorPiece armorPiece = bestArmor[i];
            if (armorPiece == null) continue;
            int armorSlot = 3 - i;
            final ArmorPiece oldArmor = new ArmorPiece(mc.thePlayer.inventory.armorItemInSlot(armorSlot), -1);
            if (ItemUtils.isStackEmpty(oldArmor.getItemStack()) || !(oldArmor.getItemStack().getItem() instanceof ItemArmor) || ARMOR_COMPARATOR.compare(oldArmor, armorPiece) < 0) {
                if (!ItemUtils.isStackEmpty(oldArmor.getItemStack()) && move(8 - armorSlot, true)) {
                    locked = true;
                    return;
                }
                if (ItemUtils.isStackEmpty(mc.thePlayer.inventory.armorItemInSlot(armorSlot)) && move(armorPiece.getSlot(), false)) {
                    locked = true;
                    return;
                }
            }
        }
        locked = false;
    };

    public boolean isLocked() {
        return isEnabled() && locked;
    }

    private boolean move(int item, boolean isArmorSlot) {
        if (!isArmorSlot && item < 9 && hotbarValue.getValue() && !(mc.currentScreen instanceof GuiInventory)) {
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(item));
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventoryContainer.getSlot(item).getStack()));
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            delay = TimeUtils.randomDelay(minDelay, maxDelay);
            return true;
        } else if (!(noMoveValue.getValue() && MoveUtil.isMoving()) && (!invOpenValue.getValue() || mc.currentScreen instanceof GuiInventory) && item != -1) {
            final boolean openInventory = simulateInventory.getValue() && !(mc.currentScreen instanceof GuiInventory);
            if (openInventory) mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(OPEN_INVENTORY_ACHIEVEMENT));
            boolean full = isArmorSlot;
            if (full) for (ItemStack iItemStack : mc.thePlayer.inventory.mainInventory) if (ItemUtils.isStackEmpty(iItemStack)) {
                full = false;
                break;
            }
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, full ? item : (isArmorSlot ? item : (item < 9 ? item + 36 : item)), full ? 1 : 0, full ? 4 : 1, mc.thePlayer);
            delay = TimeUtils.randomDelay(minDelay, maxDelay);
            if (openInventory) mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow());
            return true;
        }
        return false;
    }

}
