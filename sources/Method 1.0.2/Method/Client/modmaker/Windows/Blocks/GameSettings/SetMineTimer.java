package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class SetMineTimer extends Block {

    public SetMineTimer() {
        super("SetMineTimer", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Set Mine Timer" + "\n" + "mc.timer.elapsedTicks = " + "\n" + "mc.timer.renderPartialTicks = " + "\n" + "mc.timer.elapsedPartialTicks = " + "\n" + "mc.timer.tickLength = ";
        ddOptions.add("elapsedTicks");
        ddOptions.add("renderPartialTicks");
        ddOptions.add("elapsedPartialTicks");
        ddOptions.add("tickLength");

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "elapsedTicks":
                Minecraft.getMinecraft().timer.elapsedTicks = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "renderPartialTicks":
                Minecraft.getMinecraft().timer.renderPartialTicks = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "elapsedPartialTicks":
                Minecraft.getMinecraft().timer.elapsedPartialTicks = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "tickLength":
                Minecraft.getMinecraft().timer.tickLength = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
        }
        super.runCode(dragableBlock, event);
    }

}
