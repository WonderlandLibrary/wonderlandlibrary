package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class DecimalTrim extends Block {

    public DecimalTrim() {
        super( "DecimalTrim", MainBlockType.Numbers, Tabs.Math, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = " Trim to ";
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Trims a number to a certain decimal place";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        double num = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
        double trim = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
        return (double) Math.round(num * Math.pow(10, trim)) / Math.pow(10, trim);
    }


}
