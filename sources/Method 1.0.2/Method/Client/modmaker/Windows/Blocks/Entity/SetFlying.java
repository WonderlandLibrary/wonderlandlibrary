package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class SetFlying extends Block {

    public SetFlying() {
        super("SetFlying", MainBlockType.Default, Tabs.Entity, BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.words[0] = "Set Flying ";
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.description = "Set player is Flying    " + "\n" + "mc.player.capabilities.isFlying ";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.capabilities.isFlying = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event);
        super.runCode(dragableBlock, event);
    }

}
