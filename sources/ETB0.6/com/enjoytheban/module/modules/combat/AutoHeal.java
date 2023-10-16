package com.enjoytheban.module.modules.combat;

import java.awt.Color;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.Type;
import com.enjoytheban.api.events.world.EventPostUpdate;
import com.enjoytheban.api.events.world.EventPreUpdate;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.TimerUtil;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoHeal extends Module {

    private Numbers<Double> health = new Numbers("Health", "health", 3.0, 0.0, 10.0, 0.5), delay = new Numbers("Delay", "delay", 400.0, 0.0, 1000.0, 10.0);

    private Option<Boolean> jump = new Option("Jump", "jump", true);

    private Mode<Enum> mode = new Mode("Mode", "mode", HealMode.values(), HealMode.Potion);

    static boolean currentlyPotting = false;
    private boolean isUsing;
    private int slot;
    private TimerUtil timer = new TimerUtil();

    public AutoHeal() {
        super("AutoHeal", new String[]{"autopot", "autop", "autosoup"}, ModuleType.Combat);
        setColor(new Color(76,249,247).getRGB());
        addValues(health, delay, jump, mode);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (timer.hasReached(delay.getValue()) && mc.thePlayer.getHealth() <= health.getValue() * 2) {
            isUsing = (slot = mode.getValue() == HealMode.Potion ? getPotionSlot() : mode.getValue() == HealMode.Soup ? getSoupSlot() : getPotionSlot()) != -1 && (jump.getValue() ? mc.thePlayer.onGround : true);
            if (isUsing) {
                timer.reset();
                if (mode.getValue() == HealMode.Potion) {
                	currentlyPotting = true;
                    e.setPitch(jump.getValue() ? -90 : 90);
                    if(timer.hasReached(1)) {
                    currentlyPotting = false;
                    timer.reset();
                    }
                }
            }
        }
    }

    @EventHandler
    private void onUpdatePost(EventPostUpdate e) {
        if (isUsing) {
            int current = mc.thePlayer.inventory.currentItem, next = mc.thePlayer.nextSlot();
            mc.thePlayer.moveToHotbar(slot, next);
            mc.thePlayer.sendQueue
                    .addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = next));
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
            mc.thePlayer.sendQueue
                    .addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = current));
            isUsing = false;
            if (mc.thePlayer.onGround && jump.getValue() && mode.getValue() == HealMode.Potion) {
                mc.thePlayer.setSpeed(0);
                mc.thePlayer.motionY = 0.42;
            }
        }
    }

    // return the slot with a potion in it yaayyyy im a retard
    private int getPotionSlot() {
        int slot = -1;
        for (Slot s : mc.thePlayer.inventoryContainer.inventorySlots) {
            if (!s.getHasStack()) {
                continue;
            }
            ItemStack is = s.getStack();
            if (!(is.getItem() instanceof ItemPotion)) {
                continue;
            }
            ItemPotion ip = (ItemPotion) is.getItem();
            if (!ip.isSplash(is.getMetadata())) {
                continue;
            }
            boolean hasHealing = false;
            for (PotionEffect pe : ip.getEffects(is)) {
                if (pe.getPotionID() != Potion.heal.id) {
                    continue;
                }
                hasHealing = true;
                break;
            }
            if (!hasHealing) {
                continue;
            }
            slot = s.slotNumber;
            break;
        }
        return slot;
    }

    private int getSoupSlot() {
        int slot = -1;
        for (Slot s : mc.thePlayer.inventoryContainer.inventorySlots) {
            if (!s.getHasStack()) {
                continue;
            }
            ItemStack is = s.getStack();
            if (!(is.getItem() instanceof ItemSoup)) {
                continue;
            }
            slot = s.slotNumber;
            break;
        }
        return slot;
    }

    // get the number of pots and soups, really should have made this into one
    // method but im lazy
    private int getPotionCount() {
        int count = 0;
        for (Slot s : mc.thePlayer.inventoryContainer.inventorySlots) {
            if (!s.getHasStack()) {
                continue;
            }
            ItemStack is = s.getStack();
            if (!(is.getItem() instanceof ItemPotion)) {
                continue;
            }
            ItemPotion ip = (ItemPotion) is.getItem();
            if (!ip.isSplash(is.getMetadata())) {
                continue;
            }
            boolean hasHealing = false;
            for (PotionEffect pe : ip.getEffects(is)) {
                if (pe.getPotionID() != Potion.heal.id) {
                    continue;
                }
                hasHealing = true;
                break;
            }
            if (!hasHealing) {
                continue;
            }
            count++;
        }
        return count;
    }

    private int getSoupCount() {
        int count = 0;
        for (Slot s : mc.thePlayer.inventoryContainer.inventorySlots) {
            if (!s.getHasStack()) {
                continue;
            }
            ItemStack is = s.getStack();
            if (!(is.getItem() instanceof ItemSoup)) {
                continue;
            }
            count++;
        }
        return count;
    }

    enum HealMode {
        Potion, Soup
    }
}