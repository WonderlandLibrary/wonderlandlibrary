// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.combat;

import java.util.HashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.event.RegisterEvent;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class AutoSoup extends Module
{
    Timer timer;
    public static String DELAY;
    public static String HEALTH;
    public String DROP;
    
    public AutoSoup(final ModuleData data) {
        super(data);
        this.timer = new Timer();
        this.DROP = "DROP";
        ((HashMap<String, Setting<Integer>>)this.settings).put(AutoSoup.HEALTH, new Setting<Integer>(AutoSoup.HEALTH, 3, "Maximum health before healing."));
        ((HashMap<String, Setting<Integer>>)this.settings).put(AutoSoup.DELAY, new Setting<Integer>(AutoSoup.DELAY, 350, "Delay before healing again.", 50.0, 100.0, 1000.0));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.DROP, new Setting<Boolean>(this.DROP, false, "Drop soup after use."));
    }
    
    @RegisterEvent(events = { EventMotion.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre()) {
                final int soupSlot = getSoupFromInventory();
                if (soupSlot != -1 && AutoSoup.mc.thePlayer.getHealth() < ((HashMap<K, Setting<Number>>)this.settings).get(AutoSoup.HEALTH).getValue().floatValue() && this.timer.delay(((HashMap<K, Setting<Number>>)this.settings).get(AutoSoup.DELAY).getValue().floatValue())) {
                    this.swap(getSoupFromInventory(), 6);
                    AutoSoup.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                    AutoSoup.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(AutoSoup.mc.thePlayer.inventory.getCurrentItem()));
                    AutoSoup.mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(AutoSoup.mc.thePlayer.inventory.currentItem));
                }
            }
        }
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        AutoSoup.mc.playerController.windowClick(AutoSoup.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, AutoSoup.mc.thePlayer);
    }
    
    public static int getSoupFromInventory() {
        final Minecraft mc = Minecraft.getMinecraft();
        int soup = -1;
        int counter = 0;
        for (int i = 1; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (Item.getIdFromItem(item) == 282) {
                    ++counter;
                    soup = i;
                }
            }
        }
        return soup;
    }
    
    static {
        AutoSoup.DELAY = "DELAY";
        AutoSoup.HEALTH = "HEALTH";
    }
}
