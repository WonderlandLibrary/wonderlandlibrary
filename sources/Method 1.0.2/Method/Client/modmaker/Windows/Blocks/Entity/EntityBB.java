package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;

public class EntityBB extends Block {

    public EntityBB() {
        super("EntityBB", MainBlockType.BoundingBox, Tabs.Entity, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "Entity";
        this.description = "Gets A entitys Bounding Box " + "\n" + "EntityBoundingbox.getBoundingBox()";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).getEntityBoundingBox();
    }

}
