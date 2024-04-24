package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;

public class GetEntityId extends Block {

    public GetEntityId() {
        super("GetEntityId", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Id of entity" + "\n" + "(Entity).getEntityId()";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getEntityId();
    }

}
