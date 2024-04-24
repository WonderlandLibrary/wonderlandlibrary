package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;

public class EntityIsTamed extends Block {

    public EntityIsTamed() {
        super("EntityIsTamed", MainBlockType.Boolean, Tabs.Entity, BlockObjects.Name,BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Returns true if Entity is Tamed" + "\n" + "(EntityTameable).isTamed()";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        if(dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof EntityTameable)
            return ((EntityTameable) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isTamed();
        else if(dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event) instanceof AbstractHorse)
            return ((AbstractHorse) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).isTame();
        return false;
    }

}
