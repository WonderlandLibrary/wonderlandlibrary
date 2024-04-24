package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class ObjectMouseOver extends Block {

    public ObjectMouseOver() {
        super("ObjectMouseOver", MainBlockType.RayTraceResult, Tabs.Game, BlockObjects.Name);
        this.description = "Returns what object (RaytraceResult) mouse is over ingame" + "\n" + "mc.objectMouseOver";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().objectMouseOver;
    }

}
