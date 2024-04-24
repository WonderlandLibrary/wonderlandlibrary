package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.item.EntityItem;

public class SetItemOwner extends Block {

    public SetItemOwner() {
        super("SetItemOwner", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.words[0] = "Owner";


        this.description = "Set Item Owner";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).owner = (String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event);
        super.runCode(dragableBlock, event);
    }
}
