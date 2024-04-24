package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.passive.EntityPig;

public class isSaddled extends Block {

    public isSaddled() {
        super("isSaddled", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "Pig Only!";
        this.description = "Returns true if EntityPig has been Saddled   " + "\n" + "EntityPig.getSaddled()";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((EntityPig) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getSaddled();
    }

}
