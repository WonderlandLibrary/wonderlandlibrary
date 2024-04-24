package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class BlockStrength extends Block {

    public BlockStrength() {
        super("BlockStrength", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.words[0] = "Itemstack";
        this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        this.description = " Returns the strength of the block at the given position.";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return blockStrength((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event), (ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event));
    }


    private float blockStrength(BlockPos pos, ItemStack stack) {
        float hardness = Minecraft.getMinecraft().world.getBlockState(pos).getBlockHardness(Minecraft.getMinecraft().world, pos);
        if (hardness < 0.0F)
            return 0.0F;

        return getDigSpeed(Minecraft.getMinecraft().world.getBlockState(pos), pos, stack) / hardness / (!canHarvestBlock(Minecraft.getMinecraft().world.getBlockState(pos).getBlock(), pos, stack) ? 100F : 30F);
    }

    private boolean canHarvestBlock(net.minecraft.block.Block block, BlockPos pos, ItemStack stack) {
        IBlockState state = Minecraft.getMinecraft().world.getBlockState(pos);
        state = state.getBlock().getActualState(state, Minecraft.getMinecraft().world, pos);

        if (state.getMaterial().isToolNotRequired())
            return true;

        String tool = block.getHarvestTool(state);

        if (stack.isEmpty() || tool == null) {
            return Minecraft.getMinecraft().player.canHarvestBlock(state);
        }

        final int toolLevel = stack.getItem().getHarvestLevel(stack, tool, Minecraft.getMinecraft().player, state);

        if (toolLevel < 0) {
            return Minecraft.getMinecraft().player.canHarvestBlock(state);
        }

        return toolLevel >= block.getHarvestLevel(state);
    }


    private float getDigSpeed(IBlockState state, BlockPos pos, ItemStack stack) {
        float f = stack.getDestroySpeed(state);

        if (f > 1.0F) {
            int i = EnchantmentHelper.getEfficiencyModifier(Minecraft.getMinecraft().player);
            if (i > 0 && !stack.isEmpty()) f += (float) (i * i + 1);
        }

        if (Minecraft.getMinecraft().player.isPotionActive(MobEffects.HASTE)) {
            f *= 1.0F + (float) (Objects.requireNonNull(Minecraft.getMinecraft().player.getActivePotionEffect(MobEffects.HASTE)).getAmplifier() + 1) * 0.2F;
        }

        if (Minecraft.getMinecraft().player.isPotionActive(MobEffects.MINING_FATIGUE)) {
            float f1;

            switch (Objects.requireNonNull(Minecraft.getMinecraft().player.getActivePotionEffect(MobEffects.MINING_FATIGUE)).getAmplifier()) {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (Minecraft.getMinecraft().player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(Minecraft.getMinecraft().player))
            f /= 5.0F;

        if (!Minecraft.getMinecraft().player.onGround) {
            f /= 5.0F;
        }

        f = net.minecraftforge.event.ForgeEventFactory.getBreakSpeed(Minecraft.getMinecraft().player, state, f, pos);
        return (f < 0 ? 0 : f);
    }

}
