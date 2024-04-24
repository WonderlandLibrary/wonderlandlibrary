package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.ColorUtils;

public class NewColor extends Block {

    public NewColor() {
        super("NewColor", MainBlockType.Numbers, Tabs.Render, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);


        this.words[0] = "Hue:";
        this.words[1] = "Saturation:";
        this.words[2] = "Brightness:";
        this.words[3] = "Alpha:";
        this.words[4] = "Rainbow:";

        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));

        this.description = "Create custom colors, or use rainbow";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        if (dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,4, event))
            return ColorUtils.rainbow(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event), dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event));
        else
            return new java.awt.Color((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event), (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event)).getRGB();

    }


}
