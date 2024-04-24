package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class TickRate extends Block {

    public TickRate() {
        super("TickRate", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Set minecraft tick length" + "\n" + "Note Value multiplied by 50, eg 1 = normal "+ "\n" + "mc.timer.tickLength = 50 * (float);";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().timer.tickLength = (float) (50 * dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }

}
