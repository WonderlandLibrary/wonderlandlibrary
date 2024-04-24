package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.passive.EntitySheep;

public class IsSheared extends Block {

    public IsSheared() {
        super("IsSheared", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "Sheep Only!";
        this.description = "Returns true if EntitySheep has been getSheared   " + "\n" + "EntitySheep.getSheared()";

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((EntitySheep) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getSheared();
    }

}
