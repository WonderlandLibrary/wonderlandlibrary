package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class InPortal extends Block {

    public InPortal() {
        super("InPortal", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.description = "Set in portal flag " + "\n" + "mc.player.inPortal(boolean)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.inPortal = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event);
        super.runCode(dragableBlock, event);
    }

}
