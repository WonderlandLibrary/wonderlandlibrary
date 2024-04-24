package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class loadedEntityList extends Block {

    public loadedEntityList() {
        super("loadedEntityList", MainBlockType.Array, Tabs.Entity, BlockObjects.Name);
        this.description = "Returns minecraft world loaded entity list   " + "\n" + "mc.world.loadedEntityList";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().world.loadedEntityList;
    }

}
