/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MISC;

import com.darkmagician6.eventapi.EventTarget;
import java.util.List;
import java.util.Random;
import me.AveReborn.Value;
import me.AveReborn.events.EventUpdate;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.timeUtils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ChestStealer
extends Mod {
    public Value<Double> delay = new Value<Double>("ChestStealer_Delay", 65.0, 0.0, 200.0, 1.0);
    public Value<Boolean> cchest = new Value<Boolean>("ChestStealer_CloseChest", true);
    TimeHelper time = new TimeHelper();

    public ChestStealer() {
        super("ChestStealer", Category.MISC);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Container container = Minecraft.thePlayer.openContainer;
        if (container != null && container instanceof ContainerChest) {
            ContainerChest containerchest = (ContainerChest)container;
            int i2 = 0;
            while (i2 < containerchest.getLowerChestInventory().getSizeInventory()) {
                if (containerchest.getLowerChestInventory().getStackInSlot(i2) != null && this.time.delay((float)this.delay.getValueState().doubleValue()) && new Random().nextInt(100) <= 80) {
                    this.mc.playerController.windowClick(containerchest.windowId, i2, 0, 1, Minecraft.thePlayer);
                    this.time.reset();
                }
                ++i2;
            }
            if (this.isContainerEmpty(container) && this.cchest.getValueState().booleanValue()) {
                Minecraft.thePlayer.closeScreen();
            }
        }
    }

    private boolean isContainerEmpty(Container container) {
        boolean flag = true;
        int i2 = 0;
        int j2 = container.inventorySlots.size() == 90 ? 54 : 27;
        while (i2 < j2) {
            if (container.getSlot(i2).getHasStack()) {
                flag = false;
            }
            ++i2;
        }
        return flag;
    }
}

