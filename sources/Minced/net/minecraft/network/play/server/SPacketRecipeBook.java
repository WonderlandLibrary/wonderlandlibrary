// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.crafting.IRecipe;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketRecipeBook implements Packet<INetHandlerPlayClient>
{
    private State state;
    private List<IRecipe> recipes;
    private List<IRecipe> displayedRecipes;
    private boolean guiOpen;
    private boolean filteringCraftable;
    
    public SPacketRecipeBook() {
    }
    
    public SPacketRecipeBook(final State stateIn, final List<IRecipe> recipesIn, final List<IRecipe> displayedRecipesIn, final boolean isGuiOpen, final boolean p_i47597_5_) {
        this.state = stateIn;
        this.recipes = recipesIn;
        this.displayedRecipes = displayedRecipesIn;
        this.guiOpen = isGuiOpen;
        this.filteringCraftable = p_i47597_5_;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleRecipeBook(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.state = buf.readEnumValue(State.class);
        this.guiOpen = buf.readBoolean();
        this.filteringCraftable = buf.readBoolean();
        int i = buf.readVarInt();
        this.recipes = (List<IRecipe>)Lists.newArrayList();
        for (int j = 0; j < i; ++j) {
            this.recipes.add(CraftingManager.getRecipeById(buf.readVarInt()));
        }
        if (this.state == State.INIT) {
            i = buf.readVarInt();
            this.displayedRecipes = (List<IRecipe>)Lists.newArrayList();
            for (int k = 0; k < i; ++k) {
                this.displayedRecipes.add(CraftingManager.getRecipeById(buf.readVarInt()));
            }
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.state);
        buf.writeBoolean(this.guiOpen);
        buf.writeBoolean(this.filteringCraftable);
        buf.writeVarInt(this.recipes.size());
        for (final IRecipe irecipe : this.recipes) {
            buf.writeVarInt(CraftingManager.getIDForRecipe(irecipe));
        }
        if (this.state == State.INIT) {
            buf.writeVarInt(this.displayedRecipes.size());
            for (final IRecipe irecipe2 : this.displayedRecipes) {
                buf.writeVarInt(CraftingManager.getIDForRecipe(irecipe2));
            }
        }
    }
    
    public List<IRecipe> getRecipes() {
        return this.recipes;
    }
    
    public List<IRecipe> getDisplayedRecipes() {
        return this.displayedRecipes;
    }
    
    public boolean isGuiOpen() {
        return this.guiOpen;
    }
    
    public boolean isFilteringCraftable() {
        return this.filteringCraftable;
    }
    
    public State getState() {
        return this.state;
    }
    
    public enum State
    {
        INIT, 
        ADD, 
        REMOVE;
    }
}
