package me.aidanmees.trivia.CreaTab;

import java.util.List;
import java.util.Random;

import me.aidanmees.trivia.client.tools.ItemStackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public class TabArmor
extends CreativeTabs {


    public TabArmor() {
        super(13, "ArmorTab");
    }

    @Override
    public void displayAllReleventItems(List<ItemStack> itemList) {
        String[] array;
        String[] arrstring = array = new String[]{"{Unbreakable:1,ench:[{id:0,lvl:1000}]}", "{AttributeModifiers:[{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:1,Operation:0,UUIDLeast:722576,UUIDMost:658559,Slot:\"head\"}],Unbreakable:1,ench:[{id:0,lvl:1000}]}", "{Unbreakable:1,ench:[{id:0,lvl:1000},{id:7,lvl:1000}]}", "{AttributeModifiers:[{AttributeName:\"generic.knockbackResistance\",Name:\"generic.knockbackResistance\",Amount:1,Operation:0,UUIDLeast:859071,UUIDMost:670308}],Unbreakable:1,ench:[{id:0,lvl:1000},{id:7,lvl:1000}]}"};
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack[] array1;
            String NBT = arrstring[n2];
            ItemStack diamond_helmet = new ItemStack(Items.diamond_helmet);
            ItemStack diamond_chestplate = new ItemStack(Items.diamond_chestplate);
            ItemStack diamond_leggings = new ItemStack(Items.diamond_leggings);
            ItemStack diamond_boots = new ItemStack(Items.diamond_boots);
            
            
            ItemStack[] arritemStack = array1 = new ItemStack[]{diamond_helmet, diamond_chestplate, diamond_leggings, diamond_boots};
            int n3 = arritemStack.length;
            int n4 = 0;
            while (n4 < n3) {
                ItemStack stack = arritemStack[n4];
                try {
                    stack.setTagCompound(JsonToNBT.getTagFromJson(NBT));
                }
                catch (NBTException nBTException) {
                    // empty catch block
                }
                
                itemList.add(stack);
                ++n4;
                
            }
            ItemStackUtil.fillEmpty(itemList);
            ++n2;
        }
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(Items.diamond_chestplate);
    }

    @Override
    public String getTranslatedTabLabel() {
        return "Armor";
    }

	@Override
	public Item getTabIconItem() {
		// TODO Auto-generated method stub
		return null;
	}
}

