package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class RenderEntityStatic extends Block {

    public RenderEntityStatic() {
        super("renderEntityStatic", MainBlockType.Default, Tabs.Render,BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.description = "Renders an entity statically";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().getRenderManager().renderEntityStatic((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event),Minecraft.getMinecraft().getRenderPartialTicks(),false);
        super.runCode(dragableBlock, event);
    }


}
