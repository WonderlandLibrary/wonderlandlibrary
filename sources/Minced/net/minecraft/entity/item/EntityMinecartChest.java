// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMinecartChest extends EntityMinecartContainer
{
    public EntityMinecartChest(final World worldIn) {
        super(worldIn);
    }
    
    public EntityMinecartChest(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    public static void registerFixesMinecartChest(final DataFixer fixer) {
        EntityMinecartContainer.addDataFixers(fixer, EntityMinecartChest.class);
    }
    
    @Override
    public void killMinecart(final DamageSource source) {
        super.killMinecart(source);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.CHEST), 1, 0.0f);
        }
    }
    
    @Override
    public int getSizeInventory() {
        return 27;
    }
    
    @Override
    public Type getType() {
        return Type.CHEST;
    }
    
    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.CHEST.getDefaultState().withProperty((IProperty<Comparable>)BlockChest.FACING, EnumFacing.NORTH);
    }
    
    @Override
    public int getDefaultDisplayTileOffset() {
        return 8;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:chest";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        this.addLoot(playerIn);
        return new ContainerChest(playerInventory, this, playerIn);
    }
}
