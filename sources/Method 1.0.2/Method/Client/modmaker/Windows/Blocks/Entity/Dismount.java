package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class Dismount extends Block {

    public Dismount() {
        super("Dismount", MainBlockType.Default, Tabs.Entity, BlockObjects.Name);
        this.description = "Dismounts current ridding entity" + "\n" + "mc.player.dismountRidingEntity()";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.dismountRidingEntity();
        super.runCode(dragableBlock, event);
    }

}
