// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import java.util.List;
import net.minecraft.nbt.NBTTagList;
import java.util.Iterator;
import net.minecraft.enchantment.Enchantment;
import java.util.ArrayList;
import net.minecraft.inventory.EntityEquipmentSlot;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import com.elementars.eclient.event.events.LocalPlayerUpdateEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class AutoTotem extends Module
{
    public final Value<Integer> health;
    public final Value<Integer> delayA;
    public final Value<Integer> delay;
    public static boolean switchInProgress;
    public static int offhand_delay;
    boolean didFirst;
    int switchDelay1;
    int switchDelay2;
    int slot;
    
    public AutoTotem() {
        super("AutoTotem", "Automatically places totems in your offhand", 0, Category.COMBAT, true);
        this.health = this.register(new Value<Integer>("Health", this, 20, 1, 22));
        this.delayA = this.register(new Value<Integer>("Delay", this, 1, 0, 5));
        this.delay = this.register(new Value<Integer>("Offhand Delay", this, 5, 0, 20));
        this.didFirst = false;
        this.switchDelay1 = -1;
        this.switchDelay2 = -1;
        this.slot = -1;
    }
    
    @Override
    public void onUpdate() {
        if (AutoTotem.switchInProgress) {
            return;
        }
        if (AutoTotem.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (AutoTotem.offhand_delay > 0) {
            --AutoTotem.offhand_delay;
        }
        if (this.shouldTotem() && (AutoTotem.mc.player.getHeldItemOffhand() == ItemStack.EMPTY || AutoTotem.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING)) {
            final int slot = (this.getTotemSlot() < 9) ? (this.getTotemSlot() + 36) : this.getTotemSlot();
            if (this.getTotemSlot() != -1) {
                this.slot = slot;
                AutoTotem.switchInProgress = true;
                AutoTotem.offhand_delay = this.delay.getValue();
            }
        }
    }
    
    @EventTarget
    public void onPlayerUpdate(final LocalPlayerUpdateEvent event) {
        if (AutoTotem.switchInProgress) {
            if (this.switchDelay1 > 0) {
                --this.switchDelay1;
            }
            if (this.switchDelay2 > 0) {
                --this.switchDelay2;
            }
            if (!this.didFirst) {
                AutoTotem.mc.playerController.windowClick(0, this.slot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                this.switchDelay1 = this.delayA.getValue();
                this.didFirst = true;
                return;
            }
            if (this.switchDelay1 == 0) {
                AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                this.switchDelay1 = -1;
                this.switchDelay2 = this.delayA.getValue();
                return;
            }
            if (this.switchDelay2 == 0) {
                AutoTotem.mc.playerController.windowClick(0, this.slot, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                this.switchDelay1 = -1;
                this.switchDelay2 = -1;
                this.slot = -1;
                AutoTotem.switchInProgress = false;
                this.didFirst = false;
            }
        }
    }
    
    private boolean shouldTotem() {
        return AutoTotem.mc.player.getHealth() + AutoTotem.mc.player.getAbsorptionAmount() <= (Xulu.MODULE_MANAGER.getModuleT(Offhand.class).conserveGapples.getValue() ? ((!Surround.isExposed() && isFullArmor((EntityPlayer)AutoTotem.mc.player)) ? 6 : this.health.getValue()) : this.health.getValue()) || AutoTotem.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA;
    }
    
    public static boolean isFullArmor(final EntityPlayer entity) {
        boolean fullArmor = true;
        int diamondItems = 0;
        boolean hasBlast = false;
        for (final ItemStack is : entity.getArmorInventoryList()) {
            if (is.isEmpty()) {
                fullArmor = false;
                break;
            }
            if (is.getItem() == Items.DIAMOND_HELMET) {
                ++diamondItems;
            }
            if (is.getItem() == Items.DIAMOND_CHESTPLATE) {
                ++diamondItems;
            }
            if (is.getItem() == Items.DIAMOND_LEGGINGS) {
                ++diamondItems;
            }
            if (is.getItem() == Items.DIAMOND_BOOTS) {
                ++diamondItems;
            }
            final NBTTagList enchants = is.getEnchantmentTagList();
            final List<String> enchantments = new ArrayList<String>();
            if (enchants != null) {
                for (int index = 0; index < enchants.tagCount(); ++index) {
                    final short id = enchants.getCompoundTagAt(index).getShort("id");
                    final short level = enchants.getCompoundTagAt(index).getShort("lvl");
                    final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                    if (enc != null) {
                        enchantments.add(enc.getTranslatedName((int)level));
                    }
                }
            }
            if (!enchantments.contains("Blast Protection IV")) {
                continue;
            }
            hasBlast = true;
        }
        return fullArmor && diamondItems == 4 && hasBlast;
    }
    
    int getTotemSlot() {
        int totemSlot = -1;
        for (int i = 45; i > -1; --i) {
            if (AutoTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }
        return totemSlot;
    }
    
    @Override
    public String getHudInfo() {
        int items = 0;
        for (int i = 45; i > -1; --i) {
            if (AutoTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                items += AutoTotem.mc.player.inventory.getStackInSlot(i).getCount();
            }
        }
        return items + "";
    }
    
    static {
        AutoTotem.switchInProgress = false;
        AutoTotem.offhand_delay = 0;
    }
}
