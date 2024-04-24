package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class GetDistanceToMouse extends Block {

    public GetDistanceToMouse() {
        super("GetDistanceToMouse", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));

        this.description = "Returns the distance to the mouse ( Attack fov is 50)";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        Entity entity = ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
       return Utils.fovDistanceFromMouse((EntityLivingBase) entity);

    }

}
