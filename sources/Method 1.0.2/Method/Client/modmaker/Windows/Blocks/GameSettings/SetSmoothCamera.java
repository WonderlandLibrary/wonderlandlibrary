package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class SetSmoothCamera extends Block {

    public SetSmoothCamera() {
        super("SetSmoothCamera", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));

        this.description = "Set Smooth Camera + \n" + "mc.gameSettings.smoothCamera = ";


    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().gameSettings.smoothCamera = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);

        super.runCode(dragableBlock, event);
    }

}
