/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import java.util.ArrayList;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.IntSetting;
import tk.rektsky.utils.inventory.ContainerUtil;
import tk.rektsky.utils.item.ItemsUtil;

public class AutoArmor
extends Module {
    public IntSetting delaySetting = new IntSetting("Delay", 1, 20, 10);
    public BooleanSetting throwItem = new BooleanSetting("ThrowArmor", true);
    int ticks = 0;

    public AutoArmor() {
        super("AutoArmor", "Automatically equips armor.", 0, Category.PLAYER);
    }

    @Override
    public void onEvent(Event e2) {
        if (e2 instanceof WorldTickEvent) {
            ++this.ticks;
            if (this.ticks % this.delaySetting.getValue() != 0) {
                return;
            }
            ArrayList<Slot> slots = ContainerUtil.getAllNonNullItems(this.mc.thePlayer.inventoryContainer);
            ArrayList<Slot> helmets = new ArrayList<Slot>();
            ArrayList<Slot> chestplates = new ArrayList<Slot>();
            ArrayList<Slot> leggings = new ArrayList<Slot>();
            ArrayList<Slot> boots = new ArrayList<Slot>();
            slots.forEach(slot -> {
                if (slot.getStack().getItem() instanceof ItemArmor) {
                    int s2 = 0;
                    if (((ItemArmor)slot.getStack().getItem()).getUnlocalizedName().contains("helmet")) {
                        s2 = 3;
                        helmets.add((Slot)slot);
                    }
                    if (((ItemArmor)slot.getStack().getItem()).getUnlocalizedName().contains("chest")) {
                        s2 = 2;
                        chestplates.add((Slot)slot);
                    }
                    if (((ItemArmor)slot.getStack().getItem()).getUnlocalizedName().contains("leg")) {
                        s2 = 1;
                        leggings.add((Slot)slot);
                    }
                    if (((ItemArmor)slot.getStack().getItem()).getUnlocalizedName().contains("boot")) {
                        s2 = 0;
                        boots.add((Slot)slot);
                    }
                }
            });
            helmets.sort(new ItemsUtil.ArmorComparator());
            chestplates.sort(new ItemsUtil.ArmorComparator());
            leggings.sort(new ItemsUtil.ArmorComparator());
            boots.sort(new ItemsUtil.ArmorComparator());
            if (helmets.size() >= 1 && this.mc.thePlayer.getCurrentArmor(3) != ((Slot)helmets.get(0)).getStack()) {
                if (this.mc.thePlayer.getCurrentArmor(3) != null) {
                    if (this.throwItem.getValue().booleanValue()) {
                        this.mc.playerController.windowClick(0, 5, 0, 4, this.mc.thePlayer);
                    } else {
                        this.mc.playerController.windowClick(0, 5, 1, 1, this.mc.thePlayer);
                    }
                }
                this.mc.playerController.windowClick(0, ((Slot)helmets.remove((int)0)).slotNumber, 1, 1, this.mc.thePlayer);
                return;
            }
            if (chestplates.size() >= 1 && this.mc.thePlayer.getCurrentArmor(2) != ((Slot)chestplates.get(0)).getStack()) {
                if (this.mc.thePlayer.getCurrentArmor(2) != null) {
                    if (this.throwItem.getValue().booleanValue()) {
                        this.mc.playerController.windowClick(0, 6, 0, 4, this.mc.thePlayer);
                    } else {
                        this.mc.playerController.windowClick(0, 6, 1, 1, this.mc.thePlayer);
                    }
                }
                this.mc.playerController.windowClick(0, ((Slot)chestplates.remove((int)0)).slotNumber, 1, 1, this.mc.thePlayer);
                return;
            }
            if (leggings.size() >= 1 && this.mc.thePlayer.getCurrentArmor(1) != ((Slot)leggings.get(0)).getStack()) {
                if (this.mc.thePlayer.getCurrentArmor(1) != null) {
                    if (this.throwItem.getValue().booleanValue()) {
                        this.mc.playerController.windowClick(0, 7, 0, 4, this.mc.thePlayer);
                    } else {
                        this.mc.playerController.windowClick(0, 7, 1, 1, this.mc.thePlayer);
                    }
                }
                this.mc.playerController.windowClick(0, ((Slot)leggings.remove((int)0)).slotNumber, 1, 1, this.mc.thePlayer);
                return;
            }
            if (boots.size() >= 1 && this.mc.thePlayer.getCurrentArmor(0) != ((Slot)boots.get(0)).getStack()) {
                if (this.mc.thePlayer.getCurrentArmor(0) != null) {
                    if (this.throwItem.getValue().booleanValue()) {
                        this.mc.playerController.windowClick(0, 8, 0, 4, this.mc.thePlayer);
                    } else {
                        this.mc.playerController.windowClick(0, 8, 1, 1, this.mc.thePlayer);
                    }
                }
                this.mc.playerController.windowClick(0, ((Slot)boots.remove((int)0)).slotNumber, 1, 1, this.mc.thePlayer);
                return;
            }
            if (this.throwItem.getValue().booleanValue()) {
                for (Slot helmet : helmets) {
                    if (helmet.slotNumber == 5 || helmet.slotNumber == 6 || helmet.slotNumber == 7 || helmet.slotNumber == 8) continue;
                    this.mc.playerController.windowClick(0, helmet.slotNumber, 0, 4, this.mc.thePlayer);
                    return;
                }
                for (Slot chestplate : chestplates) {
                    if (chestplate.slotNumber == 5 || chestplate.slotNumber == 6 || chestplate.slotNumber == 7 || chestplate.slotNumber == 8) continue;
                    this.mc.playerController.windowClick(0, chestplate.slotNumber, 0, 4, this.mc.thePlayer);
                    return;
                }
                for (Slot legging : leggings) {
                    if (legging.slotNumber == 5 || legging.slotNumber == 6 || legging.slotNumber == 7 || legging.slotNumber == 8) continue;
                    this.mc.playerController.windowClick(0, legging.slotNumber, 0, 4, this.mc.thePlayer);
                    return;
                }
                for (Slot boot : boots) {
                    if (boot.slotNumber == 5 || boot.slotNumber == 6 || boot.slotNumber == 7 || boot.slotNumber == 8) continue;
                    this.mc.playerController.windowClick(0, boot.slotNumber, 0, 4, this.mc.thePlayer);
                    return;
                }
            }
        }
    }
}

