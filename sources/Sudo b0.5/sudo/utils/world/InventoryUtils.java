package sudo.utils.world;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.screen.BlastFurnaceScreenHandler;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.SmokerScreenHandler;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import sudo.utils.mixins.IClientPlayerInteractionManager;

public class InventoryUtils {
	
	public static MinecraftClient mc = MinecraftClient.getInstance();
	
	private static final Action ACTION = new Action();
	public static int previousSlot = -1;
    public static final int HOTBAR_START = 0;
    public static final int HOTBAR_END = 8;

    public static final int OFFHAND = 45;

    public static final int MAIN_START = 9;
    public static final int MAIN_END = 35;

    public static final int ARMOR_START = 36;
    public static final int ARMOR_END = 39;
    
    public static Map<Integer, ItemStack> getSlots(int start, int end) {
        Map<Integer, ItemStack> slots = new ConcurrentHashMap<>();

        for (int i = start; i < end; ++i) {
            slots.put(i, mc.player.getInventory().getStack(i));
        }

        return slots;
    }
    
    public static Map<Integer, ItemStack> getSlots(ScreenHandler container, int start, int end) {
        Map<Integer, ItemStack> slots = new ConcurrentHashMap<>();

        for (int i = start; i < end; ++i) {
            slots.put(i, container.getSlot(i).getStack());
        }

        return slots;
    }
    
    public static boolean isInventoryFull() {
    	for (Integer slot : getSlots(0, MAIN_END).keySet()) {
    		if (mc.player.getInventory().getStack(slot).getItem() == Items.AIR) return false;
    	}
    	
        return true;
    }
    
    public static boolean isContainerEmpty(ScreenHandler container) {
    	for (Integer slot : getSlots(0, MAIN_END).keySet()) {
    		if (container.getSlot(slot).getStack().getItem() == Items.AIR) return false;
    	}
    	
        return true;
    }

    public static int indexToId(int i) {
        if (mc.player == null) return -1;
        ScreenHandler handler = mc.player.currentScreenHandler;

        if (handler instanceof PlayerScreenHandler) return survivalInventory(i);
        else if (handler instanceof GenericContainerScreenHandler) return genericContainer(i, ((GenericContainerScreenHandler) handler).getRows());
        else if (handler instanceof CraftingScreenHandler) return craftingTable(i);
        else if (handler instanceof FurnaceScreenHandler) return furnace(i);
        else if (handler instanceof BlastFurnaceScreenHandler) return furnace(i);
        else if (handler instanceof SmokerScreenHandler) return furnace(i);
        else if (handler instanceof Generic3x3ContainerScreenHandler) return generic3x3(i);
        else if (handler instanceof EnchantmentScreenHandler) return enchantmentTable(i);
        else if (handler instanceof BrewingStandScreenHandler) return brewingStand(i);
        else if (handler instanceof MerchantScreenHandler) return villager(i);
        else if (handler instanceof BeaconScreenHandler) return beacon(i);
        else if (handler instanceof AnvilScreenHandler) return anvil(i);
        else if (handler instanceof HopperScreenHandler) return hopper(i);
        else if (handler instanceof ShulkerBoxScreenHandler) return genericContainer(i, 3);
        else if (handler instanceof CartographyTableScreenHandler) return cartographyTable(i);
        else if (handler instanceof GrindstoneScreenHandler) return grindstone(i);
        else if (handler instanceof LecternScreenHandler) return lectern();
        else if (handler instanceof LoomScreenHandler) return loom(i);
        else if (handler instanceof StonecutterScreenHandler) return stonecutter(i);

        return -1;
    }

    private static int survivalInventory(int i) {
        if (isHotbar(i)) return 36 + i;
        if (isArmor(i)) return 5 + (i - 36);
        return i;
    }

    private static int genericContainer(int i, int rows) {
        if (isHotbar(i)) return (rows + 3) * 9 + i;
        if (isMain(i)) return rows * 9 + (i - 9);
        return -1;
    }

    private static int craftingTable(int i) {
        if (isHotbar(i)) return 37 + i;
        if (isMain(i)) return i + 1;
        return -1;
    }

    private static int furnace(int i) {
        if (isHotbar(i)) return 30 + i;
        if (isMain(i)) return 3 + (i - 9);
        return -1;
    }

    private static int generic3x3(int i) {
        if (isHotbar(i)) return 36 + i;
        if (isMain(i)) return i;
        return -1;
    }

    private static int enchantmentTable(int i) {
        if (isHotbar(i)) return 29 + i;
        if (isMain(i)) return 2 + (i - 9);
        return -1;
    }

    private static int brewingStand(int i) {
        if (isHotbar(i)) return 32 + i;
        if (isMain(i)) return 5 + (i - 9);
        return -1;
    }

    private static int villager(int i) {
        if (isHotbar(i)) return 30 + i;
        if (isMain(i)) return 3 + (i - 9);
        return -1;
    }

    private static int beacon(int i) {
        if (isHotbar(i)) return 28 + i;
        if (isMain(i)) return 1 + (i - 9);
        return -1;
    }

    private static int anvil(int i) {
        if (isHotbar(i)) return 30 + i;
        if (isMain(i)) return 3 + (i - 9);
        return -1;
    }

    private static int hopper(int i) {
        if (isHotbar(i)) return 32 + i;
        if (isMain(i)) return 5 + (i - 9);
        return -1;
    }

    private static int cartographyTable(int i) {
        if (isHotbar(i)) return 30 + i;
        if (isMain(i)) return 3 + (i - 9);
        return -1;
    }

    private static int grindstone(int i) {
        if (isHotbar(i)) return 30 + i;
        if (isMain(i)) return 3 + (i - 9);
        return -1;
    }

    private static int lectern() {
        return -1;
    }

    private static int loom(int i) {
        if (isHotbar(i)) return 31 + i;
        if (isMain(i)) return 4 + (i - 9);
        return -1;
    }

    private static int stonecutter(int i) {
        if (isHotbar(i)) return 29 + i;
        if (isMain(i)) return 2 + (i - 9);
        return -1;
    }

    public static boolean isHotbar(int i) {
        return i >= HOTBAR_START && i <= HOTBAR_END;
    }

    public static boolean isMain(int i) {
        return i >= MAIN_START && i <= MAIN_END;
    }

    public static boolean isArmor(int i) {
        return i >= ARMOR_START && i <= ARMOR_END;
    }
    
    public static FindItemResult find(Item... items) {
        return find(itemStack -> {
            for (Item item : items) {
                if (itemStack.getItem() == item) return true;
            }
            return false;
        });
    }
    
    public static FindItemResult find(Predicate<ItemStack> isGood) {
        return find(isGood, 0, mc.player.getInventory().size());
    }

    public static FindItemResult find(Predicate<ItemStack> isGood, int start, int end) {
        int slot = -1, count = 0;

        for (int i = start; i <= end; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);

            if (isGood.test(stack)) {
                if (slot == -1) slot = i;
                count += stack.getCount();
            }
        }

        return new FindItemResult(slot, count);
    }

    public static FindItemResult findFastestTool(BlockState state) {
        float bestScore = -1;
        int slot = -1;

        for (int i = 0; i < 9; i++) {
            float score = mc.player.getInventory().getStack(i).getMiningSpeedMultiplier(state);
            if (score > bestScore) {
                bestScore = score;
                slot = i;
            }
        }

        return new FindItemResult(slot, 1);
    }
    
    public static Action click() {
        ACTION.type = SlotActionType.PICKUP;
        return ACTION;
    }
    
    public static Action move() {
        ACTION.type = SlotActionType.PICKUP;
        ACTION.two = true;
        return ACTION;
    }
    
    public static class Action {
        private SlotActionType type = null;
        private boolean two = false;
        private int from = -1;
        private int to = -1;
        private int data = 0;

        private boolean isRecursive = false;

        private Action() {}

        public Action fromId(int id) {
            from = id;
            return this;
        }

        public Action from(int index) {
            return fromId(InventoryUtils.indexToId(index));
        }

        public Action fromHotbar(int i) {
            return from(InventoryUtils.HOTBAR_START + i);
        }

        public Action fromOffhand() {
            return from(InventoryUtils.OFFHAND);
        }

        public Action fromMain(int i) {
            return from(InventoryUtils.MAIN_START + i);
        }

        public Action fromArmor(int i) {
            return from(InventoryUtils.ARMOR_START + (3 - i));
        }

        public void toId(int id) {
            to = id;
            run();
        }

        public void to(int index) {
            toId(InventoryUtils.indexToId(index));
        }

        public void toHotbar(int i) {
            to(InventoryUtils.HOTBAR_START + i);
        }

        public void toOffhand() {
            to(InventoryUtils.OFFHAND);
        }

        public void toMain(int i) {
            to(InventoryUtils.MAIN_START + i);
        }

        public void toArmor(int i) {
            to(InventoryUtils.ARMOR_START + (3 - i));
        }

        // Slot

        public void slotId(int id) {
            from = to = id;
            run();
        }

        public void slot(int index) {
            slotId(InventoryUtils.indexToId(index));
        }

        public void slotHotbar(int i) {
            slot(InventoryUtils.HOTBAR_START + i);
        }

        public void slotOffhand() {
            slot(InventoryUtils.OFFHAND);
        }

        public void slotMain(int i) {
            slot(InventoryUtils.MAIN_START + i);
        }

        public void slotArmor(int i) {
            slot(InventoryUtils.ARMOR_START + (3 - i));
        }

        // Other

        private void run() {
            boolean hadEmptyCursor = mc.player.currentScreenHandler.getCursorStack().isEmpty();

            if (type != null && from != -1 && to != -1) {
               click(from);
               if (two) click(to);
            }

            SlotActionType preType = type;
            boolean preTwo = two;
            int preFrom = from;
            int preTo = to;

            type = null;
            two = false;
            from = -1;
            to = -1;
            data = 0;

            if (!isRecursive && hadEmptyCursor && preType == SlotActionType.PICKUP && preTwo && (preFrom != -1 && preTo != -1) && !mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
                isRecursive = true;
                InventoryUtils.click().slotId(preFrom);
                isRecursive = false;
            }
        }

        private void click(int id) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, id, data, type, mc.player);
        }
    }

	public static boolean swap(int slot, boolean swapBack) {
        if (slot < 0 || slot > 8) return false;
        if (swapBack && previousSlot == -1) previousSlot = mc.player.getInventory().selectedSlot;

        mc.player.getInventory().selectedSlot = slot;
        ((IClientPlayerInteractionManager) mc.interactionManager).syncSelected();
        return true;
    }
	
	public static boolean swapBack() {
        if (previousSlot == -1) return false;

        boolean return_ = swap(previousSlot, false);
        previousSlot = -1;
        return return_;
    }

	public static FindItemResult findInHotbar(Item... items) {
        return findInHotbar(itemStack -> {
            for (Item item : items) {
                if (itemStack.getItem() == item) return true;
            }
            return false;
        });
    }

    public static FindItemResult findInHotbar(Predicate<ItemStack> isGood) {
        if (isGood.test(mc.player.getOffHandStack())) {
            return new FindItemResult(OFFHAND, mc.player.getOffHandStack().getCount());
        }

        if (isGood.test(mc.player.getMainHandStack())) {
            return new FindItemResult(mc.player.getInventory().selectedSlot, mc.player.getMainHandStack().getCount());
        }

        return find(isGood, 0, 8);
    }
    
    public static int findInHotbarInt(Item item) {
        int index = -1;
        for(int i = 0; i < 9; i++) {
            if(mc.player.getInventory().getStack(i).getItem() == item) {
                index = i;
                break;
            }
        }
        return index;
    }
    
    public static int getSlot(boolean offhand, boolean reverse, Comparator<Integer> comparator) {
		return IntStream.of(getInventorySlots(offhand))
				.boxed()
				.min(comparator.reversed()).orElse(-1);
	}
    
    public static Hand selectSlot(boolean offhand, boolean reverse, Comparator<Integer> comparator) {
		return selectSlot(getSlot(offhand, reverse, comparator));
	}
    
    public static Hand selectSlot(boolean offhand, Predicate<Integer> filter) {
		return selectSlot(getSlot(offhand, filter));
	}
    
    public static int getSlot(boolean offhand, Predicate<Integer> filter) {
		return IntStream.of(getInventorySlots(offhand))
				.boxed()
				.filter(filter)
				.findFirst().orElse(-1);
	}
    
    public static Hand selectSlot(int slot) {
		if (slot >= 0 && slot <= 36) {
			if (slot < 9) {
				if (slot != mc.player.getInventory().selectedSlot) {
					mc.player.getInventory().selectedSlot = slot;
					mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
				}

				return Hand.MAIN_HAND;
			} else if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
				for (int i = 0; i <= 8; i++) {
					if (mc.player.getInventory().getStack(i).isEmpty()) {
						mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.QUICK_MOVE, mc.player);

						if (i != mc.player.getInventory().selectedSlot) {
							mc.player.getInventory().selectedSlot = i;
							mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
						}

						return Hand.MAIN_HAND;
					}
				}

				mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
				mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 36 + mc.player.getInventory().selectedSlot, 0, SlotActionType.PICKUP, mc.player);
				mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
				return Hand.MAIN_HAND;
			}
		} else if (slot == 40) {
			return Hand.OFF_HAND;
		}

		return null;
	}
    
    private static int[] getInventorySlots(boolean offhand) {
		int[] i = new int[offhand ? 38 : 37];
		
		// Add hand slots first
		i[0] = mc.player.getInventory().selectedSlot;
		i[1] = 40;

		for (int j = 0; j < 36; j++) {
			if (j != mc.player.getInventory().selectedSlot) {
				i[offhand ? j + 2 : j + 1] = j;
			}
		}
		
		return i;
	}
    
    public static int getSlotIndex(int index) {
        return index < 9 ? index + 36 : index;
    }
}