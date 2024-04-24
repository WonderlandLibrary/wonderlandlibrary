package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.util.math.Vec3d;

public class Vec3dOffset extends Block {

    public Vec3dOffset() {
        super("Vec3dOffset", MainBlockType.Vec3d, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Expands Vec3d ";
        this.words[0] = " X ";
        this.words[1] = " Y ";
        this.words[2] = " Z ";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        Vec3d pos = (Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
        double posx = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event);
        double posy = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 2, event);
        double posz = dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 3, event);

        return pos.add(posx, posy, posz);
    }

}
