package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.item.ItemStack;

public class ItemStackConvert extends Block {

    public ItemStackConvert() {
        super("ItemStackConvert", MainBlockType.Items, Tabs.NewItem, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words);
        this.typesAccepted.add(typesCollapse(MainBlockType.ItemStack));
        this.description = "ItemStack Convert returns Item from ItemStack";


    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
       return  ((ItemStack) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getItem();
    }


}
