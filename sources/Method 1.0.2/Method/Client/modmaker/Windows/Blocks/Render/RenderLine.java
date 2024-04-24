package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class RenderLine extends Block {

    public RenderLine() {
        super("RenderLine", MainBlockType.Default, Tabs.Render, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter);

        this.words[0] = "Array:";
        this.words[1] = "Color:";
        this.words[2] = "Width:";
        this.words[3] = "Boolean:";

        this.typesAccepted.add(typesCollapse(MainBlockType.Array));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));

    }

    //ArrayList<Vec3d> List, int Color, double width, boolean valBoolean

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        RenderUtils.RenderLine((ArrayList<Vec3d>) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event),
                (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event),
                dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event),
                dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,3, event));
        super.runCode(dragableBlock, event);
    }


}
