package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.proxy.Overrides.EntityRenderMixin;

public class HurtCam extends Block {

    public HurtCam() {
        super("HurtCam", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.description = "Custom Code, if true Show Hurt cam " + "\n" + "True = Show Hurt Cam" + "\n" + "False = Don't Show Hurt Cam";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        EntityRenderMixin.setCamswitch(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }

}
