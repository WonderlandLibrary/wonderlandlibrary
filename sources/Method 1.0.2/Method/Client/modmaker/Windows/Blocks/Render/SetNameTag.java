package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.Entity;

public class SetNameTag extends Block {

    public SetNameTag() {
        super("SetNameTag", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.words[0]="Name";
        this.description = "Sets the name tag of the entity";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).setCustomNameTag((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));
        super.runCode(dragableBlock, event);
    }
}
