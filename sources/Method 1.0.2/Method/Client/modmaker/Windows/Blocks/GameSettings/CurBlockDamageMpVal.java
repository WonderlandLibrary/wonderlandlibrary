package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class CurBlockDamageMpVal extends Block {

    public CurBlockDamageMpVal() {
        super("CurBlockDamageMpVal", MainBlockType.Numbers, Tabs.Game, BlockObjects.Name);
        this.description = "Returns the current block damage MP value";

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return Minecraft.getMinecraft().playerController.curBlockDamageMP = 1.0F;
    }

}
