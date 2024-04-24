package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class loadedTileEntityList extends Block {

    public loadedTileEntityList() {
        super("loadedTileEntityList", MainBlockType.Array, Tabs.Entity, BlockObjects.Name);
        this.description = "Returns minecraft world loaded Tile entity list   " + "\n" + "mc.world.loadedTileEntityList";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().world.loadedTileEntityList;
    }

}
