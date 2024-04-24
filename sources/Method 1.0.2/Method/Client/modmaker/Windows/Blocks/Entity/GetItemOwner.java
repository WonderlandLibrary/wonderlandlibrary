package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.item.EntityItem;

public class GetItemOwner extends Block {

    public GetItemOwner() {
        super("GetItemOwner", MainBlockType.String, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Owner of item" + "\n" + "(Entityid).Owner()";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((EntityItem) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).owner;
    }

}
