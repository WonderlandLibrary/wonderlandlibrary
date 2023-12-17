// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.advancements.CriteriaTriggers;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class RecipeBookServer extends RecipeBook
{
    private static final Logger LOGGER;
    
    public void add(final List<IRecipe> recipesIn, final EntityPlayerMP player) {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (final IRecipe irecipe : recipesIn) {
            if (!this.recipes.get(RecipeBook.getRecipeId(irecipe)) && !irecipe.isDynamic()) {
                this.unlock(irecipe);
                this.markNew(irecipe);
                list.add(irecipe);
                CriteriaTriggers.RECIPE_UNLOCKED.trigger(player, irecipe);
            }
        }
        this.sendPacket(SPacketRecipeBook.State.ADD, player, list);
    }
    
    public void remove(final List<IRecipe> recipesIn, final EntityPlayerMP player) {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (final IRecipe irecipe : recipesIn) {
            if (this.recipes.get(RecipeBook.getRecipeId(irecipe))) {
                this.lock(irecipe);
                list.add(irecipe);
            }
        }
        this.sendPacket(SPacketRecipeBook.State.REMOVE, player, list);
    }
    
    private void sendPacket(final SPacketRecipeBook.State state, final EntityPlayerMP player, final List<IRecipe> recipesIn) {
        player.connection.sendPacket(new SPacketRecipeBook(state, recipesIn, Collections.emptyList(), this.isGuiOpen, this.isFilteringCraftable));
    }
    
    public NBTTagCompound write() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setBoolean("isGuiOpen", this.isGuiOpen);
        nbttagcompound.setBoolean("isFilteringCraftable", this.isFilteringCraftable);
        final NBTTagList nbttaglist = new NBTTagList();
        for (final IRecipe irecipe : this.getRecipes()) {
            nbttaglist.appendTag(new NBTTagString(CraftingManager.REGISTRY.getNameForObject(irecipe).toString()));
        }
        nbttagcompound.setTag("recipes", nbttaglist);
        final NBTTagList nbttaglist2 = new NBTTagList();
        for (final IRecipe irecipe2 : this.getDisplayedRecipes()) {
            nbttaglist2.appendTag(new NBTTagString(CraftingManager.REGISTRY.getNameForObject(irecipe2).toString()));
        }
        nbttagcompound.setTag("toBeDisplayed", nbttaglist2);
        return nbttagcompound;
    }
    
    public void read(final NBTTagCompound tag) {
        this.isGuiOpen = tag.getBoolean("isGuiOpen");
        this.isFilteringCraftable = tag.getBoolean("isFilteringCraftable");
        final NBTTagList nbttaglist = tag.getTagList("recipes", 8);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final ResourceLocation resourcelocation = new ResourceLocation(nbttaglist.getStringTagAt(i));
            final IRecipe irecipe = CraftingManager.getRecipe(resourcelocation);
            if (irecipe == null) {
                RecipeBookServer.LOGGER.info("Tried to load unrecognized recipe: {} removed now.", (Object)resourcelocation);
            }
            else {
                this.unlock(irecipe);
            }
        }
        final NBTTagList nbttaglist2 = tag.getTagList("toBeDisplayed", 8);
        for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
            final ResourceLocation resourcelocation2 = new ResourceLocation(nbttaglist2.getStringTagAt(j));
            final IRecipe irecipe2 = CraftingManager.getRecipe(resourcelocation2);
            if (irecipe2 == null) {
                RecipeBookServer.LOGGER.info("Tried to load unrecognized recipe: {} removed now.", (Object)resourcelocation2);
            }
            else {
                this.markNew(irecipe2);
            }
        }
    }
    
    private List<IRecipe> getRecipes() {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (int i = this.recipes.nextSetBit(0); i >= 0; i = this.recipes.nextSetBit(i + 1)) {
            list.add(CraftingManager.REGISTRY.getObjectById(i));
        }
        return list;
    }
    
    private List<IRecipe> getDisplayedRecipes() {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (int i = this.newRecipes.nextSetBit(0); i >= 0; i = this.newRecipes.nextSetBit(i + 1)) {
            list.add(CraftingManager.REGISTRY.getObjectById(i));
        }
        return list;
    }
    
    public void init(final EntityPlayerMP player) {
        player.connection.sendPacket(new SPacketRecipeBook(SPacketRecipeBook.State.INIT, this.getRecipes(), this.getDisplayedRecipes(), this.isGuiOpen, this.isFilteringCraftable));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
