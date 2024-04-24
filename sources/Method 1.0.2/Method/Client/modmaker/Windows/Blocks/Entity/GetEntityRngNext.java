package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;

public class GetEntityRngNext extends Block {

    public GetEntityRngNext() {
        super("GetEntityRngNext", MainBlockType.Numbers, Tabs.Entity, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns rng of entity";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getRNG().nextDouble();
    }

}
