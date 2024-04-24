package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class Respawn extends Block {

    public Respawn() {
        super("Respawn", MainBlockType.Default, Tabs.Entity, BlockObjects.Name);
        this.description = "Respawn Player   " + "\n" + "minecraft.player.respawnPlayer()";


    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.respawnPlayer();
        super.runCode(dragableBlock, event);
    }

}
