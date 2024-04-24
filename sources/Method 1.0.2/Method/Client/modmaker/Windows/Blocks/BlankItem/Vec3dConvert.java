package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.Vec3d;

public class Vec3dConvert extends Block {

    public Vec3dConvert() {
        super("Vec3dConvert", MainBlockType.Numbers, Tabs.NewItem, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.ddOptions.add("X");
        this.ddOptions.add("Y");
        this.ddOptions.add("Z");

        this.words[0] = "Vec3d:";
        this.words[1] = "Num:";
        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X":
                return ((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).x;
            case "Y":
                return ((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).y;
            case "Z":
                return ((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).z;

        }
        return 0;
    }


}
