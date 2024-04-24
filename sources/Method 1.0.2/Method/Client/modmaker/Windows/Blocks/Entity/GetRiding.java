package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;

public class GetRiding extends Block {

    public GetRiding() {
        super("GetRiding", MainBlockType.Entity, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Given a Living Entity returns riding Entity  " + "\n" + "(EntityLivingBase).getRidingEntity";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getRidingEntity();
    }

}
