package Method.Client.utils.Creativetabs;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;

public class Creativetabhelper {


    public static ItemStack ItemWithEnchants(Item foe, ArrayList<Enchantment> enchants, ArrayList<Integer> levels) {
        ItemStack item = new ItemStack(foe);

        try {
            int forlevels = 0;


            for (Enchantment ench : enchants) {
                if (item.stackTagCompound == null) {
                    item.setTagCompound(new NBTTagCompound());
                }
                if (!item.stackTagCompound.hasKey("ench", 9)) {
                    item.stackTagCompound.setTag("ench", new NBTTagList());
                }
                NBTTagList nbttaglist = item.stackTagCompound.getTagList("ench", 10);
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setShort("id", (short) Enchantment.getEnchantmentID(ench));
                nbttagcompound.setInteger("lvl", levels.get(forlevels)); // Really mc, I have to use this vs the built in set enchantment....
                nbttaglist.appendTag(nbttagcompound);

                forlevels++;
            }

        } catch (Exception ignored) {
        }
        return item;
    }

    static void Unbreakpack(Item item, ArrayList<Enchantment> enchants, ArrayList<Integer> levels, NonNullList<ItemStack> itemList) {
        if (enchants == null && levels == null) {
            return;
        }
        ItemStack D32k;

        D32k = ItemWithEnchants(item, enchants, levels);

        D32k.setTagInfo("Unbreakable", new NBTTagInt(1));

        itemList.add(D32k);
    }

    static void Attributeitems(Item item, ArrayList<Enchantment> enchants, ArrayList<Integer> levels, NonNullList<ItemStack> itemList, EntityEquipmentSlot ro, boolean Enchanted) {
        if (enchants == null && levels == null) {
            Enchanted = false;
        }
        ItemStack D32k;
        if (Enchanted)
            D32k = ItemWithEnchants(item, enchants, levels);
        else {
            D32k = new ItemStack(item);
            if (D32k.stackTagCompound == null) {
                D32k.setTagCompound(new NBTTagCompound());
            }
        }
        if (ro == EntityEquipmentSlot.MAINHAND) {
            D32k.addAttributeModifier("generic.attackDamage", new AttributeModifier("Weapon modifier", 20, 0), ro);
            D32k.addAttributeModifier("generic.attackDamage", new AttributeModifier("Weapon modifier", 20, 0), EntityEquipmentSlot.OFFHAND);

            D32k.addAttributeModifier("generic.attackSpeed", new AttributeModifier("Tool modifier", 20, 0), ro);
            D32k.addAttributeModifier("generic.attackSpeed", new AttributeModifier("Tool modifier", 20, 0), EntityEquipmentSlot.OFFHAND);

            D32k.addAttributeModifier("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2, 0), ro);
            D32k.addAttributeModifier("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2, 0), EntityEquipmentSlot.OFFHAND);

            D32k.addAttributeModifier("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200, 0), ro);
            D32k.addAttributeModifier("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200, 0), EntityEquipmentSlot.OFFHAND);

        }
        if (ro == EntityEquipmentSlot.OFFHAND) {
            D32k.addAttributeModifier("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2, 0), EntityEquipmentSlot.MAINHAND);
            D32k.addAttributeModifier("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2, 0), EntityEquipmentSlot.OFFHAND);

        }
        if (ro == EntityEquipmentSlot.HEAD) {
            D32k.addAttributeModifier("generic.maxHealth", new AttributeModifier("effect.healthBoost", -20, 0), EntityEquipmentSlot.MAINHAND);
            D32k.addAttributeModifier("generic.maxHealth", new AttributeModifier("effect.healthBoost", -20, 0), EntityEquipmentSlot.OFFHAND);

        }
        if (ro == EntityEquipmentSlot.CHEST) {
            D32k.addAttributeModifier("generic.attackDamage", new AttributeModifier("Weapon modifier", 20, 0), EntityEquipmentSlot.HEAD);
            D32k.addAttributeModifier("generic.attackDamage", new AttributeModifier("Weapon modifier", 20, 0), EntityEquipmentSlot.CHEST);
            D32k.addAttributeModifier("generic.attackDamage", new AttributeModifier("Weapon modifier", 20, 0), EntityEquipmentSlot.LEGS);
            D32k.addAttributeModifier("generic.attackDamage", new AttributeModifier("Weapon modifier", 20, 0), EntityEquipmentSlot.FEET);

            D32k.addAttributeModifier("generic.attackSpeed", new AttributeModifier("Tool modifier", 20, 0), EntityEquipmentSlot.HEAD);
            D32k.addAttributeModifier("generic.attackSpeed", new AttributeModifier("Tool modifier", 20, 0), EntityEquipmentSlot.CHEST);
            D32k.addAttributeModifier("generic.attackSpeed", new AttributeModifier("Tool modifier", 20, 0), EntityEquipmentSlot.LEGS);
            D32k.addAttributeModifier("generic.attackSpeed", new AttributeModifier("Tool modifier", 20, 0), EntityEquipmentSlot.FEET);

            D32k.addAttributeModifier("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2, 0), EntityEquipmentSlot.HEAD);
            D32k.addAttributeModifier("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2, 0), EntityEquipmentSlot.CHEST);
            D32k.addAttributeModifier("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2, 0), EntityEquipmentSlot.LEGS);
            D32k.addAttributeModifier("generic.movementSpeed", new AttributeModifier("Sprinting speed boost", 2, 0), EntityEquipmentSlot.FEET);

            D32k.addAttributeModifier("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200, 0), EntityEquipmentSlot.HEAD);
            D32k.addAttributeModifier("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200, 0), EntityEquipmentSlot.CHEST);
            D32k.addAttributeModifier("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200, 0), EntityEquipmentSlot.LEGS);
            D32k.addAttributeModifier("generic.maxHealth", new AttributeModifier("effect.healthBoost 255", 200, 0), EntityEquipmentSlot.FEET);


            D32k.addAttributeModifier("generic.armorToughness", new AttributeModifier("Armor toughness", 20, 0), EntityEquipmentSlot.HEAD);
            D32k.addAttributeModifier("generic.armorToughness", new AttributeModifier("Armor toughness", 20, 0), EntityEquipmentSlot.CHEST);
            D32k.addAttributeModifier("generic.armorToughness", new AttributeModifier("Armor toughness", 20, 0), EntityEquipmentSlot.LEGS);
            D32k.addAttributeModifier("generic.armorToughness", new AttributeModifier("Armor toughness", 20, 0), EntityEquipmentSlot.FEET);
        }

        itemList.add(D32k);
    }


    static void Packsize(Item item, ArrayList<Enchantment> enchants, ArrayList<Integer> levels, NonNullList<ItemStack> itemList) {
        if (enchants == null && levels == null) {
            return;
        }
        ItemStack D32k = ItemWithEnchants(item, enchants, levels);
        itemList.add(D32k);

        ItemStack DSword64 = ItemWithEnchants(item, enchants, levels);
        DSword64.setCount(64);
        itemList.add(DSword64);

        ItemStack Damaged = ItemWithEnchants(item, enchants, levels);
        Damaged.setItemDamage(Damaged.getMaxDamage() + 100);
        itemList.add(Damaged);

        ItemStack Damage64 = ItemWithEnchants(item, enchants, levels);
        Damage64.setCount(64);
        Damage64.setItemDamage(Damage64.getMaxDamage() + 100);
        itemList.add(Damage64);
    }



}
