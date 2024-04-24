package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class YourSelf extends Block {

    public YourSelf() {
        super("YourSelf", MainBlockType.Entity, Tabs.Utils, BlockObjects.Name );
        this.description = "Returns the player" + "\n" + "mc.player";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().player;
    }

}
