package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.monster.EntitySlime;

public class GetSlimeSize extends Block {

    public GetSlimeSize() {
        super("GetSlimeSize", MainBlockType.Numbers, Tabs.Entity,BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns Slime Size of EntitySlime" + "\n" + "(EntitySlime).getSlimeSize";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((EntitySlime) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getSlimeSize();
    }

}
