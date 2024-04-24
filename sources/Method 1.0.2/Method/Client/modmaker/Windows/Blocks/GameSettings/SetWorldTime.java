package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class SetWorldTime extends Block {

    public SetWorldTime() {
        super("SetWorldTime", MainBlockType.Default, Tabs.Game, BlockObjects.Name,  BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Change World time, Client Side" + "\n" + "mc.world.setWorldTime(long)";


    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().world.setWorldTime((long) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }

}
