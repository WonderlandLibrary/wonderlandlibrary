package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class TicksExisted extends Block {

    public TicksExisted() {
        super("ticksExisted", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name );
        this.description = "Returns the ticksExisted of the player";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().player.ticksExisted;
    }

}
