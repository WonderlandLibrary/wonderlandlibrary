package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class ShowDebug extends Block {

    public ShowDebug() {
        super("ShowDebug", MainBlockType.Boolean, Tabs.Game, BlockObjects.Name);

        this.description = "ShowDebug";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
      return   Minecraft.getMinecraft().gameSettings.showDebugInfo;
    }

}
