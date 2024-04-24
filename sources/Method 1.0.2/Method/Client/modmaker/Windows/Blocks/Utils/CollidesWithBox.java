package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;

public class CollidesWithBox extends Block {

    public CollidesWithBox() {
        super("CollidesWithBox", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BoundingBox));
        this.description = "Returns true if the BoundingBox collides with any block";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().world.collidesWithAnyBlock((AxisAlignedBB) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
    }

}
