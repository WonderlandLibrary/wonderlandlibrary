package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class BlockWorkbench extends Block {
   public boolean onBlockActivated(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumFacing var5, float var6, float var7, float var8) {
      if (var1.isRemote) {
         return true;
      } else {
         var4.displayGui(new BlockWorkbench.InterfaceCraftingTable(var1, var2));
         var4.triggerAchievement(StatList.field_181742_Z);
         return true;
      }
   }

   protected BlockWorkbench() {
      super(Material.wood);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public static class InterfaceCraftingTable implements IInteractionObject {
      private final World world;
      private final BlockPos position;

      public IChatComponent getDisplayName() {
         return new ChatComponentTranslation(Blocks.crafting_table.getUnlocalizedName() + ".name", new Object[0]);
      }

      public String getName() {
         return null;
      }

      public boolean hasCustomName() {
         return false;
      }

      public String getGuiID() {
         return "minecraft:crafting_table";
      }

      public InterfaceCraftingTable(World var1, BlockPos var2) {
         this.world = var1;
         this.position = var2;
      }

      public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
         return new ContainerWorkbench(var1, this.world, this.position);
      }
   }
}
