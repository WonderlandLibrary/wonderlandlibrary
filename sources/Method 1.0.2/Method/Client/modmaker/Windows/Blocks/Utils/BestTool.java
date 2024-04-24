package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class BestTool extends Block {

    public BestTool() {
        super("BestTool", MainBlockType.Numbers, Tabs.Utils,BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.words[0] = "Full Inventory";
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.description = " Returns the index of the best tool for the block at the given position. ";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return getTool((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,1, event) ? 36 : 9);
    }

    private int getTool(BlockPos pos, int slots) {
        int index = -1;
        float CurrentFastest = 1.0f;
        for (int i = 0; i <= slots; i++) {
            final ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                final float digSpeed = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
                final float destroySpeed = stack.getDestroySpeed(Minecraft.getMinecraft().world.getBlockState(pos));

                if (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockEnderChest) {
                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
                        if ((digSpeed + destroySpeed) > CurrentFastest) {
                            CurrentFastest = (digSpeed + destroySpeed);
                            index = i;
                        }
                    }
                    continue;
                }
                if ((digSpeed + destroySpeed) > CurrentFastest) {
                    CurrentFastest = (digSpeed + destroySpeed);
                    index = i;
                }
            }
        }
        return index;
    }

}
