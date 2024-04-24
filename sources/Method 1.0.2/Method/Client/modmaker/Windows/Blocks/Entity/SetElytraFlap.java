package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.entity.AbstractClientPlayer;

public class SetElytraFlap extends Block {

    public SetElytraFlap() {
        super("SetElytraFlap", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        ddOptions.add("rotateElytraX");
        ddOptions.add("rotateElytraY");
        ddOptions.add("rotateElytraZ");
        this.words[0] = "Angle";
        this.description = "Set Elytra  angle rotateElytraX, rotateElytraY, rotateElytraZ";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "rotateElytraX":
                ((AbstractClientPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).rotateElytraX = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
                break;
            case "rotateElytraY":
                ((AbstractClientPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).rotateElytraY = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
                break;
            case "rotateElytraZ":
                ((AbstractClientPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event)).rotateElytraZ = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event);
                break;

        }
        super.runCode(dragableBlock, event);
    }
}
