package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.item.EntityBoat;

public class BoatUpdateInputs extends Block {

    public BoatUpdateInputs() {
        super("BoatUpdateInputs", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "leftInputDown";
        this.words[1] = "rightInputDown";
        this.words[2] = "forwardInputDown";
        this.words[3] = "backInputDown";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((EntityBoat) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).updateInputs(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,1, event),
                dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,2, event),
                dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,3, event),
                dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,4, event));
        super.runCode(dragableBlock, event);
    }

}
