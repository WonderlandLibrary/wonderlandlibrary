package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.BlockUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class PlaceEntity extends Block {

    public PlaceEntity() {
        super("PlaceEntity", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "BlockPos";
        this.words[1] = "Rotate";
        this.words[2] = "canSeeBlock";
        this.words[3] = "Hand";
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));
        this.description = "Place Entity like a Ender Crystal At the BlockPos";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        BlockUtils.placeEntity((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event),
                dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 1, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 2, event),
                (EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 3, event));
        super.runCode(dragableBlock, event);
    }

}
