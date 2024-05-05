package net.minecraft.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public static class BlockAnvil.Anvil
implements IInteractionObject {
    private final World world;
    private final BlockPos position;

    public BlockAnvil.Anvil(World worldIn, BlockPos pos) {
        this.world = worldIn;
        this.position = pos;
    }

    public String getName() {
        return "anvil";
    }

    public boolean hasCustomName() {
        return false;
    }

    public IChatComponent getDisplayName() {
        return new ChatComponentTranslation(Blocks.anvil.getUnlocalizedName() + ".name", new Object[0]);
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerRepair(playerInventory, this.world, this.position, playerIn);
    }

    public String getGuiID() {
        return "minecraft:anvil";
    }
}
