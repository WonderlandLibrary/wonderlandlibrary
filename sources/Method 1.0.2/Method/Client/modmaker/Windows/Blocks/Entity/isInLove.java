package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.passive.EntityAnimal;

public class isInLove extends Block {

    public isInLove() {
        super("isInLove", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "Animal Only!";
        this.description = "Returns true if Animal is in love   " + "\n" + "EntityAnimal.isInLove()";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((EntityAnimal) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isInLove();
    }

}
