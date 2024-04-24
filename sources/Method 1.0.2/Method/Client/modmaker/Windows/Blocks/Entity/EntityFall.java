package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLiving;

public class EntityFall extends Block {

    public EntityFall() {
        super("EntityFall", MainBlockType.Default, Tabs.Entity, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);

        this.description = "Entity Fall Entity.Fall(distance, damage);";
        this.words[0] = "distance";
        this.words[1] = "damage";


        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        // distance, damage
        ((EntityLiving) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).fall(
                (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event),
                (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 2, event));

    }

}
