// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import org.apache.logging.log4j.LogManager;
import net.minecraft.item.crafting.IRecipe;
import java.util.List;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Lists;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

public class ItemKnowledgeBook extends Item
{
    private static final Logger LOGGER;
    
    public ItemKnowledgeBook() {
        this.setMaxStackSize(1);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        final NBTTagCompound nbttagcompound = itemstack.getTagCompound();
        if (!playerIn.capabilities.isCreativeMode) {
            playerIn.setHeldItem(handIn, ItemStack.EMPTY);
        }
        if (nbttagcompound != null && nbttagcompound.hasKey("Recipes", 9)) {
            if (!worldIn.isRemote) {
                final NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 8);
                final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    final String s = nbttaglist.getStringTagAt(i);
                    final IRecipe irecipe = CraftingManager.getRecipe(new ResourceLocation(s));
                    if (irecipe == null) {
                        ItemKnowledgeBook.LOGGER.error("Invalid recipe: " + s);
                        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                    }
                    list.add(irecipe);
                }
                playerIn.unlockRecipes(list);
                playerIn.addStat(StatList.getObjectUseStats(this));
            }
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        ItemKnowledgeBook.LOGGER.error("Tag not valid: " + nbttagcompound);
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
