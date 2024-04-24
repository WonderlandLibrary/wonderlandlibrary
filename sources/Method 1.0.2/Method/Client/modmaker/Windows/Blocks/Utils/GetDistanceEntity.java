package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;

public class GetDistanceEntity extends Block {

    public GetDistanceEntity() {
        super("GetDistanceEntity", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Words,
                BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "Distance Entity1: ";
        this.words[1] = "Entity2: ";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns the distance between two entities";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getDistance((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
    }

}
