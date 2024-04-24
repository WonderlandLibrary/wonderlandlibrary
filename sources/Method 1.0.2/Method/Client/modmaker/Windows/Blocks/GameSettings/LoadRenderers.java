package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class LoadRenderers extends Block {

    public LoadRenderers() {
        super("LoadRenderers", MainBlockType.Default, Tabs.Game, BlockObjects.Name);
        this.description = "Load Renderers, for texture stuff" + "\n" + "mc.renderGlobal.loadRenderers()";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
        super.runCode(dragableBlock, event);
    }

}
