package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;

public class SpawnLightning extends Block {

    public SpawnLightning() {
        super("SpawnLightning", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.description = "Spawns a lightning bolt at the given position";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().world.spawnEntity(new EntityLightningBolt(Minecraft.getMinecraft().world,

                ((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).x, ((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).y, ((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).z, true));
        super.runCode(dragableBlock, event);
    }

}
