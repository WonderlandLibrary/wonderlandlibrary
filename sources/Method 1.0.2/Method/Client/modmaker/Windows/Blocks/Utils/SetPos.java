package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;

public class SetPos extends Block {

    public SetPos() {
        super("SetPos", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.description = "SetPos of any entity.";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "X";
        this.words[1] = "Y";
        this.words[2] = "Z";
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).setPosition((double) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event), (double) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 2, event), (double) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 3, event));
        super.runCode(dragableBlock, event);
    }

}
