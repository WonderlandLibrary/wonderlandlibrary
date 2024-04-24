package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class getDamagedBlocks extends Block {

    public getDamagedBlocks() {
        super("getDamagedBlocks", MainBlockType.Array, Tabs.Game, BlockObjects.Name);
        this.description = "Get Damaged Blocks";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return new ArrayList<>(Minecraft.getMinecraft().renderGlobal.damagedBlocks.values());
    }

}
