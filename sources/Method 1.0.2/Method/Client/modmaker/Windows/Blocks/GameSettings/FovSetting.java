package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class FovSetting extends Block {

    public FovSetting() {
        super("FovSetting", MainBlockType.Default, Tabs.Game, BlockObjects.Name,BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Sets ingame Fov setting" + "\n" + "mc.gameSettings.fovSetting(float)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().gameSettings.fovSetting = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
        super.runCode(dragableBlock, event);
    }

}
