package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class Jump extends Block {

    public Jump() {
        super("Jump", MainBlockType.Default, Tabs.Game, BlockObjects.Name);
        this.description = "Jump!" + "\n" + "mc.player.jump()";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.jump();
        super.runCode(dragableBlock, event);
    }

}
