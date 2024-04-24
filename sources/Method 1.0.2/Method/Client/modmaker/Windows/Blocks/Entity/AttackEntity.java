package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.system.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class AttackEntity extends Block {

    public AttackEntity() {
        super("AttackEntity", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Attacks an entity" + "\n" + "mc.playerController.attackEntity(mc.player, (Entity))";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().player, (Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));

        super.runCode(dragableBlock, event);
    }

}
