package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;

public class SwingArm extends Block {

    public SwingArm() {
        super("SwingArm", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.Words,
                BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "Hand: ";
        this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));
        this.description = "Swing arm of Entity  " + "\n" + "EntityLivingBase.swingArm(EnumHand)";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).swingArm((EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event));
        super.runCode(dragableBlock, event);
    }
}
