// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiChest;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class ChestStealer extends Module
{
    public static final String DELAY = "DELAY";
    public static final String DROP = "DROP";
    Timer timer;
    
    public ChestStealer(final ModuleData data) {
        super(data);
        this.timer = new Timer();
        ((HashMap<String, Setting<Integer>>)this.settings).put("DELAY", new Setting<Integer>("DELAY", 100, "MS delay before grabbing next item.", 50.0, 0.0, 1000.0));
        ((HashMap<String, Setting<Boolean>>)this.settings).put("DROP", new Setting<Boolean>("DROP", false, "Auto drop items."));
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre() && ChestStealer.mc.currentScreen instanceof GuiChest) {
                final GuiChest guiChest = (GuiChest)ChestStealer.mc.currentScreen;
                boolean full = true;
                ItemStack[] arrayOfItemStack;
                for (int j = (arrayOfItemStack = ChestStealer.mc.thePlayer.inventory.mainInventory).length, i = 0; i < j; ++i) {
                    final ItemStack item = arrayOfItemStack[i];
                    if (item == null) {
                        full = false;
                        break;
                    }
                }
                if (!full) {
                    for (int index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); ++index) {
                        final ItemStack stack = guiChest.lowerChestInventory.getStackInSlot(index);
                        if (stack != null && this.timer.delay(50 * ((HashMap<K, Setting<Number>>)this.settings).get("DELAY").getValue().intValue())) {
                            ChestStealer.mc.playerController.windowClick(guiChest.inventorySlots.windowId, index, 0, (int)(Object)!((HashMap<K, Setting<Boolean>>)this.settings).get("DROP").getValue(), ChestStealer.mc.thePlayer);
                            if (((HashMap<K, Setting<Boolean>>)this.settings).get("DROP").getValue()) {
                                ChestStealer.mc.playerController.windowClick(guiChest.inventorySlots.windowId, -999, 0, 0, ChestStealer.mc.thePlayer);
                            }
                            this.timer.reset();
                        }
                    }
                }
            }
        }
    }
}
