package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class EnchantmentModifier extends Block {

    public EnchantmentModifier() {
        super("EnchantmentModifier", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Words,
                BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "ItemStack: ";
        this.words[1] = "Entity: ";
        this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Enchantment Modifier EnchantmentHelper.getModifierForCreature(ItemStack, EntityLivingBase)";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
       return EnchantmentHelper.getModifierForCreature((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event)).getCreatureAttribute());
    }

}
