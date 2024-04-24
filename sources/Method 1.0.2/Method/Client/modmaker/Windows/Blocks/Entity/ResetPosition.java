package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;

public class ResetPosition extends Block {

    public ResetPosition() {
        super("ResetPosition", MainBlockType.Default, Tabs.Entity, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Reset position to Bounding Box   " + "\n" + "Entity.resetPositionToBB()";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).resetPositionToBB();
        super.runCode(dragableBlock, event);
    }

}
