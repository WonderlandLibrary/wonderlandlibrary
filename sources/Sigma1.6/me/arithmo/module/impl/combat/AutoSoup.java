/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.combat;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoSoup
extends Module {
    Timer timer = new Timer();
    public static String DELAY = "DELAY";
    public static String HEALTH = "HEALTH";
    public String DROP = "DROP";

    public AutoSoup(ModuleData data) {
        super(data);
        this.settings.put(HEALTH, new Setting<Integer>(HEALTH, 3, "Maximum health before healing."));
        this.settings.put(DELAY, new Setting<Integer>(DELAY, 350, "Delay before healing again.", 50.0, 100.0, 1000.0));
        this.settings.put(this.DROP, new Setting<Boolean>(this.DROP, false, "Drop soup after use."));
    }

    @RegisterEvent(events={EventMotion.class})
    public void onEvent(Event event) {
        EventMotion em;
        int soupSlot;
        if (event instanceof EventMotion && (em = (EventMotion)event).isPre() && (soupSlot = AutoSoup.getSoupFromInventory()) != -1 && AutoSoup.mc.thePlayer.getHealth() < ((Number)((Setting)this.settings.get(HEALTH)).getValue()).floatValue() && this.timer.delay(((Number)((Setting)this.settings.get(DELAY)).getValue()).floatValue())) {
            this.swap(AutoSoup.getSoupFromInventory(), 6);
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(AutoSoup.mc.thePlayer.inventory.getCurrentItem()));
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(AutoSoup.mc.thePlayer.inventory.currentItem));
        }
    }

    protected void swap(int slot, int hotbarNum) {
        AutoSoup.mc.playerController.windowClick(AutoSoup.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, AutoSoup.mc.thePlayer);
    }

    public static int getSoupFromInventory() {
        Minecraft mc = Minecraft.getMinecraft();
        int soup = -1;
        int counter = 0;
        for (int i = 1; i < 45; ++i) {
            ItemStack is;
            Item item;
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || Item.getIdFromItem(item = (is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) != 282) continue;
            ++counter;
            soup = i;
        }
        return soup;
    }
}

