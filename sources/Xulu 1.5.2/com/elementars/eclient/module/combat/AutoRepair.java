// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import java.util.HashMap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemExpBottle;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import java.util.Map;
import net.minecraft.inventory.ClickType;
import com.elementars.eclient.command.Command;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import com.elementars.eclient.util.BlockInteractionHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.gui.inventory.GuiContainer;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class AutoRepair extends Module
{
    private final Value<Integer> delay;
    private final Value<Integer> damage;
    private int mostDamagedSlot;
    private int mostDamage;
    private int lastSlot;
    private int counter;
    private int armorCount;
    private int wait;
    private int[] slots;
    private boolean shouldThrow;
    private boolean shouldArmor;
    private boolean falg;
    
    public AutoRepair() {
        super("AutoRepair", "Automatically repairs your armor", 0, Category.COMBAT, true);
        this.delay = this.register(new Value<Integer>("Delay", this, 16, 12, 24));
        this.damage = this.register(new Value<Integer>("Heal Damage %", this, 60, 10, 90));
    }
    
    @Override
    public void onEnable() {
        this.falg = false;
        this.mostDamage = -1;
        this.mostDamagedSlot = -1;
        this.shouldArmor = false;
        this.armorCount = 0;
        this.slots = new int[3];
        this.wait = 0;
        this.takeOffArmor();
        if (Xulu.MODULE_MANAGER.getModuleByName("EXPFast").isToggled()) {
            this.falg = true;
            Xulu.MODULE_MANAGER.getModuleByName("EXPFast").disable();
        }
    }
    
    @Override
    public void onDisable() {
        if (this.falg) {
            Xulu.MODULE_MANAGER.getModuleByName("EXPFast").toggle();
        }
    }
    
    @Override
    public void onUpdate() {
        if (AutoRepair.mc.player == null || !this.isToggled() || AutoRepair.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.shouldThrow) {
            BlockInteractionHelper.lookAtBlock(new BlockPos((Vec3i)AutoRepair.mc.player.getPosition().add(0, -1, 0)));
            AutoRepair.mc.player.inventory.currentItem = this.findXP();
            AutoRepair.mc.playerController.processRightClick((EntityPlayer)AutoRepair.mc.player, (World)AutoRepair.mc.world, EnumHand.MAIN_HAND);
            if (this.isRepaired() || this.counter > 40) {
                this.shouldThrow = false;
                this.shouldArmor = true;
                AutoRepair.mc.player.inventory.currentItem = this.lastSlot;
                Command.sendChatMessage("Finished Repairing");
            }
            ++this.counter;
        }
        if (this.shouldArmor) {
            if (this.wait >= this.delay.getValue()) {
                this.wait = 0;
                AutoRepair.mc.playerController.windowClick(0, this.slots[this.armorCount], 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoRepair.mc.player);
                AutoRepair.mc.playerController.updateController();
                ++this.armorCount;
                if (this.armorCount > 2) {
                    this.armorCount = 0;
                    this.shouldArmor = false;
                    this.disable();
                    return;
                }
            }
            ++this.wait;
        }
    }
    
    public int getMostDamagedSlot() {
        for (final Map.Entry<Integer, ItemStack> armorSlot : getArmor().entrySet()) {
            final ItemStack stack = armorSlot.getValue();
            if (stack.getItemDamage() > this.mostDamage) {
                this.mostDamage = stack.getItemDamage();
                this.mostDamagedSlot = armorSlot.getKey();
            }
        }
        return this.mostDamagedSlot;
    }
    
    public boolean isRepaired() {
        for (final Map.Entry<Integer, ItemStack> armorSlot : getArmor().entrySet()) {
            final ItemStack stack = armorSlot.getValue();
            if (armorSlot.getKey() == this.mostDamagedSlot) {
                final float percent = this.damage.getValue() / 100.0f;
                final int dam = Math.round(stack.getMaxDamage() * percent);
                final int goods = stack.getMaxDamage() - stack.getItemDamage();
                return dam <= goods;
            }
        }
        return false;
    }
    
    public int findXP() {
        this.lastSlot = AutoRepair.mc.player.inventory.currentItem;
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoRepair.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemExpBottle) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            Command.sendChatMessage("No EXP in hotbar!");
            this.disable();
            return 1;
        }
        return slot;
    }
    
    public boolean isSpace() {
        int spareSlots = 0;
        for (final Map.Entry<Integer, ItemStack> invSlot : getInventory().entrySet()) {
            final ItemStack stack = invSlot.getValue();
            if (stack.getItem() == Items.AIR) {
                this.slots[spareSlots] = invSlot.getKey();
                if (++spareSlots > 2) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public void takeOffArmor() {
        if (this.isSpace()) {
            this.getMostDamagedSlot();
            if (this.mostDamagedSlot != -1) {
                for (final Map.Entry<Integer, ItemStack> armorSlot : getArmor().entrySet()) {
                    if (armorSlot.getKey() != this.mostDamagedSlot) {
                        AutoRepair.mc.playerController.windowClick(0, (int)armorSlot.getKey(), 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoRepair.mc.player);
                    }
                }
                this.counter = 0;
                this.shouldThrow = true;
                return;
            }
        }
        Command.sendChatMessage("Please ensure there is atleast 3 inv slots open!");
        this.disable();
    }
    
    private static Map<Integer, ItemStack> getInventory() {
        return getInventorySlots(9, 44);
    }
    
    private static Map<Integer, ItemStack> getArmor() {
        return getInventorySlots(5, 8);
    }
    
    private static Map<Integer, ItemStack> getInventorySlots(int current, final int last) {
        final Map<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack)AutoRepair.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return fullInventorySlots;
    }
}
