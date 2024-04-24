package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Utils;
import net.minecraft.util.math.Vec3d;

public class NeededRotation extends Block {

    public NeededRotation() {
        super("NeededRotation", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.words[0] = "Vec3d";
        this.words[1] = "Yaw";
        this.words[2] = "Pitch";
        ddOptions.add("X");
        ddOptions.add("Y");
        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Returns the needed rotation to get to the given vector";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X":
                return Utils.getNeededRotations((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event),(float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event),(float)dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event))[0];
            case "Y":
                return Utils.getNeededRotations((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event),(float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event),(float)dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event))[1];
        }
        return 0;
    }

}
