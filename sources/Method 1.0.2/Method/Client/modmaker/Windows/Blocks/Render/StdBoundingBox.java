package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.util.math.BlockPos;

public class StdBoundingBox extends Block {

    public StdBoundingBox() {
        super("StdBoundingBox", MainBlockType.BoundingBox, Tabs.Render, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.description = "Creates a 1x1x1 bounding box";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return RenderUtils.Standardbb((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }


}
