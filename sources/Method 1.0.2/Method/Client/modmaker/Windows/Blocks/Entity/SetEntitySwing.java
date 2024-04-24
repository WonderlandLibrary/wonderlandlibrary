package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.entity.EntityLivingBase;

public class SetEntitySwing extends Block {

    public SetEntitySwing() {
        super("SetEntitySwing", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter,
                BlockObjects.Words,BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        ddOptions.add("swingProgress");
        ddOptions.add("swingProgressInt");
        this.words[0] = "swingProgress";
        this.description = "Set Entity Swing Progress  " + "\n" + "EntityLiving.swingProgress " + "\n" + "EntityLiving.swingProgressInt";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "swingProgress":
                ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).swingProgress = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "swingProgressInt":
                ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).swingProgressInt = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
        }
        super.runCode(dragableBlock, event);
    }
}
