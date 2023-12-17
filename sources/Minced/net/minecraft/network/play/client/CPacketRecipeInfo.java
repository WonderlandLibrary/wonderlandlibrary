// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketRecipeInfo implements Packet<INetHandlerPlayServer>
{
    private Purpose purpose;
    private IRecipe recipe;
    private boolean isGuiOpen;
    private boolean filteringCraftable;
    
    public CPacketRecipeInfo() {
    }
    
    public CPacketRecipeInfo(final IRecipe p_i47518_1_) {
        this.purpose = Purpose.SHOWN;
        this.recipe = p_i47518_1_;
    }
    
    public CPacketRecipeInfo(final boolean p_i47424_1_, final boolean p_i47424_2_) {
        this.purpose = Purpose.SETTINGS;
        this.isGuiOpen = p_i47424_1_;
        this.filteringCraftable = p_i47424_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.purpose = buf.readEnumValue(Purpose.class);
        if (this.purpose == Purpose.SHOWN) {
            this.recipe = CraftingManager.getRecipeById(buf.readInt());
        }
        else if (this.purpose == Purpose.SETTINGS) {
            this.isGuiOpen = buf.readBoolean();
            this.filteringCraftable = buf.readBoolean();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.purpose);
        if (this.purpose == Purpose.SHOWN) {
            buf.writeInt(CraftingManager.getIDForRecipe(this.recipe));
        }
        else if (this.purpose == Purpose.SETTINGS) {
            buf.writeBoolean(this.isGuiOpen);
            buf.writeBoolean(this.filteringCraftable);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.handleRecipeBookUpdate(this);
    }
    
    public Purpose getPurpose() {
        return this.purpose;
    }
    
    public IRecipe getRecipe() {
        return this.recipe;
    }
    
    public boolean isGuiOpen() {
        return this.isGuiOpen;
    }
    
    public boolean isFilteringCraftable() {
        return this.filteringCraftable;
    }
    
    public enum Purpose
    {
        SHOWN, 
        SETTINGS;
    }
}
