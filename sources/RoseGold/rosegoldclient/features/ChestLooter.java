package rosegoldclient.features;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rosegoldclient.Main;
import rosegoldclient.events.SecondEvent;
import rosegoldclient.events.TickEndEvent;
import rosegoldclient.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChestLooter {
    private long lastClickTime = 0L;
    private final List<Map.Entry<Slot, Boolean>> items = new ArrayList<>(27);
    private Set<String> leftClick = Stream.of("Kaian Scroll", "Fairy Powder", "Stolen Goods", "Golden Avia Feather", "Fiery Aura", "Windy Aura", "Watery Aura", "Sought-After Ore", "Ancient Currency", "Glimmering Coin", "Doom Stone", "Lunar Charm", "Nose Ring", "Decaying Heart", "Stolen Pearls", "Antique Metal", "Luxroot Cuttings", "Emerald").collect(Collectors.toSet());
    private final Set<String> shiftClick = Stream.of("Fire Powder IV", "Air Powder IV", "Thunder Powder IV", "Water Powder IV", "Earth Powder IV").collect(Collectors.toSet());

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        items.clear();
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        ContainerChest container;
        if (!Main.configFile.chestLoot || !(event.getGui() instanceof GuiChest)) return;
        if (((GuiChest) event.getGui()).inventorySlots instanceof ContainerChest && StringUtils.stripControlCodes((container = (ContainerChest) ((GuiChest) event.getGui()).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText()).contains("Loot Chest I")) {
            if (items.isEmpty()) {
                HashMap<Slot, Boolean> map = new HashMap<>();
                List<Slot> chestSlots = container.inventorySlots.subList(0, 27);
                for (Slot slot : chestSlots) {
                    if (!slot.getHasStack()) continue;
                    String itemName = StringUtils.stripControlCodes(slot.getStack().getDisplayName());
                    if (leftClick.stream().anyMatch(itemName::contains)) {
                        map.put(slot, false);
                        continue;
                    }
                    if(shiftClick.stream().anyMatch(itemName::contains)) {
                        map.put(slot, true);
                    }
                }
                items.addAll(map.entrySet());
            } else if (System.currentTimeMillis() - lastClickTime > (long) Main.configFile.chestLootDelay) {
                Main.mc.playerController.windowClick(
                        Main.mc.player.openContainer.windowId,
                        items.get(0).getKey().slotNumber,
                        0,
                        items.get(0).getValue() ? ClickType.QUICK_MOVE : ClickType.PICKUP,
                        Main.mc.player
                );
                items.remove(0);
                lastClickTime = System.currentTimeMillis();
            }
        }
    }

    @SubscribeEvent
    public void onSecond(SecondEvent event) {
        Set<String> mainStream = Stream.of("Kaian Scroll", "Fairy Powder", "Stolen Goods", "Golden Avia Feather", "Fiery Aura", "Windy Aura", "Watery Aura", "Sought-After Ore", "Ancient Currency", "Glimmering Coin", "Doom Stone", "Lunar Charm", "Nose Ring", "Decaying Heart", "Stolen Pearls", "Antique Metal", "Luxroot Cuttings", "Emerald").collect(Collectors.toSet());
        if(Main.configFile.doChestLootFilter) {
            String[] split = Main.configFile.chestLootFilter.split(",");
            mainStream.addAll(Stream.of(split).collect(Collectors.toSet()));
        }
        leftClick = mainStream;
    }
}
