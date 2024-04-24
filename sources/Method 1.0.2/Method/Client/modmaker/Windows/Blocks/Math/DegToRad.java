package Method.Client.modmaker.Windows.Blocks.Math;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class DegToRad extends Block {

    public DegToRad() {
        super( "DegToRad", MainBlockType.Numbers, Tabs.Math, BlockObjects.Name,BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Converts degrees to radians";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event) * (float) (Math.PI / 180.0f);
    }


}
