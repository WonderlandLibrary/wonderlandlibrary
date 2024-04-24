package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class OnCriticalHit extends Block {

    public OnCriticalHit() {
        super("OnCriticalHit", MainBlockType.Default, Tabs.Utils, BlockObjects.Name,BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "mc.player.onCriticalHit(Entity)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.onCriticalHit((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));

     super.runCode(dragableBlock, event);
    }

}
