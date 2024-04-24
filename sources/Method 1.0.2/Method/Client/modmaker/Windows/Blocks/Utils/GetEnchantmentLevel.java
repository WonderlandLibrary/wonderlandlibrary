package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;

public class GetEnchantmentLevel extends Block {

    public GetEnchantmentLevel() {
        super("GetEnchantmentLevel", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        this.words[0] = "Enchantment";
        ddOptions.add("PROTECTION");
        ddOptions.add("FIRE_PROTECTION");
        ddOptions.add("FEATHER_FALLING");
        ddOptions.add("BLAST_PROTECTION");
        ddOptions.add("PROJECTILE_PROTECTION");
        ddOptions.add("RESPIRATION");
        ddOptions.add("AQUA_AFFINITY");
        ddOptions.add("THORNS");
        ddOptions.add("DEPTH_STRIDER");
        ddOptions.add("FROST_WALKER");
        ddOptions.add("BINDING_CURSE");
        ddOptions.add("SHARPNESS");
        ddOptions.add("SMITE");
        ddOptions.add("BANE_OF_ARTHROPODS");
        ddOptions.add("KNOCKBACK");
        ddOptions.add("FIRE_ASPECT");
        ddOptions.add("LOOTING");
        ddOptions.add("SWEEPING");
        ddOptions.add("EFFICIENCY");
        ddOptions.add("SILK_TOUCH");
        ddOptions.add("UNBREAKING");
        ddOptions.add("FORTUNE");
        ddOptions.add("POWER");
        ddOptions.add("PUNCH");
        ddOptions.add("FLAME");
        ddOptions.add("INFINITY");
        ddOptions.add("LUCK_OF_THE_SEA");
        ddOptions.add("LURE");
        ddOptions.add("MENDING");
        ddOptions.add("VANISHING_CURSE");


        this.description = "Gets the material of the block. IBlockState";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        ItemStack stack = ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
        switch (dragableBlock.dropDowns.getSelected()) {
            case "PROTECTION":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack);
            case "FIRE_PROTECTION":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, stack);
            case "FEATHER_FALLING":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, stack);
            case "BLAST_PROTECTION":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);
            case "PROJECTILE_PROTECTION":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.PROJECTILE_PROTECTION, stack);
            case "RESPIRATION":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.RESPIRATION, stack);
            case "AQUA_AFFINITY":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.AQUA_AFFINITY, stack);
            case "THORNS":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, stack);
            case "DEPTH_STRIDER":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack);
            case "FROST_WALKER":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.FROST_WALKER, stack);
            case "BINDING_CURSE":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE, stack);
            case "SHARPNESS":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
            case "SMITE":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, stack);
            case "BANE_OF_ARTHROPODS":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS, stack);
            case "KNOCKBACK":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
            case "FIRE_ASPECT":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
            case "LOOTING":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
            case "SWEEPING":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
            case "EFFICIENCY":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
            case "SILK_TOUCH":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
            case "UNBREAKING":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
            case "FORTUNE":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            case "POWER":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
            case "PUNCH":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
            case "FLAME":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);
            case "INFINITY":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
            case "LUCK_OF_THE_SEA":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, stack);
            case "LURE":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.LURE, stack);
            case "MENDING":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
            case "VANISHING_CURSE":
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, stack);
            default:
                return 0;
        }
    }


}
