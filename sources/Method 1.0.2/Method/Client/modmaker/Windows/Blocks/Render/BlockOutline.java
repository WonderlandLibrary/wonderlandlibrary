package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.util.math.AxisAlignedBB;

public class BlockOutline extends Block {

    public BlockOutline() {
        super("BlockOutline", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        //String s, AxisAlignedBB bb, int c, Double width

        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.BoundingBox));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

        this.words[0] = "BoundingBox:";
        this.words[1] = "Color:";
        this.words[2] = "Width:";


        this.description = "Renders a block outline" + "\n" + "BoundingBox: The bounding box of the block" + "\n" + "Color: The color of the outline" + "\n" + "Width: The width of the outline"
        + "\n" + "   Types:     Outline Full Flat FlatOutline Beacon Xspot Tracer Shape None\n";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        RenderUtils.RenderBlock((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), (AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event));
        super.runCode(dragableBlock, event);
    }


}
