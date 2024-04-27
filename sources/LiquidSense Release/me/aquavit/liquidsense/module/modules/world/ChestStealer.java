package me.aquavit.liquidsense.module.modules.world;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.ChestEvent;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.utils.timer.TimeUtils;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.player.InvClean;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.List;

@ModuleInfo(name = "ChestStealer", description = "Automatically steals all items from a chest.", category = ModuleCategory.WORLD)
public class ChestStealer extends Module {
    private IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 200, 0, 400) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = minDelayValue.get();
            if (i > newValue) {
                set(i);
            }
            nextDelay = TimeUtils.randomDelay(minDelayValue.get(), get());
        }
    };
    private IntegerValue minDelayValue = new IntegerValue("MinDelay", 150, 0, 400) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = maxDelayValue.get();
            if (i < newValue) {
                set(i);
            }
            nextDelay = TimeUtils.randomDelay(get(), maxDelayValue.get());
        }
    };

    private BoolValue takeRandomizedValue = new BoolValue("TakeRandomized", false);
    private BoolValue onlyItemsValue = new BoolValue("OnlyItems", false);
    private BoolValue noCompassValue = new BoolValue("NoCompass", false);
    private BoolValue usefulValue = new BoolValue("Useful", true);
    private BoolValue autoCloseValue = new BoolValue("AutoClose", true);
    public static BoolValue slientstealer = new BoolValue("SlientStealer",false);

    private IntegerValue autoCloseMaxDelayValue = new IntegerValue("AutoCloseMaxDelay", 0, 0, 400) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = autoCloseMinDelayValue.get();
            if (i > newValue) set(i);
            nextCloseDelay = TimeUtils.randomDelay(autoCloseMinDelayValue.get(), this.get());
        }
    };

    private IntegerValue autoCloseMinDelayValue = new IntegerValue("AutoCloseMinDelay", 0, 0, 400) {
        @Override
        protected void onChanged(final Integer oldValue, final Integer newValue) {
            final int i = autoCloseMaxDelayValue.get();
            if (i < newValue) set(i);
            nextCloseDelay = TimeUtils.randomDelay(this.get(), autoCloseMaxDelayValue.get());
        }
    };

    private BoolValue closeOnFullValue = new BoolValue("CloseOnFull", true);
    private BoolValue chestTitleValue = new BoolValue("ChestTitle", true);

    private MSTimer delayTimer = new MSTimer();
    private MSTimer autoCloseTimer = new MSTimer();
    private long nextDelay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
    private long nextCloseDelay = TimeUtils.randomDelay(autoCloseMinDelayValue.get(), autoCloseMaxDelayValue.get());
    private int contentReceived = 0;

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        GuiScreen screen = mc.currentScreen;

        if (!(screen instanceof GuiChest) || !delayTimer.hasTimePassed(nextDelay)) {
            autoCloseTimer.reset();
            return;
        }

        if (noCompassValue.get() && mc.thePlayer.inventory.getCurrentItem() != null &&
                mc.thePlayer.inventory.getCurrentItem().getItem() != null &&
                mc.thePlayer.inventory.getCurrentItem().getItem().getUnlocalizedName().equals("item.compass"))
            return;

        if (chestTitleValue.get() && (((GuiChest) screen).lowerChestInventory == null ||
                !((GuiChest) screen).lowerChestInventory.getName().contains(
                        new ItemStack(Item.itemRegistry.getObject(new ResourceLocation("minecraft:chest"))).getDisplayName())))
            return;

	    InvClean inventoryCleaner = (InvClean) LiquidSense.moduleManager.getModule(InvClean.class);

        if (!isEmpty((GuiChest) screen) && !(closeOnFullValue.get() && fullInventory())) {
            autoCloseTimer.reset();

            if (takeRandomizedValue.get()) {
                List<Slot> items = new ArrayList<Slot>();
                do {
                    for (int i = 0; i < ((GuiChest) screen).inventoryRows * 9; ++i) {
                        final Slot slot = ((GuiChest) screen).inventorySlots.inventorySlots.get(i);
                        if (slot.getStack() != null && (!onlyItemsValue.get() || !(slot.getStack().getItem() instanceof ItemBlock)) &&
                                (!inventoryCleaner.getState() || inventoryCleaner.isUseful(slot.getStack(),-1)))
                            items.add(slot);
                    }
                    int randomSlot = new Random().nextInt(items.size());
                    Slot slot = items.get(randomSlot);
                    move((GuiChest) screen, slot);
                } while (delayTimer.hasTimePassed(nextDelay) && !items.isEmpty());
                return;
            }

            for (int j = 0; j < ((GuiChest) screen).inventoryRows * 9; ++j) {
                final Slot slot = ((GuiChest) screen).inventorySlots.inventorySlots.get(j);
                if (delayTimer.hasTimePassed(nextDelay) && slot.getStack() != null &&
                        (!onlyItemsValue.get() || !(slot.getStack().getItem() instanceof ItemBlock)) &&
                        (!usefulValue.get() || inventoryCleaner.isUseful(slot.getStack(), -1)))
                    move((GuiChest) screen, slot);
            }
        } else if ((autoCloseValue.get() || slientstealer.get()) && ((GuiChest) screen).inventorySlots.windowId == contentReceived && autoCloseTimer.hasTimePassed(nextCloseDelay)) {
            mc.thePlayer.closeScreen();
            nextCloseDelay = TimeUtils.randomDelay(autoCloseMinDelayValue.get(), autoCloseMaxDelayValue.get());
        }
    }

    @EventTarget
    public void onChest(ChestEvent event) {
        GuiScreen screen = mc.currentScreen;
        if (slientstealer.get() && screen instanceof GuiChest) {
            GuiChest chest = (GuiChest) screen;
            if (!(chestTitleValue.get() && (chest.lowerChestInventory == null ||
                    !chest.lowerChestInventory.getName().contains(
                            new ItemStack(Item.itemRegistry.getObject(new ResourceLocation("minecraft:chest"))).getDisplayName())))) {
                mc.setIngameFocus();
                mc.currentScreen = screen;
                Fonts.font18.drawString("Stealing", (new ScaledResolution(mc).getScaledWidth() / 2) - (Fonts.font18.getStringWidth("Stealing") / 2)
                        , (new ScaledResolution(mc).getScaledHeight() / 2) + 30, 0xffffffff);
                event.cancelEvent();
            }
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S30PacketWindowItems) contentReceived = ((S30PacketWindowItems) packet).func_148911_c();
    }

    private void move(GuiChest screen, Slot slot) {
        screen.handleMouseClick(slot, slot.slotNumber, 0, 1);
        delayTimer.reset();
        nextDelay = TimeUtils.randomDelay(minDelayValue.get(), maxDelayValue.get());
    }

    private boolean isEmpty(GuiChest chest) {
	    InvClean inventoryCleaner = (InvClean) LiquidSense.moduleManager.getModule(InvClean.class);

        for (int i = 0; i < chest.inventoryRows * 9; ++i) {
            Slot slot = chest.inventorySlots.inventorySlots.get(i);

            if (slot.getStack() != null && (!onlyItemsValue.get() || !(slot.getStack().getItem() instanceof ItemBlock)) &&
                    (!usefulValue.get() || inventoryCleaner.isUseful(slot.getStack(), -1)))
            return false;
        }

        return true;
    }

    private boolean fullInventory() {
        for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; ++i) {
            if (mc.thePlayer.inventory.mainInventory[i] == null) {
                return false;
            }
        }
        return true;
    }
}
