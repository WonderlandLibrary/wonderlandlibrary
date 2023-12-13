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

public class TabSword
extends CreativeTabs {
	ItemStack empty = new ItemStack(Blocks.air);


    public TabSword() {
        super(14, "SwordTab");
    }

    @Override
    public void displayAllReleventItems(List<ItemStack> itemList) {
        String[] array;
        String[] arrstring = array = new String[]{"{Unbreakable:1,ench:[{id:16,lvl:1000}]}", "{Unbreakable:1,ench:[{id:21,lvl:1000}]}", "{Unbreakable:1,ench:[{id:16,lvl:1000},{id:21,lvl:1000}]}", "{Unbreakable:1,ench:[{id:19,lvl:5}]}", "{Unbreakable:1,ench:[{id:19,lvl:1000}]}"};
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack[] array1;
            String NBT = arrstring[n2];
            ItemStack diamond_sword = new ItemStack(Items.diamond_sword);
            ItemStack golden_sword = new ItemStack(Items.golden_sword);
            ItemStack iron_sword = new ItemStack(Items.iron_sword);
            ItemStack stone_sword = new ItemStack(Items.stone_sword);
            ItemStack wooden_sword = new ItemStack(Items.wooden_sword);
            ItemStack stick = new ItemStack(Items.stick);
            ItemStack[] arritemStack = array1 = new ItemStack[]{diamond_sword, golden_sword, iron_sword, stone_sword, wooden_sword, stick};
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
            itemList.add(this.empty);
            itemList.add(this.empty);
            itemList.add(this.empty);
            ++n2;
        }
    }

    @Override
    public String getTranslatedTabLabel() {
        return "Swords";
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(Items.diamond_sword);
    }

	@Override
	public Item getTabIconItem() {
		// TODO Auto-generated method stub
		return null;
	}
}

