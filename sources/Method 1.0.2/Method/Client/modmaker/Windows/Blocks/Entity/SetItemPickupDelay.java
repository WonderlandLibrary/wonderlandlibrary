package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.item.EntityItem;

public class SetItemPickupDelay extends Block {

    public SetItemPickupDelay() {
        super("SetItemPickupDelay", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "Delay";


        this.description = "Set Item Pickup Delay";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).pickupDelay = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
        super.runCode(dragableBlock, event);
    }
}
