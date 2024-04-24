package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class EntityBBOffset extends Block {

    public EntityBBOffset() {
        super("EntityBBOffset", MainBlockType.Default, Tabs.Entity, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "Entity";
        this.words[1] = "BBOffset";
        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.description = "Expands A entitys Bounding Box " + "\n" + "EntityBoundingbox.getBoundingBox().offset(Vec3d)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).getEntityBoundingBox().offset((Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
        super.runCode(dragableBlock, event);
    }

}
