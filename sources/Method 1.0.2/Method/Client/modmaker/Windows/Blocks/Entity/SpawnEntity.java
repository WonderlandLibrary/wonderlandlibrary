package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class SpawnEntity extends Block {

    public SpawnEntity() {
        super("SpawnEntity", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Spawn Entity into World  " + "\n" + "mc.world.spawnEntity(Entity)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().world.spawnEntity((Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }

}
