package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventAttack;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.modules.Module;

public class ModuleAutoSword extends Module implements EventListener {
   public ModuleAutoSword() {
      super("AutoSword", "Automatically selects the best weapon to attack with", new String[]{"autosword"}, Module.Category.Combat, 423646);
   }

   public void onEvent(Event event) {
      if(event instanceof EventAttack && !((EventAttack)event).isCancelled()) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         int slot = -1;
         float dmg = 0.0F;

         for(int i = 0; i < 9; ++i) {
            ItemStack stack = player.inventory.mainInventory[i];
            float tdmg = this.getItemRateing(stack);
            if(tdmg > dmg) {
               slot = i;
               dmg = tdmg;
            }
         }

         if(slot != -1 && (double)dmg > 0.5D) {
            player.inventory.currentItem = slot;
            AC.getMC().playerController.syncCurrentPlayItem();
         }
      }

   }

   private float getItemRateing(ItemStack itemStack) {
      if(itemStack == null) {
         return 0.0F;
      } else {
         Item item = itemStack.getItem();
         float enchMod = 0.0F;
         if(itemStack.isItemEnchanted()) {
            for(int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); ++i) {
               NBTTagCompound tag = itemStack.getEnchantmentTagList().getCompoundTagAt(i);
               short id = tag.getShort("id");
               short lvl = tag.getShort("lvl");
               if(id == Enchantment.sharpness.effectId) {
                  enchMod += 0.5F * (float)lvl;
               }
            }
         }

         return item instanceof ItemSword?((ItemSword)item).attackDamage + enchMod:(item instanceof ItemTool?((ItemTool)item).damageVsEntity + enchMod:0.5F + enchMod);
      }
   }
}
