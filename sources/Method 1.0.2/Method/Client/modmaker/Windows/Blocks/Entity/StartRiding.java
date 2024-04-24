package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class StartRiding extends Block {

    public StartRiding() {
        super("StartRiding", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        this.description = "Set Player to start riding Entity  " + "\n" + "mc.player.startRiding( entity, force)";
        this.words[0] = "Forced";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.startRiding((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,1, event));
        super.runCode(dragableBlock, event);
    }

}
