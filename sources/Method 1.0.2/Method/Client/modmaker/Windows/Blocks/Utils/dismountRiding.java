package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class dismountRiding extends Block {

    public dismountRiding() {
        super("dismountRiding", MainBlockType.Default, Tabs.Utils, BlockObjects.Name);
        this.description = "Dismounts the player from the riding entity.";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.dismountRidingEntity();
        super.runCode(dragableBlock, event);
    }

}
