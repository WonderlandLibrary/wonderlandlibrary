package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;

public class Extinguish extends Block {

    public Extinguish() {
        super("Extinguish", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Tries to Extinguish Entity" + "\n" + "(Entity).extinguish()";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((EntityLivingBase)dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).extinguish();
        super.runCode(dragableBlock, event);
    }

}
