package best.azura.client.impl.module.impl.player;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.other.ItemUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.impl.value.dependency.BooleanDependency;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@ModuleInfo(name = "Chest Stealer", description = "Steal items out of chests", category = Category.PLAYER)
public class ChestStealer extends Module {
    private final BooleanValue closeValue = new BooleanValue("Close", "Auto close the chests", true);
    private final NumberValue<Double> closeDelayValue = new NumberValue<Double>("Close Delay", "The delay that has to pass before the chest gets closed", new BooleanDependency(closeValue, true), 0.0, 1.0, 0.0, 1000.0);
    private final NumberValue<Double> delayValue = new NumberValue<Double>("Delay", "Change the delay between clicking slots", 0.0, 1.0, 0.0, 1000.0);
    private final NumberValue<Double> randomizationValue = new NumberValue<Double>("Randomization", "Randomization of the delay", 0.0, 1.0, 0.0, 1000.0);
    private final NumberValue<Double> lootDelayValue = new NumberValue<Double>("Start Delay", "Delay before starting to loot the chest", 0.0, 1.0, 0.0, 1000.0);
    private final BooleanValue intelligentValue = new BooleanValue("Intelligent", "Make the chest stealer smart", true);
    private final BooleanValue checkValue = new BooleanValue("Check title", "Check the title of the chest", true);
    public static final BooleanValue silent = new BooleanValue("Silent", "Silent", false);
    private final DelayUtil delay = new DelayUtil(), openDelay = new DelayUtil();

    @EventHandler
    public Listener<EventRender2D> eventRender2DListener = eventRender2D -> {
        if(this.mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
            if (silent.getObject()) {
                if(!Mouse.isGrabbed()) {
                    mc.inGameHasFocus = true;
                    mc.mouseHelper.grabMouseCursor();
                }
            }
        }
    };

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = e -> {
        if (this.mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
            if (!openDelay.hasReached(lootDelayValue.getObject())) return;
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            if (checkValue.getObject()) {
                if (!(chest.getLowerChestInventory().getDisplayName().getFormattedText().contains("Chest") ||
                        chest.getLowerChestInventory().getDisplayName().getFormattedText().contains(I18n.format("container.chest")) ||
                        chest.getLowerChestInventory().getDisplayName().getFormattedText().contains("container.chest") ||
                        chest.getLowerChestInventory().getDisplayName().getFormattedText().contains("block.minecraft.chest") ||
                        chest.getLowerChestInventory().getDisplayName().getFormattedText().contains("LOW")))
                    return;
            }
            ArrayList<Integer> validSlots = new ArrayList<>();
            try {
                for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                    if (chest.getLowerChestInventory().getStackInSlot(i) == null) continue;
                    boolean b = checkItem(i);
                    if (b) validSlots.add(i);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            for (int i : validSlots) {
                if (this.delay.hasReached(getRandomDelay(), true))
                    this.mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
            }
            if (validSlots.isEmpty() && delay.hasReached(closeDelayValue.getObject()) && closeValue.getObject()) mc.thePlayer.closeScreen();
        } else {
            openDelay.reset();
        }
    };

    private int getRandomDelay() {
        int min = delayValue.getObject().intValue();
        int max = randomizationValue.getObject().intValue();
        if (max == 0)
            return min;
        return ThreadLocalRandom.current().nextInt(min, min + max);
    }

    private boolean checkItem(int i) {
        if (!intelligentValue.getObject()) return true;
        if (this.mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);
            if (stack == null) return false;
            if (ItemUtil.getItemDamage(stack) <= 0) return false;
            for (int o = 0; o < chest.getLowerChestInventory().getSizeInventory(); o++) {
                if (i == o) continue;
                ItemStack stack1 = chest.getLowerChestInventory().getStackInSlot(o);
                if (stack1 == null) continue;
                if (!stack.getItem().getClass().equals(stack1.getItem().getClass())) continue;
                if (stack.getItem() instanceof ItemBlock) continue;
                if (stack.getItem() instanceof ItemArmor &&
                        ((ItemArmor) stack.getItem()).armorType !=  ((ItemArmor) stack1.getItem()).armorType) continue;
                if (ItemUtil.getItemDamage(stack) < ItemUtil.getItemDamage(stack1)) return false;
            }
            for (int o = 0; o < 9 * 5; o++) {
                if (!mc.thePlayer.inventoryContainer.getSlot(o).getHasStack()) continue;
                ItemStack stack1 = mc.thePlayer.inventoryContainer.getSlot(o).getStack();
                if (stack1 == null) continue;
                if (!stack.getItem().getClass().equals(stack1.getItem().getClass())) continue;
                if (stack.getItem() instanceof ItemBlock) continue;
                if (stack.getItem() instanceof ItemArmor &&
                        ((ItemArmor) stack.getItem()).armorType !=  ((ItemArmor) stack1.getItem()).armorType) continue;
                if (ItemUtil.getItemDamage(stack) < ItemUtil.getItemDamage(stack1)) return false;
            }
        }
        return true;
    }
}