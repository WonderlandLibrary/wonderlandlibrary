package ravenweave.client.module.modules.beta;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.DoubleSliderSetting;
import ravenweave.client.module.setting.impl.TickSetting;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// Coded by poltergeist / localdrive
// https://femboy.thighs.wtf/h99k2q74ia7ywubdri.png
// I SWEAR I DIDNT SKID, I WAS GIVEN THE CODE RIGHTFULLY :SOB:

public class Refill extends Module {
    private DoubleSliderSetting delay;
    private TickSetting pots, soup;
    private int lastShiftedPotIndex = -1;
    private long lastUsageTime = 0;
    private long longranddel = 800;

    public Refill() {
        super("Refill", ModuleCategory.beta); // Category: Player
        this.registerSetting(new DescriptionSetting("Must use bind to use"));
        this.registerSetting(delay = new DoubleSliderSetting("Delay", 100, 125, 50, 250, 5));
        this.registerSetting(pots = new TickSetting("Pots", true));
        this.registerSetting(soup = new TickSetting("Soup", true));
    }

    @Override
    public void onEnable() {
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().currentScreen == null) {
            newDelay();
            openInventory();
            if (isHotbarFull()) {
                closeInventory();
            }
        } else {
            this.disable();
        }
    }

    private void openInventory() {
        mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
    }

    public void refillHotbar() {
        int nextPotIndex = findNextPotIndex();
        if (nextPotIndex != -1) {
            newDelay();
            shiftRightClickItem(nextPotIndex);
            lastShiftedPotIndex = nextPotIndex;

            if (isHotbarFull()) {
                closeInventory();
            }

        } else {
            closeInventory();
        }
    }

    public int findNextPotIndex() {
        int inventorySize = mc.thePlayer.inventory.getSizeInventory();
        int startIndex = (lastShiftedPotIndex + 1 + 9) % inventorySize;

        for (int i = startIndex; i != startIndex - 1; i = (i + 1) % inventorySize) {
            int slotIndex = i % inventorySize;

            if (slotIndex < 9) {
                continue;
            }

            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slotIndex);

            if (isValidStack(stack)) {
                lastShiftedPotIndex = slotIndex;
                return slotIndex;
            }

            if (i == (startIndex - 1 + inventorySize) % inventorySize) {
                break;
            }
        }
        return -1;
    }

    public void newDelay() {
        double minDelayValue = delay.getInputMin();
        double maxDelayValue = delay.getInputMax();
        double randomDelay = minDelayValue + (Math.random() * (maxDelayValue - minDelayValue));
        longranddel = (long) randomDelay;
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        long currentTime = System.currentTimeMillis();
        if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory && !isHotbarFull()) {
            if (currentTime - lastUsageTime >= longranddel) {
                refillHotbar();
                lastUsageTime = currentTime;
            }
        }
    }

    private boolean isValidStack(ItemStack stack) {
        if (stack == null) return false;
        return (pots.isToggled() && isPot(stack)) || (soup.isToggled() && isSoup(stack));
    }

    private boolean isSoup(ItemStack stack) {
        return stack.getItem() == Items.mushroom_stew;
    }

    private boolean isPot(ItemStack stack) {
        if (stack.getItem() instanceof ItemPotion) {
            int metadata = stack.getMetadata();

            Set<Integer> validMeta = new HashSet<>(Arrays.asList(16385, 16389, 16417, 16421, 16449, 16481));

            if (validMeta.contains(metadata)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isHotbarFull() {
        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }

    private void shiftRightClickItem(int slotIndex) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slotIndex, 0, 1, mc.thePlayer);
        mc.playerController.updateController();
    }
    private void closeInventory() {
        mc.thePlayer.closeScreen();
        mc.playerController.sendPacketDropItem(mc.thePlayer.inventory.getItemStack());
        this.disable();
    }
}
