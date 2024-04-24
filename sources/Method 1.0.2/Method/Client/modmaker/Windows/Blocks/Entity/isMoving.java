package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;
import net.minecraft.entity.Entity;

public class isMoving extends Block {

    public isMoving() {
        super("isMoving", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns true if the entity is moving.";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return Utils.isMoving(((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)));
    }

}
